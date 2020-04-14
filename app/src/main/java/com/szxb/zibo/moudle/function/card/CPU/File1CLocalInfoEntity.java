package com.szxb.zibo.moudle.function.card.CPU;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;
import com.szxb.zibo.moudle.function.card.CardInfoEntity;

import static java.lang.System.arraycopy;

//-------------------------------18文件
public class File1CLocalInfoEntity {
    String complete_mark;                    //完成标志 00  完成 01 未完成
    String link_number;                    //线路编号
    String driver_direction;                //行车方向
    String boarding_site_index;            //上车站点索引
    String pre_preferential_amount;                  //上车时全程票价
    String boarding_time;                //上车时间
    String verify3;                        //校验 M1卡独有

    String vehicle_number;                //车辆号
    String pose_id;                        //psam卡号
    String line_1;                            //线路号1
    String driver_number;                //驾驶员编号
    String version;                        //版本号
    String full_fare;        //优惠前金额
    String reserved3;         //预留 CPU卡独有


    public int praseFile1CLocal(int i, byte[] date, String selete_aid) {

        //complete_mark;                    //完成标志
        byte[] complete_mark = new byte[1];
        arraycopy(date, i, complete_mark, 0, complete_mark.length);
        i += complete_mark.length;
        this.complete_mark = (String) FileUtils.byte2Parm(complete_mark, Type.HEX);

        //link_number[2];                    //线路编号
        byte[] link_number = new byte[2];
        arraycopy(date, i, link_number, 0, link_number.length);
        i += link_number.length;
        this.link_number = (String) FileUtils.byte2Parm(link_number, Type.HEX);

        //driver_direction;                //行车方向
        byte[] driver_direction = new byte[1];
        arraycopy(date, i, driver_direction, 0, driver_direction.length);
        i += driver_direction.length;
        this.driver_direction = (String) FileUtils.byte2Parm(driver_direction, Type.HEX);

        //boarding_site_index;            //上车站点索引
        byte[] boarding_site_index = new byte[1];
        arraycopy(date, i, boarding_site_index, 0, boarding_site_index.length);
        i += boarding_site_index.length;
        this.boarding_site_index = (String) FileUtils.byte2Parm(boarding_site_index, Type.HEX);

        // pre_preferential_amount;        //优惠后金额
        byte[] pre_preferential_amount = new byte[2];
        arraycopy(date, i, pre_preferential_amount, 0, pre_preferential_amount.length);
        i += pre_preferential_amount.length;
        this.pre_preferential_amount = (String) FileUtils.byte2Parm(pre_preferential_amount, Type.HEX);


        //boarding_time[7];                //上车时间
        byte[] boarding_time = new byte[7];
        arraycopy(date, i, boarding_time, 0, boarding_time.length);
        i += boarding_time.length;
        this.boarding_time = (String) FileUtils.byte2Parm(boarding_time, Type.HEX);

        if (selete_aid.equals("04")) {
            //verify3[2];                        //校验
            byte[] verify3 = new byte[2];
            arraycopy(date, i, verify3, 0, verify3.length);
            i += verify3.length;
            this.verify3 = (String) FileUtils.byte2Parm(verify3, Type.HEX);
        }


        //vehicle_number[3];                //车辆号
        byte[] vehicle_number = new byte[3];
        arraycopy(date, i, vehicle_number, 0, vehicle_number.length);
        i += vehicle_number.length;
        this.vehicle_number = (String) FileUtils.byte2Parm(vehicle_number, Type.HEX);

        //pose_id[6];                        //psam卡号
        byte[] pose_id = new byte[6];
        arraycopy(date, i, pose_id, 0, pose_id.length);
        i += pose_id.length;
        this.pose_id = (String) FileUtils.byte2Parm(pose_id, Type.HEX);

        //line_1;                        //线路号1
        byte[] line_1 = new byte[1];
        arraycopy(date, i, line_1, 0, line_1.length);
        i += line_1.length;
        this.line_1 = (String) FileUtils.byte2Parm(line_1, Type.HEX);

        // driver_number[3];                //驾驶员编号
        byte[] driver_number = new byte[3];
        arraycopy(date, i, driver_number, 0, driver_number.length);
        i += driver_number.length;
        this.driver_number = (String) FileUtils.byte2Parm(driver_number, Type.HEX);

        // version;                        //版本号
        byte[] version = new byte[1];
        arraycopy(date, i, version, 0, version.length);
        i += version.length;
        this.version = (String) FileUtils.byte2Parm(version, Type.HEX);

        //full_fare[2];                    //上车时全程票价
        byte[] full_fare = new byte[2];
        arraycopy(date, i, full_fare, 0, full_fare.length);
        i += full_fare.length;
        this.full_fare = (String) FileUtils.byte2Parm(full_fare, Type.HEX);


        if (selete_aid.equals("03")) {
            // pre_preferential_amount;        //优惠前金额
            byte[] reserved3 = new byte[4];
            arraycopy(date, i, reserved3, 0, reserved3.length);
            i += reserved3.length;
            this.reserved3 = (String) FileUtils.byte2Parm(reserved3, Type.HEX);
        }


        return i;
    }

    public void setFull_fare(int full_fare) {
        this.full_fare = FileUtils.formatHexStringToByteString(2, FileUtils.bytesToHexString(FileUtils.int2byte2(full_fare)));
    }

    public void setComplete_mark(String complete_mark) {
        this.complete_mark = FileUtils.formatHexStringToByteString(1, complete_mark);
    }

    public void setLink_number(String link_number) {
        this.link_number = FileUtils.formatHexStringToByteString(2, link_number);
    }

    public void setDriver_direction(String driver_direction) {
        String nowLineDir = FileUtils.formatHexStringToByteString(1, driver_direction);
        this.driver_direction = nowLineDir.equals("01") ? "00" : "01";
    }

    public void setBoarding_site_index(int boarding_site_index) {
        this.boarding_site_index = FileUtils.formatHexStringToByteString(1, FileUtils.bytesToHexString(FileUtils.int2byte2(boarding_site_index)));
    }


    public void setBoarding_time(String boarding_time) {
        this.boarding_time = FileUtils.formatHexStringToByteString(7, boarding_time);
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = FileUtils.formatHexStringToByteString(3, vehicle_number);
    }

    public void setPose_id(String pose_id) {
        this.pose_id = FileUtils.formatHexStringToByteString(6, pose_id);
    }

    public void setVerify3(String verify3) {
        this.verify3 = FileUtils.formatHexStringToByteString(2, verify3);
    }


    public void setLine_1(String line_1) {
        try {
            int line = Integer.parseInt(line_1.substring(2, 6));
            String lineStr = FileUtils.formatHexStringToByteString(2, Integer.toHexString(line));
            line_1 = lineStr.substring(0, 2);
        } catch (Exception e) {

        }
        this.line_1 = FileUtils.formatHexStringToByteString(1, line_1);
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = FileUtils.formatHexStringToByteString(3, driver_number);
    }

    public void setVersion(String version) {
        this.version = FileUtils.formatHexStringToByteString(1, version);
    }

    public void setPre_preferential_amount(int pre_preferential_amount) {
        this.pre_preferential_amount = FileUtils.formatHexStringToByteString(2, FileUtils.bytesToHexString(FileUtils.int2byte2(pre_preferential_amount)));
    }

    public int getFull_fare() {
        return Integer.parseInt(full_fare, 16);
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public String getBoarding_time() {
        return boarding_time;
    }


    public String getComplete_mark() {
        return complete_mark;
    }

    public String getLink_number() {
        return link_number;
    }

    public String getDriver_direction() {
        return driver_direction;
    }

    public String getBoarding_site_index() {
        return boarding_site_index;
    }

    public int getBoarding_site_indexInt() {
        return Integer.parseInt(boarding_site_index,16);
    }

    public String getVerify3() {
        return verify3;
    }

    public String getPose_id() {
        return pose_id;
    }

    public String getLine_1() {
        return line_1;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public String getVersion() {
        return version;
    }

    public String getPre_preferential_amount() {
        return pre_preferential_amount;
    }

    public String getReserved3() {
        return reserved3;
    }

    @Override
    public String toString() {
        return complete_mark
                + link_number
                + driver_direction
                + boarding_site_index
                + pre_preferential_amount
                + boarding_time
                + (verify3 == null ? "" : verify3)
                + vehicle_number
                + pose_id
                + line_1
                + driver_number
                + version
                + full_fare
                + (reserved3 == null ? "" : reserved3);
    }
}
