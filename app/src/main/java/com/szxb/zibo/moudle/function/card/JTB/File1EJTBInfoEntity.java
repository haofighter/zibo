package com.szxb.zibo.moudle.function.card.JTB;

import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.Type;

import static java.lang.System.arraycopy;

//-------------------------------18文件
public class File1EJTBInfoEntity {
    String transaction_type_1e;            //交易类型
    String terminal_number_1e;            //终端编号
    String trade_serial_number_1e;        //交易流水号
    String transaction_amount_1e;        //交易金额
    String transaction_balance_1e;        //交易后余额
    String transaction_time_1e;            //交易日期时间
    String transaction_city_code_1e;    //受理方城市代码
    String institutional_identity_1e;    //受理方机构标识
    String reserve2_1e;                    //本规范预留


    public int praseFile(int i, byte[] date) {

        byte[] transaction_type_1e = new byte[1];
        arraycopy(date, i, transaction_type_1e, 0, transaction_type_1e.length);
        i += transaction_type_1e.length;
        this.transaction_type_1e = (String) FileUtils.byte2Parm(transaction_type_1e, Type.HEX);

        byte[] terminal_number_1e = new byte[8];
        arraycopy(date, i, terminal_number_1e, 0, terminal_number_1e.length);
        i += terminal_number_1e.length;
        this.terminal_number_1e = (String) FileUtils.byte2Parm(terminal_number_1e, Type.HEX);

        byte[] trade_serial_number_1e = new byte[8];
        arraycopy(date, i, trade_serial_number_1e, 0, trade_serial_number_1e.length);
        i += trade_serial_number_1e.length;
        this.trade_serial_number_1e = (String) FileUtils.byte2Parm(trade_serial_number_1e, Type.HEX);

        byte[] transaction_amount_1e = new byte[4];
        arraycopy(date, i, transaction_amount_1e, 0, transaction_amount_1e.length);
        i += transaction_amount_1e.length;
        this.transaction_amount_1e = (String) FileUtils.byte2Parm(transaction_amount_1e, Type.HEX);

        byte[] transaction_balance_1e = new byte[4];
        arraycopy(date, i, transaction_balance_1e, 0, transaction_balance_1e.length);
        i += transaction_balance_1e.length;
        this.transaction_balance_1e = (String) FileUtils.byte2Parm(transaction_balance_1e, Type.HEX);

        byte[] transaction_time_1e = new byte[7];
        arraycopy(date, i, transaction_time_1e, 0, transaction_time_1e.length);
        i += transaction_time_1e.length;
        this.transaction_time_1e = (String) FileUtils.byte2Parm(transaction_time_1e, Type.HEX);


        byte[] transaction_city_code_1e = new byte[2];
        arraycopy(date, i, transaction_city_code_1e, 0, transaction_city_code_1e.length);
        i += transaction_city_code_1e.length;
        this.transaction_city_code_1e = (String) FileUtils.byte2Parm(transaction_city_code_1e, Type.HEX);

        byte[] institutional_identity_1e = new byte[8];
        arraycopy(date, i, institutional_identity_1e, 0, institutional_identity_1e.length);
        i += institutional_identity_1e.length;
        this.institutional_identity_1e = (String) FileUtils.byte2Parm(institutional_identity_1e, Type.HEX);

        byte[] reserve2_1e = new byte[6];
        arraycopy(date, i, reserve2_1e, 0, reserve2_1e.length);
        i += reserve2_1e.length;
        this.reserve2_1e = (String) FileUtils.byte2Parm(reserve2_1e, Type.HEX);

        return i;
    }

    public void setTransaction_type_1e(String transaction_type_1e) {//1
        this.transaction_type_1e = FileUtils.formatHexStringToByteString(1,transaction_type_1e);
    }

    public void setTerminal_number_1e(String terminal_number_1e) {//8
        this.terminal_number_1e = FileUtils.formatHexStringToByteString(8,terminal_number_1e);
    }

    public void setTrade_serial_number_1e(String trade_serial_number_1e) {//8
        this.trade_serial_number_1e = FileUtils.formatHexStringToByteString(8,trade_serial_number_1e);
    }

    public void setTransaction_amount_1e(int pay) {//4
        transaction_amount_1e=FileUtils.formatHexStringToByteString(4, FileUtils.bytesToHexString(FileUtils.int2byte2(pay)));
    }

    public void setTransaction_balance_1e(int payAfter) {//4
        transaction_balance_1e=FileUtils.formatHexStringToByteString(4, FileUtils.bytesToHexString(FileUtils.int2byte2(payAfter)));
    }

    public void setTransaction_time_1e(String transaction_time_1e) {//7
        this.transaction_time_1e = FileUtils.formatHexStringToByteString(7,transaction_time_1e);
    }

    public void setTransaction_city_code_1e(String transaction_city_code_1e) {//2
        this.transaction_city_code_1e = FileUtils.formatHexStringToByteString(2,transaction_city_code_1e);
    }

    public void setInstitutional_identity_1e(String institutional_identity_1e) {//8
        this.institutional_identity_1e = FileUtils.formatHexStringToByteString(8,institutional_identity_1e);
    }

    public void setReserve2_1e(String reserve2_1e) {//6
        this.reserve2_1e = FileUtils.formatHexStringToByteString(6,reserve2_1e);
    }


    public String getTransaction_time_1e() {
        return transaction_time_1e;
    }

    public String getTransaction_amount_1e() {
        return transaction_amount_1e;
    }

    public String getTrade_serial_number_1e() {
        return trade_serial_number_1e;
    }

    public String getPayDate(){
        return   transaction_type_1e+            //交易类型
         terminal_number_1e+            //终端编号
         trade_serial_number_1e+        //交易流水号
         transaction_amount_1e+        //交易金额
         transaction_balance_1e+        //交易后余额
         transaction_time_1e+            //交易日期时间
         transaction_city_code_1e+    //受理方城市代码
         institutional_identity_1e+    //受理方机构标识
         reserve2_1e;                    //本规范预留
    }







}
