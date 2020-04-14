package com.szxb.zibo.config.zibo.RequestDate;

public class PosInfoDate {

    /*****************设备信息*******************/
    private String level_no;// 所属级别编号
    private String psam_no;//psam卡号
    private String company_no;//运营单位编码
    private String line_no;//线路编号
    private String bus_no; //车辆编号
    private String bus_type; // 车辆等级
    private String base_price; // 基础票价
    private String softwar_ver; //软件版本
    private String wav_ver; // 语音版本
    private String csn_ver; //黑名单版本
    private String whl_ver; //白名单版本
    private String par_ver; // 基本参数版本
    private String usr_ver; // 用户参数版本
    private String far_ver; // 票价参数版本
    private String lin_ver; // 线路文件版本
    private String pub_ver; // 二维码公钥版本
    private String cert_ver; //二维码证书版本
    private String xml_ver; //配置文件版本
    private String system_ver; // 系统版本
    private String lib_ver; // 动态库版本
    private String ums_terminal_no; // 银联终端号
    private String ums_tenant_no; // 银联商户号
    private String ums_key_ver; // 银联秘钥MD5值
    private String blk_ver; // ODA黑名单版本
    private String driver_no; // 司机编号
    private String psamInf; // 司机编号
    private String union_com_no; // 银联商户号(必须上送)
    private String union_term_no; // 银联终端号(必须上送)







    /***********心跳***************/
    private String current_time; //yyyyMMddHHmmss 设备时钟信息
    private String max_pos_sn;//最大终端交易序号
    private String un_upload_num;//未上传交易记录数
    private String un_upload_num_ymt;//未上传交易记录数
    private String un_upload_num_ums;//未上传银联记录数
    private String un_upload_num_bucai;//未上传补采交易
    private String location_info;//经维度
    private String soft_ver;//版本


    /*******************************/
    private String tradetype;//交易类型    0:司机签到签退 1:IC 卡交易 2:二维码
    private String record_mark;//记录标识   保证记录唯一性
    private String collect_type;//采集类型   0:正常采集;1:补采集
    private String trans_size;//采集类型   填实际长度(单位:字节)
    private String record_data;//采集类型   填实际长度(单位:字节)


    public String getLevel_no() {
        return level_no;
    }

    public void setLevel_no(String level_no) {
        this.level_no = level_no;
    }

    public String getPsam_no() {
        return psam_no;
    }

    public void setPsam_no(String psam_no) {
        this.psam_no = psam_no;
    }

    public String getCompany_no() {
        return company_no;
    }

    public void setCompany_no(String company_no) {
        this.company_no = company_no;
    }

    public String getLine_no() {
        return line_no;
    }

    public void setLine_no(String line_no) {
        this.line_no = line_no;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public String getBus_type() {
        return bus_type;
    }

    public void setBus_type(String bus_type) {
        this.bus_type = bus_type;
    }

    public String getBase_price() {
        return base_price;
    }

    public void setBase_price(String base_price) {
        this.base_price = base_price;
    }

    public String getSoftwar_ver() {
        return softwar_ver;
    }

    public void setSoftwar_ver(String softwar_ver) {
        this.softwar_ver = softwar_ver;
    }

    public String getWav_ver() {
        return wav_ver;
    }

    public void setWav_ver(String wav_ver) {
        this.wav_ver = wav_ver;
    }

    public String getCsn_ver() {
        return csn_ver;
    }

    public void setCsn_ver(String csn_ver) {
        this.csn_ver = csn_ver;
    }

    public String getWhl_ver() {
        return whl_ver;
    }

    public void setWhl_ver(String whl_ver) {
        this.whl_ver = whl_ver;
    }

    public String getPar_ver() {
        return par_ver;
    }

    public void setPar_ver(String par_ver) {
        this.par_ver = par_ver;
    }

    public String getUsr_ver() {
        return usr_ver;
    }

    public void setUsr_ver(String usr_ver) {
        this.usr_ver = usr_ver;
    }

    public String getFar_ver() {
        return far_ver;
    }

    public void setFar_ver(String far_ver) {
        this.far_ver = far_ver;
    }

    public String getPub_ver() {
        return pub_ver;
    }

    public void setPub_ver(String pub_ver) {
        this.pub_ver = pub_ver;
    }

    public String getCert_ver() {
        return cert_ver;
    }

    public void setCert_ver(String cert_ver) {
        this.cert_ver = cert_ver;
    }

    public String getXml_ver() {
        return xml_ver;
    }

    public void setXml_ver(String xml_ver) {
        this.xml_ver = xml_ver;
    }

    public String getSystem_ver() {
        return system_ver;
    }

    public void setSystem_ver(String system_ver) {
        this.system_ver = system_ver;
    }

    public String getLib_ver() {
        return lib_ver;
    }

    public void setLib_ver(String lib_ver) {
        this.lib_ver = lib_ver;
    }

    public String getUms_terminal_no() {
        return ums_terminal_no;
    }

    public void setUms_terminal_no(String ums_terminal_no) {
        this.ums_terminal_no = ums_terminal_no;
    }

    public String getUms_tenant_no() {
        return ums_tenant_no;
    }

    public void setUms_tenant_no(String ums_tenant_no) {
        this.ums_tenant_no = ums_tenant_no;
    }

    public String getUms_key_ver() {
        return ums_key_ver;
    }

    public void setUms_key_ver(String ums_key_ver) {
        this.ums_key_ver = ums_key_ver;
    }

    public String getBlk_ver() {
        return blk_ver;
    }

    public void setBlk_ver(String blk_ver) {
        this.blk_ver = blk_ver;
    }

    public String getDriver_no() {
        return driver_no;
    }

    public void setDriver_no(String driver_no) {
        this.driver_no = driver_no;
    }

    public String getLin_ver() {
        return lin_ver;
    }

    public void setLin_ver(String lin_ver) {
        this.lin_ver = lin_ver;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getMax_pos_sn() {
        return max_pos_sn;
    }

    public void setMax_pos_sn(String max_pos_sn) {
        this.max_pos_sn = max_pos_sn;
    }

    public String getUn_upload_num() {
        return un_upload_num;
    }

    public void setUn_upload_num(String un_upload_num) {
        this.un_upload_num = un_upload_num;
    }

    public String getUn_upload_num_ymt() {
        return un_upload_num_ymt;
    }

    public void setUn_upload_num_ymt(String un_upload_num_ymt) {
        this.un_upload_num_ymt = un_upload_num_ymt;
    }

    public String getUn_upload_num_ums() {
        return un_upload_num_ums;
    }

    public void setUn_upload_num_ums(String un_upload_num_ums) {
        this.un_upload_num_ums = un_upload_num_ums;
    }

    public String getUn_upload_num_bucai() {
        return un_upload_num_bucai;
    }

    public void setUn_upload_num_bucai(String un_upload_num_bucai) {
        this.un_upload_num_bucai = un_upload_num_bucai;
    }

    public String getLocation_info() {
        return location_info;
    }

    public void setLocation_info(String location_info) {
        this.location_info = location_info;
    }

    public String getSoft_ver() {
        return soft_ver;
    }

    public void setSoft_ver(String soft_ver) {
        this.soft_ver = soft_ver;
    }

    public String getTradetype() {
        return tradetype;
    }

    public void setTradetype(String tradetype) {
        this.tradetype = tradetype;
    }

    public String getRecord_mark() {
        return record_mark;
    }

    public void setRecord_mark(String record_mark) {
        this.record_mark = record_mark;
    }

    public String getCollect_type() {
        return collect_type;
    }

    public void setCollect_type(String collect_type) {
        this.collect_type = collect_type;
    }

    public String getTrans_size() {
        return trans_size;
    }

    public void setTrans_size(String trans_size) {
        this.trans_size = trans_size;
    }

    public String getRecord_data() {
        return record_data;
    }

    public void setRecord_data(String record_data) {
        this.record_data = record_data;
    }

    public String getPsamInf() {
        return psamInf;
    }

    public void setPsamInf(String psamInf) {
        this.psamInf = psamInf;
    }

    public String getUnion_term_no() {
        return union_term_no;
    }

    public void setUnion_term_no(String union_term_no) {
        this.union_term_no = union_term_no;
    }

    public String getUnion_com_no() {
        return union_com_no;
    }

    public void setUnion_com_no(String union_com_no) {
        this.union_com_no = union_com_no;
    }
}
