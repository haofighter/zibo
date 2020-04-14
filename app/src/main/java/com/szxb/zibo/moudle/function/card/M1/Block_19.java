package com.szxb.zibo.moudle.function.card.M1;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_19 {
    //块0x19
    String load_transaction_number_2;    //钱包圈存交易次数            //预留
    String transaction_number_2;        //钱包消费交易次数            //刷卡次数
    String transaction_type_2;                //交易类型                    //交易类型标识
    String transaction_money_2;            //交易金额                    //预留
    String blacklist_type_2;                //黑名单标志                    //黑名单标志
    String load_point_2;                    //圈存记录指针                //预留
    String record_pointer_2;                //消费记录指针                //刷卡记录指针
    String interval_marker_2;                //区间标志位                    //预留
    String reserved2_2;                    //预留
    String verify2_2;                    //校验                     //校验

    public int praseBlock(int i, byte[] date) {
        //钱包圈存交易次数
        byte[] load_transaction_number_2 = new byte[2];
        arraycopy(date, i, load_transaction_number_2, 0, load_transaction_number_2.length);
        i += load_transaction_number_2.length;
        this.load_transaction_number_2 = (String) FileUtils.byte2Parm(load_transaction_number_2, Type.HEX);

        //钱包消费交易次数
        byte[] transaction_number_2 = new byte[2];
        arraycopy(date, i, transaction_number_2, 0, transaction_number_2.length);
        i += transaction_number_2.length;
        this.transaction_number_2 = (String) FileUtils.byte2Parm(transaction_number_2, Type.HEX);

        //交易类型
        byte[] transaction_type_2 = new byte[1];
        arraycopy(date, i, transaction_type_2, 0, transaction_type_2.length);
        i += transaction_type_2.length;
        this.transaction_type_2 = (String) FileUtils.byte2Parm(transaction_type_2, Type.HEX);

        //交易金额
        byte[] transaction_money_2 = new byte[2];
        arraycopy(date, i, transaction_money_2, 0, transaction_money_2.length);
        i += transaction_money_2.length;
        this.transaction_money_2 = (String) FileUtils.byte2Parm(transaction_money_2, Type.HEX);

        //黑名单标志
        byte[] blacklist_type_2 = new byte[1];
        arraycopy(date, i, blacklist_type_2, 0, blacklist_type_2.length);
        i += blacklist_type_2.length;
        this.blacklist_type_2 = (String) FileUtils.byte2Parm(blacklist_type_2, Type.HEX);

        //圈存记录指针
        byte[] load_point_2 = new byte[1];
        arraycopy(date, i, load_point_2, 0, load_point_2.length);
        i += load_point_2.length;
        this.load_point_2 = (String) FileUtils.byte2Parm(load_point_2, Type.HEX);

        //消费记录指针
        byte[] record_pointer_2 = new byte[1];
        arraycopy(date, i, record_pointer_2, 0, record_pointer_2.length);
        i += record_pointer_2.length;
        this.record_pointer_2 = (String) FileUtils.byte2Parm(record_pointer_2, Type.HEX);

        //区间标志位
        byte[] interval_marker_2 = new byte[1];
        arraycopy(date, i, interval_marker_2, 0, interval_marker_2.length);
        i += interval_marker_2.length;
        this.interval_marker_2 = (String) FileUtils.byte2Parm(interval_marker_2, Type.HEX);

        //预留
        byte[] reserved2_2 = new byte[1];
        arraycopy(date, i, reserved2_2, 0, reserved2_2.length);
        i += reserved2_2.length;
        this.reserved2_2 = (String) FileUtils.byte2Parm(reserved2_2, Type.HEX);


        //校验
        byte[] verify2_2 = new byte[4];
        arraycopy(date, i, verify2_2, 0, verify2_2.length);
        i += verify2_2.length;
        this.verify2_2 = (String) FileUtils.byte2Parm(verify2_2, Type.HEX);
        return i;
    }

    @Override
    public String toString() {
        return load_transaction_number_2 + transaction_number_2 + transaction_type_2 + transaction_money_2 + blacklist_type_2 + load_point_2 + record_pointer_2 + interval_marker_2 + reserved2_2 + verify2_2;
    }

    public String getBlacklist_type_2() {
        return blacklist_type_2;
    }

    public void setBlacklist_type_2(String blacklist_type_2) {
        this.blacklist_type_2 = blacklist_type_2;
    }
}
