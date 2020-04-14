package com.szxb.zibo.moudle.function.card.M1;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_5 {
    //块0x5
    String release_time;                 //发行日期
    String valid_time;                    //有效日期
    public String child_cardType;                    //卡子类型
    String reserved;                    //预留空间
    String add_money_pos_num;            //首次加款pos

    public int praseBlock(int i, byte[] date) {
        //发行日期
        byte[] release_time = new byte[4];
        arraycopy(date, i, release_time, 0, release_time.length);
        i += release_time.length;
        this.release_time = (String) FileUtils.byte2Parm(release_time, Type.HEX);

        //有效日期
        byte[] valid_time = new byte[4];
        arraycopy(date, i, valid_time, 0, valid_time.length);
        i += valid_time.length;
        this.valid_time = (String) FileUtils.byte2Parm(valid_time, Type.HEX);

        //预留空间
        byte[] child_cardType = new byte[1];
        arraycopy(date, i, child_cardType, 0, child_cardType.length);
        i += child_cardType.length;
        this.child_cardType = (String) FileUtils.byte2Parm(child_cardType, Type.HEX);

        //预留空间
        byte[] reserved = new byte[3];
        arraycopy(date, i, reserved, 0, reserved.length);
        i += reserved.length;
        this.reserved = (String) FileUtils.byte2Parm(reserved, Type.HEX);

        //首次加款pos
        byte[] add_money_pos_num = new byte[4];
        arraycopy(date, i, add_money_pos_num, 0, add_money_pos_num.length);
        i += add_money_pos_num.length;
        this.add_money_pos_num = (String) FileUtils.byte2Parm(add_money_pos_num, Type.HEX);

        return i;
    }

    public String getRelease_time() {
        return release_time;
    }

    public String getValid_time() {
        return valid_time;
    }

    public String getChild_cardType() {
        return child_cardType;
    }

    public String getReserved() {
        return reserved;
    }

    public String getAdd_money_pos_num() {
        return add_money_pos_num;
    }

    @Override
    public String toString() {
        return "Block_5{" + release_time + valid_time + child_cardType + reserved + add_money_pos_num + '}';
    }
}
