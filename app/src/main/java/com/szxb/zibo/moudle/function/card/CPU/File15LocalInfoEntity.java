package com.szxb.zibo.moudle.function.card.CPU;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

//-------------------------------15文件
public class File15LocalInfoEntity {
    String card_issuer;                    //发卡机构标识
    String version_number;                    //应用版本号
    String city_code;                    //城市代码
    String card_type;                        //卡类型
    String pan;                          //卡内号
    String enabling_time;                 //启用日期
    String valid_time;                    //有效日期
    String sign_name;                        //是否记名
    String cash_pledge;                    //押金
    String min_amount;                    //最低余额限制
    String using_mark;                        //使用标记
    String max_amount;                    //最高消费金额
    String online_account_mark;            //联机账户标记
    String taxi_driver_card;                //出租车司机卡标志
    String reserve;                        //预留


    public int praseFile15Local(int i, byte[] date) {
        Log.i("File15LocalInfoEntity",FileUtils.bytesToHexString(date));
        //发卡机构标识
        byte[] card_issuer = new byte[8];
        arraycopy(date, i, card_issuer, 0, card_issuer.length);
        i += card_issuer.length;
        this.card_issuer = (String) FileUtils.byte2Parm(card_issuer, Type.HEX);

        //应用版本号
        byte[] version_number = new byte[1];
        arraycopy(date, i, version_number, 0, version_number.length);
        i += version_number.length;
        this.version_number = (String) FileUtils.byte2Parm(version_number, Type.HEX);

        //城市代码
        byte[] city_code = new byte[2];
        arraycopy(date, i, city_code, 0, city_code.length);
        i += city_code.length;
        this.city_code = (String) FileUtils.byte2Parm(city_code, Type.HEX);

        //卡类型
        byte[] card_type = new byte[1];
        arraycopy(date, i, card_type, 0, card_type.length);
        i += card_type.length;
        this.card_type = (String) FileUtils.byte2Parm(card_type, Type.HEX);

        //卡内号
        byte[] pan = new byte[10];
        arraycopy(date, i, pan, 0, pan.length);
        i += pan.length;
        this.pan = (String) FileUtils.byte2Parm(pan, Type.HEX);

        //enabling_time[4];                 //启用日期
        byte[] enabling_time = new byte[4];
        arraycopy(date, i, enabling_time, 0, enabling_time.length);
        i += enabling_time.length;
        this.enabling_time = (String) FileUtils.byte2Parm(enabling_time, Type.HEX);

        //valid_time[4];                    //有效日期
        byte[] valid_time = new byte[4];
        arraycopy(date, i, valid_time, 0, valid_time.length);
        i += valid_time.length;
        this.valid_time = (String) FileUtils.byte2Parm(valid_time, Type.HEX);

        //sign_name;                        //是否记名
        byte[] sign_name = new byte[1];
        arraycopy(date, i, sign_name, 0, sign_name.length);
        i += sign_name.length;
        this.sign_name = (String) FileUtils.byte2Parm(sign_name, Type.HEX);

        //cash_pledge[2];                    //押金
        byte[] cash_pledge = new byte[2];
        arraycopy(date, i, cash_pledge, 0, cash_pledge.length);
        i += cash_pledge.length;
        this.cash_pledge = (String) FileUtils.byte2Parm(cash_pledge, Type.HEX);

        //min_amount[2];                    //最低余额限制
        byte[] min_amount = new byte[2];
        arraycopy(date, i, min_amount, 0, min_amount.length);
        i += min_amount.length;
        this.min_amount = (String) FileUtils.byte2Parm(min_amount, Type.HEX);

        //using_mark;                        //使用标记
        byte[] using_mark = new byte[1];
        arraycopy(date, i, using_mark, 0, using_mark.length);
        i += using_mark.length;
        this.using_mark = (String) FileUtils.byte2Parm(using_mark, Type.HEX);

        //max_amount[4];                    //最高消费金额
        byte[] max_amount = new byte[4];
        arraycopy(date, i, max_amount, 0, max_amount.length);
        i += max_amount.length;
        this.max_amount = (String) FileUtils.byte2Parm(max_amount, Type.HEX);

        //online_account_mark;            //联机账户标记
        byte[] online_account_mark = new byte[1];
        arraycopy(date, i, online_account_mark, 0, online_account_mark.length);
        i += online_account_mark.length;
        this.online_account_mark = (String) FileUtils.byte2Parm(online_account_mark, Type.HEX);

        //taxi_driver_card;                //出租车司机卡标志
        byte[] taxi_driver_card = new byte[1];
        arraycopy(date, i, taxi_driver_card, 0, taxi_driver_card.length);
        i += taxi_driver_card.length;
        this.taxi_driver_card = (String) FileUtils.byte2Parm(taxi_driver_card, Type.HEX);

        //reserve[8];                        //预留
        byte[] reserve = new byte[8];
        arraycopy(date, i, reserve, 0, reserve.length);
        i += reserve.length;
        this.reserve = (String) FileUtils.byte2Parm(reserve, Type.HEX);

        return i;
    }

    public String getCard_issuer() {
        return card_issuer;
    }

    public String getVersion_number() {
        return version_number;
    }

    public String getCity_code() {
        return city_code;
    }

    public String getCard_type() {
        return card_type;
    }

    public String getPan() {
        return pan;
    }

    public String getEnabling_time() {
        return enabling_time;
    }

    public String getValid_time() {
        return valid_time;
    }

    public String getSign_name() {
        return sign_name;
    }

    public String getCash_pledge() {
        return cash_pledge;
    }

    public String getMin_amount() {
        return min_amount;
    }

    public String getUsing_mark() {
        return using_mark;
    }

    public String getMax_amount() {
        return max_amount;
    }

    public String getOnline_account_mark() {
        return online_account_mark;
    }

    public String getTaxi_driver_card() {
        return taxi_driver_card;
    }

    public String getReserve() {
        return reserve;
    }
}
