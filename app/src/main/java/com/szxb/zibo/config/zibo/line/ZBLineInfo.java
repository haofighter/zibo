package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ZBLineInfo {
    /**
     * acnt : 01
     * routeversion : 20190813122203
     * routeno : 010001
     * routename : 博山至张店
     */

    private String acnt;
    private String routeversion;
    @Unique
    private String routeno;
    private String routename;
    String rev1;
    String rev2;
    String rev3;
    String rev4;
    String rev5;
    @Generated(hash = 1752145834)
    public ZBLineInfo(String acnt, String routeversion, String routeno,
            String routename, String rev1, String rev2, String rev3, String rev4,
            String rev5) {
        this.acnt = acnt;
        this.routeversion = routeversion;
        this.routeno = routeno;
        this.routename = routename;
        this.rev1 = rev1;
        this.rev2 = rev2;
        this.rev3 = rev3;
        this.rev4 = rev4;
        this.rev5 = rev5;
    }
    @Generated(hash = 1788257911)
    public ZBLineInfo() {
    }
    public String getAcnt() {
        return this.acnt;
    }
    public void setAcnt(String acnt) {
        this.acnt = acnt;
    }
    public String getRouteversion() {
        return this.routeversion;
    }
    public void setRouteversion(String routeversion) {
        this.routeversion = routeversion;
    }
    public String getRouteno() {
        return this.routeno;
    }
    public void setRouteno(String routeno) {
        this.routeno = routeno;
    }
    public String getRoutename() {
        return this.routename;
    }
    public void setRoutename(String routename) {
        this.routename = routename;
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


}
