package com.szxb.zibo.moudle.function.unionpay.config;

import com.szxb.java8583.module.manager.IManager;
import com.szxb.zibo.BuildConfig;
import com.szxb.zibo.moudle.function.unionpay.UnionUtil;
import com.szxb.zibo.util.sp.CommonSharedPreferences;

/**
 * 作者：Tangren on 2018-07-06
 * 包名：com.szxb.unionpay
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class UnionPayManager implements IManager {

    private int tradeSeq;

    /**
     * 商户号
     */
    private String mch_id;

    /**
     * 银联分配的秘钥
     */
    private String key;

    /**
     * 批次号(签到获取)
     */
    private String batchNum;

    /**
     * mac秘钥(签到获取)
     */
    private String macKey;

    /**
     * 银联分配
     */
    private String TPDU = "6006010000";

    /**
     * 银联分配设备号
     */
    private String posSN;

    /**
     * 操作员编号(3位)
     */
    private String operator;

    /**
     * IP
     */
    private String unionPayUrl;

    /**
     * aid下载列表
     */
    private String aidIndexList;

    /**
     * 上次更新日期
     */
    private long lastUpdateTime = 0;

    @Override
    public void loadFromPrefs() {
        mch_id = UnionConfig.getMch();
        String binName = BuildConfig.BIN_NAME;
        TPDU = "6005010000";
        tradeSeq = UnionConfig.tradeSeq();
        key = UnionConfig.key();
        posSN = UnionConfig.getUnionPonSn();
        unionPayUrl = UnionConfig.getUnionPayUrl();
        operator = UnionConfig.operatorNumber();
        macKey = UnionConfig.macKey();
        batchNum = UnionConfig.batchNum();
        aidIndexList = UnionConfig.getAidIndexList();
        lastUpdateTime = UnionConfig.getLastUpdateTime();
    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public void setHost(String s) {

    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public void setPort(int i) {

    }

    @Override
    public void setUnionPayUrl(String s) {
        this.unionPayUrl = s;
        CommonSharedPreferences.put("union_pay_url", s);
    }

    @Override
    public String getUnionPayUrl() {
        return unionPayUrl;
    }

    @Override
    public void setMachId(String s) {
        this.mch_id = s;
        CommonSharedPreferences.put("mch", s);
    }

    @Override
    public String getMchId() {
        return mch_id;
    }

    @Override
    public void setKey(String s) {
        this.key = s;
        CommonSharedPreferences.put("key", s);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setPosSn(String s) {
        this.posSN = s;
        CommonSharedPreferences.put("union_pos_sn", s);
    }

    @Override
    public String getPosSn() {
        return posSN;
    }

    @Override
    public String getBatchNum() {
        return batchNum;
    }

    @Override
    public void setBatchNum(String s) {
        this.batchNum = s;
        CommonSharedPreferences.put("batch_num", s);
    }

    @Override
    public int getTradeSeq() {
        return tradeSeq;
    }

    @Override
    public void setTradeSeq() {
//        String seq = String.format("%06d", tradeSeq);
        tradeSeq = (int) CommonSharedPreferences.get("trade_seq", 0);
//        int incSeq = UnionUtil.string2Int(seq.substring(3, 6));
        if (tradeSeq >= 999990) {
            tradeSeq = 1;
        }
        tradeSeq += 1;
//        String randomNumStr = String.format("%03d", (int) (Math.random() * 999)) + String.format("%03d", incSeq);
//        this.tradeSeq = UnionUtil.string2Int(randomNumStr);
        CommonSharedPreferences.put("trade_seq", tradeSeq);
    }

    @Override
    public void setTradeSeq(int i) {

    }


    @Override
    public String getOperatorNumber() {
        return operator;
    }

    @Override
    public void setOperatorNumber(String s) {
        this.operator = s;
        CommonSharedPreferences.put("operator_number", s);
    }

    @Override
    public String getMacKey() {
        return macKey;
    }

    @Override
    public void setMacKey(String s) {
        this.macKey = s;
        CommonSharedPreferences.put("mac_key", s);
    }

    @Override
    public String getTPDU() {
        return TPDU;
    }

    @Override
    public void setTPDU(String s) {

    }

    @Override
    public String aidIndexList() {
        return aidIndexList;
    }

    @Override
    public void setAidIndexList(String s) {
        this.aidIndexList = s;
        CommonSharedPreferences.put("aid_index_list", s);
    }

    @Override
    public String publicIndexList() {
        return null;
    }

    @Override
    public void setpublicIndexList(String s) {

    }

    @Override
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setCurrentUpdateTime(long time) {
        this.lastUpdateTime = time;
        CommonSharedPreferences.put("last_update_time", time);
    }
}
