package com.szxb.zibo.moudle.function.unionpay.dispose;

import android.os.SystemClock;
import android.util.Log;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.module.SignIn;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.quickstart.SingletonFactory;
import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.lib.Util.ThreadUtils;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.db.dao.UnionPayEntityDao;
import com.szxb.zibo.db.dao.XdRecordDao;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.moudle.function.unionpay.UnionPay;
import com.szxb.zibo.moudle.function.unionpay.UnionUtil;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.moudle.function.unionpay.entity.UnionPayEntity;
import com.szxb.zibo.moudle.function.unionpay.http.BaseByteRequest;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static com.szxb.zibo.moudle.function.unionpay.unionutil.HexUtil.yuan2Fen;


/**
 * 作者：Tangren on 2018-09-08
 * 包名：com.szxb.unionpay.dispose
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class SyncSSLRequest {

    /**
     * @param type     类型
     * @param sendData 源数据
     * @return 银联响应
     */
    synchronized public BankResponse request(int type, byte[] sendData) throws Exception {
        BankResponse response = new BankResponse();
        response.setType(type);
        String url = BusllPosManage.getPosManager().getUnionPayUrl();
        BaseByteRequest baseSyncRequest = new BaseByteRequest(url, RequestMethod.POST);
        InputStream stream = new ByteArrayInputStream(sendData);
        baseSyncRequest.setDefineRequestBody(stream, "x-ISO-TPDU/x-auth");
        Log.i("银联交易", "开始1" + System.currentTimeMillis());
        Response<byte[]> execute = SyncRequestExecutor.INSTANCE.execute(baseSyncRequest);
        Log.i("银联交易", "返回1" + System.currentTimeMillis());
        if (execute.isSucceed()) {
            Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
            Iso8583Message message0810 = factory.parse(execute.get());
            Log.d("SyncSSLRequest", "(request.java:58)type=" + (type == UnionUtil.PAY_TYPE_BANK_IC ? "银联卡>>\n" : "银联二维码>>\n") + message0810.toFormatString());
            doDispose(type, response, message0810);
        } else {
//            response.setResCode(BankCardParse.ERROR_NET);
            SoundPoolUtil.play(VoiceConfig.qingchongshua);
            BusToast.showToast("网络超时", false);
//            ThreadUtils.getInstance().createSch("bankRefund").schedule(new Runnable() {
//                @Override
//                public void run() {
////                    new BankRefund().run();
//                }
//            }, 3, TimeUnit.SECONDS);
        }
        return response;
    }

    /**
     * 银联
     *
     * @param type        类型
     * @param icResponse  .
     * @param message0810 返回的报文
     */
    private void doDispose(int type, BankResponse icResponse, Iso8583Message message0810) throws Exception {
        String resCode = message0810.getValue(39).getValue();
        String tradeSeq = message0810.getValue(11).getValue();
        String batchNum = message0810.getValue(60).getValue().substring(2, 8);
        String uniqueFlag = tradeSeq + batchNum;
        XdRecordDao dao = DBCore.getDaoSession().getXdRecordDao();
        XdRecord unique = dao.queryBuilder()
                .where(XdRecordDao.Properties.Flag.eq(uniqueFlag))
                .limit(1).build().unique();
        unique.setRes4(batchNum);
        if (unique != null) {
            switch (resCode) {
                case "00":
                case "A2":
                case "A4":
                case "A5":
                case "A6":
                    //支付成功
                    String amount = message0810.getValue(4).getValue();
                    icResponse.setResCode(BankCardParse.SUCCESS);
                    if (type == UnionUtil.PAY_TYPE_BANK_IC) {
                        //银联卡返回2域
                        icResponse.setMainCardNo(message0810.getValue(2).getValue());
                    }
                    unique.setRealFree(amount);
                    SoundPoolUtil.play(VoiceConfig.shuamachenggong);
//                    SoundPoolUtil.play(VoiceConfig.yinhangshanfuka);
                    icResponse.setMsg("扣款成功\n扣款金额" + yuan2Fen(amount) + "元");
                    icResponse.setLastTime(SystemClock.elapsedRealtime());
                    break;
                case "A0":
                    //重新签到
                    BusToast.showToast(
                            (type == UnionUtil.PAY_TYPE_BANK_IC ? "刷卡" : "扫码") + "失败\n正在重新签到", false);
                    BusllPosManage.getPosManager().setTradeSeq();
                    Iso8583Message message = SignIn.getInstance().message(BusllPosManage.getPosManager().getTradeSeq());
                    UnionPay.getInstance().exeSSL(UnionConfig.SIGN, message.getBytes(), true);
                    break;
                case "94"://重复交易（流水号重复）
                    icResponse.setMsg("流水号重复\n请重刷");
                    break;
                case "51"://余额不足
                    icResponse.setMsg("余额不足");
                    SoundPoolUtil.play(VoiceConfig.yuebuzu);
                    break;
                case "54"://卡过期
                    icResponse.setMsg("卡过期");
                    SoundPoolUtil.play(VoiceConfig.ic_invalid);
                    break;
                default:
                    SoundPoolUtil.play(VoiceConfig.wuxiaoma);
                    icResponse.setMsg((type == UnionUtil.PAY_TYPE_BANK_IC ? "刷卡" : "扫码") + "失败[" + UnionUtil.unionPayStatus(resCode) + "]");
                    break;
            }
            unique.setUnionPayStatus(resCode);
            BusToast.showToast(icResponse.getMsg(), true);
            if (unique.getNewExtraDate().endsWith(FileUtils.bytesToHexString("DD".getBytes()))) {
                MiLog.i("银联", "二维码 记录 附加数据：" + unique.getNewExtraDate());
                String rescode = FileUtils.bytesToHexString(resCode.getBytes());
                String ex = unique.getNewExtraDate().substring(0, unique.getNewExtraDate().length() - 4);
//                                    MiLog.i("银联记录 附加数据",FileUtils.stringToAsc(rescode));
                unique.setNewExtraDate(ex + FileUtils.formatHexStringToByteString(2, rescode));
                MiLog.i("银联", "二维码 记录 附加数据:" + unique.getExtraDate() + "   " + unique.getNewExtraDate());
            }
            //记录异步修改
            DBManagerZB.saveRecord(unique);
            try {
                RecordUpload.uploadRecord(unique);
            } catch (Exception e) {
                Log.i("银联", "上送数据报错 错误：" + e.getMessage());
            }
        }
    }

}
