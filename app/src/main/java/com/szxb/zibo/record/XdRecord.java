package com.szxb.zibo.record;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.Type;
import com.szxb.jni.SerialCom;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.util.Util;
import com.szxb.zibo.util.md5.Crc;
import com.szxb.zibo.util.md5.MD5;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.File;

import static java.lang.System.arraycopy;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;


@Entity
public class XdRecord {

    /**
     * 交易记录标示 唯一
     */
    @Unique
    private String recordTag;

    public void setRecordTag(String recordTag) {
        this.recordTag = recordTag;
    }


    /**
     * 记录长度标示  1 标识本条记录的长度 legnth/128
     */
    private String recordLenth;

    private String flag;//用做记录查询的唯一标示

    public void setRecordLenth(String recordLenth) {
        this.recordLenth = FileUtils.formatHexStringToByteString(1, recordLenth);
    }


    /**
     * 交易记录版本
     * 2
     * 0x01
     */
    private String recordVersion;

    public void setRecordVersion(String recordVersion) {
        this.recordVersion = FileUtils.formatHexStringToByteString(2, recordVersion);
    }

    /**
     * 交易大类
     * 1
     */
    private String recordBigType;


    public void setRecordBigType(String recordBigType) {
        this.recordBigType = FileUtils.formatHexStringToByteString(1, recordBigType);
    }


    /**
     * 交易小类
     * 1
     * 0x01钱包消费 0x02月票消费 0x03黑名单锁卡交易
     */
    private String recordSmallType;

    public void setRecordSmallType(String recordSmallType) {
        this.recordSmallType = FileUtils.formatHexStringToByteString(1, recordSmallType);
    }

    /**
     * 卡介质体系
     * 1
     * 0x01:本地 M1 0x02:本地 CPU 0x03:城联一卡通 0x04:交通部卡 0x05:支付宝乘车码 0x06:微信乘车码 0x07:银联二维码 0x08:银联双免
     */
    private String tradeType;


    public void setTradeType(String tradeType) {
        this.tradeType = FileUtils.formatHexStringToByteString(1, tradeType);
    }

    /**
     * 行业类型 0x00:客服
     * 1
     * 0x01:公交
     */
    private String industryType;


    public void setIndustryType(String industryType) {
        this.industryType = FileUtils.formatHexStringToByteString(1, industryType);
    }

    /**
     * 交易标示
     * 1
     * DD:正常灰交易 FD:正常交易
     */
    private String payType;


    public void setPayType(String payType) {
        this.payType = FileUtils.formatHexStringToByteString(1, payType);
    }

    /**
     * 商户编号
     * 10
     * ASC 不足前补F
     */
    private String merchantNum;


    public void setMerchantNum(String merchantNum) {
//        this.merchantNum = FileUtils.hexStringFromatByF(10, FileUtils.asciiToHex(merchantNum));
        this.merchantNum = FileUtils.hexStringFromatByF(10, "");
    }


    /**
     * 公司编号
     * 10
     * ASC 不足前补F
     */
    private String companyNum;


    public void setCompnayNum(String comnayNum) {
//        this.companyNum = FileUtils.hexStringFromatByF(10, FileUtils.asciiToHex(comnayNum));
        this.companyNum = FileUtils.hexStringFromatByF(10, "");
    }

    /**
     * 分公司编号
     * 10
     * ASC 不足前补F
     */
    private String unionNum;


    public void setUnionNum(String unionNum) {
//        this.unionNum = FileUtils.hexStringFromatByF(10, FileUtils.asciiToHex(unionNum));
        this.unionNum = FileUtils.hexStringFromatByF(10, "");
    }

    /**
     * 线路编号
     * 10
     * ASC 不足前补F
     */
    private String lineNum;


    public void setLineNum(String lineNum) {
        this.lineNum = FileUtils.hexStringFromatByF(10, FileUtils.asciiToHex(lineNum));
    }

    private String carNum;

    /**
     * 车辆编号 10
     * ASC 不足前补F
     */
    public void setCarNum(String carNum) {
        this.carNum = FileUtils.hexStringFromatByF(10, FileUtils.asciiToHex(carNum));
    }

    private String stationNum;

    /**
     * 站点编号  1 bin
     */
    public void setStationNum(int stationNum) {
//        long tradeCount = FileUtils.hexStringToInt(stationNum);
//        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte2(tradeCount), Type.HEX);
        String str = Integer.toHexString(stationNum);
        this.stationNum = FileUtils.formatHexStringToByteString(1, str);
    }

    public void setStationNum(String stationNum) {
        this.stationNum = FileUtils.formatHexStringToByteString(1, stationNum);
    }

    private String longitude;

    /**
     * 当前经度  4 bin
     */
    public void setLongitude(String longitude) {
        if (longitude.length() > 8) {
            longitude = longitude.substring(0, 8);
        }
        long tradeCount = FileUtils.hexStringToInt(longitude);
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte(tradeCount), Type.HEX);
        this.longitude = FileUtils.formatHexStringToByteString(4, str);
    }

    private String latitude;

    /**
     * 当前纬度  4 bin
     */
    public void setLatitude(String latitude) {
        if (latitude.length() > 8) {
            latitude = latitude.substring(0, 8);
        }
        long tradeCount = FileUtils.hexStringToInt(latitude);
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte(tradeCount), Type.HEX);
        this.latitude = FileUtils.formatHexStringToByteString(4, str);
    }

    private String posSn;

    /**
     * POS机编号 20ASC
     */
    public void setPosSn(String posSn) {
        this.posSn = FileUtils.hexStringFromatByF(20, FileUtils.asciiToHex(posSn));
    }

    private String mainPSAM;

    /**
     * 主卡PSAM编号 6HEX
     */
    public void setMainPSAM(String mainPSAM) {
        this.mainPSAM = FileUtils.formatHexStringToByteString(6, mainPSAM);
    }

    private String tradePSAM;

    /**
     * 交易PSAM编号 6HEX
     */
    public void setTradePSAM(String tradePSAM) {
        this.tradePSAM = FileUtils.formatHexStringToByteString(6, tradePSAM);
    }

    private String driverNum;

    /**
     * 司机编号  10BCD
     */
    public void setDriverNum(String driverNum) {
        String dr = (String) FileUtils.byte2Parm(FileUtils.hexStringToBytes(driverNum), Type.BCDW);
        while (dr.length() < 20) {
            dr = "0" + dr;
        }
        this.driverNum = dr;
    }

    private String useCardnum;

    /**
     * 应用卡号  10HEX
     */
    public void setUseCardnum(String useCardnum) {
        this.useCardnum = FileUtils.formatHexStringToByteString(10, useCardnum);
    }

    private String tradeTime;

    /**
     * 交易时间  7HEX
     */
    public void setTradeTime(String tradeTime) {
        this.tradeTime = FileUtils.formatHexStringToByteString(7, tradeTime);
        MiLog.i("刷卡", "记录的交易时间：" + tradeTime);
    }

    public String getTradeTime() {
        return tradeTime;
    }

    private String tradeNum;

    /**
     * 终端交易序号  4 bin
     */
    public void setTradeNum(int tradeNum) {
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte(tradeNum), Type.HEX);
        this.tradeNum = FileUtils.formatHexStringToByteString(4, str);
        MiLog.i("刷卡", "终端交易序号" + tradeNum + "");
    }

    public String getTradeNum() {
        return tradeNum;
    }

    private String tradePay = "0000";

    /**
     * 交易金额  4
     * bin 多票二维码传基本票价后折扣金额
     */


    public void setTradePay(long tradePay) {
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte(tradePay), Type.HEX);
        this.tradePay = FileUtils.formatHexStringToByteString(4, str);
    }

    private String tradePayNum;

    /**
     * 交易值   4
     * bin 多票二维码传基本票价
     */


    public void setTradePayNum(int tradePayNum) {
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte(tradePayNum), Type.HEX);
        this.tradePayNum = FileUtils.formatHexStringToByteString(4, str);
    }

    public void setTradePayNum(String tradePayNum) {
        this.tradePayNum = FileUtils.formatHexStringToByteString(4, tradePayNum);
    }


    private String tradeDiscount;

    /**
     * 交易后卡余额 4  bin
     */


    public void setTradeDiscount(long tradeDiscount) {
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte(tradeDiscount), Type.HEX);
        this.tradeDiscount = FileUtils.formatHexStringToByteString(4, str);
    }

    public void setTradeDiscount(String tradeDiscount) {
        this.tradeDiscount = FileUtils.formatHexStringToByteString(4, tradeDiscount);
    }

    private String cardTradeCount;

    /**
     * 卡交易序号  2  bin
     */
    public void setCardTradeCount(String cardTradeCount) {
        int tradeCount = FileUtils.hexStringToInt(FileUtils.getSHByte(cardTradeCount));
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte2(tradeCount), Type.HEX);
        this.cardTradeCount = FileUtils.formatHexStringToByteString(2, str);
    }

    private String cardTradeTAC;

    /**
     * 交易TAC码  4HEX
     */


    public void setCardTradeTAC(String cardTradeTAC) {
        this.cardTradeTAC = FileUtils.formatHexStringToByteString(4, cardTradeTAC);
    }

    private String samTradeCount;

    /**
     * Sam卡交易序号  4  bin
     */


    public void setSamTradeCount(String samTradeCount) {
        this.samTradeCount = FileUtils.formatHexStringToByteString(4, FileUtils.getSHByte(samTradeCount));
    }

    private String cityCode;

    /**
     * 城市代码  2HEX
     */


    public void setCityCode(String cityCode) {
        this.cityCode = FileUtils.formatHexStringToByteString(2, cityCode);
        if (this.cityCode.equals("0000")) {
            this.cityCode = "2550";
        }
    }

    private String creatCardMechanism;

    /**
     * 发卡/码机构代码  5
     * HEX 不足前补
     * F
     */


    public void setCreatCardMechanism(String creatCardMechanism) {
        this.creatCardMechanism = FileUtils.formatHexStringToByteString(5, creatCardMechanism);
    }

    private String mainCardType;

    /**
     * 主卡类型  1HEX
     */


    public void setMainCardType(String mainCardType) {
        this.mainCardType = FileUtils.formatHexStringToByteString(1, mainCardType);
    }

    private String childCardType;

    /**
     * 子卡类型  1HEX
     */


    public void setChildCardType(String childCardType) {
        this.childCardType = FileUtils.formatHexStringToByteString(1, childCardType);
    }

    private String direction;

    /**
     * 上下行  1HEX  0x01上行 0x02下行 0x00一票
     */


    public void setDirection(String direction) {
        this.direction = FileUtils.formatHexStringToByteString(1, direction);
    }

    private String inCardStatus;

    /**
     * 上下车  1HEX  0x01上车(多票二维码统一传01) 0x02下车 0x03补扣消费 0x00一票
     */


    public void setInCardStatus(int inCardStatus) {
        this.inCardStatus = FileUtils.formatHexStringToByteString(1, FileUtils.bytesToHexString(FileUtils.int2byte2(inCardStatus)));
    }

    public void setInCardStatus(String inCardStatus) {
        this.inCardStatus = FileUtils.formatHexStringToByteString(1, inCardStatus);
    }

    private String beforTradePosSn;

    /**
     * 前次终端代号  6 HEX 交易终端号
     */


    public void setBeforTradePosSn(String beforTradePosSn) {
        this.beforTradePosSn = FileUtils.formatHexStringToByteString(6, beforTradePosSn);
    }

    private String beforTradeType;

    /**
     * 前次交易类型  1HEX
     */


    public void setBeforTradeType(String beforTradeType) {
        this.beforTradeType = FileUtils.formatHexStringToByteString(1, beforTradeType);
    }

    private String beforTradeTime;

    /**
     * 前次交易时间  7HEX
     */


    public void setBeforTradeTime(String beforTradeTime) {
        this.beforTradeTime = FileUtils.formatHexStringToByteString(7, beforTradeTime);
    }

    private String beforTradePrice;

    /**
     * 前次交易金额   4  bin
     */


    public void setBeforTradePrice(String beforTradePrice) {
        int tradeCount = FileUtils.hexStringToInt(beforTradePrice);
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte(tradeCount), Type.HEX);
        this.beforTradePrice = FileUtils.formatHexStringToByteString(4, str);
    }

    private String changeLineNum;

    /**
     * 换乘线路编号  10ASC
     */


    public void setChangeLineNum(String changeLineNum) {
        this.changeLineNum = FileUtils.formatHexStringToByteString(10, FileUtils.asciiToHex(changeLineNum));
    }

    private String changePosSn;

    /**
     * 换乘终端代号  6
     * HEX 交易终端号
     */


    public void setChangePosSn(String changePosSn) {
        this.changePosSn = FileUtils.formatHexStringToByteString(6, changePosSn);
    }

    /**
     * 换乘最近使用日期时间  7
     * HEX 交易终端号
     */
    private String changeNearTime;


    public void setChangeNearTime(String changeNearTime) {
        this.changeNearTime = FileUtils.formatHexStringToByteString(7, changeNearTime);
    }

    /**
     * 换乘优惠金额  2HEX
     */
    private String changePayPrice;


    public void setChangePayPrice(String changePayPrice) {
        this.changePayPrice = FileUtils.formatHexStringToByteString(2, changePayPrice);
    }

    /**
     * 附加数据长度  2
     * HEX 附加交易真实长度，后补 0不算
     */
    private String extraDateLenth;


    public void setExtraDateLenth(int extraDateLenth) {
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte(extraDateLenth), Type.HEX);
        this.extraDateLenth = str.substring(0, 4);
    }

    /**
     * 附加数据内容 N
     * HEX 总长度满足 128的倍数，
     * 满足不了 就在附加交易后补 0
     */

    private String extraDate;

    private String newExtraDate;


    @Unique
    private long creatTime;

    public long getCreatTime() {
        return creatTime;
    }

    public void setExtraDate(String extraDate) {
        if (!extraDate.equals("")) {
            setExtraDateLenth(FileUtils.hexStringToBytes(extraDate).length);
        }
        this.extraDate = extraDate;
    }

    /**
     * 上传状态
     * 0 未上传  1 已上传  2 补传
     */
    String updateFlag = "0";

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    /**
     * 二维码
     */
    String qrCode;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * 刷卡的状态
     */
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updateFlag = "0";
    }

    /**
     * 卡交易前余额
     */
    long balance;

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    /**
     * 提示语音的类型 取自组装命令是的cardinfo中的tradeType
     */
    int voiceType;

    public int getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(int voiceType) {
        this.voiceType = voiceType;
    }

    /**
     * 发送的消费命令
     */
    String payCommand;

    public String getPayCommand() {
        return payCommand;
    }

    public void setPayCommand(String payCommand) {
        this.payCommand = payCommand;
    }

    /**
     * 刷卡前卡中的交易序号
     */
    String lastTradeCount;


    public  void praseDate(String str) {
        int i = 0;
        recordLenth = str.substring(0, i = +2);
        recordVersion = str.substring(i, i += 4);
        recordBigType = str.substring(i, i += 2);
        recordSmallType = str.substring(i, i += 2);
        tradeType = str.substring(i, i += 2);
        industryType = str.substring(i, i += 2);
        payType = str.substring(i, i += 2);
        merchantNum = str.substring(i, i += 20);
        companyNum = str.substring(i, i += 20);
        unionNum = str.substring(i, i += 20);
        lineNum = str.substring(i, i += 20);
        carNum = str.substring(i, i += 20);
        stationNum = str.substring(i, i += 2);
        longitude = str.substring(i, i += 8);
        latitude = str.substring(i, i += 8);
        posSn = str.substring(i, i += 40);
        mainPSAM = str.substring(i, i += 12);
        tradePSAM = str.substring(i, i += 12);
        driverNum = str.substring(i, i += 20);
        useCardnum = str.substring(i, i += 20);
        tradeTime = str.substring(i, i += 14);
        tradeNum = str.substring(i, i += 8);
        tradePay = str.substring(i, i += 8);
        tradePayNum = str.substring(i, i += 8);
        tradeDiscount = str.substring(i, i += 8);
        cardTradeCount = str.substring(i, i += 4);
        cardTradeTAC = str.substring(i, i += 8);
        samTradeCount = str.substring(i, i += 8);
        cityCode = str.substring(i, i += 4);
        creatCardMechanism = str.substring(i, i += 10);
        mainCardType = str.substring(i, i += 2);
        childCardType = str.substring(i, i += 2);
        direction = str.substring(i, i += 2);
        inCardStatus = str.substring(i, i += 2);
        beforTradePosSn = str.substring(i, i += 12);
        beforTradeType = str.substring(i, i += 2);
        beforTradeTime = str.substring(i, i += 14);
        beforTradePrice = str.substring(i, i += 8);

        Log.i("解析完成","");
    }


    public String fromateString(boolean isDriver) {
        String hexString = recordLenth;
        hexString += recordVersion;
        hexString += recordBigType;
        hexString += recordSmallType;
        hexString += tradeType;
        hexString += industryType;
        hexString += payType;
        hexString += merchantNum;
        hexString += companyNum;
        hexString += unionNum;
        hexString += lineNum;
        hexString += carNum;
        hexString += stationNum;
        hexString += longitude;
        hexString += latitude;
        hexString += posSn;
        hexString += mainPSAM;
        hexString += tradePSAM;
        hexString += driverNum;
        hexString += useCardnum;
        hexString += tradeTime;
        hexString += tradeNum;
        hexString += tradePay;
        hexString += tradePayNum;
        if (!isDriver) {
            hexString += tradeDiscount;
        }
        hexString += cardTradeCount;
        hexString += cardTradeTAC;
        hexString += samTradeCount;
        hexString += cityCode;
        hexString += creatCardMechanism;
        hexString += mainCardType;
        hexString += childCardType;
        hexString += direction;
        hexString += inCardStatus;
        hexString += beforTradePosSn;
        hexString += beforTradeType;
        hexString += beforTradeTime;
        hexString += beforTradePrice;
        if (!isDriver) {
            hexString += changeLineNum;
            hexString += changePosSn;
            hexString += changeNearTime;
            hexString += changePayPrice;
        }
        hexString += extraDateLenth;
        hexString += extraDate;
        int size = FileUtils.hexStringToBytes(hexString).length;
        byte[] date;
        if ((size + 2 + newExtraDate.length() / 2) % 128 == 0) {
            date = new byte[size];
        } else {
            date = new byte[((size + 2 + newExtraDate.length() / 2) / 128 + 1) * 128 - 2 - newExtraDate.length() / 2];
        }
        arraycopy(FileUtils.hexStringToBytes(hexString), 0, date, 0, FileUtils.hex2byte(hexString).length);
        hexString = FileUtils.bytesToHexString(date);

        hexString += newExtraDate;
        byte[] hexS = FileUtils.hexStringToBytes(hexString);
        String crc = FileUtils.bytesToHexString(FileUtils.int2byte(Crc.CRC16_IBM(hexS))).substring(0, 4);
        hexString += FileUtils.formatHexStringToByteString(2, crc);

        return hexString;
    }

    @Override
    public String toString() {
        String date = fromateString(false);
        byte[] length = FileUtils.int2byte2(FileUtils.hex2byte(date).length / 128);
        String str = FileUtils.bytesToHexString(length);
        setRecordLenth(str);
        return fromateString(false);
    }

    public String toDriverString() {
        String date = fromateString(true);
        byte[] length = FileUtils.int2byte2(FileUtils.hex2byte(date).length / 128);
        String str = FileUtils.bytesToHexString(length);
        setRecordLenth(str);
        return fromateString(true);
    }


    public String getRecordLenth() {
        return this.recordLenth;
    }

    public String getRecordVersion() {
        return this.recordVersion;
    }

    public String getRecordBigType() {
        return this.recordBigType;
    }

    public String getRecordSmallType() {
        return this.recordSmallType;
    }

    public String getTradeType() {
        return this.tradeType;
    }

    public String getIndustryType() {
        return this.industryType;
    }

    public String getPayType() {
        return this.payType;
    }

    public String getMerchantNum() {
        return this.merchantNum;
    }

    public String getCompanyNum() {
        return this.companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getUnionNum() {
        return this.unionNum;
    }

    public String getLineNum() {
        return this.lineNum;
    }

    public String getCarNum() {
        return this.carNum;
    }

    public String getStationNum() {
        return this.stationNum;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getPosSn() {
        return this.posSn;
    }

    public String getMainPSAM() {
        return this.mainPSAM;
    }

    public String getTradePSAM() {
        return this.tradePSAM;
    }

    public String getDriverNum() {
        return this.driverNum;
    }

    public String getUseCardnum() {
        return this.useCardnum;
    }

    public String getTradePay() {
        return this.tradePay;
    }

    public String getTradePayNum() {
        return this.tradePayNum;
    }

    public String getTradeDiscount() {
        return this.tradeDiscount;
    }

    public String getCardTradeCount() {
        return this.cardTradeCount;
    }

    public String getCardTradeTAC() {
        return this.cardTradeTAC;
    }

    public String getSamTradeCount() {
        return this.samTradeCount;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public String getCreatCardMechanism() {
        return this.creatCardMechanism;
    }

    public String getMainCardType() {
        return this.mainCardType;
    }

    public String getChildCardType() {
        return this.childCardType;
    }

    public String getDirection() {
        return this.direction;
    }

    public String getInCardStatus() {
        return this.inCardStatus;
    }

    public String getBeforTradePosSn() {
        return this.beforTradePosSn;
    }

    public String getBeforTradeType() {
        return this.beforTradeType;
    }

    public String getBeforTradeTime() {
        return this.beforTradeTime;
    }

    public String getBeforTradePrice() {
        return this.beforTradePrice;
    }

    public String getChangeLineNum() {
        return this.changeLineNum;
    }

    public String getChangePosSn() {
        return this.changePosSn;
    }

    public String getChangeNearTime() {
        return this.changeNearTime;
    }

    public String getChangePayPrice() {
        return this.changePayPrice;
    }

    public String getExtraDateLenth() {
        return this.extraDateLenth;
    }

    public String getExtraDate() {
        return this.extraDate;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public void setTradePay(String tradePay) {
        this.tradePay = tradePay;
    }

    public String getRecordTag() {
        return recordTag;
    }

    public void setExtraDateLenth(String extraDateLenth) {
        this.extraDateLenth = extraDateLenth;
    }

    public XdRecord() {
        setStatus("99");
        setTradeNum(0);
        setRecordTag(Util.Random(5) + System.currentTimeMillis());
        setRecordLenth("");
        setTradeType("");
        setTradePSAM("");
        setUseCardnum("");
        setTradePay(0);
        setTradeDiscount(0);
        setCardTradeCount("0");
        setCardTradeTAC("");
        setSamTradeCount("00");
        setCreatCardMechanism("");
        setMainCardType("");
        setChildCardType("");
        setDirection("");
        setInCardStatus(0);
        setBeforTradePosSn("");
        setBeforTradeType("");
        setBeforTradeTime("");
        setBeforTradePrice("");
        setChangeLineNum("");
        setChangePosSn("");
        setChangeNearTime("");
        setChangePayPrice("");
        setExtraDateLenth(0);
        setExtraDate("");
        setRecordVersion("01");
        setRecordBigType("1f");
        setRecordSmallType("01");
        setIndustryType("01");
        setPayType("DD");//DD:正常灰交易 FD:正常交易
        setMerchantNum(BusApp.getPosManager().getMchID());
        setCompnayNum(BusApp.getPosManager().getCompanyID());
        setUnionNum(BusApp.getPosManager().getUnitno());
        setLineNum(BusApp.getPosManager().getLineNo());
        setCarNum(BusApp.getPosManager().getBusNo());
        setStationNum(BusApp.getPosManager().getStationID());
        setLongitude((BusApp.getPosManager().getLocation()[0] + "").replace(".", ""));
        setLatitude((BusApp.getPosManager().getLocation()[1] + "").replace(".", ""));
        setPosSn(BusApp.getPosManager().getPosSN());
        setMainPSAM(BusApp.getPosManager().getMainPSAM());
        setDriverNum(BusApp.getPosManager().getDriverNo());
        setTradePayNum(BusApp.getPosManager().getBasePrice());//HEX 多票二维码传基本票价
        setCityCode(BusApp.getPosManager().getCityCode());
        setTradeTime(DateUtil.getCurrentDate2());
        setCreatTime(System.currentTimeMillis());
        setNewExtraDate("");
    }

    @Generated(hash = 2106004647)
    public XdRecord(String recordTag, String recordLenth, String flag, String recordVersion, String recordBigType,
                    String recordSmallType, String tradeType, String industryType, String payType, String merchantNum, String companyNum,
                    String unionNum, String lineNum, String carNum, String stationNum, String longitude, String latitude, String posSn,
                    String mainPSAM, String tradePSAM, String driverNum, String useCardnum, String tradeTime, String tradeNum,
                    String tradePay, String tradePayNum, String tradeDiscount, String cardTradeCount, String cardTradeTAC,
                    String samTradeCount, String cityCode, String creatCardMechanism, String mainCardType, String childCardType,
                    String direction, String inCardStatus, String beforTradePosSn, String beforTradeType, String beforTradeTime,
                    String beforTradePrice, String changeLineNum, String changePosSn, String changeNearTime, String changePayPrice,
                    String extraDateLenth, String extraDate, String newExtraDate, long creatTime, String updateFlag, String qrCode,
                    String status, long balance, int voiceType, String payCommand, String lastTradeCount) {
        this.recordTag = recordTag;
        this.recordLenth = recordLenth;
        this.flag = flag;
        this.recordVersion = recordVersion;
        this.recordBigType = recordBigType;
        this.recordSmallType = recordSmallType;
        this.tradeType = tradeType;
        this.industryType = industryType;
        this.payType = payType;
        this.merchantNum = merchantNum;
        this.companyNum = companyNum;
        this.unionNum = unionNum;
        this.lineNum = lineNum;
        this.carNum = carNum;
        this.stationNum = stationNum;
        this.longitude = longitude;
        this.latitude = latitude;
        this.posSn = posSn;
        this.mainPSAM = mainPSAM;
        this.tradePSAM = tradePSAM;
        this.driverNum = driverNum;
        this.useCardnum = useCardnum;
        this.tradeTime = tradeTime;
        this.tradeNum = tradeNum;
        this.tradePay = tradePay;
        this.tradePayNum = tradePayNum;
        this.tradeDiscount = tradeDiscount;
        this.cardTradeCount = cardTradeCount;
        this.cardTradeTAC = cardTradeTAC;
        this.samTradeCount = samTradeCount;
        this.cityCode = cityCode;
        this.creatCardMechanism = creatCardMechanism;
        this.mainCardType = mainCardType;
        this.childCardType = childCardType;
        this.direction = direction;
        this.inCardStatus = inCardStatus;
        this.beforTradePosSn = beforTradePosSn;
        this.beforTradeType = beforTradeType;
        this.beforTradeTime = beforTradeTime;
        this.beforTradePrice = beforTradePrice;
        this.changeLineNum = changeLineNum;
        this.changePosSn = changePosSn;
        this.changeNearTime = changeNearTime;
        this.changePayPrice = changePayPrice;
        this.extraDateLenth = extraDateLenth;
        this.extraDate = extraDate;
        this.newExtraDate = newExtraDate;
        this.creatTime = creatTime;
        this.updateFlag = updateFlag;
        this.qrCode = qrCode;
        this.status = status;
        this.balance = balance;
        this.voiceType = voiceType;
        this.payCommand = payCommand;
        this.lastTradeCount = lastTradeCount;
    }

    public String getLastTradeCount() {
        return lastTradeCount;
    }

    public void setLastTradeCount(String lastTradeCount) {
        int tradeCount = FileUtils.hexStringToInt(FileUtils.getSHByte(lastTradeCount));
        String str = (String) FileUtils.byte2Parm(FileUtils.int2byte2(tradeCount), Type.HEX);
        this.lastTradeCount = FileUtils.formatHexStringToByteString(2, str);
//        this.lastTradeCount = FileUtils.formatHexStringToByteString(2, FileUtils.getSHByte(lastTradeCount));
        MiLog.i("刷卡", "刷卡前卡交易序号：" + FileUtils.getSHByte(lastTradeCount));
    }

    public String getNewExtraDate() {
        return newExtraDate;
    }

    public void setNewExtraDate(String newExtraDate) {
        this.newExtraDate = newExtraDate;
    }


}
