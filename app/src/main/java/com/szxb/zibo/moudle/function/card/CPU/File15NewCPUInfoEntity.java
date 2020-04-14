package com.szxb.zibo.moudle.function.card.CPU;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class File15NewCPUInfoEntity {
    //--------------------------------------------------应用基本信息文件（0015）
    String issuer_code_15;            //发卡方代码
    String city_code_15;            //城市代码
    String industry_code_15;        //行业代码
    String reserved_1;                //预留
    String enable_flag_15;                //卡片启用标志
    String application_version_15;        //应用版本号
    String each_logo;                //互通标识
    String serial_number_15;        //应用序列号
    String enable_date;                //启用日期
    String valid_date_15;            //有效日期
    String reserved_2;                //预留

    public int praseFile(int i, byte[] date) {
        byte[] carddate = new byte[30];
        arraycopy(date, i, carddate, 0, carddate.length);
        MiLog.i("15卡数据", (String) FileUtils.byte2Parm(carddate, Type.HEX));

        //发卡方代码
        byte[] issuer_code_15 = new byte[2];
        arraycopy(date, i, issuer_code_15, 0, issuer_code_15.length);
        i += issuer_code_15.length;
        this.issuer_code_15 = (String) FileUtils.byte2Parm(issuer_code_15, Type.HEX);

        //城市代码
        byte[] city_code_15 = new byte[2];
        arraycopy(date, i, city_code_15, 0, city_code_15.length);
        i += city_code_15.length;
        this.city_code_15 = (String) FileUtils.byte2Parm(city_code_15, Type.HEX);

        //行业代码
        byte[] industry_code_15 = new byte[2];
        arraycopy(date, i, industry_code_15, 0, industry_code_15.length);
        i += industry_code_15.length;
        this.industry_code_15 = (String) FileUtils.byte2Parm(industry_code_15, Type.HEX);

        //预留
        byte[] reserved_1 = new byte[2];
        arraycopy(date, i, reserved_1, 0, reserved_1.length);
        i += reserved_1.length;
        this.reserved_1 = (String) FileUtils.byte2Parm(reserved_1, Type.HEX);
        //卡片启用标志
        byte[] enable_flag_15 = new byte[1];
        arraycopy(date, i, enable_flag_15, 0, enable_flag_15.length);
        i += enable_flag_15.length;
        this.enable_flag_15 = (String) FileUtils.byte2Parm(enable_flag_15, Type.HEX);

        //应用版本号
        byte[] application_version_15 = new byte[1];
        arraycopy(date, i, application_version_15, 0, application_version_15.length);
        i += application_version_15.length;
        this.application_version_15 = (String) FileUtils.byte2Parm(application_version_15, Type.HEX);

        //互通标识
        byte[] each_logo = new byte[2];
        arraycopy(date, i, each_logo, 0, each_logo.length);
        i += each_logo.length;
        this.each_logo = (String) FileUtils.byte2Parm(each_logo, Type.HEX);

        //应用序列号
        byte[] serial_number_15 = new byte[8];
        arraycopy(date, i, serial_number_15, 0, serial_number_15.length);
        i += serial_number_15.length;
        this.serial_number_15 = (String) FileUtils.byte2Parm(serial_number_15, Type.HEX);
        //启用日期
        byte[] enable_date = new byte[4];
        arraycopy(date, i, enable_date, 0, enable_date.length);
        i += enable_date.length;
        this.enable_date = (String) FileUtils.byte2Parm(enable_date, Type.HEX);

        //有效日期
        byte[] valid_date_15 = new byte[4];
        arraycopy(date, i, valid_date_15, 0, valid_date_15.length);
        i += valid_date_15.length;
        this.valid_date_15 = (String) FileUtils.byte2Parm(valid_date_15, Type.HEX);

        //预留
        byte[] reserved_2 = new byte[2];
        arraycopy(date, i, reserved_2, 0, reserved_2.length);
        i += reserved_2.length;
        this.reserved_2 = (String) FileUtils.byte2Parm(reserved_2, Type.HEX);

        return i;
    }

    public String getValid_date_15() {
        return valid_date_15;
    }

    public void setValid_date_15(String valid_date_15) {
        this.valid_date_15 = valid_date_15;
    }

    public String getIssuer_code_15() {
        return issuer_code_15;
    }
}
