package com.szxb.java8583.module;

/**
 * 作者：Tangren on 2018-07-05
 * 包名：com.szxb.java8583.module
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class BusCard {

    //金额
    private int money;

    //主账号
    private String mainCardNo;

    //卡序号
    private String cardNum;

    //55域数据
    private String tlv55;

    //流水号1-999999
    private long tradeSeq;

    //mac,签到获得
    private String macKey;

    //2磁道数据
    private String magTrackData;


    //出站时间
    private String busOutStationTime;


    public String getMagTrackData() {
        return magTrackData;
    }

    public void setMagTrackData(String magTrackData) {
        this.magTrackData = magTrackData;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMainCardNo() {
        return mainCardNo;
    }

    public void setMainCardNo(String mainCardNo) {
        this.mainCardNo = mainCardNo;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getTlv55() {
        return tlv55;
    }

    public void setTlv55(String tlv55) {
        this.tlv55 = tlv55;
    }

    public long getTradeSeq() {
        return tradeSeq;
    }

    public void setTradeSeq(long tradeSeq) {
        this.tradeSeq = tradeSeq;
    }

    public String getMacKey() {
        return macKey;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    public String getBusOutStationTime() {
        return busOutStationTime;
    }

    public void setBusOutStationTime(String busOutStationTime) {
        this.busOutStationTime = busOutStationTime;
    }
}
