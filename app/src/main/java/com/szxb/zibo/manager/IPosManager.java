package com.szxb.zibo.manager;

public interface IPosManager {
    void loadFromPrefs(int city, String binName);

    String getLineName();

    void setLineName(String var1);

    String getLineNo();

    void setLineNo(String var1);

    int getBasePrice();

    void setBasePrice(int var1);



    int getmchTrxId();


    String getBusNo();

    void setBusNo(String bus_no);


    String getCityCode();

    void setCityCode(String cityCode);

    String getCompanyID();

    void setDriverNo(String no);

    String getDriverNo();

    String getPosSN();

    String getLastVersion();

    void setLastVersion(String version);

    String getBinVersion();


    /**
     * @param direction 行驶方向
     *                  00 上行
     *                  01 下行
     */
    void setDirection(String direction);

    String getDirection();

    /**
     * @param stationName 站点名
     */
    void setStationName(String stationName);

    String getStationName();

    /**
     * @param stationID 站点ID
     */
    void setStationID(int stationID);

    int getStationID();

    /**
     * @param location 经纬度
     */
    void setLocation(double[] location);

    double[] getLocation();


    //类型：单票、多票
    void setType(int type);

    int getType();



}
