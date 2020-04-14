package com.szxb.jni;


import android.util.Log;

/**
 * Created by lilei on 18-1-24.
 */

public class SerialCom {
    static final String TAG = "SerialCom";

    // Used to load the 'native-lib' library on application startup.
    static {
        try {
            System.loadLibrary("szxb-lib");
        }
        catch (Throwable e) {
            Log.e(TAG,"szxblib");
        }

    }



    public static native int SerialComOpen();
    public static native int SerialComClose();
    public static native int SerialComRead(byte[] readBuf,int nTimeOut);
    public static native int SerialComWrite(byte[] writeBuf,int nSendLen);
    public static native int DevRest();
    public static native long CRC32(byte[] bBuf,int index,int nSendLen);

}
