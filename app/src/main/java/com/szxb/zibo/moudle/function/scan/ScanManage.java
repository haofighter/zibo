package com.szxb.zibo.moudle.function.scan;

import android.util.Log;

import com.hao.lib.Util.FileUtils;

import com.hao.lib.Util.MiLog;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.haikou.param.BuildConfigParam;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.moudle.function.scan.alicode.AliCodeManage;
import com.szxb.zibo.moudle.function.scan.freecode.FreeCodeManage;
import com.szxb.zibo.moudle.function.scan.tencentcode.TencentCodeManage;
import com.szxb.zibo.moudle.function.unionpay.dispose.BankQRParse;
import com.szxb.zibo.moudle.function.unionpay.dispose.BankResponse;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.szxb.zibo.moudle.function.unionpay.UnionUtil.notice;

public class ScanManage {
    private ScanManage() {
    }

    public static ScanManage getInstance() {
        return ScanManageHelp.scanManage;
    }

    static class ScanManageHelp {
        public static final ScanManage scanManage = new ScanManage();
    }


    String oldScan = "";
    long scanInputTime = 0;
    long scanISpace = 2000;//刷码间隔时间


    public void scanRe(byte[] qrCode) throws Exception {
        try {
            String result = new String(qrCode);
            long time = Math.abs(scanInputTime - System.currentTimeMillis());
            MiLog.i("二维码","   "+time+"    二维码数据："+result+"      \n"+FileUtils.bytesToHexString(qrCode));
            if ( time<scanISpace && oldScan.equals(result)){//300ms视为一次二维码
                return;
            }

            oldScan = result;
            scanInputTime = System.currentTimeMillis();


            if (BusApp.getInstance().checkSetting()) {
                return;
            }

            if (isMyQRcode(result)) {//小兵二维码
                BusApp.getPosManager().setBasePrice(1);
                BusToast.showToast("设置票价成功", true);
            } else if (result.contains("BlueRing")) {
                FreeCodeManage.getInstance().posScan(result);
            } else if (isTenQRcode(result)) {//腾讯二维码
                if (BusApp.getPosManager().getLineNo().equals("")) {
                    BusToast.showToast("请设置线路", false);
                    return;
                }
                TencentCodeManage.getInstance().posScan(qrCode);
            } else if (qrCode.length >= 332 && qrCode.length <= 364) {
                if (BusApp.getPosManager().getLineNo().equals("")) {
                    BusToast.showToast("请设置线路", false);
                    return;
                }
                FreeCodeManage.getInstance().praseJTBScan(qrCode);
            } else {
                //如果没有线路号 则提示请设置线路
                if (BusApp.getPosManager().getLineNo().equals("")) {
                    BusToast.showToast("请设置线路", false);
                } else {
                    if (isAllNum(result) && result.length() < 50&&result.startsWith("62")) {//云闪付二维码
//                        if (!BusApp.getInstance().getNetWorkState()) {
//                            BusToast.showToast("暂不支持此方式交易", false);
//                            SoundPoolUtil.play(VoiceConfig.zanshibunengshiyongcifangshijiaoyi);
//                            return;
//                        }

                        //用于未设置银联参数
                        if (FileUtils.deleteCover(BusllPosManage.getPosManager().getMacKey()).equals("")) {
                            BusToast.showToast("暂不支持此方式乘车", false);
                            SoundPoolUtil.play(VoiceConfig.zanshibunengshiyongcifangshijiaoyi);
                            return;
                        }

                        if (BusApp.oldCardTag.equals(result)) {
                            BusToast.showToast("请刷新重扫", false);
                            SoundPoolUtil.play(VoiceConfig.qingshuaxinchongsao);
                            return;
                        }
                        int money = PraseLine.getPayPrice("41", "00", BusApp.getPosManager().getBasePrice());

                        if (money > 1500) {
                            notice(VoiceConfig.qingchongshua, "金额超出最大限制[" + money + "]", false);
                            return;
                        } else if (money == -1) {
                            SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                            BusToast.showToast("无票价", false);
                            return;
                        }

                        BankQRParse qrParse = new BankQRParse();
                        BankResponse response = qrParse.parseResponse(money, result);
                        if (response.getResCode() > 0) {
                            Log.i("银联交易", "返回" + System.currentTimeMillis());
                            BusToast.showToast(response.getMsg(), true);
                        }
                    } else if (result.length() > 50) {//交通部二维码
                        AliCodeManage.getInstance().posScan(qrCode);//阿里二维码验证
                    } else {
                        BusToast.showToast("无效码", false);
                        SoundPoolUtil.play(VoiceConfig.wuxiaoma);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MiLog.i("二维码错误", e.getMessage());
        }

    }


    private boolean isMyQRcode(String qrcode) {
        return qrcode != null && qrcode.indexOf("szxb") == 0;
    }


    public static boolean isTenQRcode(String qrcode) {
        return qrcode != null && qrcode.indexOf("TX") == 0;
    }


    /**
     * @param var .
     * @return 是否是全数字
     */
    public static boolean isAllNum(String var) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(var);
        return matcher.matches();
    }

}
