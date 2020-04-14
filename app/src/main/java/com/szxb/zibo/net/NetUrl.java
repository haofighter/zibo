package com.szxb.zibo.net;

public class NetUrl {

    //
//    private static final String IP = "http://2t183d9338.iask.in:37092/jnbus";
////    private static final String IP = "http://129.204.55.41/jnbus";
//    private static final String IP = "http://129.204.55.41/hkbus";
//    private static final String IP = "http://120.220.53.11:10065";
    private static final String IP = "http://139.9.113.219:10065";
    //上送机器信息
    public static final String SEND_INFO_SERVER = IP + "/tms/Task-server/commonUpload/text";
    //上送记录
    public static final String RECORD_UPLOAD = IP + "/gprs/gprs-server/emptech";

    //
//
    //腾讯公钥
    public final static String TENCENT_PUBLIC_KEY = IP + "/interaction/getpubkey";
    //腾讯秘钥
    public final static String TENCENT_MAC_KEY = IP + "/interaction/getmackey";
    //交通部秘钥
    public final static String TRAN_MAC_KEY = IP + "/interaction/getjtmac";
    //交通证书
    public final static String TRAN_CERTVERQUERY = IP + "/interaction/certverquery";
    //上传刷卡记录
    public static final String UP_CARD_RECORD = IP + "/interaction/carduploadzb1";
    //上传刷卡记录
    public static final String UP_JTB_RECORD = IP + "/interaction/ownqrc";

    //上传刷卡记录
    public static final String UP_UNION_RECORD = IP + "/interaction/bankjourAll";


    //上传管理卡记录
    public static final String UP_driverinfoload_RECORD = IP + "/interaction/driverinfoload";
    //阿里公钥
//    public static final String ALI_PUBLIC_KEY = "http://129.204.55.41/hkbus/interaction/getAlimainkey";
    public static final String ALI_PUBLIC_KEY = IP + "/interaction/getAlipayPubKey";

    //下载线路
    public final static String DOWN_LINE = IP + "/interaction/getLineInfo";

    //校准时间
    public static final String REG_TIME_URL = "http://134.175.56.14/bipeqt/interaction/getStandardTime";

    //TX二维码上传地址
    public static final String UPLOAD_TXSACN = IP + "/interaction/wxposrecv";

    //支付宝扫码记录
    public static final String UPLOAD_ALSACN = IP + "/interaction/zfbposrecv";

    //sim卡信息上传
    public static final String SIM_MESSEGE = IP + "/interaction/savesim";
}
