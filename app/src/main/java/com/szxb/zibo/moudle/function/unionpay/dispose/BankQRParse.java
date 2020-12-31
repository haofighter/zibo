package com.szxb.zibo.moudle.function.unionpay.dispose;


import android.util.Log;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.BankScanPay;
import com.szxb.java8583.module.manager.BusllPosManage;

import com.szxb.lib.Util.FileUtils;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.moudle.function.unionpay.UnionPay;
import com.szxb.zibo.moudle.function.unionpay.UnionUtil;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.moudle.function.unionpay.entity.UnionPayEntity;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.record.RecordUpload;
import com.szxb.zibo.record.XdRecord;

import static com.szxb.zibo.util.DateUtil.getCurrentDate;


/**
 * 作者：Tangren on 2018-09-11
 * 包名：com.szxb.unionpay.dispose
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class BankQRParse {
    synchronized public BankResponse parseResponse(int amount, String qrCode) throws Exception {

        BusllPosManage.getPosManager().setTradeSeq();
        //同步保存记录

        SyncSSLRequest syncSSLRequest = new SyncSSLRequest();
        Iso8583Message iso8583Message = BankScanPay.getInstance()
                .qrPayMessage(qrCode, amount, BusllPosManage.getPosManager().getTradeSeq(), BusllPosManage.getPosManager().getMacKey());
        saveQRUnionPayEntity(amount, qrCode, iso8583Message.getBytes());
        Log.i("银联", "二维码交易" + "    流水号：" + BusllPosManage.getPosManager().getTradeSeq() + "    金额：" + amount + "       二维码：" + qrCode);
        BankResponse response = syncSSLRequest.request(UnionUtil.PAY_TYPE_BANK_QR, iso8583Message.getBytes());
        return response;
    }

    private void saveQRUnionPayEntity(int amount, String qr, byte[] sendData) {
        XdRecord xdRecord = new XdRecord();
        String unionpos = FileUtils.formatHexStringToByteString(4, BusllPosManage.getPosManager().getPosSn());
        String batchNum = FileUtils.formatHexStringToByteString(3, FileUtils.getSHByte(BusllPosManage.getPosManager().getBatchNum()));
        String tradeSeq = FileUtils.getSHByte(FileUtils.formatHexStringToByteString(3, Integer.toHexString(BusllPosManage.getPosManager().getTradeSeq())));
        String extraDate = FileUtils.bytesToHexString(sendData);
        xdRecord.setExtraDate(extraDate);
        xdRecord.setNewExtraDate(unionpos + batchNum + tradeSeq + FileUtils.bytesToHexString("DD".getBytes()));
        xdRecord.setTradePay(amount);
        xdRecord.setTradeType("07");

        xdRecord.setRes3(String.format("%06d", BusllPosManage.getPosManager().getTradeSeq()));
        xdRecord.setRes2(qr);
        xdRecord.setRes4(BusllPosManage.getPosManager().getBatchNum());
        xdRecord.setUseCardnum(qr);
        xdRecord.setRecordVersion("0003");
        xdRecord.setMainCardType("41");
        xdRecord.setChildCardType("00");
        xdRecord.setUnionPayStatus("408");
        xdRecord.setFlag(String.format("%06d", BusllPosManage.getPosManager().getTradeSeq()) + BusllPosManage.getPosManager().getBatchNum());
        xdRecord.setCarNum(BusApp.getPosManager().getBusNo());
        RecordUpload.saveRecord(xdRecord, false, false);
    }
}
