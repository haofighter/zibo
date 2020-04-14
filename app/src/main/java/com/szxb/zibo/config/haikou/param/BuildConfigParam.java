package com.szxb.zibo.config.haikou.param;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 配置文件中配置的参数
 */
@Entity
public class BuildConfigParam {

    /**
     * ip : ftp地址
     * port : ftp 端口号
     * user : ftp用户名
     * psw : ftp密码
     * mch_id : 微信商户号
     * city_code : 城市代码
     * city_code : 城市代码
     * url_ip : http ip
     * is_supp_scan_pay : true    是否支持扫码支付
     * is_supp_ic_pay : true      是否支持ic卡支付
     * is_supp_union_pay : false  是否支持银联卡支付
     * is_supp_key_board : false  是否票价键盘
     * city : 淄博[0]
     */

    @Id
    Long id;
    private String ip;
    private int port;
    private String user;
    private String psw;
    private String mch_id;
    private String city_code;
    private String url_ip;
    private String city;
    private String organization;//机构代码 用于互联互通
    private boolean is_supp_scan_pay;
    private boolean is_supp_ic_pay;
    private boolean is_supp_union_pay;
    private boolean is_supp_ali_pay;
    private Long updateTime;


    public boolean isIs_supp_ali_pay() {
        return is_supp_ali_pay;
    }

    public void setIs_supp_ali_pay(boolean is_supp_ali_pay) {
        this.is_supp_ali_pay = is_supp_ali_pay;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPsw() {
        return this.psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getMch_id() {
        return this.mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getCity_code() {
        return this.city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getUrl_ip() {
        return this.url_ip;
    }

    public void setUrl_ip(String url_ip) {
        this.url_ip = url_ip;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean getIs_supp_scan_pay() {
        return this.is_supp_scan_pay;
    }

    public void setIs_supp_scan_pay(boolean is_supp_scan_pay) {
        this.is_supp_scan_pay = is_supp_scan_pay;
    }

    public boolean getIs_supp_ic_pay() {
        return this.is_supp_ic_pay;
    }

    public void setIs_supp_ic_pay(boolean is_supp_ic_pay) {
        this.is_supp_ic_pay = is_supp_ic_pay;
    }

    public boolean getIs_supp_union_pay() {
        return this.is_supp_union_pay;
    }

    public void setIs_supp_union_pay(boolean is_supp_union_pay) {
        this.is_supp_union_pay = is_supp_union_pay;
    }

    public boolean getIs_supp_ali_pay() {
        return this.is_supp_ali_pay;
    }

    public boolean getIs_supp_key_board() {
        return this.is_supp_key_board;
    }

    public void setIs_supp_key_board(boolean is_supp_key_board) {
        this.is_supp_key_board = is_supp_key_board;
    }

    private boolean is_supp_key_board;


    @Generated(hash = 901701009)
    public BuildConfigParam(Long id, String ip, int port, String user, String psw,
            String mch_id, String city_code, String url_ip, String city,
            String organization, boolean is_supp_scan_pay, boolean is_supp_ic_pay,
            boolean is_supp_union_pay, boolean is_supp_ali_pay, Long updateTime,
            boolean is_supp_key_board) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.psw = psw;
        this.mch_id = mch_id;
        this.city_code = city_code;
        this.url_ip = url_ip;
        this.city = city;
        this.organization = organization;
        this.is_supp_scan_pay = is_supp_scan_pay;
        this.is_supp_ic_pay = is_supp_ic_pay;
        this.is_supp_union_pay = is_supp_union_pay;
        this.is_supp_ali_pay = is_supp_ali_pay;
        this.updateTime = updateTime;
        this.is_supp_key_board = is_supp_key_board;
    }

    @Generated(hash = 38727365)
    public BuildConfigParam() {
    }



    @Override
    public String toString() {
        return "BuildConfigParam{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", user='" + user + '\'' +
                ", psw='" + psw + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", city_code='" + city_code + '\'' +
                ", url_ip='" + url_ip + '\'' +
                ", city='" + city + '\'' +
                ", organization='" + organization + '\'' +
                ", is_supp_scan_pay=" + is_supp_scan_pay +
                ", is_supp_ic_pay=" + is_supp_ic_pay +
                ", is_supp_union_pay=" + is_supp_union_pay +
                ", is_supp_ali_pay=" + is_supp_ali_pay +
                ", is_supp_key_board=" + is_supp_key_board +
                '}';
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
