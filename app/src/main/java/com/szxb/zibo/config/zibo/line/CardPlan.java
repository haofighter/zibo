package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CardPlan {
    private String cardCaseNum;//卡方案编号
    String cardType;//卡类型编号char (4)
    String childCardType;//卡类型编号char (2)
    String parentCardType;//卡类型编号char (2)
    String cardPayRuleNum;//计费规则编号char (4)
    String rev;//预留char (4)
    String cardName;// H卡类名称
    String needCheckBlack;//L是否检查黑名单卡
    String needCheckStartTime;//E是否检查卡内有 效起始时间
    String needCheckEndTime;//T是否检查卡内有 效终止时间
    String balanceType;//C值类型标志  1 位数字 + 8位数字  0:无值类型 1:金额 2:次数 3:有效期 4:金额/次数双钱包  8位数字表示钱包上限(单位:分) 次数不判断上限
    String rechargeTag;//A充值标志 1 位数字 + 12 位 数字 0:不可充值  1:充金额:6 位金额值 + 6 位金额 2:充次数:4 位次数 + 8 位金额 3:充有效期:4 位天数 + 8 位金额 4:金额/次数双钱包
    String overdraw;//O是否允许透支/ 透支金额  0:不能透支 1:可透支  8 位数字为透支上限值(单位:分)
    String voiceType;//P读卡时发出的提示音
    String retrueMoney;//D退押金标志  0:不可退押金 1:可退押金
    String retrueMoneyTag;//R退卡余额标志  0:不能退 1:可以退
    String retrueMonthTag;//B退月票标志 0:不能退 1:可以退
    String recover;//X回收标志
    String serviceCharge;//I手续费收取  1 位数字 + 10 位 数字  0:不收手续费 1:收手续费:3 位起始月数+3 位截 至月数 + 4 位金额
    String rentMoney;//G手续费收取 0:不收租金1:收租金:3 位收租月数+3 位起始 月数+3 位截至月数 + 4 位金额
    String rentType;//J售卡模式 00:出租 01:销售 02:纪念
    String registered;//N记名标志 0:不记名 1:记名
    String giveWallet;//M是否赠送钱包  0: 不是 1:赠送钱包(月票) 2:赠送钱包(金额)
    String minMoney;//W是否控制卡内最 低余额  1 位数字 + 8 位数 字 0:不控制 1:控制最低余额
    String giveInterval;//S是否允许年 月票\赠送 钱包刷卡和 年月票\赠 送钱包刷卡 间隔 5 位数字 SXYYYY 参数，X 代表含义如下:>0:钱包>1:月票>2:年票>3:钱包 + 月票>4:钱包 + 年票 YYYY 为刷卡间隔时间，但是对于钱包
    String useInterval;//z刷卡间隔

    @Generated(hash = 451522688)
    public CardPlan(String cardCaseNum, String cardType, String childCardType, String parentCardType, String cardPayRuleNum, String rev,
            String cardName, String needCheckBlack, String needCheckStartTime, String needCheckEndTime, String balanceType, String rechargeTag,
            String overdraw, String voiceType, String retrueMoney, String retrueMoneyTag, String retrueMonthTag, String recover,
            String serviceCharge, String rentMoney, String rentType, String registered, String giveWallet, String minMoney, String giveInterval,
            String useInterval) {
        this.cardCaseNum = cardCaseNum;
        this.cardType = cardType;
        this.childCardType = childCardType;
        this.parentCardType = parentCardType;
        this.cardPayRuleNum = cardPayRuleNum;
        this.rev = rev;
        this.cardName = cardName;
        this.needCheckBlack = needCheckBlack;
        this.needCheckStartTime = needCheckStartTime;
        this.needCheckEndTime = needCheckEndTime;
        this.balanceType = balanceType;
        this.rechargeTag = rechargeTag;
        this.overdraw = overdraw;
        this.voiceType = voiceType;
        this.retrueMoney = retrueMoney;
        this.retrueMoneyTag = retrueMoneyTag;
        this.retrueMonthTag = retrueMonthTag;
        this.recover = recover;
        this.serviceCharge = serviceCharge;
        this.rentMoney = rentMoney;
        this.rentType = rentType;
        this.registered = registered;
        this.giveWallet = giveWallet;
        this.minMoney = minMoney;
        this.giveInterval = giveInterval;
        this.useInterval = useInterval;
    }

    @Generated(hash = 396376488)
    public CardPlan() {
    }

    public String getCardCaseNum() {
        return cardCaseNum;
    }

    public void setCardCaseNum(String cardCaseNum) {
        this.cardCaseNum = cardCaseNum;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getChildCardType() {
        return childCardType;
    }

    public void setChildCardType(String childCardType) {
        this.childCardType = childCardType;
    }

    public String getParentCardType() {
        return parentCardType;
    }

    public void setParentCardType(String parentCardType) {
        this.parentCardType = parentCardType;
    }

    public String getCardPayRuleNum() {
        return cardPayRuleNum;
    }

    public void setCardPayRuleNum(String cardPayRuleNum) {
        this.cardPayRuleNum = cardPayRuleNum;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getNeedCheckBlack() {
        return needCheckBlack;
    }

    public void setNeedCheckBlack(String needCheckBlack) {
        this.needCheckBlack = needCheckBlack;
    }

    public String getNeedCheckStartTime() {
        return needCheckStartTime;
    }

    public void setNeedCheckStartTime(String needCheckStartTime) {
        this.needCheckStartTime = needCheckStartTime;
    }

    public String getNeedCheckEndTime() {
        return needCheckEndTime;
    }

    public void setNeedCheckEndTime(String needCheckEndTime) {
        this.needCheckEndTime = needCheckEndTime;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getRechargeTag() {
        return rechargeTag;
    }

    public void setRechargeTag(String rechargeTag) {
        this.rechargeTag = rechargeTag;
    }

    public String getOverdraw() {
        return overdraw;
    }

    public void setOverdraw(String overdraw) {
        this.overdraw = overdraw;
    }

    public String getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(String voiceType) {
        this.voiceType = voiceType;
    }

    public String getRetrueMoney() {
        return retrueMoney;
    }

    public void setRetrueMoney(String retrueMoney) {
        this.retrueMoney = retrueMoney;
    }

    public String getRetrueMoneyTag() {
        return retrueMoneyTag;
    }

    public void setRetrueMoneyTag(String retrueMoneyTag) {
        this.retrueMoneyTag = retrueMoneyTag;
    }

    public String getRetrueMonthTag() {
        return retrueMonthTag;
    }

    public void setRetrueMonthTag(String retrueMonthTag) {
        this.retrueMonthTag = retrueMonthTag;
    }

    public String getRecover() {
        return recover;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getRentMoney() {
        return rentMoney;
    }

    public void setRentMoney(String rentMoney) {
        this.rentMoney = rentMoney;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getGiveWallet() {
        return giveWallet;
    }

    public void setGiveWallet(String giveWallet) {
        this.giveWallet = giveWallet;
    }

    public String getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(String minMoney) {
        this.minMoney = minMoney;
    }

    public String getGiveInterval() {
        return giveInterval;
    }

    public void setGiveInterval(String giveInterval) {
        this.giveInterval = giveInterval;
    }

    public String getUseInterval() {
        if(useInterval==null||useInterval.equals("")){
            useInterval="0";
        }
        return useInterval;
    }

    public void setUseInterval(String useInterval) {
        this.useInterval = useInterval;
    }
}
