package com.szxb.zibo.moudle.function.card.JTB;

import android.util.Log;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

//-------------------------------1A文件
public class File1AJTBInfoEntity {
    String record_id_1a;//    [2] 记录id
    String record_length_1a;                //记录长度
    String valid_identification_1a;           //应用有效标识
    String interflow_transactions_1a;        //互联互通交易标识
    String apply_lock_1a;                    //应用锁定标识
    String trade_serial_number_1a;        //交易流水号
    String transaction_status_1a;            //交易状态
    String boarding_city_code_1a;        //上车城市代码
    String alight_city_code_1a;            //下车城市代码
    String boarding_mark_1a;            //上车机构标识
    String alight_mark_1a;                //下车机构标识
    String boarding_the_site_1a;            //上车站点
    String alight_the_site_1a;                //下车站点
    String boarding_terminal_number_1a;    //上车终端编号
    String alight_terminal_number_1a;    //下车终端编号
    String boarding_time_1a;            //上车时间
    String alight_time_1a;                //下车时间
    String boarding_max_amount_1a;        //标注金额
    String direction_identity_1a;            //方向标识
    String boarding_line_number_1a;        //上车线路号
    String boarding_vehicle_number_1a;    //上车车辆号
    String reserved_1a;                //预留空间


    public int praseFile(int i, byte[] date) {

//        uint8_t record_id_1a[2];                 //记录id
        byte[] record_id_1a = new byte[2];
        arraycopy(date, i, record_id_1a, 0, record_id_1a.length);
        i += record_id_1a.length;
        this.record_id_1a = (String) FileUtils.byte2Parm(record_id_1a, Type.HEX);


//        uint8_t record_length_1a;                //记录长度
        byte[] record_length_1a = new byte[1];
        arraycopy(date, i, record_length_1a, 0, record_length_1a.length);
        i += record_length_1a.length;
        this.record_length_1a = (String) FileUtils.byte2Parm(record_length_1a, Type.HEX);


//        uint8_t valid_identification_1a;           //应用有效标识
        byte[] valid_identification_1a = new byte[1];
        arraycopy(date, i, valid_identification_1a, 0, valid_identification_1a.length);
        i += valid_identification_1a.length;
        this.valid_identification_1a = (String) FileUtils.byte2Parm(valid_identification_1a, Type.HEX);


//        uint8_t interflow_transactions_1a;        //互联互通交易标识 互联互通交易标识
//1－采用分时分段扣费／复合消费
//2-采用预授权消费
        byte[] interflow_transactions_1a = new byte[1];
        arraycopy(date, i, interflow_transactions_1a, 0, interflow_transactions_1a.length);
        i += interflow_transactions_1a.length;
        this.interflow_transactions_1a = (String) FileUtils.byte2Parm(interflow_transactions_1a, Type.HEX);


//        uint8_t apply_lock_1a;                    //应用锁定标识
        byte[] apply_lock_1a = new byte[1];
        arraycopy(date, i, apply_lock_1a, 0, apply_lock_1a.length);
        i += apply_lock_1a.length;
        this.apply_lock_1a = (String) FileUtils.byte2Parm(apply_lock_1a, Type.HEX);


//        uint8_t trade_serial_number_1a[8];        //交易流水号
        byte[] trade_serial_number_1a = new byte[8];
        arraycopy(date, i, trade_serial_number_1a, 0, trade_serial_number_1a.length);
        i += trade_serial_number_1a.length;
        this.trade_serial_number_1a = (String) FileUtils.byte2Parm(trade_serial_number_1a, Type.HEX);


//        uint8_t transaction_status_1a;            //交易状态 交易状态 00已下车 01已上车
        byte[] transaction_status_1a = new byte[1];
        arraycopy(date, i, transaction_status_1a, 0, transaction_status_1a.length);
        i += transaction_status_1a.length;
        this.transaction_status_1a = (String) FileUtils.byte2Parm(transaction_status_1a, Type.HEX);
        Log.i("刷卡", "卡中的原始   交易状态：" + this.transaction_status_1a);


//        uint8_t boarding_city_code_1a[2];        //上车城市代码
        byte[] boarding_city_code_1a = new byte[2];
        arraycopy(date, i, boarding_city_code_1a, 0, boarding_city_code_1a.length);
        i += boarding_city_code_1a.length;
        this.boarding_city_code_1a = (String) FileUtils.byte2Parm(boarding_city_code_1a, Type.HEX);


//        uint8_t alight_city_code_1a[2];            //下车城市代码
        byte[] alight_city_code_1a = new byte[2];
        arraycopy(date, i, alight_city_code_1a, 0, alight_city_code_1a.length);
        i += alight_city_code_1a.length;
        this.alight_city_code_1a = (String) FileUtils.byte2Parm(alight_city_code_1a, Type.HEX);


//        uint8_t boarding_mark_1a[8];            //上车机构标识
        byte[] boarding_mark_1a = new byte[8];
        arraycopy(date, i, boarding_mark_1a, 0, boarding_mark_1a.length);
        i += boarding_mark_1a.length;
        this.boarding_mark_1a = (String) FileUtils.byte2Parm(boarding_mark_1a, Type.HEX);


//        uint8_t alight_mark_1a[8];                //下车机构标识
        byte[] alight_mark_1a = new byte[8];
        arraycopy(date, i, alight_mark_1a, 0, alight_mark_1a.length);
        i += alight_mark_1a.length;
        this.alight_mark_1a = (String) FileUtils.byte2Parm(alight_mark_1a, Type.HEX);


//        uint8_t boarding_the_site_1a[8];            //上车站点
        byte[] boarding_the_site_1a = new byte[8];
        arraycopy(date, i, boarding_the_site_1a, 0, boarding_the_site_1a.length);
        i += boarding_the_site_1a.length;
        this.boarding_the_site_1a = (String) FileUtils.byte2Parm(boarding_the_site_1a, Type.HEX);


//        uint8_t alight_the_site_1a[8];                //下车站点
        byte[] alight_the_site_1a = new byte[8];
        arraycopy(date, i, alight_the_site_1a, 0, alight_the_site_1a.length);
        i += alight_the_site_1a.length;
        this.alight_the_site_1a = (String) FileUtils.byte2Parm(alight_the_site_1a, Type.HEX);


//        uint8_t boarding_terminal_number_1a[8];    //上车终端编号
        byte[] boarding_terminal_number_1a = new byte[8];
        arraycopy(date, i, boarding_terminal_number_1a, 0, boarding_terminal_number_1a.length);
        i += boarding_terminal_number_1a.length;
        this.boarding_terminal_number_1a = (String) FileUtils.byte2Parm(boarding_terminal_number_1a, Type.HEX);


//        uint8_t alight_terminal_number_1a[8];    //下车终端编号
        byte[] alight_terminal_number_1a = new byte[8];
        arraycopy(date, i, alight_terminal_number_1a, 0, alight_terminal_number_1a.length);
        i += alight_terminal_number_1a.length;
        this.alight_terminal_number_1a = (String) FileUtils.byte2Parm(alight_terminal_number_1a, Type.HEX);


//        uint8_t boarding_time_1a[7];            //上车时间
        byte[] boarding_time_1a = new byte[7];
        arraycopy(date, i, boarding_time_1a, 0, boarding_time_1a.length);
        i += boarding_time_1a.length;
        this.boarding_time_1a = (String) FileUtils.byte2Parm(boarding_time_1a, Type.HEX);


//        uint8_t alight_time_1a[7];                //下车时间
        byte[] alight_time_1a = new byte[7];
        arraycopy(date, i, alight_time_1a, 0, alight_time_1a.length);
        i += alight_time_1a.length;
        this.alight_time_1a = (String) FileUtils.byte2Parm(alight_time_1a, Type.HEX);


//        uint8_t boarding_max_amount_1a[4];        //标注金额
        byte[] boarding_max_amount_1a = new byte[4];
        arraycopy(date, i, boarding_max_amount_1a, 0, boarding_max_amount_1a.length);
        i += boarding_max_amount_1a.length;
        this.boarding_max_amount_1a = (String) FileUtils.byte2Parm(boarding_max_amount_1a, Type.HEX);


//        uint8_t direction_identity_1a;            //方向标识
        byte[] direction_identity_1a = new byte[1];
        arraycopy(date, i, direction_identity_1a, 0, direction_identity_1a.length);
        i += direction_identity_1a.length;
        this.direction_identity_1a = (String) FileUtils.byte2Parm(direction_identity_1a, Type.HEX);


//        uint8_t boarding_line_number_1a[2];        //上车线路号
        byte[] boarding_line_number_1a = new byte[2];
        arraycopy(date, i, boarding_line_number_1a, 0, boarding_line_number_1a.length);
        i += boarding_line_number_1a.length;
        this.boarding_line_number_1a = (String) FileUtils.byte2Parm(boarding_line_number_1a, Type.HEX);


//        uint8_t boarding_vehicle_number_1a[6];    //上车车辆号
        byte[] boarding_vehicle_number_1a = new byte[6];
        arraycopy(date, i, boarding_vehicle_number_1a, 0, boarding_vehicle_number_1a.length);
        i += boarding_vehicle_number_1a.length;
        this.boarding_vehicle_number_1a = (String) FileUtils.byte2Parm(boarding_vehicle_number_1a, Type.HEX);

//        uint8_t reserved_1a[34];                //预留空间
        byte[] reserved_1a = new byte[34];
        arraycopy(date, i, reserved_1a, 0, reserved_1a.length);
        i += reserved_1a.length;
        this.reserved_1a = (String) FileUtils.byte2Parm(reserved_1a, Type.HEX);

        return i;
    }


    public String getTransaction_status_1a() {
        return transaction_status_1a;
    }

    public String getBoarding_max_amount_1a() {
        return boarding_max_amount_1a;
    }

    public int getBoarding_the_site_1a() {
        return Integer.parseInt(boarding_the_site_1a, 16);
    }


    public void setRecord_id_1a(String record_id_1a) {//2
        this.record_id_1a = FileUtils.formatHexStringToByteString(2, record_id_1a);
    }

    public void setRecord_length_1a(String record_length_1a) {//1
        this.record_length_1a = FileUtils.formatHexStringToByteString(1, record_length_1a);
    }

    public void setValid_identification_1a(String valid_identification_1a) {//1
        this.valid_identification_1a = FileUtils.formatHexStringToByteString(1, valid_identification_1a);
        ;
    }

    public void setInterflow_transactions_1a(String interflow_transactions_1a) {//1
        this.interflow_transactions_1a = FileUtils.formatHexStringToByteString(1, interflow_transactions_1a);
    }

    public void setApply_lock_1a(String apply_lock_1a) {//1
        this.apply_lock_1a = FileUtils.formatHexStringToByteString(1, apply_lock_1a);
    }

    public void setTrade_serial_number_1a(String trade_serial_number_1a) {//8
        this.trade_serial_number_1a = FileUtils.formatHexStringToByteString(8, trade_serial_number_1a);
    }

    public void setTransaction_status_1a(String transaction_status_1a) {//1
        this.transaction_status_1a = FileUtils.formatHexStringToByteString(1, transaction_status_1a);
    }

    public void setBoarding_city_code_1a(String boarding_city_code_1a) {//2
        this.boarding_city_code_1a = FileUtils.formatHexStringToByteString(2, boarding_city_code_1a);
        ;
    }

    public void setAlight_city_code_1a(String alight_city_code_1a) {//2
        this.alight_city_code_1a = FileUtils.formatHexStringToByteString(2, alight_city_code_1a);
        ;
    }

    public void setBoarding_mark_1a(String boarding_mark_1a) {//8
        this.boarding_mark_1a = FileUtils.formatHexStringToByteString(8, boarding_mark_1a);
        ;
    }

    public void setAlight_mark_1a(String alight_mark_1a) {//8
        this.alight_mark_1a = FileUtils.formatHexStringToByteString(8, alight_mark_1a);
        ;
    }

    public void setBoarding_the_site_1a(String boarding_the_site_1a) {//8
        this.boarding_the_site_1a = FileUtils.formatHexStringToByteString(8, boarding_the_site_1a);
        ;
    }

    public void setBoarding_the_site_1a(int boarding_the_site_1a) {
        this.boarding_the_site_1a = FileUtils.formatHexStringToByteString(8, FileUtils.bytesToHexString(FileUtils.int2byte2(boarding_the_site_1a)));
    }


    public void setAlight_the_site_1a(String alight_the_site_1a) {//8
        this.alight_the_site_1a = FileUtils.formatHexStringToByteString(8, alight_the_site_1a);
        ;
    }

    public void setAlight_the_site_1a(int boarding_site_index) {
        this.alight_the_site_1a = FileUtils.formatHexStringToByteString(8, FileUtils.bytesToHexString(FileUtils.int2byte2(boarding_site_index)));
    }

    public void setBoarding_terminal_number_1a(String boarding_terminal_number_1a) {//8
        this.boarding_terminal_number_1a = FileUtils.formatHexStringToByteString(8, boarding_terminal_number_1a);
        ;
    }

    public void setAlight_terminal_number_1a(String alight_terminal_number_1a) {//8
        this.alight_terminal_number_1a = FileUtils.formatHexStringToByteString(8, alight_terminal_number_1a);
        ;
    }

    public void setBoarding_time_1a(String boarding_time_1a) {//7
        this.boarding_time_1a = FileUtils.formatHexStringToByteString(7, boarding_time_1a);
        ;
    }

    public void setAlight_time_1a(String alight_time_1a) {//7
        this.alight_time_1a = FileUtils.formatHexStringToByteString(7, alight_time_1a);
        ;
    }

    public void setBoarding_max_amount_1a(long boarding_max_amount_1a) {//4
        this.boarding_max_amount_1a = FileUtils.formatHexStringToByteString(4, FileUtils.bytesToHexString(FileUtils.int2byte2(boarding_max_amount_1a)));
        ;
    }

    public void setDirection_identity_1a(String direction_identity_1a) {//1
        this.direction_identity_1a = FileUtils.formatHexStringToByteString(1, direction_identity_1a);
        ;
    }

    public void setBoarding_line_number_1a(String boarding_line_number_1a) {//2
        this.boarding_line_number_1a = FileUtils.formatHexStringToByteString(2, boarding_line_number_1a);
    }

    public void setBoarding_vehicle_number_1a(String boarding_vehicle_number_1a) {//6
        this.boarding_vehicle_number_1a = FileUtils.formatHexStringToByteString(6, boarding_vehicle_number_1a);
    }

    public void setReserved_1a(String reserved_1a) {//34  //线路编号第一字节+司机编号(3字节)+优惠前金额(2字节)
        this.reserved_1a = FileUtils.formatHexStringToByteString(34, reserved_1a, true);
    }


    public String getBoarding_line_number_1a() {
        return boarding_line_number_1a;
    }

    public String getBoarding_vehicle_number_1a() {
        return boarding_vehicle_number_1a;
    }

    public String getDirection_identity_1a() {
        return direction_identity_1a;
    }

    public String getBoarding_time_1a() {
        return boarding_time_1a;
    }

    public String getAlight_time_1a() {
        return alight_time_1a;
    }

    public String getPaydate() {
        return record_id_1a +//    [2] 记录id
                record_length_1a +                //记录长度
                valid_identification_1a +           //应用有效标识
                interflow_transactions_1a +        //互联互通交易标识
                apply_lock_1a +                    //应用锁定标识
                trade_serial_number_1a +        //交易流水号
                transaction_status_1a +            //交易状态
                boarding_city_code_1a +        //上车城市代码
                alight_city_code_1a +            //下车城市代码
                boarding_mark_1a +            //上车机构标识
                alight_mark_1a +                //下车机构标识
                boarding_the_site_1a +            //上车站点
                alight_the_site_1a +                //下车站点
                boarding_terminal_number_1a +    //上车终端编号
                alight_terminal_number_1a +    //下车终端编号
                boarding_time_1a +            //上车时间
                alight_time_1a +                //下车时间
                boarding_max_amount_1a +        //标注金额
                direction_identity_1a +            //方向标识
                boarding_line_number_1a +        //上车线路号
                boarding_vehicle_number_1a +    //上车车辆号
                reserved_1a;                //预留空间
    }

}
