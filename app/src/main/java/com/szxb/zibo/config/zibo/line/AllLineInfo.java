package com.szxb.zibo.config.zibo.line;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

public class AllLineInfo {

    private List<ZBLineInfo> allline;

    public List<ZBLineInfo> getAllline() {
        return allline;
    }

    public void setAllline(List<ZBLineInfo> allline) {
        this.allline = allline;
    }

}
