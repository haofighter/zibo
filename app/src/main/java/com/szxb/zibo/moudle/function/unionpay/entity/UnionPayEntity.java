package com.szxb.zibo.moudle.function.unionpay.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 作者：Tangren on 2018-07-06
 * 包名：com.szxb.unionpay.entity
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@Entity
public class UnionPayEntity {

    @Id(autoincrement = true)
    private Long id;

    //商户号
    private String mchId;

    //银联设备号
    private String unionPosSn;

    //设备号
    private String posSn;

    //车辆号
    private String busNo;

    //应付金额
    private int totalFee;

    //实际扣款金额
    private int payFee;

    //扣款返回码
    private String resCode = "408";

    //交易时间
    private String time;

    //交易流水
    private int tradeSeq;

    //主账号
    private String mainCardNo;

    //批次号
    private String batchNum;

    //线路名称
    private String bus_line_name;

    //线路号
    private String bus_line_no;

    //司机编号
    private String driverNum;

    //分公司号
    private String unitno;

    //上传状态
    private Integer upStatus;

    //唯一标示：批次号+流水号
//    @Unique
    private String uniqueFlag;

    //55域数据
    private String tlv55;

    //单条支付数据
    private String singleData;

    //预留1(目前作为卡片序号cardNum)
    private String reserve_1;

    //预留2(目前作为银联卡与银联二维码的标志,null 表示银联卡)
    private String reserve_2;
    private String reserve_3;
    private String reserve_4;
    private String paySendDate;//向银联发送的请求数据
    private boolean isODA;//是否为oda交易
    private boolean isTrade;//是否交易完成
    private int repeatCount;//重复次数
    private int repeatTotleFree;//累计金额
    private long sendPayTime;//累计金额
    private String payStatus;//海口需要的状态


    String cardNum;
    String cardData;
    String allDriver;//完整司机号

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardData() {
        return cardData;
    }

    public void setCardData(String cardData) {
        this.cardData = cardData;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getRepeatTotleFree() {
        return repeatTotleFree;
    }

    public void setRepeatTotleFree(int repeatTotleFree) {
        this.repeatTotleFree = repeatTotleFree;
    }

    public boolean isODA() {
        return isODA;
    }

    public void setODA(boolean ODA) {
        isODA = ODA;
    }

    public boolean isTrade() {
        return isTrade;
    }

    public void setTrade(boolean trade) {
        isTrade = trade;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMchId() {
        return this.mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getUnionPosSn() {
        return this.unionPosSn;
    }

    public void setUnionPosSn(String unionPosSn) {
        this.unionPosSn = unionPosSn;
    }

    public String getPosSn() {
        return this.posSn;
    }

    public void setPosSn(String posSn) {
        this.posSn = posSn;
    }

    public String getBusNo() {
        return this.busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }


    public String getResCode() {
        return this.resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getMainCardNo() {
        return this.mainCardNo;
    }

    public void setMainCardNo(String mainCardNo) {
        this.mainCardNo = mainCardNo;
    }

    public String getBatchNum() {
        return this.batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getBus_line_name() {
        return this.bus_line_name;
    }

    public void setBus_line_name(String bus_line_name) {
        this.bus_line_name = bus_line_name;
    }

    public String getBus_line_no() {
        return this.bus_line_no;
    }

    public void setBus_line_no(String bus_line_no) {
        this.bus_line_no = bus_line_no;
    }

    public String getDriverNum() {
        return this.driverNum;
    }

    public void setDriverNum(String driverNum) {
        this.driverNum = driverNum;
    }

    public String getUnitno() {
        return this.unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public Integer getUpStatus() {
        return this.upStatus;
    }

    public void setUpStatus(Integer upStatus) {
        this.upStatus = upStatus;
    }

    public String getUniqueFlag() {
        return this.uniqueFlag;
    }

    public void setUniqueFlag(String uniqueFlag) {
        this.uniqueFlag = uniqueFlag;
    }

    public String getTlv55() {
        return this.tlv55;
    }

    public void setTlv55(String tlv55) {
        this.tlv55 = tlv55;
    }

    public String getSingleData() {
        return this.singleData;
    }

    public void setSingleData(String singleData) {
        this.singleData = singleData;
    }

    public String getReserve_1() {
        return this.reserve_1;
    }

    public void setReserve_1(String reserve_1) {
        this.reserve_1 = reserve_1;
    }

    public String getReserve_2() {
        return this.reserve_2;
    }

    public void setReserve_2(String reserve_2) {
        this.reserve_2 = reserve_2;
    }

    public String getReserve_3() {
        return this.reserve_3;
    }

    public void setReserve_3(String reserve_3) {
        this.reserve_3 = reserve_3;
    }

    public String getReserve_4() {
        return this.reserve_4;
    }

    public void setReserve_4(String reserve_4) {
        this.reserve_4 = reserve_4;
    }


    public String getTranType() {
        return this.tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getReserve() {
        return this.reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getBiztype() {
        return this.biztype;
    }

    public void setBiztype(String biztype) {
        this.biztype = biztype;
    }

    public String getAcquirer() {
        return this.acquirer;
    }

    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }

    public String getConductorid() {
        return this.conductorid;
    }

    public void setConductorid(String conductorid) {
        this.conductorid = conductorid;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransdata() {
        return this.transdata;
    }

    public void setTransdata(String transdata) {
        this.transdata = transdata;
    }

    public Long getCreattime() {
        return this.creattime;
    }

    public void setCreattime(Long creattime) {
        this.creattime = creattime;
    }

    public String getPaySendDate() {
        return this.paySendDate;
    }

    public void setPaySendDate(String paySendDate) {
        this.paySendDate = paySendDate;
    }

    public boolean getIsODA() {
        return this.isODA;
    }

    public void setIsODA(boolean isODA) {
        this.isODA = isODA;
    }

    public boolean getIsTrade() {
        return this.isTrade;
    }

    public void setIsTrade(boolean isTrade) {
        this.isTrade = isTrade;
    }

    public int getTotalFee() {
        return this.totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getTradeSeq() {
        return this.tradeSeq;
    }

    public void setTradeSeq(int tradeSeq) {
        this.tradeSeq = tradeSeq;
    }

    public long getSendPayTime() {
        return this.sendPayTime;
    }

    public void setSendPayTime(long sendPayTime) {
        this.sendPayTime = sendPayTime;
    }

    String tranType;//交易类型(1-刷卡 2-扫码)
    String reserve;//预留
    String biztype;//06 银联   07 ODA
    String acquirer;//收单单位标识
    String conductorid;//售票员 id
    String currency;//156人民币
    String transdata;//银联二维码原始信息
    Long creattime;//银联二维码原始信息

    @Generated(hash = 1496709299)
    public UnionPayEntity(Long id, String mchId, String unionPosSn, String posSn, String busNo,
                          int totalFee, int payFee, String resCode, String time, int tradeSeq, String mainCardNo,
                          String batchNum, String bus_line_name, String bus_line_no, String driverNum, String unitno,
                          Integer upStatus, String uniqueFlag, String tlv55, String singleData, String reserve_1,
                          String reserve_2, String reserve_3, String reserve_4, String paySendDate, boolean isODA,
                          boolean isTrade, int repeatCount, int repeatTotleFree, long sendPayTime, String payStatus,
                          String cardNum, String cardData, String allDriver, String tranType, String reserve,
                          String biztype, String acquirer, String conductorid, String currency, String transdata,
                          Long creattime) {
        this.id = id;
        this.mchId = mchId;
        this.unionPosSn = unionPosSn;
        this.posSn = posSn;
        this.busNo = busNo;
        this.totalFee = totalFee;
        this.payFee = payFee;
        this.resCode = resCode;
        this.time = time;
        this.tradeSeq = tradeSeq;
        this.mainCardNo = mainCardNo;
        this.batchNum = batchNum;
        this.bus_line_name = bus_line_name;
        this.bus_line_no = bus_line_no;
        this.driverNum = driverNum;
        this.unitno = unitno;
        this.upStatus = upStatus;
        this.uniqueFlag = uniqueFlag;
        this.tlv55 = tlv55;
        this.singleData = singleData;
        this.reserve_1 = reserve_1;
        this.reserve_2 = reserve_2;
        this.reserve_3 = reserve_3;
        this.reserve_4 = reserve_4;
        this.paySendDate = paySendDate;
        this.isODA = isODA;
        this.isTrade = isTrade;
        this.repeatCount = repeatCount;
        this.repeatTotleFree = repeatTotleFree;
        this.sendPayTime = sendPayTime;
        this.payStatus = payStatus;
        this.cardNum = cardNum;
        this.cardData = cardData;
        this.allDriver = allDriver;
        this.tranType = tranType;
        this.reserve = reserve;
        this.biztype = biztype;
        this.acquirer = acquirer;
        this.conductorid = conductorid;
        this.currency = currency;
        this.transdata = transdata;
        this.creattime = creattime;
    }

    @Generated(hash = 14483869)
    public UnionPayEntity() {
    }

    public int getPayFee() {
        return payFee;
    }

    public void setPayFee(int payFee) {
        this.payFee = payFee;
    }

    public String getAllDriver() {
        return this.allDriver;
    }

    public void setAllDriver(String allDriver) {
        this.allDriver = allDriver;
    }

    public String getPayStatus() {
        return this.payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
