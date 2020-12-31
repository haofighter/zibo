package com.szxb.zibo.moudle.function.card.JTB;

import android.util.Log;


import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.Type;

import static java.lang.System.arraycopy;

//-------------------------------
public class File1AJTBInfoEntity {
    private String record_id_1a;//    [2] 记录id  记录ID标识 2702  2 HEX
    private String record_length_1a;                //记录长度   0x7D   1  HEX
    private String valid_identification_1a;           //应用有效标识      1  HEX
    private String interflow_transactions_1a;        //互联互通交易标识    1  HEX
    private String apply_lock_1a;                    //应用锁定标识    1  BCD
    private String trade_serial_number_1a;        //交易流水号       8   BCD
    private String transaction_status_1a;            //交易状态     1     BCD
    private String boarding_city_code_1a;        //上车城市代码     2     BCD
    private String boarding_mark_1a;            //上车机构标识     8   BCD
    private String boarding_operating_1a;            //上车运营商代码     2    HEX高字节在前
    private String boarding_lineno_1a;            //上车线路号     2    HEX高字节在前
    private String boarding_station_1a;            //上车站点     1    HEX
    private String boarding_carno_1a;            //上车车辆号     8    ASCII
    private String boarding_possn_1a;            //上车终端编号     8    BCD
    private String boarding_time_1a;            //上车时间     7    YYYYMMDDHHMMSS
    private String boarding_pay_all_1a;            // 标注金额 用于逃票追缴 4HEX高字节在前
    private String diraction_1a;            // 方向标识 AB ：上行 BA：下行  1 HEX
    private String alight_city_code_1a;            // 下车城市代码 2     BCD
    private String alight_mark_1a;            // 下车机构标识 8     BCD
    private String alight_operating_1a;            // 下车运营商代码 2     HEX高字节在前
    private String alight_lineno_1a;            // 下车线路号 2     HEX高字节在前
    private String alight_station_1a;            // 下车线路号 1     BCD
    private String alight_carno_1a;            // 下车车辆号 8     ASCII
    private String alight_possn_1a;            // 下车终端编号 8     BCD
    private String alight_time_1a;            //下车时间     7   YYYYMMDDHHMMSS
    private String alight_pay_1a;            //交易金额     4   HEX高字节在前
    private String mileage;            //乘车里程     2   HEX高字节在前
    private String lineno;            //线路编号     1   HEX
    private String driverno;            //司机编号     3   HEX
    private String allpay;            //优惠前金额     2   HEX
    private String res;            //预留     20   HEX


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


//        uint8_t boarding_mark_1a[8];            //上车机构标识
        byte[] boarding_mark_1a = new byte[8];
        arraycopy(date, i, boarding_mark_1a, 0, boarding_mark_1a.length);
        i += boarding_mark_1a.length;
        this.boarding_mark_1a = (String) FileUtils.byte2Parm(boarding_mark_1a, Type.HEX);


//        String boarding_operating_1a;            //上车运营商代码     2    HEX高字节在前
        byte[] boarding_operating_1a = new byte[2];
        arraycopy(date, i, boarding_operating_1a, 0, boarding_operating_1a.length);
        i += boarding_operating_1a.length;
        this.boarding_operating_1a = (String) FileUtils.byte2Parm(boarding_operating_1a, Type.HEX);


//        String boarding_lineno_1a;            //上车线路号     2    HEX高字节在前
        byte[] boarding_lineno_1a = new byte[2];
        arraycopy(date, i, boarding_lineno_1a, 0, boarding_lineno_1a.length);
        i += boarding_lineno_1a.length;
        this.boarding_lineno_1a = (String) FileUtils.byte2Parm(boarding_lineno_1a, Type.HEX);


//        String boarding_station_1a;            //上车站点     1    HEX
        byte[] boarding_station_1a = new byte[1];
        arraycopy(date, i, boarding_station_1a, 0, boarding_station_1a.length);
        i += boarding_station_1a.length;
        this.boarding_station_1a = (String) FileUtils.byte2Parm(boarding_station_1a, Type.HEX);


//        String boarding_carno_1a;            //上车车辆号     8    ASCII
        byte[] boarding_carno_1a = new byte[8];
        arraycopy(date, i, boarding_carno_1a, 0, boarding_carno_1a.length);
        i += boarding_carno_1a.length;
        this.boarding_carno_1a = (String) FileUtils.byte2Parm(boarding_carno_1a, Type.HEX);


//        String boarding_possn_1a;            //上车终端编号     8    BCD
        byte[] boarding_possn_1a = new byte[8];
        arraycopy(date, i, boarding_possn_1a, 0, boarding_possn_1a.length);
        i += boarding_possn_1a.length;
        this.boarding_possn_1a = (String) FileUtils.byte2Parm(boarding_possn_1a, Type.HEX);


//        String boarding_time_1a;            //上车时间     7    YYYYMMDDHHMMSS
        byte[] boarding_time_1a = new byte[7];
        arraycopy(date, i, boarding_time_1a, 0, boarding_time_1a.length);
        i += boarding_time_1a.length;
        this.boarding_time_1a = (String) FileUtils.byte2Parm(boarding_time_1a, Type.HEX);


//        String boarding_pay_all_1a;            // 标注金额 用于逃票追缴 4    HEX高字节在前
        byte[] boarding_pay_all_1a = new byte[4];
        arraycopy(date, i, boarding_pay_all_1a, 0, boarding_pay_all_1a.length);
        i += boarding_pay_all_1a.length;
        this.boarding_pay_all_1a = (String) FileUtils.byte2Parm(boarding_pay_all_1a, Type.HEX);

//        String boarding_diraction_1a;            // 方向标识 AB ：上行 BA：下行  1 HEX
        byte[] boarding_diraction_1a = new byte[1];
        arraycopy(date, i, boarding_diraction_1a, 0, boarding_diraction_1a.length);
        i += boarding_diraction_1a.length;
        this.diraction_1a = (String) FileUtils.byte2Parm(boarding_diraction_1a, Type.HEX);

//        String alight_city_code_1a;            // 下车城市代码 2     BCD
        byte[] alight_city_code_1a = new byte[2];
        arraycopy(date, i, alight_city_code_1a, 0, alight_city_code_1a.length);
        i += alight_city_code_1a.length;
        this.alight_city_code_1a = (String) FileUtils.byte2Parm(alight_city_code_1a, Type.HEX);

//        String alight_mark_1a;            // 下车机构标识 8     BCD
        byte[] alight_mark_1a = new byte[8];
        arraycopy(date, i, alight_mark_1a, 0, alight_mark_1a.length);
        i += alight_mark_1a.length;
        this.alight_mark_1a = (String) FileUtils.byte2Parm(alight_mark_1a, Type.HEX);


//        String alight_operating_1a;            // 下车运营商代码 2     HEX高字节在前
        byte[] alight_operating_1a = new byte[2];
        arraycopy(date, i, alight_operating_1a, 0, alight_operating_1a.length);
        i += alight_operating_1a.length;
        this.alight_operating_1a = (String) FileUtils.byte2Parm(alight_operating_1a, Type.HEX);

//        String alight_lineno_1a;            // 下车线路号 2     HEX高字节在前
        byte[] alight_lineno_1a = new byte[2];
        arraycopy(date, i, alight_lineno_1a, 0, alight_lineno_1a.length);
        i += alight_lineno_1a.length;
        this.alight_lineno_1a = (String) FileUtils.byte2Parm(alight_lineno_1a, Type.HEX);

//        String alight_station_1a;            // 下车线路号 1     BCD
        byte[] alight_station_1a = new byte[1];
        arraycopy(date, i, alight_station_1a, 0, alight_station_1a.length);
        i += alight_station_1a.length;
        this.alight_station_1a = (String) FileUtils.byte2Parm(alight_station_1a, Type.HEX);

//        String alight_carno_1a;            // 下车车辆号 8     ASCII
        byte[] alight_carno_1a = new byte[8];
        arraycopy(date, i, alight_carno_1a, 0, alight_carno_1a.length);
        i += alight_carno_1a.length;
        this.alight_carno_1a = (String) FileUtils.byte2Parm(alight_carno_1a, Type.HEX);

//        String alight_possn_1a;            // 下车终端编号 8     BCD
        byte[] alight_possn_1a = new byte[8];
        arraycopy(date, i, alight_possn_1a, 0, alight_possn_1a.length);
        i += alight_possn_1a.length;
        this.alight_possn_1a = (String) FileUtils.byte2Parm(alight_possn_1a, Type.HEX);


//        String alight_time_1a;            //下车时间     7   YYYYMMDDHHMMSS
        byte[] alight_time_1a = new byte[7];
        arraycopy(date, i, alight_time_1a, 0, alight_time_1a.length);
        i += alight_time_1a.length;
        this.alight_time_1a = (String) FileUtils.byte2Parm(alight_time_1a, Type.HEX);


//        String alight_pay_1a;            //交易金额     4   HEX高字节在前
        byte[] alight_pay_1a = new byte[4];
        arraycopy(date, i, alight_pay_1a, 0, alight_pay_1a.length);
        i += alight_pay_1a.length;
        this.alight_pay_1a = (String) FileUtils.byte2Parm(alight_pay_1a, Type.HEX);


//        String mileage;            //乘车里程     2   HEX高字节在前
        byte[] mileage = new byte[2];
        arraycopy(date, i, mileage, 0, mileage.length);
        i += mileage.length;
        this.mileage = (String) FileUtils.byte2Parm(mileage, Type.HEX);


//        String lineno;            //线路编号     1   HEX
        byte[] lineno = new byte[1];
        arraycopy(date, i, lineno, 0, lineno.length);
        i += lineno.length;
        this.lineno = (String) FileUtils.byte2Parm(lineno, Type.HEX);


//        String driverno;            //司机编号     3   HEX
        byte[] driverno = new byte[3];
        arraycopy(date, i, driverno, 0, driverno.length);
        i += driverno.length;
        this.driverno = (String) FileUtils.byte2Parm(driverno, Type.HEX);


//        String allpay;            //优惠前金额     2   HEX
        byte[] allpay = new byte[2];
        arraycopy(date, i, allpay, 0, allpay.length);
        i += allpay.length;
        this.allpay = (String) FileUtils.byte2Parm(allpay, Type.HEX);


//        String res;            //预留     20   HEX
        byte[] res = new byte[20];
        arraycopy(date, i, res, 0, res.length);
        i += res.length;
        this.res = (String) FileUtils.byte2Parm(res, Type.HEX);


        return i;
    }


    public String getAlight_pay_1a() {
        return alight_pay_1a;
    }

    public void setAllpay(int allpay) {
        this.allpay = FileUtils.formatHexStringToByteString(2, FileUtils.bytesToHexString(FileUtils.int2byte2(allpay)));
    }

    public String getAllpay() {
        return allpay;
    }

    public void setTransaction_status_1a(String transaction_status_1a) {
        this.transaction_status_1a = FileUtils.formatHexStringToByteString(1, transaction_status_1a);
    }

    public String getTransaction_status_1a() {
        return transaction_status_1a;
    }

    public int getBoarding_station_1a() {
        return Integer.parseInt(boarding_station_1a, 16);
    }

    public String getAlight_time_1a() {
        return alight_time_1a;
    }

    public String getBoarding_time_1a() {
        return boarding_time_1a;
    }

    public String getBoarding_carno_1a() {
        return boarding_carno_1a;
    }

    public String getBoarding_diraction_1a() {
        return diraction_1a.toUpperCase().equals("AB") ? "00" : "01";
    }

    public void setDiraction_1a(String diraction) {
        diraction_1a = Integer.parseInt(diraction) == 1 ? "AB" : "BA";
    }


    public String getBoarding_lineno_1a() {
        return boarding_lineno_1a;
    }


    public void setLineno(String lineno) {
        this.lineno = FileUtils.formatHexStringToByteString(1, lineno);
    }

    public String getLineno() {
        return lineno;
    }

    public void setDriverno(String driverno) {
        this.driverno = FileUtils.formatHexStringToByteString(3, driverno);
    }

    public void setAllpay(String allpay) {
        this.allpay = FileUtils.formatHexStringToByteString(2, allpay);
    }

    public void setTrade_serial_number_1a(String trade_serial_number_1a) {
        this.trade_serial_number_1a = FileUtils.formatHexStringToByteString(8, trade_serial_number_1a);
    }

    public void setBoarding_city_code_1a(String boarding_city_code_1a) {
        this.boarding_city_code_1a = FileUtils.formatHexStringToByteString(2, boarding_city_code_1a);
    }

    public void setAlight_city_code_1a(String alight_city_code_1a) {
        this.alight_city_code_1a = FileUtils.formatHexStringToByteString(2, alight_city_code_1a);
    }

    public void setBoarding_mark_1a(String boarding_mark_1a) {
        this.boarding_mark_1a = FileUtils.formatHexStringToByteString(8, boarding_mark_1a);
    }

    public void setAlight_mark_1a(String alight_mark_1a) {
        this.alight_mark_1a = FileUtils.formatHexStringToByteString(8, alight_mark_1a);
    }

    public void setBoarding_station_1a(int boarding_station_1a) {
        this.boarding_station_1a = FileUtils.formatHexStringToByteString(1, FileUtils.bytesToHexString(FileUtils.int2byte2(boarding_station_1a)));
    }

    public void setAlight_station_1a(int alight_station_1a) {
        this.alight_station_1a = FileUtils.formatHexStringToByteString(1, FileUtils.bytesToHexString(FileUtils.int2byte2(alight_station_1a)));
    }

    public void setAlight_possn_1a(String alight_possn_1a) {
        this.alight_possn_1a = FileUtils.formatHexStringToByteString(8, alight_possn_1a);
    }

    public void setBoarding_possn_1a(String boarding_possn_1a) {
        this.boarding_possn_1a = FileUtils.formatHexStringToByteString(8, boarding_possn_1a);
    }

    public void setAlight_time_1a(String alight_time_1a) {
        this.alight_time_1a = FileUtils.formatHexStringToByteString(7, alight_time_1a);
    }

    public void setBoarding_time_1a(String boarding_time_1a) {
        this.boarding_time_1a = FileUtils.formatHexStringToByteString(7, boarding_time_1a);
    }

    public void setAlight_lineno_1a(String alight_lineno_1a) {
        this.alight_lineno_1a = FileUtils.formatHexStringToByteString(2, alight_lineno_1a);
    }

    public void setBoarding_lineno_1a(String boarding_lineno_1a) {
        this.boarding_lineno_1a = FileUtils.formatHexStringToByteString(2, boarding_lineno_1a);
    }

    public void setAlight_carno_1a(String alight_carno_1a) {
        this.alight_carno_1a = FileUtils.formatHexStringToByteString(8, alight_carno_1a);
    }

    public void setBoarding_carno_1a(String boarding_carno_1a) {
        this.boarding_carno_1a = FileUtils.formatHexStringToByteString(8, boarding_carno_1a);
    }

    public void setBoarding_pay_all_1a(int boarding_pay_all_1a) {
        this.boarding_pay_all_1a = FileUtils.formatHexStringToByteString(4, FileUtils.bytesToHexString(FileUtils.int2byte2(boarding_pay_all_1a)));
    }

    public void setAlight_pay_1a(int alight_pay_1a) {
        this.alight_pay_1a = FileUtils.formatHexStringToByteString(4, FileUtils.bytesToHexString(FileUtils.int2byte2(alight_pay_1a)));
    }

    public String getAlight_lineno_1a() {
        return alight_lineno_1a;
    }

    public String getAlight_carno_1a() {
        return alight_carno_1a;
    }

    public String getBoarding_pay_all_1a() {
        return boarding_pay_all_1a;
    }

    public String getPaydate() {
        return record_id_1a//    [2] 记录id  记录ID标识 2702  2 HEX
                + record_length_1a                //记录长度   0x7D   1  HEX
                + valid_identification_1a           //应用有效标识      1  HEX
                + interflow_transactions_1a        //互联互通交易标识    1  HEX
                + apply_lock_1a                    //应用锁定标识    1  BCD
                + trade_serial_number_1a        //交易流水号       8   BCD
                + transaction_status_1a            //交易状态     1     BCD
                + boarding_city_code_1a        //上车城市代码     2     BCD
                + boarding_mark_1a            //上车机构标识     8   BCD
                + boarding_operating_1a            //上车运营商代码     2    HEX高字节在前
                + boarding_lineno_1a            //上车线路号     2    HEX高字节在前
                + boarding_station_1a            //上车站点     1    HEX
                + boarding_carno_1a            //上车车辆号     8    ASCII
                + boarding_possn_1a            //上车终端编号     8    BCD
                + boarding_time_1a            //上车时间     7    YYYYMMDDHHMMSS
                + boarding_pay_all_1a            // 标注金额 用于逃票追缴 4HEX高字节在前
                + diraction_1a            // 方向标识 AB ：上行 BA：下行  1 HEX
                + alight_city_code_1a            // 下车城市代码 2     BCD
                + alight_mark_1a            // 下车机构标识 8     BCD
                + alight_operating_1a            // 下车运营商代码 2     HEX高字节在前
                + alight_lineno_1a            // 下车线路号 2     HEX高字节在前
                + alight_station_1a            // 下车线路号 1     BCD
                + alight_carno_1a            // 下车车辆号 8     ASCII
                + alight_possn_1a            // 下车终端编号 8     BCD
                + alight_time_1a            //下车时间     7   YYYYMMDDHHMMSS
                + alight_pay_1a            //交易金额     4   HEX高字节在前
                + mileage            //乘车里程     2   HEX高字节在前
                + lineno            //线路编号     1   HEX
                + driverno            //司机编号     3   HEX
                + allpay            //优惠前金额     2   HEX
                + FileUtils.bytesToHexString(new byte[20]);            //预留     20   HEX
    }

    public String getDriverno() {
        return driverno;
    }
}
