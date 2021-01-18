package com.szxb.installapk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroaderCastInstall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("这是监听事件：", "监听");
        String packageName = "";
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            packageName = intent.getData().getSchemeSpecificPart();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            packageName = intent.getData().getSchemeSpecificPart();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            packageName = intent.getData().getSchemeSpecificPart();
        }
        Log.i("这是监听事件：", "packageName=" + packageName);
    }
}
