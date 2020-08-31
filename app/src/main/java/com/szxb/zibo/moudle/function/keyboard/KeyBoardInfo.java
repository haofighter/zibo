package com.szxb.zibo.moudle.function.keyboard;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class KeyBoardInfo {
    byte[] alldate;


    String lineNo;
    int stationId;
    String diraction;

    String backLineNo;
    int backstationId;
    String backDiraction;

    String stationKey;
    String synKey;
    String priceKey;


    public void setDate(byte[] key) {
        alldate = key;

        int i = 0;
        byte[] beat = new byte[1];
        arraycopy(key, i, beat, 0, beat.length);
        i += beat.length;

        byte[] lineNoLenth = new byte[1];
        arraycopy(key, i, lineNoLenth, 0, lineNoLenth.length);
        i += lineNoLenth.length;
//                    MiLog.i("接收站点数据", "lineNoLenth：" + FileUtils.bytesToHexString(lineNoLenth));

        byte[] lineNoNeed = new byte[9];
        arraycopy(key, i, lineNoNeed, 0, lineNoNeed.length);
        i += lineNoNeed.length;
        String line = FileUtils.bytesToHexString(lineNoNeed);

        byte[] lineNo = new byte[lineNoLenth[0]];
        arraycopy(lineNoNeed, 0, lineNo, 0, lineNo.length);
        this.lineNo = (String) FileUtils.byte2Parm(lineNo, Type.ASCLL);


        byte[] stationNoNeed = new byte[2];
        arraycopy(key, i, stationNoNeed, 0, stationNoNeed.length);
        i += stationNoNeed.length;
        stationId =stationNoNeed[1];

        byte[] diraction = new byte[1];
        arraycopy(key, i, diraction, 0, diraction.length);
        i += diraction.length;
        if (diraction[0] == 0) {
            this.diraction = "0001";
        } else {
            this.diraction = "0002";
        }

        byte[] backLineNoLenth = new byte[1];
        arraycopy(key, i, backLineNoLenth, 0, backLineNoLenth.length);
        i += lineNoLenth.length;
//                    MiLog.i("接收站点数据", "lineNoLenth：" + FileUtils.bytesToHexString(lineNoLenth));

        byte[] backLineNoNeed = new byte[9];
        arraycopy(key, i, backLineNoNeed, 0, backLineNoNeed.length);
        i += lineNoNeed.length;

        byte[] backLineNo = new byte[lineNoLenth[0]];
        arraycopy(lineNoNeed, 0, backLineNo, 0, backLineNo.length);
        this.backLineNo = (String) FileUtils.byte2Parm(lineNo, Type.ASCLL);
        ;
//
        byte[] backStationNoNeed = new byte[2];
        arraycopy(key, i, backStationNoNeed, 0, backStationNoNeed.length);
        i += stationNoNeed.length;
        backstationId = backStationNoNeed[1];
//                    MiLog.i("接收站点数据", "stationNoNeed：" + FileUtils.bytesToHexString(stationNoNeed));

        byte[] backDiraction = new byte[1];
        arraycopy(key, i, backDiraction, 0, backDiraction.length);
        i += diraction.length;
        if (diraction[0] == 0) {
            this.backDiraction = "0001";
        } else {
            this.backDiraction = "0002";
        }

        byte[] stationKey = new byte[3];
        arraycopy(key, i, stationKey, 0, stationKey.length);
        i += stationKey.length;
        this.stationKey = FileUtils.bytesToHexString(stationKey);
//                    MiLog.i("接收站点数据", "diraction：" + FileUtils.bytesToHexString(diraction));

        byte[] synKey = new byte[3];
        arraycopy(key, i, synKey, 0, synKey.length);
        i += synKey.length;
        this.synKey = FileUtils.bytesToHexString(synKey);
//                    MiLog.i("接收站点数据", "diraction：" + FileUtils.bytesToHexString(diraction));

        byte[] priceKey = new byte[10];
        arraycopy(key, i, priceKey, 0, priceKey.length);
        i += priceKey.length;
        this.priceKey = FileUtils.bytesToHexString(priceKey);
    }


    public byte[] getAlldate() {
        return alldate;
    }


    public String getStationKey() {
        return stationKey;
    }

    public String getSynKey() {
        return synKey;
    }

    public String getPriceKey() {
        return priceKey;
    }

    public String getLineNo() {
        return lineNo;
    }

    public int getStationId() {
        return stationId;
    }

    public String getDiraction() {
        return diraction;
    }

    public String getBackLineNo() {
        return backLineNo;
    }

    public int getBackstationId() {
        return backstationId;
    }

    public String getBackDiraction() {
        return backDiraction;
    }
}
