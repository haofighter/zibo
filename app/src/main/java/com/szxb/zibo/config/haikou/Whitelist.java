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
    private String organizationCode;
    @Unique
    private String PAN;
    private String description;

    @Generated(hash = 516782703)
    public Whitelist(Long id, String organizationCode, String PAN,
                     String description) {
        this.id = id;
        this.organizationCode = organizationCode;
        this.PAN = PAN;
        this.description = description;
    }

    @Generated(hash = 683262075)
    public Whitelist() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationCode() {
        return this.organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getPAN() {
        return this.PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
