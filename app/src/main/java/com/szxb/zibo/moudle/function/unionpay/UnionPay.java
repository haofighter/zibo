package com.szxb.zibo.moudle.function.unionpay;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.module.SignIn;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.quickstart.SingletonFactory;
import com.szxb.java8583.quickstart.special.SpecialField62;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.db.dao.XdRecordDao;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.moudle.function.unionpay.http.CallServer;
import com.szxb.zibo.moudle.function.unionpay.http.HttpListener;
import com.szxb.zibo.moudle.function.unionpay.unionutil.SSLContextUtil;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.szxb.zibo.moudle.function.unionpay.unionutil.HexUtil.yuan2Fen;
import static com.szxb.zibo.moudle.function.unionpay.unionutil.ParseUtil.parseMackey;


/**
 * 作者：Tangren on 2018-07-06
 * 包名：com.szxb.unionpay
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class UnionPay {

    private volatile static UnionPay instance = null;

    private UnionPay() {
    }

    public static UnionPay getInstance() {
        if (instance == null) {
            synchronized (UnionPay.class) {
                if (instance == null) {
                    instance = new UnionPay();
                }
            }
        }
        return instance;
    }


    public void exeSSL(int what, byte[] sendData, final boolean isTip) {
        if (BusApp.getPosManager().getLineType().endsWith("P")) {
            BusToast.showToast("暂不支持此方式交易", false);
            SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
            return;
        }


        String url = BusllPosManage.getPosManager().getUnionPayUrl();
        url.replace("http", "https");
        url = "https://140.207.168.62:30000/mjc/webtrans/VPB_lb";
        final Request<byte[]> request = NoHttp.createByteArrayRequest(url, RequestMethod.POST);
        request.setHeader("User-Agent", "Donjin Http 0.1");
        request.setHeader("Cache-Control", "no-cache");
        request.setHeader("Accept", "*/*");
        request.setHeader("Accept-Encoding", "*");
        request.setHeader("Connection", "close");
        request.setHeader("HOST", "120.204.69.139:30000");
        request.setHeader("HOST", "140.207.168.62:30000");

        if (what == UnionConfig.PAY) {
            request.setConnectTimeout(3000);
            request.setReadTimeout(3000);
        }
        Log.i("银联交易", "开始" + System.currentTimeMillis());
        try {
            InputStream stream = new ByteArrayInputStream(sendData);
            request.setDefineRequestBody(stream, "x-ISO-TPDU/x-auth");
            SSLContext sslContext = SSLContextUtil.getSSLContext(BusApp.getInstance());
            request.setHostnameVerifier(SSLContextUtil.getHostnameVerifier());
            if (sslContext != null) {
                SSLSocketFactory socketFactory = sslContext.getSocketFactory();
                request.setSSLSocketFactory(socketFactory);

                CallServer.getHttpclient().add(what, request, new HttpListener<byte[]>() {
                    @Override
                    public void success(int what, Response<byte[]> response) {
                        try {
                            Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
                            factory.setSpecialFieldHandle(62, new SpecialField62());
                            Iso8583Message message0810 = factory.parse(response.get());

                            if (what == UnionConfig.SIGN) {//签到
                                if (message0810.getValue(39).getValue().equals("00")) {
                                    String batchNum = message0810.getValue(60).getValue().substring(2, 8);
                                    BusllPosManage.getPosManager().setBatchNum(batchNum);
                                    parseMackey(message0810.getValue(62).getValue(), isTip);
                                    BusApp.unionIsSign = true;
                                } else {
                                    if (isTip) {
                                        BusToast.showToast("银联签到失败[" + message0810.getValue(39).getValue() + "]", false);
                                    }
                                }
                            } else if (what == UnionConfig.PAY) {//消费
                                int pay_fee = Integer.parseInt(message0810.getValue(4).getValue());
                                String resCode = message0810.getValue(39).getValue();
                                String tradeSeq = message0810.getValue(11).getValue();
                                String batchNum = message0810.getValue(60).getValue().substring(2, 8);
                                String uniqueFlag = tradeSeq + batchNum;
                                XdRecordDao dao = DBCore.getDaoSession().getXdRecordDao();
                                XdRecord unique = dao.queryBuilder()
                                        .where(XdRecordDao.Properties.Flag.eq(uniqueFlag))
                                        .limit(1).build().unique();
                                if (unique != null) {

                                    switch (resCode) {
                                        case "00":
                                        case "A2":
                                        case "A4":
                                        case "A5":
                                        case "A6":
                                            SoundPoolUtil.play(VoiceConfig.yinhangshanfuka);
                                            String amount = message0810.getValue(4).getValue();
                                            BusToast.showToast("扣款成功\n扣款金额" + yuan2Fen(amount) + "元", true);
                                            Log.i("数据库", "银联消费完成2");
                                            break;
                                        case "A0":
                                            //重新签到
                                            BusToast.showToast("刷卡失败,正在签到,稍后重试", false);
                                            BusllPosManage.getPosManager().setTradeSeq();
                                            Iso8583Message message = SignIn.getInstance().message(BusllPosManage.getPosManager().getTradeSeq());
                                            UnionPay.getInstance().exeSSL(UnionConfig.SIGN, message.getBytes(), true);
                                            break;
                                        case "94":
                                            //重复交易（流水号重复）
                                            BusToast.showToast("刷卡失败,流水重复,请重试", false);
                                            SoundPoolUtil.play(VoiceConfig.qingchongshua);
                                            Log.d("UnionPay", "(success.java:104)重复支付");
                                            break;
                                        case "51":
                                            //余额不足
                                            SoundPoolUtil.play(VoiceConfig.yuebuzu);
                                            BusToast.showToast("刷卡失败,余额不足", false);
                                            Log.d("UnionPay", "(success.java:130)余额不足");
                                            break;
                                        case "54":
                                            //卡过期
                                            SoundPoolUtil.play(VoiceConfig.ic_invalid);
                                            BusToast.showToast("卡过期", false);
                                            Log.d("UnionPay", "(success.java:136)卡过期");
                                            break;
                                        default:
                                            BusToast.showToast("刷卡失败[" + UnionUtil.unionPayStatus(resCode) + "]", false);
                                            SoundPoolUtil.play(VoiceConfig.qingchongshua);
                                            break;
                                    }
                                }
                                if (unique.getNewExtraDate().endsWith(FileUtils.bytesToHexString("DD".getBytes()))) {
                                    MiLog.i("银联记录 附加数据", unique.getNewExtraDate());
                                    String rescode = FileUtils.bytesToHexString(resCode.getBytes());
                                    String ex = unique.getNewExtraDate().substring(0, unique.getNewExtraDate().length() - 4);
//                                    MiLog.i("银联记录 附加数据",FileUtils.stringToAsc(rescode));
                                    unique.setNewExtraDate(ex + FileUtils.formatHexStringToByteString(2, rescode));
                                    MiLog.i("银联记录 附加数据", unique.getExtraDate() + "   " + unique.getNewExtraDate());
                                }
                                //记录异步修改
                                DBManagerZB.saveRecord(unique);
                                try {
                                    RecordUpload.uploadRecord(unique);
                                } catch (Exception e) {
                                    Log.i("上送数据报错", "错误：" + e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (isTip) {
                                if (what == UnionConfig.SIGN) {
                                    BusToast.showToast("银联签到失败", false);
                                } else {
                                    BusToast.showToast("银联刷卡失败", false);
                                }
                            } else {
                                SoundPoolUtil.play(VoiceConfig.qingchongshua);
                            }
                        }
                    }

                    @Override
                    public void fail(int what, String e) {
                        if (what == UnionConfig.SIGN) {//签到
                            if (isTip) {
                                BusToast.showToast("网络异常", false);
                            }
                        } else {
                            SoundPoolUtil.play(VoiceConfig.qingchongshua);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public byte[] exeSyncSSL(byte[] sendData) {
        String url = BusllPosManage.getPosManager().getUnionPayUrl();
        Request<byte[]> request = NoHttp.createByteArrayRequest(url, RequestMethod.POST);
        request.setHeader("User-Agent", "Donjin Http 0.1");
        request.setHeader("Cache-Control", "no-cache");
        request.setHeader("Accept", "*/*");
        request.setHeader("Accept-Encoding", "*");
        request.setHeader("Connection", "close");
        request.setHeader("HOST", "120.204.69.139:30000");

        InputStream stream = new ByteArrayInputStream(sendData);
        request.setDefineRequestBody(stream, "x-ISO-TPDU/x-auth");
        SSLContext sslContext = SSLContextUtil.getSSLContext(BusApp.getInstance().getApplicationContext());
        request.setHostnameVerifier(SSLContextUtil.getHostnameVerifier());

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        request.setSSLSocketFactory(socketFactory);
        Response<byte[]> execute = SyncRequestExecutor.INSTANCE.execute(request);
        return execute.get();
    }


}
