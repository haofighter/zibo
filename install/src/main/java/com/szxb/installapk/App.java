package com.szxb.installapk;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.lilei.tool.tool.IToolInterface;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class App extends Application {
    private IToolInterface mService;
    static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initService();
    }

    public static App getInstance() {
        return instance;
    }

    //连接服务
    private void initService() {
        Intent i = new Intent();
        i.setAction("com.lypeer.aidl");
        i.setPackage("com.lilei.tool.tool");
        boolean ret = bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.i("apk安装", "服务调用  ret=" + ret);
    }

    public IToolInterface getmService() {
        return mService;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IToolInterface.Stub.asInterface(service);
            Log.i("apk安装", "服务连接成功");
        }
    };

    private Activity activitie = null;

    public Activity getNowActivitie() {
        return activitie;
    }
}
