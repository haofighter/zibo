package com.szxb.zibo.manager;

import android.text.TextUtils;
import android.util.Log;

import com.example.zhoukai.modemtooltest.ModemToolTest;
import com.google.gson.Gson;
import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.line.StationName;
import com.szxb.zibo.config.zibo.line.ZBLineInfo;
import com.szxb.zibo.db.bean.ConfigParam;
import com.szxb.zibo.db.bean.FTPEntity;
import com.szxb.zibo.moudle.function.location.GPSEvent;
import com.szxb.zibo.record.AppParamInfo;
import com.szxb.zibo.util.Util;
import com.szxb.zibo.util.sp.CommonSharedPreferences;
import com.szxb.zibo.util.sp.FetchAppConfig;

import java.util.List;

public class PosManager implements IPosManager, IAddRess, ISwitch {
    public static final String TX_PUB = "TX_PUB";
    public static final String TX_MAC = "TX_MAC";
    public static final String ALI_PUB = "ALI_PUB";
    public static final String JTB_PUB = "JTB_PUB";
    public static final String DG_PUB = "DG_PUB";
    public static final String FR_PUB = "FR_PUB";
    public static final String CL_PUB = "CL_PUB";


    private String lineType = "O";//当前使用的线路的类型  O 一票制  P 前后门刷卡分段 X 阶梯性分段
    private String m1psam = "000000000000";
    private String cpupsam = "000000000000";
    private String jtbpsam = "000000000000";
    private long posManagerSetTime;//配置更新时间
    private String keybroadAddress = "1234";//键盘地址
    private String myselfkeybroadAddress = "5678";//键盘地址
    private String myselfkeybroadAddressCache = "5678";//键盘地址
    private String UpDateInfo = "";//更新信息

    /**
     * 操作状态 主要针对 多票和分段
     */
    private int operate; //0 自动  1 手动

    /**
     * 当前卡机的状态
     */
    private int posUpDate = 1; //0 上下车机  1 上车机  2 下车机

    /**
     * 路线名
     */
    private String lineName;

    /**
     * 路线号
     */
    private String lineNo;

    /**
     * 当前行驶方向
     */
    private String direction = "0001";// 0001  上行  0002 下行

    /**
     * 当前站点名
     */
    private String stationName;

    /**
     * 当前站点ID
     */
    private int stationID = 1;

    /**
     * 设备号
     */
    private String posSn;

    /**
     * 基础金额
     */
    private int basePrice;


    /**
     * 370300zibo  371200laiwu
     */
    private String cityCode;

    /**
     * 车牌号
     */
    private String bus_no = "000000";


    /**
     * 司机签到时间
     */
    private long signTime;

    /**
     * 公司号、食堂号
     */
    private String companyID;
    /**
     * 司机号
     */
    private String driverNo = "00000000";

    /**
     * 语音文件版本
     */
    private String wavVer = "00000000000000";
    /**
     * 黑名单文件版本
     */
    private String csnVer = "00000000000000";
    /**
     * 白名单文件版本
     */
    private String whlVer = "00000000000000";
    /**
     * 票价文件版本
     */
    private String farver = "00000000000000";
    /**
     * 基本参数文件版本
     */
    private String parver = "00000000000000";
    /**
     * 用户参数文件版本
     */
    private String usrver = "00000000000000";
    /**
     * 线路文件版本
     */
    private String Linver = "00000000000000";
    /**
     * 分公司号
     */
    private String unitno = "00000";

    /**
     * 商户号
     */
    private String mchID = "000000";


    private String pub_ver = "00000000000000";//二维码公钥版本
    private String cert_ver = "00000000000000";//二维码证书版本
    private String xml_ver = "00000000000000"; // 配置文件版本
    private String system_ver = "00000000000000";//系统版本
    private String lib_ver = "00000000000000";//动态库版本
    private String ums_terminal_no;  //银联终端号
    private String ums_tenant_no;// 银联商户号
    private String ums_key_ver = "00000000000000000000000000000000";//银联秘钥MD5值
    private String blk_ver = "00000000000000";   // ODA黑名单版本


    private String ftpIP;
    private int ftpPort;
    private String ftpUser;
    private String ftpPsw;

    private FTPEntity ftpEntity;

    //上个bin版本
    private String lastVersion;

    //当前bin
    private String binName;

    //是否支持微信支付
    private boolean isSuppScanPay = false;

    //是否支持银联卡支付
    private boolean isSuppUnionPay = false;

    //是否支付刷公交卡
    private boolean isSuppIcPay = false;

    //是否支持票价键盘
    private boolean isSuppKeyBoard = false;

    //递增流水号
    private int numSeq = 0;


    //当前经纬度
    private double location[] = new double[2];


    //0单票，1多票
    private int type = 0;

    //安装方向 0:前门 1:后门
    private int windowDirection = 0;


    private String conductor;

    private String mainPSAM;
    private int GPSStatus;
    private String downloadApkName;

    @Override
    public void loadFromPrefs(final int city, final String bin) {
        try {
            //pos基础参数
            initBase(bin);

            //初始化SN号
            initSn();

//            //读取配置参数
//            config(city);

        } catch (Exception e) {
            e.printStackTrace();
            MiLog.i("错误", "PosManager(run.java:229)参数加载失败>>>>异常>>" + e.toString());
        }
    }

    private void initBase(String bin) {
        companyID = FetchAppConfig.unitno();
        lastVersion = FetchAppConfig.getLastVersion();
        binName = bin;
        numSeq = FetchAppConfig.getNumSeq();
        unitno = FetchAppConfig.getUnion();
        type = FetchAppConfig.getType();
        windowDirection = FetchAppConfig.getWindowDirection();
        signTime = FetchAppConfig.getSignTime();
        conductor = FetchAppConfig.getConductor();

        mchID = FetchAppConfig.getMchId();
        mainPSAM = FetchAppConfig.getMainPsam();
        ums_tenant_no = FetchAppConfig.getUms_tenant_no();
        lineType = FetchAppConfig.getLineType();

        farver = FetchAppConfig.getFarver();
        pub_ver = FetchAppConfig.getPubver();
        usrver = FetchAppConfig.getUsrver();
        csnVer = FetchAppConfig.getCsnVer();
        ums_key_ver = FetchAppConfig.getUms_key_ver();
        loadRunParam();
    }


    private void loadRunParam() {
        stationName = FetchAppConfig.getStationName();
        stationID = FetchAppConfig.getStationID();
        direction = FetchAppConfig.getDirection();
        keybroadAddress = (String) CommonSharedPreferences.get("keybroadAddress", "0000");
        myselfkeybroadAddress = (String) CommonSharedPreferences.get("myselfkeybroadAddress", "0001");
        AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
        posUpDate = (int) CommonSharedPreferences.get("posUpDate", 1);
        bus_no = appParamInfo.getBusNo();
        driverNo = appParamInfo.getDriverNo();
        lineNo = appParamInfo.getLinNo();
        lineName = appParamInfo.getLinName();
        basePrice = appParamInfo.getBasePrice();
        Linver = appParamInfo.getLinVer();
    }

    private void config(int city) {
        String config = Util.readAssetsFile("config.json", BusApp.getInstance().getApplication());
        ConfigParam configParam = new Gson().fromJson(config, ConfigParam.class);
        List<ConfigParam.ConfigBean> configList = configParam.getConfig();
        ConfigParam.ConfigBean configBean = configList.get(city);
        isSuppIcPay = configBean.isIs_supp_ic_pay();
        isSuppScanPay = configBean.isIs_supp_scan_pay();
        isSuppUnionPay = configBean.isIs_supp_union_pay();
        cityCode = configBean.getCity_code();
        ftpIP = configBean.getIp();
        ftpPort = configBean.getPort();
        ftpUser = configBean.getUser();
        ftpPsw = configBean.getPsw();
        isSuppKeyBoard = configBean.isIs_supp_key_board();
        ftpEntity = new FTPEntity(ftpIP, ftpPort, ftpUser, ftpPsw);
        MiLog.i("配置参数", "PosManager(config.java:308)" + ftpEntity);
    }

    private void initSn() {
        String item;
        try {
            item = ModemToolTest.getItem(7);
        } catch (Exception e) {
            item = "Q6BBB1T000000000";
        }
        if (TextUtils.isEmpty(item)) {
            posSn = "Q6BBB1T000000000";
        } else {
            posSn = item;
        }
    }

    @Override
    public String getLineName() {
        return lineName;
    }

    @Override
    public void setLineName(String var1) {
        this.lineName = var1;
        AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
        appParamInfo.setLinName(var1);
        MiLog.i("流程", "参数配置   保存线路：" + var1);
        DBManagerZB.saveAppParamInfo(appParamInfo);
        setPosManagerSetTime(System.currentTimeMillis());
    }

    @Override
    public String getLineNo() {
        if (lineNo == null || lineNo.equals("000000")) {
            loadRunParam();
        }
        return lineNo;
    }

    public String getNewCpuLine() {
        String link_number = lineNo;
        if (link_number.length() == 6) {
            try {
                int company = Integer.parseInt(link_number.substring(0, 2));
                int line = Integer.parseInt(link_number.substring(2, 6));
                String comStr = FileUtils.formatHexStringToByteString(1, Integer.toHexString(company));
                String lineStr = FileUtils.formatHexStringToByteString(2, Integer.toHexString(line));
                link_number = comStr + lineStr;
            } catch (Exception e) {

            }
        }
        return link_number;
    }

    public String getKeybroadAddress() {
        return keybroadAddress;
    }

    public void setKeybroadAddress(String keybroadAddress) {
        keybroadAddress = FileUtils.formatHexStringToByteString(2, keybroadAddress);
        CommonSharedPreferences.put("keybroadAddress", keybroadAddress);
        this.keybroadAddress = keybroadAddress;
    }

    public String getM1Line() {
        String link_number = lineNo;
        if (link_number.length() == 6) {
            try {
                int company = Integer.parseInt(link_number.substring(0, 2));
                int line = Integer.parseInt(link_number.substring(2, 6));
                String comStr = FileUtils.formatHexStringToByteString(1, Integer.toHexString(company));
                String lineStr = FileUtils.formatHexStringToByteString(1, Integer.toHexString(line));
                link_number = comStr + lineStr;
            } catch (Exception e) {

            }
        }
        return link_number;
    }


    @Override
    public void setLineNo(String var1) {
        this.lineNo = var1;
        AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
        appParamInfo.setLinNo(var1);
        ZBLineInfo zbLineInfo = DBManagerZB.checkedLineInfo(var1);

        if (zbLineInfo != null) {
            appParamInfo.setLinName(zbLineInfo.getRoutename());
        }
        MiLog.i("流程", "参数配置   保存线路号：" + var1);
        DBManagerZB.saveAppParamInfo(appParamInfo);
        CommonSharedPreferences.put("line_no", var1);
        setPosManagerSetTime(System.currentTimeMillis());
    }

    @Override
    public int getBasePrice() {
        if (basePrice == 0) {
            loadRunParam();
        }
        return basePrice;
    }

    @Override
    public void setBasePrice(int var1) {
        this.basePrice = var1;
        AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
        appParamInfo.setBasePrice(var1);
        MiLog.i("流程", "参数配置   保存基础票价：" + var1);
        DBManagerZB.saveAppParamInfo(appParamInfo);
        CommonSharedPreferences.put("base_price", var1);
    }


    public int getmchTrxId() {
        if (numSeq >= 99999999) {
            numSeq = 0;
        }
        numSeq++;
        CommonSharedPreferences.put("num_seq", numSeq);
        return numSeq;
    }

    public int getmchTrxIdNow() {
        return numSeq;
    }


    @Override
    public String getBusNo() {
        if (bus_no == null || bus_no.equals("000000")) {
            loadRunParam();
        }
        return bus_no;
    }

    @Override
    public void setBusNo(String bus_no) {
        this.bus_no = bus_no;
        if (bus_no.length() >= 6) {
            myselfkeybroadAddress = FileUtils.formatHexStringToByteString(2, bus_no.substring(2));
        }
        AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
        appParamInfo.setBusNo(bus_no);
        MiLog.i("流程", "参数配置   保存车辆号：" + bus_no);
        DBManagerZB.saveAppParamInfo(appParamInfo);
        setPosManagerSetTime(System.currentTimeMillis());
    }


    @Override
    public String getCityCode() {
        return cityCode;
    }

    @Override
    public void setCityCode(String cityCoid) {
        this.cityCode = cityCoid;
        CommonSharedPreferences.put("cityCode", companyID);
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
        CommonSharedPreferences.put("companyID", companyID);
    }

    @Override
    public String getCompanyID() {
        return companyID;
    }

    @Override
    public void setDriverNo(String driverNo) {
        this.driverNo = driverNo;
        AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
        appParamInfo.setDriverNo(driverNo);
        MiLog.i("流程", "参数配置   保存司机号：" + driverNo);
        DBManagerZB.saveAppParamInfo(appParamInfo);
        setPosManagerSetTime(System.currentTimeMillis());
    }

    @Override
    public String getDriverNo() {
        if (driverNo == null || driverNo.equals("")) {
            driverNo = "00000000";
        }
        return driverNo;
    }


    @Override
    public String getPosSN() {
        if (posSn == null || posSn.equals("")) {
            posSn = "1234567890";
        }
        return posSn;
    }

    @Override
    public String getLastVersion() {
        return lastVersion;
    }

    @Override
    public void setLastVersion(String version) {
        this.lastVersion = version;
        CommonSharedPreferences.put("last_bin", version);
    }

    @Override
    public String getBinVersion() {
        return binName;
    }

    @Override
    public void setDirection(String direction) {
        if (!getLineType().equals("O")) {
            if (direction.equals("0000")) {
                direction = "0001";
            }
        }
        this.direction = FileUtils.formatHexStringToByteString(2, direction);
        CommonSharedPreferences.put("direction", this.direction);
    }

    @Override
    public String getDirection() {
        if (!getLineType().equals("O")) {
            if (direction.equals("0000")) {
                direction = "0001";
            }
        }
        return direction;
    }

//    @Override
//    public void setStationName(String stationName) {
//        if (!TextUtils.equals(this.stationName, stationName)) {
//            CommonSharedPreferences.put("stationName", stationName);
//        }
//
//        this.stationName = stationName;
//    }

    @Override
    public String getStationName() {
        StationName stationName = DBManagerZB.checkStation(BusApp.getPosManager().getDirection(), stationID);
        if (stationName != null) {
            return stationName.getStationName();
        } else {
            Log.i("站点名", "未查询到数据" + BusApp.getPosManager().getDirection() + "      " + "   " + stationID);
        }
        return "";
    }

    @Override
    public void setStationID(int stationID) {
        if (this.stationID != stationID) {
            CommonSharedPreferences.put("stationID", stationID);
        }
        this.stationID = stationID;
    }

    @Override
    public int getStationID() {
        return stationID;
    }

    @Override
    public void setLocation(double[] location) {
        this.location = location;
    }

    //经纬度 经度在前
    @Override
    public double[] getLocation() {
        if (location.length < 2) {
            if (GPSEvent.bdLocation != null) {
                location = new double[]{GPSEvent.bdLocation.getLongitude(), GPSEvent.bdLocation.getLatitude()};
            } else {
                location = new double[]{0, 0};
            }
        }
        return location;
    }


    @Override
    public void setType(int type) {
        this.type = type;
        CommonSharedPreferences.put("type", type);
    }

    @Override
    public int getType() {
        return type;
    }


    @Override
    public void setFtpIp(String ip) {
        this.ftpIP = ip;
        CommonSharedPreferences.put("ftp_ip", ip);
    }

    @Override
    public String getFtpIP() {
        return ftpIP;
    }

    @Override
    public void setPort(int port) {
        this.ftpPort = port;
        CommonSharedPreferences.put("ftp_port", port);
    }

    @Override
    public int getPort() {
        return ftpPort;
    }

    @Override
    public void setFtpPsw(String psw) {
        this.ftpPsw = psw;
        CommonSharedPreferences.put("ftp_psw", psw);
    }

    @Override
    public String getFtpPsw() {
        return ftpPsw;
    }

    @Override
    public void setFtpUser(String user) {
        this.ftpUser = user;
        CommonSharedPreferences.put("ftp_user", user);
    }

    @Override
    public String getFtpUser() {
        return ftpUser;
    }

    @Override
    public void setUrlIp(String ip) {

    }

    @Override
    public String getUrlIp() {
        return null;
    }

    @Override
    public void setFTP(FTPEntity ftp) {
        this.ftpEntity = ftp;
        if (ftp != null) {
            CommonSharedPreferences.put("ftp_ip", ftp.getI());
            CommonSharedPreferences.put("ftp_user", ftp.getU());
            CommonSharedPreferences.put("ftp_psw", ftp.getPsw());
            CommonSharedPreferences.put("ftp_port", ftp.getP());
        }
    }

    @Override
    public FTPEntity getFTP() {
        return ftpEntity;
    }

    @Override
    public boolean isSuppScanPay() {
        return isSuppScanPay;
    }

    @Override
    public boolean isSuppIcPay() {
        return isSuppIcPay;
    }

    @Override
    public boolean isSuppUnionPay() {
        return isSuppUnionPay;
    }

    @Override
    public boolean isSuppKeyBoard() {
        return isSuppKeyBoard;
    }

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
        CommonSharedPreferences.put("signTime", signTime);
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
        CommonSharedPreferences.put("conductor", conductor);
    }

    public String getWavVer() {
        return wavVer;
    }

    public void setWavVer(String wavVer) {
        this.wavVer = wavVer;
    }

    public String getCsnVer() {
        return csnVer;
    }

    public void setCsnVer(String csnVer) {
        this.csnVer = csnVer;
        CommonSharedPreferences.put("csnVer", csnVer);
        MiLog.i("流程", "保存备份数据   csnVer=" + csnVer);
        BusApp.getInstance().saveBackeUp();//保存基本配置
    }

    public String getWhlVer() {
        return whlVer;
    }

    public void setWhlVer(String whlVer) {
        this.whlVer = whlVer;
        MiLog.i("流程", "保存备份数据   whlVer=" + whlVer);
        BusApp.getInstance().saveBackeUp();//保存基本配置
    }

    public String getFarver() {
        return farver;
    }

    public void setFarver(String farver) {
        this.farver = farver;
        CommonSharedPreferences.put("farver", farver);
        BusApp.getInstance().saveBackeUp();//保存基本配置
        AppParamInfo appParamInfo = DBManagerZB.checkAppParamInfo();
        appParamInfo.setLinVer(farver);
        MiLog.i("流程", "参数配置   保存farver版本：" + farver);
        DBManagerZB.saveAppParamInfo(appParamInfo);
    }

    public String getParver() {
        return parver;
    }

    public void setParver(String parver) {
        this.parver = parver;
    }

    public String getUsrver() {
        return usrver;
    }

    public void setUsrver(String usrver) {
        this.usrver = usrver;
        CommonSharedPreferences.put("usrver", usrver);
        MiLog.i("流程", "保存备份数据   usrver=" + usrver);
        BusApp.getInstance().saveBackeUp();//保存基本配置
    }


    public String getLib_ver() {
        return lib_ver;
    }

    public void setLib_ver(String lib_ver) {
        this.lib_ver = lib_ver;
    }

    public String getUms_terminal_no() {
        if (ums_terminal_no == null || ums_terminal_no.equals("")) {
            ums_terminal_no = "000000000000000";
        }
        return ums_terminal_no;
    }

    public void setUms_terminal_no(String ums_terminal_no) {
        this.ums_terminal_no = ums_terminal_no;
    }

    public String getUms_tenant_no() {
        if (ums_tenant_no == null || ums_tenant_no.equals("")) {
            ums_tenant_no = "00000000";
        }
        return ums_tenant_no;
    }

    public void setUms_tenant_no(String ums_tenant_no) {
        CommonSharedPreferences.put("Ums_tenant_no", ums_tenant_no);
        this.ums_tenant_no = ums_tenant_no;
    }

    public String getUms_key_ver() {
        if (ums_key_ver == null || ums_key_ver.equals("")) {
            ums_key_ver = "00000000000000000000000000000000";
        }
        return ums_key_ver;
    }

    public void setUms_key_ver(String ums_key_ver) {
        this.ums_key_ver = ums_key_ver;
        CommonSharedPreferences.put("ums_key_ver", ums_key_ver);
        MiLog.i("流程", "保存备份数据   ums_key_ver=" + ums_key_ver);
        BusApp.getInstance().saveBackeUp();//保存基本配置
    }

    public String getBlk_ver() {
        return blk_ver;
    }

    public void setBlk_ver(String blk_ver) {
        this.blk_ver = blk_ver;
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

    public String getPub_ver() {
        return pub_ver;
    }

    public void setPub_ver(String pub_ver) {
        this.pub_ver = pub_ver;
        CommonSharedPreferences.put("pub_ver", pub_ver);
        MiLog.i("流程", "保存备份数据   pub_ver=" + pub_ver);
        BusApp.getInstance().saveBackeUp();//保存基本配置
    }

    public String getLinver() {
        return Linver;
    }

    public void setLinver(String linver) {
        Linver = linver;
    }


    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
        CommonSharedPreferences.put("unitno", unitno);
    }

    public String getMchID() {
        return mchID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
        CommonSharedPreferences.put("mchID", mchID);
    }

    public String getMainPSAM() {
        if (mainPSAM.equals("000000") && m1psam != null) {
            mainPSAM = m1psam;
        }
        return mainPSAM;
    }

    public void setMainPSAM(String psamNo) {
        mainPSAM = psamNo;
        CommonSharedPreferences.put("mainPSAM", mainPSAM);
    }

    public void setM1psam(String m1psam) {
        this.m1psam = m1psam;
    }

    public void setJTBpsam(String jtbpsam) {
        this.jtbpsam = jtbpsam;
    }

    public void setCpupsam(String cpupsam) {
        this.cpupsam = cpupsam;
    }

    public String getM1psam() {
        if (m1psam == null || m1psam.equals("")) {
            m1psam = "000000000000";
        }
        return m1psam;
    }

    public String getCpupsam() {
        return cpupsam;
    }

    public String getJTBpsam() {
        return jtbpsam;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType() {
        this.lineType = FetchAppConfig.getLineType();
    }

    public void setGPSSTATUS(int i) {
        GPSStatus = i;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public void setNeedIntallApk(boolean filePath) {
        CommonSharedPreferences.put("NeedIntallApkPath", filePath);
    }

    public void setIntallApkPath(String filePath) {
        CommonSharedPreferences.put("filePath", filePath);
    }

    public String getInstallApk() {
        return (String) CommonSharedPreferences.get("installapk", "");
    }

    //设置辅助安装的apk名称
    public void setInstallApk(String installApk) {
        CommonSharedPreferences.put("installapk", installApk);
    }

    public void setPosManagerSetTime(long posManagerSetTime) {
        this.posManagerSetTime = posManagerSetTime;
    }

    public int getPosUpDate() {
        return posUpDate;
    }

    public void setPosUpDate(int posUpDate) {
        CommonSharedPreferences.put("posUpDate", posUpDate);
        this.posUpDate = posUpDate;
    }

    public void init(PosManager manager) {
        setLineType();
        setPosManagerSetTime(manager.posManagerSetTime);
        setOperate(manager.operate);
        setPosUpDate(manager.posUpDate);
        setLineName(manager.lineName);
        setLineNo(manager.lineNo);
        setDirection(manager.direction);
        setStationID(manager.stationID);
        setBasePrice(manager.basePrice);
        setCityCode(manager.cityCode);
        setBusNo(manager.bus_no);
        setSignTime(manager.signTime);
        setCompanyID(manager.companyID);
        setDriverNo(manager.driverNo);
        setWavVer(manager.wavVer);
        setCsnVer(manager.csnVer);
        setWhlVer(manager.whlVer);
        if (getFarver().equals("00000000000000")) {
            setFarver(manager.farver);
        }
        setParver(manager.parver);
        setUsrver(manager.usrver);
        if (getLinver().equals("00000000000000")) {
            setLinver(manager.Linver);
        }
        setUnitno(manager.unitno);
        setMchID(manager.mchID);
        setPub_ver(manager.pub_ver);
        setCert_ver(manager.cert_ver);
        setXml_ver(manager.xml_ver);
        system_ver = (manager.system_ver);
        setLib_ver(manager.lib_ver);
        setUms_terminal_no(manager.ums_terminal_no);
        setUms_tenant_no(manager.ums_tenant_no);
        setUms_key_ver(manager.ums_key_ver);
        setBlk_ver(manager.blk_ver);
        setFtpIp(manager.ftpIP);
        setPort(manager.ftpPort);
        setFtpUser(manager.ftpUser);
        setFtpPsw(manager.ftpPsw);
        setFTP(manager.ftpEntity);
        setLastVersion(manager.lastVersion);
        isSuppScanPay = (manager.isSuppScanPay);
        isSuppUnionPay = (manager.isSuppUnionPay);
        isSuppIcPay = (manager.isSuppIcPay);
        isSuppKeyBoard = (manager.isSuppKeyBoard);
        numSeq = (manager.numSeq);
        setLocation(manager.location);
        setType(manager.type);
        windowDirection = (manager.windowDirection);
        setConductor(manager.conductor);
        setMainPSAM(manager.mainPSAM);
        GPSStatus = manager.GPSStatus;
    }


    public String getMyselfkeybroadAddress() {
        return myselfkeybroadAddress;
    }

    public void setMyselfkeybroadAddress(String myselfkeybroadAddress) {
        myselfkeybroadAddress = FileUtils.formatHexStringToByteString(2, myselfkeybroadAddress);
        CommonSharedPreferences.put("myselfkeybroadAddress", myselfkeybroadAddress);
        this.myselfkeybroadAddress = myselfkeybroadAddress;
    }

    public String getMyselfkeybroadAddressCache() {
        return myselfkeybroadAddressCache;
    }

    public void setMyselfkeybroadAddressCache(String myselfkeybroadAddressCache) {
        this.myselfkeybroadAddressCache = myselfkeybroadAddressCache;
    }

    /**
     * 清除版本信息
     */
    public void clearRunParam() {
        setUsrver("00000000000000");
        setCsnVer("00000000000000");
        setUms_key_ver("00000000000000000000000000000000");
        setPub_ver("00000000000000");
        setFarver("00000000000000");
        setLinver("00000000000000");
    }

    public void saveDownApkName(String apkname) {
        this.downloadApkName = apkname;
    }

    public String getDownloadApkName() {
        return downloadApkName;
    }

    public void setIsClean(boolean b) {
        CommonSharedPreferences.put("IsClean", b);
        setUpDateInfo("加载补丁失败,回退版本");
    }

    public boolean getIsClean() {
        return (boolean) CommonSharedPreferences.get("IsClean", false);
    }


    public void setUpDateInfo(String s) {
        UpDateInfo = s;
    }

    public String getUpDateInfo() {
        return UpDateInfo;
    }
}

