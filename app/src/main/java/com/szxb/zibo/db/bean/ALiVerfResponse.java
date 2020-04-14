package com.szxb.zibo.db.bean;

/**
 * 作者：L on 2019-07-08 15:19
 * 云公交卡验码数据返回
 */

public class ALiVerfResponse {
    private int balance = 0;
    private int cost = 0;
    private String userID = "";
    private String cardID = "";
    private byte[] cardData = new byte[0];
    private String cardType = "";
    private byte[] message = new byte[0];
    private String uniqueID = "";
    private int tradeTime = 0;
    private int qrcodeType = 0;

    private int returnCode;
    private String description;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public byte[] getCardData() {
        return cardData;
    }

    public void setCardData(byte[] cardData) {
        this.cardData = cardData;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public int getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(int tradeTime) {
        this.tradeTime = tradeTime;
    }

    public int getQrcodeType() {
        return qrcodeType;
    }

    public void setQrcodeType(int qrcodeType) {
        this.qrcodeType = qrcodeType;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
