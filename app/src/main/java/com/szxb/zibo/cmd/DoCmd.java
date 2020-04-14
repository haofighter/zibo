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
import com.szxb.zibo.moudle.function.card.KeyBorad;
import com.szxb.zibo.moudle.function.card.PraseCard;
import com.szxb.zibo.moudle.function.scan.ScanManage;
import com.szxb.zibo.moudle.function.unionpay.UnionPay;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.util.apkmanage.AppUtil;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.MiLinkBlockQueue;
import com.szxb.zibo.util.SecretUtils;
import com.szxb.zibo.util.sp.CommonSharedPreferences;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.szxb.jni.SerialCom.SerialComWrite;
import static java.lang.System.arraycopy;

public class DoCmd {
    private static final String TAG = DoCmd.class.getSimpleName();
    private static String priceSetting = "";

    public static BlockingQueue<devCmd> queue = new MiLinkBlockQueue(1);

    public static void doHeart(devCmd myCmd) {
        Log.d(TAG, "doHeart");
        Log.d(TAG, "len = " + myCmd.getnRecvLen());
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
            Log.i("错误", "二维码报错");
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
            devCmd bean = queue.poll(2, TimeUnit.SECONDS);
            if (bean != null && bean.getS() == 0) {
                Log.d(TAG, "取到值了  doGetVersion   刷卡数据？");
                Log.d(TAG, "doGetVersion");
                return new String(bean.getDataBuf());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "doGetVersion err");

        return null;
    }

    public static void doCard(devCmd myCmd) {
        try {
            byte[] cardDate = new byte[myCmd.getnRecvLen()];
            arraycopy(myCmd.getDataBuf(), 0, cardDate, 0, myCmd.getnRecvLen());
            PraseCard.praseCardDate(cardDate);
        } catch (Exception e) {
            Log.i("错误", "doCard:" + e.getMessage());
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
            devCmd bean = queue.poll(2000, TimeUnit.MILLISECONDS);
            if (bean != null && bean.getS() == 0) {
                Log.d(TAG, "doGetVersion");
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
        MiLog.i(TAG, "刷卡 发送消费 = " + FileUtils.byte2Parm(sendCmd, Type.HEX));
        try {
            queue.clear();
            SerialComWrite(sendCmd, sendCmd.length);
            devCmd bean = queue.poll(2000, TimeUnit.MILLISECONDS);
            if (bean != null && bean.getS() == 0) {
                return bean;
            }
        } catch (InterruptedException e) {
            MiLog.i(TAG, "刷卡 queue 报错啦" + e.getMessage());
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
            devCmd bean = queue.poll(2000, TimeUnit.MILLISECONDS);
            Log.d(TAG, "取到值了  checkMac   刷卡数据？");
            if (bean != null && bean.getS() == 0) {
                Log.d(TAG, "doGetVersion");
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
            devCmd bean = queue.poll(2000, TimeUnit.MILLISECONDS);
            if (bean != null && bean.getS() == 0) {
                Log.d(TAG, "doGetVersion");
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
            devCmd bean = queue.poll(2, TimeUnit.SECONDS);
            if (bean != null && bean.getS() == 0 && bean.getDataBuf() != null) {
                Log.d(TAG, "resetPSAM=" + FileUtils.bytesToHexString(bean.getDataBuf()));
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


                //PSAM卡号
                byte[] CpuSerialNum = new byte[10];
                arraycopy(PASMresult, i, CpuSerialNum, 0, CpuSerialNum.length);
                i += CpuSerialNum.length;
                String cpuSerialNum = (String) FileUtils.byte2Parm(CpuSerialNum, Type.HEX);


                ////密钥索引 有卡01
                byte[] CpuKey_index = new byte[1];
                arraycopy(PASMresult, i, CpuKey_index, 0, CpuKey_index.length);
                i += CpuKey_index.length;
                String cpukey_index = (String) FileUtils.byte2Parm(CpuKey_index, Type.HEX);


            }

        } catch (Exception e) {
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
            Log.i("错误", "PASM卡复位失败");
        }
    }

    static long keyTime = 0;

    public static void doKeyPress(devCmd devCmd) {
        try {
            Log.i("按键", "doKeyPress(DoCmd.java:237)" + FileUtils.bytesToHexString(devCmd.getDataBuf()));

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
            Log.i("错误", "doKeyPress(DoCmd.java:237)" + e.getMessage());
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
                Log.i("获取银联密钥的数据", FileUtils.bytesToHexString(sendDate));
                Rx.getInstance().sendMessage("unionParam", sendDate);
            } else {
                setUnionparam(devCmd);
            }
        } catch (Exception e) {
            Log.i("错误", "获取银联参数数据");
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
            devCmd bean = queue.poll(2, TimeUnit.SECONDS);
            if (bean != null && bean.getS() == 0 && bean.getDataBuf() != null) {
                Log.d(TAG, "doGetVersion");
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
                MiLog.i("重新寻卡", "重新寻卡");
                devCmd verCmd = new devCmd();
                verCmd.setCla((byte) 0x86);
                verCmd.setIns((byte) 0x45);
                verCmd.setDataBuf(null);
                verCmd.setnRecvLen(0);

                byte[] sendCmd = verCmd.packageData();

                SerialComWrite(sendCmd, sendCmd.length);
                try {
                    devCmd bean = queue.poll(2, TimeUnit.SECONDS);
                    if (bean != null && bean.getS() == 0) {
                        Log.d(TAG, "resetCard");
                    }
                    if (bean == null) {
                        Log.d(TAG, "resetCard null");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "resetCard err");
                }
            }
        });
    }

    public static void dokeyboard(devCmd devCmd) {
        try {
            byte[] keyboardInfo = new byte[devCmd.getnRecvLen()];
            arraycopy(devCmd.getDataBuf(), 0, keyboardInfo, 0, devCmd.getnRecvLen());
            Log.i("键盘按键", FileUtils.bytesToHexString(keyboardInfo));

            KeyBorad keyBorad = new KeyBorad();
            keyBorad.prase(keyboardInfo);

            byte[] key = FileUtils.hexStringToBytes(keyBorad.getDate());
            Log.i("键盘按键 按键代码", keyBorad.getDate() + "         " + new String(key));
            switch (keyBorad.getDate()) {
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
                    if (BusApp.getPosManager().getLineType().equals("P")) {
                        InitConfigZB.addstation();
                    }
                    break;
                case "28"://减站
                    if (BusApp.getPosManager().getLineType().equals("P")) {
                        InitConfigZB.refuseStation();
                    }
                    break;
                case "6c"://确认
                    BusApp.getPosManager().setBasePrice((int) (Float.parseFloat(priceSetting) * 100));
                    BusToast.showToast("设置票价：" + FileUtils.fen2Yuan(BusApp.getPosManager().getBasePrice()), true);
                    break;
                case "70"://F1
                    break;
                case "71"://F2
                    break;
                case "72"://F3
                    break;
                case "73"://清除
                    priceSetting = "";
                    break;
            }
        } catch (Exception e) {
            MiLog.i("按键 错误", "" + e.getMessage());
        }
    }

    public static void sendStationInfo() {
        devCmd verCmd = new devCmd();
        verCmd.setCla((byte) 0x8e);
        verCmd.setIns((byte) 0x03);
        KeyBorad keyBorad = new KeyBorad();
        keyBorad.setDestinationAddress("08");
        keyBorad.setOriginAddress("0a");
        keyBorad.setStart("aabb");

        keyBorad.setStatus("00");
        keyBorad.setDivice("02");
        keyBorad.setCommand("00");

        byte[] lineNo = BusApp.getPosManager().getLineNo().getBytes();
        byte[] lineNoNeed = new byte[9];
        arraycopy(lineNo, 0, lineNoNeed, 0, lineNo.length);
        byte[] lineNolenth = new byte[]{(byte) lineNo.length};

        byte[] stationNo = new byte[]{(byte) BusApp.getPosManager().getStationID()};
        byte[] stationNoNeed = new byte[2];
        arraycopy(stationNo, 0, stationNoNeed, stationNoNeed.length - stationNo.length, stationNo.length);

        Log.i("发送站点数据 上下行", BusApp.getPosManager().getDirection());
        byte[] diraction = new byte[]{(byte) (Integer.parseInt(BusApp.getPosManager().getDirection()) == 1 ? 0 : 1)};
        Log.i("发送站点数据 上下行", FileUtils.bytesToHexString(diraction));
        byte[] stationName = new byte[5];
        try {
            stationName = BusApp.getPosManager().getStationName().getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] stationNameNeed = new byte[30];
        arraycopy(stationName, 0, stationNameNeed, 0, stationName.length);
        byte[] stationNameLen = new byte[]{(byte) stationName.length};

        byte[] bytes = FileUtils.mergeByte(lineNolenth, lineNoNeed, stationNoNeed, diraction, stationNameLen, stationNameNeed);
        MiLog.i("发送站点数据", "当前站信息：" + FileUtils.bytesToHexString(bytes));
        keyBorad.setDataLenth(bytes.length);
        keyBorad.setDate(FileUtils.bytesToHexString(bytes));

        byte[] alldate = FileUtils.hexStringToBytes(keyBorad.toString());
//        byte[] alldate = FileUtils.hexStringToBytes("080aaabb000200002c0634303032353500000002000007c9cfd0d03220200000000000000000000000000000000000000000000000f1");
        verCmd.setDataBuf(alldate);
        verCmd.setnRecvLen(alldate.length);
        MiLog.i("发送站点数据", FileUtils.bytesToHexString(alldate));
        queue.clear();
        byte[] sendCmd = verCmd.packageData();
        SerialComWrite(sendCmd, sendCmd.length);
//        try {
//            devCmd bean = queue.poll(2, TimeUnit.SECONDS);
//            if (bean != null && bean.getS() == 0 && bean.getDataBuf() != null) {
//                Log.d(TAG, "doGetVersion");
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

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
            Log.d(TAG, "刷卡锁卡");
            devCmd bean = queue.poll(2000, TimeUnit.MILLISECONDS);
            Log.d(TAG, "取到值了  checkMac   刷卡数据？" + FileUtils.bytesToHexString(bean.getDataBuf()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
