package com.szxb.zibo.moudle.function.card;

import android.text.TextUtils;
import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.ThreadUtils;

import com.hao.lib.base.Rx.Rx;
import com.hao.lib.base.Rx.RxMessage;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.cmd.DoCmd;
import com.szxb.zibo.cmd.devCmd;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.line.CardPlan;
import com.szxb.zibo.config.zibo.line.FarePlan;
import com.szxb.zibo.config.zibo.line.FareRulePlan;
import com.szxb.zibo.config.zibo.line.PraseLine;
import com.szxb.zibo.moudle.function.card.CPU.File05NewCPUInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.File15NewCPUInfoEntity;
import com.szxb.zibo.moudle.function.card.CPU.FileCpuPay;
import com.szxb.zibo.moudle.function.card.M1.FileM1Pay;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static com.szxb.zibo.base.BusApp.CPU_CARD;
import static com.szxb.zibo.base.BusApp.M1_CARD;
import static com.szxb.zibo.base.BusApp.NEW_CPU_CARD;
import static java.lang.System.arraycopy;

public class PraseCard {
    public static   String lastCardUseCardno=null;

    //过期后能使用的卡片
    static List<String> spCard = new ArrayList<String>() {
        @Override
        public boolean contains(Object o) {
            for (int i = 0; i < size(); i++) {
                if (get(i).equals(o)) {
                    return true;
                }
            }
            return super.contains(o);
        }
    };

    static List<String> errCard = new ArrayList<String>() {
        @Override
        public boolean contains(Object o) {
            for (int i = 0; i < size(); i++) {
                if (get(i).equals(o)) {
                    return true;
                }
            }
            return super.contains(o);
        }
    };

    private static void initSpCard() {
        spCard.add("02");
    }

    private static void initErrCard() {
        errCard.add("2b");
        errCard.add("2c");
        errCard.add("2d");
        errCard.add("2e");
        errCard.add("2f");
        errCard.add("32");
        errCard.add("3a");
        errCard.add("3d");
        errCard.add("3e");
        errCard.add("3f");
        errCard.add("66");
        errCard.add("67");
        errCard.add("68");
        errCard.add("24");
        errCard.add("26");
        errCard.add("27");
        errCard.add("28");
        errCard.add("42");
        errCard.add("41");
        errCard.add("43");
        errCard.add("45");//多票区域数据写入异常
    }

    public static void praseCardDate(byte[] bytes) {
        try {
            initSpCard();
            initErrCard();
            price = 0;//初始化消费价格  防止受到前一次刷卡影响
            //卡解析
            CardInfoEntity cardInfoEntity = new CardInfoEntity();

//            MiLog.i("刷卡", FileUtils.bytesToHexString(bytes));

//            bytes = FileUtils.hexStringToBytes("0000002020010715412604002c26de0608000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000425500001300782317fae8c1f010303e82014070920200708000000002550000114070910000000000000000000020088000027100000000000000000000000009e16000061e9ffff9e16000009f609f69e16000061e9ffff9e16000009f609f6000607a50102d0000f01020018e718e7000607a50102d0000f01020018e718e7000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010406000101c20119042515442218e700152925500100490100040433ffffff");
//            解析卡 如果00 则表示解析成功
            if (!cardInfoEntity.putDate(bytes).equals("00")) {
                MiLog.i("刷卡", "卡解析失败");
                return;
            }
            lastCardUseCardno=cardInfoEntity.getVehicle_number();

            //判断卡片是否能够使用  稽查卡查询不到相应的规则无法在管理卡前判断
            if (!cardInfoEntity.cardType.equals("11") && !cardCanUse(cardInfoEntity)) {
                return;
            }

            if (cardInfoEntity.status.equals("00") && cardInfoEntity.isManageCard) {
                if (manageCardSetting(cardInfoEntity)) {
                    return;
                }
            }

            XdRecord xdRecord = checkMac(cardInfoEntity);
            if (xdRecord == null) {
                return;
            }

            if (cardInfoEntity.status.equals("00")) {
                packagePayCmd(cardInfoEntity, xdRecord == null ? new XdRecord() : xdRecord);
            }
        } catch (Exception e) {
            MiLog.i("错误", "卡解析 " + e.getMessage());
        }
    }

    //管理卡操作
    private static boolean manageCardSetting(CardInfoEntity cardInfoEntity) {
        switch (cardInfoEntity.cardType) {
            case "06":
                if (cardInfoEntity.childCardType.endsWith("11")) {//司机卡
                    return driveCardManage(cardInfoEntity);
                } else {
                    return false;
                }
//                else if (cardInfoEntity.childCardType.endsWith("21")) {//客服卡
//                    return driveCardManage(cardInfoEntity);
//                } else if (cardInfoEntity.childCardType.endsWith("10")) {//员工卡
//                    return driveCardManage(cardInfoEntity);
//                } else if (cardInfoEntity.childCardType.endsWith("01")) {//员工卡
//                    return driveCardManage(cardInfoEntity);
//                }
            case "10"://线路设置卡
                if (!BusApp.getPosManager().getDriverNo().equals("00000000")) {
                    BusApp.getPosManager().setDriverNo("00000000");
                    SoundPoolUtil.play(VoiceConfig.xiaban);
                }
                break;
            case "11"://数据采集卡
                if (!BusApp.getPosManager().getDriverNo().equals("00000000")) {
                    XdRecord xdRecord = new XdRecord();
                    xdRecord.setRecordVersion("0005");
                    xdRecord.setUseCardnum(cardInfoEntity.cardNo);
                    xdRecord.setTradeType("01");
                    xdRecord.setRecordSmallType("02");
                    xdRecord.setMainCardType(cardInfoEntity.realCardType);//TODO 主卡类型
                    xdRecord.setChildCardType(cardInfoEntity.realChildCardType);//TODO 子卡类型
                    xdRecord.setDirection(BusApp.getPosManager().getDirection());
                    xdRecord.setInCardStatus(cardInfoEntity.getTranseType());
                    setXdRecordOldInfo(cardInfoEntity, xdRecord);
                    xdRecord.setTradePSAM(BusApp.getPosManager().getM1psam());
                    RecordUpload.saveRecord(xdRecord, true);
                    BusApp.getPosManager().setDriverNo("00000000");
                    SoundPoolUtil.play(VoiceConfig.xiaban);
                }
                break;
            case "12"://签点卡
                break;
            case "13"://检测卡
                break;
            case "14"://充值员操作授权卡
                break;
            case "15"://程序下载授权卡
                break;
            case "16"://出租车数据采集卡
                break;
            case "17"://加油卡
                break;
            case "18"://稽查卡
                if (BusApp.getInstance().checkSetting()) {
                    return true;
                }
                return false;
//                XdRecord xdRecord = new XdRecord();
//                xdRecord.setRecordVersion("0001");
//                xdRecord.setUseCardnum(cardInfoEntity.cardNo);
//                xdRecord.setTradeType("01");
//                xdRecord.setMainCardType("98");//TODO 主卡类型
//                xdRecord.setDirection(BusApp.getPosManager().getDirection());
//                xdRecord.setInCardStatus(cardInfoEntity.getTranseType());
//                setXdRecordOldInfo(cardInfoEntity, xdRecord);
//                xdRecord.setTradePSAM(BusApp.getPosManager().getM1psam());
//                xdRecord.setChildCardType(cardInfoEntity.childCardType);//TODO 子卡类型
//                RecordUpload.saveRecord(xdRecord, true);
//                SoundPoolUtil.play(VoiceConfig.dang);
//                BusToast.showToast("稽查卡", true);
        }
        return true;
    }

    static int price = 0;

    //组装消费命令
    private static void packagePayCmd(final CardInfoEntity cardInfoEntity, final XdRecord xdRecord) throws Exception {
        //是否为黑名单
        boolean isBlack = DBManagerZB.checkedBlack(cardInfoEntity.cardNo);

        if (BusApp.getInstance().checkSetting()) {
            return;
        }

        int i = 0;
        final byte[] pay = new byte[256];

        pay[0] = FileUtils.int2byte(Integer.parseInt(cardInfoEntity.selete_aid))[0];
        i++;

        byte[] uid = FileUtils.hex2byte(cardInfoEntity.uid);
        arraycopy(uid, 0, pay, i, uid.length);
        i += uid.length;
        String transTime=DateUtil.getCurrentDate2();
        cardInfoEntity.setTranseTime(transTime);
        MiLog.i("刷卡", "刷卡交易时间：" + cardInfoEntity.getTranseTime());
        if (TextUtils.equals(cardInfoEntity.selete_aid, "02")) {
            if (isBlack || cardInfoEntity.cardNo.equals("2550000000000005")) {
                SoundPoolUtil.play(VoiceConfig.heimingdanka);
                BusToast.showToast("黑名单卡", false);
                Rx.getInstance().sendMessage("lockNewCpu");
                return;
            }

            price = PraseLine.getCardPayPrice(cardInfoEntity, cardInfoEntity.cardType, cardInfoEntity.childCardType);
            if (price == -1) {
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                BusToast.showToast("暂不支持此卡上车", false);
                return;
            } else if (price == -2) {
                SoundPoolUtil.play(VoiceConfig.yuebuzu);
                BusToast.showToast("余额不足\n余额" + FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", false);
                return;
            } else if (price < 0) {
                SoundPoolUtil.play(VoiceConfig.cuowu);
                BusToast.showToast("获取票价错误", false);
                return;
            }


            if (cardInfoEntity.getBalance() < price) {
                BusToast.showToast("余额不足\n余额" + FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", false);
                SoundPoolUtil.play(VoiceConfig.yuebuzu);
                return;
            }

            xdRecord.setTradeTime(transTime);
            xdRecord.setLastTradeCount(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_number_18());
            FileCpuPay fileCpuPay = new FileCpuPay();
            fileCpuPay.setSelete_aid(cardInfoEntity.selete_aid);
            fileCpuPay.setUid(cardInfoEntity.uid);
            fileCpuPay.setTransaction_amount(price);
            fileCpuPay.setTransaction_time(cardInfoEntity.getTranseTime());
            fileCpuPay.setMark_update_1C("01");
            cardInfoEntity.getnewCPUMorePriceInfo().setBoarding_time(cardInfoEntity.transeTime);
            fileCpuPay.setFile17NewCPUInfoEntity(cardInfoEntity.getnewCPUMorePriceInfo());


//            // 用于测试老年卡补票失败 的情况
//            cardInfoEntity.getnewCPUMorePriceInfo().setFull_fare(Integer.parseInt("ffff",16));
//            fileCpuPay.setFile17NewCPUInfoEntity(cardInfoEntity.getnewCPUMorePriceInfo());

            byte[] fileCpuPaybyte = FileUtils.hex2byte(fileCpuPay.toNewCpuString());
            xdRecord.setPayCommand(FileUtils.bytesToHexString(fileCpuPaybyte));
            arraycopy(fileCpuPaybyte, 0, pay, 0, fileCpuPaybyte.length);

        } else if (TextUtils.equals(cardInfoEntity.selete_aid, "03")) {//CPU卡
            if (isBlack) {
                SoundPoolUtil.play(VoiceConfig.heimingdanka);
                BusToast.showToast("黑名单卡", false);
                return;
            }

            price = PraseLine.getCardPayPrice(cardInfoEntity, "65", "01");
            if (price == -1) {
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                BusToast.showToast("暂不支持此卡上车", false);
                return;
            } else if (price == -2) {
                SoundPoolUtil.play(VoiceConfig.yuebuzu);
                BusToast.showToast("余额不足\n余额:" + FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", false);
                return;
            } else if (price < 0) {
                SoundPoolUtil.play(VoiceConfig.cuowu);
                BusToast.showToast("获取票价错误", false);
                return;
            }


            if (cardInfoEntity.getBalance() < price) {
                BusToast.showToast("余额不足\n余额:" + FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", false);
                SoundPoolUtil.play(VoiceConfig.yuebuzu);
                return;
            }


            xdRecord.setLastTradeCount(cardInfoEntity.file18LocalInfoEntity.transaction_number_18);
            if (xdRecord.getPayCommand() == null || xdRecord.getPayCommand().equals("")) {
                xdRecord.setTradeTime(transTime);
                FileCpuPay fileCpuPay = new FileCpuPay();
                fileCpuPay.setSelete_aid(cardInfoEntity.selete_aid);
                fileCpuPay.setUid(cardInfoEntity.uid);
                fileCpuPay.setTransaction_amount(price);
                fileCpuPay.setTransaction_time(cardInfoEntity.getTranseTime());
                if (cardInfoEntity.transeType == 3) {//如果是补票交易 需要对多票区域进行更新
                    fileCpuPay.setMark_update_1C("01");
                } else {
                    fileCpuPay.setMark_update_1C(BusApp.getPosManager().getLineType().endsWith("P") ? "01" : "00");
                }
                cardInfoEntity.getMorePriceInfo().setBoarding_time(cardInfoEntity.transeTime);
                fileCpuPay.setFile1CLocalInfoEntity(cardInfoEntity.getMorePriceInfo());


//                // 用于测试老年卡补票失败 的情况
//                cardInfoEntity.getMorePriceInfo().setFull_fare(Integer.parseInt("ffff",16));
//                fileCpuPay.setFile1CLocalInfoEntity(cardInfoEntity.getMorePriceInfo());
                byte[] fileCpuPaybyte = FileUtils.hex2byte(fileCpuPay.toOldCpuString());
                xdRecord.setPayCommand(FileUtils.bytesToHexString(fileCpuPaybyte));
                arraycopy(fileCpuPaybyte, 0, pay, 0, fileCpuPaybyte.length);
            } else {
                MiLog.i("刷卡", "03  校验  进行数据恢复");
                byte[] fileCpuPaybyte = FileUtils.hex2byte(xdRecord.getPayCommand());
                byte[] mark_update = new byte[]{0x00};
                arraycopy(fileCpuPaybyte, 16, mark_update, 0, mark_update.length);
                xdRecord.setPayCommand(FileUtils.bytesToHexString(fileCpuPaybyte));
                arraycopy(fileCpuPaybyte, 0, pay, 0, fileCpuPaybyte.length);
            }
        } else if (TextUtils.equals(cardInfoEntity.selete_aid, "04")) {//M1卡
            price = PraseLine.getCardPayPrice(cardInfoEntity, cardInfoEntity.cardType, cardInfoEntity.childCardType);
            if (price == -1) {
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                BusToast.showToast("暂不支持此卡上车", false);
                return;
            } else if (price == -2) {
                SoundPoolUtil.play(VoiceConfig.yuebuzu);
                BusToast.showToast("余额不足\n余额" + FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", false);
                return;
            } else if (price < 0) {
                SoundPoolUtil.play(VoiceConfig.cuowu);
                BusToast.showToast("获取票价错误", false);
                return;
            }

            if (cardInfoEntity.getBalance() < price) {
                BusToast.showToast("余额不足\n余额" + FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", false);
                SoundPoolUtil.play(VoiceConfig.yuebuzu);
                return;
            }

            MiLog.i("刷卡", "卡类型：" + cardInfoEntity.cardType + "      " + cardInfoEntity.childCardType);
            FileM1Pay fileM1Pay = new FileM1Pay();

            if (xdRecord == null || xdRecord.getLastTradeCount() == null || xdRecord.getLastTradeCount().equals("0000")) {
                MiLog.i("刷卡", "无失败记录,直接更新24");
                fileM1Pay.setMark_update_24("00");
            } else {//有错误记录需要验证；
                String number = cardInfoEntity.fileM1InfoEntity.getBlock_18().transaction_number;
                MiLog.i("刷卡", "开始验证:" + number);
                long cardTrade = Long.parseLong(number, 16);
                long laseCardTrade = Long.parseLong(FileUtils.getSHByte(xdRecord.getLastTradeCount()), 16);
                MiLog.i("刷卡", "上次刷卡前卡交易序号：" + laseCardTrade + "      本次刷卡前交易序号：" + cardTrade);
                if (cardTrade - laseCardTrade == 1) {
                    xdRecord.setCardTradeCount(FileUtils.getSHByte(xdRecord.getLastTradeCount()));
                    fileM1Pay.setMark_update_24("01");
                } else {
                    fileM1Pay.setMark_update_24("00");
                }
            }


            xdRecord.setLastTradeCount(cardInfoEntity.fileM1InfoEntity.getBlock_18().transaction_number);
            if (xdRecord.getPayCommand() == null || xdRecord.getPayCommand().equals("")) {
                xdRecord.setTradeTime(transTime);
                fileM1Pay.setSelete_aid(cardInfoEntity.selete_aid);
                fileM1Pay.setUid(cardInfoEntity.uid);
                cardInfoEntity.getMorePriceInfo().setBoarding_time(cardInfoEntity.transeTime);
                fileM1Pay.setFile1CLocalInfoEntity(cardInfoEntity.getMorePriceInfo());
                if (cardInfoEntity.transeType == 3) {//如果是补票交易 需要对多票区域进行更新
                    fileM1Pay.setMark_update_1C("01");
                } else {
                    fileM1Pay.setMark_update_1C(BusApp.getPosManager().getLineType().endsWith("P") ? "01" : "00");
                }

                if (cardInfoEntity.getBalance() < price) {
                    BusToast.showToast("余额不足\n余额" + FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", false);
                    SoundPoolUtil.play(VoiceConfig.yuebuzu);
                    return;
                }
                fileM1Pay.setTransaction_time(cardInfoEntity.getTranseTime());
                if (!isBlack) {
                    fileM1Pay.setBlacklist_type("00"); //04 黑名单  00 其他
                    if (cardInfoEntity.getTranseType() == 1) {
                        fileM1Pay.setMark_update_9("01");
                    } else {
                        if (price == 0) {
                            fileM1Pay.setMark_update_9("01");
                        } else {
                            fileM1Pay.setMark_update_9("00");
                            fileM1Pay.setTransaction_type("06");
                            fileM1Pay.setTransaction_amount(price);
                        }
                    }
                } else {
                    cardInfoEntity.fileM1InfoEntity.getBlock_18().setBlacklist_type("04");
                    fileM1Pay.setBlacklist_type("04");
                    fileM1Pay.setMark_update_9("01");
                }

//                fileM1Pay.setMark_update_24("00");
//                用于测试老年卡补票失败 的情况
//                cardInfoEntity.getMorePriceInfo().setFull_fare(Integer.parseInt("ffff",16));
//                fileM1Pay.setFile1CLocalInfoEntity(cardInfoEntity.getMorePriceInfo());

                byte[] fileM1Paybyte = FileUtils.hex2byte(fileM1Pay.toString());
                arraycopy(fileM1Paybyte, 0, pay, i, fileM1Paybyte.length);
                xdRecord.setPayCommand(FileUtils.bytesToHexString(pay));
            } else {
                MiLog.i("刷卡", "04  校验  进行数据恢复 不更新钱包");
                byte[] fileM1Paybyte = FileUtils.hex2byte(xdRecord.getPayCommand());
                fileM1Pay.praseDate(fileM1Paybyte);
                fileM1Pay.setMark_update_9("01");
                fileM1Pay.setMark_update_24("01");
                cardInfoEntity.transeType = xdRecord.getVoiceType();
                price = fileM1Pay.getTransaction_amount();
                fileM1Paybyte = FileUtils.hex2byte(fileM1Pay.toString());
                arraycopy(fileM1Paybyte, 0, pay, i, fileM1Paybyte.length);
                xdRecord.setPayCommand(FileUtils.bytesToHexString(pay));
            }
        }

        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                devCmd de = DoCmd.getPayRecord(pay);
                MiLog.i("刷卡", "刷卡消费返回" + FileUtils.bytesToHexString(de.getDataBuf()));
                try {
                    PraseCard.payResponse(de, cardInfoEntity, price, xdRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //消费返回数据
    public static void payResponse(devCmd devCmd, CardInfoEntity cardInfoEntity, int price, XdRecord newXdRecord) throws Exception {
        try {
            byte[] resultDate = devCmd.getDataBuf();

//            resultDate = FileUtils.hexStringToBytes("0000000004010f060006000b08020018e718e70004010f060006000b08020018e718e70000000000000000000000000000000078650000879affff7865000009f609f678650000879affff7865000009f609f6726500008d9affff7265000009f609f695c00bfc82b98b3b");

            byte[] payResult = new byte[devCmd.getnRecvLen()];
            arraycopy(resultDate, 0, payResult, 0, payResult.length);
            PraseConsumCard praseConsumCard = new PraseConsumCard(payResult, cardInfoEntity);
            if (newXdRecord == null) {
                newXdRecord = new XdRecord();
            }
            BusApp.oldCardTag = cardInfoEntity.uid;

            if (cardInfoEntity.selete_aid.equals("04")) {
                if (cardInfoEntity.fileM1InfoEntity.getBlock_18().blacklist_type.equals("04")) {
                    SoundPoolUtil.play(VoiceConfig.heimingdanka);
                    BusToast.showToast("黑名单卡", false);
                    return;
                }
            }

            if (errCard.contains(praseConsumCard.status)) {
                XdRecord xdRecord = DBManagerZB.checkRecordNearNow(cardInfoEntity.cardNo);
                if (xdRecord != null) {
                    newXdRecord.setStatus(praseConsumCard.status);
                }
                newXdRecord.setPayType("DD");
                initConsumCardToRecord(cardInfoEntity, praseConsumCard, newXdRecord);
                saveRecord(cardInfoEntity, price, newXdRecord, false);
                MiLog.i("刷卡", "错误记录保存：" + praseConsumCard.status);
                BusToast.showToast("刷卡失败【" + praseConsumCard.getStatus() + "】", false);
                showErr(praseConsumCard.getSw());
            } else if (!TextUtils.equals(praseConsumCard.getStatus(), "00")) {
                BusToast.showToast("刷卡失败【" + praseConsumCard.getStatus() + "】", false);
                showErr(praseConsumCard.getSw());
            } else {
                initConsumCardToRecord(cardInfoEntity, praseConsumCard, newXdRecord);
                newXdRecord.setPayType("FD");
                saveRecord(cardInfoEntity, price, newXdRecord);
                showVoice(cardInfoEntity, newXdRecord);
                BusToast.showToast("\n本次扣款：" + FileUtils.fen2Yuan(price) + "元\n余额：" +
                        FileUtils.fen2Yuan(praseConsumCard.getBalance()) + "元", true);
            }
            MiLog.i("刷卡", "完成");
        } catch (Exception e) {
            MiLog.i("刷卡", "消费错误" + e.getMessage());
            BusToast.showToast("刷卡失败【消费错误】", false);
            SoundPoolUtil.play(VoiceConfig.qingchongshua);
        }
    }

    //刷卡错误语音
    private static void showErr(String sw) {
        if (sw.equals("9401")) {
            SoundPoolUtil.play(VoiceConfig.yuebuzu);
        } else {
            SoundPoolUtil.play(VoiceConfig.qingchongshua);
        }
    }

    //是否是上下班卡 true 表示为正常上下班 false 表示为其他卡
    private static boolean driveCardManage(CardInfoEntity cardInfoEntity) {
        String driveNum = BusApp.getPosManager().getDriverNo();

        XdRecord xdRecord = new XdRecord();
        String tradeType = "01";
        if (cardInfoEntity.selete_aid.equals("04")) {
            tradeType = "01";
        } else if (cardInfoEntity.selete_aid.equals("03")) {
            tradeType = "03";
        } else if (cardInfoEntity.selete_aid.equals("02")) {
            tradeType = "02";
        }
        xdRecord.setRecordVersion("0005");
        xdRecord.setUseCardnum(cardInfoEntity.cardNo);
        xdRecord.setTradeType(tradeType);

        xdRecord.setMainCardType(cardInfoEntity.cardType);//TODO 主卡类型
        xdRecord.setChildCardType(cardInfoEntity.childCardType);//TODO 子卡类型
        xdRecord.setDirection(BusApp.getPosManager().getDirection());
        xdRecord.setInCardStatus(cardInfoEntity.getTranseType());

        setXdRecordOldInfo(cardInfoEntity, xdRecord);
        xdRecord.setTradePSAM(BusApp.getPosManager().getM1psam());

        String driver = cardInfoEntity.getDriverNum();
        if (driveNum == null || driveNum.equals("") || driveNum.equals("00000000")) {
            BusApp.getPosManager().setDriverNo(driver);
            SoundPoolUtil.play(VoiceConfig.shangban);
            xdRecord.setRecordSmallType("01");
            RecordUpload.saveRecord(xdRecord, true);
        } else {
            if (driveNum.equals(driver)) {
                xdRecord.setRecordSmallType("02");
                RecordUpload.saveRecord(xdRecord, true);
                BusApp.getPosManager().setDriverNo("00000000");
                SoundPoolUtil.play(VoiceConfig.xiaban);
            } else {
                return false;
            }
        }
        return true;
    }

    public static void showVoice(CardInfoEntity cardInfoEntity, XdRecord xdRecord) {
        FareRulePlan fareRulePlan = null;
        try {
            FarePlan farePlan = DBManagerZB.checkFarePlan();
            if (farePlan != null) {
                CardPlan cardPlan = DBManagerZB.checkCardPlan(farePlan.getCardCaseNUm(), cardInfoEntity.cardType, cardInfoEntity.childCardType);
                fareRulePlan = DBManagerZB.checkPricePayRule(cardPlan.getCardPayRuleNum());
            }
        } catch (Exception e) {

        }



        if (BusApp.getPosManager().getLineType().equals("P")) {
            if (xdRecord.getInCardStatus().equals("01")) {//上车
                SoundPoolUtil.play(VoiceConfig.qingshangche);
                return;
            } else if (xdRecord.getInCardStatus().equals("02")) {//下车
                if (fareRulePlan != null && Integer.parseInt(fareRulePlan.getFareRulePrice()) != 0) {
                    if (cardInfoEntity.getBalance() < 1000) {
                        SoundPoolUtil.play(VoiceConfig.qingxiacheqingchongzhi);
                        return;
                    }
                }
                SoundPoolUtil.play(VoiceConfig.qingxiache);
                return;
            } else if (xdRecord.getInCardStatus().equals("03")) {//补票
                SoundPoolUtil.play(VoiceConfig.bupiao);
                return;
            } else if (xdRecord.getInCardStatus().equals("00")) {//单票

            } else {//未知¬
                SoundPoolUtil.play(VoiceConfig.qingchongshua);
                return;
            }
        }
//        if (cardInfoEntity.getTranseType() == 1) {
//            SoundPoolUtil.play(VoiceConfig.qingshangche);
//            return;
//        } else if (cardInfoEntity.getTranseType() == 2) {
//            SoundPoolUtil.play(VoiceConfig.qingxiache);
//            return;
//        } else if (cardInfoEntity.getTranseType() == 3) {
//            SoundPoolUtil.play(VoiceConfig.bupiao);
//            return;
//        } else if (cardInfoEntity.getTranseType() == 4) {
//            SoundPoolUtil.play(VoiceConfig.qingchongshua);
//            return;
//        }


        if (fareRulePlan != null && Integer.parseInt(fareRulePlan.getFareRulePrice()) != 0) {
            if (cardInfoEntity.getBalance() < 1000) {
                SoundPoolUtil.play(VoiceConfig.qingchongzhi);
                return;
            }
        }


        switch (cardInfoEntity.cardType) {
            case "01"://普通卡
            case "05"://纪念卡(拥军卡)
                if (cardInfoEntity.childCardType.endsWith("01")) {
                    SoundPoolUtil.play(VoiceConfig.wuchangxianxueka);
                } else {
                    SoundPoolUtil.play(VoiceConfig.dang);
                }
                break;
            case "18"://稽查卡
                SoundPoolUtil.play(VoiceConfig.dang);
                break;
            case "02"://学生卡
                SoundPoolUtil.play(VoiceConfig.xueshengka);
                break;
            case "03"://老年卡
                if (cardInfoEntity.childCardType.endsWith("01")) {
                    SoundPoolUtil.play(VoiceConfig.aixinka);
                } else {
                    SoundPoolUtil.play(VoiceConfig.laonianka);
                }
                break;
            case "04"://免费卡(荣军卡)
                SoundPoolUtil.play(VoiceConfig.rongjunka);
                break;
            case "06":
//                    SoundPoolUtil.play(VoiceConfig.yuangongka);
                SoundPoolUtil.play(VoiceConfig.dangdang);
                break;
            case "10"://线路卡
                SoundPoolUtil.play(VoiceConfig.xianluka);
                break;
            case "11"://采集卡     强制下班用的
                SoundPoolUtil.play(VoiceConfig.caijika);
                break;
            case "12"://签点卡
                SoundPoolUtil.play(VoiceConfig.qiandianka);
                break;
            case "13"://检测卡
                SoundPoolUtil.play(VoiceConfig.jianceka);
                break;
            case "14"://充值员授权操作卡
                SoundPoolUtil.play(VoiceConfig.chongzhiyuanshouquancaozuoka);
                break;
            case "15"://程序下载授权卡
                SoundPoolUtil.play(VoiceConfig.chengxuxiazaishouquanka);
                break;
            case "16"://出租车数据采集卡
                SoundPoolUtil.play(VoiceConfig.chuzucheshujucaijika);
                break;
            case "17"://加油卡
                SoundPoolUtil.play(VoiceConfig.jiayouka);
                break;
            case "65"://加油卡
                if (cardInfoEntity.childCardType.endsWith("01")) {
                    SoundPoolUtil.play(VoiceConfig.dang);
                } else {
                    SoundPoolUtil.play(VoiceConfig.cuowu);
                }
                break;
            default:
                SoundPoolUtil.play(VoiceConfig.dang);
                break;
        }
    }

    public static void initConsumCardToRecord(CardInfoEntity cardInfoEntity, PraseConsumCard praseConsumCard, XdRecord xdRecord) {
        String select_aid = cardInfoEntity.selete_aid;
        xdRecord.setStatus(praseConsumCard.getStatus());
        xdRecord.setTradeDiscount(praseConsumCard.balance);
        if (select_aid.equals(NEW_CPU_CARD)) {
            if (xdRecord.getCardTradeCount().equals("0000")) {
                MiLog.i("刷卡", "新增数据" + "         交易后卡余额：" + praseConsumCard.balance);
                xdRecord.setCardTradeCount(cardInfoEntity.getFile18NewCPUInfoEntity().getTransaction_number_18());
            } else {
                MiLog.i("刷卡", "补全数据:" + xdRecord.getStatus() + "   记录标示：" + xdRecord.getRecordTag() + "         交易后卡余额：" + praseConsumCard.balance);
            }
            xdRecord.setCardTradeTAC(praseConsumCard.fileCpuPayResult.getTAC());
            xdRecord.setSamTradeCount(praseConsumCard.fileCpuPayResult.getSam_transaction_number());
        } else if (select_aid.equals(CPU_CARD)) {
            if (xdRecord.getCardTradeCount().equals("0000")) {
                MiLog.i("刷卡", "新增数据" + "         交易后卡余额：" + praseConsumCard.balance);
                xdRecord.setCardTradeCount(cardInfoEntity.file18LocalInfoEntity.transaction_number_18);
            } else {
                MiLog.i("刷卡", "补全数据:" + xdRecord.getStatus() + "   记录标示：" + xdRecord.getRecordTag() + "         交易后卡余额：" + praseConsumCard.balance);
            }
            xdRecord.setCardTradeTAC(praseConsumCard.fileCpuPayResult.getTAC());
            xdRecord.setSamTradeCount(praseConsumCard.fileCpuPayResult.getSam_transaction_number());
        } else if (select_aid.equals(M1_CARD)) {
            if (xdRecord.getCardTradeCount().equals("0000")) {
                MiLog.i("刷卡", "新增数据" + "         交易后卡余额：" + praseConsumCard.balance);
                xdRecord.setCardTradeCount(cardInfoEntity.fileM1InfoEntity.getBlock_18().transaction_number);//TODO M1卡交易序号
            } else {
                MiLog.i("刷卡", "补全数据:" + xdRecord.getStatus() + "   记录标示：" + xdRecord.getRecordTag() + "         交易后卡余额：" + praseConsumCard.balance);
            }
            xdRecord.setCardTradeTAC(praseConsumCard.fileMIPayResult.tac);//TODO MI卡TAC
//          xdRecord.setUseCardnum(praseConsumCard.fileMIPayResult.c());//TODO 应用卡号
        }
        MiLog.i("刷卡", "交易完成时的卡交易序号：" + xdRecord.getCardTradeCount());
    }

    //直接保存数据并上送
    public static void saveRecord(CardInfoEntity cardInfoEntity, long price, XdRecord xdRecord) {
        saveRecord(cardInfoEntity, price, xdRecord, true);
    }

    /**
     * @param cardInfoEntity
     * @param price
     * @param xdRecord
     * @param isneedUp       是否直接进行上送处理
     */
    public static void saveRecord(CardInfoEntity cardInfoEntity, long price, XdRecord xdRecord, boolean isneedUp) {
        String tradeType = "01";
        if (cardInfoEntity.selete_aid.equals("04")) {
            tradeType = "01";
        } else if (cardInfoEntity.selete_aid.equals("03")) {
            tradeType = "03";
        }
        if (cardInfoEntity.getPayAllPrice() != 0) {
            xdRecord.setTradePayNum(cardInfoEntity.getPayAllPrice());
        }
        xdRecord.setVoiceType(cardInfoEntity.transeType);
        xdRecord.setBalance(cardInfoEntity.getBalance());
        xdRecord.setRecordVersion("0001");
        xdRecord.setTradeType(tradeType);
        xdRecord.setTradePay(price);
        xdRecord.setTradeTime(xdRecord.getTradeTime());
        xdRecord.setTradePSAM(cardInfoEntity.selete_aid.equals("03") ? BusApp.getPosManager().getCpupsam() : BusApp.getPosManager().getMainPSAM());//TODO 当前交易的PSAM
        if (xdRecord.getInCardStatus().equals("00")) {
            xdRecord.setInCardStatus(cardInfoEntity.getTranseType());
        }
        xdRecord.setDirection(BusApp.getPosManager().getDirection());
        xdRecord.setUseCardnum(cardInfoEntity.cardNo);//TODO 应用卡号
        xdRecord.setMainCardType(cardInfoEntity.cardType);//TODO 主卡类型
        xdRecord.setChildCardType(cardInfoEntity.childCardType);//TODO 子卡类型
        if (cardInfoEntity.selete_aid.equals(NEW_CPU_CARD)) {
//            xdRecord.setMainCardType(cardInfoEntity.realCardType);//TODO 主卡类型
//            xdRecord.setChildCardType(cardInfoEntity.realChildCardType);//TODO 子卡类型
            xdRecord.setCreatCardMechanism(cardInfoEntity.file15NewCPUInfoEntity.getIssuer_code_15());
            xdRecord.setBeforTradePosSn(cardInfoEntity.file17NewCPUInfoEntity.getVehicle_number());
            xdRecord.setBeforTradeType(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_type());
            xdRecord.setBeforTradeTime(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_time());
            xdRecord.setBeforTradePrice(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_amount());
            xdRecord.setTradePSAM(BusApp.getPosManager().getM1psam());
        } else if (cardInfoEntity.selete_aid.equals(CPU_CARD)) {
            xdRecord.setCreatCardMechanism(cardInfoEntity.file15LocalInfoEntity.getCard_issuer());
            xdRecord.setMainCardType("65");//TODO 主卡类型
            xdRecord.setChildCardType("01");//TODO 子卡类型
            xdRecord.setBeforTradePosSn(cardInfoEntity.file1CLocalInfoEntity.getVehicle_number());
            xdRecord.setBeforTradeType(cardInfoEntity.file18LocalInfoEntity.transaction_type);
            xdRecord.setBeforTradeTime(cardInfoEntity.file18LocalInfoEntity.transaction_time);
            xdRecord.setBeforTradePrice(cardInfoEntity.file18LocalInfoEntity.transaction_amount);
            xdRecord.setTradePSAM(BusApp.getPosManager().getCpupsam());
        } else if (cardInfoEntity.selete_aid.equals(M1_CARD)) {
//          xdRecord.setUseCardnum(praseConsumCard.fileMIPayResult.c());//TODO 应用卡号
//          xdRecord.setSamTradeCount(praseConsumCard.fileCpuPayResult.getTAC());//TODO sam卡交易序号
//          xdRecord.setCreatCardMechanism(cardInfoEntity.getCurrentDate2());//TODO 发卡机构代码
//            xdRecord.setMainCardType(cardInfoEntity.realCardType);//TODO 主卡类型
//            xdRecord.setChildCardType(cardInfoEntity.realChildCardType);//TODO 子卡类型
            xdRecord.setBeforTradePosSn(cardInfoEntity.fileM1InfoEntity.getBlock_1C1D().getVehicle_number());
            xdRecord.setBeforTradeType(cardInfoEntity.fileM1InfoEntity.getBlock_18().transaction_type);
            xdRecord.setBeforTradeTime(cardInfoEntity.fileM1InfoEntity.getBlock_1C1D().getBoarding_time());
            xdRecord.setBeforTradePrice(cardInfoEntity.fileM1InfoEntity.getBlock_18().transaction_money);
            xdRecord.setTradePSAM(BusApp.getPosManager().getM1psam());
        }

        if(cardInfoEntity.getTranseType()==3){
            if(lastCardUseCardno!=null) {
                xdRecord.setCarNum(lastCardUseCardno);
            }else{
                xdRecord.setCarNum(BusApp.getPosManager().getBusNo());
            }
        }else{
            xdRecord.setCarNum(BusApp.getPosManager().getBusNo());
        }

        RecordUpload.saveRecord(xdRecord, isneedUp, false);
    }

    //判断此卡是否能够使用
    public static boolean cardCanUse(CardInfoEntity cardInfoEntity) {
        FarePlan farePlan = DBManagerZB.checkFarePlan();
        if (farePlan != null) {
            CardPlan cardPlan = DBManagerZB.checkCardPlan(farePlan.getCardCaseNUm(), cardInfoEntity.cardType, cardInfoEntity.childCardType);
            if (cardPlan != null) {
                int limitTime = Integer.parseInt(cardPlan.getUseInterval());
                XdRecord xdRecord = DBManagerZB.checkXdRecordByCardNo(cardInfoEntity.cardNo);
                if (cardInfoEntity.isExpire) {
                    MiLog.i("刷卡", "卡已过期");
                    if (cardPlan.getNeedCheckEndTime().equals("1")) {
                        if (spCard.contains(cardInfoEntity.cardType)) {
                            MiLog.i("刷卡", "特殊卡片过期   卡类型：" + cardInfoEntity.cardType);
                            cardInfoEntity.cardType = "01";
                            cardInfoEntity.childCardType = "00";
                            cardPlan = DBManagerZB.checkCardPlan(farePlan.getCardCaseNUm(), cardInfoEntity.cardType, cardInfoEntity.childCardType);
                            limitTime = Integer.parseInt(cardPlan.getUseInterval());
                        } else {
                            BusToast.showToast("请年审", false);
                            SoundPoolUtil.play(VoiceConfig.qingnianshen);
                            return false;
                        }
                    }
                }

                if (cardInfoEntity.cardType.equals("06") && (cardInfoEntity.getDriverNum().equals(BusApp.getPosManager().getDriverNo()) || BusApp.getPosManager().getDriverNo().equals("00000000"))) {
                    return true;
                } else {
                    if (xdRecord != null) {
                        if (System.currentTimeMillis() - xdRecord.getCreatTime() < 2 * 1000) {
                            MiLog.i("刷卡","刷卡限制  重复刷卡限制：2s内");
                            return false;
                        } else if (System.currentTimeMillis() - xdRecord.getCreatTime() >= 2 * 1000 && System.currentTimeMillis() - xdRecord.getCreatTime() < limitTime * 1000) {
                            MiLog.i("刷卡", "刷卡限制  重复刷卡限制：" + limitTime + "    " + xdRecord.getPayType());
                            SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                            BusToast.showToast("重复刷卡", false);
                            return false;
                        }
                    }
                }
            } else {
                SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
                BusToast.showToast("暂不支持此种方式乘车", false);
                return false;
            }
            return true;
        } else {
            SoundPoolUtil.play(VoiceConfig.zanshibunengshiyong);
            BusToast.showToast("暂不支持此种方式乘车", false);
            return false;
        }
    }

    //设置记录中的上一次刷卡信息
    private static void setXdRecordOldInfo(CardInfoEntity cardInfoEntity, XdRecord xdRecord) {
        if (cardInfoEntity.selete_aid.equals("04")) {
            xdRecord.setBeforTradePosSn(cardInfoEntity.fileM1InfoEntity.getBlock_1C1D().getVehicle_number());
            xdRecord.setBeforTradeType(cardInfoEntity.fileM1InfoEntity.getBlock_18().transaction_type);
            xdRecord.setBeforTradeTime(cardInfoEntity.fileM1InfoEntity.getBlock_1C1D().getBoarding_time());
            xdRecord.setBeforTradePrice(cardInfoEntity.fileM1InfoEntity.getBlock_18().transaction_money);
        } else if (cardInfoEntity.selete_aid.equals("02")) {
            xdRecord.setBeforTradePosSn(cardInfoEntity.getnewCPUMorePriceInfo().getVehicle_number());
            xdRecord.setBeforTradeType(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_type());
            xdRecord.setBeforTradeTime(cardInfoEntity.getnewCPUMorePriceInfo().getBoarding_time());
            xdRecord.setBeforTradePrice(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_amount());
        }
    }

    private static XdRecord checkMac(CardInfoEntity cardInfoEntity) {
        XdRecord xdRecord = DBManagerZB.checkRecordNearNow(cardInfoEntity.cardNo);
        XdRecord newxdRecord = new XdRecord();
        //可通过余额和交易时间来判定卡片是否交易成功
        //CPU 18文件交易时间  M1
        if (xdRecord != null) {
            newxdRecord.setCardTradeCount(FileUtils.getSHByte(xdRecord.getCardTradeCount()));
            newxdRecord.setLastTradeCount(FileUtils.getSHByte(xdRecord.getLastTradeCount()));
            newxdRecord.setRecordTag(xdRecord.getRecordTag());
            newxdRecord.setTradeNum(xdRecord.getTradeNum());
            newxdRecord.setTradeTime(xdRecord.getTradeTime());
            cardInfoEntity.setTranseType(xdRecord.getVoiceType());
            int payPrice = Integer.parseInt(FileUtils.getSHByte(xdRecord.getTradePay()), 16);
            MiLog.i("刷卡", "校验mac  有错误记录  记录创建时间：" + xdRecord.getTradeTime() + "         记录标示：" + xdRecord.getRecordTag() + "   错误码：" + xdRecord.getStatus()
                    + "\n" + "记录中的上次余额：" + xdRecord.getBalance() + "   卡中的当前余额：" + cardInfoEntity.getBalance());
            String tradeTime = xdRecord.getTradeTime();
            //如果产生0元消费 或消费失败
            if (xdRecord.getBalance() == cardInfoEntity.getBalance()) {
                if (payPrice == 0) {
                    MiLog.i("刷卡", "校验   校验mac 03  0元消费");
                    if (cardInfoEntity.selete_aid.equals("03")) {
                        String cardTime = cardInfoEntity.file18LocalInfoEntity.transaction_time;//单票消费时间
                        String cardMoreTime = cardInfoEntity.file1CLocalInfoEntity.getBoarding_time();//多票消费时间
                        if (cardTime.equals(tradeTime)) {//表示进行了免费交易
                            MiLog.i("刷卡", "校验   校验mac 03  开始校验 发送校验命令");
                            Rx.getInstance().sendMessage("checkMac", new Object[]{cardInfoEntity, xdRecord});//发送命令校准记录
                            return null;
                        } else {//交易失败
                            MiLog.i("刷卡", "校验mac 03  交易失败");
                            return newxdRecord;
                        }
                    } else if (cardInfoEntity.selete_aid.equals("02")) {
                        String cardTime = cardInfoEntity.file18NewCPUInfoEntity.getTransaction_time();
                        if (cardTime.equals(tradeTime)) {//表示进行了免费交易
                            MiLog.i("刷卡", "校验mac  02 开始校验  发送校验命令");
                            Rx.getInstance().sendMessage("checkMac", new Object[]{cardInfoEntity, xdRecord});//发送命令校准记录
                            return null;
                        } else {//交易失败
                            MiLog.i("刷卡", "校验mac 02  交易失败");
                            return newxdRecord;
                        }
                    } else if (cardInfoEntity.selete_aid.equals("04")) {
                        if (BusApp.getPosManager().getLineType().endsWith("P")) {
                            String cardTime = cardInfoEntity.fileM1InfoEntity.getBlock_1C1D().getBoarding_time();
                            MiLog.i("刷卡", "校验  多票时间：" + cardTime + "    记录时间：" + tradeTime);
                            if (cardTime.equals(tradeTime)) {
                                MiLog.i("刷卡", "校验  无扣费数据  多票区域数据写入成功 完成");
                                showVoice(cardInfoEntity, xdRecord);
                                xdRecord.setStatus("00");
                                xdRecord.setPayType("FD");
                                xdRecord.setTradeDiscount(cardInfoEntity.getBalance());
                                MiLog.i("刷卡", "无扣费数据  多票区域数据写入成功 完成");
                                saveRecord(cardInfoEntity, payPrice, xdRecord);
                                BusToast.showToast("\n校验扣款：" + FileUtils.fen2Yuan(payPrice) + "元\n余额：" +
                                        FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", true);
                                return null;
                            } else {
                                MiLog.i("刷卡", "校验  无扣费数据  多票区域数据未写入成功 数据重置");
                            }
                        } else {
                            MiLog.i("刷卡", "校验   校验mac 03  有价格消费 失败");
                            return newxdRecord;
                        }
                    }
                } else {
                    MiLog.i("刷卡", "校验  校验mac  有票价但未扣费 交易失败" + "   交易序号：" + newxdRecord.getCardTradeCount());
                    return newxdRecord;
                }
            } else {
                if (cardInfoEntity.selete_aid.equals("04")) {
                    if (BusApp.getPosManager().getLineType().endsWith("P")) {//如果是多票 则需要对卡数据进行纠正
                        if (cardInfoEntity.fileM1InfoEntity.getBlock_1C1D().getBoarding_time().equals(cardInfoEntity.transeTime)) {
                            MiLog.i("刷卡", "校验   有扣费数据  多票区域数据写入成功  完成");
                            long pay = xdRecord.getBalance() - cardInfoEntity.getBalance();
                            showVoice(cardInfoEntity, xdRecord);
                            xdRecord.setStatus("00");
                            xdRecord.setPayType("FD");
                            xdRecord.setTradeDiscount(cardInfoEntity.getBalance());
                            saveRecord(cardInfoEntity, pay, xdRecord);
                            BusToast.showToast("\n校验扣款：" + FileUtils.fen2Yuan(pay) + "元\n余额：" +
                                    FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", true);
                            return null;
                        } else {
                            MiLog.i("刷卡", "校验   多票区域数据写入失败 需要重新写入 需要重置数据");
                            return xdRecord;
                        }
                    } else {
                        MiLog.i("刷卡", "校验  单票校验 刷卡金额消费成功 完成" + xdRecord.getTradeTime());
                        long pay = xdRecord.getBalance() - cardInfoEntity.getBalance();
                        showVoice(cardInfoEntity, xdRecord);
                        xdRecord.setStatus("00");
                        xdRecord.setPayType("FD");
                        xdRecord.setTradeDiscount(cardInfoEntity.getBalance());
                        saveRecord(cardInfoEntity, pay, xdRecord);
                        BusToast.showToast("\n校验扣款：" + FileUtils.fen2Yuan(pay) + "元\n余额：" +
                                FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", true);
                        return null;
                    }
                } else {
                    MiLog.i("刷卡", "校验  开始校验  发送校验命令");
                    Rx.getInstance().sendMessage("checkMac", new Object[]{cardInfoEntity, xdRecord});//发送命令校准记录
                }
                return null;
            }
        } else {
            return newxdRecord;
        }
        return newxdRecord;
    }

    public static void checkMacBack(@Nullable devCmd checkMac, CardInfoEntity cardInfoEntity, XdRecord xdRecord) {
        MiLog.i("刷卡", "校验mac");
        try {
            int i = 0;
            byte[] result = new byte[checkMac.getnRecvLen()];
            arraycopy(checkMac.getDataBuf(), 0, result, 0, result.length);
            String str = FileUtils.bytesToHexString(result);

            byte[] Status = new byte[1];
            arraycopy(result, i, Status, 0, Status.length);
            String status = FileUtils.bytesToHexString(Status);
            i += Status.length;

            byte[] Sw = new byte[1];
            arraycopy(result, i, Sw, 0, Sw.length);
            String sw = FileUtils.bytesToHexString(Sw);
            i += Sw.length;

            byte[] Mac = new byte[4];
            arraycopy(result, i, Mac, 0, Mac.length);
            String mac = FileUtils.bytesToHexString(Mac);
            i += Mac.length;

            byte[] Tac = new byte[4];
            arraycopy(result, i, Tac, 0, Tac.length);
            String tac = FileUtils.bytesToHexString(Tac);
            i += Tac.length;


            xdRecord.setCardTradeTAC(tac);
            xdRecord.setStatus("00");
            xdRecord.setPayType("FD");
            int pay = 0;
            if (cardInfoEntity.selete_aid.equals("03")) {
                xdRecord.setTradeTime(cardInfoEntity.file18LocalInfoEntity.transaction_time);
//                xdRecord.setCardTradeCount(cardInfoEntity.file18LocalInfoEntity.transaction_number_18);
                pay = Integer.parseInt(cardInfoEntity.file18LocalInfoEntity.transaction_amount, 16);
            } else if (cardInfoEntity.selete_aid.equals("02")) {
                xdRecord.setTradeTime(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_time());
//                xdRecord.setCardTradeCount(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_number_18());
                pay = Integer.parseInt(cardInfoEntity.file18NewCPUInfoEntity.getTransaction_amount(), 16);
            }
            xdRecord.setTradeDiscount(cardInfoEntity.getBalance());
            DBManagerZB.saveRecord(xdRecord);
            showVoice(cardInfoEntity, xdRecord);
            MiLog.i("刷卡", "获取tac完成   保存记录成功");
            BusToast.showToast("\n本次扣款：" + FileUtils.fen2Yuan(pay) + "元\n余额：" +
                    FileUtils.fen2Yuan(cardInfoEntity.getBalance()) + "元", true);
        } catch (Exception e) {
            Log.i("刷卡", "校验出错：" + e.getMessage());
        }
    }
}
