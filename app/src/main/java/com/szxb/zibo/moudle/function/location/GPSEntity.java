package com.szxb.zibo.moudle.function.location;

/**
 * Created by Evergarden on 2018/1/14.
 */

public class GPSEntity {

    private int stationNo;
    private String LocTypeDescription;//定位类型
    private double Latitude;//经度
    private double Longitude;//纬度
    private float Radius;//半径
    private String mark;//当前mark

    private double len;//距离

    public int getStationNo() {
        return stationNo;
    }

    public void setStationNo(int stationNo) {
        this.stationNo = stationNo;
    }

    public String getLocTypeDescription() {
        return LocTypeDescription;
    }

    public void setLocTypeDescription(String locTypeDescription) {
        LocTypeDescription = locTypeDescription;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public float getRadius() {
        return Radius;
    }

    public void setRadius(float radius) {
        Radius = radius;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public double getLen() {
        return len;
    }

    public void setLen(double len) {
        this.len = len;
    }

    public GPSEntity(int stationNo, String locTypeDescription, double latitude, double longitude, float radius, String mark, float len) {
        this.stationNo = stationNo;
        LocTypeDescription = locTypeDescription;
        Latitude = latitude;
        Longitude = longitude;
        Radius = radius;
        this.mark = mark;
        this.len = len;
    }

    public GPSEntity(String locTypeDescription, double latitude, double longitude, float radius) {
        LocTypeDescription = locTypeDescription;
        Latitude = latitude;
        Longitude = longitude;
        Radius = radius;
    }

    public GPSEntity(int stationNo, double latitude, double longitude, float radius, String mark) {
        this.stationNo = stationNo;
        Latitude = latitude;
        Longitude = longitude;
        Radius = radius;
        this.mark=mark;
    }


    public GPSEntity(int stationNo, double latitude, double longitude, float radius, String mark, double len) {
        this.stationNo = stationNo;
        Latitude = latitude;
        Longitude = longitude;
        Radius = radius;
        this.mark = mark;
        this.len = len;
    }

    public GPSEntity() {
    }

    @Override
    public String toString() {
        return "GPSEntity{" +
                "stationNo=" + stationNo +
                ", LocTypeDescription='" + LocTypeDescription + '\'' +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", Radius=" + Radius +
                '}';
    }
}
