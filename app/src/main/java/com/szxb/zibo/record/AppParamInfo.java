package com.szxb.zibo.record;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AppParamInfo {
    @Unique
    long runId;
    String driverNo;
    String linNo;
    String linName;
    String busNo;
    int basePrice;
    String linVer;
    String lineType;

    @Generated(hash = 1398782373)
    public AppParamInfo(long runId, String driverNo, String linNo, String linName,
            String busNo, int basePrice, String linVer, String lineType) {
        this.runId = runId;
        this.driverNo = driverNo;
        this.linNo = linNo;
        this.linName = linName;
        this.busNo = busNo;
        this.basePrice = basePrice;
        this.linVer = linVer;
        this.lineType = lineType;
    }
    @Generated(hash = 719964441)
    public AppParamInfo() {
    }

    public long getRunId() {
        return this.runId;
    }
    public void setRunId(long runId) {
        this.runId = runId;
    }
    public String getDriverNo() {
        return this.driverNo;
    }
    public void setDriverNo(String driverNo) {
        this.driverNo = driverNo;
    }
    public String getLinNo() {
        return this.linNo;
    }
    public void setLinNo(String linNo) {
        this.linNo = linNo;
    }
    public String getLinName() {
        return this.linName;
    }
    public void setLinName(String linName) {
        this.linName = linName;
    }
    public String getBusNo() {
        return this.busNo;
    }
    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }
    public int getBasePrice() {
        return this.basePrice;
    }
    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }
    public String getLinVer() {
        return this.linVer;
    }
    public void setLinVer(String linVer) {
        this.linVer = linVer;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }
}
