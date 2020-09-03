package com.szxb.zibo.moudle.function.card.JTB;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.MiLog;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

//CPU卡消费 命令数据
public class FileJTBPayResult {
    //--------------------------------------------------------------------------------------------------消费初始化
    String card_remain;            //卡片余额
    String transaction_number;    //交易序号
    String overdrawn_account;    //透支限额
    String key_version;            //密钥版本号
    String key_flag;                //密钥标识
    String random_number;            //伪随机数
    //--------------------------------------------------------------------------------------------------PSAM卡计算的MAC1
    String sam_transaction_number; //终端交易序号
    String MAC1;                   //MAC1
    //--------------------------------------------------------------------------------------------------验证MAC1，并返回
    String TAC;                     //交易认证码
    String MAC2;                   //MAC2
    //--------------------------------------------------------------------------------------------------卡片信息
    String uid;                        //唯一标识
    String card_id;                   //应用主账号后8字节(组MAC1用)//取自15文件pan号
    String card_type;                    //卡类型
    //--------------------------------------------------------------------------------------------------PSAM卡验证MAC2，获取余额
    String balance;                    //余额

    public void praseDate(int i, byte[] date) {

//        uint8_t card_remain[4];            //卡片余额
        byte[] card_remain = new byte[4];
        arraycopy(date, i, card_remain, 0, card_remain.length);
        i += card_remain.length;
        this.card_remain = (String) FileUtils.byte2Parm(card_remain, Type.HEX);

//        uint8_t transaction_number[2];    //交易序号
        byte[] transaction_number = new byte[2];
        arraycopy(date, i, transaction_number, 0, transaction_number.length);
        i += transaction_number.length;
        this.transaction_number = (String) FileUtils.byte2Parm(transaction_number, Type.HEX);

//        uint8_t overdrawn_account[3];    //透支限额
        byte[] overdrawn_account = new byte[3];
        arraycopy(date, i, overdrawn_account, 0, overdrawn_account.length);
        i += overdrawn_account.length;
        this.overdrawn_account = (String) FileUtils.byte2Parm(overdrawn_account, Type.HEX);

//        uint8_t key_version;            //密钥版本号
        byte[] key_version = new byte[1];
        arraycopy(date, i, key_version, 0, key_version.length);
        i += key_version.length;
        this.key_version = (String) FileUtils.byte2Parm(key_version, Type.HEX);

//        uint8_t key_flag;                //密钥标识
        byte[] key_flag = new byte[1];
        arraycopy(date, i, key_flag, 0, key_flag.length);
        i += key_flag.length;
        this.key_flag = (String) FileUtils.byte2Parm(key_flag, Type.HEX);

//        uint8_t random_number[4];            //伪随机数
        byte[] random_number = new byte[4];
        arraycopy(date, i, random_number, 0, random_number.length);
        i += random_number.length;
        this.random_number = (String) FileUtils.byte2Parm(random_number, Type.HEX);

//        uint8_t sam_transaction_number[4]; //终端交易序号
        byte[] sam_transaction_number = new byte[4];
        arraycopy(date, i, sam_transaction_number, 0, sam_transaction_number.length);
        i += sam_transaction_number.length;
        this.sam_transaction_number = (String) FileUtils.byte2Parm(sam_transaction_number, Type.HEX);

//        uint8_t MAC1[4];                   //MAC1
        byte[] MAC1 = new byte[4];
        arraycopy(date, i, MAC1, 0, MAC1.length);
        i += MAC1.length;
        this.MAC1 = (String) FileUtils.byte2Parm(MAC1, Type.HEX);

//        uint8_t TAC[4];                     //交易认证码
        byte[] TAC = new byte[4];
        arraycopy(date, i, TAC, 0, TAC.length);
        i += TAC.length;
        this.TAC = (String) FileUtils.byte2Parm(TAC, Type.HEX);

//        uint8_t MAC2[4];                   //MAC2
        byte[] MAC2 = new byte[4];
        arraycopy(date, i, MAC2, 0, MAC2.length);
        i += MAC2.length;
        this.MAC2 = (String) FileUtils.byte2Parm(MAC2, Type.HEX);

//        uint8_t uid[4];                        //唯一标识
        byte[] uid = new byte[4];
        arraycopy(date, i, uid, 0, uid.length);
        i += uid.length;
        this.uid = (String) FileUtils.byte2Parm(uid, Type.HEX);

//        uint8_t card_id[8] ;                   //应用主账号后8字节(组MAC1用)//取自15文件pan号
        byte[] card_id = new byte[8];
        arraycopy(date, i, card_id, 0, card_id.length);
        i += card_id.length;
        this.card_id = (String) FileUtils.byte2Parm(card_id, Type.HEX);

//        uint8_t card_type;                    //卡类型
        byte[] card_type = new byte[1];
        arraycopy(date, i, card_type, 0, card_type.length);
        i += card_type.length;
        this.card_type = (String) FileUtils.byte2Parm(card_type, Type.HEX);

//        uint8_t balance[4];
        byte[] balance = new byte[4];
        arraycopy(date, i, balance, 0, balance.length);
        i += balance.length;
        this.balance = (String) FileUtils.byte2Parm(balance, Type.HEX);

    }

    public int getBalance() {
        return Integer.parseInt(balance,16);
    }

    public String getTAC() {
        return TAC;
    }
}
