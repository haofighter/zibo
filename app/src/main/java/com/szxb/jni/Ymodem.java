package com.szxb.jni;

import android.content.res.AssetManager;
import android.util.Log;

/**
 * Created by lilei on 18-3-6.
 */

public class Ymodem {
    static final String TAG = "Ymodem";
    static {
        try {
            System.loadLibrary("ymodem");
        }
        catch (Throwable e) {
            Log.e(TAG,"ymodem");
        }

    }

    public static native int ymodemUpdate(AssetManager ass, String filename);
}
