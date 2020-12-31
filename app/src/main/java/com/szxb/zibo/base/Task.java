package com.szxb.zibo.base;

import android.util.Log;


import com.szxb.lib.Util.ThreadUtils;
import com.szxb.zibo.moudle.function.unionpay.dispose.BankRefund;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.util.apkmanage.AppUtil;

import java.util.concurrent.TimeUnit;

public class Task {

    public static void runTask() {
        ThreadUtils.getInstance().
                createSch("upload").

                scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RecordUpload.upLoadCardRecord();

                        } catch (Exception e) {
                            Log.i("错误", "上传错误  upload");
                        }
                    }
                }, 10, 15, TimeUnit.SECONDS);

        ThreadUtils.getInstance().
                createSch("unionODA").
                scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BusApp.netIsCanUse = AppUtil.isNetPingUsable();//用于判断网络是否可用
                            RecordUpload.checkUnionODA();
                        } catch (Exception e) {
                            Log.i("错误", "上传错误  unionODA");
                        }
                    }
                }, 10, 5, TimeUnit.SECONDS);

//冲正
//        ThreadUtils.getInstance().createSch("checkUnion").scheduleAtFixedRate(new BankRefund(), 1, 10, TimeUnit.SECONDS);

    }


}
