package com.szxb.zibo.Mqtt;

import com.hao.lib.Util.FileUtils;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.record.XdRecord;

public class XdRecordUpload {
    String tenantNo;
    String mediaType;
    String reportType;
    String priority;
    RecordDate content;
    String createTime;


    public XdRecordUpload(XdRecord xdRecord) {
        this.tenantNo = BusApp.getPosManager().getPosSN();
        this.mediaType = "Q6-B";
        this.reportType = "";
        this.priority = "999";
        this.createTime = xdRecord.getTradeTime();
        content = new RecordDate();
        content.devNo = BusApp.getPosManager().getPosSN();
        content.recordNark = xdRecord.getCreatTime() + xdRecord.getTradeNum();
        content.collectType = "0";
        content.transSize = FileUtils.hexStringToBytes(xdRecord.toString()).length + "";
        content.recordData = xdRecord.toString();
    }
}
