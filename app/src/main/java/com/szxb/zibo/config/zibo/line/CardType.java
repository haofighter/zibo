package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CardType {
    String cardTypeNum;//卡类型编号
    String deposit;//押金
    String cost;//费用
    String effectiveTime;//有效时段

    @Generated(hash = 1838109231)
    public CardType(String cardTypeNum, String deposit, String cost,
            String effectiveTime) {
        this.cardTypeNum = cardTypeNum;
        this.deposit = deposit;
        this.cost = cost;
        this.effectiveTime = effectiveTime;
    }

    @Generated(hash = 1248446024)
    public CardType() {
    }

    public String getCardTypeNum() {
        return cardTypeNum;
    }

    public void setCardTypeNum(String cardTypeNum) {
        this.cardTypeNum = cardTypeNum;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    @Override
    public String toString() {
        return "CardType{" +
                "cardTypeNum='" + cardTypeNum + '\'' +
                ", deposit='" + deposit + '\'' +
                ", cost='" + cost + '\'' +
                ", effectiveTime='" + effectiveTime + '\'' +
                '}';
    }
}
