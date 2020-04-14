package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FarePlan {
    private String pricCaseNum;//计价方案编号
    private String pricePayCaseNum;   //基本价格方案编号
    private String cardCaseNUm;//卡方案编号
    String rev1;
    String rev2;
    String rev3;
    String rev4;
    String rev5;
    @Generated(hash = 1654964936)
    public FarePlan(String pricCaseNum, String pricePayCaseNum, String cardCaseNUm,
            String rev1, String rev2, String rev3, String rev4, String rev5) {
        this.pricCaseNum = pricCaseNum;
        this.pricePayCaseNum = pricePayCaseNum;
        this.cardCaseNUm = cardCaseNUm;
        this.rev1 = rev1;
        this.rev2 = rev2;
        this.rev3 = rev3;
        this.rev4 = rev4;
        this.rev5 = rev5;
    }
    @Generated(hash = 933415710)
    public FarePlan() {
    }
    public String getPricCaseNum() {
        return this.pricCaseNum;
    }
    public void setPricCaseNum(String pricCaseNum) {
        this.pricCaseNum = pricCaseNum;
    }
    public String getPricePayCaseNum() {
        return this.pricePayCaseNum;
    }
    public void setPricePayCaseNum(String pricePayCaseNum) {
        this.pricePayCaseNum = pricePayCaseNum;
    }
    public String getCardCaseNUm() {
        return this.cardCaseNUm;
    }
    public void setCardCaseNUm(String cardCaseNUm) {
        this.cardCaseNUm = cardCaseNUm;
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
        return "FarePlan{" +
                "pricCaseNum='" + pricCaseNum + '\'' +
                ", pricePayCaseNum='" + pricePayCaseNum + '\'' +
                ", cardCaseNUm='" + cardCaseNUm + '\'' +
                '}';
    }
}
