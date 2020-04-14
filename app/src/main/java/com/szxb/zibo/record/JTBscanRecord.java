package com.szxb.zibo.record;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JTBscanRecord {
    @Id(autoincrement = true)
    Long id;

    String bizType;
    String acquirer;
    String terseno;
    String terminalId;
    String driverId;
    String conductorId;
    String transTime;
    String transCityCode;
    String lineId;
    String lineName;
    String station;
    String stationName;
    String currency;
    String chargeType;
    int totalFee;
    int payFee;
    String qrCode;
    String busno;
    String isUpload;
    String qrCodeData;
    Long createtime;
    int repeatCount;
    String bizData;
    String allDriver;//完整司机号
    String cardNo;//二维码卡号



    @Generated(hash = 1322767162)
    public JTBscanRecord(Long id, String bizType, String acquirer, String terseno,
            String terminalId, String driverId, String conductorId,
            String transTime, String transCityCode, String lineId, String lineName,
            String station, String stationName, String currency, String chargeType,
            int totalFee, int payFee, String qrCode, String busno, String isUpload,
            String qrCodeData, Long createtime, int repeatCount, String bizData,
            String allDriver, String cardNo) {
        this.id = id;
        this.bizType = bizType;
        this.acquirer = acquirer;
        this.terseno = terseno;
        this.terminalId = terminalId;
        this.driverId = driverId;
        this.conductorId = conductorId;
        this.transTime = transTime;
        this.transCityCode = transCityCode;
        this.lineId = lineId;
        this.lineName = lineName;
        this.station = station;
        this.stationName = stationName;
        this.currency = currency;
        this.chargeType = chargeType;
        this.totalFee = totalFee;
        this.payFee = payFee;
        this.qrCode = qrCode;
        this.busno = busno;
        this.isUpload = isUpload;
        this.qrCodeData = qrCodeData;
        this.createtime = createtime;
        this.repeatCount = repeatCount;
        this.bizData = bizData;
        this.allDriver = allDriver;
        this.cardNo = cardNo;
    }

    @Generated(hash = 1939293323)
    public JTBscanRecord() {
    }

   

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBizType() {
        return this.bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getAcquirer() {
        return this.acquirer;
    }

    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }

    public String getTerseno() {
        return this.terseno;
    }

    public void setTerseno(String terseno) {
        this.terseno = terseno;
    }

    public String getTerminalId() {
        return this.terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getDriverId() {
        return this.driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getConductorId() {
        return this.conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public String getTransTime() {
        return this.transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransCityCode() {
        return this.transCityCode;
    }

    public void setTransCityCode(String transCityCode) {
        this.transCityCode = transCityCode;
    }

    public String getLineId() {
        return this.lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return this.lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStation() {
        return this.station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getChargeType() {
        return this.chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public int getTotalFee() {
        return this.totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getPayFee() {
        return this.payFee;
    }

    public void setPayFee(int payFee) {
        this.payFee = payFee;
    }

    public String getQrCode() {
        return this.qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBusno() {
        return this.busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    public String getQrCodeData() {
        return this.qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public Long getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public int getRepeatCount() {
        return this.repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public String getBizData() {
        return this.bizData;
    }

    public void setBizData(String bizData) {
        this.bizData = bizData;
    }

    public String getAllDriver() {
        return this.allDriver;
    }

    public void setAllDriver(String allDriver) {
        this.allDriver = allDriver;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }


}
