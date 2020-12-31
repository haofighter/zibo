package com.szxb.zibo.moudle.function.scan.freecode;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lilei.tool.tool.IToolInterface;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.SignIn;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.jni.JTQR;
import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.lib.base.Rx.Rx;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.cmd.DoCmd;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.config.zibo.PublicKey;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.config.zibo.line.ZBLineInfo;
import com.szxb.zibo.db.dao.ZBLineInfoDao;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.manager.PosManager;
import com.szxb.zibo.moudle.function.unionpay.UnionPay;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.moudle.init.InitActiivty;
import com.szxb.zibo.moudle.zibo.SelectLineActivity;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.apkmanage.AppUtil;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.util.sp.CommonSharedPreferences;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.szxb.lib.Util.FileUtils.fen2Yuan;
import static com.szxb.zibo.config.zibo.InitConfigZB.INFO_UP;
import static java.lang.System.arraycopy;

public class FreeCodeManage {
    private FreeCodeManage() {
    }

    public void posScan(String result) {
        try {
            FreeScanEntity freeScanEntity = new Gson().fromJson(result, FreeScanEntity.class);

            if (freeScanEntity.getBlueRing().getType().equals("Driver")) {//司机
                BusApp.getPosManager().setDriverNo(freeScanEntity.getBlueRing().getValue());
                BusToast.showToast("设置成功\n司机：" + freeScanEntity.getBlueRing().getValue(), true);
                SoundPoolUtil.play(VoiceConfig.sijika);
            } else if (freeScanEntity.getBlueRing().getType().equals("Price")) {//票价
                if (TextUtils.equals(BusApp.getPosManager().getLineNo(), "")) {
                    BusToast.showToast("请先设置线路", false);
                    return;
                }
                BusApp.getPosManager().setBasePrice(Integer.parseInt(freeScanEntity.getBlueRing().getValue()));
                BusToast.showToast("设置成功\n票价：" + freeScanEntity.getBlueRing().getValue(), true);
                SoundPoolUtil.play(VoiceConfig.shuamachenggong);
            } else if (freeScanEntity.getBlueRing().getType().equals("Conductor")) {//售票员
//                appRunConfigEntity.setConductor(freeScanEntity.getBlueRing().getValue());
                BusToast.showToast("设置成功\n售票员：" + freeScanEntity.getBlueRing().getValue(), true);
                SoundPoolUtil.play(VoiceConfig.shuamachenggong);
            } else if (freeScanEntity.getBlueRing().getType().equals("Carno")) {//车辆号
                if (freeScanEntity.getBlueRing().getValue().length() > 8) {
                    BusToast.showToast("请检查车号", true);
                    SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
                    return;
                }
                BusApp.getPosManager().setBusNo(freeScanEntity.getBlueRing().getValue());
                BusToast.showToast("设置成功\n车辆号：" + freeScanEntity.getBlueRing().getValue(), true);
                SoundPoolUtil.play(VoiceConfig.shuamachenggong);
            } else if (freeScanEntity.getBlueRing().getType().equals("Line")) {//线路设置
                String line = freeScanEntity.getBlueRing().getValue();
                if (line.equals("400255_1")) {
                    PraseLine.praseLine(FileUtils.readAssetsFileTobyte("20200909000001.far", BusApp.getInstance().getApplication()));
                } else if (line.equals("400251_1")) {
                    PraseLine.praseLine(FileUtils.readAssetsFileTobyte("20191228195940.far", BusApp.getInstance().getApplication()));
                } else {
                    BusToast.showToast("正在获取线路：" + freeScanEntity.getBlueRing().getValue(), true);
                    ZBLineInfo zbLineInfo = DBCore.getDaoSession().getZBLineInfoDao().queryBuilder().where(ZBLineInfoDao.Properties.Routeno.eq(freeScanEntity.getBlueRing().getValue())).limit(1).unique();
                    if (zbLineInfo == null) {
                        BusToast.showToast("暂无此线路", true);
                        return;
                    }
                    BusApp.getPosManager().setLineName(zbLineInfo.getRoutename());
                    BusApp.getPosManager().setLineNo(zbLineInfo.getRouteno());
                    BusApp.getPosManager().setBasePrice(0);
                    MiLog.i("流程", "posmanager 初始化  线路版本  票价版本" );
                    BusApp.getPosManager().setFarver("00000000000000");
                    BusApp.getPosManager().setLinver("00000000000000");
                    BusToast.showToast("下载线路", true);
                    Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Response<JSONObject> response = InitConfigZB.sendInfoToServer(INFO_UP);
                            } catch (Exception e) {
                            }
                        }
                    }, 0, TimeUnit.SECONDS);
                }

//                InitConfig.setLine(freeScanEntity.getBlueRing().getValue(), true);
            } else if (freeScanEntity.getBlueRing().getType().equals("Time")) {//设置时间
                String time = freeScanEntity.getBlueRing().getValue();
                Date date = DateUtil.String2Date(time);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                int sec = calendar.get(Calendar.SECOND);
                try {
                    IToolInterface iToolInterface = BusApp.getInstance().getmService();
                    if (iToolInterface != null) {
                        iToolInterface.setDateTime(year, month, day, hour, min, sec);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
                    BusToast.showToast("时间设置异常", true);
                    return;
                }
                SoundPoolUtil.play(VoiceConfig.shuamachenggong);
                BusToast.showToast("时间设置成功", true);
            } else if (freeScanEntity.getBlueRing().getType().equals("City")) {//设置城市
                BusApp.getPosManager().setCityCode(freeScanEntity.getBlueRing().getValue());
                BusToast.showToast("设置当前城市代码：\n" + freeScanEntity.getBlueRing().getValue(), true);
                SoundPoolUtil.play(VoiceConfig.shuamachenggong);

            } else if (freeScanEntity.getBlueRing().getType().equals("Address")) {//设置键盘通讯地址
                if (freeScanEntity.getBlueRing().getValue().length() != 4) {
                    BusToast.showToast("键盘通讯地址错误", false);
                    SoundPoolUtil.play(VoiceConfig.cuowu);
                    return;
                }
                BusApp.getPosManager().setKeybroadAddress(freeScanEntity.getBlueRing().getValue());
                BusToast.showToast("键盘通讯地址：\n" + freeScanEntity.getBlueRing().getValue(), true);
                SoundPoolUtil.play(VoiceConfig.shuamachenggong);
            } else if (freeScanEntity.getBlueRing().getType().equals("MyAddress")) {//设置本机
                if (freeScanEntity.getBlueRing().getValue().length() != 4) {
                    BusToast.showToast("键盘通讯地址错误", false);
                    SoundPoolUtil.play(VoiceConfig.cuowu);
                    return;
                }
                BusApp.getPosManager().setMyselfkeybroadAddressCache(freeScanEntity.getBlueRing().getValue());
                Rx.getInstance().sendMessage("keyboardAddress", freeScanEntity.getBlueRing().getValue());
            } else if (freeScanEntity.getBlueRing().getType().equals("Union")) {//设置银联参数
                if (BusApp.getPosManager().getLineType().equals("P")) {
                    BusToast.showToast("该线路暂不支持此方式乘车", true);
                    return;
                }

                try {
                    String union = freeScanEntity.getBlueRing().getValue();
                    String[] unionStr = union.split("-");
                    BusllPosManage.getPosManager().setMachId(unionStr[0]);
                    BusllPosManage.getPosManager().setKey(unionStr[1]);
                    BusllPosManage.getPosManager().setPosSn(unionStr[2]);

                    Log.d("Util", "(updateUnionParam.java:334)银联参数设置成功>>>马上签到");
                    BusllPosManage.getPosManager().setTradeSeq();
                    Iso8583Message message = SignIn.getInstance().message(BusllPosManage.getPosManager().getTradeSeq());
                    Log.d("LoopCard", "(run.java:242)" + message.toFormatString());

                    CommonSharedPreferences.put("union", union);
                    UnionPay.getInstance().exeSSL(UnionConfig.SIGN, message.getBytes(), true);
                } catch (Exception e) {
                    BusToast.showToast("二维码有误", false);
                    SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
                }
            }
        } catch (Exception e) {
            Log.i("错误", "自有码设置错误");
        }
    }

    private static class FreeCodeManageHelp {
        private static FreeCodeManage fcm = new FreeCodeManage();
    }

    public static FreeCodeManage getInstance() {
        return FreeCodeManageHelp.fcm;
    }

    private void packegePay(String extraDate, String cardNo, QRData qrData) {
        XdRecord xdRecord = new XdRecord();
        xdRecord.setQrCode(extraDate);
        xdRecord.setExtraDate(extraDate);
        if (DBManagerZB.checkRepeatScan(extraDate) == null) {
            int price = 0;

            if (qrData.getIssuer().equals("03664530")) {
                price = PraseLine.getNormalPayPrice("66", "00");//金额
                xdRecord.setMainCardType("66");
                xdRecord.setChildCardType("00");
                xdRecord.setTradeType("14");
            } else {
                price = PraseLine.getNormalPayPrice("33", "00");//金额
                xdRecord.setTradeType("0a");
                xdRecord.setMainCardType("33");
                xdRecord.setChildCardType("00");
            }

            xdRecord.setTradePay(price);
            xdRecord.setUseCardnum(cardNo);
            xdRecord.setRecordVersion("0001");

            if (BusApp.getPosManager().getLineType().equals("P")) {
                BusToast.showToast("扫码成功", true);
                xdRecord.setInCardStatus("01");
                SoundPoolUtil.play(VoiceConfig.shuamachenggong);
            } else {
                BusToast.showToast("请您上车\n金额：" + fen2Yuan(price) + "元", true);
                SoundPoolUtil.play(VoiceConfig.shuamachenggong);
            }

            RecordUpload.saveRecord(xdRecord);
        } else {
            BusToast.showToast("重复使用", false);
            SoundPoolUtil.play(VoiceConfig.qingshuaxinchongsao);
        }
    }


    public void praseJtbScan(String result) {
        try {
            QRData qrData = new QRData(FileUtils.hexStringToBytes(result));

            XdRecord xdRecord = DBManagerZB.checkXdRecordByqrCode(result);
            if (xdRecord != null) {
                SoundPoolUtil.play(VoiceConfig.qingshuaxinchongsao);
                BusToast.showToast("二维码重复使用", false);
                return;
            }

            int resultCode = QRVerifyUtil.getInstance().verifyQR(qrData);


            if (resultCode == 5) {
                packegePay(result, qrData.getUserPayID(), qrData);
            } else if (resultCode == 4) {
                BusToast.showToast("二维码过期", false);
                SoundPoolUtil.play(VoiceConfig.erweimaguoqi);
            } else {
                BusToast.showToast("二维码格式错误", false);
                SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
            }
        } catch (Exception e) {
            BusToast.showToast("二维码解析错误", false);
            SoundPoolUtil.play(VoiceConfig.cuowu);
        }

//        Log.i("二维码解析", "解码前时间：" + System.currentTimeMillis());
//        JTB_QR_outline jtbQrOutline = DecryptUtil.qrDecrypt(map, result);
//        Log.i("二维码解析", "解码：" + jtbQrOutline.getMsg());
//        if (jtbQrOutline.getCode().equals("9000")) {
//            SoundPoolUtil.play(Config.quanchengyunka);
//            saveRecord(result, jtbQrOutline);
//        } else if (jtbQrOutline.getCode().equals("9007")) {
//            SoundPoolUtil.play(Config.erweimageshicuowu);
//        } else {
//            InitActiivty.resetParam();
//            SoundPoolUtil.play(Config.erweimamiyaoguoqi);
//        }
//        BusToast.showToast(jtbQrOutline.getMsg() + "");
    }

}
