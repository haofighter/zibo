package com.szxb.zibo.config.haikou;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BlackList {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String cardStart;
    private String cardEnd;
    private String type;

    @Generated(hash = 621689033)
    public BlackList(Long id, String cardStart, String cardEnd, String type) {
        this.id = id;
        this.cardStart = cardStart;
        this.cardEnd = cardEnd;
        this.type = type;
    }

    @Generated(hash = 1200343381)
    public BlackList() {
    }

    public String getCardStart() {
        return cardStart;
    }

    public void setCardStart(String cardStart) {
        this.cardStart = cardStart;
    }

    public String getCardEnd() {
        return cardEnd;
    }

    public void setCardEnd(String cardEnd) {
        this.cardEnd = cardEnd;
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
