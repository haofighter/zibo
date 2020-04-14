package com.szxb.zibo.moudle.function.card.CPU;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class File10NewCPUInfoEntity {
    //--------------------------------------------------18文件
    String transaction_number_10;    //交易序号
    String reserve_4;                //预留
    String transaction_amount_10;        //交易金额
    String transaction_type_10;            //交易类型标识
    String pose_id_10;                    //终端机编号
    String transaction_time_10;        //交易时间

    public int praseFile(int i, byte[] date) {
        Log.i("File15LocalInfoEntity", FileUtils.bytesToHexString(date));
        //交易序号
        byte[] transaction_number_10 = new byte[2];
        arraycopy(date, i, transaction_number_10, 0, transaction_number_10.length);
        i += transaction_number_10.length;
        this.transaction_number_10 = (String) FileUtils.byte2Parm(transaction_number_10, Type.HEX);

        //预留
        byte[] reserve_4 = new byte[3];
        arraycopy(date, i, reserve_4, 0, reserve_4.length);
        i += reserve_4.length;
        this.reserve_4 = (String) FileUtils.byte2Parm(reserve_4, Type.HEX);

        //交易金额
        byte[] transaction_amount_10 = new byte[4];
        arraycopy(date, i, transaction_amount_10, 0, transaction_amount_10.length);
        i += transaction_amount_10.length;
        this.transaction_amount_10 = (String) FileUtils.byte2Parm(transaction_amount_10, Type.HEX);

        //交易类型标识
        byte[] transaction_type_10 = new byte[1];
        arraycopy(date, i, transaction_type_10, 0, transaction_type_10.length);
        i += transaction_type_10.length;
        this.transaction_type_10 = (String) FileUtils.byte2Parm(transaction_type_10, Type.HEX);

        //终端机编号
        byte[] pose_id_10 = new byte[6];
        arraycopy(date, i, pose_id_10, 0, pose_id_10.length);
        i += pose_id_10.length;
        this.pose_id_10 = (String) FileUtils.byte2Parm(pose_id_10, Type.HEX);

        //交易时间
        byte[] transaction_time_10 = new byte[7];
        arraycopy(date, i, transaction_time_10, 0, transaction_time_10.length);
        i += transaction_time_10.length;
        this.transaction_time_10 = (String) FileUtils.byte2Parm(transaction_time_10, Type.HEX);

        return i;
    }


}
