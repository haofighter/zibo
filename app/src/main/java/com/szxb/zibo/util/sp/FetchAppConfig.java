package com.szxb.zibo.util.sp;


import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.line.FareRulePlan;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

/**
 * 作者: Tangren on 2017/7/12
 * 包名：com.szxb.onlinbus.Util
 * 邮箱：996489865@qq.com
 * 获取全局的SP数据
 */

public class FetchAppConfig {


    //公司号
    public static String unitno() {
        return (String) CommonSharedPreferences.get("unitno", String.format("%08d", 0));
    }



    public static String getLastVersion() {
        return (String) CommonSharedPreferences.get("last_bin", "000000");
    }


    //数字流水00000000-99999999
    public static int getNumSeq() {
        return (int) CommonSharedPreferences.get("num_seq", 1);
    }




    /**
     * @return 站点名
     */
    public static String getStationName() {
        return (String) CommonSharedPreferences.get("stationName", "未获取到站点");
    }

    /**
     * @return 站点ID
     */
    public static int getStationID() {
        return (int) CommonSharedPreferences.get("stationID", 1);
    }

    /**
     * @return 默认上行
     */
    public static String getDirection() {
        return (String) CommonSharedPreferences.get("direction", "0001");
    }

    //线路类型：0单票，1多票
    public static int getType() {
        return (Integer) CommonSharedPreferences.get("type", 0);
    }

    //安装方向：0前门，1后门
    public static int getWindowDirection() {
        return (Integer) CommonSharedPreferences.get("windowDirection", 0);
    }

    /**
     * @return 是否下载完成
     */
    public static int getDownFinish() {
        return (Integer) CommonSharedPreferences.get("downFinish", 0);
    }

    /**
     * @return APK版本信息
     */
    public static String getAPKInfo() {
        return (String) CommonSharedPreferences.get("APKInfo", "未获取到APK信息");
    }

    public static long getSignTime() {
        return (Long) CommonSharedPreferences.get("signTime", 0l);
    }

    public static String getConductor() {
        return (String) CommonSharedPreferences.get("conductor", "");
    }


    public static String getUnion() {
        return (String) CommonSharedPreferences.get("unitno", "000000");
    }

    public static String getMchId() {
        return (String) CommonSharedPreferences.get("mchID", "000000");
    }

    public static String getMainPsam() {

        return (String) CommonSharedPreferences.get("mainPSAM", "000000");
    }

    public static String getUms_tenant_no() {
        return (String) CommonSharedPreferences.get("Ums_tenant_no", "");
    }

    public static String getLineType() {
        FareRulePlan fareRulePlan = DBManagerZB.checkFareRulePlan();
        if (fareRulePlan == null) {
            return "O";
        } else {
            return fareRulePlan.getFareRuleType();
        }
    }



    public static String getFarver() {
        return (String) CommonSharedPreferences.get("farver", "00000000000000");
    }

    public static String getPubver() {
        return (String) CommonSharedPreferences.get("pub_ver", "00000000000000");
    }

    public static String getUsrver() {
        return (String) CommonSharedPreferences.get("usrver", "00000000000000");
    }

    public static String getCsnVer() {
        return (String) CommonSharedPreferences.get("csnVer", "00000000000000");
    }

    public static String getUms_key_ver() {
        return (String) CommonSharedPreferences.get("ums_key_ver", "000000000000000000");
    }
}