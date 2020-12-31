package com.szxb.zibo.moudle.function.card.M1;


import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_4 {
    //块0x4
    String city_code;                    //城市代码
    String industry_code;                //行业代码
    String issuer_code;                    //发行流水号
    String authentication;                //卡认证码
    String enable_type;                    //启用标志
    String card_type;                        //卡类型
    String cash_pledge;                    //押金

    public int praseBlock(int i, byte[] date) {
        int start = i;
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

        //发行流水号
        byte[] issuer_code = new byte[4];
        arraycopy(date, i, issuer_code, 0, issuer_code.length);
        i += issuer_code.length;
        this.issuer_code = (String) FileUtils.byte2Parm(issuer_code, Type.HEX);

        //卡认证码
        byte[] authentication = new byte[4];
        arraycopy(date, i, authentication, 0, authentication.length);
        i += authentication.length;
        this.authentication = (String) FileUtils.byte2Parm(authentication, Type.HEX);

        //启用标志
        byte[] enable_type = new byte[1];
        arraycopy(date, i, enable_type, 0, enable_type.length);
        i += enable_type.length;
        this.enable_type = (String) FileUtils.byte2Parm(enable_type, Type.HEX);

        //卡类型
        byte[] card_type = new byte[1];
        arraycopy(date, i, card_type, 0, card_type.length);
        i += card_type.length;
        this.card_type = (String) FileUtils.byte2Parm(card_type, Type.HEX);


        //押金
        byte[] cash_pledge = new byte[2];
        arraycopy(date, i, cash_pledge, 0, cash_pledge.length);
        i += cash_pledge.length;
        this.cash_pledge = (String) FileUtils.byte2Parm(cash_pledge, Type.HEX);

        return i;
    }

    public String getCity_code() {
        return city_code;
    }

    public String getIndustry_code() {
        return industry_code;
    }

    public String getIssuer_code() {
        return issuer_code;
    }

    public String getAuthentication() {
        return authentication;
    }

    public String getEnable_type() {
        return enable_type;
    }

    public String getCard_type() {
        return card_type;
    }

    public String getCash_pledge() {
        return cash_pledge;
    }

    @Override
    public String toString() {
        return "Block_4{" + city_code + industry_code
                + issuer_code + authentication + enable_type
                + card_type + cash_pledge +
                '}';
    }
}
