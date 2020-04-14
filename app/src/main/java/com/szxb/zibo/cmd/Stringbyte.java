package com.szxb.zibo.cmd;

/**
 * Created by lilei on 18-1-25.
 */

public class Stringbyte {

    static final String TAG = "Stringbyte";
    static final String FILENAME = "Parm";

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String bytesToHexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || len <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    int memcmp(byte[] buf1, byte[] buf2, int len) {
        int i = 0;

        for (i = 0; i < len; i++) {
            if (((int) buf1[i] & 0xff) > ((int) buf2[i] & 0xff)) {
                return 1;
            } else if (((int) buf1[i] & 0xff) < ((int) buf2[i] & 0xff)) {
                return -1;
            }
        }

        return 0;
    }

}
