package com.szxb.zibo.util.md5;

import android.util.Log;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.TreeMap;

public class MD5 {
    //盐，用于混交md5
    private static final String slat = "chezaijitogprs";

    public static String md5(String dataStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        byte[] byteArray;
        try {
            byteArray = dataStr.getBytes("UTF8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            Log.i("加密后的数据", hexValue.toString());
            return hexValue.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }


    private static TreeMap<String, Object> sortParam(Object o) {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        Class clazz = o.getClass();
        Field[] fields = clazz.getFields();
        Field[] fieldAlls = clazz.getDeclaredFields();
        for (Field field : fieldAlls) {
            try {
                field.setAccessible(true);
                String fn = field.getName();
                treeMap.put(fn, field.get(o));
                for (Field fie : fields) {
                    if (fn.equals(fie.getName())) {
                        treeMap.remove(fn);
                    } else if (fn.equals("sign")) {
                        treeMap.remove(fn);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return treeMap;
    }


    public static String getStringForMd5(Object o) {
        TreeMap<String, Object> treeMap = sortParam(o);
        String str = "";
        Iterator iter = treeMap.keySet().iterator();
        while (iter.hasNext()) {
            // 获取key
            String key = (String) iter.next();

            if (treeMap.get(key) != null) {
                if (treeMap.get(key) instanceof String) {
                    str += key + "=" + treeMap.get(key) + "&";
                } else {
                    str += key + "=" + sortParamJson(treeMap.get(key)) + "&";
                }
            }
        }
        str = str + "key=" + slat;
        Log.i("加密前的数据", str);
        return md5(str);
    }


    public static String sortParamJson(Object o) {
        TreeMap<String, Object> treeMap = sortParam(o);
        String str = "{";
        Iterator iter = treeMap.keySet().iterator();
        while (iter.hasNext()) {
            // 获取key
            String key = (String) iter.next();
            if (treeMap.get(key) != null) {
                if(treeMap.get(key) instanceof String) {
                    str += "\"" + key + "\":\"" + treeMap.get(key) + "\",";
                }else{
                    str += "\"" + key + "\":"+ sortParamJson(treeMap.get(key))+",";
                }
            }
        }
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        return str + "}";
    }



    public static String Make_CRC(byte[] data) {
        byte[] buf = new byte[data.length];// 存储需要产生校验码的数据
        for (int i = 0; i < data.length; i++) {
            buf[i] = data[i];
        }
        int len = buf.length;
        int crc = 0xFFFF;//16位
        for (int pos = 0; pos < len; pos++) {
            if (buf[pos] < 0) {
                crc ^= (int) buf[pos] + 256; // XOR byte into least sig. byte of
                // crc
            } else {
                crc ^= (int) buf[pos]; // XOR byte into least sig. byte of crc
            }
            for (int i = 8; i != 0; i--) { // Loop over each bit
                if ((crc & 0x0001) != 0) { // If the LSB is set
                    crc >>= 1; // Shift right and XOR 0xA001
                    crc ^= 0xA001;
                } else
                    // Else LSB is not set
                    crc >>= 1; // Just shift right
            }
        }
        String c = Integer.toHexString(crc);
        if (c.length() == 4) {
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 3) {
            c = "0" + c;
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 2) {
            c = "0" + c.substring(1, 2) + "0" + c.substring(0, 1);
        }
        return c;
    }
}