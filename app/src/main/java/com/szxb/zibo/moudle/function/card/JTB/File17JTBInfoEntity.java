package com.szxb.zibo.moudle.function.card.JTB;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

//-------------------------------17文件
public class File17JTBInfoEntity {
    String international_code;    //国际代码  4
    String province_code;        //省级代码  2
    String city_code;            //城市代码  2
    String interflow_flag;        //互通标识 2
    String card_type;                 //互通卡种类型 1


    public int praseFile(int i, byte[] date) {

        byte[] international_code = new byte[4];
        arraycopy(date, i, international_code, 0, international_code.length);
        i += international_code.length;
        this.international_code = (String) FileUtils.byte2Parm(international_code, Type.HEX);

        byte[] province_code = new byte[2];
        arraycopy(date, i, province_code, 0, province_code.length);
        i += province_code.length;
        this.province_code = (String) FileUtils.byte2Parm(province_code, Type.HEX);

        byte[] city_code = new byte[2];
        arraycopy(date, i, city_code, 0, city_code.length);
        i += city_code.length;
        this.city_code = (String) FileUtils.byte2Parm(city_code, Type.HEX);

        byte[] interflow_flag = new byte[2];
        arraycopy(date, i, interflow_flag, 0, interflow_flag.length);
        i += interflow_flag.length;
        this.interflow_flag = (String) FileUtils.byte2Parm(interflow_flag, Type.HEX);

        byte[] card_type = new byte[1];
        arraycopy(date, i, card_type, 0, card_type.length);
        i += card_type.length;
        this.card_type = (String) FileUtils.byte2Parm(card_type, Type.HEX);

        return i;
    }

    public String getCard_type() {
        return card_type;
    }
}
