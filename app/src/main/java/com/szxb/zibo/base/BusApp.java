package com.szxb.zibo.base;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.bluering.sdk.qrcode.jtb.JTBQRCodeSDK;
import com.hao.lib.Util.MiLog;
import com.hao.lib.base.MI2App;
import com.lilei.tool.tool.IToolInterface;
import com.szxb.zibo.BuildConfig;
import com.szxb.zibo.Mqtt.GetPushService;
import com.szxb.zibo.config.haikou.AppBuildConfig;
import com.szxb.zibo.config.haikou.AppPreload;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.manager.PosManager;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.apkmanage.ApkUtilImpl;
import com.szxb.zibo.util.apkmanage.IApkUtil;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BusApp extends MI2App {
    public static final String CPU_CARD = "03";
    public static final String M1_CARD = "04";
    public static final String NEW_CPU_CARD = "02";
    static BusApp instance;
    private static PosManager manager;
    private static int city = BuildConfig.CITY;
    private static String binName = BuildConfig.BIN_NAME;
    public static int downProgress = 0;

    public static Boolean netIsCanUse = true;
    //服务操作
    private IToolInterface mService;
    public final static AppPreload appPreload = new AppPreload();

    public static String oldCardTag = "";//前一次扫码 或刷卡的标识  用于判断是否被打断
    public static long oldCardTime = 0;//前一次扫码 或刷卡的标识  用于判断是否被打断

    public final static long repeatCount = 20;//连刷的最高次数
    public static boolean unionIsSign = false;//银联签到

    public GetPushService.MBinder mBinder;//MQTT的服务
    ApkUtilImpl iApkUtil;

    public IApkUtil getApkUtil() {
        if (iApkUtil == null) {
            iApkUtil = new ApkUtilImpl(this);
        }
        return iApkUtil;
    }

    @Override
    public void onCreate() {
        String processName = getProcessName(this, android.os.Process.myPid());
        if (processName != null && processName.endsWith("remote")) {
            return;
        }
        super.onCreate();
        instance = this;
        manager = new PosManager();
        MiLog.i("流程", "开机启动  app进入"+getPakageVersion());
        try {
            initConfig();
            manager.loadFromPrefs(city, binName);
            if (JTBQRCodeSDK.initSDK("/data/data/com.szxb.zibo/")) {
                MiLog.i("交通部", "初始化成功    版本号：" + JTBQRCodeSDK.getSDKVer());
            } else {
                MiLog.i("交通部", "初始化失败");
            }
            MiLog.clear(3);
        } catch (Exception e) {
            Log.i("流程错误", "app");
        }
        startHeart();
    }

    Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void startHeart() {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    InitConfigZB.sendHeart();
                } catch (Exception e) {
                }

            }
        }, 10, 10, TimeUnit.MINUTES);
    }

    public static PosManager getPosManager() {
        if (manager == null) {
            manager = new PosManager();
            manager.loadFromPrefs(city, binName);
        }
        return manager;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static BusApp getInstance() {
        return instance;
    }

    private void initConfig() {
        SDKInitializer.initialize(this);
        DBCore.init(this);
        Log.i("流程", "数据库初始化成功");
        AppBuildConfig.createConfig(BuildConfig.CITY);
        SoundPoolUtil.init(this);
        //初始化网络请求
        NoHttp.initialize(InitializationConfig.newBuilder(this)
                .networkExecutor(new URLConnectionNetworkExecutor())
                .connectionTimeout(15 * 1000)
                .build());

        initService();
    }

    //连接服务
    private void initService() {
        Intent i = new Intent();
        i.setAction("com.lypeer.aidl");
        i.setPackage("com.lilei.tool.tool");
        boolean ret = bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
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
        }
    };


    @SuppressLint("WrongConstant")
    public void bindMqtt() {
        Intent intent = new Intent(this, GetPushService.class);
        bindService(intent, mqttServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mqttServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (GetPushService.MBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    public String getPakageVersion() {
        try {
            return getPackageManager().getPackageInfo(
                    "com.szxb.zibo", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    //获取sim卡iccid
    @SuppressLint("MissingPermission")
    public String getIccid() {
        String iccid = "N/A";
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        iccid = telephonyManager.getSimSerialNumber();
        return iccid == null ? "N|A" : iccid;
    }


    /**
     * @return 是否有网络
     */
    public boolean getNetWorkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return true;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }


    public boolean checkSetting() {
        if (BusApp.getPosManager().getDriverNo().equals("00000000")) {
            BusToast.showToast("请刷司机卡", false);
            SoundPoolUtil.play(VoiceConfig.sijiweishangban);
            return true;
        }
        if (BusApp.getPosManager().getLineNo() == null || BusApp.getPosManager().equals("")) {
            BusToast.showToast("请设置线路", false);
            return true;
        }
        return false;
    }

    @Override
    public void finishAll() {
        super.finishAll();
    }


    /**
     * @param context .
     * @param pid     .
     * @return 获取进程名
     */
    public static String getProcessName(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return null;
        }

        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == pid) {
                return runningAppProcess.processName;
            }
        }
        return null;
    }
}
