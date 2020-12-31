package com.szxb.zibo.util.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.szxb.zibo.base.BusApp;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.content.Context.MODE_MULTI_PROCESS;
import static android.content.Context.MODE_PRIVATE;

/**
 * 作者: Tangren on 2017/7/8
 * 包名：com.szxb.onlinbus.db.sp
 * 邮箱：996489865@qq.com
 * 保存秘钥/获取保存后的
 */

public class CommonSharedPreferences {

    public static final String FILE_NAME = "XB_BASE_PARAMS_TEMP";

    public static void put(String key, Object value) {

        SharedPreferences sp = BusApp.getInstance().getApplication().getSharedPreferences(FILE_NAME, Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sp.edit();

        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long)
            editor.putLong(key, (Long) value);
        else if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else editor.putString(key, (String) value);

        SharedPreferencesCompat.apply(editor);
    }


    public static Object get(String key, Object defaultValue) {
        SharedPreferences sp = BusApp.getInstance().getApplication().getSharedPreferences(FILE_NAME, Context.MODE_WORLD_READABLE);
        if (defaultValue instanceof Boolean)
            return sp.getBoolean(key, (Boolean) defaultValue);
        else if (defaultValue instanceof Long)
            return sp.getLong(key, (Long) defaultValue);
        else if (defaultValue instanceof Integer)
            return sp.getInt(key, (Integer) defaultValue);
        else return sp.getString(key, (String) defaultValue);
    }


    private static class SharedPreferencesCompat {

        private static final Method method = findApplyMethod();

        private static Method findApplyMethod() {
            Class clz = SharedPreferences.Editor.class;
            try {
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (method != null) {
                    method.invoke(editor);
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }

    }

}
