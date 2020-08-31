package com.szxb.zibo.cmd;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;

import static com.szxb.jni.SerialCom.CRC32;

public class devCmd {

    private static final String TAG = devCmd.class.getSimpleName();

    final static byte STX = 0x02;
    final static byte ETX = 0x03;

    private byte stx;
    private byte cla;
    private byte ins;
    private byte p;
    private byte s;
    private int nRecvLen;
    private byte[] dataBuf;
    private long recvCrc;
    private long crc;

    public int checkRecvData(byte[] data, int len) {
        int nRet = 0;
        int index = 0;

        do {
            if (data[0] != STX) {
                nRet = -1;
                break;
            }

            if (data[len - 1] != ETX) {
                nRet = -2;
                break;
            }

            recvCrc = data[len - 5] & 0xff;
            recvCrc = recvCrc * 256 + (data[len - 4] & 0xff);
            recvCrc = recvCrc * 256 + (data[len - 3] & 0xff);
            recvCrc = recvCrc * 256 + (data[len - 2] & 0xff);

            Log.d(TAG, "recvCrc = " + recvCrc);

            crc = CRC32(data, 1, len - 6);
            Log.d(TAG, "crc = " + crc);

            if (recvCrc != crc) {
                nRet = -3;
                break;
            }

            stx = data[index++];
            cla = data[index++];
            ins = data[index++];
            p = data[index++];
            s = data[index++];
            nRecvLen = (data[index++] & 0xff);
            nRecvLen *= 256;
            nRecvLen += (data[index++] & 0xff);

            Log.d(TAG, "len = " + nRecvLen);

            if (nRecvLen > 0) {
                dataBuf = new byte[nRecvLen];

                for (int i = 0; i < nRecvLen; i++) {
                    dataBuf[i] = data[index++];
                }
            }
        } while (false);
        return nRet;
    }

    public byte[] packageData() {
        int index = 0;
        byte[] sendBuf = new byte[512];
        sendBuf[index++] = STX;
        sendBuf[index++] = cla;
        sendBuf[index++] = ins;
        sendBuf[index++] = p;
        sendBuf[index++] = (byte) (nRecvLen / 256);
        sendBuf[index++] = (byte) (nRecvLen % 256);

        if (nRecvLen > 0 && dataBuf != null) {
            for (int i = 0; i < nRecvLen; i++) {
                sendBuf[index++] = dataBuf[i];
            }
        }

        crc = CRC32(sendBuf, 1, index - 1);

        sendBuf[index++] = (byte) ((crc >> 24) & 0xff);
        sendBuf[index++] = (byte) ((crc >> 16) & 0xff);
        sendBuf[index++] = (byte) ((crc >> 8) & 0xff);
        sendBuf[index++] = (byte) ((crc) & 0xff);
        sendBuf[index++] = ETX;

        byte[] retBuf = new byte[index];
        System.arraycopy(sendBuf, 0, retBuf, 0, index);
        return retBuf;
    }


    public void doCmd() {
         if ((cla & 0x80) != 0) {
            switch (cla) {
                case (byte) 0x8b:
                    DoCmd.doHeart(devCmd.this);
                    break;
                case (byte) 0x87:
                    DoCmd.doQRcode(devCmd.this);
                    break;
                case (byte) 0x86://寻卡
                    DoCmd.doCard(devCmd.this);
                    break;
                case (byte) 0x05://PSAM卡检测返回结果
                    DoCmd.doPSAM(devCmd.this);
                    break;
                case (byte) 0x83://按键
                    DoCmd.doKeyPress(devCmd.this);
                    break;
                case (byte) 0x8e://外接键盘
                    DoCmd.dokeyboard(devCmd.this);
                    break;
            }
        } else {
            if (dataBuf != null) {
                boolean b = DoCmd.queue.offer(devCmd.this);
            } else {
            }
        }
    }


    public byte getCla() {
        return cla;
    }

    public void setCla(byte cla) {
        this.cla = cla;
    }

    public byte getIns() {
        return ins;
    }

    public void setIns(byte ins) {
        this.ins = ins;
    }

    public byte getP() {
        return p;
    }

    public void setP(byte p) {
        this.p = p;
    }

    public byte getS() {
        return s;
    }

    public void setS(byte s) {
        this.s = s;
    }

    public int getnRecvLen() {
        return nRecvLen;
    }

    public void setnRecvLen(int nRecvLen) {
        this.nRecvLen = nRecvLen;
    }

    public byte[] getDataBuf() {
        return dataBuf;
    }

    public void setDataBuf(byte[] dataBuf) {
        this.dataBuf = dataBuf;
    }

}
