package com.szxb.zibo.moudle.function.card.M1;

import android.util.Log;

import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.moudle.function.card.CPU.File1CLocalInfoEntity;
import com.szxb.zibo.moudle.function.card.M1.Block_18;
import com.szxb.zibo.moudle.function.card.M1.Block_4;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.arraycopy;

public class FileM1InfoEntity {

    Block_4 block_4;
    Block_5 block_5;
    Block_6 block_6;
    Block_8 block_8;
    Block_9 block_9;
    Block_A block_A;
    Block_18 block_18;
    Block_19 block_19;

    //块0x2C~2E
    Block_11 block_11;//管理卡控制信息
    //    Block_1C1D block_1C1D;
    File1CLocalInfoEntity block_1C1D; //此处结构与cpu卡的结构相同 故使用同一个方便后续针对补票等相关判断的处理


    public int praseFileM1(int i, byte[] date, String select_aid) {
        block_4 = new Block_4();
        block_5 = new Block_5();
        block_6 = new Block_6();
        block_8 = new Block_8();
        block_9 = new Block_9();
        block_A = new Block_A();
        block_11 = new Block_11();
        block_18 = new Block_18();
        block_19 = new Block_19();
        block_1C1D = new File1CLocalInfoEntity();

        i = block_4.praseBlock(i, date);
        MiLog.i("刷卡", "卡信息：block_4=" + block_4.toString());
        i = block_5.praseBlock(i, date);
        MiLog.i("刷卡", "卡信息：block_5=" + block_5.toString());
        i = block_6.praseBlock(i, date);
        MiLog.i("刷卡", "卡信息：block_6=" + block_6.toString());
        i = block_8.praseBlock(i, date);
        MiLog.i("刷卡", "卡信息：block_8=" + block_8.toString());
        i = block_9.praseBlock(i, date);
        MiLog.i("刷卡", "卡信息：block_9=" + block_9.toString());
        i = block_A.praseBlock(i, date);
        MiLog.i("刷卡", "卡信息：block_A=" + block_A.toString());
        i = block_18.praseBlock(i, date);
        MiLog.i("刷卡", "卡信息：block_18=" + block_18.toString());
        i = block_19.praseBlock(i, date);
        MiLog.i("刷卡", "卡信息：block_19=" + block_19.toString());

        //管理卡控制信息
        byte[] block_11_date = new byte[48];
        arraycopy(date, i, block_11_date, 0, block_11_date.length);
        block_11.praseBlock(i, date, block_4.getCard_type());
        i += block_11_date.length;
        MiLog.i("刷卡", "卡信息：block_11=" + block_11.toString());

        i = block_1C1D.praseFile1CLocal(i, date, select_aid);
        MiLog.i("刷卡", "卡信息：block_1C1D=" + block_1C1D.toString());


        return i;
    }

    public Block_4 getBlock_4() {
        return block_4;
    }

    public void setBlock_4(Block_4 block_4) {
        this.block_4 = block_4;
    }

    public Block_6 getBlock_6() {
        return block_6;
    }

    public void setBlock_6(Block_6 block_6) {
        this.block_6 = block_6;
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

    public File1CLocalInfoEntity getBlock_1C1D() {
        return block_1C1D;
    }

    public void setBlock_1C1D(File1CLocalInfoEntity block_1C1D) {
        this.block_1C1D = block_1C1D;
    }

    public Block_5 getBlock_5() {
        return block_5;
    }

    public void setBlock_5(Block_5 block_5) {
        this.block_5 = block_5;
    }

    public Block_11 getBlock_11() {
        return block_11;
    }

    public void setBlock_11(Block_11 block_11) {
        this.block_11 = block_11;
    }

}
