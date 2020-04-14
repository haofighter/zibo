package com.szxb.zibo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.hao.lib.Util.SystemUtils;

public class NetChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean netWorkState = SystemUtils.INSTANCE.getNetWorkState(context);
            Log.d("NetChangeReceiver","(onReceive.java:22)网络状态发送了改变>>>" + netWorkState);
        }
    }

}
