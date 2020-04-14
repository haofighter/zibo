package com.szxb.zibo.moudle.function.card;

import com.hao.lib.Util.FileUtils;
import com.szxb.zibo.util.md5.Crc;

import java.io.File;

import static java.lang.System.arraycopy;

public class KeyBorad {
    String destinationAddress;
    String originAddress;
    String start;
    String status;
    String divice;
    String command;
    String dataLenth;
    String date;
    String crc;


    public void prase(byte[] keyboardInfo) {
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


        byte[] Data = new byte[getDataLenthInt()];
        arraycopy(keyboardInfo, i, Data, 0, Data.length);
        i += Data.length;
        date = FileUtils.bytesToHexString(Data);

        byte[] Crc = new byte[1];
        arraycopy(keyboardInfo, i, Crc, 0, Crc.length);
        i += Crc.length;
        crc = FileUtils.bytesToHexString(Data);
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
        return Integer.parseInt(dataLenth, 16);
    }

    public void setDataLenth(int dataLenth) {
        this.dataLenth = FileUtils.formatHexStringToByteString(2, Integer.toHexString(dataLenth));
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = FileUtils.formatHexStringToByteString(getDataLenthInt(), date);
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = FileUtils.formatHexStringToByteString(1, crc);
    }


    @Override
    public String toString() {
        String data = destinationAddress
                + originAddress
                + start
                + status
                + divice
                + command
                + dataLenth
                + date;
        byte[] dateBytes = FileUtils.hexStringToBytes(toCrcString());
        byte[] crcByte = new byte[]{Crc.getCrc8(dateBytes)};
        crc = FileUtils.bytesToHexString(crcByte);
        return data + crc;
    }

    public String toCrcString() {
        String data = status
                + divice
                + command
                + dataLenth
                + date;
        return data;
    }
}
