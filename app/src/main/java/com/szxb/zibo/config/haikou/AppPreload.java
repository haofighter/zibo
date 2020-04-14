package com.szxb.zibo.config.haikou;

/**
 * app初始化需要加载的预加载项
 */
public class AppPreload {
    boolean binLoadSucess;
    boolean TencentMacKeySuc;
    boolean TencentPublicKeySuc;
    boolean ALPublicKey;
    boolean JTBParam;
    boolean WHITE;//白名单
    boolean BLACK;//黑名单

    public boolean sucesse() {
        return binLoadSucess;
    }

    public boolean isBinLoadSucess() {
        return binLoadSucess;
    }

    public void setBinLoadSucess(boolean binLoadSucess) {
        this.binLoadSucess = binLoadSucess;
    }

    public boolean isTencentMacKeySuc() {
        return TencentMacKeySuc;
    }

    public void setTencentMacKeySuc(boolean tencentMacKeySuc) {
        TencentMacKeySuc = tencentMacKeySuc;
    }

    public boolean isTencentPublicKeySuc() {
        return TencentPublicKeySuc;
    }

    public void setTencentPublicKeySuc(boolean tencentPublicKeySuc) {
        TencentPublicKeySuc = tencentPublicKeySuc;
    }

    public boolean isALPublicKey() {
        return ALPublicKey;
    }

    public void setALPublicKey(boolean ALPublicKey) {
        this.ALPublicKey = ALPublicKey;
    }

    public boolean isJTBParam() {
        return JTBParam;
    }

    public void setJTBParam(boolean JTBParam) {
        this.JTBParam = JTBParam;
    }
}
