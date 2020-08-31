package com.szxb.zibo.moudle.function.location;


import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.hao.lib.Util.MiLog;
import com.hao.lib.base.Rx.Rx;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.config.zibo.line.StationName;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.util.Util;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by Evergarden on 2018/1/14.
 */

public class GPSEvent {

    private static String TAG = "GPS";
    private static LocationService locationService;
    private static LocationClient mLocationClient;
    private static long locationTime = 0;
    public static BDLocation bdLocation = null;
    public static GPSEntity entity = null;
    static int nowStation = 0;

    //使用百度定位服务
    public static void GPSEventOpen() {
        if (!isOPen(BusApp.getInstance())) {
            openGPS(BusApp.getInstance());
        }

        if (BusApp.getPosManager().getLineType().equals("O")) {
            return;
        }

        locationService = new LocationService(BusApp.getInstance());
        mLocationClient = new LocationClient(BusApp.getInstance());

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//        option.setCoorType("gcj02");
//        option.setCoorType("bd09");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(3000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效\

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(true);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false


        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(mListener);
        mLocationClient.start();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - locationTime > 60000) {
                    Log.w("GPS定位未启用", "GPS模块未启用");
                    BusApp.getPosManager().setGPSSTATUS(-1);
//                    mLocationClient.restart();
                }
            }
        }, 0, 2, TimeUnit.SECONDS);

    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法
     *
     */
    private static BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                bdLocation = location;
                locationTime = System.currentTimeMillis();
//                GPSEntity gpsEntity = new GPSEntity(location.getLocTypeDescription(), location.getLatitude(),
//                        location.getLongitude(), location.getRadius());

                GPSEntity gpsEntity = new GPSEntity(location.getLocTypeDescription(), location.getLatitude(),
                        location.getLongitude(), location.getRadius());


                //用于设置百度定位识别的卫星数
                if (location.getLocType() != 161) {
                    BusApp.getPosManager().setGPSSTATUS(location.getSatelliteNumber());
                } else {
                    BusApp.getPosManager().setGPSSTATUS(20);
                }

                if (BusApp.getPosManager().getOperate() == 0) {//自动
                    Calculation(gpsEntity);
                }

            } else {
                BusApp.getPosManager().setGPSSTATUS(-1);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.w("GPS定位失败", "站点坐标" + s);
            super.onConnectHotSpotMessage(s, i);
        }
    };

    // 计算距离
    public static void Calculation(GPSEntity gpsEntity) {
        try {
            List<StationName> stationNames = DBManagerZB.checkStation(BusApp.getPosManager().getDirection());

            for (StationName stationName : stationNames) {
                //坐标转换
                double[] add = GPSToBaidu.wgs2bd(Util.get6Double(stationName.getLat()), Util.get6Double(stationName.getLon()));
                LatLng gp1 = new LatLng(Util.get6Double(add[0]), Util.get6Double(add[1]));

                LatLng gp2 = new LatLng(Util.get6Double(gpsEntity.getLatitude()), Util.get6Double(gpsEntity.getLongitude()));
                double length1 = DistanceUtil.getDistance(gp1, gp2);

                //定位在站点附近50m即为到达此站点
                if (length1 < 50) {
                    if (BusApp.getPosManager().getStationID() != stationName.getStationNoInt()) {
                        BusApp.getPosManager().setStationID(stationName.getStationNoInt());
                        BusApp.getPosManager().setBasePrice(PraseLine.getMorePayPrice(null, true, true));
                        SoundPoolUtil.play(VoiceConfig.didi);
                        if (stationName.getStationNoInt() == DBManagerZB.checkStationMax(BusApp.getPosManager().getDirection()).getStationNoInt()) {//表示到了终点
                            BusApp.getPosManager().setDirection(BusApp.getPosManager().getDirection().endsWith("0001") ? "0002" : "0001");
                            StationName stationNameFrist=  DBManagerZB.checkStation(BusApp.getPosManager().getDirection()).get(0);
                            double[] firstStation = GPSToBaidu.wgs2bd(Util.get6Double(stationNameFrist.getLat()), Util.get6Double(stationNameFrist.getLon()));
                            gpsEntity.setLatitude(firstStation[0]);
                            gpsEntity.setLongitude(firstStation[1]);
//                            BusApp.getPosManager().setStationName(stationName.getStationName());
                            Calculation(gpsEntity);//切换方向后重新定位站点
                        } else {
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i("错误", e.getMessage());
        }
    }


    //关闭GPS
    public static void GPSClose() {
        if (locationService != null) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
            locationService = null;
            mLocationClient = null;
        }
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        //boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps) {
            return true;
        }

        return false;
    }


    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    public static final void openGPS(Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { // if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

}