package com.szxb.zibo.config.haikou;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by lilei on 18-1-5.
 */


@Entity
public class Whitelist {
    @Id(autoincrement = true)
    private Long id;
    String user;
    @Unique
    String cardno;
    String deadCardno;
    String level;
    String password;

    @Generated(hash = 1516590987)
    public Whitelist(Long id, String user, String cardno, String deadCardno,
            String level, String password) {
        this.id = id;
        this.user = user;
        this.cardno = cardno;
        this.deadCardno = deadCardno;
        this.level = level;
        this.password = password;
    }

    @Generated(hash = 683262075)
    public Whitelist() {
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public void setDeadCardno(String deadCardno) {
        this.deadCardno = deadCardno;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Whitelist{" +
                ", user='" + user + '\'' +
                ", cardno='" + cardno + '\'' +
                ", deadCardno='" + deadCardno + '\'' +
                ", level='" + level + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return this.user;
    }

    public String getCardno() {
        return this.cardno;
    }

    public String getDeadCardno() {
        return this.deadCardno;
    }

    public String getLevel() {
        return this.level;
    }

    public String getPassword() {
        return this.password;
    }
}
