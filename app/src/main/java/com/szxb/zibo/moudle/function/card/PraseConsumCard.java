package com.szxb.zibo.moudle.function.card;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;
import com.szxb.zibo.moudle.function.card.CPU.FileCpuPayResult;
import com.szxb.zibo.moudle.function.card.JTB.FileJTBPay;
import com.szxb.zibo.moudle.function.card.JTB.FileJTBPayResult;
import com.szxb.zibo.moudle.function.card.M1.FileMIPayResult;

import java.io.File;

import static java.lang.System.arraycopy;

/**
 * 作者：L on 2019/7/26 11:12
 * 消费数据解析
 */

public class PraseConsumCard {
    String status;     //用于存储错误码
    String sw;        //每次操作后的sw值，用于配合错误码上报错误
    FileCpuPayResult fileCpuPayResult;
    FileMIPayResult fileMIPayResult;
    FileJTBPayResult fileJTBPayResult;


    int trandePrice;
    int balance;//消费后余额

    public PraseConsumCard(byte[] payResult, CardInfoEntity cardInfoEntity) throws Exception {
        int i = 0;
        //sw[2];        //每次操作后的sw值，用于配合错误码上报错误
        byte[] status = new byte[1];
        arraycopy(payResult, i, status, 0, status.length);
        i += status.length;
        this.status = (String) FileUtils.byte2Parm(status, Type.HEX);

        //sw[2];        //每次操作后的sw值，用于配合错误码上报错误
        byte[] sw = new byte[2];
        arraycopy(payResult, i, sw, 0, sw.length);
        i += sw.length;
        this.sw = (String) FileUtils.byte2Parm(sw, Type.HEX);


        if (cardInfoEntity.selete_aid.equals("02")) {
            fileCpuPayResult = new FileCpuPayResult();
            fileCpuPayResult.praseResult(i, payResult);
            balance = FileUtils.hexStringToInt(fileCpuPayResult.getBalance());
        } else if (cardInfoEntity.selete_aid.equals("03")) {
            fileCpuPayResult = new FileCpuPayResult();
            fileCpuPayResult.praseResult(i, payResult);
            balance = FileUtils.hexStringToInt(fileCpuPayResult.getBalance());
        } else if (cardInfoEntity.selete_aid.equals("04")) {
            fileMIPayResult = new FileMIPayResult();
            fileMIPayResult.praseResult(i, payResult, cardInfoEntity);
            balance = fileMIPayResult.getBlock_9_after().getBlanace();
        } else if (cardInfoEntity.selete_aid.equals("01")) {
            fileJTBPayResult = new FileJTBPayResult();
            fileJTBPayResult.praseDate(i, payResult);
            balance = fileJTBPayResult.getBalance();
        }
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
