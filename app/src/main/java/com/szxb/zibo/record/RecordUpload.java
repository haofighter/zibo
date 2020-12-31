package com.szxb.zibo.record;

import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.module.BankPay;
import com.szxb.java8583.module.BusCard;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.config.zibo.RequestDate.PosInfoDate;
import com.szxb.zibo.config.zibo.RequestDate.RequestParam;
import com.szxb.zibo.moudle.function.unionpay.UnionPay;
import com.szxb.zibo.moudle.function.unionpay.config.UnionConfig;
import com.szxb.zibo.moudle.function.unionpay.entity.UnionPayEntity;
import com.szxb.zibo.net.JsonRequest;
import com.szxb.zibo.net.NetUrl;
import com.szxb.zibo.util.DateUtil;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordUpload {
    public static void saveRecord(XdRecord xdRecord) {
        xdRecord.setCarNum(BusApp.getPosManager().getBusNo());
        saveRecord(xdRecord, true, false);
    }

    public static void saveRecord(XdRecord xdRecord, boolean isDriver) {
        xdRecord.setCarNum(BusApp.getPosManager().getBusNo());
        xdRecord.setLineNum(BusApp.getPosManager().getLineNo());
        saveRecord(xdRecord, true, isDriver);
    }


    public static void saveRecord(XdRecord xdRecord, boolean isUpLoad, boolean isDriver) {
        if (isDriver) {
            xdRecord.setRecordBigType("62");
        } else {
            xdRecord.setRecordBigType("1f");
            xdRecord.setRecordSmallType("01");
        }
        if (xdRecord.getTradeNum().equals("00000000")) {
            xdRecord.setTradeNum(BusApp.getPosManager().getmchTrxId());
        }
        xdRecord.setIndustryType("01");
//        xdRecord.setPayType(xdRecord.status.equals("00") ? "FD" : "DD");//DD:正常灰交易 FD:正常交易
        xdRecord.setMerchantNum(BusApp.getPosManager().getMchID());
        xdRecord.setCompnayNum(BusApp.getPosManager().getCompanyID());
        xdRecord.setUnionNum(BusApp.getPosManager().getUnitno());

        xdRecord.setStationNum(BusApp.getPosManager().getStationID());
        xdRecord.setLongitude((BusApp.getPosManager().getLocation()[0] + "").replace(".", ""));
        xdRecord.setLatitude((BusApp.getPosManager().getLocation()[1] + "").replace(".", ""));
        xdRecord.setPosSn(BusApp.getPosManager().getPosSN());

//        xdRecord.setTradePSAM(praseConsumCard.);//TODO 当前交易的PSAM
        xdRecord.setDriverNum(BusApp.getPosManager().getDriverNo());
        xdRecord.setDirection(BusApp.getPosManager().getDirection());
        int lenth = FileUtils.hex2byte(xdRecord.toString()).length / 128;
        if (FileUtils.hex2byte(xdRecord.toString()).length / 128 > 0) {
            lenth += 1;
        }
        xdRecord.setRecordLenth(FileUtils.bytesToHexString(FileUtils.int2byte(lenth)));
        Log.i("刷卡", "记录保存   车号：" + xdRecord.getCarNum() + "      线路：" + xdRecord.getLineNum());
        DBManagerZB.saveRecord(xdRecord);
        if (isUpLoad) {
            try {
                RecordUpload.uploadRecord(xdRecord, isDriver);
            } catch (Exception e) {
                Log.i("上送数据报错", "错误：" + e.getMessage());
            }
        }
    }


    //刷卡记录上送
    public static void upLoadCardRecord() throws Exception {
        XdRecord xdRecord = DBManagerZB.checkRecordNoUp();
        if (xdRecord != null) {
            MiLog.i("记录", "上送：" + xdRecord.getStatus());
            uploadRecord(xdRecord, false);
        }
        XdRecord xdRecordErr = DBManagerZB.checkRecordNoUpErr();
        if (xdRecordErr != null) {
            MiLog.i("记录", "上送：" + xdRecordErr.getStatus());
            uploadRecord(xdRecordErr, false);
        }
    }

    public static void uploadRecord(final XdRecord xdRecord) {
        uploadRecord(xdRecord, false);
    }

    /**
     * 上传记录信息
     *
     * @param xdRecord
     * @param isDriver 是否为管理卡
     */
    public static void uploadRecord(final XdRecord xdRecord, final boolean isDriver) {
        MiLog.i("刷卡", "上传记录：" + xdRecord.getStatus());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JsonRequest macRequest = new JsonRequest(NetUrl.RECORD_UPLOAD);
                    PosInfoDate posInfoDate = new PosInfoDate();
                    posInfoDate.setTradetype("1");//0:司机签到签退 1:IC 卡交易 2:二维码
                    posInfoDate.setRecord_mark(xdRecord.getCreatTime() + xdRecord.getTradeNum());
                    posInfoDate.setCollect_type("0");
                    posInfoDate.setTrans_size(FileUtils.hexStringToBytes(xdRecord.toString()).length + "");
                    if (isDriver) {
                        posInfoDate.setRecord_data(xdRecord.toDriverString().toUpperCase());
                    } else {
                        posInfoDate.setRecord_data(xdRecord.toString().toUpperCase());
                    }
                    macRequest.setDefineRequestBodyForJson(RequestParam.getRequestParam(posInfoDate, "2"));
                    Long time = System.currentTimeMillis();
                    MiLog.i("记录", "uploadRecord：" + time + "请求" + RequestParam.getRequestParam(posInfoDate, "2"));
                    Response<JSONObject> execute = SyncRequestExecutor.INSTANCE.execute(macRequest);
                    JSONObject json = execute.get();
                    String result = "";
                    if (json != null) {
                        result = json.toString();
                    }
                    MiLog.i("记录", "uploadRecord：" + time + "返回" + result);
                    if (result.contains("请求成功")) {
                        DBManagerZB.updateXdRecord(xdRecord);
                    }
                } catch (Exception e) {
                    MiLog.i("错误", "记录uploadRecord：" + e.getMessage());
                }
            }
        }).start();
    }

    public static void checkUnionODA() throws Exception {
        UnionPayEntity unionPayEntities = DBManagerZB.checkUnionODA();
        if (!BusApp.unionIsSign || unionPayEntities == null) {
            return;
        }

        BusCard busCard = new BusCard();
        busCard.setMainCardNo(unionPayEntities.getMainCardNo());
        busCard.setCardNum(unionPayEntities.getCardNum());
        busCard.setMagTrackData(unionPayEntities.getCardData());
        busCard.setTlv55(unionPayEntities.getTlv55());
        busCard.setMacKey(BusllPosManage.getPosManager().getMacKey());
        busCard.setMoney(unionPayEntities.getTotalFee());
        busCard.setTradeSeq(unionPayEntities.getTradeSeq());

        Iso8583Message iso8583Message = BankPay.getInstance().payMessage(busCard, false);
        byte[] sendData = iso8583Message.getBytes();

        unionPayEntities.setUniqueFlag(FileUtils.polishString(busCard.getTradeSeq() + "", 6) + BusllPosManage.getPosManager().getBatchNum());
        //DBManagerZB.insertUnionPayRecord(unionPayEntities);
        //TODO 修改数据
        if (BusApp.getInstance().getNetWorkState()) {
            Log.i("ODA历史交易", "交易" + unionPayEntities.getMainCardNo() + "       " + unionPayEntities.getTradeSeq() + "         " + unionPayEntities.getBatchNum());
            UnionPay.getInstance().exeSSL(UnionConfig.PAY, sendData, false);
        }
    }

    static int recordSaveNum = 3000;

    //将超过5000条的数据 转换成文件进行储存
    public static void clearDateBase() {
        try {
            long allcount = DBManagerZB.checkXdRecordUpload();
            MiLog.i("数据清理", "记录条数：" + allcount);
            if (allcount > recordSaveNum) {
                List<XdRecord> scanRecordEntities = DBManagerZB.checkXdRecordUploadList();
                String scanRecord = new Gson().toJson(scanRecordEntities);
                FileUtils.saveStrToFile(scanRecord, new File(Environment.getExternalStorageDirectory() + "/Record/xd_" + DateUtil.getCurrentDate2() + "_record.txt"));
                DBManagerZB.deleteXdRecord();
            }


            File file = new File(Environment.getExternalStorageDirectory() + "/Record");
            List<File> remove = new ArrayList<>();
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    String[] strs = files[i].getName().split("_");
                    long date = Long.parseLong(strs[1]);//yyyyMMddHHmmss  转为int  20190621000000
                    long now = Long.parseLong(DateUtil.getCurrentDate2());//       20190721000000
                    if (now - date > 100000000) {
                        remove.add(files[i]);
                    }
                }
            }
            for (int i = 0; i < remove.size(); i++) {
                Log.i("文件清理", remove.get(i).getName());
                remove.get(i).delete();
            }
        } catch (Exception e) {
            Log.i("错误", "clearDateBase(RecordUpload.java:207) 数据清除错误" + e.getMessage());
        }
    }


}
