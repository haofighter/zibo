package com.szxb.zibo.config.zibo;

import android.util.Log;

import com.szxb.lib.Util.FileUtils;
import com.szxb.lib.Util.MiLog;
import com.szxb.zibo.base.BusApp;
import com.szxb.zibo.config.haikou.Black;
import com.szxb.zibo.config.haikou.BlackList;
import com.szxb.zibo.config.haikou.Whitelist;
import com.szxb.zibo.config.zibo.line.CardPlan;
import com.szxb.zibo.config.zibo.line.CardType;
import com.szxb.zibo.config.zibo.line.FareRulePlan;
import com.szxb.zibo.config.zibo.line.FarePlan;
import com.szxb.zibo.config.zibo.line.BasicFare;
import com.szxb.zibo.config.zibo.line.StationDivide;
import com.szxb.zibo.config.zibo.line.StationName;
import com.szxb.zibo.config.zibo.line.StationPayPrice;
import com.szxb.zibo.config.zibo.line.ZBLineInfo;
import com.szxb.zibo.db.dao.BasicFareDao;
import com.szxb.zibo.db.dao.BlackDao;
import com.szxb.zibo.db.dao.BlackListDao;
import com.szxb.zibo.db.dao.CardPlanDao;
import com.szxb.zibo.db.dao.ColletGpsInfoDao;
import com.szxb.zibo.db.dao.FareRulePlanDao;
import com.szxb.zibo.db.dao.PublicKeyDao;
import com.szxb.zibo.db.dao.StationNameDao;
import com.szxb.zibo.db.dao.StationPayPriceDao;
import com.szxb.zibo.db.dao.UnionPayEntityDao;
import com.szxb.zibo.db.dao.WhitelistDao;
import com.szxb.zibo.db.dao.XdRecordDao;
import com.szxb.zibo.db.dao.ZBLineInfoDao;
import com.szxb.zibo.db.manage.DBCore;
import com.szxb.zibo.manager.PosManager;
import com.szxb.zibo.moudle.function.gps.ColletGpsInfo;
import com.szxb.zibo.moudle.function.unionpay.entity.UnionPayEntity;
import com.szxb.zibo.record.AppParamInfo;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.util.DateUtil;
import com.szxb.zibo.util.sp.CommonSharedPreferences;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import static com.szxb.zibo.db.manage.DBCore.getDaoSession;


public class DBManagerZB {

    public static void saveAppParamInfo(AppParamInfo runParam) {
        runParam.setRunId(1);
        getDaoSession().getAppParamInfoDao().insertOrReplaceInTx(runParam);
        BusApp.getInstance().saveBackeUp();//保存基本配置
    }

    public static AppParamInfo checkAppParamInfo() {
        AppParamInfo appParamInfo = getDaoSession().getAppParamInfoDao().queryBuilder().limit(1).unique();
        if (appParamInfo == null) {
            appParamInfo = new AppParamInfo();
            appParamInfo.setBusNo((String) CommonSharedPreferences.get("bus_no", "000000"));
            appParamInfo.setDriverNo((String) CommonSharedPreferences.get("driver_no", "00000000"));
            appParamInfo.setLinName((String) CommonSharedPreferences.get("line_name", "未获取到线路"));
            appParamInfo.setLinNo((String) CommonSharedPreferences.get("line_no", "000000"));
            appParamInfo.setBasePrice((Integer) CommonSharedPreferences.get("base_price", 0));
            appParamInfo.setLinVer((String) CommonSharedPreferences.get("Linver", "00000000000000"));
            appParamInfo.setRunId(1);
        }
        return appParamInfo;
    }

    public static void savaAllLineInfo(List<ZBLineInfo> all) {
        getDaoSession().getZBLineInfoDao().deleteAll();
        getDaoSession().getZBLineInfoDao().insertOrReplaceInTx(all);
    }

    public static List<ZBLineInfo> getAllLineInfo() {
        return getDaoSession().getZBLineInfoDao().queryBuilder().list();
    }

    public static ZBLineInfo checkedLineInfo(String line) {
        return getDaoSession().getZBLineInfoDao().queryBuilder().where(ZBLineInfoDao.Properties.Routeno.eq(line)).limit(1).unique();
    }


    //保存所有密钥
    public static void saveAllPub(List<PublicKey> publicKeys) {
        getDaoSession().getPublicKeyDao().deleteAll();
        Log.i("保存 密钥", publicKeys.toString());
        getDaoSession().getPublicKeyDao().insertOrReplaceInTx(publicKeys);
    }


    //保存所有的卡方案
    public static void saveAllCard(List<CardPlan> cardPlans) {
        getDaoSession().getCardPlanDao().insertOrReplaceInTx(cardPlans);
    }

    //消费点划分
    public static void saveAllStation(List<StationDivide> stationDivides) {
        getDaoSession().getStationDivideDao().insertOrReplaceInTx(stationDivides);
    }

    //保存基本价格方案
    public static void savePraseBasePriceCase(List<BasicFare> basicFares) {

        getDaoSession().getBasicFareDao().insertOrReplaceInTx(basicFares);
    }

    //计价方案信息
    public static void savePriceCase(List<FarePlan> proceCases) {
        getDaoSession().getFarePlanDao().insertOrReplaceInTx(proceCases);
    }

    //卡类型内容
    public static void saveCardType(List<CardType> cardTypes) {
        getDaoSession().getCardTypeDao().insertOrReplaceInTx(cardTypes);
    }

    //计费规则生成规则
    public static void saveFareRulePlan(List<FareRulePlan> fareRulePlans) {

        getDaoSession().getFareRulePlanDao().insertOrReplaceInTx(fareRulePlans);
    }

    //经纬度设置
    public static void savaStationName(List<StationName> stationNames) {
        getDaoSession().getStationNameDao().insertOrReplaceInTx(stationNames);
    }


    //获取所有的票价方案
    public static FarePlan checkFarePlan() {
        return getDaoSession().getFarePlanDao().queryBuilder().limit(1).unique();
    }

    //获取当前的一个票价规则
    public static FareRulePlan checkFareRulePlan() {
        return getDaoSession().getFareRulePlanDao().queryBuilder().limit(1).unique();
    }

    //查询卡方案
    public static CardPlan checkCardPlan(String cardCaseNUm, String cardType, String childCardtype) {
        return getDaoSession().getCardPlanDao().queryBuilder().where(CardPlanDao.Properties.CardCaseNum.eq(cardCaseNUm), CardPlanDao.Properties.ParentCardType.eq(cardType), CardPlanDao.Properties.ChildCardType.eq(childCardtype)).limit(1).unique();
    }

    public static BasicFare getBasicPrice(String pricCaseNum) {
        return getDaoSession().getBasicFareDao().queryBuilder().where(BasicFareDao.Properties.BasepricCaseNum.eq(pricCaseNum)).limit(1).unique();
    }

    public static FareRulePlan checkPricePayRule(String cardCaseNum) {
        return getDaoSession().getFareRulePlanDao().queryBuilder().where(FareRulePlanDao.Properties.FareRulePlanNum.eq(cardCaseNum)).limit(1).unique();
    }


    public static List<PublicKey> getTXPublicKey(String tag, int valueof) {
        String publicCreatTag = "";
        if (tag.equals(PosManager.TX_PUB)) {//微信公钥
            publicCreatTag = "7778707562";
        } else if (tag.equals(PosManager.TX_MAC)) {//微信MAC密钥
            publicCreatTag = "77786D6163";
        } else if (tag.equals(PosManager.ALI_PUB)) {//支付宝公钥
            publicCreatTag = "7A66627075";
        } else if (tag.equals(PosManager.JTB_PUB)) {
            publicCreatTag = "51524D4F54 ";
        } else if (tag.equals(PosManager.DG_PUB)) {
            publicCreatTag = "5152444754";
        } else if (tag.equals(PosManager.FR_PUB)) {//自建码公钥
            publicCreatTag = "6A74627075";
        } else if (tag.equals(PosManager.CL_PUB)) {//城联二维码公钥
            publicCreatTag = "636C707562";
        }
        if (valueof == -1) {
            return getDaoSession().getPublicKeyDao().queryBuilder().where(PublicKeyDao.Properties.PublicCreatTag.eq(publicCreatTag)).list();
        } else {
            return getDaoSession().getPublicKeyDao().queryBuilder().where(PublicKeyDao.Properties.PublicCreatTag.eq(publicCreatTag), PublicKeyDao.Properties.PublicKeyTag.eq(valueof)).list();
        }
    }


    /**
     * 查询单个类型的数据
     * 0001(交易记录里的版本号)-本地 M1 卡、通卡 CPU 卡、微 信、自建码
     * 0002-支付宝乘车码
     * 0003-银联
     *
     * @return
     */
    public static List<XdRecord> checkXdRecord(String type, boolean isCard) throws Exception {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        QueryBuilder<XdRecord> queryBuilder = xdRecordDao.queryBuilder().where(XdRecordDao.Properties.RecordVersion.eq(type));
        if (isCard) {
            queryBuilder.where(XdRecordDao.Properties.ExtraDateLenth.eq("0000"));
        } else {
            queryBuilder.where(XdRecordDao.Properties.ExtraDateLenth.notEq("0000"));
        }
        List<XdRecord> list = queryBuilder.orderDesc(XdRecordDao.Properties.TradeTime).list();
        return list;
    }


    /**
     * 查询银联oda数据
     */
    public static UnionPayEntity checkUnionODA() throws Exception {
        UnionPayEntityDao unionPayEntityDao = getDaoSession().getUnionPayEntityDao();
        return unionPayEntityDao.queryBuilder().where(UnionPayEntityDao.Properties.IsODA.eq(true),
                UnionPayEntityDao.Properties.IsTrade.eq(false)
        ).orderDesc(UnionPayEntityDao.Properties.Time).limit(1).unique();
    }

    public static void saveRecord(XdRecord xdRecord) {
        MiLog.i("刷卡", "卡交易序号   当前记录id：" + xdRecord.getRecordTag() + "    当前记录的状态：" + xdRecord.getStatus() + "      " + xdRecord.getUnionPayStatus());
        MiLog.i("刷卡", "记录保存时的卡交易序号：" + xdRecord.getCardTradeCount() + "  卡余额：" + xdRecord.getBalance() + "    卡号：" + xdRecord.getUseCardnum());
        MiLog.i("刷卡", "保存记录条数" + DBCore.getDaoSession().getXdRecordDao().insertOrReplace(xdRecord));
    }

    public static void updateXdRecord(XdRecord xdRecord) {
        XdRecordDao xdRecordDao = DBCore.getDaoSession().getXdRecordDao();
        xdRecord = xdRecordDao.queryBuilder().where(XdRecordDao.Properties.CreatTime.eq(xdRecord.getCreatTime()), XdRecordDao.Properties.TradeNum.eq(xdRecord.getTradeNum())).limit(1).unique();
        if (xdRecord != null) {
            xdRecord.setUpdateFlag("1");
            MiLog.i("刷卡", "卡交易序号：更新记录");
            saveRecord(xdRecord);
        }
    }

    /**
     * 通过上下行标示
     *
     * @param priceTypeNum 上下行标示（消费点划分编号）
     * @return
     */
    public static List<StationName> checkStation(String priceTypeNum) {
        return DBCore.getDaoSession().getStationNameDao().queryBuilder().where(StationNameDao.Properties.PriceTypeNum.eq(priceTypeNum)).list();
    }

    /**
     * 通过上下行标示
     *
     * @param priceTypeNum 上下行标示（消费点划分编号）
     * @return
     */
    public static StationName checkStation(String priceTypeNum, int id) {
        String nid = "";
        if (id < 10) {
            nid += "0" + id;
        } else {
            nid = "" + id;
        }
        return DBCore.getDaoSession().getStationNameDao().queryBuilder().where(StationNameDao.Properties.PriceTypeNum.eq(priceTypeNum), StationNameDao.Properties.StationNo.eq(nid)).limit(1).unique();
    }

    /**
     * 通过上下行标示
     *
     * @param priceTypeNum 上下行标示（消费点划分编号）
     * @return
     */
    public static StationName checkStationMax(String priceTypeNum) throws Exception {
        return DBCore.getDaoSession().getStationNameDao().queryBuilder().where(StationNameDao.Properties.PriceTypeNum.eq(priceTypeNum)).orderDesc(StationNameDao.Properties.StationNo).limit(1).unique();
    }

    public static BasicFare checkBasicPrice(String priceTypeNum) {
        return DBCore.getDaoSession().getBasicFareDao().queryBuilder().where(BasicFareDao.Properties.PriceTypeNum.eq(priceTypeNum)).limit(1).unique();
    }

    //保存站点的阶梯票价的票价规则
    public static void saveAllStationPrice(List<StationPayPrice> stationPayPrices) {
        StationPayPriceDao stationPayPriceDao = DBCore.getDaoSession().getStationPayPriceDao();
        stationPayPriceDao.insertOrReplaceInTx(stationPayPrices);
    }


    /**
     * 通过站点老查询票价
     *
     * @param priceTypeNum 方向
     * @param up           上车站点
     * @param down         下车站点
     * @return 对应的票价 没有票价则返回-1
     */
    public static long checkMoreTicket(String priceTypeNum, int up, int down) throws Exception {
        StationPayPrice stationPayPrice = DBCore.getDaoSession().getStationPayPriceDao().queryBuilder().where(
                StationPayPriceDao.Properties.PriceTypeNum.eq(priceTypeNum),
                StationPayPriceDao.Properties.Up.eq(up),
                StationPayPriceDao.Properties.Down.eq(down)
        ).limit(1).unique();
        if (stationPayPrice != null) {
            return stationPayPrice.getPrice();
        } else {
            return -1;
        }
    }


    public static void clearAllLine() {
        getDaoSession().getFarePlanDao().deleteAll();//清除计价方案信息
        getDaoSession().getBasicFareDao().deleteAll();//清除基本价格方案
        getDaoSession().getStationDivideDao().deleteAll();//清除所有消费点划分信息
        getDaoSession().getCardPlanDao().deleteAll();//清除卡方案生成规则
        getDaoSession().getCardTypeDao().deleteAll();//卡类型内容
        getDaoSession().getFareRulePlanDao().deleteAll();//计费规则生成规则
        getDaoSession().getStationNameDao().deleteAll();//经纬度设置
        getDaoSession().getStationPayPriceDao().deleteAll();//删除所有的站点消费
        getDaoSession().getStationNameDao().deleteAll();//运营点汉字
    }

    //查询是否有重复的二维码进行上车操作
    public static XdRecord checkRepeatScan(String extraDate) {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(XdRecordDao.Properties.QrCode.eq(extraDate)).limit(1).unique();
    }

    //查询有多少上传的数据
    public static long checkUploadRecodeNum() {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(XdRecordDao.Properties.UpdateFlag.eq("1")).count();
    }

    //查询5000调已上传数据
    public static List<XdRecord> checkUploadRecod() {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(XdRecordDao.Properties.UpdateFlag.eq("1")).limit(5000).list();
    }

    public static void deleteScanRecord(List<XdRecord> scanRecordEntities) {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        xdRecordDao.deleteInTx(scanRecordEntities);
    }

    public static List checkUnionRecord() {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        QueryBuilder<XdRecord> queryBuilder = xdRecordDao.queryBuilder().where(XdRecordDao.Properties.RecordVersion.eq("0003"));
        return queryBuilder.orderDesc(XdRecordDao.Properties.TradeTime).list();
    }

    public static boolean checkRecordIn2H(String cardNo, String direction) {
        direction = FileUtils.formatHexStringToByteString(1, direction);
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(XdRecordDao.Properties.UseCardnum.like("%" + cardNo), XdRecordDao.Properties.Direction.eq(direction), XdRecordDao.Properties.TradeTime.ge(DateUtil.getBeforNow(2))).count() > 0;
    }

    public static XdRecord checkRecordNearNow(String cardNo) {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        Log.i("查询数据", cardNo);
        cardNo = FileUtils.deleteCover(cardNo);
        return xdRecordDao.queryBuilder().where(
                XdRecordDao.Properties.UseCardnum.like("%" + cardNo),
                XdRecordDao.Properties.RecordVersion.notEq("0005"),
                XdRecordDao.Properties.Status.notEq("00")
                , XdRecordDao.Properties.CreatTime.gt(System.currentTimeMillis() - 5 * 60 * 1000)
        ).orderDesc(XdRecordDao.Properties.CreatTime).limit(1).unique();
    }

    public static XdRecord checkICCardRecordNearNow(String cardNo, int limitTime) {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        Log.i("查询数据", cardNo);
        cardNo = FileUtils.deleteCover(cardNo);
        return xdRecordDao.queryBuilder().where(
                XdRecordDao.Properties.UseCardnum.like("%" + cardNo),
                XdRecordDao.Properties.RecordVersion.notEq("0005")
                , XdRecordDao.Properties.CreatTime.gt(System.currentTimeMillis() - limitTime * 1000)
        ).orderDesc(XdRecordDao.Properties.CreatTime).limit(1).unique();
    }

    //插入批量黑名单
    public static void insertBlackList(List<BlackList> blackLists) {
        getDaoSession().getBlackListDao().insertOrReplaceInTx(blackLists);
    }

    //删除批量黑名单
    public static void deleteBlackList(List<BlackList> blackLists) {
        getDaoSession().getBlackListDao().deleteInTx(blackLists);
    }

    //插入单个黑名单
    public static void insertBlack(List<Black> blacks) {
        getDaoSession().getBlackDao().insertOrReplaceInTx(blacks);
    }

    //删除单个黑名单
    public static void deleteBlack(List<Black> blacks) {
        getDaoSession().getBlackDao().deleteInTx(blacks);
    }


    //插入单个黑名单
    public static boolean checkedBlack(String cardNo) {
        cardNo = FileUtils.formatHexStringToByteString(10, cardNo);
        try {
            long countMore = getDaoSession().getBlackListDao().queryBuilder().where(
                    BlackListDao.Properties.CardStart.ge(cardNo),
                    BlackListDao.Properties.CardEnd.le(cardNo)

            ).count();
            if (countMore > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        try {
            long countMore = getDaoSession().getBlackDao().queryBuilder().where(
                    BlackDao.Properties.CardNum.eq(cardNo)
            ).count();
            if (countMore > 0) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    //通过cardno查询一条当前最近的一条正常记录
    public static XdRecord checkXdRecordByCardNo(String cardNo) {
        cardNo = FileUtils.formatHexStringToByteString(10, cardNo);
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(
                XdRecordDao.Properties.UseCardnum.eq(cardNo),
                XdRecordDao.Properties.Status.eq("00"),
                XdRecordDao.Properties.PayType.notEq("dd"),
                XdRecordDao.Properties.InCardStatus.in("00", "01", "02")
        ).orderDesc(XdRecordDao.Properties.CreatTime).limit(1).unique();
    }

    //通过cardno查询一条当前最近的一条正常记录
    public static XdRecord checkScanXdRecordByCardNo(String cardNo) {
        cardNo = FileUtils.formatHexStringToByteString(10, cardNo);
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(
                XdRecordDao.Properties.UseCardnum.eq(cardNo)
        ).orderDesc(XdRecordDao.Properties.CreatTime).limit(1).unique();
    }

    public static XdRecord checkXdRecordByqrCode(String bytesToHexString) {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(XdRecordDao.Properties.QrCode.eq(bytesToHexString)).orderDesc(XdRecordDao.Properties.CreatTime).limit(1).unique();
    }


    public static XdRecord checkRecordNoUp() {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(
                XdRecordDao.Properties.UpdateFlag.eq("0"),
                XdRecordDao.Properties.Status.eq("00"),
                XdRecordDao.Properties.UnionPayStatus.in("FD", "FF", "00", "A2", "A4", "A5", "A6", "FE")
        ).orderAsc(XdRecordDao.Properties.CreatTime).limit(1).unique();
    }

    public static XdRecord checkRecordNoUpErr() {
        XdRecordDao xdRecordDao = getDaoSession().getXdRecordDao();
        return xdRecordDao.queryBuilder().where(
                XdRecordDao.Properties.UpdateFlag.eq("0"),
                XdRecordDao.Properties.Status.notEq("00"),
                XdRecordDao.Properties.CreatTime.lt(System.currentTimeMillis() - 5 * 60 * 1000)
        ).orderAsc(XdRecordDao.Properties.CreatTime).limit(1).unique();
    }


    /******************************经纬度信息采集¬*******************************************/

    //查询采集的gps信息
    public static List<ColletGpsInfo> getCollectStationInfo() {
        ColletGpsInfoDao colletGpsInfoDao = DBCore.getDaoSession().getColletGpsInfoDao();
        return colletGpsInfoDao.queryBuilder().build().list();
    }

    public static void addStationInfo(ColletGpsInfo colletGpsInfo) {
        ColletGpsInfoDao colletGpsInfoDao = DBCore.getDaoSession().getColletGpsInfoDao();
        colletGpsInfoDao.insertOrReplace(colletGpsInfo);
    }

    public static List<ColletGpsInfo> selectColletNumForDiraction(int dira) {
        ColletGpsInfoDao colletGpsInfoDao = DBCore.getDaoSession().getColletGpsInfoDao();
        return colletGpsInfoDao.queryBuilder().where(ColletGpsInfoDao.Properties.Diraction.eq(dira)).list();
    }

    public static void clearStation() {
        ColletGpsInfoDao colletGpsInfoDao = DBCore.getDaoSession().getColletGpsInfoDao();
        colletGpsInfoDao.deleteAll();
    }

    public static void deleteStation(ColletGpsInfo colletGpsInfo) {
        ColletGpsInfoDao colletGpsInfoDao = DBCore.getDaoSession().getColletGpsInfoDao();
        colletGpsInfoDao.delete(colletGpsInfo);
    }

    public static long checkUnUp() {
        return DBCore.getDaoSession().getXdRecordDao().queryBuilder().where(XdRecordDao.Properties.UpdateFlag.eq("0")).count();
    }


    //插入单个黑名单
    public static void insertWhite(List<Whitelist> whitelists) {
        getDaoSession().getWhitelistDao().insertOrReplaceInTx(whitelists);
    }

    //删除单个黑名单
    public static void deleteWhite() {
        getDaoSession().getWhitelistDao().deleteAll();
    }

    public static boolean checkedWhite(String card_issuer) {
        return getDaoSession().getWhitelistDao().queryBuilder().where(WhitelistDao.Properties.Cardno.like("%" + card_issuer)).list().size() > 0;
    }

    public static long checkXdRecordUpload() {
        //查询已上传的记录数量
        return getDaoSession().getXdRecordDao().queryBuilder().where(XdRecordDao.Properties.UpdateFlag.eq(1)).count();
    }

    public static List<XdRecord> checkXdRecordUploadList() {
        return getDaoSession().getXdRecordDao().queryBuilder().where(XdRecordDao.Properties.UpdateFlag.eq(1)).list();
    }

    public static void deleteXdRecord() {
        getDaoSession().getXdRecordDao().deleteAll();
    }
}
