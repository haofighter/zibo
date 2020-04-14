package com.szxb.zibo.config.zibo.RequestDate;

import android.util.Log;

import com.google.gson.Gson;
import com.hao.lib.Util.MiLog;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.util.md5.MD5;
import com.szxb.zibo.util.sp.CommonSharedPreferences;

public class RequestParam {


    /**
     * version : 1.0
     * channel_code : 0000001
     * timestamp : 1536333865
     * seq_no : 20180825000001
     * req_type : 1
     * charset : UTF-8
     * sign_type : MD5
     * customer_code : overallForXJ
     * merchant_no : 001
     * terminal_no : 285774087700030081114
     * terminal_type : 5210
     * trans_data :
     * sign : 5A05BC186D173A70C59913A0D39E7A94
     */

    private String version;
    private String channel_code;
    private String timestamp;
    private String seq_no;
    private String req_type;
    private String charset;
    private String sign_type;
    private String customer_code;
    private String merchant_no;
    private String terminal_no;
    private String terminal_type;
    private Object trans_data;
    private String sign;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChannel_code() {
        return channel_code;
    }

    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getTerminal_no() {
        return terminal_no;
    }

    public void setTerminal_no(String terminal_no) {
        this.terminal_no = terminal_no;
    }

    public String getTerminal_type() {
        return terminal_type;
    }

    public void setTerminal_type(String terminal_type) {
        this.terminal_type = terminal_type;
    }

    public Object getTrans_data() {
        return trans_data;
    }

    public void setTrans_data(Object trans_data) {
        this.trans_data = trans_data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static String getRequestParam(Object transdate, String reqType) {
        RequestParam requestParam = new RequestParam();
        requestParam.setVersion(BusApp.getInstance().getPakageVersion());
        requestParam.setChannel_code("0000001");
        requestParam.setTimestamp(System.currentTimeMillis() / 1000 + "");//1536333865
        requestParam.setSeq_no(getSeqNum());
        requestParam.setReq_type(reqType);
        requestParam.setCharset("UTF-8");
        requestParam.setSign_type("MD5");
        requestParam.setCustomer_code("881641");
        requestParam.setMerchant_no("001");
        requestParam.setTerminal_no(BusApp.getPosManager().getPosSN());
        requestParam.setTerminal_type("Q6-B");
        requestParam.setTrans_data(transdate);
        requestParam.setSign(MD5.getStringForMd5(requestParam).toUpperCase());
        String re = new Gson().toJson(requestParam);
        return re;
    }

    //获取请求序号
    public static String getSeqNum() {

        String seqndate = (String) CommonSharedPreferences.get("seqndate", "0");
        int seqnum = (int) CommonSharedPreferences.get("seqnum", 0);

        if (seqndate.equals(DateUtil.getCurrentDate6())) {
            seqnum++;
            CommonSharedPreferences.put("seqnum", seqnum);
        } else {
            seqndate = DateUtil.getCurrentDate6();
            seqnum = 1;
            CommonSharedPreferences.put("seqndate", DateUtil.getCurrentDate6());
            CommonSharedPreferences.put("seqnum", seqnum);
        }
        String strNum = "000000";
        strNum = strNum.substring(0, strNum.length() - (seqnum + "").length());
        return seqndate + strNum + seqnum;
    }
}
