package com.szxb.zibo.moudle.function.card.M1;


import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_8 {
    //块0x7
    String cumulative_premium_value;    //累计加款值
    String reserved1;                    //预留空间
    public int praseBlock(int i, byte[] date) {
        //累计加款值
        byte[] cumulative_premium_value = new byte[4];
        arraycopy(date, i, cumulative_premium_value, 0, cumulative_premium_value.length);
        i += cumulative_premium_value.length;
        this.cumulative_premium_value = (String) FileUtils.byte2Parm(cumulative_premium_value, Type.HEX);

        //预留空间
        byte[] reserved1 = new byte[12];
        arraycopy(date, i, reserved1, 0, reserved1.length);
        i += reserved1.length;
        this.reserved1 = (String) FileUtils.byte2Parm(reserved1, Type.HEX);
        return i;
    }

    @Override
    public String toString() {
        return "Block_8{" + cumulative_premium_value  + reserved1 +
                '}';
    }
}
