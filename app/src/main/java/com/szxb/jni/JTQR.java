package com.szxb.jni;


import android.util.Log;

public class JTQR {

    static {
        try {
            System.loadLibrary("QR");
        } catch (Throwable e) {
            Log.e("jni", "i can't find QR so!");
            e.printStackTrace();
        }
    }


    //bQRbuf 验签数据
    //pubKey 压缩公钥 66
    //sig r||s 校验值 128
    public static native int QrVerify(byte[] bQRbuf, String pubKey, String sig);

    public static native int sm3Digest(byte[] dataBuf, int len, byte[] digBuf);

    public static native int sm4Crypt(boolean b, byte[] input, byte[] outBuf, byte[] k);

}
