package com.szxb.zibo.moudle.function.card.CPU;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

//-------------------------------05文件
public class File05NewCPUInfoEntity {
    //--------------------------------------------------发行基本信息文件（0005）
    String issuer_code;            //发卡方代码
    String city_code;            //城市代码
    String industry_code;        //行业代码
    String county_code;            //县区代码
    String bus_company_code;    //公交公司代码
    String serial_number;        //应用序列号
    String card_main_type;            //卡主类型
    String card_type;                //卡子类型
    String release_date;        //发行日期
    String release_device_information;//发行设备信息
    String application_version;    //应用版本号
    String enable_flag;            //卡片启用标志
    String sale_date;            //售卡日期
    String valid_date;            //有效日期
    String deposit;                //押金
    String reserved_0;            //预留


    public int praseFile(int i, byte[] date) {
        Log.i("File15LocalInfoEntity", FileUtils.bytesToHexString(date));
        //发卡方代码
        byte[] issuer_code = new byte[2];
        arraycopy(date, i, issuer_code, 0, issuer_code.length);
        i += issuer_code.length;
        this.issuer_code = (String) FileUtils.byte2Parm(issuer_code, Type.HEX);

        //城市代码
        byte[] city_code = new byte[2];
        arraycopy(date, i, city_code, 0, city_code.length);
        i += city_code.length;
        this.city_code = (String) FileUtils.byte2Parm(city_code, Type.HEX);

        //行业代码
        byte[] industry_code = new byte[2];
        arraycopy(date, i, industry_code, 0, industry_code.length);
        i += industry_code.length;
        this.industry_code = (String) FileUtils.byte2Parm(industry_code, Type.HEX);

        //县区代码
        byte[] county_code = new byte[2];
        arraycopy(date, i, county_code, 0, county_code.length);
        i += county_code.length;
        this.county_code = (String) FileUtils.byte2Parm(county_code, Type.HEX);

        //公交公司代码
        byte[] bus_company_code = new byte[2];
        arraycopy(date, i, bus_company_code, 0, bus_company_code.length);
        i += bus_company_code.length;
        this.bus_company_code = (String) FileUtils.byte2Parm(bus_company_code, Type.HEX);

        //应用序列号
        byte[] serial_number = new byte[8];
        arraycopy(date, i, serial_number, 0, serial_number.length);
        i += serial_number.length;
        this.serial_number = (String) FileUtils.byte2Parm(serial_number, Type.HEX);

        //卡主类型
        byte[] card_main_type = new byte[1];
        arraycopy(date, i, card_main_type, 0, card_main_type.length);
        i += card_main_type.length;
        this.card_main_type = (String) FileUtils.byte2Parm(card_main_type, Type.HEX);

        //卡子类型
        byte[] card_type = new byte[1];
        arraycopy(date, i, card_type, 0, card_type.length);
        i += card_type.length;
        this.card_type = (String) FileUtils.byte2Parm(card_type, Type.HEX);

        //发行日期
        byte[] release_date = new byte[4];
        arraycopy(date, i, release_date, 0, release_date.length);
        i += release_date.length;
        this.release_date = (String) FileUtils.byte2Parm(release_date, Type.HEX);

        //发行设备信息
        byte[] release_device_information = new byte[6];
        arraycopy(date, i, release_device_information, 0, release_device_information.length);
        i += release_device_information.length;
        this.release_device_information = (String) FileUtils.byte2Parm(release_device_information, Type.HEX);

        //应用版本号
        byte[] application_version = new byte[2];
        arraycopy(date, i, application_version, 0, application_version.length);
        i += application_version.length;
        this.application_version = (String) FileUtils.byte2Parm(application_version, Type.HEX);

        //卡片启用标志
        byte[] enable_flag = new byte[1];
        arraycopy(date, i, enable_flag, 0, enable_flag.length);
        i += enable_flag.length;
        this.enable_flag = (String) FileUtils.byte2Parm(enable_flag, Type.HEX);

        //售卡日期
        byte[] sale_date = new byte[4];
        arraycopy(date, i, sale_date, 0, sale_date.length);
        i += sale_date.length;
        this.sale_date = (String) FileUtils.byte2Parm(sale_date, Type.HEX);

        //有效日期
        byte[] valid_date = new byte[4];
        arraycopy(date, i, valid_date, 0, valid_date.length);
        i += valid_date.length;
        this.valid_date = (String) FileUtils.byte2Parm(valid_date, Type.HEX);

        //押金
        byte[] deposit = new byte[1];
        arraycopy(date, i, deposit, 0, deposit.length);
        i += deposit.length;
        this.deposit = (String) FileUtils.byte2Parm(deposit, Type.HEX);

        //预留
        byte[] reserved_0 = new byte[6];
        arraycopy(date, i, reserved_0, 0, reserved_0.length);
        i += reserved_0.length;
        this.reserved_0 = (String) FileUtils.byte2Parm(reserved_0, Type.HEX);

        return i;
    }


    public String getCard_main_type() {
        return card_main_type;
    }

    public String getCard_type() {
        return card_type;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public String getValid_date() {
        return valid_date;
    }
}
