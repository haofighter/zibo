package com.szxb.zibo.moudle.function.card.M1;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_9 {
    //块0x8
    int blanace;                        //钱包
    String blanace_inverse_code;        //4字节钱包反码
    String blanace_;                    //钱包 同blanace
    String verify;                        //校验

    public int praseBlock(int i, byte[] date) {

        //钱包
        byte[] blanace = new byte[4];
        arraycopy(date, i, blanace, 0, blanace.length);
        i += blanace.length;
        String b = FileUtils.bytesToHexString(blanace);


        //4字节钱包反码
        byte[] blanace_inverse_code = new byte[4];
        arraycopy(date, i, blanace_inverse_code, 0, blanace_inverse_code.length);
        i += blanace_inverse_code.length;
        this.blanace_inverse_code = (String) FileUtils.byte2Parm(blanace_inverse_code, Type.HEX);

        if(b.endsWith("ff")) {
            this.blanace = -FileUtils.byte2int(blanace_inverse_code);
        }else{
            this.blanace = FileUtils.byte2int(blanace);
        }

        //钱包
        byte[] blanace_ = new byte[4];
        arraycopy(date, i, blanace_, 0, blanace_.length);
        i += blanace_.length;
        this.blanace_ = (String) FileUtils.byte2Parm(blanace_, Type.HEX);

        //校验
        byte[] verify = new byte[4];
        arraycopy(date, i, verify, 0, verify.length);
        i += verify.length;
        this.verify = (String) FileUtils.byte2Parm(verify, Type.HEX);

        return i;
    }

    public int getBlanace() {
        return blanace;
    }

    public void setBlanace(int blanace) {
        this.blanace = blanace;
    }

    public String getBlanace_inverse_code() {
        return blanace_inverse_code;
    }

    public void setBlanace_inverse_code(String blanace_inverse_code) {
        this.blanace_inverse_code = blanace_inverse_code;
    }

    public String getBlanace_() {
        return blanace_;
    }

    public void setBlanace_(String blanace_) {
        this.blanace_ = blanace_;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    @Override
    public String toString() {
        return "Block_9{" + blanace  + blanace_inverse_code +  blanace_  + verify  +
                '}';
    }
}
