package com.szxb.zibo.moudle.function.scan.alicode;

import com.ibuscloud.ibuscloudposlib.IBusCloudPos;
import com.ibuscloud.ibuscloudposlib.constant.IBusCloudStdRetCodeEnum;
import com.ibuscloud.ibuscloudposlib.constant.TicketType;
import com.ibuscloud.ibuscloudposlib.domain.ExtendInfo;
import com.ibuscloud.ibuscloudposlib.domain.PosParam;
import com.ibuscloud.ibuscloudposlib.domain.SDKParam;
import com.ibuscloud.ibuscloudposlib.domain.TradeRequestInfo;
import com.ibuscloud.ibuscloudposlib.domain.TradeResponseInfo;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.db.bean.ALiVerfResponse;

/**
 * 作者：L on 2019-07-08 11:43
 */

public class ALiCloudVerif {
    private static boolean isInited = false;
    /**
     * 城市编码
     */
    private static final String cityid = "330100";
    /**
     * 公司编码
     */
    private static final String companyid = "33010001";
    /**
     * 机具软件版本号，不超过15字节
     */
    private static final String version = "v1.0.0";
    /**
     * 商户类型(实际使用按照示例传入)
     */
    private static final String merchantType = "1";
    /**
     * 司机打卡时间（GMT）
     */
    private static final int attandanceTime = 1513641600;
    /**
     * oemid，由支付宝分配
     */
    private static final String oemid = "9998112123";
    private static String merchantId = "33030001";
    private static String fleetCode = "1";
    /**
     * 经度
     */
    private static final double longitude = 120.089312;
    /**
     * 纬度
     */
    private static final double latitude = 30.135178;
    /**
     * 卡类型白名单数据，从后台获取的字符串数据
     */
    private static final String cardType = "0A1E1208494E543030303031";
//0A1E120854303337303130301208494E543030303031
    /**
     * 密钥数据，从后台获取的字符串数据
     */
    private static final String key = "0AB812080112480A02323212423032444639354536433734393" +
            "14530463930413233323230373542443937334642434232443136334239323632334242453135" +
            "33463635383134353833463137433912480A02323312423033393946354539323441334335423" +
            "84543464536463344314245394237433137364245424536383537463432383834394342384538" +
            "42434443453638394438323712480A02323412423033434132424644354336423532443045384" +
            "43832363337384546323341303138333944333643363046373645434442314243433042324535" +
            "35453034323531463012480A02323512423032353537443541463136413334353831354332463" +
            "33839363533354137463936394141374245394630413330303338364646304436333741373839" +
            "31303031423012480A02323612423032394144303631343843383145303032354342313638353" +
            "53931353133424536353741364139413942453945383342324545313130323231423236333036" +
            "45464312480A02323712423033383731383339393939443630303344393037453938383444433" +
            "13839323832363138343534383043323133453438304435333045303343453436303834303837" +
            "12480A02323812423033373942453131373631353043383344323536463032304435423041343" +
            "0394137363135433939454243373037304632374537363239303044413736304633324612480A" +
            "02323912423033344246443441314533453339433531424343363045363244443346383044383" +
            "3393241343431383135353330394535304632434444393439383546414430323612480A023330" +
            "12423033393939413530453632383837384630363730444242464532304639383931394534464" +
            "5454242303043464544393846303836374335333133353436363933443412480A023331124230" +
            "33303739423237363932423230413331354439354134384441324637463243434330423830373" +
            "2334638413635444431334233393939373036444537354635353912480A023130124230334234" +
            "36394630373634423243423933464145444436303446413143454342374141333341414336453" +
            "2313745303641344436464138363734394135314536434312480A023131124230333635433734" +
            "45364233443533443237343243423745463630444137384633393331424532384143313245323" +
            "5444630354334343943443034443041453039364612480A023132124230333130313039414544" +
            "39423036443338414631304641313732393131394530354334363535373841334144424344464" +
            "2454444413732333131324532444231334312480A023133124230333443423334413444313546" +
            "39313134413338363843433642364339303033463839363832413843343544384242453434424" +
            "4333838434330424441324331434512480A023134124230333636423845334134424245313245" +
            "44313143353442343841394539423732344130374346384539384637383532324232334546443" +
            "9354436413245433030444412480A023135124230324537303739353830434530373141393238" +
            "34343645383134323845433438373341324638433837393331313638374134363646444143443" +
            "1334639444432394512480A023136124230323339364435304242434631354134304433424244" +
            "30364637443737363344334535373935313238463346423146454330433935303835434331464" +
            "5363336453112480A023137124230324330364541373036464343343946323030424145363046" +
            "45354638354335313945453231363230383231423337304342383135414238343942453232413" +
            "9443512480A023138124230333338334636303533353535354335323737424335313633463038" +
            "37433745414530443739434343383642394442393635363544394133333334303444363338431" +
            "2480A023139124230333943423537314435433133393846333430443031433338304245363736" +
            "423531423742413444414345443044323238373941323734303342423346343944353912470A0" +
            "13012423032414232464442304142323335303643343830313236343245314135373246433436" +
            "4235443842384545334239324136303243433131303939323146383442304512470A013112423" +
            "03245413935413039364242354245393639333633354443443232333144323130453135423838" +
            "30334331304646453532393342323941363732353143333630354212470A01321242303338383" +
            "23444393241454641324231454538463334394641324333384442423044304541423035374239" +
            "394646324631383939424246413146323946313136324212470A0133124230334234314235313" +
            "73531334530334542454533444234443537353934453541304631393638384539413530363741" +
            "4232334236433631333636363334414635373212470A013412423032383241424238383136383" +
            "54543383038324238313643383446304146374241373134363734423032373631374141394330" +
            "42454135343046353042363132343512470A01351242303237354645344537313432323942313" +
            "73332453243464441343430313146363731384539453232363142373441453338383643363637" +
            "443233414232314542374612470A0136124230324434414244363237374334323132354543393" +
            "33831314642373546303245384432394345313946313742303131384130364642363444444441" +
            "4438463135314412470A013712423033433833334243443646453436344632463932334433423" +
            "83743343934354537463636314436383641333739423435343330364337393030304544333737" +
            "38423112470A01381242303237304536394531373139433833354333303744313630353031343" +
            "14530353237444435423939453032354637443541393141454142364446393544433736303612" +
            "470A0139124230323330383636434237464139383030453737394538373342384539373836323" +
            "935453835314346333237393035464632393031323342393146324534453444453312480A0232" +
            "30124230334636363737303734343234424636314139454639303636334439314439434339374" +
            "130324535343632443033383646424342454437464331313145434631324112480A0232311242" +
            "30323844433145333334464136313741373131423945354135303630453239444144413033414" +
            "63044423634324239323634393033323234413341393235303941320A2712250A013012203031" +
            "323334353637383930313233343536373839303132333435363738393132";


    public static boolean verifALiCode() {
        IBusCloudStdRetCodeEnum rc = IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_SUCCESS;

        // 初始化工作只需要做一次
        if (!isInited) {
            // 初始化POS参数
            rc = initPosParam();
            if (IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_SUCCESS != rc) {
                //init pos param failed...
                return false;
            } else {
                //init pos param success...
            }

            rc = initSDKParam();
            if (IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_SUCCESS != rc) {
                //init sdk param failed...
                return false;
            } else {
                //init sdk param success...
            }
            isInited = true;
        }
        return true;

//        // 初始化成功以后就可以验证二维码了
//        rc = processTrade(qrCode, tickPrice);
//        // 注意！！！实际处理时，这里只判断是否成功，这里引入过期是因为使用了实现保存好的二维码数据，所以跑demo
//        // 的时候，一定是会提示过期的！！！请务必注意！！！
//        if (IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_SUCCESS != rc &&
//                IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_QRCODE_INFO_EXPIRED != rc &&
//                IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_QRCODE_KEY_EXPIRED != rc) {
//            sb.append("init process trade failed...\n");
//            sb.append(rc);
//            return sb.toString();
//        } else {
//            sb.append("init process trade success...\n\n");
//        }
//
//        sb.append("Congratulations!\n");
//        return sb.toString();
    }

    // 初始化POS机参数
    private static IBusCloudStdRetCodeEnum initPosParam() {
        PosParam posParam = new PosParam();
        try {
            posParam.setPosid(BusApp.getPosManager().getPosSN());
            posParam.setBusid(BusApp.getPosManager().getBusNo());
            posParam.setRouteid(BusApp.getPosManager().getLineNo());
            posParam.setCityid(cityid);
            posParam.setCompanyid(companyid);
            posParam.setDriverid(BusApp.getPosManager().getDriverNo());
            posParam.setMerchantType(merchantType);
            posParam.setSwVersion(version);
            posParam.setAttendanceTime(attandanceTime);
            posParam.setOemid(oemid);
            posParam.setMerchantId(merchantId);
            posParam.setFleetCode(fleetCode);
        } catch (Exception e) {
            e.printStackTrace();
            return IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_UNKNOWN_ERROR;
        }
        return IBusCloudPos.initPosParam(posParam);
    }

    //初始化SDK参数
    private static IBusCloudStdRetCodeEnum initSDKParam() {
        SDKParam sdkParam = new SDKParam();
        sdkParam.setKeyInfo(key);
        sdkParam.setCardTypeInfo(cardType);

        return IBusCloudPos.initSDKParam(sdkParam);
    }

    //执行交易流程
    static ALiVerfResponse processTrade(byte[] qrCode, int tickPrice) {
        // 调用交易接口
        TradeRequestInfo tradeRequestInfo = new TradeRequestInfo();
        TradeResponseInfo tradeResponseInfo = new TradeResponseInfo();
        ExtendInfo extendInfo = new ExtendInfo();
        tradeRequestInfo.setCost(tickPrice);
        tradeRequestInfo.setTicketPrice(tickPrice);
        tradeRequestInfo.setLongitude(longitude);
        tradeRequestInfo.setLatitude(latitude);
        tradeRequestInfo.setTicketType(TicketType.INTEGRATED_TICKETING);
        byte[] byteQRC = (qrCode);

        IBusCloudStdRetCodeEnum rc =
                IBusCloudPos.processTradeProcV1(byteQRC, tradeRequestInfo, tradeResponseInfo, extendInfo);
        byte[] message = tradeResponseInfo.getMessage();

        ALiVerfResponse aLiVerfResponse = new ALiVerfResponse();
        aLiVerfResponse.setBalance(tradeResponseInfo.getBalance());
        aLiVerfResponse.setCost(tradeResponseInfo.getCost());
        aLiVerfResponse.setUserID(tradeResponseInfo.getUserID());
        aLiVerfResponse.setCardID(tradeResponseInfo.getCardID());
        aLiVerfResponse.setCardData(tradeResponseInfo.getCardData());
        aLiVerfResponse.setCardType(tradeResponseInfo.getCardType());
        aLiVerfResponse.setMessage(tradeResponseInfo.getMessage());
        aLiVerfResponse.setUniqueID(tradeResponseInfo.getUniqueID());
        aLiVerfResponse.setTradeTime(tradeResponseInfo.getTradeTime());
        aLiVerfResponse.setQrcodeType(tradeResponseInfo.getQrcodeType());

        aLiVerfResponse.setReturnCode(rc.getReturnCode());
        aLiVerfResponse.setDescription(rc.getDescription());

//        if (message.length > 0) {
//            if (rc == IBusCloudStdRetCodeEnum.IBUSCLOUDSDK_SUCCESS) {
////                int qrcodeType = tradeResponseInfo.getQrcodeType();
////                if (qrcodeType == 1) {
////                    // 支付宝逻辑
////                } else if (qrcodeType == 2) {
////                    // 定制公交逻辑
////                }
//                return ;
//            } else {
//                // 验码失败错误逻辑
//                return ;
//            }
//        }

        return aLiVerfResponse;
    }
}
