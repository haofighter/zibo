package com.szxb.zibo.record;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ManageCardRecord {
    @Id(autoincrement = true)
    Long id;

    String bizType;//11-司机考勤
    String transSeqId;//交易流水号,由设备前置定义,不可重复
    String signType;// 签到类型，in-签入，off-签退
    String transCityCode;//运营方编码，通过设备解析线路计费文件后上送。具体参考5.2.2线路信息运营方编码
    String acquirer;//首单方标示，通过设备解析线路计费文件后上送。具体参考5.2.2线路信息运营方编码
    String terminalId;// 机具设备终端编号，本笔消费发生时的psam卡终端号
    String psamCardId;//PSAM卡号
    String psamTerminalId;//PSAM终端号
    String lineId;// 线路Id，通过设备解析线路计费文件后上送。具体参考5.2.2线路信息线路id
    String transTime;//刷卡时间
    String refeNo;//终端流水号，在一段时间内必须唯一
    String veguckeNO;//车号
    String cardId;//司机卡号
    String jobNO;//员工号即driverId
    int UpStatus;//0  未上传  1 已上传
    int createTime;
    
    @Generated(hash = 1033293241)
    public ManageCardRecord() {
    }
    @Generated(hash = 1870881133)
    public ManageCardRecord(Long id, String bizType, String transSeqId,
            String signType, String transCityCode, String acquirer,
            String terminalId, String psamCardId, String psamTerminalId,
            String lineId, String transTime, String refeNo, String veguckeNO,
            String cardId, String jobNO, int UpStatus, int createTime) {
        this.id = id;
        this.bizType = bizType;
        this.transSeqId = transSeqId;
        this.signType = signType;
        this.transCityCode = transCityCode;
        this.acquirer = acquirer;
        this.terminalId = terminalId;
        this.psamCardId = psamCardId;
        this.psamTerminalId = psamTerminalId;
        this.lineId = lineId;
        this.transTime = transTime;
        this.refeNo = refeNo;
        this.veguckeNO = veguckeNO;
        this.cardId = cardId;
        this.jobNO = jobNO;
        this.UpStatus = UpStatus;
        this.createTime = createTime;
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
    public String getTransSeqId() {
        return this.transSeqId;
    }
    public void setTransSeqId(String transSeqId) {
        this.transSeqId = transSeqId;
    }
    public String getSignType() {
        return this.signType;
    }
    public void setSignType(String signType) {
        this.signType = signType;
    }
    public String getTransCityCode() {
        return this.transCityCode;
    }
    public void setTransCityCode(String transCityCode) {
        this.transCityCode = transCityCode;
    }
    public String getAcquirer() {
        return this.acquirer;
    }
    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }
    public String getTerminalId() {
        return this.terminalId;
    }
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
    public String getPsamCardId() {
        return this.psamCardId;
    }
    public void setPsamCardId(String psamCardId) {
        this.psamCardId = psamCardId;
    }
    public String getLineId() {
        return this.lineId;
    }
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
    public String getTransTime() {
        return this.transTime;
    }
    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
    public String getRefeNo() {
        return this.refeNo;
    }
    public void setRefeNo(String refeNo) {
        this.refeNo = refeNo;
    }
    public String getVeguckeNO() {
        return this.veguckeNO;
    }
    public void setVeguckeNO(String veguckeNO) {
        this.veguckeNO = veguckeNO;
    }
    public String getCardId() {
        return this.cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getJobNO() {
        return this.jobNO;
    }
    public void setJobNO(String jobNO) {
        this.jobNO = jobNO;
    }

    public int getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }
    public int getUpStatus() {
        return this.UpStatus;
    }
    public void setUpStatus(int UpStatus) {
        this.UpStatus = UpStatus;
    }
    public String getPsamTerminalId() {
        return this.psamTerminalId;
    }
    public void setPsamTerminalId(String psamTerminalId) {
        this.psamTerminalId = psamTerminalId;
    }

}
