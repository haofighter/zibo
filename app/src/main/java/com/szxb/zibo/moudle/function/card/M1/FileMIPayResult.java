package com.szxb.zibo.moudle.function.card.M1;

import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.Type;
import com.szxb.zibo.moudle.function.card.CardInfoEntity;

import static java.lang.System.arraycopy;


public class FileMIPayResult {

    public Block_18 block_18;
    public Block_19 block_19;
    public Block_8 block_8;
    public Block_9 block_9;
    public Block_A block_A;
    public Block_9 block_9_after;
    public String tac;
    public String uid;

    public void praseResult(int i, byte[] payResult, CardInfoEntity cardInfoEntity) {
        block_18 = new Block_18();
        block_19 = new Block_19();
        block_8 = new Block_8();
        block_9 = new Block_9();
        block_A = new Block_A();
        block_9_after = new Block_9();

        i = block_18.praseBlock(i, payResult);
        i = block_19.praseBlock(i, payResult);
        i = block_8.praseBlock(i, payResult);
        i = block_9.praseBlock(i, payResult);
        i = block_A.praseBlock(i, payResult);
        i = block_9_after.praseBlock(i, payResult);

        byte[] tac = new byte[2];
        arraycopy(payResult, i, tac, 0, tac.length);
        i += tac.length;
        this.tac = (String) FileUtils.byte2Parm(tac, Type.HEX);

        byte[] uid = new byte[2];
        arraycopy(payResult, i, uid, 0, uid.length);
        i += uid.length;
        this.uid = (String) FileUtils.byte2Parm(uid, Type.HEX);

    }


    public Block_18 getBlock_18() {
        return block_18;
    }

    public void setBlock_18(Block_18 block_18) {
        this.block_18 = block_18;
    }

    public Block_19 getBlock_19() {
        return block_19;
    }

    public void setBlock_19(Block_19 block_19) {
        this.block_19 = block_19;
    }

    public Block_8 getBlock_8() {
        return block_8;
    }

    public void setBlock_8(Block_8 block_8) {
        this.block_8 = block_8;
    }

    public Block_9 getBlock_9() {
        return block_9;
    }

    public void setBlock_9(Block_9 block_9) {
        this.block_9 = block_9;
    }

    public Block_A getBlock_A() {
        return block_A;
    }

    public void setBlock_A(Block_A block_A) {
        this.block_A = block_A;
    }

    public Block_9 getBlock_9_after() {
        return block_9_after;
    }

    public void setBlock_9_after(Block_9 block_9_after) {
        this.block_9_after = block_9_after;
    }
}
