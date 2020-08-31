package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StationPayPrice {
    String priceTypeNum;//上下行方向
    int up;//上车站点号
    int down;//下车站点号
    long price;//价格

    @Generated(hash = 1412983864)
    public StationPayPrice(String priceTypeNum, int up, int down, long price) {
        this.priceTypeNum = priceTypeNum;
        this.up = up;
        this.down = down;
        this.price = price;
    }

    @Generated(hash = 1008526239)
    public StationPayPrice() {
    }

    @Override
    public String toString() {
        return "StationPayPrice{" +
                "上下行='" + priceTypeNum + '\'' +
                ", 上车站=" + up +
                ", 下车站=" + down +
                ", 价格=" + price +
                '}' + "\n";
    }

    public String getPriceTypeNum() {
        return this.priceTypeNum;
    }

    public void setPriceTypeNum(String priceTypeNum) {
        this.priceTypeNum = priceTypeNum;
    }

    public int getUp() {
        return this.up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return this.down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public long getPrice() {
        return this.price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
