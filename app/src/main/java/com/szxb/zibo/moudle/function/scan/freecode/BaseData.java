package com.szxb.zibo.moudle.function.scan.freecode;


import com.hao.lib.Util.FileUtils;

/**
 * 作者：wuxinxi on 2019/9/10
 * 包名：com.szxb.yps.cmd.handler.card.data2
 * TODO:一句话描述
 */
public class BaseData {
    public byte[] data;

    public BaseData(byte[] data) {
        this.data = data;
    }

    public String getHexRes(int length, int index) {
        byte[] temp = new byte[length];
        System.arraycopy(data, index, temp, 0, temp.length);
        return FileUtils.bytesToHexString(temp);
    }

    public String getBcdRes(int length, int index) {
        byte[] temp = new byte[length];
        System.arraycopy(data, index, temp, 0, temp.length);
        return FileUtils.bcd2Str(temp);
    }




}
