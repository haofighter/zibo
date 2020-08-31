package com.szxb.zibo.moudle.function.keyboard;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.szxb.zibo.util.md5.Crc;

import java.io.File;

import static java.lang.System.arraycopy;

public class KeyBorad {
    String netAddress;
    String destinationAddress;
    String originAddress;
    String start;
    String status;
    String divice;
    String command;
    String dataLenth;
    KeyBoardInfo keyBoardInfo;
    String runInfo;
    String crc;

    public void prase(byte[] keyboardInfo) {
        try {
            int i = 0;

            byte[] DestinationAddress = new byte[1];
            arraycopy(keyboardInfo, i, DestinationAddress, 0, DestinationAddress.length);
            i += DestinationAddress.length;
            destinationAddress = FileUtils.bytesToHexString(DestinationAddress);

            byte[] OriginAddress = new byte[1];
            arraycopy(keyboardInfo, i, OriginAddress, 0, OriginAddress.length);
            i += OriginAddress.length;
            originAddress = FileUtils.bytesToHexString(OriginAddress);

            byte[] Start = new byte[2];
            arraycopy(keyboardInfo, i, Start, 0, Start.length);
            i += Start.length;
            start = FileUtils.bytesToHexString(Start);

            byte[] Status = new byte[1];
            arraycopy(keyboardInfo, i, Status, 0, Status.length);
            i += Status.length;
            status = FileUtils.bytesToHexString(Status);

            byte[] Divice = new byte[1];
            arraycopy(keyboardInfo, i, Divice, 0, Divice.length);
            i += Divice.length;
            divice = FileUtils.bytesToHexString(Divice);

            byte[] Command = new byte[1];
            arraycopy(keyboardInfo, i, Command, 0, Command.length);
            i += Command.length;
            command = FileUtils.bytesToHexString(Command);

            byte[] DataLenth = new byte[2];
            arraycopy(keyboardInfo, i, DataLenth, 0, DataLenth.length);
            i += DataLenth.length;
            dataLenth = FileUtils.bytesToHexString(DataLenth);


            byte[] date = new byte[getDataLenthInt()];
            arraycopy(keyboardInfo, i, date, 0, date.length);
            i += date.length;
            keyBoardInfo = new KeyBoardInfo();
            keyBoardInfo.setDate(date);


            byte[] Crc = new byte[1];
            arraycopy(keyboardInfo, i, Crc, 0, Crc.length);
            i += Crc.length;
            crc = FileUtils.bytesToHexString(keyBoardInfo.getAlldate());
        } catch (Exception e) {
            MiLog.i("错误", "键盘数据解析错误：" + e.getMessage());
        }
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = FileUtils.formatHexStringToByteString(1, destinationAddress);
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = FileUtils.formatHexStringToByteString(1, originAddress);
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = FileUtils.formatHexStringToByteString(2, start);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = FileUtils.formatHexStringToByteString(1, status);
    }

    public String getDivice() {
        return divice;
    }

    public void setDivice(String divice) {
        this.divice = FileUtils.formatHexStringToByteString(1, divice);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = FileUtils.formatHexStringToByteString(1, command);
    }

    public int getDataLenthInt() {
        return Integer.parseInt(dataLenth,16);
    }

    public void setDataLenth(int dataLenth) {
        this.dataLenth = FileUtils.formatHexStringToByteString(2, Integer.toHexString(dataLenth));
    }

    public String getDate() {
        return FileUtils.bytesToHexString(keyBoardInfo.getAlldate());
    }

    public void setDate(byte[] date) {
        if (this.keyBoardInfo == null) {
            this.keyBoardInfo = new KeyBoardInfo();
        }
        this.keyBoardInfo.setDate(date);
    }

    public KeyBoardInfo getKeyBoardInfo() {
        return keyBoardInfo;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = FileUtils.formatHexStringToByteString(1, crc);
    }

    public String getNetAddress() {
        return netAddress;
    }

    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress;
    }

    @Override
    public String toString() {
        String data = netAddress
                + destinationAddress
                + originAddress
                + start
                + status
                + divice
                + command
                + dataLenth
                + FileUtils.formatHexStringToByteString(getDataLenthInt(), FileUtils.bytesToHexString(keyBoardInfo.getAlldate()));
        byte[] dateBytes = FileUtils.hexStringToBytes(toCrcString(FileUtils.formatHexStringToByteString(getDataLenthInt(), FileUtils.bytesToHexString(keyBoardInfo.getAlldate()))));
        byte[] crcByte = new byte[]{Crc.getCrc8(dateBytes)};
        crc = FileUtils.bytesToHexString(crcByte);
        return data + crc;
    }

    public String getRunInfoDate() {
        String data = netAddress
                + destinationAddress
                + originAddress
                + start
                + status
                + divice
                + command
                + dataLenth
                + runInfo;
        byte[] dateBytes = FileUtils.hexStringToBytes(toCrcString(runInfo));
        byte[] crcByte = new byte[]{Crc.getCrc8(dateBytes)};
        crc = FileUtils.bytesToHexString(crcByte);
        return data + crc;
    }

    public void setRunInfo(String runInfo) {
        this.runInfo = runInfo;
    }

    public String toCrcString(String date) {
        String data = status
                + divice
                + command
                + dataLenth
                + date;
        return data;
    }
}
