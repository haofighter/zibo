package com.szxb.zibo.config.zibo;

import com.szxb.jni.SerialCom;

import java.lang.ref.WeakReference;

public class DataPageProcess {
    final static byte STX = 0x02;
    final static byte ETX = 0x03;
    static byte[] sendBuf = new byte[512];

    public static byte[] packageData(int nRecvLen, byte[] dataBuf, byte cla, byte ins) {
        int index = 0;
        sendBuf[index++] = STX;
        sendBuf[index++] = cla;
        sendBuf[index++] = ins;
        sendBuf[index++] = 0;
        sendBuf[index++] = (byte) (nRecvLen / 256);
        sendBuf[index++] = (byte) (nRecvLen % 256);

        if (nRecvLen > 0 && dataBuf != null) {
            for (int i = 0; i < nRecvLen; i++) {
                sendBuf[index++] = dataBuf[i];
            }
        }

        long crc = SerialCom.CRC32(sendBuf, 1, index - 1);

        sendBuf[index++] = (byte) ((crc >> 24) & 0xff);
        sendBuf[index++] = (byte) ((crc >> 16) & 0xff);
        sendBuf[index++] = (byte) ((crc >> 8) & 0xff);
        sendBuf[index++] = (byte) ((crc) & 0xff);
        sendBuf[index++] = ETX;

        byte[] retBuf = new byte[index];
        System.arraycopy(sendBuf, 0, retBuf, 0, index);
        WeakReference<byte[]> reference = new WeakReference<>(retBuf);
        return reference.get();
    }

}