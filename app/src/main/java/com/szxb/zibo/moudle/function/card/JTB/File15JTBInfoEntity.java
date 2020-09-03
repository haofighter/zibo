package com.szxb.zibo.moudle.function.card.JTB;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

//-------------------------------15文件
public class File15JTBInfoEntity {
    String card_issuer;            //发卡机构标识 判断白名单依据之一（前四字节）
    String application_type;        // 应用类型标识
    String enable_identification;    //发卡机构应用版本（启用标识） 00 未启用  01 启用
    String pan;                  //卡内号
    String enabling_time;         //启用日期
    String valid_time;            //有效日期
    String fci_data;            //发卡机构自定义FCI数据


    public int praseFile(int i, byte[] date) {

        byte[] card_issuer = new byte[8];
        arraycopy(date, i, card_issuer, 0, card_issuer.length);
        i += card_issuer.length;
        this.card_issuer = (String) FileUtils.byte2Parm(card_issuer, Type.HEX);

        byte[] application_type = new byte[1];
        arraycopy(date, i, application_type, 0, application_type.length);
        i += application_type.length;
        this.application_type = (String) FileUtils.byte2Parm(application_type, Type.HEX);

        byte[] enable_identification = new byte[1];
        arraycopy(date, i, enable_identification, 0, enable_identification.length);
        i += enable_identification.length;
        this.enable_identification = (String) FileUtils.byte2Parm(enable_identification, Type.HEX);

        byte[] pan = new byte[10];
        arraycopy(date, i, pan, 0, pan.length);
        i += pan.length;
        this.pan = (String) FileUtils.byte2Parm(pan, Type.HEX);

        byte[] enabling_time = new byte[4];
        arraycopy(date, i, enabling_time, 0, enabling_time.length);
        i += enabling_time.length;
        this.enabling_time = (String) FileUtils.byte2Parm(enabling_time, Type.HEX);


        //boarding_time[7];                //上车时间
        byte[] valid_time = new byte[4];
        arraycopy(date, i, valid_time, 0, valid_time.length);
        i += valid_time.length;
        this.valid_time = (String) FileUtils.byte2Parm(valid_time, Type.HEX);

        //boarding_time[2];                //上车时间
        byte[] fci_data = new byte[2];
        arraycopy(date, i, fci_data, 0, fci_data.length);
        i += fci_data.length;
        this.fci_data = (String) FileUtils.byte2Parm(fci_data, Type.HEX);


        return i;
    }

    public String getCard_issuer() {
        return card_issuer;
    }

    public String getPan() {
        return pan;
    }
}
