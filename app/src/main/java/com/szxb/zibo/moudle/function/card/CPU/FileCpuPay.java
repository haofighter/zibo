package com.szxb.zibo.moudle.function.card.CPU;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.szxb.zibo.moudle.function.unionpay.unionutil.HexUtil;

//CPU卡消费 命令数据
public class FileCpuPay {
    String selete_aid;
    String uid;        //唯一识别符
    String transaction_amount;        //交易金额
    String transaction_time;        //交易日期时间
    String mark_update_1C;//判断是否更新多票 00标识不更新，01更新

    ////用于多票
    File1CLocalInfoEntity file1CLocalInfoEntity;

    File17NewCPUInfoEntity file17NewCPUInfoEntity;

    public FileCpuPay() {
        setSelete_aid("");
        setUid("");
        setTransaction_amount(0);
        setTransaction_time("");
    }

    public void setFile1CLocalInfoEntity(File1CLocalInfoEntity file1CLocalInfoEntity) {
        this.file1CLocalInfoEntity = file1CLocalInfoEntity;
    }

    public void setFile17NewCPUInfoEntity(File17NewCPUInfoEntity file17NewCPUInfoEntity) {
        this.file17NewCPUInfoEntity = file17NewCPUInfoEntity;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = FileUtils.formatHexStringToByteString(7, transaction_time);
    }

    public void setTransaction_amount(int transaction_amount) {
        this.transaction_amount = FileUtils.formatHexStringToByteString(4, FileUtils.bytesToHexString(FileUtils.int2byte2(transaction_amount)));
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

    public String toOldCpuString() {
        Log.i("消费", "多票："+file1CLocalInfoEntity.toString());
        String string = selete_aid + uid + transaction_amount + transaction_time + mark_update_1C + file1CLocalInfoEntity.toString();
        Log.i("刷卡", "消费数据："+string);
        return string;
    }

    public String toNewCpuString() {
        Log.i("消费","多票："+ file17NewCPUInfoEntity.toString());
        String string = selete_aid + uid + transaction_amount + transaction_time + mark_update_1C + file17NewCPUInfoEntity.toString();
        Log.i("刷卡", "消费数据："+string);
        return string;
    }


}
