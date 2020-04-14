package com.szxb.zibo.record;

import java.util.List;

public class CardUpResponse {

    /**
     * dataList : [{"cardNo":"9730000000441192","uid":"bed4bd3a","result":"消费流水登记成功","termid":"Q6B5A1T218350019","tradeTime":"2019-05-18 13:35:51","rescode":"0000"}]
     * result : 消费流水已登记成功
     * rescode : 0000
     */

    private String result;
    private String rescode;
    private List<DataListBean> dataList;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRescode() {
        return rescode;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        /**
         * cardNo : 9730000000441192
         * uid : bed4bd3a
         * result : 消费流水登记成功
         * termid : Q6B5A1T218350019
         * tradeTime : 2019-05-18 13:35:51
         * rescode : 0000
         */

        private String cardNo;
        private String uid;
        private String result;
        private String termid;
        private String tradeTime;
        private String rescode;

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getTermid() {
            return termid;
        }

        public void setTermid(String termid) {
            this.termid = termid;
        }

        public String getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(String tradeTime) {
            this.tradeTime = tradeTime;
        }

        public String getRescode() {
            return rescode;
        }

        public void setRescode(String rescode) {
            this.rescode = rescode;
        }

        @Override
        public String toString() {
            return "DataListBean{" +
                    "cardNo='" + cardNo + '\'' +
                    ", uid='" + uid + '\'' +
                    ", result='" + result + '\'' +
                    ", termid='" + termid + '\'' +
                    ", tradeTime='" + tradeTime + '\'' +
                    ", rescode='" + rescode + '\'' +
                    '}';
        }
    }
}
