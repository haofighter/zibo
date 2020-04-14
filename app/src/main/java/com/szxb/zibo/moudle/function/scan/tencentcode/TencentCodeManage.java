package com.szxb.zibo.moudle.function.scan.tencentcode;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.config.zibo.PublicKey;
import com.szxb.zibo.config.zibo.line.CardPlan;
import com.szxb.zibo.config.zibo.line.FarePlan;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.manager.PosManager;
import com.szxb.zibo.record.XdRecord;
import com.tencent.wlxsdk.WlxSdk;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.moudle.function.scan.QRCode;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.runTool.RunSettiing;
import com.szxb.zibo.util.apkmanage.AppUtil;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.util.Calendar;
import java.util.List;

import static com.hao.lib.Util.FileUtils.fen2Yuan;
import static com.szxb.zibo.moudle.function.scan.QRCode.QR_ERROR;

public class TencentCodeManage {
    private WlxSdk wxSdk;

    private String oldCard;
    private String oldOpenId = "";

    private TencentCodeManage() {
        wxSdk = new WlxSdk();
    }

    private static class TencenCodeManageHelp {
        public static final TencentCodeManage tcm = new TencentCodeManage();
    }

    public static TencentCodeManage getInstance() {
        return TencenCodeManageHelp.tcm;
    }

    public void posScan(byte[] qrcode) throws Exception {
        if (wxSdk == null) wxSdk = new WlxSdk();
        int init = wxSdk.init(new String(qrcode));
        int key_id = wxSdk.get_key_id();
        String open_id = wxSdk.get_open_id();
        int mac_root_id = Integer.parseInt(wxSdk.get_mac_root_id());
        List<PublicKey> pubKey = DBManagerZB.getTXPublicKey(PosManager.TX_PUB, key_id);
        List<PublicKey> macKey = DBManagerZB.getTXPublicKey(PosManager.TX_MAC, mac_root_id);
        if (pubKey.size() == 0 && macKey.size() == 0) {
            BusApp.getPosManager().setPub_ver("00000000000000");
            BusToast.showToast("腾讯密钥配置失败", false);
            return;
        }
        int price = PraseLine.getNormalPayPrice("31", "00");//金额
        if (init == 0 && key_id > 0) {
            int verify = wxSdk.verify(open_id
                    , pubKey.get(0).getPublicKey()
                    , price
                    , (byte) 1
                    , (byte) 1
                    , BusApp.getPosManager().getPosSN()
                    , AppUtil.Random(8)
                    , macKey.get(0).getPublicKey());
            byte[] record = wxSdk.get_record();
            String openid = wxSdk.get_open_id();

            request(verify, price, qrcode, openid, record);
            oldOpenId = BusApp.oldCardTag = open_id;
            oldCard = new String(qrcode);
        } else {
            BusToast.showToast("二维码配置出错", false);
            SoundPoolUtil.play(VoiceConfig.wuxiaoma);
        }
    }

    public void request(int tag, int price, byte[] qrcode, String cardNo, byte[] record) throws Exception {
        switch (tag) {
            case QRCode.EC_SUCCESS:
                XdRecord xdRecord = new XdRecord();
                String extraDate = FileUtils.bytesToHexString(record);
                xdRecord.setExtraDate(extraDate);
                if (DBManagerZB.checkRepeatScan(FileUtils.bytesToHexString(qrcode)) == null) {
                    xdRecord.setTradePay(price);
                    xdRecord.setTradeType("06");
                    xdRecord.setMainCardType("31");
                    xdRecord.setChildCardType("00");
                    xdRecord.setUseCardnum(cardNo);
                    xdRecord.setRecordVersion("0001");
                    xdRecord.setQrCode(FileUtils.bytesToHexString(qrcode));
                    xdRecord.setDirection(BusApp.getPosManager().getDirection());
                    if (checkUseTime(xdRecord.getMainCardType(), xdRecord.getChildCardType(), xdRecord.getUseCardnum())) {
                        return;
                    }
                    if (BusApp.getPosManager().getLineType().equals("P")) {
//                        if (BusApp.getPosManager().getPosUpDate() == 2) {
//                            xdRecord.setInCardStatus("02");
//                        } else if (BusApp.getPosManager().getPosUpDate() == 1) {
//                            xdRecord.setInCardStatus("01");
//                        } else {
//                            if (DBManagerZB.checkRecordIn2H(cardNo
//                                    , BusApp.getPosManager().getDirection())) {
//                                xdRecord.setInCardStatus("02");
//                            } else {
//                                xdRecord.setInCardStatus("01");
//                            }
//                        }
                        xdRecord.setInCardStatus("01");
                        BusToast.showToast("扫码成功", true);
                        SoundPoolUtil.play(VoiceConfig.shuamachenggong);
                    } else {
                        BusToast.showToast("请您上车\n金额：" + fen2Yuan(price) + "元", true);
                        SoundPoolUtil.play(VoiceConfig.shuamachenggong);
                    }
                    RecordUpload.saveRecord(xdRecord);
                } else {
                    SoundPoolUtil.play(VoiceConfig.erweimachongfushiyong);
                    BusToast.showToast("二维码重复使用[" + tag + "]", false);
                }
                break;
            case QRCode.EC_MAC_OR_PUBLIC_ER:
                SoundPoolUtil.play(QR_ERROR);
                break;
            case QR_ERROR://非腾讯或者小兵二维码
            case QRCode.EC_CARD_CERT_SIGN_ALG_NOT_SUPPORT://卡证书签名算法不支持
            case QRCode.EC_MAC_ROOT_KEY_DECRYPT_ERR://加密的mac根密钥解密失败
            case QRCode.EC_QRCODE_SIGN_ALG_NOT_SUPPORT://二维码签名算法不支持
            case QRCode.EC_OPEN_ID://输入的openid不符
            case QRCode.EC_CARD_CERT://卡证书签名错误
            case QRCode.EC_FAIL://系统异常
                SoundPoolUtil.play(VoiceConfig.wuxiaoma);
                BusToast.showToast("二维码有误[" + tag + "]", false);
                break;
            case QRCode.EC_FEE://超出最大金额
                SoundPoolUtil.play(VoiceConfig.ec_fee);
                BusToast.showToast("超出最大金额[" + QRCode.EC_FEE + "]", false);
                break;
            case QRCode.EC_BALANCE://余额不足
                SoundPoolUtil.play(VoiceConfig.yuebuzu);
                BusToast.showToast("余额不足[" + QRCode.EC_BALANCE + "]", false);
                break;
//                String noticeStr = "二维码过期[10006]";
//                SoundPoolUtil.play(VoiceConfig.erweimaguoqi);
//                RunSettiing.getInstance().retTime(true);
//                BusToast.showToast(noticeStr, false);
//                break;
            case QRCode.EC_CODE_TIME://二维码过期
            case QRCode.REFRESH_QR_CODE://请刷新二维码
                SoundPoolUtil.play(VoiceConfig.erweimaguoqi);
                RunSettiing.getInstance().retTime(false);
                BusToast.showToast("请刷新二维码[" + tag + "]", false);
                break;
            case QRCode.EC_MAC_SIGN_ERR://mac校验失败
            case QRCode.EC_USER_SIGN://二维码签名错误
            case QRCode.EC_FORMAT://二维码格式错误
            case QRCode.EC_USER_PUBLIC_KEY://卡证书用户公钥错误
            case QRCode.EC_CARD_PUBLIC_KEY://卡证书公钥错误
            case QRCode.EC_PARAM_ERR://参数错误
            case QRCode.EC_CARD_CERT_TIME://卡证书过期，提示用户联网刷新二维码
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
                SoundPoolUtil.play(VoiceConfig.erweimamiyaoguoqi);
                BusToast.showToast("请刷新二维码[" + tag + "]", false);
                break;
            default:
                SoundPoolUtil.play(VoiceConfig.wuxiaoma);
                BusToast.showToast("二维码有误[" + tag + "]", false);
                break;
        }
    }


    public static boolean checkUseTime(String cardType, String childCardType, String cardNo) {
        FarePlan farePlan = DBManagerZB.checkFarePlan();
        if (farePlan != null) {
            CardPlan cardPlan = DBManagerZB.checkCardPlan(farePlan.getCardCaseNUm(), cardType, childCardType);
            if (cardPlan != null) {
                int limitTime = Integer.parseInt(cardPlan.getUseInterval());
                MiLog.i("刷码", "间隔时间：" + limitTime);
                XdRecord oldXdRecord = DBManagerZB.checkScanXdRecordByCardNo(cardNo);
                if (oldXdRecord != null && System.currentTimeMillis() - oldXdRecord.getCreatTime() < limitTime * 1000) {
                    BusToast.showToast("重复刷码", true);
                    SoundPoolUtil.play(VoiceConfig.erweimachongfushiyong);
                    return true;
                }
            }
        }
        return false;
    }
}
