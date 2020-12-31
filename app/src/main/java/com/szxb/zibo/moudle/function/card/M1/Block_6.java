package com.szxb.zibo.moudle.function.card.M1;


import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_6 {
    //块0x6
    String add_money_time;                //加款时间，年月日时
    String original_amount;                //原额
    String add_money_num;                //首次加款值
    String amount;                        //余额
    String operator_num;                //操作员编号

    public int praseBlock(int i, byte[] date) {
        //加款时间，年月日时
        byte[] add_money_time = new byte[4];
        arraycopy(date, i, add_money_time, 0, add_money_time.length);
        i += add_money_time.length;
        this.add_money_time = (String) FileUtils.byte2Parm(add_money_time, Type.HEX);

        //原额
        byte[] original_amount = new byte[3];
        arraycopy(date, i, original_amount, 0, original_amount.length);
        i += original_amount.length;
        this.original_amount = (String) FileUtils.byte2Parm(original_amount, Type.HEX);

        //首次加款值
        byte[] add_money_num = new byte[3];
        arraycopy(date, i, add_money_num, 0, add_money_num.length);
        i += add_money_num.length;
        this.add_money_num = (String) FileUtils.byte2Parm(add_money_num, Type.HEX);

        //余额
        byte[] amount = new byte[3];
        arraycopy(date, i, amount, 0, amount.length);
        i += amount.length;
        this.amount = (String) FileUtils.byte2Parm(amount, Type.HEX);

        //操作员编号
        byte[] operator_num = new byte[3];
        arraycopy(date, i, operator_num, 0, operator_num.length);
        i += operator_num.length;
        this.operator_num = (String) FileUtils.byte2Parm(operator_num, Type.HEX);
        return i;
    }

    @Override
    public String toString() {
        return "Block_6{" + add_money_time  + original_amount + add_money_num  + amount  + operator_num +
                '}';
    }
}
