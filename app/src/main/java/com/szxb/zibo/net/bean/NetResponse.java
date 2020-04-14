package com.szxb.zibo.net.bean;

import java.util.List;

public class NetResponse {


    private String result;
    private DataBean data;
    private String rescode;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getRescode() {
        return rescode;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }


    public static class DataBean {
        /**
         * keys : [{"mac_key":"E7BBD0BD7769B1BF73B5AE07A2D5D733","key_id":"1"},{"mac_key":"B431AF7CD446C6221ACF9395B50C8C8E","key_id":"2"},{"mac_key":"C3F6FD41C4A7F558AD5789DE4B474DE0","key_id":"3"},{"mac_key":"EF9BF841012ACDF8D09322873B24EA70","key_id":"4"},{"mac_key":"97CBB4CAB13D91D8AB73B9FA5641A9F6","key_id":"5"},{"mac_key":"7C040960A8794D8D6EA4FCD764FEDB73","key_id":"6"},{"mac_key":"43100ADE5632616F2629070779EB07E5","key_id":"7"},{"mac_key":"C5E2A32E8EC50CFDF1A8721D73C2F625","key_id":"8"}]
         * curVersion : 1
         */

        private List<KeysInfo> keys;
        private int curVersion;
        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<KeysInfo> getKeys() {
            return keys;
        }

        public void setKeys(List<KeysInfo> keys) {
            this.keys = keys;
        }

        public int getCurVersion() {
            return curVersion;
        }

        public void setCurVersion(int curVersion) {
            this.curVersion = curVersion;
        }


    }

    public static class KeysInfo {
        /**
         * mac_key : E7BBD0BD7769B1BF73B5AE07A2D5D733
         * key_id : 1
         */

        private String mac_key;
        private String key_id;
        private String pub_key;
        private String public_key;//支付宝

        public String getPub_key() {
            return pub_key;
        }

        public void setPub_key(String pub_key) {
            this.pub_key = pub_key;
        }

        public String getMac_key() {
            return mac_key;
        }

        public void setMac_key(String mac_key) {
            this.mac_key = mac_key;
        }

        public String getKey_id() {
            return key_id;
        }

        public void setKey_id(String key_id) {
            this.key_id = key_id;
        }
    }
}
