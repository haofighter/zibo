package com.szxb.zibo.moudle.function.card.M1;


import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.Type;

import static java.lang.System.arraycopy;

public class Block_A {
    //块0x9  块0x8的副本
    int blanace_2;                    //钱包
    String blanace_inverse_code_2;        //4字节钱包反码
    String blanace__2;                    //钱包
    String verify_2;                    //校验
    public int praseBlock(int i, byte[] date) {

        //钱包
        byte[] blanace = new byte[4];
        arraycopy(date, i, blanace, 0, blanace.length);
        i += blanace.length;
        this.blanace_2 = FileUtils.byte2int(blanace);
        String b = FileUtils.bytesToHexString(blanace);

        //4字节钱包反码
        byte[] blanace_inverse_code_2 = new byte[4];
        arraycopy(date, i, blanace_inverse_code_2, 0, blanace_inverse_code_2.length);
        i += blanace_inverse_code_2.length;
        this.blanace_inverse_code_2 = (String) FileUtils.byte2Parm(blanace_inverse_code_2, Type.HEX);


        if(b.endsWith("FF")) {
            this.blanace_2 = -FileUtils.byte2int(blanace_inverse_code_2);
        }else{
            this.blanace_2 = FileUtils.byte2int(blanace);
        }

        //钱包
        byte[] blanace__2 = new byte[4];
        arraycopy(date, i, blanace__2, 0, blanace__2.length);
        i += blanace__2.length;
        this.blanace__2 = (String) FileUtils.byte2Parm(blanace__2, Type.HEX);

        //校验
        byte[] verify_2 = new byte[4];
        arraycopy(date, i, verify_2, 0, verify_2.length);
        i += verify_2.length;
        this.verify_2 = (String) FileUtils.byte2Parm(verify_2, Type.HEX);

        return i;
    }

    @Override
    public String toString() {
        return "Block_A{" + blanace_2 +  blanace_inverse_code_2 + blanace__2 + verify_2 +
                '}';
    }

    public int getBlanace() {
        return blanace_2;
    }
}
