package com.szxb.zibo.moudle.function.unionpay;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.BankPay;
import com.szxb.java8583.module.BusCard;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.cmd.DoCmd;
import com.szxb.zibo.cmd.devCmd;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.db.dao.UnionAidEntityDao;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.moudle.function.unionpay.entity.PassCode;
import com.szxb.zibo.moudle.function.unionpay.entity.TERM_INFO;
import com.szxb.zibo.moudle.function.unionpay.entity.UnionAidEntity;
import com.szxb.zibo.moudle.function.unionpay.unionutil.TLV;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.io.File;
import java.util.*;

import static com.szxb.zibo.moudle.function.unionpay.UnionUtil.*;
import static com.szxb.zibo.util.DateUtil.getCurrentDate;
import static java.lang.System.arraycopy;

/**
 * 作者：Tangren on 2018-07-07
 * 包名：com.szxb.unionpay
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class UnionCard {

    private List<String[]> listTLV;

    private Map<String, String> mapTLV;

    private TERM_INFO term_info = new TERM_INFO();

    private int money = 1;

    private ArrayList<Map<String, String>> AIDiameters;

    private PassCode retPassCode = new PassCode();

    private Map<String, String> aid = new HashMap<>();

    private UnionCard() {
        AIDiameters = new ArrayList<>();
        UnionAidEntityDao dao = DBCore.getDaoSession().getUnionAidEntityDao();
        List<UnionAidEntity> list = dao.queryBuilder().build().list();
        for (int i = 0; i < list.size(); i++) {
            String useaidparameter = list.get(i).getIcParam().substring(2, list.get(i).getIcParam().length());
            Log.d("Field AID", useaidparameter);
            List<String[]> listaidparameters = TLV.decodingTLV(useaidparameter);
            Map<String, String> aidparametersMap = TLV
                    .decodingTLV(listaidparameters);
            AIDiameters.add(aidparametersMap);
        }

    }

    private volatile static UnionCard instance = null;

    public static UnionCard getInstance() {
        if (instance == null) {
            synchronized (UnionCard.class) {
                if (instance == null) {
                    instance = new UnionCard();
                }
            }
        }
        return instance;
    }


    private String tempStr;
    private long lastScanTime2 = 0;
    private int ret = 0;

    public void run(String aid) {
        try {

            money = PraseLine.getPayPrice("41", "00", BusApp.getPosManager().getBasePrice());

            if (money > 1500) {
                notice(VoiceConfig.qingchongshua, "金额超出最大限制[" + money + "]", false);
                return;
            } else if (money == -1) {
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                BusToast.showToast("无票价", false);
                return;
            }

            checkUnion(aid);
            if (ret != 0) {
                BusToast.showToast("刷卡失败[" + ret + "]", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = EXCEPTION;
        }
    }

    /**
     * @param result .
     * @return 检查是否拦截
     */
    private boolean filterCheck(String result) {
        Log.d("LoopCard", "(filterCheck.java:296)result=" + result + ",tempStr=" + tempStr);
        if (TextUtils.equals(result, tempStr) &&
                !checkQR2(SystemClock.elapsedRealtime(), lastScanTime2)) {
            return true;
        }
        lastScanTime2 = SystemClock.elapsedRealtime();
        return false;
    }

    /**
     * @param currentTime .
     * @param lastTime    .
     * @return .
     */
    private boolean checkQR2(long currentTime, long lastTime) {
        return currentTime - lastTime > 3000;
    }

    /**
     * @param currentTime .
     * @param lastTime    .
     * @return .
     */
    private boolean checkQR(long currentTime, long lastTime) {
        return currentTime - lastTime > 2000;
    }


    public void checkUnion(String aid) {

        byte[] head = new byte[]{0x00, (byte) 0xA4, 0x04, 0x00};

        byte[] bAid = FileUtils.hexStringToBytes(aid);
        byte[] sAidCommmond = new byte[bAid.length + 5];
        byte[] aidLeng = new byte[]{(byte) bAid.length};
        arraycopy(head, 0, sAidCommmond, 0, head.length);
        arraycopy(aidLeng, 0, sAidCommmond, 4, aidLeng.length);
        arraycopy(bAid, 0, sAidCommmond, 5, bAid.length);
        String strs = FileUtils.bytesToHexString(sAidCommmond);
        devCmd unionInfo = DoCmd.checkUnion(sAidCommmond);

        MiLog.i("银联刷卡", "aid下发:" + sAidCommmond);
        if (unionInfo == null) {
            BusToast.showToast("解析卡失败,卡校验失败", false);
            return;
        }

        String checkStr = FileUtils.bytesToHexString(unionInfo.getDataBuf());
        listTLV = TLV.decodingTLV(checkStr);
        mapTLV = TLV.decodingTLV(listTLV);

        listTLV = TLV.decodingPDOL(mapTLV.get("9f38"));

        Log.d("银联刷卡", "(run.java:126)9f38>>" + mapTLV.get("9f38"));

        mapTLV = TLV.decodingTLV(listTLV);

        int len = 0;
        StringBuilder pDOLBuilder = new StringBuilder();

        for (String key : mapTLV.keySet()) {
            len += Integer.parseInt(mapTLV.get(key));
            switch (key) {
                case "9f66"://终端交易属性,是否支持CDCVM
                    pDOLBuilder.append(term_info.ttq);
                    break;
                case "9f02"://授权金额（支付金额）
                    String payMoney = String.format("%012d", money);
                    pDOLBuilder.append(payMoney);
                    retPassCode.setTAG9F02(payMoney);
                    break;
                case "9f03"://返现金额，0
                    String str9f03 = String.format("%012d", 0);
                    pDOLBuilder.append(str9f03);
                    retPassCode.setTAG9F03(str9f03);
                    break;
                case "9f1a"://国家代码
                    pDOLBuilder.append(term_info.terminal_country_code);
                    retPassCode.setTAG9F1A(term_info.terminal_country_code);
                    break;
                case "95"://终端验证结果
                    String str95 = String.format("%010d", 0);
                    pDOLBuilder.append(str95);
                    retPassCode.setTAG95(str95);
                    break;
                case "5f2a"://交易货币代码
                    pDOLBuilder.append(term_info.transaction_currency_code);
                    retPassCode.setTAG5F2A(term_info.transaction_currency_code);
                    break;
                case "9a"://交易日期yyMMdd
                    String transDate = getCurrentDate("yyMMdd");
                    pDOLBuilder.append(transDate);
                    retPassCode.setTAG9A(transDate);
                    break;
                case "9c"://交易类型
                    pDOLBuilder.append("00");
                    retPassCode.setTAG9C("00");
                    break;
                case "9f37"://不可预知数
                    Random random = new Random();
                    String randomStr = String.format("%08x",
                            random.nextInt());
                    pDOLBuilder.append(randomStr);
                    retPassCode.setTAG9F37(randomStr);
                    break;
                case "df60"://
                    pDOLBuilder.append("00");
                    break;
                case "9f21"://时间HHmmss
                    String transTime = getCurrentDate("HHmmss");
                    pDOLBuilder.append(transTime);
                    break;
                case "df69"://
                    pDOLBuilder.append("01");
                    break;
            }
        }

        String GPO = "80a80000"
                + Integer.toHexString(len + 2) + "83"
                + Integer.toHexString(len) + pDOLBuilder.toString();

        Log.d("银联刷卡", "(run.java:191)GPO=" + GPO);

        devCmd gpoDate = DoCmd.checkUnion(FileUtils.hex2byte(GPO));
        Log.i("银联刷卡", "gpo下发" +(gpoDate==null?"空数据":FileUtils.bytesToHexString(gpoDate.getDataBuf())));
        if (gpoDate == null) {
            BusToast.showToast("卡解析失败,校验失败2", false);
            return;
        }

        listTLV = TLV.decodingTLV(FileUtils.bytesToHexString(gpoDate.getDataBuf()));
        mapTLV = TLV.decodingTLV(listTLV);

        if (mapTLV.containsKey("9f36")) {
            retPassCode.setTAG9F36(mapTLV.get("9f36"));
        }

        if (mapTLV.containsKey("5f34")) {
            retPassCode.setTAG5F34(mapTLV.get("5f34"));
        }

        if (mapTLV.containsKey("9f10")) {
            retPassCode.setTAG9F10(mapTLV.get("9f10"));
        }

        if (mapTLV.containsKey("57")) {
            retPassCode.setTAG57(mapTLV.get("57"));
        }

        if (mapTLV.containsKey("9f27")) {
            retPassCode.setTAG9F27(mapTLV.get("9f27"));
            Log.d("9F27", retPassCode.getTAG9F27());
        }

        if (mapTLV.containsKey("9f26")) {
            retPassCode.setTAG9F26(mapTLV.get("9f26"));
        }

        if (mapTLV.containsKey("82")) {
            retPassCode.setTAG82(mapTLV.get("82"));
        }

        try {
            BusllPosManage.getPosManager().setTradeSeq();
        } catch (Exception e) {
            e.getMessage();
        }

        int index = retPassCode.getTAG57().indexOf("d");

        Log.d("银联刷卡", "(run.java:263)getTAG57=" + retPassCode.getTAG57() + "<<index=" + index);

        String mainCardNo = retPassCode.getTAG57();
        if (index > 0) {
            mainCardNo = retPassCode.getTAG57().substring(0, index);
        }
        String cardNum = retPassCode.getTAG5F34();
        String cardData = retPassCode.getTAG57();
        String tlv = retPassCode.toString();

        if (filterCheck(mainCardNo)) {
            ret = REPEAT;
            if (checkQR(SystemClock.elapsedRealtime(), lastScanTime2) && TextUtils.equals(mainCardNo, tempStr)) {
            }
            return;
        }

        try {
            BusCard busCard = new BusCard();
            busCard.setMainCardNo(mainCardNo);
            busCard.setCardNum(cardNum);
            busCard.setMagTrackData(cardData);
            busCard.setTlv55(tlv);
            busCard.setMacKey(BusllPosManage.getPosManager().getMacKey());
            busCard.setMoney(money);
            busCard.setTradeSeq(BusllPosManage.getPosManager().getTradeSeq());

            Log.i("银联刷卡", "组装请求包");
            Iso8583Message iso8583Message = BankPay.getInstance().payMessage(busCard, false);
            byte[] sendData = iso8583Message.getBytes();

            BusApp.oldCardTag = mainCardNo;

            XdRecord xdRecord = new XdRecord();
            String unionpos = FileUtils.formatHexStringToByteString(4, BusllPosManage.getPosManager().getPosSn());
            String batchNum = FileUtils.formatHexStringToByteString(3, FileUtils.getSHByte(BusllPosManage.getPosManager().getBatchNum()));
            String tradeSeq = FileUtils.getSHByte(FileUtils.formatHexStringToByteString(3, Integer.toHexString(BusllPosManage.getPosManager().getTradeSeq())));
            String extraDate = FileUtils.bytesToHexString(sendData);
            xdRecord.setExtraDate(extraDate);
            xdRecord.setNewExtraDate(unionpos + batchNum + tradeSeq + FileUtils.bytesToHexString("DD".getBytes()));
            xdRecord.setTradePay(money);
            xdRecord.setTradeType("08");
            xdRecord.setUseCardnum(mainCardNo);
            xdRecord.setRecordVersion("0003");
            xdRecord.setMainCardType("42");
            xdRecord.setChildCardType("00");
            xdRecord.setFlag(String.format("%06d", BusllPosManage.getPosManager().getTradeSeq()) + BusllPosManage.getPosManager().getBatchNum());
            xdRecord.setCarNum(BusApp.getPosManager().getBusNo());
            RecordUpload.saveRecord(xdRecord, false, false);
            Log.i("银联刷卡", "更新记录");
            UnionPay.getInstance().exeSSL(UnionConfig.PAY, sendData, true);

            ret = 0;
            tempStr = mainCardNo;
        } catch (Exception e) {
            Log.i("错误", "银联卡刷卡报错" + e.getMessage());
        }
    }

}
