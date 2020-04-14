package com.szxb.zibo.config.zibo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PublicKey {
    String publicCreatTag;////注册标识：5字节
    int publicKeyTag;  //公钥索引：1字节（HEX）
    String hashAlgorithmTag;
    String publicAlgorithmTag;
    String publicIndexTag;
    String publicLenth;
    String publicKey;
    String rev1;
    String rev2;
    String rev3;
    String rev4;
    String rev5;
    @Generated(hash = 846106932)
    public PublicKey(String publicCreatTag, int publicKeyTag,
            String hashAlgorithmTag, String publicAlgorithmTag,
            String publicIndexTag, String publicLenth, String publicKey,
            String rev1, String rev2, String rev3, String rev4, String rev5) {
        this.publicCreatTag = publicCreatTag;
        this.publicKeyTag = publicKeyTag;
        this.hashAlgorithmTag = hashAlgorithmTag;
        this.publicAlgorithmTag = publicAlgorithmTag;
        this.publicIndexTag = publicIndexTag;
        this.publicLenth = publicLenth;
        this.publicKey = publicKey;
        this.rev1 = rev1;
        this.rev2 = rev2;
        this.rev3 = rev3;
        this.rev4 = rev4;
        this.rev5 = rev5;
    }
    @Generated(hash = 285518041)
    public PublicKey() {
    }
    public String getPublicCreatTag() {
        return this.publicCreatTag;
    }
    public void setPublicCreatTag(String publicCreatTag) {
        this.publicCreatTag = publicCreatTag;
    }
    public int getPublicKeyTag() {
        return this.publicKeyTag;
    }
    public void setPublicKeyTag(int publicKeyTag) {
        this.publicKeyTag = publicKeyTag;
    }
    public String getHashAlgorithmTag() {
        return this.hashAlgorithmTag;
    }
    public void setHashAlgorithmTag(String hashAlgorithmTag) {
        this.hashAlgorithmTag = hashAlgorithmTag;
    }
    public String getPublicAlgorithmTag() {
        return this.publicAlgorithmTag;
    }
    public void setPublicAlgorithmTag(String publicAlgorithmTag) {
        this.publicAlgorithmTag = publicAlgorithmTag;
    }
    public String getPublicIndexTag() {
        return this.publicIndexTag;
    }
    public void setPublicIndexTag(String publicIndexTag) {
        this.publicIndexTag = publicIndexTag;
    }
    public String getPublicLenth() {
        return this.publicLenth;
    }
    public void setPublicLenth(String publicLenth) {
        this.publicLenth = publicLenth;
    }
    public String getPublicKey() {
        return this.publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public String getRev1() {
        return this.rev1;
    }
    public void setRev1(String rev1) {
        this.rev1 = rev1;
    }
    public String getRev2() {
        return this.rev2;
    }
    public void setRev2(String rev2) {
        this.rev2 = rev2;
    }
    public String getRev3() {
        return this.rev3;
    }
    public void setRev3(String rev3) {
        this.rev3 = rev3;
    }
    public String getRev4() {
        return this.rev4;
    }
    public void setRev4(String rev4) {
        this.rev4 = rev4;
    }
    public String getRev5() {
        return this.rev5;
    }
    public void setRev5(String rev5) {
        this.rev5 = rev5;
    }

    @Override
    public String toString() {
        return "PublicKey{" +
                "publicCreatTag='" + publicCreatTag + '\'' +
                ", publicKeyTag=" + publicKeyTag +
                ", hashAlgorithmTag='" + hashAlgorithmTag + '\'' +
                ", publicAlgorithmTag='" + publicAlgorithmTag + '\'' +
                ", publicIndexTag='" + publicIndexTag + '\'' +
                ", publicLenth='" + publicLenth + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}'+"\n";
    }
}
