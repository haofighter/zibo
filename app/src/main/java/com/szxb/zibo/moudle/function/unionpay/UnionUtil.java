package com.szxb.zibo.moudle.function.unionpay;

import android.text.TextUtils;

import android.util.Log;

import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.voice.SoundPoolUtil;

public class UnionUtil {

    //银联卡
    public static final int PAY_TYPE_BANK_IC = "BANK_IC".hashCode();
    //银联二维码
    public static final int PAY_TYPE_BANK_QR = "BANK_QR".hashCode();

    /**
     * @param resCode 状态
     * @return 银联卡上传状态
     */
    public static String unionPayStatus(String resCode) {
        if (TextUtils.isEmpty(resCode)) {
            return "超时";
        }
        switch (resCode) {
            case "00":
                return "已扣款";
            case "444":
                return "已冲正";
            case "03":
                return "无效商户";
            case "04":
                return "无效卡";
            case "05":
                return "认证失败";
            case "13":
                return "无效金额";
            case "14":
                return "无效卡号";
            case "30":
                return "报文错误";
            case "41":
                return "挂失卡";
            case "43":
                return "被窃卡";
            case "51":
                return "余额不足";
            case "54":
                return "卡过期";
            case "57":
                return "此卡不允许交易";
            case "58":
                return "无效终端";
            case "97":
                return "终端未登记";
            case "98":
                return "超时";
            case "A0":
                return "重签到";
            case "A2":
            case "A4":
            case "A5":
            case "A6":
                return "待确认";
            case "94":
                return "流水重复";
            case "408":
                return "超时";
            default:
                return "UNK[" + resCode + "]";
        }
    }

    /**
     * @param music  .
     * @param tipVar .
     * @param isOk   .
     */
    public static void notice(int music, String tipVar, boolean isOk) {
        SoundPoolUtil.play(music);
        BusToast.showToast(tipVar, isOk);
    }


    public static final int SIGN = 100000;
    public static final int PAY = 200000;

    //null
    public static final int NULL = -1;
    public static final int NULL2 = -2;
    public static final int NULL3 = -3;
    public static final int NULL4 = -4;
    public static final int NULL5 = -5;
    public static final int NULL6 = -6;
    public static final int NULL7 = -7;

    //invalid
    public static final int INVALID = -8;

    public static final int INVALID2 = -11;

    //exception
    public static final int EXCEPTION = -9;

    //repeat
    public static final int REPEAT = -10;

    //String 2 int
    public static int string2Int(String var) {
        try {
            return Integer.valueOf(var);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Util","(string2Int.java:33)var=" + var + ",数字类型转换异常>>" + e.toString());
            return 0;
        }
    }
}
