package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ContinuousRule {
    String lineNum;
    String stationNum;//站点编号
    String longitude;//经度
    String latitude;//纬度

    @Generated(hash = 817192608)
    public ContinuousRule(String lineNum, String stationNum, String longitude,
            String latitude) {
        this.lineNum = lineNum;
        this.stationNum = stationNum;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Generated(hash = 598433655)
    public ContinuousRule() {
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getStationNum() {
        return stationNum;
    }

    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ContinuousRule{" +
                "lineNum='" + lineNum + '\'' +
                ", stationNum='" + stationNum + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
