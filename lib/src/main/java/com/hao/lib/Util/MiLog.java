package com.hao.lib.Util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MiLog {
    static boolean isShowLog = true;
    static boolean isSaveLog = true;


    public static void i(String tag, String msg) {
        if (isShowLog) {
            Log.i(tag, msg);
        }
        if (isSaveLog) {
            File file = new File(Environment.getExternalStorageDirectory() + "/log/" + tag + "_" + DataUtils.getStringDateYMD() + ".txt");
            if (file.exists()) {
                String string = tag + "     " + msg;
                FileUtils.writeToFile(file, string);
            } else {
                String string = tag + "     " + msg;
                FileUtils.checkFile(file);
                FileUtils.byteToFile(string.getBytes(), file);
            }
        }
    }

    public static void clear(int time) {
        File file = new File(Environment.getExternalStorageDirectory() + "/log");
        try {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    long fileCreatTime = Long.parseLong(files[i].getName().split("_")[1].substring(0,8));
                    if (fileCreatTime - Long.parseLong(DataUtils.getStringDateYMD()) > time) {
                        files[i].delete();
                    }
                }
            }
        } catch (Exception e) {
            MiLog.i("日志清除失败", "原因" + e.getMessage());
            if (file.exists()) {
                FileUtils.delAllFile(file.getPath());
            }
        }
    }
}
