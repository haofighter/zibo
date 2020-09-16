package com.szxb.zibo.config.zibo.line;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.Type;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.haikou.Black;
import com.szxb.zibo.config.haikou.BlackList;
import com.szxb.zibo.config.haikou.Whitelist;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.InitConfigZB;
import com.szxb.zibo.config.zibo.PublicKey;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.moudle.function.card.CardInfoEntity;
import com.szxb.zibo.moudle.function.card.JTB.File1AJTBInfoEntity;
import com.szxb.zibo.moudle.function.card.JTB.File1EJTBInfoEntity;
import com.szxb.zibo.moudle.function.card.JTB.FileJTBPay;
import com.szxb.zibo.moudle.function.location.GPSEntity;
import com.szxb.zibo.moudle.function.location.GPSEvent;
import com.szxb.zibo.util.BusToast;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.util.sp.CommonSharedPreferences;
import com.szxb.zibo.voice.SoundPoolUtil;
import com.szxb.zibo.voice.VoiceConfig;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.szxb.zibo.db.manage.DBCore.getDaoSession;
import static java.lang.System.arraycopy;


/**
 * 目前线路信息只保存一条
 * 如果有设置其他线路
 * 当票价文件进行解析保存时会将之前所保存的信息进行删除处理
 */
public class PraseLine {


    public static void praseAllLine(File file) {
        try {
            byte[] bytes = FileUtils.readFile(file);
            praseAllLineByte(bytes);
        } catch (Exception e) {
            BusToast.showToast("初始化线路参数失败", false);
        }
    }

    public static void praseAllLineByte(byte[] bytes) {
        try {
            String line = new String(bytes);
            Log.i("线路  文件内容", line);
            AllLineInfo allLineInfo = new Gson().fromJson(line, AllLineInfo.class);
            DBManagerZB.savaAllLineInfo(allLineInfo.getAllline());
        } catch (Exception e) {
            BusApp.getPosManager().setLinver("00000000000000");
            InitConfigZB.sendHeart();
            BusToast.showToast("解析线路失败\n正在重新下载", false);
        }
    }


    public static void praseLine(File file) {
        try {
            byte[] line = FileUtils.readFile(file);
            praseLine(line);
        } catch (Exception e) {
            BusApp.getPosManager().setFarver("00000000000000");
            InitConfigZB.sendHeart();
            BusToast.showToast("解析线路失败\n正在重新下载", false);
        }
    }

    public static void praseLine(byte[] line) throws Exception {
        DBManagerZB.clearAllLine();
        try {
            if (line == null || line.length == 0) {
                return;
            }
            String string = new String(line, "GB2312");
            Log.i("票价  文件内容", string);
            String[] priceStr = string.split("//\\*");
            for (int i = 0; i < priceStr.length; i++) {
                if (priceStr[i].contains("far.FarePlan")) {
                    prasePriceCase(priceStr[i]);
                } else if (priceStr[i].contains("far.BasicFare")) {
                    praseBasePriceCase(priceStr[i]);
                } else if (priceStr[i].contains("far.StationDivide")) {
                    praseStationCase(priceStr[i]);
                } else if (priceStr[i].contains("far.CardPlan")) {
                    praseCardCase(priceStr[i]);
                } else if (priceStr[i].contains("far.CardType")) {
                    praseCardType(priceStr[i]);
                } else if (priceStr[i].contains("far.FareRulePlan")) {
                    praseFareRulePlan(priceStr[i]);
                } else if (priceStr[i].contains("far.ContinuousRule")) {
                    praseContinuousRule(priceStr[i]);
                } else if (priceStr[i].contains("far.StationName")) {
                    praseStationName(priceStr[i]);
                }
            }

            try {
                FarePlan farePlan = DBManagerZB.checkFarePlan();
                FareRulePlan fareRulePlan = DBManagerZB.checkFareRulePlan();
                BusApp.getPosManager().setLineType();
                if (fareRulePlan.fareRuleType.equals("O")) {
                    GPSEvent.GPSClose();
                    if (farePlan != null) {
                        BasicFare basicFare = DBManagerZB.getBasicPrice(farePlan.getPricePayCaseNum());
                        BusApp.getPosManager().setBasePrice(Integer.parseInt(basicFare.getPrice()));
                        MiLog.i("参数配置", "设置基础票价：" + basicFare.getPrice());
                    }
//                    BusApp.getPosManager().setDirection("0000");
                } else if (fareRulePlan.fareRuleType.equals("P")) {
                    GPSEvent.GPSEventOpen();
                    BusApp.getPosManager().setBasePrice(PraseLine.getMorePayPrice(null, true, true));
//                    BusApp.getPosManager().setDirection("0001");
                } else if (fareRulePlan.fareRuleType.equals("X")) {
                    GPSEvent.GPSEventOpen();
                    BusApp.getPosManager().setBasePrice(PraseLine.getMorePayPrice(null, true, true));
//                    BusApp.getPosManager().setDirection("0001");
                }
            } catch (Exception e) {
                BusApp.getPosManager().setBasePrice(0);
                MiLog.i("错误：", "设置基础票价报错：" + e.getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            Log.i("错误", "文件/票价错误" + e.getMessage());
        }
    }

    //运营点汉字显示
    private static void praseStationName(String s) {
        s = s.replace("*\\\\far.StationName=", "").trim();
        String[] rules = s.split("\t");
        for (int i = 0; i < rules.length; i++) {
            String diraction = rules[i].substring(0, 4);
            List<StationName> stationNames = DBManagerZB.checkStation(diraction);
            if (rules[i].length() > 4) {
                String stationStr = rules[i].substring(4);
                try {
                    byte[] stationBytes = stationStr.getBytes("GBK");
                    MiLog.i("参数配置", "站点：" + stationStr + "        长度=" + stationBytes.length);
                    for (int j = 0; j < stationNames.size(); j++) {
                        try {
                            if (j < stationNames.size()) {
                                int start = j * 10;
                                int end = j * 10 + 10;
//                                stationNames.get(j).setStationName(stations.substring(start, end));
                                byte[] station = new byte[10];
                                MiLog.i("参数配置", "站点 当前起始位置：" + start);
                                if (end > stationBytes.length) {
                                    station = new byte[stationBytes.length - start];
                                }
                                arraycopy(stationBytes, start, station, 0, station.length);
                                String stationName = new String(station, "GBK");
                                MiLog.i("参数配置", " 站点 当前站点名：" + stationName);
                                stationNames.get(j).setStationName(stationName);
                            }
                        } catch (Exception e) {
                            MiLog.i("错误", "线路文件 站点名匹配错误   当前坐标j=" + j + "当前文件长度：" + rules[i].substring(4).length());
                        }
                    }

                } catch (Exception e) {

                }

//                String stations=stationStr.replace("  ", " ");
//                for (int j = 0; j < stationNames.size(); j++) {
//                    try {
//                        if (j < stationNames.size()) {
//                            int start = j * 5;
//                            int end = (j * 5 + 5) > stations.length() ? stations.length() : (j * 5 + 5);
//                            stationNames.get(j).setStationName(stations.substring(start, end));
//                        }
//                    } catch (Exception e) {
//                        MiLog.i("线路文件 站点名匹配错误", "当前坐标j=" + j + "当前文件长度：" + rules[i].substring(4).length());
//                    }
//                }
            }
            DBManagerZB.savaStationName(stationNames);
            prasePrice(diraction);
        }
    }

    //经纬度设置 *\\far.ContinuousRule=Schedule=00000000000000000000000000000000000000000000000000000000		//*
    private static void praseContinuousRule(String s) {
        s = s.replace("*\\\\far.ContinuousRule=", "").trim();
        s = s.replace("Schedule=", "").trim();//单票线路有的标示
        String lineNo = s.substring(0, 6);
        String[] rules = s.substring(6).split("\t");
        List<StationName> stationNames = new ArrayList<>();
        for (int i = 0; i < rules.length; i++) {
            for (int j = 0; j < rules[i].length() / 20; j++) {
                StationName stationName = new StationName();
                stationName.setLineNo(lineNo);
                stationName.setStationNo(rules[i].substring(j * 20, j * 20 + 2));
                stationName.setLon(new StringBuffer(rules[i].substring(j * 20 + 2, j * 20 + 11)).insert(3, ".").toString());
                stationName.setLat(new StringBuffer(rules[i].substring(j * 20 + 11, j * 20 + 20)).insert(3, ".").toString());
                stationName.setPriceTypeNum("000" + (i + 1));
                stationNames.add(stationName);
            }
        }
        DBManagerZB.savaStationName(stationNames);
    }

    //计费规则生成规则  *\\far.FareRulePlan=1000\O*60\U00000000000\S0	1001\O*60\U00000000000\S0	1002\O*40\U00000000000\S0	1003\O*00\U00000000000\S0	1004\O*00\U00000000000\S0	1005\O*00\U00000000000\S0	1006\O*60\U00000000000\S0	1007\O*00\U00000000000\S0	1008\O*00\U00000000000\S0	1009\O*00\U00000000000\S0	1010\O*00\U00000000000\S0	1011\O*100\U00000000000\S0	1012\O*60\U00000000000\S0	//*
    private static void praseFareRulePlan(String s) {
        s = s.replace("*\\\\far.FareRulePlan=", "").trim();
        String[] rules = s.split("\t");
        List<FareRulePlan> fareRulePlans = new ArrayList<>();
        for (int i = 0; i < rules.length; i++) {
            String[] detailRules = rules[i].split("\\\\");
            FareRulePlan fareRulePlan = new FareRulePlan();
            fareRulePlan.setFareRulePlanNum(detailRules[0]);
            fareRulePlan.setFareRuleType(detailRules[1].substring(0, 1));
            fareRulePlan.setFareRulePrice(detailRules[1].substring(2));
            fareRulePlan.setUnkownString(detailRules[2]);
            fareRulePlan.setUnkownTag(detailRules[3]);
            fareRulePlans.add(fareRulePlan);
        }
        DBManagerZB.saveFareRulePlan(fareRulePlans);
        Log.i("文件   票价", fareRulePlans.toString());
    }

    // *\\far.CardType=0100000000000000000000000000	0101000000000000000000000000	0200000000000000000000000000	0300000000000000000000000000	0301000000000000000000000000	0400000000000000000000000000	0500000000000000000000000000	0601000000000000000000000000	0611000000000000000000000000	0621000000000000000000000000	1800000000000000000000000000	3100000000000000000000000000	6501000000000000000000000000	//*
    //卡类型内容
    private static void praseCardType(String s) {
        s = s.replace("*\\\\far.CardType=", "").trim();
        List<CardType> cardTypes = new ArrayList<>();
        String[] rules = s.split("\t");
        for (int i = 0; i < rules.length; i++) {
            CardType cardType = new CardType();
            cardType.setCardTypeNum(rules[i].substring(0, 2));
            cardType.setDeposit(rules[i].substring(2, 10));
            cardType.setCost(rules[i].substring(10, 18));
            cardType.setEffectiveTime(rules[i].substring(18, 26));
            cardTypes.add(cardType);
        }
        DBManagerZB.saveCardType(cardTypes);
        Log.i("文件  票价 基本价格方案", cardTypes.toString());
    }

    //解析基本价格方案  *\\far.BasicFare=0010000000101000	//*
    private static void praseBasePriceCase(String s) {
        s = s.replace("*\\\\far.BasicFare=", "").trim();
        List<BasicFare> basicFares = new ArrayList<>();
        String[] rules = s.split("\t");
        for (int i = 0; i < rules.length; i++) {
            Log.i("文件  票价", "价格规则：" + rules[i]);
            BasicFare basicFare = new BasicFare();
            String pricePayInfoStr = rules[i];
            if (pricePayInfoStr.length() >= 16) {
                basicFare.setBasepricCaseNum(pricePayInfoStr.substring(0, 4));
                basicFare.setPrice(pricePayInfoStr.substring(4, 12));
                basicFare.setPriceTypeNum(pricePayInfoStr.substring(12, 16));
                List<Long> prices = new ArrayList<>();
                for (int j = 3; j <= pricePayInfoStr.length() / 8; j++) {
                    try {
                        prices.add(Long.parseLong(rules[i].substring((j - 1) * 8, j * 8)));
                    } catch (Exception e) {
                        prices.add(0l);
                    }
                }
                basicFare.setPrices(new Gson().toJson(prices));
            }
            basicFares.add(basicFare);
        }
        Log.i("文件  票价 基本价格方案", basicFares.toString());
        DBManagerZB.savePraseBasePriceCase(basicFares);
    }

    public static void prasePrice(String priceTypeNum) {
        List<StationName> stationNames = DBManagerZB.checkStation(priceTypeNum);
        List<Long> prices = new Gson().fromJson(DBManagerZB.checkBasicPrice(priceTypeNum).getPrices(), new TypeToken<List<Long>>() {
        }.getType());
        List<StationPayPrice> stationPayPrices = new ArrayList<>();
        for (int i = 1; i < stationNames.size() + 1; i++) {
            for (int j = i; j < stationNames.size() + 1; j++) {

                StationPayPrice stationPayPrice = new StationPayPrice();
                stationPayPrice.up = i;
                stationPayPrice.priceTypeNum = priceTypeNum;
                stationPayPrice.down = j;
                int priceIndex = 0;
                for (int k = 1; k < i; k++) {
                    priceIndex += stationNames.size() - i + k + 1;
                }
                try {
                    Log.i("价格规则", stationPayPrice.toString());
//                    if (i == j) {
//                        stationPayPrice.price = 0;
//                        stationPayPrices.add(stationPayPrice);
//                    } else {
                    stationPayPrice.price = prices.get(priceIndex + j - i);
                    stationPayPrices.add(stationPayPrice);
//                    }
                } catch (Exception e) {
                    Log.i("价格规则", "获取票价失败" + (priceIndex + j - i));
                }
            }
        }

        Log.i("价格规则", stationPayPrices.toString());
        DBManagerZB.saveAllStationPrice(stationPayPrices);
    }

    //解析计价方案信息 *\\far.FarePlan=904600101046	//*
    private static void prasePriceCase(String priceCase) {
        priceCase = priceCase.replace("*\\\\far.FarePlan=", "").trim();
        List<FarePlan> proceCases = new ArrayList<>();
        String[] proceCaseRule = priceCase.split("\t");
        for (int i = 0; i < proceCaseRule.length; i++) {
            if (proceCaseRule[i].length() == 12) {
                FarePlan farePlan = new FarePlan();
                farePlan.setPricCaseNum(proceCaseRule[i].substring(0, 4));
                farePlan.setPricePayCaseNum(proceCaseRule[i].substring(4, 8));
                farePlan.setCardCaseNUm(proceCaseRule[i].substring(8, 12));
                proceCases.add(farePlan);
            }
        }
        DBManagerZB.savePriceCase(proceCases);
        Log.i("文件   票价 计价方案信息", proceCases.toString());
    }

    //消费点划分   *\\far.StationDivide=1000	//*
    private static void praseStationCase(String stationDivide) {
        stationDivide = stationDivide.replace("*\\\\far.StationDivide=", "").trim();
        String[] stationDivideRule = stationDivide.split("\t");
        List<StationDivide> stationDivides = new ArrayList<>();
        for (int i = 0; i < stationDivideRule.length; i++) {
            String stationDivideStr = stationDivideRule[i];
            StationDivide stationDivideInfo = new StationDivide();
            stationDivideInfo.setPriceTypeNum(stationDivideStr.substring(0, 4));
            List<String> stations = new ArrayList<>();
            for (int j = 0; j < (stationDivideStr.length() - 4) / 10; j++) {
                stations.add(stationDivideStr.substring(4 + j * 10, 14 + j * 10));
            }
            stationDivideInfo.setPirceAdd(new Gson().toJson(stations));
            stationDivides.add(stationDivideInfo);
        }
        DBManagerZB.saveAllStation(stationDivides);
        Log.i("文件  票价 消费点划分", stationDivides.toString());
    }

    //卡方案生成规则  *\\far.CardPlan=1046010010000000\L1\E1\T1\C000000000\O000000000\P0001\M0\W000000000\Z1\H普通卡	010110010000\L1\E1\T1\C000000000\O000000000\P0008\M0\W000000000\Z1\H无偿献血卡	020010020000\L1\E1\T1\C000000000\O000000000\P0002\M0\W000000000\Z300\H学生卡	030010030000\L1\E1\T1\C000000000\O000000000\P0003\M0\W000000000\Z300\H老年卡	030110040000\L1\E1\T1\C000000000\O000000000\P0004\M0\W000000000\Z300\H爱心卡	040010050000\L1\E1\T1\C000000000\O000000000\P0005\M0\W000000000\Z300\H荣军卡	050010060000\L1\E1\T1\C000000000\O000000000\P0001\M0\W000000000\Z1\H拥军卡	060110070000\L1\E1\T1\C000000000\O000000000\P0006\M0\W000000000\Z300\H员工卡	061110080000\L1\E1\T1\C000000000\O000000000\P0006\M0\W000000000\Z300\H司机卡	062110090000\L1\E1\T1\C000000000\O000000000\P0006\M0\W000000000\Z300\H客服卡	180010100000\L1\E1\T1\C000000000\O000000000\P0001\M0\W000000000\Z300\H稽查卡	310010110000\L0\E0\T0\C000000000\O000000000\P0016\M0\W000000000\Z1\H微信	650110120000\L1\E1\T1\C000000000\O000000000\P0001\M0\W000000000\Z1\H城联普通卡		//*
    private static void praseCardCase(String cardCase) {
        cardCase = cardCase.replace("*\\\\far.CardPlan=", "").trim();
        String[] cardRules = cardCase.split("\t\t");
        List<CardPlan> cardPlans = new ArrayList<>();
        for (int i = 0; i < cardRules.length; i++) {
            String caseNum = cardRules[i].substring(0, 4);
            String[] cardInfos = cardRules[i].substring(4).trim().split("\t");
            for (int j = 0; j < cardInfos.length; j++) {
                CardPlan cardPlan = new CardPlan();
                cardPlan.setCardCaseNum(caseNum);
                cardPlan.setCardType(cardInfos[j].substring(0, 4));
                cardPlan.setParentCardType(cardInfos[j].substring(0, 2));
                cardPlan.setChildCardType(cardInfos[j].substring(2, 4));
                cardPlan.setCardPayRuleNum(cardInfos[j].substring(4, 8));
                cardPlan.setRev(cardInfos[j].substring(8, 12));
                String[] cardInfoStr = cardInfos[j].substring(12).split("\\\\");
                for (int k = 0; k < cardInfoStr.length; k++) {
                    if (cardInfoStr[k].startsWith("H")) {
                        cardPlan.setCardName(cardInfoStr[k].replace("H", ""));
                    } else if (cardInfoStr[k].startsWith("L")) {
                        cardPlan.setNeedCheckBlack(cardInfoStr[k].replace("L", ""));
                    } else if (cardInfoStr[k].startsWith("E")) {
                        cardPlan.setNeedCheckStartTime(cardInfoStr[k].replace("E", ""));
                    } else if (cardInfoStr[k].startsWith("T")) {
                        cardPlan.setNeedCheckEndTime(cardInfoStr[k].replace("T", ""));
                    } else if (cardInfoStr[k].startsWith("C")) {
                        cardPlan.setBalanceType(cardInfoStr[k].replace("C", ""));
                    } else if (cardInfoStr[k].startsWith("A")) {
                        cardPlan.setRechargeTag(cardInfoStr[k].replace("A", ""));
                    } else if (cardInfoStr[k].startsWith("O")) {
                        cardPlan.setNeedCheckStartTime(cardInfoStr[k].replace("O", ""));
                    } else if (cardInfoStr[k].startsWith("P")) {
                        cardPlan.setVoiceType(cardInfoStr[k].replace("P", ""));
                    } else if (cardInfoStr[k].startsWith("D")) {
                        cardPlan.setRetrueMoney(cardInfoStr[k].replace("D", ""));
                    } else if (cardInfoStr[k].startsWith("R")) {
                        cardPlan.setRetrueMoneyTag(cardInfoStr[k].replace("R", ""));
                    } else if (cardInfoStr[k].startsWith("B")) {
                        cardPlan.setRetrueMonthTag(cardInfoStr[k].replace("B", ""));
                    } else if (cardInfoStr[k].startsWith("X")) {
                        cardPlan.setRecover(cardInfoStr[k].replace("X", ""));
                    } else if (cardInfoStr[k].startsWith("I")) {
                        cardPlan.setServiceCharge(cardInfoStr[k].replace("I", ""));
                    } else if (cardInfoStr[k].startsWith("G")) {
                        cardPlan.setRentMoney(cardInfoStr[k].replace("G", ""));
                    } else if (cardInfoStr[k].startsWith("J")) {
                        cardPlan.setRentType(cardInfoStr[k].replace("J", ""));
                    } else if (cardInfoStr[k].startsWith("N")) {
                        cardPlan.setRegistered(cardInfoStr[k].replace("N", ""));
                    } else if (cardInfoStr[k].startsWith("M")) {
                        cardPlan.setGiveWallet(cardInfoStr[k].replace("M", ""));
                    } else if (cardInfoStr[k].startsWith("W")) {
                        cardPlan.setMinMoney(cardInfoStr[k].replace("W", ""));
                    } else if (cardInfoStr[k].startsWith("S")) {
                        cardPlan.setGiveInterval(cardInfoStr[k].replace("S", ""));
                    } else if (cardInfoStr[k].startsWith("Z")) {
                        cardPlan.setUseInterval(cardInfoStr[k].replace("Z", ""));
                    }
                }
                cardPlans.add(cardPlan);
            }
        }
        DBManagerZB.saveAllCard(cardPlans);
        Log.i("文件  票价 卡方案生成规则", cardPlans.toString());
    }

    public static void prasePub(File file) {
        try {
            byte[] pub = FileUtils.readFile(file);
            prasePub(pub);
        } catch (Exception e) {
            MiLog.i("文件  密钥", "读取失败");
        }
    }

    //解析密钥
    public static void prasePub(byte[] file) {
        try {

            String pubStr = new String(file);
            String[] pubSingles = pubStr.split("\n");
            List<PublicKey> publicKeys = new ArrayList<>();
            for (int i = 0; i < pubSingles.length; i++) {
                String[] pubSingle = pubSingles[i].split(" ");
                if (pubSingle.length == 7) {
                    PublicKey publicKey = new PublicKey();
                    publicKey.setPublicCreatTag(pubSingle[0]);
                    publicKey.setPublicKeyTag(Integer.parseInt(pubSingle[1], 16));
                    publicKey.setHashAlgorithmTag(pubSingle[2]);
                    publicKey.setPublicAlgorithmTag(pubSingle[3]);
                    publicKey.setPublicIndexTag(pubSingle[4]);
                    publicKey.setPublicLenth(pubSingle[5]);
                    publicKey.setPublicKey(pubSingle[6]);
                    publicKeys.add(publicKey);
                }
            }
            DBManagerZB.saveAllPub(publicKeys);
            MiLog.i("文件  密钥", publicKeys.toString());
        } catch (Exception e) {
            MiLog.i("文件  密钥", "解析失败");
        }
    }

    //解析黑名单文件
    public static void praseCsn(File file) {
        try {
            CommonSharedPreferences.put("black", file.getName());
            byte[] csn = FileUtils.readFile(file);
            praseCsnByte(csn, file.getName());
        } catch (Exception e) {
            MiLog.i("黑名单解析失败", "");
        }
    }

    public static void praseCsnByte(byte[] csn, String fileName) {
        int i = 0;
//            //文件明
//            byte[] Status = new byte[18];
//            arraycopy(csn, i, Status, 0, Status.length);
//            i += Status.length;
//            String fileName = (String) FileUtils.byte2Parm(Status, Type.HEX);

        //文件头信息
        byte[] Head = new byte[9];
        arraycopy(csn, i, Head, 0, Head.length);
        i += Head.length;
        String head = new String(Head);

        List<BlackList> addblackLists = new ArrayList<>();
        List<BlackList> refuseblackLists = new ArrayList<>();
        int index = 0;
        while (index <= 1000) {
            index++;
            try {
                BlackList blackList = new BlackList();
                byte[] BlackStart = new byte[10];
                arraycopy(csn, i, BlackStart, 0, BlackStart.length);
                String blackStart = FileUtils.bytesToHexString(BlackStart);
                if (blackStart.startsWith("ffff")) {
                    break;
                }
                i += BlackStart.length;
                blackList.setCardStart(blackStart);

                byte[] BlackEnd = new byte[10];
                arraycopy(csn, i, BlackEnd, 0, BlackEnd.length);
                i += BlackEnd.length;
                String blackEnd = FileUtils.bytesToHexString(BlackEnd);
                blackList.setCardEnd(blackEnd);

                Log.i("批量黑名单", "blackStart=" + blackStart + "     blackEnd= " + blackEnd);
                byte[] Tag = new byte[1];
                arraycopy(csn, i, Tag, 0, Tag.length);
                i += Tag.length;
                String tag = FileUtils.bytesToHexString(Tag);
                blackList.setType(tag);

                if (tag.equals("00")) {
                    refuseblackLists.add(blackList);
                } else {
                    addblackLists.add(blackList);
                }
            } catch (Exception e) {
                Log.i("黑名单批量解析", e.getMessage());
            }
        }

        if (fileName.contains("_")) {
            DBManagerZB.insertBlackList(addblackLists);
            DBManagerZB.deleteBlackList(refuseblackLists);
        } else {
            getDaoSession().getBlackListDao().deleteAll();
            DBManagerZB.insertBlackList(addblackLists);
            Log.i("黑名单批量解析", "全量删除");
        }


        List<Black> addblacks = new ArrayList<>();
        List<Black> refuseblacks = new ArrayList<>();
        int blackNum = 0;
        while (blackNum <= 100000) {
            index++;
            try {
                Black black = new Black();
                byte[] BlackNo = new byte[10];
                arraycopy(csn, i, BlackNo, 0, BlackNo.length);
                String blackStart = FileUtils.bytesToHexString(BlackNo);
                i += BlackNo.length;
                black.setCardNum(blackStart);

                Log.i("当前黑名单", "blackStart=" + blackStart + "   ");
                if (blackStart.startsWith("2550") && blackStart.endsWith("0005")) {
                    Log.i("黑名单", "blackStart=" + blackStart);
                }

                byte[] Tag = new byte[1];
                arraycopy(csn, i, Tag, 0, Tag.length);
                i += Tag.length;
                String tag = FileUtils.bytesToHexString(Tag);
                black.setType(tag);

                if (tag.equals("00")) {
                    refuseblacks.add(black);
                } else {
                    addblacks.add(black);
                }

                if (blackStart.contains("50004005")) {
                    Log.i("找到一个黑名单卡", blackStart + "      " + addblacks.size() + "       " + refuseblacks.size());
                }
            } catch (Exception e) {
                Log.i("黑名单单个解析", e.getMessage() + "      " + addblacks.size() + "       " + refuseblacks.size());
                break;
            }
        }
        if (fileName.contains("_")) {
            DBManagerZB.insertBlack(addblacks);
            DBManagerZB.deleteBlack(refuseblacks);
        } else {
            getDaoSession().getBlackDao().deleteAll();
            DBManagerZB.insertBlack(addblacks);
            Log.i("黑名单批量解析", "全量删除");
        }

    }


    /**
     * 单票制获取实际扣款票价
     *
     * @param cardType
     * @param childCardType
     * @return
     */
    public static int getNormalPayPrice(String cardType, String childCardType) {
        return getPayPrice(cardType, childCardType, BusApp.getPosManager().getBasePrice());
    }

    /**
     * 分段票价
     *
     * @param cardType
     * @param childCardType
     * @return
     */
    public static int getSectionPayPrice(String cardType, String childCardType, int price) {

        return getPayPrice(cardType, childCardType, price);
    }


    /**
     * 多票票价计算
     *
     * @param cardInfoEntity 当前卡信息
     * @param isup           是否为上车
     * @param isFull         是否为最大票价
     * @return isup=true 返回当前站至最大站点的票价 isFull=true  返回当前站至最大站点的票价 isFull=false 返回上车站至当前站的票价
     */
    public static int getMorePayPrice(CardInfoEntity cardInfoEntity, boolean isup, boolean isFull) {
        try {
            if (isup) {//上车 获取到终点的价格
                return (int) DBManagerZB.checkMoreTicket(BusApp.getPosManager().getDirection(),
                        BusApp.getPosManager().getStationID(),
                        DBManagerZB.checkStationMax(BusApp.getPosManager().getDirection()).getStationNoInt());

            } else {//下车
                if (isFull) {//获取至终点的票价
                    return (int) DBManagerZB.checkMoreTicket(BusApp.getPosManager().getDirection(),
                            cardInfoEntity.getBoarding_site_indexInt(),
                            DBManagerZB.checkStationMax(BusApp.getPosManager().getDirection()).getStationNoInt());
                } else {//获取当前站下车的票价
                    int cardStation = cardInfoEntity.getBoarding_site_indexInt();
                    int nowStation = BusApp.getPosManager().getStationID();
                    if (cardStation < nowStation) {
                        return (int) DBManagerZB.checkMoreTicket(BusApp.getPosManager().getDirection(),
                                cardInfoEntity.getBoarding_site_indexInt(),
                                BusApp.getPosManager().getStationID());
                    } else {
                        return (int) DBManagerZB.checkMoreTicket(BusApp.getPosManager().getDirection(),
                                BusApp.getPosManager().getStationID(),
                                cardInfoEntity.getBoarding_site_indexInt());
                    }
                }
            }
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 通过卡类型  及基础票价来获取实际扣款票价
     *
     * @param cardType      卡类型
     * @param childCardType 卡姿类型
     * @param price         基础票价
     * @return 折扣票价（实际扣款）
     */
    public static int getPayPrice(String cardType, String childCardType, int price) {
        try {
            FarePlan farePlan = DBManagerZB.checkFarePlan();
            if (farePlan != null) {
                CardPlan cardPlan = DBManagerZB.checkCardPlan(farePlan.getCardCaseNUm(), cardType, childCardType);
                FareRulePlan fareRulePlan = DBManagerZB.checkPricePayRule(cardPlan.getCardPayRuleNum());
                MiLog.i("刷卡", "当前价格=" + fareRulePlan.fareRulePrice);
                return price * (Integer.parseInt(fareRulePlan.fareRulePrice)) / 100;
            } else {
                price = -1;
            }
        } catch (Exception e) {
            price = -1;
        }
        return price;
    }


    /**
     * 获取当前计算得到的票价
     *
     * @param cardInfoEntity
     * @param cardType
     * @param childCardType
     * @return 当前交易需要扣款的金额 -1 无法获取当前票价规则 -2卡内金额不足以支付全票价格 0正常
     */
    public static int getCardPayPrice(CardInfoEntity cardInfoEntity, String cardType, String childCardType) {
        int price = BusApp.getPosManager().getBasePrice();
        switch (BusApp.getPosManager().getLineType()) {
            case "O":
                price = getNormalPayPrice(cardType, childCardType);
                cardInfoEntity.setPayAllPrice(BusApp.getPosManager().getBasePrice());
                cardInfoEntity.setTranseType(0);
                break;
            case "P":
                if (cardInfoEntity.getComplete_mark().equals("00")) { //表示交易完成 当前为上车
                    //表示上车 计算当前站至终点的票价
                    cardInfoEntity.setTranseType(1);
                    int fullprice = getMorePayPrice(cardInfoEntity, true, true);
                    cardInfoEntity.setFull_fare(fullprice);
                    cardInfoEntity.setComplete_mark("01");
                    cardInfoEntity.setPayAllPrice(0);
                    int realpay = getPayPrice(cardType, childCardType, fullprice);
                    if (realpay >= 0) {
                        cardInfoEntity.setPre_preferential_amount(realpay);
                    } else {
                        return realpay;
                    }

                    if (cardInfoEntity.getBalance() < realpay) {
                        price = -2;
                    } else {
                        price = 0;
                    }
                    setMoreTicketInfo(cardInfoEntity, fullprice);
                } else {
                    boolean isSameBus = false;//表示非同一辆车
                    boolean isSameDiraction = false;//非同一个方向
                    boolean isLimitTime = false;//间隔时间超过两小时
                    boolean isSameLine = false;
                    boolean isFiveMin = false;
                    try {
                        int nowDir = Integer.parseInt(BusApp.getPosManager().getDirection()) == 1 ? 0 : 1;
                        String nowBus = BusApp.getPosManager().getBusNo();

                        if (cardInfoEntity.selete_aid.equals("02")) {
                            String nowLine = BusApp.getPosManager().getNewCpuLine();
                            String cardBus = cardInfoEntity.getnewCPUMorePriceInfo().getVehicle_number();
                            String cardDir = cardInfoEntity.getnewCPUMorePriceInfo().getDriver_direction();
                            String cardLine = cardInfoEntity.getnewCPUMorePriceInfo().getLink_number();
                            isSameBus = cardBus.equals(nowBus);//表示非同一辆车
                            isSameDiraction = Integer.parseInt(cardDir) == nowDir;//非同一个方向
                            isLimitTime = System.currentTimeMillis() - DateUtil.getDateLong(cardInfoEntity.getnewCPUMorePriceInfo().getBoarding_time(), "yyyyMMddHHmmss") > 5 * 60 * 60 * 1000;//间隔时间超过两小时
                            isFiveMin = System.currentTimeMillis() - DateUtil.getDateLong(cardInfoEntity.getnewCPUMorePriceInfo().getBoarding_time(), "yyyyMMddHHmmss") > 10 * 60 * 1000;//间隔时间超过10

                            isSameLine = nowLine.equals(cardLine);
                        } else if (cardInfoEntity.selete_aid.equals("01")) {
                            if (cardInfoEntity.getFile1AJTBInfoEntity().getTransaction_status_1a().equals("00")) {//已完成
                                String nowLine = BusApp.getPosManager().getLineNo().substring(2);
                                String cardBus = cardInfoEntity.getFile1AJTBInfoEntity().getBoarding_vehicle_number_1a();
                                String cardDir = cardInfoEntity.getFile1AJTBInfoEntity().getDirection_identity_1a();
                                String cardLine = cardInfoEntity.getFile1AJTBInfoEntity().getBoarding_line_number_1a();
                                isSameBus = FileUtils.formatHexStringToByteString(6, nowBus).equals(cardBus);//表示非同一辆车
                                isSameDiraction = Integer.parseInt(cardDir) == nowDir;//非同一个方向
                                isLimitTime = System.currentTimeMillis() - DateUtil.getDateLong(cardInfoEntity.getFile1AJTBInfoEntity().getAlight_time_1a(), "yyyyMMddHHmmss") > 5 * 60 * 60 * 1000;//间隔时间超过两小时
                                isFiveMin = System.currentTimeMillis() - DateUtil.getDateLong(cardInfoEntity.getFile1AJTBInfoEntity().getAlight_time_1a(), "yyyyMMddHHmmss") > 10 * 60 * 1000;//间隔时间超过10分钟
                                isSameLine = nowLine.equals(cardLine);
                            } else {
                                String nowLine = BusApp.getPosManager().getLineNo().substring(2);
                                String cardBus = cardInfoEntity.getFile1AJTBInfoEntity().getBoarding_vehicle_number_1a();
                                String cardDir = cardInfoEntity.getFile1AJTBInfoEntity().getDirection_identity_1a();
                                String cardLine = cardInfoEntity.getFile1AJTBInfoEntity().getBoarding_line_number_1a();
                                isSameBus = FileUtils.formatHexStringToByteString(6, nowBus).equals(cardBus);//表示非同一辆车
                                isSameDiraction = Integer.parseInt(cardDir) == nowDir;//非同一个方向
                                isLimitTime = System.currentTimeMillis() - DateUtil.getDateLong(cardInfoEntity.getFile1AJTBInfoEntity().getBoarding_time_1a(), "yyyyMMddHHmmss") > 5 * 60 * 60 * 1000;//间隔时间超过两小时
                                isFiveMin = System.currentTimeMillis() - DateUtil.getDateLong(cardInfoEntity.getFile1AJTBInfoEntity().getBoarding_time_1a(), "yyyyMMddHHmmss") > 10 * 60 * 1000;//间隔时间超过10分钟
                                isSameLine = nowLine.equals(cardLine);
                            }
                        } else {
                            String nowLine = BusApp.getPosManager().getM1Line();
                            String cardBus = cardInfoEntity.getMorePriceInfo().getVehicle_number();
                            String cardDir = cardInfoEntity.getMorePriceInfo().getDriver_direction();
                            String cardLine = cardInfoEntity.getMorePriceInfo().getLink_number();
                            isSameBus = cardBus.equals(nowBus);//表示非同一辆车
                            isSameDiraction = Integer.parseInt(cardDir) == nowDir;//非同一个方向
                            isLimitTime = System.currentTimeMillis() - DateUtil.getDateLong(cardInfoEntity.getMorePriceInfo().getBoarding_time(), "yyyyMMddHHmmss") > 5 * 60 * 60 * 1000;//间隔时间超过两小时
                            isFiveMin = System.currentTimeMillis() - DateUtil.getDateLong(cardInfoEntity.getMorePriceInfo().getBoarding_time(), "yyyyMMddHHmmss") > 10 * 60 * 1000;//间隔时间超过10分钟
                            isSameLine = nowLine.equals(cardLine);
                        }
                    } catch (Exception e) {
                    }

                    int fullPrice = cardInfoEntity.getFull_fare();//卡记录中的优惠前的金额
                    int cardRealPrice = Integer.parseInt(cardInfoEntity.getPre_preferential_amount(), 16);//卡记录中的优惠后的金额
                    int nowStation = BusApp.getPosManager().getStationID();


                    if (!isSameBus || isLimitTime || !isSameLine) { // 非同车  时间限制之外  非同线路
                        if (cardInfoEntity.getComplete_mark().equals("01")) {//表示交易未完成 当前为补票
                            cardInfoEntity.setComplete_mark("00");
                            cardInfoEntity.setTranseType(3);
                            setMoreTicketInfo(cardInfoEntity, fullPrice);
                            if (cardInfoEntity.getFull_fare() > 5000) {
                                cardInfoEntity.setPayAllPrice(cardRealPrice);
                            } else {
                                cardInfoEntity.setPayAllPrice(cardInfoEntity.getFull_fare());
                            }
                            if (getPayPrice(cardType, childCardType, cardInfoEntity.getFull_fare()) == 0) {
                                return 0;
                            }
                            return cardRealPrice;
                        } else {
                            cardInfoEntity.setComplete_mark("00");
                            cardInfoEntity.setPayAllPrice(fullPrice);
                            setMoreTicketInfo(cardInfoEntity, fullPrice);
                            cardInfoEntity.setTranseType(3);
                            return 0;
                        }
                    } else {
                        cardInfoEntity.setTranseType(2);
//                        isFiveMin = true;
                        //超过5分钟  同方向  同站点
                        if (!isFiveMin && isSameDiraction && cardInfoEntity.getBoarding_site_indexInt() == BusApp.getPosManager().getStationID()) {
                            cardInfoEntity.setFull_fare(fullPrice);
                            cardInfoEntity.setPre_preferential_amount(0);
                            cardInfoEntity.setPayAllPrice(0);
                            cardInfoEntity.setComplete_mark("00");
                            return 0;
                        } else {
//                            if (cardInfoEntity.getBoarding_site_indexInt() > BusApp.getPosManager().getStationID()) {//下车站点号小于上车站点号 视为补票交易
//                                cardInfoEntity.setTranseType(3);
//                                realPrice = getMorePayPrice(cardInfoEntity, false, true);
//                            } else {
                            if (!isSameDiraction && nowStation == 1) {//如果当前站为起点站 则下车不需要判定方向 直接扣除总票价
                                cardInfoEntity.setComplete_mark("00");
                                setMoreTicketInfo(cardInfoEntity, fullPrice);
                                cardInfoEntity.setTranseType(2);
                                if (cardInfoEntity.getFull_fare() > 5000) {
                                    cardInfoEntity.setPayAllPrice(cardRealPrice);
                                } else {
                                    cardInfoEntity.setPayAllPrice(cardInfoEntity.getFull_fare());
                                }
                                if (getPayPrice(cardType, childCardType, cardInfoEntity.getFull_fare()) == 0) {
                                    return 0;
                                }
                                return cardRealPrice;
//                                return getPayPrice(cardType, childCardType, cardInfoEntity.getFull_fare());
                            } else {
                                int realPrice = getMorePayPrice(cardInfoEntity, false, false);
                                cardInfoEntity.setFull_fare(fullPrice);
                                cardInfoEntity.setPre_preferential_amount(realPrice);
                                cardInfoEntity.setComplete_mark("00");
                                cardInfoEntity.setPayAllPrice(realPrice);
                                return getPayPrice(cardType, childCardType, realPrice);
//                            }
                            }
                        }
                    }
                }
                break;
            case "X":
                cardInfoEntity.setTranseType(0);
                int nowPrice = getMorePayPrice(cardInfoEntity, true, true);
                cardInfoEntity.setPayAllPrice(nowPrice);
                price = getSectionPayPrice(cardType, childCardType, nowPrice); //由于分段的票价和多票票价一样 所以取至终点的票价
                break;
        }
        MiLog.i("刷卡", "消费金额=" + price);
        return price;
    }

    //填充多票消费数据
    public static void setMoreTicketInfo(CardInfoEntity cardInfoEntity, int fullPrice) {
        if (cardInfoEntity.selete_aid.equals("02")) {
            cardInfoEntity.getnewCPUMorePriceInfo().setDriver_direction(BusApp.getPosManager().getDirection());
            cardInfoEntity.getnewCPUMorePriceInfo().setVehicle_number(BusApp.getPosManager().getBusNo());
            cardInfoEntity.getnewCPUMorePriceInfo().setBoarding_site_index(BusApp.getPosManager().getStationID());
            cardInfoEntity.getnewCPUMorePriceInfo().setBoarding_time(DateUtil.getCurrentDate2());
            cardInfoEntity.getnewCPUMorePriceInfo().setLink_number(BusApp.getPosManager().getNewCpuLine());
            cardInfoEntity.getnewCPUMorePriceInfo().setDriver_number(BusApp.getPosManager().getDriverNo());
            cardInfoEntity.getnewCPUMorePriceInfo().setPose_id(BusApp.getPosManager().getM1psam());
            cardInfoEntity.getnewCPUMorePriceInfo().setFull_fare(fullPrice);
        } else if (cardInfoEntity.selete_aid.equals("01")) {
            cardInfoEntity.getFile1AJTBInfoEntity().setTrade_serial_number_1a(BusApp.getPosManager().getmchTrxId() + "");
            if (cardInfoEntity.getTranseType() == 1) {//上车
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_city_code_1a("0000");
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_city_code_1a("4530");
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_mark_1a(FileUtils.hexStringFromatByF(8, "13664530", false));
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_mark_1a("00000000");
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_the_site_1a(BusApp.getPosManager().getStationID());
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_the_site_1a("00");
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_terminal_number_1a(BusApp.getPosManager().getJTBpsam());
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_terminal_number_1a("00000000");
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_time_1a(DateUtil.getCurrentDate2());
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_time_1a("0");
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_max_amount_1a(fullPrice);
                String nowLineDir = FileUtils.formatHexStringToByteString(1, BusApp.getPosManager().getDirection());
                cardInfoEntity.getFile1AJTBInfoEntity().setDirection_identity_1a(nowLineDir.equals("01") ? "00" : "01");
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_line_number_1a(BusApp.getPosManager().getLineNo().substring(2, 6));
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_vehicle_number_1a(BusApp.getPosManager().getBusNo());
            } else {//下车
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_city_code_1a("0000");
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_city_code_1a("4530");
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_mark_1a("00000000");
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_mark_1a(FileUtils.hexStringFromatByF(8, "13664530", false));
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_the_site_1a("00");
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_the_site_1a(BusApp.getPosManager().getStationID());
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_terminal_number_1a("00000000");
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_terminal_number_1a(BusApp.getPosManager().getJTBpsam());
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_time_1a("0");
                cardInfoEntity.getFile1AJTBInfoEntity().setAlight_time_1a(DateUtil.getCurrentDate2());
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_max_amount_1a(fullPrice);
                String nowLineDir = FileUtils.formatHexStringToByteString(1, BusApp.getPosManager().getDirection());
                cardInfoEntity.getFile1AJTBInfoEntity().setDirection_identity_1a(nowLineDir.equals("01") ? "00" : "01");
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_line_number_1a(BusApp.getPosManager().getLineNo().substring(2, 6));
                cardInfoEntity.getFile1AJTBInfoEntity().setBoarding_vehicle_number_1a(BusApp.getPosManager().getBusNo());
            }
        } else {
            cardInfoEntity.getMorePriceInfo().setDriver_direction(BusApp.getPosManager().getDirection());
            cardInfoEntity.getMorePriceInfo().setVehicle_number(BusApp.getPosManager().getBusNo());
            cardInfoEntity.getMorePriceInfo().setBoarding_site_index(BusApp.getPosManager().getStationID());
            cardInfoEntity.getMorePriceInfo().setBoarding_time(DateUtil.getCurrentDate2());
            cardInfoEntity.getMorePriceInfo().setLink_number(BusApp.getPosManager().getM1Line());
            cardInfoEntity.getMorePriceInfo().setDriver_number(BusApp.getPosManager().getDriverNo());
            cardInfoEntity.getMorePriceInfo().setLine_1(BusApp.getPosManager().getLineNo());//分公司编号写到线路号1里面
            cardInfoEntity.getMorePriceInfo().setVersion("01");
            cardInfoEntity.getMorePriceInfo().setFull_fare(fullPrice);
        }

        if (cardInfoEntity.selete_aid.equals("04")) {//M1
            cardInfoEntity.getMorePriceInfo().setPose_id(BusApp.getPosManager().getM1psam());
            cardInfoEntity.getMorePriceInfo().setVerify3("18E7");
        } else if (cardInfoEntity.selete_aid.equals("02")) {
            cardInfoEntity.getnewCPUMorePriceInfo().setPose_id(BusApp.getPosManager().getM1psam());
        } else if (cardInfoEntity.selete_aid.equals("03")) {//CPU
            cardInfoEntity.getMorePriceInfo().setPose_id(BusApp.getPosManager().getCpupsam());
            cardInfoEntity.getMorePriceInfo().setReserved3("00000000");
        }
    }

    //白名单解析
    public static void praseUsr(File file) {

        try {
            CommonSharedPreferences.put("black", file.getName());
            byte[] csn = FileUtils.readFile(file);
            praseUsrByte(csn, file.getName());
        } catch (Exception e) {
            MiLog.i("流程", "白名单解析失败");
        }
    }

    //白名单
    public static void praseUsrByte(byte[] csn, String name) {
        try {
            String csnStr = new String(csn);

            String[] usrInfo = csnStr.split("=");
            String head = usrInfo[0];
            String style = head.substring(4, 5);
            String content = usrInfo[1];

            String[] usrs = content.split("\\t");

            List<Whitelist> whitelists = new ArrayList<>();
            for (int i = 0; i < usrs.length; i++) {
                try {
                    Whitelist whitelist = new Whitelist();
                    whitelist.setUser(usrs[i].substring(0, 6));
                    whitelist.setCardno(usrs[i].substring(6, 6 + 9));
                    whitelist.setDeadCardno(usrs[i].substring(6 + 9, 6 + 9 + 16));
                    whitelist.setLevel(usrs[i].substring(6 + 9 + 16, 6 + 9 + 16 + 1));
                    whitelist.setPassword(usrs[i].substring(6 + 9 + 16 + 1, 6 + 9 + 16 + 1 + 6));
                    Log.i("流程", "单个白名单：" + whitelist.toString());
                    whitelists.add(whitelist);
                } catch (Exception e) {
                    MiLog.i("流程", "白名单单个解析失败：" + usrs[i]);
                }
            }
            if (style.equals("0")) {
                DBManagerZB.deleteWhite();
            }
            DBManagerZB.insertWhite(whitelists);
        } catch (Exception e) {
            MiLog.i("流程", "白名单解析失败");
        }
    }
}