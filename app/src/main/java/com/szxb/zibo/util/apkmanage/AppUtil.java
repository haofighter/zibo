package com.szxb.zibo.util.apkmanage;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.util.Log;

import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.util.sp.CommonSharedPreferences;

import java.util.List;
import java.util.Map;

public class AppUtil {
    /**
     * 随机字符串
     *
     * @param length
     * @return
     */
    public static String Random(int length) {
        char[] ss = new char[length];
        int i = 0;
        while (i < length) {
            int f = (int) (Math.random() * 5);
            if (f == 0)
                ss[i] = (char) ('A' + Math.random() * 26);
            else if (f == 1)
                ss[i] = (char) ('a' + Math.random() * 26);
            else
                ss[i] = (char) ('0' + Math.random() * 10);
            i++;
        }
        return String.valueOf(ss);
    }


    //获取随机字符串 hex
    public static String hexRandomStr(int lenth) {
        String[] str = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E"};
        String result = "";
        for (int i = 0; i < lenth; i++) {
            result += str[(int) (Math.random() * str.length)];
        }

        Log.i("随机字符串", result);
        return result;
    }


    /**
     * 是否有网络
     *
     * @return boolean
     */
    public static boolean checkNetStatus() {
        ConnectivityManager cm = (ConnectivityManager) BusApp.getInstance().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Boolean isWifiConn = networkInfo.isConnected();
        NetworkInfo networkInfo_ = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        Boolean isMobileConn = networkInfo_.isConnected();
        return isWifiConn || isMobileConn;
    }

    /**
     * 判断当前网络是否可用(通用方法)
     * 耗时12秒
     *
     * @return
     */
    public static boolean isNetPingUsable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("ping -c 3 www.baidu.com");
            int ret = process.waitFor();
            if (ret == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getTranNum() {
        int tranNum = (int) CommonSharedPreferences.get("tranNun", 0);
        if (tranNum >= 99999) {
            tranNum = 0;
        }
        tranNum++;
        CommonSharedPreferences.put("tranNun", tranNum);
        return tranNum;
    }




    /**
     * 2017年10月19日19:26:03
     */
    //卸载所有第三方apk
    public static void unInstallAPk(String name) {
        List<Map> ar = BusApp.getInstance().getApkUtil().getThirdPartyInformation(0);
        for (int i = 0; i < ar.size(); i++) {
            Map m = ar.get(i);
            String packageName = m.get("packageName").toString();
            if(packageName.equals(name)) {
                BusApp.getInstance().getApkUtil().uninstallApk(packageName);
            }
        }
    }

    public static boolean getAppProcessName(String appName) {
        //当前应用pid
        final PackageManager packageManager = BusApp.getInstance().getApplication().getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // get all apps
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        for (int i = 0; i < apps.size(); i++) {
            String name = apps.get(i).activityInfo.packageName;
            if (name.equals(appName)) {
                return true;
            }
        }
        return false;
    }

}