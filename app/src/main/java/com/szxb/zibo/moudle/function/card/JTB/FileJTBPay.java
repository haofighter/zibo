package com.szxb.zibo.moudle.function.card.JTB;

import android.util.Log;

import com.szxb.lib.Util.MiLog;


//CPU卡消费 命令数据
public class FileJTBPay {
    String updata_1A;            //1A文件更新标识

    File1EJTBInfoEntity file1EJTBInfoEntity;
    File1AJTBInfoEntity file1AJTBInfoEntity;

    public String getPayDate() {
        MiLog.i("刷卡", "消费数据长度：1E：" + file1EJTBInfoEntity.getPayDate().length() + "      1A： " + file1AJTBInfoEntity.getPaydate().length());
        return updata_1A + file1EJTBInfoEntity.getPayDate() + file1AJTBInfoEntity.getPaydate();
    }

    public void setFile1EJTBInfoEntity(File1EJTBInfoEntity file1EJTBInfoEntity) {
        this.file1EJTBInfoEntity = file1EJTBInfoEntity;
    }

    public void setFile1AJTBInfoEntity(File1AJTBInfoEntity file1AJTBInfoEntity) {
        this.file1AJTBInfoEntity = file1AJTBInfoEntity;
    }

    public File1AJTBInfoEntity getFile1AJTBInfoEntity() {
        return file1AJTBInfoEntity;
    }

    public void setUpdata_1A(String updata_1A) {
        this.updata_1A = updata_1A;
    }

    public String getUpdata_1A() {
        return updata_1A;
    }
}
