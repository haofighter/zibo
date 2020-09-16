package com.szxb.zibo.moudle.function.scan.freecode;

import android.text.TextUtils;
import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.szxb.jni.JTQR;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.PublicKey;
import com.szxb.zibo.manager.PosManager;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Tangren on 2018-11-27
 * 包名：com.szxb.test
 * 邮箱：996489865@qq.com
 */

public class QRVerifyUtil {

    private volatile static QRVerifyUtil instance = null;

    private QRData qrData;

    private QRVerifyUtil() {
    }

    public static QRVerifyUtil getInstance() {
        if (instance == null) {
            synchronized (QRVerifyUtil.class) {
                if (instance == null) {
                    instance = new QRVerifyUtil();
                }
            }
        }
        return instance;
    }

    /**
     * app 交通部标准二维码
     *
     * @param qrData .
     * @return .
     */
    public int verifyQR(QRData qrData) {
        QRData.QRIssuerCert qrIssuerCert = qrData.getQrIssuerCert();
        byte[] verifyData = qrIssuerCert.getCertSignData();
        String sign = qrIssuerCert.getCertSign();

        Log.i("交通部二维码", "密钥索引：" + qrIssuerCert.getcAIndex());
        List<PublicKey> keys = new ArrayList<>();
        if (qrData.getIssuer().equals("03664530")) {
            keys = DBManagerZB.getTXPublicKey(PosManager.FR_PUB, 23);
        } else {
            keys = DBManagerZB.getTXPublicKey(PosManager.FR_PUB, 1);
        }
        if (keys.size() == 0) {
            BusToast.showToast("交通部公钥获取失败", false);
            SoundPoolUtil.play(VoiceConfig.cuowu);
            return -1;
        }
        String key = keys.get(0).getPublicKey();
        if (TextUtils.isEmpty(key)) {
            return 0;
        }

        if (TextUtils.equals(key, "")) {
//            return NumberConstant.CERT_KEY_NO_EXIST;
            return 0;
        }

        byte[] keyByte = FileUtils.hex2byte(key);
        byte[] keyCompress = FileUtils.compress(keyByte);
        String keyHex = FileUtils.bytesToHexString(keyCompress);

        int res = JTQR.QrVerify(verifyData, keyHex, sign);
        if (res != 0) {
//            return NumberConstant.FAIL_CERT_INVALID;
            return 1;
        }

        verifyData = qrData.getIssuerSignData();
        key = qrIssuerCert.getCertKey();
        sign = qrData.getIssuerSign();
        res = JTQR.QrVerify(verifyData, key, sign);
        if (res != 0) {
//            return NumberConstant.FAIL_CARD_ISSUER_AUTH;
            return 2;
        }

        verifyData = qrData.getUserPrivateKeySignData();
        key = qrData.getUserPublicKey();
        sign = qrData.getUserPrivateKeySign();
        res = JTQR.QrVerify(verifyData, key, sign);
        if (res != 0) {
//            return NumberConstant.FAIL_USER_PUB_KEY;
            return 3;
        }

        if (qrData.isBeOverdue()) {
//            return NumberConstant.FAIL_QR_TIME_OUT;
            return 4;
        }
        this.qrData = qrData;
//        return NumberConstant.SUCCESS;
        return 5;
    }


//    /**
//     * 小程序二维码
//     *
//     * @param qrCode .
//     * @return .
//     */
//    public AppletsQRData verifyApplets(byte[] qrCode) {
//        String key = BusApp.getPosManager().getAppletsKey();
//        if (TextUtils.isEmpty(key)) {
//            return new AppletsQRData(NumberConstant.TODAY_KEY_NO_EXIST);
//        }
//        String decode = Des3Util.decode(key, qrCode);
//        if (TextUtils.isEmpty(decode)) {
//            return new AppletsQRData(NumberConstant.FAIL_INVALID);
//        }
//        if (decode == null || decode.length() < NumberConstant.APPLETS_QR_LEN) {
//            return new AppletsQRData(NumberConstant.QR_LEN_ERROR);
//        }
//        int index = 0;
//        AppletsQRData qrData = new AppletsQRData();
//        qrData.qrCode = decode;
//        qrData.createTime = NumberUtil.str2Long(decode.substring(index, index += NumberConstant.CREATE_TIME_LEN));
//        qrData.userId = decode.substring(index, index += NumberConstant.USER_ID_LEN);
//        qrData.userPhone = decode.substring(index, index += NumberConstant.USER_PHONE_LEN);
//        qrData.balance = decode.substring(index, index += NumberConstant.BALANCE_LEN);
//        qrData.qrType = decode.substring(index, index += NumberConstant.QR_TYPE_LEN);
//        qrData.dis = decode.substring(index, index += NumberConstant.DIS_LEN);
//        qrData.orderNo = decode.substring(index);
//        long currentTime = System.currentTimeMillis();
//        if (Math.abs(currentTime - qrData.createTime) > NumberConstant.NUM_60 * 1000) {
//            qrData.response = NumberConstant.FAIL_QR_TIME_OUT;
//            return qrData;
//        }
//        return qrData;
//    }
}
