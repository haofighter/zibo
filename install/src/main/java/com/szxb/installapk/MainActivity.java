package com.szxb.installapk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;


import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    long time = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        installApk();
        MiLog.i("apk安装", "启动线程安装");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    ScheduledFuture scheduledFuture;

    public void installApk() {
        time = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
//                    Context mcontext = App.getInstance().createPackageContext("com.szxb.zibo", CONTEXT_IGNORE_SECURITY);
//                    SharedPreferences msharedpreferences = mcontext.getSharedPreferences("XB_BASE_PARAMS_TEMP", MODE_PRIVATE);
//                    String filePath = msharedpreferences.getString("filePath", "");
                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zibo.apk";
                    File file = new File(filePath);
                    MiLog.i("apk安装", "获取到的apk地址：" + filePath);
                    String uninstallStr = "fail";
                    if (file.exists()) {
                        if (App.getInstance().getmService() != null) {
                            MiLog.i("apk安装", "开始卸载");
                            uninstallStr = App.getInstance().getmService().apkUninstall("com.szxb.zibo");
                            MiLog.i("apk安装", "卸载完成,结果：" + uninstallStr);
                            MiLog.i("apk安装", "开始安装");
                            String str = App.getInstance().getmService().apkInstall(filePath);
                            MiLog.i("apk安装", "安装完成,结果" + str);
                            if (str.contains("Success")) {
                                file.delete();
                                MiLog.i("apk安装", "安装成功" + str);
                                PackageManager packageManager = App.getInstance().getPackageManager();
                                Intent it = packageManager.getLaunchIntentForPackage("com.szxb.zibo");
                                App.getInstance().startActivity(it);
                                MainActivity.this.finish();
                            } else {
                                MiLog.i("apk安装", "安装失败：" + str);
                            }
                        }
                    }
                } catch (Exception e) {
                    MiLog.i("apk安装", "未获取到  错误：" + e.getMessage());
                    finish();
                }
            }
        }).start();
    }
}
