package com.szxb.zibo.config.zibo;

public class Result {


    /**
     * customer_code : 881641
     * sign : 599E3674D9EFD3FAAA9B13B7FDEEE9CD
     * req_type : 1
     * timestamp : 1565777898694
     * result_msg : 请求成功！
     * merchant_no : 001
     * seq_no : 20190814000008
     * server_time : 1565777898
     * charset : UTF-8
     * trans_data : {"request_code":"lin","server_time":"1565777898","task_no":"3c9cf3ef90444a4fa99cff3427a3d757","request_content":{"protocol":"fastdfs","packet_size":"18324","httpUrl":"http://192.168.6.43:10091/group1/M00/00/01/wKgGK11SOwqANjR3AABHlLDTP1I929.lin/20190813122215_D5C6.lin","group_name":"group1","url":"Fastdfs://120.220.53.11:22000/group1/M00/00/01/wKgGK11SOwqANjR3AABHlLDTP1I929.lin/20190813122215_D5C6.lin","md5":"d7833396dd6ad268c38946722a0ebec4"}}
     * channel_code : 0000001
     * version : 1.0.0
     * result_code : 0
     * terminal_type : Q6-B
     * terminal_no : Q6B5B1T219263019
     * sign_type : MD5
     */

    private String customer_code;
    private String sign;
    private String req_type;
    private String timestamp;
    private String result_msg;
    private String merchant_no;
    private String seq_no;
    private String server_time;
    private String charset;
    private TransDataBean trans_data;
    private String channel_code;
    private String version;
    private String result_code;
    private String terminal_type;
    private String terminal_no;
    private String sign_type;

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(String seq_no) {
        this.seq_no = seq_no;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public TransDataBean getTrans_data() {
        return trans_data;
    }

    public void setTrans_data(TransDataBean trans_data) {
        this.trans_data = trans_data;
    }

    public String getChannel_code() {
        return channel_code;
    }

    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getTerminal_type() {
        return terminal_type;
    }

    public void setTerminal_type(String terminal_type) {
        this.terminal_type = terminal_type;
    }

    public String getTerminal_no() {
        return terminal_no;
    }

    public void setTerminal_no(String terminal_no) {
        this.terminal_no = terminal_no;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class TransDataBean {
        /**
         * request_code : lin
         * server_time : 1565777898
         * task_no : 3c9cf3ef90444a4fa99cff3427a3d757
         * request_content : {"protocol":"fastdfs","packet_size":"18324","httpUrl":"http://192.168.6.43:10091/group1/M00/00/01/wKgGK11SOwqANjR3AABHlLDTP1I929.lin/20190813122215_D5C6.lin","group_name":"group1","url":"Fastdfs://120.220.53.11:22000/group1/M00/00/01/wKgGK11SOwqANjR3AABHlLDTP1I929.lin/20190813122215_D5C6.lin","md5":"d7833396dd6ad268c38946722a0ebec4"}
         */

        private String request_code;
        private String server_time;
        private String task_no;
        private RequestContentBean request_content;

        public String getRequest_code() {
            return request_code;
        }

        public void setRequest_code(String request_code) {
            this.request_code = request_code;
        }

        public String getServer_time() {
            return server_time;
        }

        public void setServer_time(String server_time) {
            this.server_time = server_time;
        }

        public String getTask_no() {
            return task_no;
        }

        public void setTask_no(String task_no) {
            this.task_no = task_no;
        }

        public RequestContentBean getRequest_content() {
            return request_content;
        }

        public void setRequest_content(RequestContentBean request_content) {
            this.request_content = request_content;
        }

        public static class RequestContentBean {
            /**
             * protocol : fastdfs
             * packet_size : 18324
             * httpUrl : http://192.168.6.43:10091/group1/M00/00/01/wKgGK11SOwqANjR3AABHlLDTP1I929.lin/20190813122215_D5C6.lin
             * group_name : group1
             * url : Fastdfs://120.220.53.11:22000/group1/M00/00/01/wKgGK11SOwqANjR3AABHlLDTP1I929.lin/20190813122215_D5C6.lin
             * md5 : d7833396dd6ad268c38946722a0ebec4
             */

            private String protocol;
            private String packet_size;
            private String httpUrl;
            private String group_name;
            private String url;
            private String md5;
            private String ums_terminal_no;
            private String ums_tenant_no;
            private String kek;

            public String getProtocol() {
                return protocol;
            }

            public void setProtocol(String protocol) {
                this.protocol = protocol;
            }

            public String getPacket_size() {
                return packet_size;
            }

            public void setPacket_size(String packet_size) {
                this.packet_size = packet_size;
            }

            public String getHttpUrl() {
                return httpUrl;
            }

            public void setHttpUrl(String httpUrl) {
                this.httpUrl = httpUrl;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
            }

            public String getUms_terminal_no() {
                return ums_terminal_no;
            }

            public void setUms_terminal_no(String ums_terminal_no) {
                this.ums_terminal_no = ums_terminal_no;
            }

            public String getUms_tenant_no() {
                return ums_tenant_no;
            }

            public void setUms_tenant_no(String ums_tenant_no) {
                this.ums_tenant_no = ums_tenant_no;
            }

            public String getKek() {
                return kek;
            }

            public void setKek(String kek) {
                this.kek = kek;
            }
        }
    }
}
