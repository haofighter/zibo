package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class StationName {
    @Id(autoincrement = true)
            Long id;
    String priceTypeNum;//消费点划分编号
    String stationName;//运营点汉字显示
    float lon;//经度
    float lat;//纬度
    String stationNo;//站点号
    String lineNo;//线路号
    @Generated(hash = 925037881)
    public StationName(Long id, String priceTypeNum, String stationName, float lon,
            float lat, String stationNo, String lineNo) {
        this.id = id;
        this.priceTypeNum = priceTypeNum;
        this.stationName = stationName;
        this.lon = lon;
        this.lat = lat;
        this.stationNo = stationNo;
        this.lineNo = lineNo;
    }
    @Generated(hash = 1005630900)
    public StationName() {
    }
    public String getPriceTypeNum() {
        return this.priceTypeNum;
    }
    public void setPriceTypeNum(String priceTypeNum) {
        this.priceTypeNum = priceTypeNum;
    }
    public String getStationName() {
        return this.stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public float getLon() {
        return this.lon;
    }
    public void setLon(float lon) {
        this.lon = lon;
    }
    public float getLat() {
        return this.lat;
    }
    public void setLat(float lat) {
        this.lat = lat;
    }
    public String getStationNo() {
        return this.stationNo;
    }
    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }
    public String getLineNo() {
        return this.lineNo;
    }
    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public void setLon(String lon) {
        this.lon=Float.parseFloat(lon);
    }

    public void setLat(String lat) {
        this.lat = Float.parseFloat(lat);
    }

    public int getStationNoInt(){
        try {
            return Integer.parseInt(stationNo);
        }catch (Exception e){
            return 1;
        }
    }

    @Override
    public String toString() {
        return "StationName{" +
                "方向='" + priceTypeNum + '\'' +
                ", 站点名='" + stationName + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", stationNo='" + stationNo + '\'' +
                ", lineNo='" + lineNo + '\'' +
                ",stationNoInt"+getStationNoInt()+
                '}';
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
