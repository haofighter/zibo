package com.szxb.zibo.cmd;


import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.ThreadUtils;
import com.hao.lib.Util.Type;
import com.hao.lib.base.Rx.Rx;
import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.SignIn;
import com.szxb.java8583.module.manager.BusllPosManage;

import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.haikou.ConfigContext;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.moudle.function.keyboard.KeyBoardInfo;
import com.szxb.zibo.moudle.function.keyboard.KeyBorad;
import com.szxb.zibo.moudle.function.card.PraseCard;
import com.szxb.zibo.moudle.function.scan.ScanManage;
import com.szxb.zibo.moudle.function.unionpay.UnionPay;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.util.apkmanage.AppUtil;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.MiLinkBlockQueue;
import com.szxb.zibo.util.SecretUtils;
import com.szxb.zibo.util.sp.CommonSharedPreferences;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.szxb.jni.SerialCom.SerialComWrite;
import static com.szxb.zibo.config.zibo.InitConfigZB.INFO_UP;
import static java.lang.System.arraycopy;

public class DoCmd {
    private static String priceSetting = "";
    private static String oldStationKey = "000000";
    private static String oldSynKey = "000000";

    public static BlockingQueue<devCmd> queue = new MiLinkBlockQueue(1);

    public static void doHeart(devCmd myCmd) {
    }

    public static void doQRcode(devCmd myCmd) {
        try {
            byte[] qrcode = new byte[myCmd.getnRecvLen()];
            arraycopy(myCmd.getDataBuf(), 0, qrcode, 0, myCmd.getnRecvLen());

            if (BusApp.getInstance().checkSetting()) {
                return;
            }

            //进行二维码流程
            ScanManage.getInstance().scanRe(qrcode);
        } catch (Exception e) {
            MiLog.i("错误", "二维码报错");
        }
    }

    public static String doGetVersion() {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x81);
        verCmd.setIns((byte) 0);
        verCmd.setDataBuf(null);
        verCmd.setnRecvLen(0);

        byte[] sendCmd = verCmd.packageData();

        SerialComWrite(sendCmd, sendCmd.length);

        try {
            devCmd bean = queue.poll(900, TimeUnit.SECONDS);
            if (bean != null && bean.getS() == 0) {
                return new String(bean.getDataBuf());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void doCard(devCmd myCmd) {
        try {
            byte[] cardDate = new byte[myCmd.getnRecvLen()];
            arraycopy(myCmd.getDataBuf(), 0, cardDate, 0, myCmd.getnRecvLen());
            PraseCard.praseCardDate(cardDate);
        } catch (Exception e) {
            MiLog.i("错误", "doCard:" + e.getMessage());
        }
    }

    //设置时间
    public static void setTime(String s) {
        byte[] time = FileUtils.hexStringToBytes(s);
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x82);
        verCmd.setIns((byte) 0x01);
        verCmd.setDataBuf(time);
        verCmd.setnRecvLen(time.length);
        byte[] sendCmd = verCmd.packageData();
        SerialComWrite(sendCmd, sendCmd.length);
    }

    //获取时间
    public static devCmd getTime() {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x82);
        verCmd.setIns((byte) 0x02);
        verCmd.setDataBuf(null);
        verCmd.setnRecvLen(0);
        byte[] sendCmd = verCmd.packageData();
        SerialComWrite(sendCmd, sendCmd.length);
        try {
            devCmd bean = queue.poll(900, TimeUnit.MILLISECONDS);
            if (bean != null && bean.getS() == 0) {
                return bean;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //消费获取交易记录
    public static devCmd getPayRecord(byte[] date) {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x86);
        verCmd.setIns((byte) 0x41);
        verCmd.setDataBuf(date);
        verCmd.setnRecvLen(date.length);
        byte[] sendCmd = verCmd.packageData();
        try {
            MiLog.i("刷卡", "刷卡消费发送" + FileUtils.bytesToHexString(sendCmd));
            queue.clear();
            SerialComWrite(sendCmd, sendCmd.length);
            devCmd bean = queue.poll(900, TimeUnit.MILLISECONDS);
            if (bean != null && bean.getS() == 0) {
                return bean;
            }
        } catch (InterruptedException e) {
            MiLog.i("刷卡错误", "getPayRecord 报错啦" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    //mac2卡校验
    public static devCmd checkMac(byte[] date) {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x86);
        verCmd.setIns((byte) 0x43);
        verCmd.setDataBuf(date);
        verCmd.setnRecvLen(date.length);
        byte[] sendCmd = verCmd.packageData();
        SerialComWrite(sendCmd, sendCmd.length);
        try {
            devCmd bean = queue.poll(900, TimeUnit.MILLISECONDS);
            if (bean != null && bean.getS() == 0) {
                return bean;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    //银联
    public static devCmd checkUnion(byte[] date) {

        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x86);
        verCmd.setIns((byte) 0x3f);
        verCmd.setDataBuf(date);
        verCmd.setnRecvLen(date.length);
        byte[] sendCmd = verCmd.packageData();
        SerialComWrite(sendCmd, sendCmd.length);
        try {
            devCmd bean = queue.poll(900, TimeUnit.MILLISECONDS);
            if (bean != null && bean.getS() == 0) {
                return bean;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    //复位PSAM卡命令
    public static devCmd resetPSAM() {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x85);
        verCmd.setIns((byte) 0x3e);
        verCmd.setDataBuf(null);
        verCmd.setnRecvLen(0);
        byte[] sendCmd = verCmd.packageData();
        SerialComWrite(sendCmd, sendCmd.length);
        try {
            devCmd bean = queue.poll(1, TimeUnit.SECONDS);
            if (bean != null && bean.getS() == 0 && bean.getDataBuf() != null) {
                doPSAM(bean);
                return bean;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //PSAM卡复位返回  如果无数据则表示未识别到PSAM卡
    public static void doPSAM(devCmd devCmd) {
        try {
            if (devCmd != null) {
                byte[] PASMresult = new byte[devCmd.getnRecvLen()];
                arraycopy(devCmd.getDataBuf(), 0, PASMresult, 0, PASMresult.length);

                int i = 0;
                //选择卡槽 固定00
                byte[] M1slot = new byte[1];
                arraycopy(PASMresult, i, M1slot, 0, M1slot.length);
                i += M1slot.length;

                //终端编号
                byte[] M1PosID = new byte[6];
                arraycopy(PASMresult, i, M1PosID, 0, M1PosID.length);
                i += M1PosID.length;
                String m1qposID = (String) FileUtils.byte2Parm(M1PosID, Type.HEX);
                BusApp.getPosManager().setCpupsam(m1qposID);
                BusApp.getPosManager().setMainPSAM(m1qposID);

                //PSAM卡号
                byte[] M1SerialNum = new byte[10];
                arraycopy(PASMresult, i, M1SerialNum, 0, M1SerialNum.length);
                i += M1SerialNum.length;
                String m1serialNum = (String) FileUtils.byte2Parm(M1SerialNum, Type.HEX);


                ////密钥索引 有卡01
                byte[] M1Key_index = new byte[1];
                arraycopy(PASMresult, i, M1Key_index, 0, M1Key_index.length);
                i += M1Key_index.length;
                String m1Key_index = (String) FileUtils.byte2Parm(M1Key_index, Type.HEX);

                if (PASMresult.length <= i) {
                    return;
                }

                //选择卡槽 固定00
                byte[] Cpuslot = new byte[1];
                arraycopy(PASMresult, i, Cpuslot, 0, Cpuslot.length);
                i += Cpuslot.length;

                //终端编号
                byte[] CpuPosID = new byte[6];
                arraycopy(PASMresult, i, CpuPosID, 0, CpuPosID.length);
                i += CpuPosID.length;
                String cpuPosID = (String) FileUtils.byte2Parm(CpuPosID, Type.HEX);
                BusApp.getPosManager().setM1psam(cpuPosID);
                if(BusApp.getPosManager().getMainPSAM().equals("000000000000")){
                    BusApp.getPosManager().setMainPSAM(cpuPosID);
                }


                //PSAM卡号
                byte[] CpuSerialNum = new byte[10];
                arraycopy(PASMresult, i, CpuSerialNum, 0, CpuSerialNum.length);
                i += CpuSerialNum.length;
                String cpuSerialNum = (String) FileUtils.byte2Parm(CpuSerialNum, Type.HEX);


                ////密钥索引 有卡01
                byte[] CpuKey_index = new byte[1];
                arraycopy(PASMresult, i, CpuKey_index, 0, CpuKey_index.length);
                i += CpuKey_index.length;


                if (PASMresult.length <= i) {
                    return;
                }
                //选择卡槽 固定00
                byte[] jtslot = new byte[1];
                arraycopy(PASMresult, i, Cpuslot, 0, Cpuslot.length);
                i += Cpuslot.length;

                //终端编号
                byte[] JTBPosID = new byte[6];
                arraycopy(PASMresult, i, JTBPosID, 0, JTBPosID.length);
                i += JTBPosID.length;
                String jTBPosID = (String) FileUtils.byte2Parm(JTBPosID, Type.HEX);
                BusApp.getPosManager().setJTBpsam(jTBPosID);
                if(BusApp.getPosManager().getMainPSAM().equals("000000000000")){
                    BusApp.getPosManager().setMainPSAM(jTBPosID);
                }

                //PSAM卡号
                byte[] JTBSerialNum = new byte[10];
                arraycopy(PASMresult, i, JTBSerialNum, 0, JTBSerialNum.length);
                i += JTBSerialNum.length;
                String jTBSerialNum = (String) FileUtils.byte2Parm(JTBSerialNum, Type.HEX);


                ////密钥索引 有卡01
                byte[] JTBKey_index = new byte[1];
                arraycopy(PASMresult, i, JTBKey_index, 0, JTBKey_index.length);
                i += JTBKey_index.length;
                String jtbkey_index = (String) FileUtils.byte2Parm(JTBKey_index, Type.HEX);


            }

        } catch (Exception e) {
            MiLog.i("错误", "PASM卡复位失败:" + e.getMessage());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        resetPSAM();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();

        }
    }

    static long keyTime = 0;

    public static void doKeyPress(devCmd devCmd) {
        try {

            if (Math.abs(keyTime - System.currentTimeMillis()) < 200) {
                return;
            }
            String keycode = FileUtils.bytesToHexString(devCmd.getDataBuf());
            if (keycode.equals("01000000")) {
                keycode = ConfigContext.KEY_BUTTON_TOP_LEFT;
            } else if (keycode.equals("00010000")) {
                keycode = ConfigContext.KEY_BUTTON_TOP_RIGHT;
            } else if (keycode.equals("00000100")) {
                keycode = ConfigContext.KEY_BUTTON_BOTTOM_RIGHT;
            } else if (keycode.equals("00000001")) {
                keycode = ConfigContext.KEY_BUTTON_BOTTOM_LEFT;
            }
            Rx.getInstance().sendMessage("key", keycode);
            keyTime = System.currentTimeMillis();
        } catch (Exception e) {
            MiLog.i("错误", "doKeyPress(DoCmd.java:237)" + e.getMessage());
        }

    }


    //获取银联参数数据
    public static void doGetUnionParam(devCmd devCmd) {
        try {
            byte[] qrcode = new byte[devCmd.getnRecvLen()];
            arraycopy(devCmd.getDataBuf(), 0, qrcode, 0, devCmd.getnRecvLen());
            String date = FileUtils.bytesToHexString(qrcode);

            if (devCmd.getnRecvLen() == 5 && date.toLowerCase().startsWith("0b0133") && date.toLowerCase().endsWith("0c")) {
                byte[] sendDate = new byte[21];
                sendDate[0] = 0x0b;
                sendDate[1] = 0x11;
                sendDate[2] = 0x00;
                String secret = AppUtil.hexRandomStr(32);
                CommonSharedPreferences.put("unionSecret", secret);
                try {
                    byte[] bytes = FileUtils.hexStringToBytes(secret);
                    arraycopy(bytes, 0, sendDate, 3, bytes.length);
                } catch (Exception e) {
                    Log.i("错误", e.getMessage());
                }
                sendDate[19] = qrcode[3];
                sendDate[20] = 0x0c;
                Rx.getInstance().sendMessage("unionParam", sendDate);
            } else {
                setUnionparam(devCmd);
            }
        } catch (Exception e) {
            MiLog.i("错误", "获取银联参数数据");
        }
    }

    //用于发送数据获取银联参数
    public static devCmd sendUnionParam(byte[] bytes) {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x8e);
        verCmd.setIns((byte) 0x02);
        verCmd.setDataBuf(bytes);
        verCmd.setnRecvLen(bytes.length);
        byte[] sendCmd = verCmd.packageData();
        SerialComWrite(sendCmd, sendCmd.length);
        try {
            devCmd bean = queue.poll(900, TimeUnit.SECONDS);
            if (bean != null && bean.getS() == 0 && bean.getDataBuf() != null) {
                return bean;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //用于接收数据
    public static void setUnionparam(devCmd devCmd) {
        try {
            byte[] unionParam = new byte[devCmd.getnRecvLen()];
            arraycopy(devCmd.getDataBuf(), 0, unionParam, 0, devCmd.getnRecvLen());

            int i = 0;
            byte[] Start = new byte[3];
            arraycopy(unionParam, i, Start, 0, Start.length);
            i += Start.length;
            String start = (String) FileUtils.byte2Parm(Start, Type.HEX);


            byte[] UnionMacid = new byte[15];
            arraycopy(unionParam, i, UnionMacid, 0, UnionMacid.length);
            i += UnionMacid.length;
            String unionMacid = (String) FileUtils.byte2Parm(UnionMacid, Type.ASCLL);


            byte[] UnionPosid = new byte[8];
            arraycopy(unionParam, i, UnionPosid, 0, UnionPosid.length);
            i += UnionPosid.length;
            String unionPosid = (String) FileUtils.byte2Parm(UnionPosid, Type.ASCLL);

            byte[] UnionMacNum = new byte[1];
            arraycopy(unionParam, i, UnionMacNum, 0, UnionMacNum.length);
            i += UnionMacNum.length;
            String unionMacNum = (String) FileUtils.byte2Parm(UnionMacNum, Type.HEX);

            byte[] UnionMac = new byte[16];
            arraycopy(unionParam, i, UnionMac, 0, UnionMac.length);
            i += UnionMac.length;
            String unionMac = (String) FileUtils.byte2Parm(UnionMac, Type.HEX);


            String secret = (String) CommonSharedPreferences.get("unionSecret", "");
            String macIdSe = FileUtils.bytesToHexString(SecretUtils.DES_decrypt_3(UnionMac, FileUtils.hexStringToBytes(secret)));

            BusllPosManage.getPosManager().setMachId(unionMacid);
            BusllPosManage.getPosManager().setKey(macIdSe);
            BusllPosManage.getPosManager().setPosSn(unionPosid);

            BusllPosManage.getPosManager().setTradeSeq();
            Iso8583Message message = SignIn.getInstance().message(BusllPosManage.getPosManager().getTradeSeq());
            Log.d("LoopCard(run.java:242)", message.toFormatString());
            UnionPay.getInstance().exeSSL(UnionConfig.SIGN, message.getBytes(), true);


            byte[] overDate = new byte[25];
            overDate[0] = 0x0b;
            overDate[1] = 0x15;
            overDate[2] = 0x00;

            byte[] date = "          ".getBytes();
            arraycopy(date, 0, overDate, 3, date.length);

            byte[] posSn = new byte[10];
            String posSN = BusApp.getPosManager().getPosSN();
            byte[] pos = posSN.substring(posSn.length - 10, posSN.length()).getBytes();
            arraycopy(pos, 0, posSn, 0, pos.length);
            arraycopy(posSn, 0, overDate, 13, posSn.length);

            overDate[24] = 0x0c;

            DoCmd.sendUnionParam(overDate);
        } catch (Exception e) {
            Log.i("接收银联参数", "");
        }
    }

    public static void resetCard() {
        ThreadUtils.getInstance().createSch("docmd").submit(new Runnable() {
            @Override
            public void run() {
                devCmd verCmd = new devCmd();
                verCmd.setCla((byte) 0x86);
                verCmd.setIns((byte) 0x45);
                verCmd.setDataBuf(null);
                verCmd.setnRecvLen(0);

                byte[] sendCmd = verCmd.packageData();

                SerialComWrite(sendCmd, sendCmd.length);
                try {
                    devCmd bean = queue.poll(1, TimeUnit.SECONDS);
                    if (bean != null && bean.getS() == 0) {
                        Log.d(comThread.TAG, "resetCard");
                    }
                    if (bean == null) {
                        Log.d(comThread.TAG, "resetCard null");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    MiLog.i("错误", "resetCard err");
                }
            }
        });
    }

    public static void dokeyboard(devCmd devCmd) {
        try {
            byte[] keyboardInfo = new byte[devCmd.getnRecvLen()];
            arraycopy(devCmd.getDataBuf(), 0, keyboardInfo, 0, devCmd.getnRecvLen());
            MiLog.i("键盘", "数据上报：" + FileUtils.bytesToHexString(keyboardInfo) + "     " + Thread.currentThread().getName());
            dokeyboard(keyboardInfo);
        } catch (Exception e) {
            MiLog.i("错误", "键盘 上报数据错误：" + e.getMessage());
        }
    }

    public static void dokeyboard(byte[] keyboardInfo) {
        try {
            if (FileUtils.bytesToHexString(keyboardInfo).toUpperCase().startsWith("55AA050004")) {
                Rx.getInstance().sendMessage("myselfAddress", "地址设置成功");
                BusApp.getPosManager().setMyselfkeybroadAddress(BusApp.getPosManager().getMyselfkeybroadAddressCache());
            } else {
                KeyBorad keyBorad = new KeyBorad();
                keyBorad.prase(keyboardInfo);

                String command = "";
                command += checkKeyPrass(keyBorad.getKeyBoardInfo().getStationKey(), oldStationKey);
                command += checkKeyPrass(keyBorad.getKeyBoardInfo().getSynKey(), oldSynKey);
                command += keyBorad.getKeyBoardInfo().getPriceKey();

                MiLog.i("键盘", "操作命令：" + command);
                for (int i = 0; i < command.length() / 2; i++) {
                    dokeyPrice(command.substring(i * 2, (i + 1) * 2), keyBorad.getKeyBoardInfo());
                }
                oldStationKey = keyBorad.getKeyBoardInfo().getStationKey();
                oldSynKey = keyBorad.getKeyBoardInfo().getSynKey();

                Rx.getInstance().sendMessage("sendStationInfo");
            }
        } catch (Exception e) {
            MiLog.i("错误", "键盘：" + e.getMessage());
        }
    }


    //用于比较两次命令间是否有丢包的情况
    static String checkKeyPrass(String now, String old) {
        if (now.length() == 6 && old.length() == 6) {
            String nowFist = now.substring(0, 2);
            String nowSecond = now.substring(2, 4);
            String nowThird = now.substring(4, 6);

            String oldFist = old.substring(0, 2);
            String oldSecond = old.substring(2, 4);
            String oldThird = old.substring(4, 6);

            if (oldThird.equals(nowSecond) && nowFist.equals(oldSecond)) {//表示当前未丢命令
                return nowThird;
            } else if (nowFist.equals(oldThird)) {//表示当前您丢失了一条命令
                return nowSecond + nowThird;
            } else {
                return now;
            }
        }
        return "";
    }


    public static void dokeyPrice(String key, KeyBoardInfo keyBoardInfo) {
        switch (key) {
            case "31"://1
                priceSetting += "1";
                break;
            case "32"://2
                priceSetting += "2";
                break;
            case "33"://3
                priceSetting += "3";
                break;
            case "34"://4
                priceSetting += "4";
                break;
            case "35"://5
                priceSetting += "5";
                break;
            case "36"://6
                priceSetting += "6";
                break;
            case "37"://7
                priceSetting += "7";
                break;
            case "38"://8
                priceSetting += "8";
                break;
            case "39"://9
                priceSetting += "9";
                break;
            case "30":// .
                priceSetting += ".";
                break;
            case "6e":// 0
                priceSetting += "0";
                break;
            case "6a":// #
                break;
            case "26"://加站
                if (BusApp.getPosManager().getLineType().toUpperCase().equals("P") || BusApp.getPosManager().getLineType().toUpperCase().equals("X")) {
                    InitConfigZB.addstation();
                }
                break;
            case "28"://减站
                if (BusApp.getPosManager().getLineType().toUpperCase().equals("P") || BusApp.getPosManager().getLineType().toUpperCase().equals("X")) {
                    InitConfigZB.refuseStation();
                }
                break;
            case "6c"://确认
                if (BusApp.getPosManager().getLineType().equals("X")) {
                    BusApp.getPosManager().setBasePrice((int) (Float.parseFloat(priceSetting) * 100));
                    priceSetting = "";
                    BusToast.showToast("设置票价：" + FileUtils.fen2Yuan(BusApp.getPosManager().getBasePrice()), true);
                }
                break;
            case "70"://F1 前机同步后机
                if (BusApp.getPosManager().getPosUpDate() == 2) {
                    synRuninfo(keyBoardInfo);
                }
                break;
            case "71"://F2
                break;
            case "72"://F3 后机同步前机
                if (BusApp.getPosManager().getPosUpDate() == 1) {
                    synRuninfo2(keyBoardInfo);
                }
                break;
            case "73"://清除
                priceSetting = "";
                break;
        }
    }


    //同步运行信息
    static void synRuninfo(final KeyBoardInfo keyBoardInfo) {
        BusApp.getPosManager().setDirection(keyBoardInfo.getDiraction());

        if (BusApp.getPosManager().getStationID() != keyBoardInfo.getStationId()) {
            BusApp.getPosManager().setStationID(keyBoardInfo.getStationId());
        }

        if (!BusApp.getPosManager().getLineNo().equals(keyBoardInfo.getLineNo())) {
            BusApp.getPosManager().setLineNo(keyBoardInfo.getLineNo());
            BusApp.getPosManager().setBasePrice(0);
            BusApp.getPosManager().setFarver("00000000000000");
            BusApp.getPosManager().setLinver("00000000000000");
            Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        InitConfigZB.sendInfoToServer(INFO_UP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, TimeUnit.SECONDS);
        } else {
            BusApp.getPosManager().setBasePrice(PraseLine.getMorePayPrice(null, true, true));
        }
    }

    //同步运行信息  后机同步前机
    static void synRuninfo2(final KeyBoardInfo keyBoardInfo) {
        BusApp.getPosManager().setDirection(keyBoardInfo.getBackDiraction());

        if (BusApp.getPosManager().getStationID() != keyBoardInfo.getStationId()) {
            BusApp.getPosManager().setStationID(keyBoardInfo.getBackstationId());
        }

        if (!BusApp.getPosManager().getLineNo().equals(keyBoardInfo.getBackLineNo())) {
            BusApp.getPosManager().setLineNo(keyBoardInfo.getLineNo());
            BusApp.getPosManager().setBasePrice(0);
            BusApp.getPosManager().setFarver("00000000000000");
            BusApp.getPosManager().setLinver("00000000000000");
            Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        InitConfigZB.sendInfoToServer(INFO_UP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, TimeUnit.SECONDS);
        } else {
            BusApp.getPosManager().setBasePrice(PraseLine.getMorePayPrice(null, true, true));
        }
    }

    /**
     * drivice 设备类型
     * 02 切换站
     * 05 设置票价
     * 0B 心跳
     */
    public static void sendStationInfo() {
        try {
            devCmd verCmd = new devCmd();
            verCmd.setCla((byte) 0x8e);
            verCmd.setIns((byte) 0x03);
            KeyBorad keyBorad = new KeyBorad();
            keyBorad.setNetAddress(BusApp.getPosManager().getKeybroadAddress());
            keyBorad.setDestinationAddress("08");
            if (BusApp.getPosManager().getPosUpDate() == 1) {
                keyBorad.setOriginAddress("0a");
            } else {
                keyBorad.setOriginAddress("0b");
            }
            keyBorad.setStart("aabb");

            keyBorad.setStatus("00");
            keyBorad.setDivice("0b");
            keyBorad.setCommand("00");

            byte[] lineNo = BusApp.getPosManager().getLineNo().getBytes();
            byte[] lineNoNeed = new byte[9];
            arraycopy(lineNo, 0, lineNoNeed, 0, lineNo.length);
            byte[] lineNolenth = new byte[]{(byte) lineNo.length};

            byte[] stationNo = new byte[]{(byte) BusApp.getPosManager().getStationID()};
            byte[] stationNoNeed = new byte[2];
            arraycopy(stationNo, 0, stationNoNeed, stationNoNeed.length - stationNo.length, stationNo.length);

            byte[] diraction = new byte[]{(byte) (Integer.parseInt(BusApp.getPosManager().getDirection()) == 1 ? 0 : 1)};
            byte[] stationName = new byte[5];
            try {
                stationName = BusApp.getPosManager().getStationName().getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] stationNameNeed = new byte[30];
            arraycopy(stationName, 0, stationNameNeed, 0, stationName.length);
            byte[] stationNameLen = new byte[]{(byte) stationName.length};

            byte[] price = new byte[4];
            byte[] nowprice = FileUtils.int2byte2(BusApp.getPosManager().getBasePrice());
            arraycopy(nowprice, 0, price, price.length - nowprice.length, nowprice.length);

            byte[] bytes = FileUtils.mergeByte(lineNolenth, lineNoNeed, stationNoNeed, diraction, stationNameLen, stationNameNeed, price);
            keyBorad.setDataLenth(bytes.length);
            keyBorad.setRunInfo(FileUtils.bytesToHexString(bytes));

            byte[] alldate = FileUtils.hexStringToBytes(keyBorad.getRunInfoDate());

            verCmd.setDataBuf(alldate);
            verCmd.setnRecvLen(alldate.length);
            byte[] sendCmd = verCmd.packageData();

            MiLog.i("键盘", "本机上送数据：" + FileUtils.bytesToHexString(sendCmd));
            SerialComWrite(sendCmd, sendCmd.length);

        } catch (Exception e) {
            MiLog.i("错误", "发送数据至外接键盘错误：" + e.getMessage());
        }
    }

    public static void lockNewCpu() {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x86);
        verCmd.setIns((byte) 0x47);
        verCmd.setDataBuf(null);
        verCmd.setnRecvLen(0);
        byte[] sendCmd = verCmd.packageData();
        SerialComWrite(sendCmd, sendCmd.length);
        try {
            devCmd bean = queue.poll(900, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void setAddress(String address) {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x8e);
        verCmd.setIns((byte) 0x0f);//根据串口来灵活变动

        byte[] date = new byte[5];
        date[0] = 0x02;
        byte[] addressE = FileUtils.hexStringToBytes(address);
        date[1] = addressE[0];
        date[2] = addressE[1];
        date[3] = 0x05;
        date[4] = 0x05;

        verCmd.setDataBuf(date);
        verCmd.setnRecvLen(date.length);
        byte[] sendCmd = verCmd.packageData();

        MiLog.i("地址", "设置地址：" + FileUtils.bytesToHexString(sendCmd));
        SerialComWrite(sendCmd, sendCmd.length);
    }


}
