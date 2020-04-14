package com.szxb.zibo.net.bean;

public class HeartBean {


    /**
     * customer_code : 881641
     * sign : 4D9E86E6AE0B12A6BC502638BBECC326
     * req_type : 1
     * timestamp : 1578996212624
     * result_msg : 请求成功！
     * merchant_no : 001
     * seq_no : 20200114000024
     * server_time : 1578996212
     * charset : UTF-8
     * trans_data : {"server_time":"1578996212"}
     * channel_code : 0000001
     * version : 1.1.5
     * result_code : 0
     * terminal_type : Q6-B
     * terminal_no : Q6B0B1T219140277
     * sign_type : MD5
     */

    private String customer_code;
    private String sign;
    private String req_type;
    private long timestamp;
    private String result_msg;
    private String merchant_no;
    private String seq_no;
    private String server_time;
    private String charset;
    private TransDataBean trans_data;
    private String channel_code;
    private String version;
    private String result_code;
    private String terminal_type;
    private String terminal_no;
    private String sign_type;

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public TransDataBean getTrans_data() {
        return trans_data;
    }

    public void setTrans_data(TransDataBean trans_data) {
        this.trans_data = trans_data;
    }

    public String getChannel_code() {
        return channel_code;
    }

    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getTerminal_type() {
        return terminal_type;
    }

    public void setTerminal_type(String terminal_type) {
        this.terminal_type = terminal_type;
    }

    public String getTerminal_no() {
        return terminal_no;
    }

    public void setTerminal_no(String terminal_no) {
        this.terminal_no = terminal_no;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class TransDataBean {
        /**
         * server_time : 1578996212
         */

        private String server_time;

        public String getServer_time() {
            return server_time;
        }

        public void setServer_time(String server_time) {
            this.server_time = server_time;
        }
    }
}
