package com.szxb.zibo.moudle.function.scan.alicode;

import android.util.Log;
import com.ibuscloud.ibuscloudposlib.IBusCloudPos;
import com.ibuscloud.ibuscloudposlib.constant.IBusCloudStdRetCodeEnum;

import com.szxb.jni.Alipay;
import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.config.zibo.PublicKey;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.manager.PosManager;
import com.szxb.zibo.moudle.function.scan.tencentcode.TencentCodeManage;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.runTool.RunSettiing;
import com.szxb.zibo.util.apkmanage.AppUtil;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.util.List;

import static com.szxb.jni.Alipay.init_pos_verify;
import static com.szxb.jni.Alipay.verify_qrcode_v2;
import static com.szxb.lib.Util.FileUtils.fen2Yuan;


public class AliCodeManage {

    private AliCodeManage() {
        initALiSDK();
    }

    private static class AliCodeManageHelp {
        private static AliCodeManage fcm = new AliCodeManage();
    }

    private static final String TAG = "AliCodeManage";

    public static AliCodeManage getInstance() {
        return AliCodeManage.AliCodeManageHelp.fcm;
    }

    public void posScan(byte[] qrCode) throws Exception {
        String cardList = "[\"T0370301\"]";
//        String cardList = "T0000301";
        // TODO: 2019/7/5 支付宝验码
        List<PublicKey> pubKey = DBManagerZB.getTXPublicKey(PosManager.ALI_PUB, -1);

        /**************************************************/
        String aliKey = "[";
        for (int i = 0; i < pubKey.size(); i++) {
            aliKey += "{\"key_id\":" + pubKey.get(i).getPublicKeyTag() + ",\"public_key\":\""
                    + pubKey.get(i).getPublicKey() + "\"}";
            if (i != pubKey.size() - 1) {
                aliKey += ",";
            }
        }
        aliKey += "]";
        /************************************************/
        int nRet = init_pos_verify(aliKey, cardList);
        Log.i("支付宝二维码", "nRet=" + nRet);
        String par = "{\"pos_id\":\"" + BusApp.getPosManager().getPosSN()
                + "\",\"type\":\"SINGLE\",\"subject\":\"zibo\",\"record_id\":\""
                + BusApp.getPosManager().getPosSN() + DateUtil.getCurrentDate2() + "\"}";
        Alipay tmp = new Alipay();
        Alipay.VERIFY_REQUEST_V2 req = tmp.new VERIFY_REQUEST_V2(qrCode, qrCode.length, par, 10);
        Alipay.VERIFY_RESPONSE_V2 res = verify_qrcode_v2(req);
        Log.e(TAG, "posScan: " + new String(res.getCard_type()));
        if (res.getnRet() == 1&&cardList.contains(new String(res.getCard_type()))) {//二维码检验通过
            XdRecord xdRecord = new XdRecord();
            byte[] re = res.getRecord();
            String extraDate = new String(re);
            xdRecord.setExtraDate(extraDate);

            if (DBManagerZB.checkRepeatScan(extraDate) == null) {
                int price = PraseLine.getNormalPayPrice("32", "00");
                xdRecord.setTradePay(price);
                xdRecord.setTradeType("05");
                xdRecord.setUseCardnum(new String(res.getUid()));
                xdRecord.setRecordVersion("0002");
                xdRecord.setMainCardType("32");
                xdRecord.setChildCardType("00");
                xdRecord.setDirection(BusApp.getPosManager().getDirection());
                if (TencentCodeManage.checkUseTime(xdRecord.getMainCardType(), xdRecord.getChildCardType(), xdRecord.getUseCardnum())) {
                    return;
                }
                if (BusApp.getPosManager().getLineType().equals("P")) {

//                    if (BusApp.getPosManager().getPosUpDate() == 2) {
//                        xdRecord.setInCardStatus("02");
//                    } else if (BusApp.getPosManager().getPosUpDate() == 1) {
//                        xdRecord.setInCardStatus("01");
//                    } else {
//                        if (DBManagerZB.checkRecordIn2H(new String(res.getUid()), BusApp.getPosManager().getDirection())) {
//                            xdRecord.setInCardStatus("02");
//                        } else {
//                            xdRecord.setInCardStatus("01");
//                        }
//                    }
                    xdRecord.setInCardStatus("01");
                    MiLog.i("支付宝", "验码状态：" + xdRecord.getInCardStatus() + "       " + xdRecord.getUseCardnum());
                    BusToast.showToast("扫码成功", true);
                    SoundPoolUtil.play(VoiceConfig.shuamachenggong);
                } else {
                    SoundPoolUtil.play(VoiceConfig.shuamachenggong);
                    BusToast.showToast("请您上车\n金额：" + fen2Yuan(price) + "元", true);
                }
                RecordUpload.saveRecord(xdRecord);
            } else {
                SoundPoolUtil.play(VoiceConfig.erweimachongfushiyong);
                BusToast.showToast("二维码重复使用", false);
            }
        } else if (res.getnRet() == Alipay.MALFORMED_QRCODE) {
            SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
            BusToast.showToast("二维码格式错误", false);
        } else if (res.getnRet() == Alipay.QRCODE_KEY_EXPIRED) {
            SoundPoolUtil.play(VoiceConfig.erweimamiyaoguoqi);
            BusToast.showToast("支付宝密钥过期", false);
        } else if (res.getnRet() == Alipay.POS_PARAM_ERROR) {
            SoundPoolUtil.play(VoiceConfig.saomashibai);
            BusToast.showToast("支付宝参数配置出错", false);
            BusApp.getPosManager().setPub_ver("00000000000000");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InitConfigZB.sendInfoToServer(InitConfigZB.INFO_UP);
                    } catch (Exception e) {
                    }
                }
            }).start();
        } else if (res.getnRet() == Alipay.QUOTA_EXCEEDED) {
            // SoundPoolUtil.play(VoiceConfig.jineshoixian);
            BusToast.showToast("金额受限", false);
        } else if (res.getnRet() == Alipay.NO_ENOUGH_MEMORY) {
            SoundPoolUtil.play(VoiceConfig.xitongyichang);
            BusToast.showToast("超出内存，请重启", false);
        } else if (res.getnRet() == Alipay.SYSTEM_ERROR) {
            SoundPoolUtil.play(VoiceConfig.xitongyichang);
            BusToast.showToast("系统异常", false);
        } else if (res.getnRet() == Alipay.CARDTYPE_UNSUPPORTED) {
            SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
            BusToast.showToast("不支持此种卡类型", false);
        } else if (res.getnRet() == Alipay.NOT_INITIALIZED) {
            SoundPoolUtil.play(VoiceConfig.xitongyichang);
            BusToast.showToast("未初始化SDK", false);
        } else if (res.getnRet() == Alipay.ILLEGAL_PARAM) {
            SoundPoolUtil.play(VoiceConfig.xitongyichang);
            BusToast.showToast("非法参数", false);
        } else if (res.getnRet() == Alipay.QRCODE_DUPLICATED) {
            SoundPoolUtil.play(VoiceConfig.erweimachongfushiyong);
            BusToast.showToast("二维码重复使用", false);
        } else if (res.getnRet() == Alipay.PROTO_UNSUPPORTED) {
            SoundPoolUtil.play(VoiceConfig.erweimaguoqi);
            RunSettiing.getInstance().retTime(false);
            BusToast.showToast("二维码过期", false);
        } else if (res.getnRet() == -2) {
            SoundPoolUtil.play(VoiceConfig.erweimaguoqi);
            RunSettiing.getInstance().retTime(false);
            BusToast.showToast("二维码过期", false);
        } else {
            SoundPoolUtil.play(VoiceConfig.wuxiaoma);
            BusToast.showToast("无效码", false);
        }
    }

    private void noitc(int returnCode) {
        switch (returnCode) {
            case 205://过期
                BusToast.showToast("二维码过期", false);
                SoundPoolUtil.play(VoiceConfig.erweimaguoqi);
                break;
            case 206:
                BusToast.showToast("二维码秘钥过期", false);
                SoundPoolUtil.play(VoiceConfig.erweimamiyaoguoqi);
                break;
            case 211:
                BusToast.showToast("二维码重复使用", false);
                SoundPoolUtil.play(VoiceConfig.erweimachongfushiyong);
                break;
            default:
                BusToast.showToast("二维码格式错误", false);
                SoundPoolUtil.play(VoiceConfig.erweimageshicuowu);
                break;

        }
    }

    private void initALiSDK() {
        IBusCloudStdRetCodeEnum rc = IBusCloudPos.initIBusCloudSDK();
        if (IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_SUCCESS != rc) {
            throw new RuntimeException("initIBusCloudSDK Failed!");
        }
    }

}
