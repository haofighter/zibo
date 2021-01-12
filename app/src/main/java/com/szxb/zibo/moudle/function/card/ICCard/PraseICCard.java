package com.szxb.zibo.moudle.function.card.ICCard;

import android.text.format.DateUtils;

import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.line.CardPlan;
import com.szxb.zibo.config.zibo.line.FarePlan;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.util.Calendar;
import java.util.Date;

import static java.lang.System.arraycopy;

public class PraseICCard {
    static long lastTime = 0;
    static String lastCard = "";


    public static boolean praseIC(byte[] date) throws Exception {
        if (BusApp.getPosManager().getDriverNo().equals("00000000")) {
            BusToast.showToast("请刷司机卡", false);
            SoundPoolUtil.play(VoiceConfig.sijiweishangban);
            return false;
        }

        byte[] ICcardTextByte = new byte[256];
        arraycopy(date, 4, ICcardTextByte, 0, ICcardTextByte.length);

        int index = 0;
        byte[] name = new byte[30];
        arraycopy(ICcardTextByte, index, name, 0, name.length);
        index += name.length;

        byte[] sex = new byte[2];
        arraycopy(ICcardTextByte, index, sex, 0, sex.length);
        index += sex.length;

        byte[] nation = new byte[4];
        arraycopy(ICcardTextByte, index, nation, 0, nation.length);
        index += nation.length;

        byte[] birth = new byte[16];
        arraycopy(ICcardTextByte, index, birth, 0, birth.length);
        index += birth.length;

        byte[] address = new byte[70];
        arraycopy(ICcardTextByte, index, address, 0, address.length);
        index += address.length;

        byte[] cardnumber = new byte[36];
        arraycopy(ICcardTextByte, index, cardnumber, 0, cardnumber.length);
        index += cardnumber.length;
        String cardNumber = new String(cardnumber, "UTF16-LE");

        byte[] organ = new byte[30];
        arraycopy(ICcardTextByte, index, organ, 0, organ.length);
        index += organ.length;

        byte[] startTime = new byte[16];
        arraycopy(ICcardTextByte, index, startTime, 0, startTime.length);
        index += startTime.length;
        String starttime = new String(startTime, "UTF16-LE");

        byte[] endTime = new byte[16];
        arraycopy(ICcardTextByte, index, endTime, 0, endTime.length);
        index += endTime.length;
        String endtime = new String(endTime, "UTF16-LE");

        byte[] rev = new byte[36];
        arraycopy(ICcardTextByte, index, rev, 0, rev.length);
        index += rev.length;
        MiLog.i("身份证", "姓名：" + new String(name, "UTF16-LE")
                + "\n性别：" + new String(sex, "UTF16-LE") + "      " + FileUtils.bytesToHexString(sex)
                + "\n民族：" + new String(nation, "UTF16-LE")
                + "\n生日：" + new String(birth, "UTF16-LE")
                + "\n住址：" + new String(address, "UTF16-LE")
                + "\n身份证号：" + cardNumber
                + "\n有效截止日期：" + endtime
                + "\n签发机关：" + new String(organ, "UTF16-LE")
                + "\n有效起始日期：" + starttime
                + "\n备注：" + new String(rev, "UTF16-LE"));

        int limitRepeatTime = 5 * 60 * 1000;
        FarePlan farePlan = DBManagerZB.checkFarePlan();
        if (farePlan != null) {
            CardPlan cardPlan = DBManagerZB.checkCardPlan(farePlan.getCardCaseNUm(), "72", "00");
            if (cardPlan == null) {
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                BusToast.showToast("未查询到票价方案", true);
            } else {
                limitRepeatTime = Integer.parseInt(cardPlan.getUseInterval());
                int limitUseTime = Integer.parseInt(cardPlan.getNeedCheckStartTime());
                MiLog.i("身份证", "限制时间：" + limitUseTime);
                if (limitUseTime == 0) {
                    limitUseTime = 60;
                }
                MiLog.i("身份证", "是否使用默认限制时间：" + limitUseTime);
                Date birthDay = DateUtil.getDate(new String(birth, "UTF16-LE").trim(), "yyyyMMdd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(birthDay);
                MiLog.i("身份证", "出生年份：" + calendar.get(Calendar.YEAR));
                calendar.set(calendar.get(Calendar.YEAR) + limitUseTime, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                MiLog.i("身份证", "限制年份：" + calendar.get(Calendar.YEAR));
                Date limitDay = calendar.getTime();
                MiLog.i("身份证", "允许刷卡的时间：" + DateUtil.getFormat_6().format(limitDay) + "      时间戳：" + limitDay.getTime() + "       是否允许此身份证刷卡：" + (System.currentTimeMillis() > limitDay.getTime()));
                if (!(System.currentTimeMillis() > limitDay.getTime())) {
                    SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                    BusToast.showToast("暂不能使用此方式乘车", true);
                    return false;
                }
            }
        }

        if (System.currentTimeMillis() - lastTime < 3000 && lastCard.equals(cardNumber)) {
            return false;
        }
        lastCard = cardNumber;
        lastTime = System.currentTimeMillis();


        XdRecord xdRecord = new XdRecord();
        int price = PraseLine.getNormalPayPrice("72", "00");
        if (price >=0) {
            xdRecord.setTradePay(price);
        } else {
            xdRecord.setTradePay(0);
        }
        xdRecord.setTradeType("16");

        xdRecord.setRecordVersion("0018");
        xdRecord.setMainCardType("72");
        xdRecord.setChildCardType("00");
        xdRecord.setDirection(BusApp.getPosManager().getDirection());
        xdRecord.setInCardStatus("01");
        xdRecord.setPayType("FD");
        if (BusApp.getPosManager().getLineType().equals("O")) {
            xdRecord.setInCardStatus("00");
        } else {
            xdRecord.setInCardStatus("01");
        }
        if (cardNumber.toLowerCase().contains("x")) {
            cardNumber = cardNumber.toLowerCase().replaceAll("x", "a");
        }

        if (DBManagerZB.checkedBlack(cardNumber)) {
            SoundPoolUtil.play(VoiceConfig.heimingdanka);
            BusToast.showToast("黑名单卡", false);
            return false;
        }


        XdRecord oldXdRecord = DBManagerZB.checkICCardRecordNearNow(FileUtils.hexStringFromatByF(10, cardNumber, false), limitRepeatTime);
        if (oldXdRecord != null) {
            SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
            BusToast.showToast("重复刷卡", true);
            MiLog.i("身份证", "___________________重复刷卡，暂时不能使用_____________________");
            return false;
        }


        SoundPoolUtil.play(VoiceConfig.nianzhangzhe);
        BusToast.showToast("姓名：" + new String(name, "UTF16-LE") + "\n身份证号：" + cardNumber, true);


        xdRecord.setUseCardnum(FileUtils.hexStringFromatByF(10, cardNumber, false));
        String extraDate = FileUtils.hexStringFromatByF(10, cardNumber, false) + FileUtils.formatStringToByteStringF(4, starttime) + FileUtils.formatStringToByteStringF(4, endtime);

        xdRecord.setExtraDate(extraDate);

        MiLog.i("身份证", "___________________刷卡完毕_____________________");
        RecordUpload.saveRecord(xdRecord);
        return true;
    }
}
