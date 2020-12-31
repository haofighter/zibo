package com.szxb.zibo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.moudle.init.InitActiivty;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT) || android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) { //开机启动完成后，要做的事情
            MiLog.i("流程 广播接收", "接收到开机广播");
            Intent mainActivityIntent = new Intent(context, InitActiivty.class);
            mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
