package com.szxb.zibo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import com.szxb.lib.base.Rx.Rx;


/**
 * Created by 斩断三千烦恼丝 on 2017/8/3.
 */

public class SdCardReciver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, final Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String action = intent.getAction();
                Log.d("SdCardReciver",
                        "onReceive(SdCardReciver.java:46)" + action);
                if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                    Log.i("", "SD卡已被拔出");
                } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                    //获取外置SD卡的路径
                    //  path = intent.getData().getPath();
                    final String path = intent.getData().getPath();
                    Rx.getInstance().sendMessage("sdCardIn", path);
                }
            }
        }).start();

    }

 /*   private void getInstance() {
        if (mp == null) {
            mp = MyApplication.getInstance();
        }
    }*/

    //读取配置文件，选择当前要进行的组合操作
/*    private void ReadConfigOperate() {
        mp.getEx().execute(new ReadFromSdCard(path + CombinationOperation_xml, new BackResult() {
            @Override
            public void getResulte(Map map) {
                if (map != null) {
                    String flag = map.get("operate").toString();
                    switch (flag) {
                        case "1":
                            unInstallAllAPk();
                            updateApkFromSdCard(path + "/apks/", MyApplication.getInstance());
                            break;
                        case "2":
                            break;

                        case "3":
                            break;
                    }

                }
            }
        }));
    }*/

}
