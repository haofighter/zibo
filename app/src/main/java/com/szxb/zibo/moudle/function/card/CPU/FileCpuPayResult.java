package com.szxb.zibo.moudle.function.card.CPU;


import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.Type;

import static java.lang.System.arraycopy;

public class FileCpuPayResult {
    //--------------------------------------------------------------------------------------------------消费初始化
    String card_remain;                //卡片余额 消费前
    String transaction_number;        //交易序号
    String overdrawn_account;        //透支限额
    String key_version;                //密钥版本号
    String key_flag;                    //密钥标识
    String random_number;            //伪随机数
    //--------------------------------------------------------------------------------------------------PSAM卡计算的MAC1
    String sam_transaction_number; //终端交易序号
    String MAC1;                   //MAC1
    //--------------------------------------------------------------------------------------------------验证MAC1，并返回
    String TAC;                     //交易认证码
    String MAC2;                   //MAC2
    //--------------------------------------------------------------------------------------------------PSAM卡验证MAC2，获取余额
    String balance;                    //余额
    //--------------------------------------------------------------------------------------------------卡片信息
    String uid;                        //唯一标识
    String card_id;                   //应用序列号
    String card_type;                    //卡类型

    public void praseResult(int i, byte[] date) {

        //card_remain[4];                //卡片余额
        byte[] card_remain = new byte[4];
        arraycopy(date, i, card_remain, 0, card_remain.length);
        i += card_remain.length;
        this.card_remain = (String) FileUtils.byte2Parm(card_remain, Type.HEX);


        //transaction_number[2];        //交易序号
        byte[] transaction_number = new byte[2];
        arraycopy(date, i, transaction_number, 0, transaction_number.length);
        i += transaction_number.length;
        this.transaction_number = (String) FileUtils.byte2Parm(transaction_number, Type.HEX);

        //overdrawn_account[3];        //透支限额
        byte[] overdrawn_account = new byte[3];
        arraycopy(date, i, overdrawn_account, 0, overdrawn_account.length);
        i += overdrawn_account.length;
        this.overdrawn_account = (String) FileUtils.byte2Parm(overdrawn_account, Type.HEX);

        //key_version;                //密钥版本号
        byte[] key_version = new byte[1];
        arraycopy(date, i, key_version, 0, key_version.length);
        i += key_version.length;
        this.key_version = (String) FileUtils.byte2Parm(key_version, Type.HEX);

        //key_flag;                    //密钥标识
        byte[] key_flag = new byte[1];
        arraycopy(date, i, key_flag, 0, key_flag.length);
        i += key_flag.length;
        this.key_flag = (String) FileUtils.byte2Parm(key_flag, Type.HEX);

        //random_number[4];            //伪随机数
        byte[] random_number = new byte[4];
        arraycopy(date, i, random_number, 0, random_number.length);
        i += random_number.length;
        this.random_number = (String) FileUtils.byte2Parm(random_number, Type.HEX);

        //sam_transaction_number[4]; //终端交易序号
        byte[] sam_transaction_number = new byte[4];
        arraycopy(date, i, sam_transaction_number, 0, sam_transaction_number.length);
        i += sam_transaction_number.length;
        this.sam_transaction_number = (String) FileUtils.byte2Parm(sam_transaction_number, Type.HEX);

        //MAC1[4];                   //MAC1
        byte[] MAC1 = new byte[4];
        arraycopy(date, i, MAC1, 0, MAC1.length);
        i += MAC1.length;
        this.MAC1 = (String) FileUtils.byte2Parm(MAC1, Type.HEX);

        //TAC[4];                     //交易认证码
        byte[] TAC = new byte[4];
        arraycopy(date, i, TAC, 0, TAC.length);
        i += TAC.length;
        this.TAC = (String) FileUtils.byte2Parm(TAC, Type.HEX);

        //MAC2[4];                   //MAC2
        byte[] MAC2 = new byte[4];
        arraycopy(date, i, MAC2, 0, MAC2.length);
        i += MAC2.length;
        this.MAC2 = (String) FileUtils.byte2Parm(MAC2, Type.HEX);

        //balance[4];                    //余额
        byte[] balance = new byte[4];
        arraycopy(date, i, balance, 0, balance.length);
        i += balance.length;
        this.balance = (String) FileUtils.byte2Parm(balance, Type.HEX);


        //uid[4];                        //唯一标识
        byte[] uid = new byte[4];
        arraycopy(date, i, uid, 0, uid.length);
        i += uid.length;
        this.uid = (String) FileUtils.byte2Parm(uid, Type.HEX);

        //card_id[8];                   //应用序列号
        byte[] card_id = new byte[8];
        arraycopy(date, i, card_id, 0, card_id.length);
        i += card_id.length;
        this.card_id = (String) FileUtils.byte2Parm(card_id, Type.HEX);


        //card_type;                    //卡类型
        byte[] card_type = new byte[1];
        arraycopy(date, i, card_type, 0, card_type.length);
        i += card_type.length;
        this.card_type = (String) FileUtils.byte2Parm(card_type, Type.HEX);
    }


    public String getCard_remain() {
        return card_remain;
    }

    public void setCard_remain(String card_remain) {
        this.card_remain = card_remain;
    }

    public String getTransaction_number() {
        return transaction_number;
    }

    public void setTransaction_number(String transaction_number) {
        this.transaction_number = transaction_number;
    }

    public String getOverdrawn_account() {
        return overdrawn_account;
    }

    public void setOverdrawn_account(String overdrawn_account) {
        this.overdrawn_account = overdrawn_account;
    }

    public String getKey_version() {
        return key_version;
    }

    public void setKey_version(String key_version) {
        this.key_version = key_version;
    }

    public String getKey_flag() {
        return key_flag;
    }

    public void setKey_flag(String key_flag) {
        this.key_flag = key_flag;
    }

    public String getRandom_number() {
        return random_number;
    }

    public void setRandom_number(String random_number) {
        this.random_number = random_number;
    }

    public String getSam_transaction_number() {
        return sam_transaction_number;
    }

    public void setSam_transaction_number(String sam_transaction_number) {
        this.sam_transaction_number = sam_transaction_number;
    }

    public String getMAC1() {
        return MAC1;
    }

    public void setMAC1(String MAC1) {
        this.MAC1 = MAC1;
    }

    public String getTAC() {
        return TAC;
    }

    public void setTAC(String TAC) {
        this.TAC = TAC;
    }

    public String getMAC2() {
        return MAC2;
    }

    public void setMAC2(String MAC2) {
        this.MAC2 = MAC2;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }
}
