package com.szxb.zibo.moudle.function.card.M1;

import com.hao.lib.Util.FileUtils;
import com.hao.lib.Util.Type;
import com.szxb.zibo.base.BusApp;

import static java.lang.System.arraycopy;

public class Block_11 {

    String company;//公交公司编号
    /**
     * 后4字节为司机号（第3字节是0，实际后3字节是司机号（6位）
     */
    String res;
    String driverNum;

    public int praseBlock(int i, byte[] date, String cardtype) {
        if (cardtype.equals("06")) {//司机卡解析
            byte[] company = new byte[2];
            arraycopy(date, i, company, 0, company.length);
            i += company.length;
            this.company = (String) FileUtils.byte2Parm(company, Type.HEX);

            byte[] res = new byte[1];
            arraycopy(date, i, res, 0, res.length);
            i += res.length;
            this.res = (String) FileUtils.byte2Parm(res, Type.HEX);

            byte[] driverNum = new byte[3];
            arraycopy(date, i, driverNum, 0, driverNum.length);
            i += driverNum.length;
            this.driverNum = (String) FileUtils.byte2Parm(driverNum, Type.HEX);
        }

        return i;
    }

    public String getCompany() {
        return company;
    }

    public String getRes() {
        return res;
    }

    public String getDriverNum() {
        return driverNum;
    }

    @Override
    public String toString() {
        return "Block_11{" + company + res +  driverNum +
                '}';
    }
}
