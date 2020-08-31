package com.szxb.zibo.moudle.function.gps;

import com.szxb.zibo.moudle.function.location.GPSToBaidu;
import com.szxb.zibo.util.Util;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ColletGpsInfo {
    double lat;
    double lng;
    double googlelat;
    double googlelng;
    @Id(autoincrement = true)
    Long id;
    Long staionNo;
    int diraction;

    public ColletGpsInfo(double lat, double lng, Long staionNo,
                         int diraction) {
        this.lat = lat;
        this.lng = lng;
        this.staionNo = staionNo;
        this.diraction = diraction;
        double[] add = GPSToBaidu.wgs2bd(Util.get6Double(lat), Util.get6Double(lng));
        googlelat = Util.get6Double(add[0]);
        googlelng = Util.get6Double(add[1]);
    }


    @Generated(hash = 201406919)
    public ColletGpsInfo(double lat, double lng, double googlelat, double googlelng,
                         Long id, Long staionNo, int diraction) {
        this.lat = lat;
        this.lng = lng;
        this.googlelat = googlelat;
        this.googlelng = googlelng;
        this.id = id;
        this.staionNo = staionNo;
        this.diraction = diraction;
    }


    @Generated(hash = 986227976)
    public ColletGpsInfo() {
    }


    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Long getStaionNo() {
        return this.staionNo;
    }

    public void setStaionNo(Long staionNo) {
        this.staionNo = staionNo;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDiraction() {
        return this.diraction;
    }

    public void setDiraction(int diraction) {
        this.diraction = diraction;
    }


    public double getGooglelat() {
        return this.googlelat;
    }


    public void setGooglelat(double googlelat) {
        this.googlelat = googlelat;
    }


    public double getGooglelng() {
        return this.googlelng;
    }


    public void setGooglelng(double googlelng) {
        this.googlelng = googlelng;
    }

}
