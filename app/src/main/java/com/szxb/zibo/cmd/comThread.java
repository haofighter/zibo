package com.szxb.zibo.cmd;

import android.os.Handler;
import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.szxb.jni.SerialCom;
import com.szxb.zibo.moudle.function.unionpay.unionutil.HexUtil;

import java.util.ArrayList;
import java.util.List;

import static com.szxb.jni.SerialCom.SerialComRead;
import static java.lang.System.arraycopy;


/**
 * Created by lilei on 18-1-24.
 */

public class comThread extends Thread {
    static final String TAG = "comThread";
    final static int maxLen = 2048;
    final static byte[] recvBuf = new byte[maxLen];
    static comThread comThread;
    static long lastDatetime = 0;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        SerialCom.SerialComClose();
    }

    public static comThread getInstance() {
        if (comThread == null) {
            comThread = new comThread();
        }
        return comThread;
    }


    public comThread(Handler nHandler) {
        SerialCom.SerialComOpen();
    }

    public comThread() {
        Log.i("创建", "comThread  创建了");
        SerialCom.SerialComOpen();
    }

    public int snedData(byte[] writeBuf, int len) {
        return SerialCom.SerialComWrite(writeBuf, len);
    }

    @Override
    public void run() {
        int ret = 0;
        lastDatetime = System.currentTimeMillis();
        while (true) {
            try {
                if (System.currentTimeMillis() - lastDatetime > 5000) {
                    SerialCom.DevRest();//重启k21
                    lastDatetime = System.currentTimeMillis();
                    MiLog.i(TAG, "SerialComRead lenth 重启k21");
                }
                Log.i("线程028b", "运行");
                ret = SerialComRead(recvBuf, 5000);
                if (0 > ret) {
                    Log.i(TAG, "SerialComRead err");
                } else if (0 == ret) {
                    Log.i(TAG, "SerialComRead timeOut");
                } else {
//                    devCmd mycmd = new devCmd();
////                    if (mycmd.checkRecvData(recvBuf, ret) == 0) {
////                        mycmd.doCmd();
////                    }

                    lastDatetime = System.currentTimeMillis();
                    devCmd mycmd = new devCmd();
                    List<byte[]> command = new ArrayList<>();
                    boolean isHaveComand = true;
                    byte[] allcommandDate = new byte[ret];
                    MiLog.i(TAG, "当前收到命令=" + HexUtil.bytesToHexString(recvBuf));
                    arraycopy(recvBuf, 0, allcommandDate, 0, ret);


//                    String comannd = "028e03000000350908aabb000b00002b01000000000000000000000000000000000000000000000000120100002800000000000000000000000000374aa432fd03" +
//                            "028e03000000350908aabb000b00002b0100000000000000000000000000000000000000000000000011010028000000000000000000000000000070222a764c03" +
//                            "028e03000000350908aabb000b00002b01000000000000000000000000000000000000000000000000100128000000000000000000000000000000a636e718a303";
//
//                    String comannd ="028e03000000350908aabb000b00002b0106363330303135000000002901063633303031350000000029010000000000000000000000000000000009a3043f0a03";
//                    allcommandDate = FileUtils.hexStringToBytes(comannd);

                    int dateLenthIndex = 5;
                    while (isHaveComand) {
                        byte[] dateLenth = new byte[2];
                        if (dateLenthIndex > allcommandDate.length) {
                            break;
                        }
                        arraycopy(allcommandDate, dateLenthIndex, dateLenth, 0, dateLenth.length);
                        int comandDateLenth = FileUtils.hexStringToInt(FileUtils.bytesToHexString(dateLenth));
                        if (ret >= comandDateLenth + 12) {
                            byte[] commandDate = new byte[comandDateLenth + 12];
                            arraycopy(allcommandDate, dateLenthIndex - 5, commandDate, 0, commandDate.length);
                            command.add(commandDate);
                            MiLog.i(TAG, "单条命令" + dateLenthIndex + "    " + FileUtils.bytesToHexString(commandDate));
                            dateLenthIndex += commandDate.length;
                        } else {
                            isHaveComand = false;
                        }
                    }
                    for (int i = 0; i < command.size(); i++) {
                        if (mycmd.checkRecvData(command.get(i), command.get(i).length) == 0) {
                            mycmd.doCmd();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("comThread",
                        "run(comThread.java:118)" + e.toString());
            }
        }
    }
}
