package com.szxb.zibo.config.haikou;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Black {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String cardNum;
    private String type;

    @Generated(hash = 1355401250)
    public Black(Long id, String cardNum, String type) {
        this.id = id;
        this.cardNum = cardNum;
        this.type = type;
    }

    @Generated(hash = 691855875)
    public Black() {
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
