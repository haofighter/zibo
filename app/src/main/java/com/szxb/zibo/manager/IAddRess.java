package com.szxb.zibo.manager;

import com.szxb.zibo.db.bean.FTPEntity;

public interface IAddRess {

    void setFtpIp(String ip);

    String getFtpIP();

    void setPort(int port);

    int getPort();

    void setFtpPsw(String psw);

    String getFtpPsw();

    void setFtpUser(String user);

    String getFtpUser();

    void setUrlIp(String ip);

    String getUrlIp();

    void setFTP(FTPEntity ftp);

    FTPEntity getFTP();
}
