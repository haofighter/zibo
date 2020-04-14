package com.szxb.zibo.moudle.function.card.M1;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_1C1D {
    //块0x1C~1D//用于多票
    public String complete_mark;                    //完成标志
    public String link_number;                    //线路编号
    public String driver_direction;                //行车方向
    public String boarding_site_index;            //上车站点索引
    public String full_fare;                    //上车时全程票价
    public String boarding_time;                //上车时间
    public String verify3;                        //校验
    public String vehicle_number;                //车辆号
    public String pose_id;                        //psam卡号
    public String reserved3;                    //预留


    public int praseBlock(int i, byte[] date) {
        //完成标志
        byte[] complete_mark = new byte[1];
        arraycopy(date, i, complete_mark, 0, complete_mark.length);
        i += complete_mark.length;
        this.complete_mark = (String) FileUtils.byte2Parm(complete_mark, Type.HEX);

        //线路编号
        byte[] link_number = new byte[2];
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

        //上车时全程票价
        byte[] full_fare = new byte[2];
        arraycopy(date, i, full_fare, 0, full_fare.length);
        i += full_fare.length;
        this.full_fare = (String) FileUtils.byte2Parm(full_fare, Type.HEX);

        //上车时间
        byte[] boarding_time = new byte[7];
        arraycopy(date, i, boarding_time, 0, boarding_time.length);
        i += boarding_time.length;
        this.boarding_time = (String) FileUtils.byte2Parm(boarding_time, Type.HEX);

        //校验
        byte[] verify3 = new byte[2];
        arraycopy(date, i, verify3, 0, verify3.length);
        i += verify3.length;
        this.verify3 = (String) FileUtils.byte2Parm(verify3, Type.HEX);

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

        //预留
        byte[] reserved3 = new byte[7];
        arraycopy(date, i, reserved3, 0, reserved3.length);
        i += reserved3.length;
        this.reserved3 = (String) FileUtils.byte2Parm(reserved3, Type.HEX);

        return i;
    }


}
