package com.szxb.zibo.net.bean;

public class NetResponseNew {


//    private String retmsg;
//    private DataBean pubkey_list;
//    private String retcode;

    private String pubkey_list;
    private String retmsg;
    private String retcode;

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public String getPubkey_list() {
        return pubkey_list;
    }

    public void setPubkey_list(String pubkey_list) {
        this.pubkey_list = pubkey_list;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

}
