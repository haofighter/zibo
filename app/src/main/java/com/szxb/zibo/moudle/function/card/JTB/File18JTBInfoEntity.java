package com.szxb.zibo.moudle.function.card.JTB;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

//-------------------------------18文件
public class File18JTBInfoEntity {
    String transaction_number_18;    //交易序号
    String overdrawn_account;    //透支限额
    String transaction_amount;    //交易金额
    String transaction_type;        //交易类型标识
    String pose_id;                //终端机编号
    String transaction_time;    //交易时间


    public int praseFile(int i, byte[] date) {

        byte[] transaction_number_18 = new byte[2];
        arraycopy(date, i, transaction_number_18, 0, transaction_number_18.length);
        i += transaction_number_18.length;
        this.transaction_number_18 = (String) FileUtils.byte2Parm(transaction_number_18, Type.HEX);

        byte[] overdrawn_account = new byte[3];
        arraycopy(date, i, overdrawn_account, 0, overdrawn_account.length);
        i += overdrawn_account.length;
        this.overdrawn_account = (String) FileUtils.byte2Parm(overdrawn_account, Type.HEX);

        byte[] transaction_amount = new byte[4];
        arraycopy(date, i, transaction_amount, 0, transaction_amount.length);
        i += transaction_amount.length;
        this.transaction_amount = (String) FileUtils.byte2Parm(transaction_amount, Type.HEX);

        byte[] transaction_type = new byte[1];
        arraycopy(date, i, transaction_type, 0, transaction_type.length);
        i += transaction_type.length;
        this.transaction_type = (String) FileUtils.byte2Parm(transaction_type, Type.HEX);

        byte[] pose_id = new byte[6];
        arraycopy(date, i, pose_id, 0, pose_id.length);
        i += pose_id.length;
        this.pose_id = (String) FileUtils.byte2Parm(pose_id, Type.HEX);

        byte[] transaction_time = new byte[7];
        arraycopy(date, i, transaction_time, 0, transaction_time.length);
        i += transaction_time.length;
        this.transaction_time = (String) FileUtils.byte2Parm(transaction_time, Type.HEX);

        return i;
    }

    public String getTransaction_number_18() {
        return transaction_number_18;
    }
}
