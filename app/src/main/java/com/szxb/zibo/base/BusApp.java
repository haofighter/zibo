package com.szxb.zibo.base;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.lilei.tool.tool.IToolInterface;
import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.lib.base.MI2App;
import com.szxb.zibo.BuildConfig;
import com.szxb.zibo.Mqtt.GetPushService;
import com.szxb.zibo.base.tinker.App;
import com.szxb.zibo.base.tinker.TinkerManager;
import com.szxb.zibo.config.haikou.AppBuildConfig;
import com.szxb.zibo.config.haikou.AppPreload;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.db.bean.FTPEntity;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.manager.PosManager;
import com.szxb.zibo.record.AppParamInfo;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.MMKVManager;
import com.szxb.zibo.util.apkmanage.ApkUtilImpl;
import com.szxb.zibo.util.apkmanage.IApkUtil;
import com.szxb.zibo.util.md5.MD5;
import com.szxb.zibo.util.sp.CommonSharedPreferences;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BusApp extends MI2App {
    public static final String CPU_CARD = "03";
    public static final String M1_CARD = "04";
    public static final String NEW_CPU_CARD = "02";
    public static final String JTB_CARD = "01";
    private static PosManager manager;
    private static int city = BuildConfig.CITY;
    private static String binName = BuildConfig.BIN_NAME;
    public static int downProgress = 0;
    public static String pakegeVersion = BuildConfig.VERSION_NAME;

    private static BusApp app;

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

    public static BusApp getInstance() {
        if (app == null) {
            app = new BusApp();
        }
        return app;
    }


    public void init() {
        DBCore.init(getApplication());
        String path = Environment.getExternalStorageDirectory() + "/config";
        MMKVManager.getInstance(path);
        String processName = getProcessName(getApplication(), android.os.Process.myPid());
        if (processName != null && processName.endsWith("remote")) {
            return;
        }

        getPosManager();
        MiLog.i("流程", "开机启动" + getPakageVersion());
        try {
            RecordUpload.clearDateBase();
            initConfig();
            manager.loadFromPrefs(city, binName);
            initRunParam();
        } catch (Exception e) {
            Log.i("流程错误", "app");
        }
//        byte[] csn = FileUtils.readAssetsFileTobyte("20200529030000.csn", this);
//        PraseLine.praseCsnByte(csn,"20200529030000.csn");
//        DBManagerZB.checkedBlack("2550000000000005");

//        XdRecord xdRecord=new XdRecord();
//        xdRecord.praseDate("0200011F010101FDFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF303430303338FFFFFFFF303031343736010000000000000000FFFFFFFF51364239413154323138313630313930370020095539255002000762000000000000000405960000000000000042455420200723080805CE02000000000000F4010000120C0000E00000000000000000002550000000000001000201000000001551092020072308080500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000FF1");


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
                    MiLog.i("参数配置", "检查运行参数 心跳检查：" + new Gson().toJson(DBManagerZB.checkAppParamInfo()));
                } catch (Exception e) {
                }

            }
        }, 3, 3, TimeUnit.MINUTES);
    }

    public static PosManager getPosManager() {
        if (manager == null) {
            manager = new PosManager();
            manager.loadFromPrefs(city, binName);
        }
        return manager;
    }

    public static void setManager(PosManager manager) {
        BusApp.manager.init(manager);
    }


    private void initConfig() {
        SDKInitializer.initialize(getApplication());
        Log.i("流程", "数据库初始化成功");
        AppBuildConfig.createConfig(BuildConfig.CITY);
        SoundPoolUtil.init(getApplication());
        initService();
    }

    /**
     * 升级后参数需重新下载
     */
    private void initRunParam() {
        String version = (String) MMKVManager.getInstance().get("version", "00000000000000");
        Log.i("流程", "当前版本：" + BusApp.getInstance().getPakageVersion() + "            历史版本： " + version);
        if (!BusApp.getInstance().getPakageVersion().equals(version)) {
            BusApp.getPosManager().clearRunParam();
            MMKVManager.getInstance().put("version", BusApp.getInstance().getPakageVersion());
        }

    }

    //连接服务
    private void initService() {
        Intent i = new Intent();
        i.setAction("com.lypeer.aidl");
        i.setPackage("com.lilei.tool.tool");
        boolean ret = getApplication().bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
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
        Intent intent = new Intent(getApplication(), GetPushService.class);
        getApplication().bindService(intent, mqttServiceConnection, Context.BIND_AUTO_CREATE);
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
//        try {
//            return getApplication().getPackageManager().getPackageInfo(
//                    "com.szxb.zibo", 0).versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        return pakegeVersion;
    }

    public void setPakegeVersion(String pakegeVersion) {
        this.pakegeVersion = pakegeVersion;
    }

    //获取sim卡iccid
    @SuppressLint("MissingPermission")
    public String getIccid() {
        String iccid = "N/A";
        TelephonyManager telephonyManager = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        iccid = telephonyManager.getSimSerialNumber();
        return iccid == null ? "N|A" : iccid;
    }


    /**
     * @return 是否有网络
     */
    public boolean getNetWorkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    /**
     * 备份运行数据
     */
    public void saveBackeUp() {
        if (!TextUtils.isEmpty(BusApp.getPosManager().getLineNo()) && !BusApp.getPosManager().getLineNo().equals("000000") && !TextUtils.isEmpty(BusApp.getPosManager().getBusNo()) && !BusApp.getPosManager().getBusNo().equals("000000")) {
            try {
                MMKVManager.getInstance().put("appRunInfo", new Gson().toJson(BusApp.getPosManager()));
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }


    /**
     * tinker 加载查分包
     *
     * @param path
     */
    public void loadPatch(String path) {
        BusApp.getPosManager().setUpDateInfo("正在更新版本");
        String str = SharePatchFileUtil.getMD5(new File(path));
        MiLog.i("升级", "补丁包md5:" + str);
        TinkerInstaller.onReceiveUpgradePatch(getApplication(), path);
    }

    /**
     * tinker 清除查分包 还原为基础包
     */
    public void cleanParch() {
        BusApp.getPosManager().setUpDateInfo("更新失败,判定升级失败,还原版本,需重启");
        BusApp.getPosManager().setIsClean(true);
        MiLog.i("升级", "清除补丁");
        TinkerInstaller.cleanPatch(BusApp.getInstance().getApplication());
    }
}
