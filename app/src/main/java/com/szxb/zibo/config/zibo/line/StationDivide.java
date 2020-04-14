package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StationDivide {
    private String priceTypeNum;//消费点划分编号
    private String pirceAdd;//消费点
    String rev1;
    String rev2;
    String rev3;
    String rev4;
    String rev5;

    @Generated(hash = 541241905)
    public StationDivide(String priceTypeNum, String pirceAdd, String rev1,
            String rev2, String rev3, String rev4, String rev5) {
        this.priceTypeNum = priceTypeNum;
        this.pirceAdd = pirceAdd;
        this.rev1 = rev1;
        this.rev2 = rev2;
        this.rev3 = rev3;
        this.rev4 = rev4;
        this.rev5 = rev5;
    }

    @Generated(hash = 1930441506)
    public StationDivide() {
    }

    public String getPriceTypeNum() {
        return priceTypeNum;
    }

    public void setPriceTypeNum(String priceTypeNum) {
        this.priceTypeNum = priceTypeNum;
    }

    public String getPirceAdd() {
        return pirceAdd;
    }

    public void setPirceAdd(String pirceAdd) {
        this.pirceAdd = pirceAdd;
    }

    public String getRev1() {
        return rev1;
    }

    public void setRev1(String rev1) {
        this.rev1 = rev1;
    }

    public String getRev2() {
        return rev2;
    }

    public void setRev2(String rev2) {
        this.rev2 = rev2;
    }

    public String getRev3() {
        return rev3;
    }

    public void setRev3(String rev3) {
        this.rev3 = rev3;
    }

    public String getRev4() {
        return rev4;
    }

    public void setRev4(String rev4) {
        this.rev4 = rev4;
    }

    public String getRev5() {
        return rev5;
    }

    public void setRev5(String rev5) {
        this.rev5 = rev5;
    }

    @Override
    public String toString() {
        return "StationDivide{" +
                "priceTypeNum='" + priceTypeNum + '\'' +
                ", pirceAdd='" + pirceAdd + '\'' +
                '}';
    }
}
