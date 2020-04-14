package com.szxb.zibo.record;

import java.util.List;

public class UnionUpResponse {


    /**
     * result : 消费流水已登记成功
     * datalist : [{"result":"上传正常,此流水号正常","rescode":"0002","uniqueFlag":"725007000001"},{"result":"上传失败,订单重复","rescode":"0000","uniqueFlag":"603003000001"}]
     * rescode : 0000
     */

    private String result;
    private String rescode;
    private List<DatalistBean> datalist;

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

    public List<DatalistBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<DatalistBean> datalist) {
        this.datalist = datalist;
    }

    public static class DatalistBean {
        /**
         * result : 上传正常,此流水号正常
         * rescode : 0002
         * uniqueFlag : 725007000001
         */

        private String result;
        private String rescode;
        private String uniqueFlag;

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

        public String getUniqueFlag() {
            return uniqueFlag;
        }

        public void setUniqueFlag(String uniqueFlag) {
            this.uniqueFlag = uniqueFlag;
        }
    }
}
