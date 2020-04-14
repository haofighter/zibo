package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BasicFare {
    private String basepricCaseNum;//基本价格方案编号 等同于  计价方案信息基 本价格方案编号
    private String price;//单位票价
    private String priceTypeNum;//消费点划分编号
    private String prices;
    String rev1;
    String rev2;
    String rev3;
    String rev4;
    String rev5;
    @Generated(hash = 609608400)
    public BasicFare(String basepricCaseNum, String price, String priceTypeNum,
            String prices, String rev1, String rev2, String rev3, String rev4,
            String rev5) {
        this.basepricCaseNum = basepricCaseNum;
        this.price = price;
        this.priceTypeNum = priceTypeNum;
        this.prices = prices;
        this.rev1 = rev1;
        this.rev2 = rev2;
        this.rev3 = rev3;
        this.rev4 = rev4;
        this.rev5 = rev5;
    }
    @Generated(hash = 929477455)
    public BasicFare() {
    }
    public String getBasepricCaseNum() {
        return this.basepricCaseNum;
    }
    public void setBasepricCaseNum(String basepricCaseNum) {
        this.basepricCaseNum = basepricCaseNum;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getPriceTypeNum() {
        return this.priceTypeNum;
    }
    public void setPriceTypeNum(String priceTypeNum) {
        this.priceTypeNum = priceTypeNum;
    }
    public String getPrices() {
        return this.prices;
    }
    public void setPrices(String prices) {
        this.prices = prices;
    }
    public String getRev1() {
        return this.rev1;
    }
    public void setRev1(String rev1) {
        this.rev1 = rev1;
    }
    public String getRev2() {
        return this.rev2;
    }
    public void setRev2(String rev2) {
        this.rev2 = rev2;
    }
    public String getRev3() {
        return this.rev3;
    }
    public void setRev3(String rev3) {
        this.rev3 = rev3;
    }
    public String getRev4() {
        return this.rev4;
    }
    public void setRev4(String rev4) {
        this.rev4 = rev4;
    }
    public String getRev5() {
        return this.rev5;
    }
    public void setRev5(String rev5) {
        this.rev5 = rev5;
    }


    @Override
    public String toString() {
        return "BasicFare{" +
                "basepricCaseNum='" + basepricCaseNum + '\'' +
                ", price='" + price + '\'' +
                ", priceTypeNum='" + priceTypeNum + '\'' +
                ", prices='" + prices + '\'' +
                '}';
    }
}
