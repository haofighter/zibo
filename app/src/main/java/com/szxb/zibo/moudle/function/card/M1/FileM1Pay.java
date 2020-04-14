package com.szxb.zibo.moudle.function.card.M1;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;
import com.szxb.zibo.moudle.function.card.CPU.File1CLocalInfoEntity;

import java.io.File;

import static java.lang.System.arraycopy;

//CPU卡消费 命令数据
public class FileM1Pay {
    String selete_aid;
    String uid;        //唯一识别符
    String blacklist_type;                //黑名单标志
    String transaction_type;            //交易类型
    String transaction_amount;        //交易金额
    String mark_update_24;                //交易信息   判断是否更新24块的数据 00标识更新，01不更新（以下的更新标识都是在确认块数据正确，且正副本覆盖后的条件下判断的）
    String mark_update_9;                //钱包    判断是否更新9块的数据 00标识更新，01不更新  扣费时需要更新
    String transaction_time;        //交易日期时间
    String mark_update_1C;        ////判断是否更新多票 00标识不更新，01更新
    //块0x1C~1D//用于多票
    File1CLocalInfoEntity file1CLocalInfoEntity;

    public FileM1Pay() {
        setUid("");
        setSelete_aid("");
        setUid("");        //唯一识别符
        setBlacklist_type("00");                //黑名单标志
        setTransaction_type("09");            //交易类型
        setTransaction_amount(0);        //交易金额
        setMark_update_24("");                //判断是否更新24块的数据 00标识更新，01不更新（以下的更新标识都是在确认块数据正确，且正副本覆盖后的条件下判断的）
        setMark_update_9("");                //判断是否更新9块的数据 00标识更新，01不更新
        setTransaction_time("");        //交易日期时间

    }

    public void praseDate(byte[] date) {
        int i = 0;
        byte[] selete_aid = new byte[1];
        arraycopy(date, i, selete_aid, 0, selete_aid.length);
        i += selete_aid.length;
        this.selete_aid = (String) FileUtils.byte2Parm(selete_aid, Type.HEX);

        byte[] uid = new byte[4];
        arraycopy(date, i, uid, 0, uid.length);
        i += uid.length;
        this.uid = (String) FileUtils.byte2Parm(uid, Type.HEX);

        byte[] blacklist_type = new byte[1];
        arraycopy(date, i, blacklist_type, 0, blacklist_type.length);
        i += blacklist_type.length;
        this.blacklist_type = (String) FileUtils.byte2Parm(blacklist_type, Type.HEX);

        byte[] transaction_type = new byte[1];
        arraycopy(date, i, transaction_type, 0, transaction_type.length);
        i += transaction_type.length;
        this.transaction_type = (String) FileUtils.byte2Parm(transaction_type, Type.HEX);

        byte[] transaction_amount = new byte[4];
        arraycopy(date, i, transaction_amount, 0, transaction_amount.length);
        i += transaction_amount.length;
        this.transaction_amount = (String) FileUtils.byte2Parm(transaction_amount, Type.HEX);

        byte[] mark_update_24 = new byte[1];
        arraycopy(date, i, mark_update_24, 0, mark_update_24.length);
        i += mark_update_24.length;
        this.mark_update_24 = (String) FileUtils.byte2Parm(mark_update_24, Type.HEX);

        byte[] mark_update_9 = new byte[1];
        arraycopy(date, i, mark_update_9, 0, mark_update_9.length);
        i += mark_update_9.length;
        this.mark_update_9 = (String) FileUtils.byte2Parm(mark_update_9, Type.HEX);


        byte[] transaction_time = new byte[7];
        arraycopy(date, i, transaction_time, 0, transaction_time.length);
        i += transaction_time.length;
        this.transaction_time = (String) FileUtils.byte2Parm(transaction_time, Type.HEX);

        byte[] mark_update_1C = new byte[1];
        arraycopy(date, i, mark_update_1C, 0, mark_update_1C.length);
        i += mark_update_1C.length;
        this.mark_update_1C = (String) FileUtils.byte2Parm(mark_update_1C, Type.HEX);

        file1CLocalInfoEntity = new File1CLocalInfoEntity();
        file1CLocalInfoEntity.praseFile1CLocal(i, date, "04");
    }

    public void setFile1CLocalInfoEntity(File1CLocalInfoEntity file1CLocalInfoEntity) {
        this.file1CLocalInfoEntity = file1CLocalInfoEntity;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = FileUtils.formatHexStringToByteString(7, transaction_time);
    }

    public void setMark_update_9(String mark_update_9) {
        this.mark_update_9 = FileUtils.formatHexStringToByteString(1, mark_update_9);
    }

    public void setMark_update_24(String mark_update_24) {
        this.mark_update_24 = FileUtils.formatHexStringToByteString(1, mark_update_24);
    }

    public void setTransaction_amount(int transaction_amount) {
        this.transaction_amount = FileUtils.formatHexStringToByteString(4, FileUtils.bytesToHexString(FileUtils.int2byte2(transaction_amount)));
    }

    public int getTransaction_amount() {
        return Integer.parseInt(transaction_amount, 16);
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = FileUtils.formatHexStringToByteString(1, transaction_type);
    }

    public void setBlacklist_type(String blacklist_type) {
        this.blacklist_type = FileUtils.formatHexStringToByteString(1, blacklist_type);
    }

    public void setUid(String uid) {
        this.uid = FileUtils.formatHexStringToByteString(4, uid);
    }

    public void setSelete_aid(String selete_aid) {
        this.selete_aid = FileUtils.formatHexStringToByteString(1, selete_aid);
    }

    public void setMark_update_1C(String mark_update_1C) {
        this.mark_update_1C = mark_update_1C;
    }

    @Override
    public String toString() {
        Log.i("刷卡", "校验  mark_update_24=" + (mark_update_24.equals("01") ? "不更新" : "更新") + "  mark_update_1C=" + (mark_update_1C.equals("01") ? "更新" : "不更新") + "      mark_update_9=" + (mark_update_9.equals("01") ? "不更新" : "更新"));
        String string = blacklist_type + transaction_type + transaction_amount + mark_update_24 + mark_update_9 + transaction_time + mark_update_1C + file1CLocalInfoEntity.toString();
        Log.i("刷卡","消费数据:"+ string);
        return string;
    }
}
