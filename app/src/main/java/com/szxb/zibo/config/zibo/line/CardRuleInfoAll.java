package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CardRuleInfoAll {
    private String cardCaseNum;//卡方案编号
    private String cardRuleInfos;//卡类型 及计费规则信息
    @Generated(hash = 1033528964)
    public CardRuleInfoAll(String cardCaseNum, String cardRuleInfos) {
        this.cardCaseNum = cardCaseNum;
        this.cardRuleInfos = cardRuleInfos;
    }
    @Generated(hash = 449506466)
    public CardRuleInfoAll() {
    }
    public String getCardCaseNum() {
        return this.cardCaseNum;
    }
    public void setCardCaseNum(String cardCaseNum) {
        this.cardCaseNum = cardCaseNum;
    }
    public String getCardRuleInfos() {
        return this.cardRuleInfos;
    }
    public void setCardRuleInfos(String cardRuleInfos) {
        this.cardRuleInfos = cardRuleInfos;
    }

}
