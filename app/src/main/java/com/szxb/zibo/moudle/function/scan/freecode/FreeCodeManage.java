package com.szxb.zibo.moudle.function.scan.freecode;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hao.lib.Util.FileUtils;
import com.hao.lib.base.Rx.Rx;
import com.lilei.tool.tool.IToolInterface;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.SignIn;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.jni.JTQR;
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

import static com.hao.lib.Util.FileUtils.fen2Yuan;
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
                    PraseLine.praseLine(FileUtils.readAssetsFileTobyte("20200105193254.far", BusApp.getInstance()));
                } else if (line.equals("400251_1")) {
                    PraseLine.praseLine(FileUtils.readAssetsFileTobyte("20191228195940.far", BusApp.getInstance()));
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

    public void praseJTBScan(byte[] qrCode) {

        try {
            Log.i("自有码", "线路发卡机构代码" + FileUtils.bytesToHexString(qrCode));
            int i = 0;
            byte[] Version = new byte[1];
            arraycopy(qrCode, i, Version, 0, Version.length);
            i += Version.length;
            int version = Integer.parseInt(FileUtils.bytesToHexString(Version), 16);

            if (!(version >= 0x80 && version <= 0xFF)) {
                BusToast.showToast("二维码格式错误", false);
                return;
            }

            //二维码数据长度
            byte[] ScanLen = new byte[2];
            arraycopy(qrCode, i, ScanLen, 0, ScanLen.length);
            i += ScanLen.length;
            String scanLen = FileUtils.bytesToHexString(ScanLen);

            //发卡机构公钥证书
            byte[] issuerCert = new byte[117];
            System.arraycopy(qrCode, i, issuerCert, 0, issuerCert.length);
            i += issuerCert.length;
            if (!checkCert(issuerCert)) {
//                BusToast.showToast("账户过期", false);
//                SoundPoolUtil.play(VoiceConfig.cikayiguoyouxiaoqi);
                return;
            }


            //支付账户号
            byte[] PayCount = new byte[16];
            System.arraycopy(qrCode, i, PayCount, 0, PayCount.length);
            i += PayCount.length;
            String payCount = new String(PayCount);

            //卡账户号
            byte[] CardCount = new byte[10];
            System.arraycopy(qrCode, i, CardCount, 0, CardCount.length);
            i += CardCount.length;
            String cardCount = FileUtils.bytesToHexString(CardCount);

            //发卡机构号
            byte[] CreatCardIs = new byte[4];
            System.arraycopy(qrCode, i, CreatCardIs, 0, CreatCardIs.length);
            i += CreatCardIs.length;
            String creatCardIs = FileUtils.bytesToHexString(CreatCardIs);


            //发码平台编号
            byte[] CreatScanIs = new byte[4];
            System.arraycopy(qrCode, i, CreatScanIs, 0, CreatScanIs.length);
            i += CreatScanIs.length;
            String creatScanIs = FileUtils.bytesToHexString(CreatScanIs);

            //卡账户类型
            byte[] CreatType = new byte[1];
            System.arraycopy(qrCode, i, CreatType, 0, CreatType.length);
            i += CreatType.length;
            String creatType = FileUtils.bytesToHexString(CreatType);

            if (creatType.toLowerCase().endsWith("dd")) {
                BusToast.showToast("暂不能使用", false);
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                return;
            }

            //单次消费金额上限
            byte[] PayLimit = new byte[3];
            System.arraycopy(qrCode, i, PayLimit, 0, PayLimit.length);
            i += PayLimit.length;
            String payLimit = FileUtils.bytesToHexString(PayLimit);

            //支付账户用户公钥
            byte[] PayPublic = new byte[33];
            System.arraycopy(qrCode, i, PayPublic, 0, PayPublic.length);
            i += PayPublic.length;
            String payPublic = FileUtils.bytesToHexString(PayPublic);

            //支付账户系统授权过期时间
            byte[] PayExpire = new byte[4];
            System.arraycopy(qrCode, i, PayExpire, 0, PayExpire.length);
            i += PayExpire.length;
            String payExpire = FileUtils.bytesToHexString(PayExpire);

            if (Long.parseLong(payExpire, 16) < System.currentTimeMillis() / 1000) {
                BusToast.showToast("账户过期", false);
                SoundPoolUtil.play(VoiceConfig.cikayiguoyouxiaoqi);
                return;
            }


            //二维码有效时间
            byte[] ScanExpire = new byte[2];
            System.arraycopy(qrCode, i, ScanExpire, 0, ScanExpire.length);
            i += ScanExpire.length;
            long scanExpire = Long.parseLong(FileUtils.bytesToHexString(ScanExpire), 16);

            //发卡机构自定义域长度
            byte[] FreeAreaLenth = new byte[1];
            System.arraycopy(qrCode, i, FreeAreaLenth, 0, FreeAreaLenth.length);
            i += FreeAreaLenth.length;
            int freeAreaLenth = Integer.parseInt(FileUtils.bytesToHexString(FreeAreaLenth));

            byte[] FreeArea = new byte[freeAreaLenth];
            if (freeAreaLenth != 0) {
                //发卡机构自定义域
                System.arraycopy(qrCode, i, FreeArea, 0, FreeArea.length);
                i += FreeArea.length;
                String freeArea = FileUtils.bytesToHexString(FreeArea);
            }

            byte[] IssDate = new byte[i - 3];
            System.arraycopy(qrCode, 3, IssDate, 0, IssDate.length);

            byte[] IsSignTag = new byte[1];
            System.arraycopy(qrCode, i, IsSignTag, 0, IsSignTag.length);
            i += IsSignTag.length;
            String isSignTag = FileUtils.bytesToHexString(IsSignTag);

            // 发卡机构授权签名
            byte[] IsSign = new byte[64];
            System.arraycopy(qrCode, i, IsSign, 0, IsSign.length);
            i += IsSign.length;
            String isSign = FileUtils.bytesToHexString(IsSign);

//            int isRet = JTQR.QrVerify(IssDate, payPublic, isSign);
//            if (isRet != 0) {
//                BusToast.showToast("二维码有误", false);
//                SoundPoolUtil.play(VoiceConfig.qingshuaxinchongsao);
//                return;
//            }

            //二维码生成时间
            byte[] ScanCreateTime = new byte[4];
            System.arraycopy(qrCode, i, ScanCreateTime, 0, ScanCreateTime.length);
            i += ScanCreateTime.length;
            String scanCreateTime = FileUtils.bytesToHexString(ScanCreateTime);

            if (System.currentTimeMillis() / 1000 - Long.parseLong(scanCreateTime, 16) > scanExpire) {
                BusToast.showToast("二维码过期", false);
                SoundPoolUtil.play(VoiceConfig.qingshuaxinchongsao);
                return;
            }

            byte[] PayPrivateTag = new byte[1];
            System.arraycopy(qrCode, i, PayPrivateTag, 0, PayPrivateTag.length);
            i += PayPrivateTag.length;
            String payPrivateTag = FileUtils.bytesToHexString(PayPrivateTag);

            //支付账户用户私钥签名
            byte[] PayPrivate = new byte[64];
            System.arraycopy(qrCode, i, PayPrivate, 0, PayPrivate.length);
            i += PayPrivate.length;
            String userPrivateKeySign = FileUtils.bytesToHexString(PayPrivate);

            byte[] userPrivateKeySignData = new byte[qrCode.length - 65];
            System.arraycopy(qrCode, 0, userPrivateKeySignData, 0, userPrivateKeySignData.length);

            int res = JTQR.QrVerify(userPrivateKeySignData, payPublic, userPrivateKeySign);
            if (res != 0) {
                BusToast.showToast("二维码验证失败【" + res + "】", false);
                SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
                return;
            }

            XdRecord xdRecord = DBManagerZB.checkXdRecordByqrCode(FileUtils.bytesToHexString(qrCode));
            if (xdRecord != null) {
                SoundPoolUtil.play(VoiceConfig.qingshuaxinchongsao);
                BusToast.showToast("二维码重复使用", false);
            } else {
                Log.i("交通部二维码", "验证通过");
                packegePay(qrCode, payCount);
            }
        } catch (Exception e) {
            Log.i("错误", "交通部二维码错误" + e.getMessage());
        }

    }

    private void packegePay(byte[] qrcode, String cardNo) {
        XdRecord xdRecord = new XdRecord();
        String extraDate = FileUtils.bytesToHexString(qrcode);
        xdRecord.setQrCode(extraDate);
        xdRecord.setExtraDate(extraDate);
        if (DBManagerZB.checkRepeatScan(extraDate) == null) {
            int price = PraseLine.getNormalPayPrice("33", "00");//金额
            xdRecord.setTradePay(price);
            xdRecord.setTradeType("0a");
            xdRecord.setMainCardType("33");
            xdRecord.setChildCardType("00");
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


    public boolean checkCert(byte[] issuerCert) {
        int i = 0;
        byte[] head = new byte[1];
        System.arraycopy(issuerCert, i, head, 0, head.length);
        i += head.length;
        String ihead = FileUtils.bytesToHexString(head);

        byte[] serverTag = new byte[4];
        System.arraycopy(issuerCert, i, serverTag, 0, serverTag.length);
        i += serverTag.length;

        //中心 CA 公钥索引
        byte[] caIndex = new byte[1];
        System.arraycopy(issuerCert, i, caIndex, 0, caIndex.length);
        i += caIndex.length;

        //证书格式
        byte[] Iss = new byte[1];
        System.arraycopy(issuerCert, i, Iss, 0, Iss.length);
        i += Iss.length;

        //发卡机构代码
        byte[] IssCode = new byte[4];
        System.arraycopy(issuerCert, i, IssCode, 0, IssCode.length);
        i += IssCode.length;

        //证书失效日期
        byte[] lostTime = new byte[2];
        System.arraycopy(issuerCert, i, lostTime, 0, lostTime.length);
        i += lostTime.length;
        String certInvalidDate = FileUtils.bytesToHexString(lostTime);
        String resetCertInvalidDate = certInvalidDate.substring(2) + certInvalidDate.substring(0, 2);
        String currentDate = DateUtil.getYM();
        if (resetCertInvalidDate.compareTo(currentDate) < 0) {
            BusToast.showToast("二维码过期", false);
            SoundPoolUtil.play(VoiceConfig.erweimaguoqi);
            return false;
        }


        //证书序列号
        byte[] trano = new byte[3];
        System.arraycopy(issuerCert, i, trano, 0, trano.length);
        i += trano.length;

        //发卡机构 公钥签名算法标识
        byte[] calSignTag = new byte[1];
        System.arraycopy(issuerCert, i, calSignTag, 0, calSignTag.length);
        i += calSignTag.length;

        //发卡机构 公钥加密算法标识
        byte[] calMacTag = new byte[1];
        System.arraycopy(issuerCert, i, calMacTag, 0, calMacTag.length);
        i += calMacTag.length;

        //公钥参数标识
        byte[] publicTag = new byte[1];
        System.arraycopy(issuerCert, i, publicTag, 0, publicTag.length);
        i += publicTag.length;

        //发卡机构公钥长度
        byte[] publicLen = new byte[1];
        System.arraycopy(issuerCert, i, publicLen, 0, publicLen.length);
        i += publicLen.length;

        //发卡机构公钥
        byte[] publickey = new byte[33];
        System.arraycopy(issuerCert, i, publickey, 0, publickey.length);
        i += publickey.length;

        //数字签名
        byte[] sign = new byte[64];
        System.arraycopy(issuerCert, i, sign, 0, sign.length);
        i += sign.length;
        String certSign = FileUtils.bytesToHexString(sign);


        int certSignDataLen = issuerCert.length - 70;
        int startIndex = 6;
        byte[] certSignData = new byte[certSignDataLen];
        System.arraycopy(issuerCert, startIndex, certSignData, 0, certSignData.length);

        int res = JTQR.QrVerify(certSignData, FileUtils.bytesToHexString(publickey), certSign);
        //(2.)res!=0时 返回错误码
        if (res != 0) {
            BusToast.showToast("请检查二维码【" + res + "】", false);
            SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
            return false;
        }

        return true;
    }


    public void praseJtbScan(String result) {
        try {
            QRData qrData = new QRData(FileUtils.hexStringToBytes(result));


            int resultCode = QRVerifyUtil.getInstance().verifyQR(qrData);


            if (resultCode == 5) {
                BusToast.showToast("刷码成功", true);
            } else if (resultCode == 4) {
                BusToast.showToast("二维码过期", false);
            } else {
                BusToast.showToast("二维码格式错误", false);
            }
        } catch (Exception e) {
            BusToast.showToast("二维码解析错误", false);
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
