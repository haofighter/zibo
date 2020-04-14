package com.szxb.zibo.config.zibo.RequestDate;

public class PosDownLoadReseponseDate {


    private String task_no; //服务端下发的任务唯一标识
    private String status; //0-下载成功 1-更新成功 -1-任务下载失败 -2-任务更新失败
    private String result_msg; //对应状态码的描述信息
    private Object content; //拓展内容

    public String getTask_no() {
        return task_no;
    }

    public void setTask_no(String task_no) {
        this.task_no = task_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }


    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static class PosUploadInfoDate{
        //{“protocol”:“fastdfs” “ip_port”:” 139.9.113.219: 22000”,”group”:”group1”,”path”:””,”file_name”:”xxx.log” }
        String protocol;
        String ip_port;
        String group;
        String path;
        String file_name;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getIp_port() {
            return ip_port;
        }

        public void setIp_port(String ip_port) {
            this.ip_port = ip_port;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

   }


}
