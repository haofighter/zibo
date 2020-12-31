package com.szxb.zibo.moudle.function.scan.freecode;


import com.szxb.lib.Util.FileUtils;
import com.szxb.zibo.config.zibo.DBManagerZB;
import com.szxb.zibo.manager.PosManager;

/**
 * 作者：Tangren on 2018-11-27
 * 包名：com.szxb.buspay.db.entity.bean
 * 邮箱：996489865@qq.com
 */

public class QRData {
    //二维码版本
    private String version;
    //数据长度
    private int dataLen;
    private QRIssuerCert qrIssuerCert;
    //支付账户
    private String userPayID;
    //卡账户号
    private String userID;
    //发卡机构号
    private String issuer;
    //平台编号
    private String number;
    //账户类型
    private String userType;
    //消费上限
    private int maxAmount;
    //支付账户用户公钥
    private String userPublicKey;
    //授权过期时间
    private long outTime;
    //有效时间
    private int timeOutTime;
    //自定义域长度
    private int customDataLen;
    //自定义域数据
    private String customData;
    //发卡机构授权签名
    private String issuerSign;
    //发卡机构授权签名数据【3-14】
    private byte[] issuerSignData;
    //二维码生成时间
    private long qrTime;
    //支付账户用户私钥签名
    private String userPrivateKeySign;
    //支付账户用户私钥签名数据
    private byte[] userPrivateKeySignData;

    //是否过期
    private boolean beOverdue;

    public QRData(byte[] data) {
        int index = 0;
        byte[] version = new byte[1];
        System.arraycopy(data, index, version, 0, version.length);
        setVersion(FileUtils.bytesToHexString(version));

        byte[] len = new byte[2];
        System.arraycopy(data, index += version.length, len, 0, len.length);
        setDataLen(Integer.valueOf(FileUtils.bytesToHexString(len), 16));

        byte[] cert = new byte[117];
        System.arraycopy(data, index += len.length, cert, 0, cert.length);
        qrIssuerCert = new QRIssuerCert(cert);

        byte[] payUser = new byte[16];
        System.arraycopy(data, index += cert.length, payUser, 0, payUser.length);
//        setUserPayID(FileUtils.bytesToHexString(payUser));
        setUserPayID(new String(payUser));

        byte[] cardUser = new byte[10];
        System.arraycopy(data, index += payUser.length, cardUser, 0, cardUser.length);
        setUserID(FileUtils.bytesToHexString(cardUser));

        byte[] orgNo = new byte[4];
        System.arraycopy(data, index += cardUser.length, orgNo, 0, orgNo.length);
        setIssuer(FileUtils.bytesToHexString(orgNo));

        byte[] plaNo = new byte[4];
        System.arraycopy(data, index += orgNo.length, plaNo, 0, plaNo.length);
        setNumber(FileUtils.bytesToHexString(plaNo));

        byte[] type = new byte[1];
        System.arraycopy(data, index += plaNo.length, type, 0, type.length);
        setUserType(FileUtils.bytesToHexString(type));

        byte[] maxAmount = new byte[3];
        System.arraycopy(data, index += type.length, maxAmount, 0, maxAmount.length);
        setMaxAmount(Integer.valueOf(FileUtils.bytesToHexString(maxAmount), 16));

        byte[] publicKey = new byte[33];
        System.arraycopy(data, index += maxAmount.length, publicKey, 0, publicKey.length);
        setUserPublicKey(FileUtils.bytesToHexString(publicKey));

        byte[] outTime = new byte[4];
        System.arraycopy(data, index += publicKey.length, outTime, 0, outTime.length);
        setOutTime(Long.valueOf(FileUtils.bytesToHexString(outTime), 16));

        byte[] validityTime = new byte[2];
        System.arraycopy(data, index += outTime.length, validityTime, 0, validityTime.length);
        setTimeOutTime(Integer.valueOf(FileUtils.bytesToHexString(validityTime), 16));

        byte[] customDataLen = new byte[1];
        System.arraycopy(data, index += validityTime.length, customDataLen, 0, customDataLen.length);
        setCustomDataLen(Integer.valueOf(FileUtils.bytesToHexString(customDataLen), 16));

        byte[] customData = new byte[getCustomDataLen()];
        System.arraycopy(data, index += customDataLen.length, customData, 0, customData.length);
        setCustomData(FileUtils.bytesToHexString(customData));

        int startIndex = 3;
        int length = index + customData.length - startIndex;
        issuerSignData = new byte[length];
        System.arraycopy(data, startIndex, issuerSignData, 0, issuerSignData.length);

        byte[] issuerSignFormat = new byte[1];
        System.arraycopy(data, index += customData.length, issuerSignFormat, 0, issuerSignFormat.length);

        byte[] issuerSign = new byte[64];
        System.arraycopy(data, index += issuerSignFormat.length, issuerSign, 0, issuerSign.length);
        setIssuerSign(FileUtils.bytesToHexString(issuerSign));

        byte[] qrTime = new byte[4];
        System.arraycopy(data, index += issuerSign.length, qrTime, 0, qrTime.length);
        setQrTime(Long.valueOf(FileUtils.bytesToHexString(qrTime), 16));

        //总长度为65,解析时去除签名数据格式1字节
        byte[] userPrivateKeySign = new byte[64];
        System.arraycopy(data, index + qrTime.length + 1, userPrivateKeySign, 0, userPrivateKeySign.length);
        setUserPrivateKeySign(FileUtils.bytesToHexString(userPrivateKeySign));

        userPrivateKeySignData = new byte[data.length - userPrivateKeySign.length - 1];
        System.arraycopy(data, 0, userPrivateKeySignData, 0, userPrivateKeySignData.length);

        if (issuer.equals("03664530")) {//交通部码
            long currentTime = System.currentTimeMillis() / 1000 - 8 * 60 * 60;
            long resTime = currentTime - this.qrTime;
            //满足条件则日期非法
            beOverdue = resTime < -timeOutTime || resTime > timeOutTime;

            if (beOverdue) {
                currentTime = System.currentTimeMillis() / 1000 + 8 * 60 * 60;
                resTime = currentTime - this.qrTime;
                //满足条件则日期非法
                beOverdue = resTime < -timeOutTime || resTime > timeOutTime;
            }

            if (beOverdue) {
                currentTime = System.currentTimeMillis() / 1000;
                resTime = currentTime - this.qrTime;
                //满足条件则日期非法
                beOverdue = resTime < -timeOutTime || resTime > timeOutTime;
            }
        } else {//自建码
            long currentTime = System.currentTimeMillis() / 1000;
            long resTime = currentTime - this.qrTime;
            //满足条件则日期非法
            beOverdue = resTime < -timeOutTime || resTime > timeOutTime;
        }
    }

    public boolean isBeOverdue() {
        return beOverdue;
    }

    public void setBeOverdue(boolean beOverdue) {
        this.beOverdue = beOverdue;
    }

    public int getCustomDataLen() {
        return customDataLen;
    }

    public void setCustomDataLen(int customDataLen) {
        this.customDataLen = customDataLen;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public QRIssuerCert getQrIssuerCert() {
        return qrIssuerCert;
    }

    public void setQrIssuerCert(QRIssuerCert qrIssuerCert) {
        this.qrIssuerCert = qrIssuerCert;
    }

    public String getUserPublicKey() {
        return userPublicKey;
    }

    public void setUserPublicKey(String userPublicKey) {
        this.userPublicKey = userPublicKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getDataLen() {
        return dataLen;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public String getUserPayID() {
        return userPayID;
    }

    public void setUserPayID(String userPayID) {
        this.userPayID = userPayID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getTimeOutTime() {
        return timeOutTime;
    }

    public void setTimeOutTime(int timeOutTime) {
        this.timeOutTime = timeOutTime;

    }

    public String getIssuerSign() {
        return issuerSign;
    }

    public void setIssuerSign(String issuerSign) {
        this.issuerSign = issuerSign;
    }

    public byte[] getIssuerSignData() {
        return issuerSignData;
    }

    public void setIssuerSignData(byte[] issuerSignData) {
        this.issuerSignData = issuerSignData;
    }

    public long getQrTime() {
        return qrTime;
    }

    public void setQrTime(long qrTime) {
        this.qrTime = qrTime;
    }

    public String getUserPrivateKeySign() {
        return userPrivateKeySign;
    }

    public void setUserPrivateKeySign(String userPrivateKeySign) {
        this.userPrivateKeySign = userPrivateKeySign;
    }

    public byte[] getUserPrivateKeySignData() {
        return userPrivateKeySignData;
    }

    public void setUserPrivateKeySignData(byte[] userPrivateKeySignData) {
        this.userPrivateKeySignData = userPrivateKeySignData;
    }

    public class QRIssuerCert {
        private QRIssuerCert(byte[] data) {
            int index = 5;
            byte[] caIndex = new byte[1];
            System.arraycopy(data, index, caIndex, 0, caIndex.length);
            setcAIndex(FileUtils.bytesToHexString(caIndex));

            index += 6;
            byte[] invalidDate = new byte[2];
            System.arraycopy(data, index, invalidDate, 0, invalidDate.length);
            setInvalidDate(FileUtils.bytesToHexString(invalidDate));

            index += 8;
            byte[] len = new byte[1];
            System.arraycopy(data, index, len, 0, len.length);
            setCertKeyLen(Integer.valueOf(FileUtils.bytesToHexString(len), 16));

            byte[] certKey = new byte[33];
            System.arraycopy(data, index += len.length, certKey, 0, certKey.length);
            setCertKey(FileUtils.bytesToHexString(certKey));

            byte[] certSign = new byte[64];
            System.arraycopy(data, index + certKey.length, certSign, 0, certSign.length);
            setCertSign(FileUtils.bytesToHexString(certSign));

            index = 6;
            certSignData = new byte[data.length - certSign.length - index];
            System.arraycopy(data, index, certSignData, 0, certSignData.length);

        }

        //中心 ＣＡ 公钥索引
        private String cAIndex;
        //卡证书过期日期
        private String invalidDate;
        //发卡机构公钥长度
        private int certKeyLen;
        //发卡机构公钥
        private String certKey;
        //证书签名字段[4-12]
        private byte[] certSignData;
        //卡证书签名
        private String certSign;

        public String getcAIndex() {
            return cAIndex;
        }

        public void setcAIndex(String cAIndex) {
            this.cAIndex = cAIndex;
        }

        public String getInvalidDate() {
            return invalidDate;
        }

        public void setInvalidDate(String invalidDate) {
            this.invalidDate = invalidDate;
        }

        public int getCertKeyLen() {
            return certKeyLen;
        }

        public void setCertKeyLen(int certKeyLen) {
            this.certKeyLen = certKeyLen;
        }

        public String getCertKey() {
            return certKey;
        }

        public void setCertKey(String certKey) {
            this.certKey = certKey;
        }

        public byte[] getCertSignData() {
            return certSignData;
        }

        public void setCertSignData(byte[] certSignData) {
            this.certSignData = certSignData;
        }

        public String getCertSign() {
            return certSign;
        }

        public void setCertSign(String certSign) {
            this.certSign = certSign;
        }

        @Override
        public String toString() {
            return "QRIssuerCert{" +
                    "cAIndex='" + cAIndex + '\'' +
                    ", invalidDate='" + invalidDate + '\'' +
                    ", certKeyLen=" + certKeyLen +
                    ", certKey='" + certKey + '\'' +
                    ", certSignData=" + FileUtils.bytesToHexString(certSignData) +
                    ", certSign='" + certSign + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "QRData{" +
                "version='" + version + '\'' +
                ", dataLen=" + dataLen +
                ", qrIssuerCert=" + qrIssuerCert +
                ", userPayID='" + userPayID + '\'' +
                ", userID='" + userID + '\'' +
                ", issuer='" + issuer + '\'' +
                ", number='" + number + '\'' +
                ", userType='" + userType + '\'' +
                ", maxAmount=" + maxAmount +
                ", userPublicKey='" + userPublicKey + '\'' +
                ", outTime=" + outTime +
                ", timeOutTime=" + timeOutTime +
                ", customDataLen=" + customDataLen +
                ", customData='" + customData + '\'' +
                ", issuerSign='" + issuerSign + '\'' +
                ", issuerSignData=" + FileUtils.bytesToHexString(issuerSignData) +
                ", qrTime=" + qrTime +
                ", userPrivateKeySign='" + userPrivateKeySign + '\'' +
                ", userPrivateKeySignData=" + FileUtils.bytesToHexString(userPrivateKeySignData) +
                '}';
    }
}
