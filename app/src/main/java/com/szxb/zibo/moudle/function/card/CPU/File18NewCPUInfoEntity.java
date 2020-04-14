package com.szxb.zibo.moudle.function.card.CPU;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class File18NewCPUInfoEntity {
    //--------------------------------------------------18文件
    String transaction_number_18;    //交易序号
    String reserve_3;                //预留
    String transaction_amount;        //交易金额
    String transaction_type;            //交易类型标识
    String pose_id_18;                    //终端机编号
    String transaction_time;        //交易时间

    public int praseFile(int i, byte[] date) {
        Log.i("File15LocalInfoEntity", FileUtils.bytesToHexString(date));
        //交易序号
        byte[] transaction_number_18 = new byte[2];
        arraycopy(date, i, transaction_number_18, 0, transaction_number_18.length);
        i += transaction_number_18.length;
        this.transaction_number_18 = (String) FileUtils.byte2Parm(transaction_number_18, Type.HEX);

        //预留
        byte[] reserve_3 = new byte[3];
        arraycopy(date, i, reserve_3, 0, reserve_3.length);
        i += reserve_3.length;
        this.reserve_3 = (String) FileUtils.byte2Parm(reserve_3, Type.HEX);

        //交易金额
        byte[] transaction_amount = new byte[4];
        arraycopy(date, i, transaction_amount, 0, transaction_amount.length);
        i += transaction_amount.length;
        this.transaction_amount = (String) FileUtils.byte2Parm(transaction_amount, Type.HEX);

        //交易类型标识
        byte[] transaction_type = new byte[1];
        arraycopy(date, i, transaction_type, 0, transaction_type.length);
        i += transaction_type.length;
        this.transaction_type = (String) FileUtils.byte2Parm(transaction_type, Type.HEX);

        //终端机编号
        byte[] pose_id_18 = new byte[6];
        arraycopy(date, i, pose_id_18, 0, pose_id_18.length);
        i += pose_id_18.length;
        this.pose_id_18 = (String) FileUtils.byte2Parm(pose_id_18, Type.HEX);

        //交易时间
        byte[] transaction_time = new byte[7];
        arraycopy(date, i, transaction_time, 0, transaction_time.length);
        i += transaction_time.length;
        this.transaction_time = (String) FileUtils.byte2Parm(transaction_time, Type.HEX);

        return i;
    }

    public String getTransaction_number_18() {
        return transaction_number_18;
    }

    public String getReserve_3() {
        return reserve_3;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public String getPose_id_18() {
        return pose_id_18;
    }

    public String getTransaction_time() {
        return transaction_time;
    }
}
