package com.szxb.zibo.moudle.function.card.CPU;

import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.lib.Util.Type;

import static java.lang.System.arraycopy;

public class File16NewCPUInfoEntity {
    //--------------------------------------------------持卡人基本信息文件（0016）
    String type_identifier;            //持卡人类型标识
    String work_identifier;            //持卡人职工标识
    String name;                    //持卡人姓名
    String document_number;        //持卡人证件号码
    String driverNum;        //司机编号
    String res;        //预留
    String document_type;                //持卡人证件类型

    public int praseFile(int i, byte[] date) {
        byte[] carddate = new byte[55];
        arraycopy(date, i, carddate, 0, carddate.length);
        MiLog.i("16卡数据", (String) FileUtils.byte2Parm(carddate, Type.HEX));

        //持卡人类型标识
        byte[] type_identifier = new byte[1];
        arraycopy(date, i, type_identifier, 0, type_identifier.length);
        i += type_identifier.length;
        this.type_identifier = (String) FileUtils.byte2Parm(type_identifier, Type.HEX);

        //持卡人职工标识
        byte[] work_identifier = new byte[1];
        arraycopy(date, i, work_identifier, 0, work_identifier.length);
        i += work_identifier.length;
        this.work_identifier = (String) FileUtils.byte2Parm(work_identifier, Type.HEX);

        //持卡人姓名
        byte[] name = new byte[20];
        arraycopy(date, i, name, 0, name.length);
        i += name.length;
        this.name = (String) FileUtils.byte2Parm(name, Type.HEX);

        //持卡人证件号码
        byte[] document_number = new byte[27];
        arraycopy(date, i, document_number, 0, document_number.length);
        i += document_number.length;
        this.document_number = (String) FileUtils.byte2Parm(document_number, Type.HEX);

        //持卡人证件号码
        byte[] driverNum = new byte[3];
        arraycopy(date, i, driverNum, 0, driverNum.length);
        i += driverNum.length;
        this.driverNum = (String) FileUtils.byte2Parm(driverNum, Type.HEX);

        //持卡人证件号码
        byte[] res = new byte[2];
        arraycopy(date, i, res, 0, res.length);
        i += res.length;
        this.res = (String) FileUtils.byte2Parm(res, Type.HEX);


        //卡片启用标志
        byte[] document_type = new byte[1];
        arraycopy(date, i, document_type, 0, document_type.length);
        i += document_type.length;
        this.document_type = (String) FileUtils.byte2Parm(document_type, Type.HEX);

        return i;
    }


    public String getDriverNum() {
        return driverNum;
    }
}
