package com.szxb.zibo.moudle.function.card;


import com.hao.lib.Util.*;

import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.zibo.BuildConfig;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.moudle.function.card.CPU.File05NewCPUInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File10NewCPUInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File15LocalInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File15NewCPUInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File16NewCPUInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File17NewCPUInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File18LocalInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File18NewCPUInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File1CLocalInfoEntity;
import com.szxb.zibo.moudle.function.card.JTB.File15JTBInfoEntity;
import com.szxb.zibo.moudle.function.card.JTB.File17JTBInfoEntity;
import com.szxb.zibo.moudle.function.card.JTB.File18JTBInfoEntity;
import com.szxb.zibo.moudle.function.card.JTB.File1AJTBInfoEntity;
import com.szxb.zibo.moudle.function.card.JTB.File1EJTBInfoEntity;
import com.szxb.zibo.moudle.function.card.M1.FileM1InfoEntity;
import com.szxb.zibo.moudle.function.unionpay.UnionCard;
import com.szxb.zibo.moudle.function.unionpay.unionutil.TLV;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.System.arraycopy;

public class CardInfoEntity implements Cloneable {

    //------------------------------------互联互通卡
    String status;    //用于存储错误码 00 正常的 01
    String sw;        //每次操作后的sw值，用于配合错误码上报错误
    String new_time;   //在寻卡阶段为k21时间，在消费阶段为消费时间

    String atqa;    //用于判断uid长度和
    String uid;        //唯一识别符
    String sak;        //选择确认
    String ats;    //RATS返回值
    public String selete_aid;
    int consumeType;//消费类型
    public boolean isManageCard;//管理卡
    public String cardType;//卡主类型  扣款类型  主要用于扣款的时候使用  特殊卡过期后需要以普通卡的规则扣费
    public String childCardType;//卡字类型  扣款类型  主要用于扣款的时候使用  特殊卡过期后需要以普通卡的规则扣费
    public String realCardType;//实际卡主类型  扣款类型
    public String realChildCardType;//实际卡字类型
    String cardNo;
    int transeType;//当前卡的交易类型   0 单票  1 上车  2 下车  3 补票  4 异常
    String transeTime;//当前卡的交易时间

    List<String> manageCardType = new ArrayList<>();

    {
        manageCardType.add("1100");//采集卡
        manageCardType.add("0601");
        manageCardType.add("0611");
        manageCardType.add("0621");
        manageCardType.add("1800");
        manageCardType.add("1000");
    }


    /*********老CPU**********/
    File15LocalInfoEntity file15LocalInfoEntity;
    File18LocalInfoEntity file18LocalInfoEntity;

    /*********新CPU**********/
    File05NewCPUInfoEntity file05NewCPUInfoEntity;
    File15NewCPUInfoEntity file15NewCPUInfoEntity;
    File16NewCPUInfoEntity file16NewCPUInfoEntity;
    File17NewCPUInfoEntity file17NewCPUInfoEntity;
    File18NewCPUInfoEntity file18NewCPUInfoEntity;
    File10NewCPUInfoEntity file10NewCPUInfoEntity;
    /*********M1***********/
    FileM1InfoEntity fileM1InfoEntity;

    /***********多票信息**************/
    File1CLocalInfoEntity file1CLocalInfoEntity;


    /***********JTB卡片**************/
    File15JTBInfoEntity file15JTBInfoEntity;
    File17JTBInfoEntity file17JTBInfoEntity;
    File18JTBInfoEntity file18JTBInfoEntity;
    File1AJTBInfoEntity file1AJTBInfoEntity;
    File1EJTBInfoEntity file1EJTBInfoEntity;

    private long balance;  //余额
    int payAllPrice;  //打折前的票价
    boolean isExpire;  //是否过期

    public String putDate(byte[] bytes) {

        MiLog.i("刷卡", "检测到卡片：" + FileUtils.bytesToHexString(bytes));
        int i = 0;

        //用于存储错误码
        byte[] Status = new byte[1];
        arraycopy(bytes, i, Status, 0, Status.length);
        i += Status.length;
        status = (String) FileUtils.byte2Parm(Status, Type.HEX);


        //每次操作后的sw值，用于配合错误码上报错误
        byte[] Sw = new byte[2];
        arraycopy(bytes, i, Sw, 0, Sw.length);
        i += Sw.length;
        sw = (String) FileUtils.byte2Parm(Sw, Type.HEX);


        //在寻卡阶段为k21时间，在消费阶段为消费时间
        byte[] New_time = new byte[7];
        arraycopy(bytes, i, New_time, 0, New_time.length);
        i += New_time.length;
        new_time = (String) FileUtils.byte2Parm(New_time, Type.HEX);

        //用于判断uid长度和
        byte[] Atqa = new byte[2];
        arraycopy(bytes, i, Atqa, 0, Atqa.length);
        i += Atqa.length;
        atqa = (String) FileUtils.byte2Parm(Atqa, Type.HEX);


        //唯一识别符
        byte[] Uid = new byte[4];
        arraycopy(bytes, i, Uid, 0, Uid.length);
        i += Uid.length;
        uid = (String) FileUtils.byte2Parm(Uid, Type.HEX);


        //选择确认
        byte[] Sak = new byte[1];
        arraycopy(bytes, i, Sak, 0, Sak.length);
        i += Sak.length;
        sak = (String) FileUtils.byte2Parm(Sak, Type.HEX);

        //RATS返回值
        byte[] Ats = new byte[64];
        arraycopy(bytes, i, Ats, 0, Ats.length);
        i += Ats.length;
        ats = (String) FileUtils.byte2Parm(Ats, Type.HEX);

        //01互联互通卡，00银联卡，03本地卡，04 M1卡
        byte[] Selete_aid = new byte[1];
        arraycopy(bytes, i, Selete_aid, 0, Selete_aid.length);
        i += Selete_aid.length;
        selete_aid = (String) FileUtils.byte2Parm(Selete_aid, Type.HEX);

        MiLog.i("刷卡", "uid=" + uid + "       status=" + status);
        if (!status.equals("00")) {
            if (selete_aid.equals("10")) {//住建部CPU锁卡
                if (!BusApp.oldCardTag.equals(uid)) {
                    SoundPoolUtil.play(VoiceConfig.heimingdanka);
                    BusToast.showToast("黑名单卡(锁卡)", false);
                    BusApp.oldCardTag = uid;
                    BusApp.oldCardTime = System.currentTimeMillis();
                } else {
                    if (System.currentTimeMillis() - BusApp.oldCardTime > 2000) {
                        SoundPoolUtil.play(VoiceConfig.heimingdanka);
                        BusToast.showToast("黑名单卡(锁卡)", false);
                        BusApp.oldCardTag = uid;
                        BusApp.oldCardTime = System.currentTimeMillis();
                    }
                }
            }
            return status;
        }
        MiLog.i("刷卡", "selete_aid=" + selete_aid);
        try {
            //判断卡类型进行解析
            byte[] date = new byte[1024];
            arraycopy(bytes, i, date, 0, bytes.length - i);
            if (selete_aid.equals("04")) {//M1卡
                praseM1Card(date);
                if (fileM1InfoEntity.getBlock_19().getBlacklist_type_2().equals("04")) {
                    SoundPoolUtil.play(VoiceConfig.heimingdanka);
                    BusToast.showToast("黑名单卡", false);
                    return "99";
                }
            } else if ((selete_aid.equals("00"))) {//银行卡
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                BusToast.showToast("暂时不能使用此方式乘车", false);
//                praseUnionCard(date);
                return "01";
            } else if (selete_aid.equals("03")) {//CPU卡 本地卡
                praseLoaclCard(date);
            } else if (selete_aid.equals("02")) {//住建部CPU
                praseNewCPUCard(date);
            } else if (selete_aid.equals("01")) {//交通部
                praseJTBCard(date);
            }
            MiLog.i("刷卡", "主卡类型：" + cardType + "       字卡类型：" + childCardType + "          实际主卡类型：" + realCardType + "          实际字卡类型：" + realChildCardType + "     卡号：" + cardNo);
            return "00";
        } catch (Exception e) {
            e.printStackTrace();
            return status;
        }
    }

    private void praseJTBCard(byte[] date) {
        int i = 0;
        byte[] card_pan = new byte[8];
        arraycopy(date, i, card_pan, 0, card_pan.length);
        i += card_pan.length;


        file15JTBInfoEntity = new File15JTBInfoEntity();
        i = file15JTBInfoEntity.praseFile(i, date);
        this.cardNo = file15JTBInfoEntity.getPan();

        file17JTBInfoEntity = new File17JTBInfoEntity();
        i = file17JTBInfoEntity.praseFile(i, date);

        file18JTBInfoEntity = new File18JTBInfoEntity();
        i = file18JTBInfoEntity.praseFile(i, date);

        file1EJTBInfoEntity = new File1EJTBInfoEntity();
        i = file1EJTBInfoEntity.praseFile(i, date);

        file1AJTBInfoEntity = new File1AJTBInfoEntity();
        i = file1AJTBInfoEntity.praseFile(i, date);

        byte[] blance = new byte[4];
        arraycopy(date, i, blance, 0, blance.length);
        i += blance.length;
        String balanceStr = FileUtils.bytesToHexString(blance);
        balance = FileUtils.hexStringToInt(balanceStr);


        //判断是否为本地卡
        if (file15JTBInfoEntity.getCard_issuer().contains("03664530")) {
            realCardType = cardType = "65";
            realChildCardType = childCardType = "02";
        } else {
            realCardType = cardType = "65";
            realChildCardType = childCardType = "03";
        }

        MiLog.i("刷卡", "交通部卡余额：" + balance);
    }

    private void praseNewCPUCard(byte[] date) {
        int i = 0;
        file05NewCPUInfoEntity = new File05NewCPUInfoEntity();
        i = file05NewCPUInfoEntity.praseFile(i, date);
        realCardType = cardType = file05NewCPUInfoEntity.getCard_main_type();
        realChildCardType = childCardType = file05NewCPUInfoEntity.getCard_type();


        file15NewCPUInfoEntity = new File15NewCPUInfoEntity();
        i = file15NewCPUInfoEntity.praseFile(i, date);

        file16NewCPUInfoEntity = new File16NewCPUInfoEntity();
        i = file16NewCPUInfoEntity.praseFile(i, date);

        file17NewCPUInfoEntity = new File17NewCPUInfoEntity();
        i = file17NewCPUInfoEntity.praseFile(i, date);

        file18NewCPUInfoEntity = new File18NewCPUInfoEntity();
        i = file18NewCPUInfoEntity.praseFile(i, date);

        file10NewCPUInfoEntity = new File10NewCPUInfoEntity();
        i = file10NewCPUInfoEntity.praseFile(i, date);

        byte[] blance = new byte[4];
        arraycopy(date, i, blance, 0, blance.length);
        i += blance.length;
        cardNo = file05NewCPUInfoEntity.getSerial_number();
        String balanceStr = FileUtils.bytesToHexString(blance);
        balance = FileUtils.hexStringToInt(balanceStr);

        isManageCard = manageCardType.contains(cardType + childCardType);


        if (System.currentTimeMillis() - DateUtil.getDateLong(file05NewCPUInfoEntity.getValid_date(), "yyyyMMdd") > 0) {
            isExpire = true;
        } else {
            isExpire = false;
        }
        MiLog.i("刷卡", "过期时间：" + file05NewCPUInfoEntity.getValid_date() + "     是否过期：" + isExpire);
    }

    //本地卡解析
    private void praseLoaclCard(byte[] date) {
        int i = 0;
        file15LocalInfoEntity = new File15LocalInfoEntity();
        i = file15LocalInfoEntity.praseFile15Local(i, date);

        file1CLocalInfoEntity = new File1CLocalInfoEntity();
        i = file1CLocalInfoEntity.praseFile1CLocal(i, date, selete_aid);

        file18LocalInfoEntity = new File18LocalInfoEntity();
        i = file18LocalInfoEntity.praseFile18Local(i, date);

        byte[] Balance = new byte[4];
        arraycopy(date, i, Balance, 0, Balance.length);
        String balanceStr = FileUtils.bytesToHexString(Balance);
        balance = FileUtils.hexStringToInt(balanceStr);


//        cardType = file15LocalInfoEntity.getCard_type();
//        childCardType = "00";
        realChildCardType = cardType = "65";
        realChildCardType = childCardType = "01";
        cardNo = file15LocalInfoEntity.getPan();
        if (System.currentTimeMillis() - DateUtil.getDateLong(file15LocalInfoEntity.getValid_time(), "yyyyMMdd") > 0) {
            isExpire = true;
        } else {
            isExpire = false;
        }

        MiLog.i("刷卡", "过期时间：" + file05NewCPUInfoEntity.getValid_date() + "     是否过期：" + isExpire);
    }

    //银联卡解析
    private void praseUnionCard(final byte[] date) {

        if (BusApp.getInstance().checkSetting()) {
            return;
        }
        try {
            if (FileUtils.deleteCover(BusllPosManage.getPosManager().getMacKey()).equals("")) {
                BusToast.showToast("暂不支持此方式乘车", false);
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyongcifangshijiaoyi);
                return;
            }
        } catch (Exception e) {
            BusToast.showToast("暂不支持此方式乘车", false);
            SoundPoolUtil.play(VoiceConfig.zanshibunengshiyongcifangshijiaoyi);
            return;
        }


        ThreadUtils.getInstance().createSingle("union").execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int i = 0;
                    byte[] Ppse_length = new byte[2];
                    arraycopy(date, 0, Ppse_length, 0, Ppse_length.length);
                    int ppse_length = FileUtils.hexStringToInt(FileUtils.bytesToHexString(Ppse_length));
                    i += Ppse_length.length;

                    byte[] Ppse = new byte[ppse_length];
                    arraycopy(date, i, Ppse, 0, Ppse.length);
                    String ppse = FileUtils.bytesToHexString(Ppse);
                    String ppseStr = new String(Ppse);
                    i += Ppse_length.length;

                    List<String[]> listTLV = TLV.decodingTLV(ppse);
                    Map<String, String> mapTLV = TLV.decodingTLV(listTLV);

                    String aid = mapTLV.get("4f");
                    if (aid != null && aid.startsWith("a000000333")) {
                        UnionCard.getInstance().run(aid);
                    }
                } catch (Exception e) {
                    MiLog.i("银联刷卡错误", e.toString());
                }
            }
        });
    }

    private void praseM1Card(byte[] date) {
        int i = 0;
        consumeType = 0x00;
        //解析M1文件
        fileM1InfoEntity = new FileM1InfoEntity();
        i = fileM1InfoEntity.praseFileM1(i, date, selete_aid);
        realCardType = cardType = fileM1InfoEntity.getBlock_4().getCard_type();
        realChildCardType = childCardType = fileM1InfoEntity.getBlock_5().child_cardType;
        cardNo = fileM1InfoEntity.getBlock_4().getIssuer_code();//TODO M1卡无法获取到获取到卡号
        balance = fileM1InfoEntity.getBlock_9().getBlanace();
        if (balance == 0) {
            balance = fileM1InfoEntity.getBlock_A().getBlanace();
        }

        file1CLocalInfoEntity = fileM1InfoEntity.getBlock_1C1D();
        if (System.currentTimeMillis() - DateUtil.getDateLong(fileM1InfoEntity.getBlock_5().getValid_time(), "yyyyMMdd") > 0) {
            isExpire = true;
        } else {
            isExpire = false;
        }
        MiLog.i("刷卡", "过期时间：" + fileM1InfoEntity.getBlock_5().getValid_time() + "       是否过期：" + isExpire);

        isManageCard = manageCardType.contains(cardType + childCardType);
    }

    //获取当前卡片的多票信息
    public File1CLocalInfoEntity getMorePriceInfo() {
        return file1CLocalInfoEntity;
    }

    //获取当前卡片的多票信息
    public File17NewCPUInfoEntity getnewCPUMorePriceInfo() {
        return file17NewCPUInfoEntity;
    }

    //获取当前卡片的多票信息
    public File1AJTBInfoEntity getJTBMorePriceInfo() {
        return file1AJTBInfoEntity;
    }

    public int getTranseType() {
        return transeType;
    }

    public void setTranseType(int transeType) {
        this.transeType = transeType;
    }

    public String getTranseTime() {
        return transeTime;
    }

    public void setTranseTime(String transeTime) {
        this.transeTime = transeTime;
    }

    public void setPre_preferential_amount(int i) {
        if (selete_aid.equals("03") || selete_aid.equals("04")) {
            getMorePriceInfo().setPre_preferential_amount(i);
        } else if (selete_aid.equals("02")) {
            getnewCPUMorePriceInfo().setPre_preferential_amount(i);
        }
    }

    public String getPre_preferential_amount() {
        if (selete_aid.equals("03") || selete_aid.equals("04")) {
            return getMorePriceInfo().getPre_preferential_amount();
        } else if (selete_aid.equals("02")) {
            return getnewCPUMorePriceInfo().getPre_preferential_amount();
        } else if (selete_aid.equals("01")) {
            return file1AJTBInfoEntity.getBoarding_max_amount_1a();
        }
        return "00";
    }

    public void setFull_fare(int fullprice) {
        if (selete_aid.equals("03")) {
            getMorePriceInfo().setFull_fare(fullprice);
        } else if (selete_aid.equals("02")) {
            getnewCPUMorePriceInfo().setFull_fare(fullprice);
        } else if (selete_aid.equals("01")) {
            file1AJTBInfoEntity.setBoarding_max_amount_1a(fullprice);
        }
    }

    public void setComplete_mark(String s) {
        if (selete_aid.equals("03") || selete_aid.equals("04")) {
            getMorePriceInfo().setComplete_mark(s);
        } else if (selete_aid.equals("02")) {
            getnewCPUMorePriceInfo().setComplete_mark(s);
        } else if (selete_aid.equals("01")) {
            getJTBMorePriceInfo().setTransaction_status_1a(s);
        }
    }

    public String getComplete_mark() {
        if (selete_aid.equals("03") || selete_aid.equals("04")) {
            return getMorePriceInfo().getComplete_mark();
        } else if (selete_aid.equals("02")) {
            return getnewCPUMorePriceInfo().getComplete_mark();
        } else if (selete_aid.equals("01")) {
            return getJTBMorePriceInfo().getTransaction_status_1a();
        }
        return "00";
    }

    public int getFull_fare() {
        if (selete_aid.equals("03") || selete_aid.equals("04")) {
            return getMorePriceInfo().getFull_fare();
        } else if (selete_aid.equals("02")) {
            return Integer.parseInt(getnewCPUMorePriceInfo().getFull_fare(), 16);
        } else if (selete_aid.equals("01")) {
            return Integer.parseInt(getJTBMorePriceInfo().getBoarding_max_amount_1a(), 16);
        }
        return 0;
    }

    public int getBoarding_site_indexInt() {
        if (selete_aid.equals("03") || selete_aid.equals("04")) {
            return getMorePriceInfo().getBoarding_site_indexInt();
        } else if (selete_aid.equals("02")) {
            return getnewCPUMorePriceInfo().getBoarding_site_indexInt();
        } else if (selete_aid.equals("01")) {
            return getJTBMorePriceInfo().getBoarding_the_site_1a();
        }
        return 0;
    }

    public long getBalance() {
        return balance;
    }


    public String getVehicle_number() {
        if (selete_aid.equals("03") || selete_aid.equals("04")) {
            return getMorePriceInfo().getVehicle_number();
        } else if (selete_aid.equals("02")) {
            return getnewCPUMorePriceInfo().getVehicle_number();
        } else if (selete_aid.equals("02")) {
            return getnewCPUMorePriceInfo().getVehicle_number();
        }
        return "000000";
    }

    public String getDriver_direction() {
        if (selete_aid.equals("03") || selete_aid.equals("04")) {
            return getMorePriceInfo().getDriver_direction();
        } else if (selete_aid.equals("02")) {
            return getnewCPUMorePriceInfo().getDriver_direction();
        }
        return "00";
    }

    public File18LocalInfoEntity getFile18LocalInfoEntity() {
        return file18LocalInfoEntity;
    }

    public File18NewCPUInfoEntity getFile18NewCPUInfoEntity() {
        return file18NewCPUInfoEntity;
    }

    public String getDriverNum() {
        if (selete_aid.equals("04")) {
            return fileM1InfoEntity.getBlock_11().getDriverNum();
        } else if (selete_aid.equals("02")) {
            return file16NewCPUInfoEntity.getDriverNum();
        }
        return "000000";
    }

    public int getPayAllPrice() {
        return payAllPrice;
    }

    public void setPayAllPrice(int payAllPrice) {
        this.payAllPrice = payAllPrice;
    }

    @Override
    protected CardInfoEntity clone() throws CloneNotSupportedException {
        CardInfoEntity cardInfoEntity = null;
        try {
            cardInfoEntity = (CardInfoEntity) super.clone();   //浅复制
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cardInfoEntity;
    }

    public File1AJTBInfoEntity getFile1AJTBInfoEntity() {
        if (file1AJTBInfoEntity == null) {
            file1AJTBInfoEntity = new File1AJTBInfoEntity();
        }
        return file1AJTBInfoEntity;
    }

    public File1EJTBInfoEntity getFile1EJTBInfoEntity() {
        if (file1EJTBInfoEntity == null) {
            file1EJTBInfoEntity = new File1EJTBInfoEntity();
        }
        return file1EJTBInfoEntity;
    }
}
