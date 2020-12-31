package com.szxb.zibo.util;

import android.content.Context;
import android.content.res.AssetManager;


import com.szxb.lib.Util.MiLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * 作者：L on 2019-07-08 10:30
 */

public class Util {
    /**
     * @param fileName 文件名
     * @param context  .
     * @return 本地配置参数
     */
    public static String readAssetsFile(String fileName, Context context) {
        StringBuilder builder = new StringBuilder();
        AssetManager manager = context.getAssets();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(manager.open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            MiLog.i("错误", "Util(readAssetsFile.java:212)读取本地配置文件异常>>" + e.toString());
        }
        return builder.toString();
    }

    /**
     * 补足7位数字流水
     *
     * @param i
     * @return
     */
    public static String getNumSeq(int i) {
        return String.format("%08d", i);
    }


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
                ss[i] = (char) ('A' + Math.random() * 5);
            else if (f == 1)
                ss[i] = (char) ('a' + Math.random() * 5);
            else
                ss[i] = (char) ('0' + Math.random() * 10);
            i++;
        }
        return String.valueOf(ss);
    }


    /**
     * 获取小数点后6位的小数
     * @param dou
     * @return
     */
    public static double get6Double(double dou) {
        DecimalFormat df = new DecimalFormat("#.000000");
        String str = df.format(dou);
        return Double.parseDouble(str);
    }

}
