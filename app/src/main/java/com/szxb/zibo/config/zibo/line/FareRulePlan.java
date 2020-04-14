package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FareRulePlan {
    String fareRulePlanNum;
    String fareRuleType; //O 一票制  P 前后门刷卡分段 X 阶梯性分段
    String fareRulePrice;
    String unkownString;
    String unkownTag;
    @Generated(hash = 1923568381)
    public FareRulePlan(String fareRulePlanNum, String fareRuleType,
            String fareRulePrice, String unkownString, String unkownTag) {
        this.fareRulePlanNum = fareRulePlanNum;
        this.fareRuleType = fareRuleType;
        this.fareRulePrice = fareRulePrice;
        this.unkownString = unkownString;
        this.unkownTag = unkownTag;
    }
    @Generated(hash = 814133922)
    public FareRulePlan() {
    }
    public String getFareRulePlanNum() {
        return this.fareRulePlanNum;
    }
    public void setFareRulePlanNum(String fareRulePlanNum) {
        this.fareRulePlanNum = fareRulePlanNum;
    }
    public String getFareRuleType() {
        return this.fareRuleType;
    }
    public void setFareRuleType(String fareRuleType) {
        this.fareRuleType = fareRuleType;
    }
    public String getFareRulePrice() {
        return this.fareRulePrice;
    }
    public void setFareRulePrice(String fareRulePrice) {
        this.fareRulePrice = fareRulePrice;
    }
    public String getUnkownString() {
        return this.unkownString;
    }
    public void setUnkownString(String unkownString) {
        this.unkownString = unkownString;
    }
    public String getUnkownTag() {
        return this.unkownTag;
    }
    public void setUnkownTag(String unkownTag) {
        this.unkownTag = unkownTag;
    }


    @Override
    public String toString() {
        return "FareRulePlan{" +
                "fareRulePlanNum='" + fareRulePlanNum + '\'' +
                ", fareRuleType='" + fareRuleType + '\'' +
                ", fareRulePrice='" + fareRulePrice + '\'' +
                ", unkownString='" + unkownString + '\'' +
                ", unkownTag='" + unkownTag + '\'' +
                '}'+"\n";
    }
}
