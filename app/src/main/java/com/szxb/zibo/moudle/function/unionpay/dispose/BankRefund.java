package com.szxb.zibo.moudle.function.unionpay.dispose;

import android.text.TextUtils;
import android.util.Log;

import com.szxb.java8583.core.Iso8583Message;
import com.szxb.java8583.core.Iso8583MessageFactory;
import com.szxb.java8583.module.PosRefund;
import com.szxb.java8583.module.PosScanRefund;
import com.szxb.java8583.module.manager.BusllPosManage;
import com.szxb.java8583.quickstart.SingletonFactory;
import com.szxb.java8583.quickstart.special.SpecialField62;
import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.db.dao.UnionPayEntityDao;
import com.szxb.zibo.db.dao.XdRecordDao;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.moudle.function.unionpay.entity.UnionPayEntity;
import com.szxb.zibo.moudle.function.unionpay.http.BaseByteRequest;
import com.szxb.zibo.moudle.function.unionpay.http.CallServer;
import com.szxb.zibo.moudle.function.unionpay.http.HttpListener;
import com.szxb.zibo.moudle.function.unionpay.http.HttpResponseListener;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.apkmanage.AppUtil;
import com.szxb.zibo.util.DateUtil;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：Tangren on 2018-09-10
 * 包名：com.szxb.unionpay.dispose
 * 邮箱：996489865@qq.com
 * TODO:银联冲正（刷卡、二维码）
 */

public class BankRefund extends Thread {
    @Override
    public void run() {
        super.run();
        try {
//            if (!AppUtil.checkNetStatus()) {
            if (!AppUtil.isNetPingUsable()) {
                //如果无网络,停止本次上传
                return;
            }

            String[] time = DateUtil.time(1);
            XdRecordDao xdRecord = DBCore.getDaoSession().getXdRecordDao();
            List<XdRecord> list = xdRecord.queryBuilder()
                    .where(XdRecordDao.Properties.MainCardType.in("41", "42"))
                    .where(XdRecordDao.Properties.ChildCardType.in("00", "00"))
                    .where(XdRecordDao.Properties.UnionPayStatus.notIn("FD", "FF", "FE", "00", "A2", "A4", "A5", "A6"))
                    .where(XdRecordDao.Properties.CreatTime.lt(System.currentTimeMillis() - 2*60 * 1000))
                    .limit(5).build().list();
            MiLog.i("银联", "冲正数据条数：" + list.size());
            AtomicInteger what = new AtomicInteger(111);
            for (XdRecord payEntity : list) {

                Iso8583Message refund = getIso8583Message(payEntity);
                MiLog.i("银联", "数据冲正请求：" + refund.toFormatString());
                requestRefund(what.get(), refund.getBytes(), payEntity);
                what.getAndDecrement();
            }

        } catch (Exception e) {
            e.printStackTrace();
            MiLog.i("银联", "错误(run.java:76)" + e.toString());
        }
    }


    /**
     * 组装冲正数据
     *
     * @param xdRecord
     * @return
     */
    private Iso8583Message getIso8583Message(XdRecord xdRecord) {
        if (xdRecord.getMainCardType().equals("42") && xdRecord.getChildCardType().equals("00")) {
            MiLog.i("银联", "冲正，刷卡   卡号：" + xdRecord.getUseCardnum() + "   流水号：" + xdRecord.getRes3() + "       " + xdRecord.getRes4() + "  交易金额：" + Long.parseLong(FileUtils.strHto(xdRecord.getTradePay()), 16));
            return PosRefund.getInstance().refund(xdRecord.getRes2(), xdRecord.getRes1(), Integer.parseInt(xdRecord.getRes3()), xdRecord.getRes4(), "00", Integer.parseInt(FileUtils.strHto(xdRecord.getTradePay()), 16));
        } else if (xdRecord.getMainCardType().equals("41") && xdRecord.getChildCardType().equals("00")) {
            MiLog.i("银联", "冲正，二维码   卡号：" + xdRecord.getUseCardnum() + "   流水号：" + xdRecord.getRes3() + "  交易金额：" + Integer.parseInt(FileUtils.strHto(xdRecord.getTradePay())));
            return PosScanRefund.getInstance().refun(Integer.parseInt(xdRecord.getRes3()), xdRecord.getRes2(), "00", Integer.parseInt(FileUtils.strHto(xdRecord.getTradePay())));
        } else {
            return null;
        }
    }


    private void requestRefund(int what, byte[] sendData, XdRecord entity) {
        String url = BusllPosManage.getPosManager().getUnionPayUrl();
        BaseByteRequest request = new BaseByteRequest(url, RequestMethod.POST);
        InputStream stream = new ByteArrayInputStream(sendData);
        request.setDefineRequestBody(stream, "x-ISO-TPDU/x-auth");
        CallServer.getHttpclient().add(what, request, new HttpResponseXdListener(entity));
    }


    private class HttpResponseXdListener implements HttpListener<byte[]> {

        private XdRecord payEntity;

        public HttpResponseXdListener(XdRecord payEntity) {
            this.payEntity = payEntity;
        }

        @Override
        public void success(int what, Response<byte[]> response) {
            Iso8583MessageFactory factory = SingletonFactory.forQuickStart();
            factory.setSpecialFieldHandle(62, new SpecialField62());
            Iso8583Message message0810 = factory.parse(response.get());
            MiLog.i("银联", "冲正返回" + message0810.toFormatString());
            String value = message0810.getValue(39).getValue();
            if (value.equals("00") || value.equals("25") || value.equals("12")) {
                //冲正成功
                payEntity.setUnionPayStatus("FE");
                payEntity.setRes2(value);
                payEntity.setUpdateFlag("0");
                DBManagerZB.saveRecord(payEntity);
            } else {
                MiLog.i("银联", "冲正失败" + value);
                payEntity.setUpdateFlag("0");
                payEntity.setUnionPayStatus("FD");
                payEntity.setRes2(value);
                MiLog.i("银联", "冲正失败数据：" + payEntity.getCreatTime() + "    " + payEntity.getUnionPayStatus());
                DBManagerZB.saveRecord(payEntity);
            }
        }

        @Override
        public void fail(int what, String e) {
            Log.d("银联", "(fail.java:122)" + e);
        }
    }
}
