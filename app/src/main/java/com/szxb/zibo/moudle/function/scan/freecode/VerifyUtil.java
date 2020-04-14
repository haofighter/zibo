//package com.szxb.jinan.moudle.function.scan.freecode;
//
//import android.text.TextUtils;
//import com.hao.lib.Util.FileUtils;
//import com.szxb.jinan.db.manage.DBManager;
//import com.szxb.jinan.util.BusToast;
//
//import static com.szxb.jni.JTQR.QrVerify;
//
///**
// * 作者：Tangren on 2018-11-27
// * 包名：com.szxb.test
// * 邮箱：996489865@qq.com
// */
//
//public class VerifyUtil {
//
//    //验码成功
//    public static final int SUCCESS = 0;
//
//    //解析异常
//    public static final int EXCEPTION = -1;
//
//    //CA证书不存在
//    public static final int CA_CERT_NO_EXIT = -101;
//
//    //发卡机构公钥证书无效
//    public static final int ISSUE_PUBLIC_CERT_INVALID = -102;
//
//    //发卡机构授权数据签名错误
//    public static final int ISSUE_SIGN_ERROR = -103;
//
//    //用户签名错误
//    public static final int USER_SIGN_ERROR = -104;
//
//    //二维码过期
//    public static final int QR_TIME_OUT = -105;
//
//    //金额超限
//    public static final int AMOUNT_ERROR = -106;
//
//    //公钥更新失败
//    public static final int PUBLIC_KEY_FAIL = -107;
//
//
//    private volatile static VerifyUtil instance = null;
//
//    private QRData qrData;
//
//    private VerifyUtil() {
//    }
//
//    public static VerifyUtil getInstance() {
//        if (instance == null) {
//            synchronized (VerifyUtil.class) {
//                if (instance == null) {
//                    instance = new VerifyUtil();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public int verifyQR(byte[] data) {
//        try {
//            qrData = null;
//            byte[] bytes = new byte[data.length];
////            System.arraycopy(data,0,bytes,0,bytes.length);
////            Log.e("data-->"+Util.bytesToHexString(bytes));
//            QRData qrData = new QRData(data);
//            QRData.QRIssuerCert qrIssuerCert = qrData.getQrIssuerCert();
//            byte[] verifyData = qrIssuerCert.getCertSignData();
//            String sign = qrIssuerCert.getCertSign();
//
//            String key = DBManager.checkConfig().getJTMAC();
//            if (key == null || TextUtils.equals(key, "")) {
//                BusToast.showToast("秘钥获取失败", false);
//                return PUBLIC_KEY_FAIL;
//            }
//
//            byte[] keyByte = FileUtils.hexStringToBytes(key);
//            byte[] keyCompress = FileUtils.compress(keyByte);
//            String keyHex = FileUtils.bytesToHexString(keyCompress);
//
//            int res = QrVerify(verifyData, keyHex, sign);
//            if (res != 0) {
//                return ISSUE_PUBLIC_CERT_INVALID;
//            }
//
//            verifyData = qrData.getIssuerSignData();
//            key = qrIssuerCert.getCertKey();
//            sign = qrData.getIssuerSign();
//            res = QrVerify(verifyData, key, sign);
//            if (res != 0) {
//                return ISSUE_SIGN_ERROR;
//            }
//
//            verifyData = qrData.getUserPrivateKeySignData();
//            key = qrData.getUserPublicKey();
//            sign = qrData.getUserPrivateKeySign();
//            res = QrVerify(verifyData, key, sign);
//            if (res != 0) {
//                return USER_SIGN_ERROR;
//            }
//
//            if (qrData.isBeOverdue()) {
//                return QR_TIME_OUT;
//            }
//            this.qrData = qrData;
//            return SUCCESS;
//        } catch (Exception e) {
//            e.printStackTrace();
////            Log.d("wuxinxi",
////                    "verifyQR(VerifyUtil.java:101)" + Utils.getExectionStr(e));
//        }
//        return EXCEPTION;
//    }
//
//    public QRData getQrData() {
//        return qrData;
//    }
//
//    public void setQrData(QRData qrData) {
//        this.qrData = qrData;
//    }
//}
