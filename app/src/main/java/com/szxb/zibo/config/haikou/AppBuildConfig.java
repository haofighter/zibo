package com.szxb.zibo.config.haikou;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hao.lib.Util.FileUtils;

import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.haikou.param.BuildConfigParam;
import com.szxb.zibo.db.manage.DBCore;

import java.util.List;

/**
 * APP打包的时候生成的配置参数
 */
public class AppBuildConfig {


    //获取当前的配置信息
    public static void createConfig(int city) {
        String config = FileUtils.readAssetsFile("config.json", BusApp.getInstance().getApplicationContext());
        Log.i("config", config);
        List<BuildConfigParam> configParam = new Gson().fromJson(config, new TypeToken<List<BuildConfigParam>>() {
        }.getType());
//        if (DBCore.getDaoSession().getBuildConfigParamDao().queryBuilder().list().size() == 0) {
        BuildConfigParam configBean = configParam.get(city);
        configBean.setUpdateTime(System.currentTimeMillis());
        DBCore.getDaoSession().getBuildConfigParamDao().deleteAll();
        DBCore.getDaoSession().getBuildConfigParamDao().insert(configBean);
        Log.i("config", "当前配置:" + configBean.toString());
    }




}
