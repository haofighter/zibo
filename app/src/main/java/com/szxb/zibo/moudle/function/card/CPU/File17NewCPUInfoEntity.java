package com.szxb.zibo.moudle.function.card.CPU;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class File17NewCPUInfoEntity {
    //--------------------------------------------------淄博特有分段交易（0017）
    String compound_consumption_logo;        //复合记录标识
    String record_length;                    //记录长度
    String record_unlock;                    //应用锁定标识
    String complete_mark;                    //完成标志
    String link_number;                    //线路编号
    private String driver_direction;                //行车方向
    String boarding_site_index;            //上车站点索引
    String pre_preferential_amount;                    //上车时全程票价
    String boarding_time;                //上车时间
    String vehicle_number;                //车辆号
    String pose_id;                        //psam卡号
    String driver_number;                //驾驶员编号
    String full_fare;        //优惠前金额
    String reserved3;                    //预留

    @Override
    public String toString() {
        return compound_consumption_logo +       //复合记录标识
                record_length +                    //记录长度
                record_unlock +                    //应用锁定标识
                complete_mark +                   //完成标志
                link_number +                   //线路编号
                driver_direction +               //行车方向
                boarding_site_index +           //上车站点索引
                pre_preferential_amount +                   //上车时全程票价
                boarding_time +               //上车时间
                vehicle_number +               //车辆号
                pose_id +                       //psam卡号
                driver_number +               //驾驶员编号
                full_fare +       //优惠前金额
                reserved3;                    //预留
    }

    public int praseFile(int i, byte[] date) {
        byte[] carddate = new byte[48];
        arraycopy(date, i, carddate, 0, carddate.length);
        MiLog.i("17卡数据", (String) FileUtils.byte2Parm(carddate, Type.HEX));

        //复合记录标识
        byte[] compound_consumption_logo = new byte[1];
        arraycopy(date, i, compound_consumption_logo, 0, compound_consumption_logo.length);
        i += compound_consumption_logo.length;
        this.compound_consumption_logo = (String) FileUtils.byte2Parm(compound_consumption_logo, Type.HEX);

        //记录长度
        byte[] record_length = new byte[1];
        arraycopy(date, i, record_length, 0, record_length.length);
        i += record_length.length;
        this.record_length = (String) FileUtils.byte2Parm(record_length, Type.HEX);

        //应用锁定标识
        byte[] record_unlock = new byte[1];
        arraycopy(date, i, record_unlock, 0, record_unlock.length);
        i += record_unlock.length;
        this.record_unlock = (String) FileUtils.byte2Parm(record_unlock, Type.HEX);

        //完成标志
        byte[] complete_mark = new byte[1];
        arraycopy(date, i, complete_mark, 0, complete_mark.length);
        i += complete_mark.length;
        this.complete_mark = (String) FileUtils.byte2Parm(complete_mark, Type.HEX);

        //线路编号
        byte[] link_number = new byte[3];
        arraycopy(date, i, link_number, 0, link_number.length);
        i += link_number.length;
        this.link_number = (String) FileUtils.byte2Parm(link_number, Type.HEX);

        //行车方向
        byte[] driver_direction = new byte[1];
        arraycopy(date, i, driver_direction, 0, driver_direction.length);
        i += driver_direction.length;
        this.driver_direction = (String) FileUtils.byte2Parm(driver_direction, Type.HEX);

        //上车站点索引
        byte[] boarding_site_index = new byte[1];
        arraycopy(date, i, boarding_site_index, 0, boarding_site_index.length);
        i += boarding_site_index.length;
        this.boarding_site_index = (String) FileUtils.byte2Parm(boarding_site_index, Type.HEX);

        //优惠前金额
        byte[] pre_preferential_amount = new byte[2];
        arraycopy(date, i, pre_preferential_amount, 0, pre_preferential_amount.length);
        i += pre_preferential_amount.length;
        this.pre_preferential_amount = (String) FileUtils.byte2Parm(pre_preferential_amount, Type.HEX);


        //上车时间
        byte[] boarding_time = new byte[7];
        arraycopy(date, i, boarding_time, 0, boarding_time.length);
        i += boarding_time.length;
        this.boarding_time = (String) FileUtils.byte2Parm(boarding_time, Type.HEX);

        //车辆号
        byte[] vehicle_number = new byte[3];
        arraycopy(date, i, vehicle_number, 0, vehicle_number.length);
        i += vehicle_number.length;
        this.vehicle_number = (String) FileUtils.byte2Parm(vehicle_number, Type.HEX);

        //psam卡号
        byte[] pose_id = new byte[6];
        arraycopy(date, i, pose_id, 0, pose_id.length);
        i += pose_id.length;
        this.pose_id = (String) FileUtils.byte2Parm(pose_id, Type.HEX);

        //驾驶员编号
        byte[] driver_number = new byte[3];
        arraycopy(date, i, driver_number, 0, driver_number.length);
        i += driver_number.length;
        this.driver_number = (String) FileUtils.byte2Parm(driver_number, Type.HEX);

        //上车时全程票价
        byte[] full_fare = new byte[2];
        arraycopy(date, i, full_fare, 0, full_fare.length);
        i += full_fare.length;
        this.full_fare = (String) FileUtils.byte2Parm(full_fare, Type.HEX);

        //预留
        byte[] reserved3 = new byte[16];
        arraycopy(date, i, reserved3, 0, reserved3.length);
        i += reserved3.length;
        this.reserved3 = (String) FileUtils.byte2Parm(reserved3, Type.HEX);

        return i;
    }

    public String getCompound_consumption_logo() {
        return compound_consumption_logo;
    }

    public void setCompound_consumption_logo(String compound_consumption_logo) {
        this.compound_consumption_logo = compound_consumption_logo;
    }

    public String getRecord_length() {
        return record_length;
    }

    public void setRecord_length(String record_length) {
        this.record_length = record_length;
    }

    public String getRecord_unlock() {
        return record_unlock;
    }

    public void setRecord_unlock(String record_unlock) {
        this.record_unlock = record_unlock;
    }

    public String getComplete_mark() {
        return complete_mark;
    }

    public void setComplete_mark(String complete_mark) {
        this.complete_mark = FileUtils.formatHexStringToByteString(1, complete_mark);
    }

    public String getLink_number() {
        return link_number;
    }

    public void setLink_number(String link_number) {
        this.link_number = FileUtils.formatHexStringToByteString(3, link_number);
    }

    public String getDriver_direction() {
        return driver_direction;
    }

    public void setDriver_direction(String driver_direction) {
        String nowLineDir = FileUtils.formatHexStringToByteString(1, driver_direction);
        this.driver_direction = nowLineDir.equals("01") ? "00" : "01";
    }

    public String getBoarding_site_index() {
        return boarding_site_index;
    }

    public void setBoarding_site_index(int boarding_site_index) {
        this.boarding_site_index = FileUtils.formatHexStringToByteString(1, FileUtils.bytesToHexString(FileUtils.int2byte2(boarding_site_index)));
    }

    public String getFull_fare() {
        return full_fare;
    }

    public void setFull_fare(int full_fare) {
        this.full_fare = FileUtils.formatHexStringToByteString(2, FileUtils.bytesToHexString(FileUtils.int2byte2(full_fare)));
    }

    public String getBoarding_time() {
        return boarding_time;
    }

    public void setBoarding_time(String boarding_time) {
        this.boarding_time = boarding_time;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getPose_id() {
        if (pose_id == null || pose_id.length() < 12) {
            pose_id = "000000000000";
        }

        return pose_id;
    }

    public void setPose_id(String pose_id) {
        this.pose_id = pose_id;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getPre_preferential_amount() {
        return pre_preferential_amount;
    }

    public void setPre_preferential_amount(int pre_preferential_amount) {
        this.pre_preferential_amount = FileUtils.formatHexStringToByteString(2, FileUtils.bytesToHexString(FileUtils.int2byte2(pre_preferential_amount)));
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public int getBoarding_site_indexInt() {
        return Integer.parseInt(boarding_site_index, 16);
    }
}
