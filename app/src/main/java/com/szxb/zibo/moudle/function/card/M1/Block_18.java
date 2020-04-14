package com.szxb.zibo.moudle.function.card.M1;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_18 {
    //块0x18                                //消费卡                        //管理卡数据
    public String load_transaction_number;        //钱包圈存交易次数            //预留
    public String transaction_number;            //钱包消费交易次数            //刷卡次数
    public String transaction_type;                //交易类型                    //交易类型标识
    public String transaction_money;            //交易金额                    //预留
    public String blacklist_type;                     //黑名单标志                    //黑名单标志
    public String load_point;                        //圈存记录指针                //预留
    public String record_pointer;                    //消费记录指针                //刷卡记录指针
    public String interval_marker;                //区间标志位                    //预留
    public String reserved2;                        //预留
    public String verify2;                        //校验

    public int praseBlock(int i, byte[] date) {
        //钱包圈存交易次数
        byte[] load_transaction_number = new byte[2];
        arraycopy(date, i, load_transaction_number, 0, load_transaction_number.length);
        i += load_transaction_number.length;
        this.load_transaction_number = (String) FileUtils.byte2Parm(load_transaction_number, Type.HEX);

        //钱包消费交易次数
        byte[] transaction_number = new byte[2];
        arraycopy(date, i, transaction_number, 0, transaction_number.length);
        i += transaction_number.length;
        this.transaction_number = (String) FileUtils.byte2Parm(transaction_number, Type.HEX);

        //交易类型
        byte[] transaction_type = new byte[1];
        arraycopy(date, i, transaction_type, 0, transaction_type.length);
        i += transaction_type.length;
        this.transaction_type = (String) FileUtils.byte2Parm(transaction_type, Type.HEX);

        //交易金额
        byte[] transaction_money = new byte[2];
        arraycopy(date, i, transaction_money, 0, transaction_money.length);
        i += transaction_money.length;
        this.transaction_money = (String) FileUtils.byte2Parm(transaction_money, Type.HEX);

        //黑名单标志
        byte[] blacklist_type = new byte[1];
        arraycopy(date, i, blacklist_type, 0, blacklist_type.length);
        i += blacklist_type.length;
        this.blacklist_type = (String) FileUtils.byte2Parm(blacklist_type, Type.HEX);

        //圈存记录指针
        byte[] load_point = new byte[1];
        arraycopy(date, i, load_point, 0, load_point.length);
        i += load_point.length;
        this.load_point = (String) FileUtils.byte2Parm(load_point, Type.HEX);

        //消费记录指针
        byte[] record_pointer = new byte[1];
        arraycopy(date, i, record_pointer, 0, record_pointer.length);
        i += record_pointer.length;
        this.record_pointer = (String) FileUtils.byte2Parm(record_pointer, Type.HEX);

        //区间标志位
        byte[] interval_marker = new byte[1];
        arraycopy(date, i, interval_marker, 0, interval_marker.length);
        i += interval_marker.length;
        this.interval_marker = (String) FileUtils.byte2Parm(interval_marker, Type.HEX);

        //预留
        byte[] reserved2 = new byte[1];
        arraycopy(date, i, reserved2, 0, reserved2.length);
        i += reserved2.length;
        this.reserved2 = (String) FileUtils.byte2Parm(reserved2, Type.HEX);

        //校验
        byte[] verify2 = new byte[4];
        arraycopy(date, i, verify2, 0, verify2.length);
        i += verify2.length;
        this.verify2 = (String) FileUtils.byte2Parm(verify2, Type.HEX);
        return i;
    }


    @Override
    public String toString() {
        return "Block_18{" + load_transaction_number +  transaction_number + transaction_type + transaction_money +  blacklist_type +load_point + record_pointer +interval_marker  + reserved2  + verify2 +
                '}';
    }

    public void setBlacklist_type(String blacklist_type) {
        this.blacklist_type = blacklist_type;
    }
}
