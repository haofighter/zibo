package com.szxb.zibo.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.szxb.zibo.record.JTBscanRecord;
import com.szxb.zibo.record.XdRecord;
import com.szxb.zibo.record.AppParamInfo;
import com.szxb.zibo.record.ManageCardRecord;
import com.szxb.zibo.config.haikou.BlackList;
import com.szxb.zibo.config.haikou.param.BuildConfigParam;
import com.szxb.zibo.config.haikou.Whitelist;
import com.szxb.zibo.config.haikou.Black;
import com.szxb.zibo.config.zibo.line.CardRuleInfoAll;
import com.szxb.zibo.config.zibo.line.BasicFare;
import com.szxb.zibo.config.zibo.line.CardType;
import com.szxb.zibo.config.zibo.line.StationDivide;
import com.szxb.zibo.config.zibo.line.StationName;
import com.szxb.zibo.config.zibo.line.FarePlan;
import com.szxb.zibo.config.zibo.line.StationPayPrice;
import com.szxb.zibo.config.zibo.line.CardPlan;
import com.szxb.zibo.config.zibo.line.FareRulePlan;
import com.szxb.zibo.config.zibo.line.ZBLineInfo;
import com.szxb.zibo.config.zibo.line.ContinuousRule;
import com.szxb.zibo.config.zibo.PublicKey;
import com.szxb.zibo.moudle.function.gps.ColletGpsInfo;
import com.szxb.zibo.moudle.function.unionpay.entity.UnionPayEntity;
import com.szxb.zibo.moudle.function.unionpay.entity.UnionAidEntity;

import com.szxb.zibo.db.dao.JTBscanRecordDao;
import com.szxb.zibo.db.dao.XdRecordDao;
import com.szxb.zibo.db.dao.AppParamInfoDao;
import com.szxb.zibo.db.dao.ManageCardRecordDao;
import com.szxb.zibo.db.dao.BlackListDao;
import com.szxb.zibo.db.dao.BuildConfigParamDao;
import com.szxb.zibo.db.dao.WhitelistDao;
import com.szxb.zibo.db.dao.BlackDao;
import com.szxb.zibo.db.dao.CardRuleInfoAllDao;
import com.szxb.zibo.db.dao.BasicFareDao;
import com.szxb.zibo.db.dao.CardTypeDao;
import com.szxb.zibo.db.dao.StationDivideDao;
import com.szxb.zibo.db.dao.StationNameDao;
import com.szxb.zibo.db.dao.FarePlanDao;
import com.szxb.zibo.db.dao.StationPayPriceDao;
import com.szxb.zibo.db.dao.CardPlanDao;
import com.szxb.zibo.db.dao.FareRulePlanDao;
import com.szxb.zibo.db.dao.ZBLineInfoDao;
import com.szxb.zibo.db.dao.ContinuousRuleDao;
import com.szxb.zibo.db.dao.PublicKeyDao;
import com.szxb.zibo.db.dao.ColletGpsInfoDao;
import com.szxb.zibo.db.dao.UnionPayEntityDao;
import com.szxb.zibo.db.dao.UnionAidEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig jTBscanRecordDaoConfig;
    private final DaoConfig xdRecordDaoConfig;
    private final DaoConfig appParamInfoDaoConfig;
    private final DaoConfig manageCardRecordDaoConfig;
    private final DaoConfig blackListDaoConfig;
    private final DaoConfig buildConfigParamDaoConfig;
    private final DaoConfig whitelistDaoConfig;
    private final DaoConfig blackDaoConfig;
    private final DaoConfig cardRuleInfoAllDaoConfig;
    private final DaoConfig basicFareDaoConfig;
    private final DaoConfig cardTypeDaoConfig;
    private final DaoConfig stationDivideDaoConfig;
    private final DaoConfig stationNameDaoConfig;
    private final DaoConfig farePlanDaoConfig;
    private final DaoConfig stationPayPriceDaoConfig;
    private final DaoConfig cardPlanDaoConfig;
    private final DaoConfig fareRulePlanDaoConfig;
    private final DaoConfig zBLineInfoDaoConfig;
    private final DaoConfig continuousRuleDaoConfig;
    private final DaoConfig publicKeyDaoConfig;
    private final DaoConfig colletGpsInfoDaoConfig;
    private final DaoConfig unionPayEntityDaoConfig;
    private final DaoConfig unionAidEntityDaoConfig;

    private final JTBscanRecordDao jTBscanRecordDao;
    private final XdRecordDao xdRecordDao;
    private final AppParamInfoDao appParamInfoDao;
    private final ManageCardRecordDao manageCardRecordDao;
    private final BlackListDao blackListDao;
    private final BuildConfigParamDao buildConfigParamDao;
    private final WhitelistDao whitelistDao;
    private final BlackDao blackDao;
    private final CardRuleInfoAllDao cardRuleInfoAllDao;
    private final BasicFareDao basicFareDao;
    private final CardTypeDao cardTypeDao;
    private final StationDivideDao stationDivideDao;
    private final StationNameDao stationNameDao;
    private final FarePlanDao farePlanDao;
    private final StationPayPriceDao stationPayPriceDao;
    private final CardPlanDao cardPlanDao;
    private final FareRulePlanDao fareRulePlanDao;
    private final ZBLineInfoDao zBLineInfoDao;
    private final ContinuousRuleDao continuousRuleDao;
    private final PublicKeyDao publicKeyDao;
    private final ColletGpsInfoDao colletGpsInfoDao;
    private final UnionPayEntityDao unionPayEntityDao;
    private final UnionAidEntityDao unionAidEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        jTBscanRecordDaoConfig = daoConfigMap.get(JTBscanRecordDao.class).clone();
        jTBscanRecordDaoConfig.initIdentityScope(type);

        xdRecordDaoConfig = daoConfigMap.get(XdRecordDao.class).clone();
        xdRecordDaoConfig.initIdentityScope(type);

        appParamInfoDaoConfig = daoConfigMap.get(AppParamInfoDao.class).clone();
        appParamInfoDaoConfig.initIdentityScope(type);

        manageCardRecordDaoConfig = daoConfigMap.get(ManageCardRecordDao.class).clone();
        manageCardRecordDaoConfig.initIdentityScope(type);

        blackListDaoConfig = daoConfigMap.get(BlackListDao.class).clone();
        blackListDaoConfig.initIdentityScope(type);

        buildConfigParamDaoConfig = daoConfigMap.get(BuildConfigParamDao.class).clone();
        buildConfigParamDaoConfig.initIdentityScope(type);

        whitelistDaoConfig = daoConfigMap.get(WhitelistDao.class).clone();
        whitelistDaoConfig.initIdentityScope(type);

        blackDaoConfig = daoConfigMap.get(BlackDao.class).clone();
        blackDaoConfig.initIdentityScope(type);

        cardRuleInfoAllDaoConfig = daoConfigMap.get(CardRuleInfoAllDao.class).clone();
        cardRuleInfoAllDaoConfig.initIdentityScope(type);

        basicFareDaoConfig = daoConfigMap.get(BasicFareDao.class).clone();
        basicFareDaoConfig.initIdentityScope(type);

        cardTypeDaoConfig = daoConfigMap.get(CardTypeDao.class).clone();
        cardTypeDaoConfig.initIdentityScope(type);

        stationDivideDaoConfig = daoConfigMap.get(StationDivideDao.class).clone();
        stationDivideDaoConfig.initIdentityScope(type);

        stationNameDaoConfig = daoConfigMap.get(StationNameDao.class).clone();
        stationNameDaoConfig.initIdentityScope(type);

        farePlanDaoConfig = daoConfigMap.get(FarePlanDao.class).clone();
        farePlanDaoConfig.initIdentityScope(type);

        stationPayPriceDaoConfig = daoConfigMap.get(StationPayPriceDao.class).clone();
        stationPayPriceDaoConfig.initIdentityScope(type);

        cardPlanDaoConfig = daoConfigMap.get(CardPlanDao.class).clone();
        cardPlanDaoConfig.initIdentityScope(type);

        fareRulePlanDaoConfig = daoConfigMap.get(FareRulePlanDao.class).clone();
        fareRulePlanDaoConfig.initIdentityScope(type);

        zBLineInfoDaoConfig = daoConfigMap.get(ZBLineInfoDao.class).clone();
        zBLineInfoDaoConfig.initIdentityScope(type);

        continuousRuleDaoConfig = daoConfigMap.get(ContinuousRuleDao.class).clone();
        continuousRuleDaoConfig.initIdentityScope(type);

        publicKeyDaoConfig = daoConfigMap.get(PublicKeyDao.class).clone();
        publicKeyDaoConfig.initIdentityScope(type);

        colletGpsInfoDaoConfig = daoConfigMap.get(ColletGpsInfoDao.class).clone();
        colletGpsInfoDaoConfig.initIdentityScope(type);

        unionPayEntityDaoConfig = daoConfigMap.get(UnionPayEntityDao.class).clone();
        unionPayEntityDaoConfig.initIdentityScope(type);

        unionAidEntityDaoConfig = daoConfigMap.get(UnionAidEntityDao.class).clone();
        unionAidEntityDaoConfig.initIdentityScope(type);

        jTBscanRecordDao = new JTBscanRecordDao(jTBscanRecordDaoConfig, this);
        xdRecordDao = new XdRecordDao(xdRecordDaoConfig, this);
        appParamInfoDao = new AppParamInfoDao(appParamInfoDaoConfig, this);
        manageCardRecordDao = new ManageCardRecordDao(manageCardRecordDaoConfig, this);
        blackListDao = new BlackListDao(blackListDaoConfig, this);
        buildConfigParamDao = new BuildConfigParamDao(buildConfigParamDaoConfig, this);
        whitelistDao = new WhitelistDao(whitelistDaoConfig, this);
        blackDao = new BlackDao(blackDaoConfig, this);
        cardRuleInfoAllDao = new CardRuleInfoAllDao(cardRuleInfoAllDaoConfig, this);
        basicFareDao = new BasicFareDao(basicFareDaoConfig, this);
        cardTypeDao = new CardTypeDao(cardTypeDaoConfig, this);
        stationDivideDao = new StationDivideDao(stationDivideDaoConfig, this);
        stationNameDao = new StationNameDao(stationNameDaoConfig, this);
        farePlanDao = new FarePlanDao(farePlanDaoConfig, this);
        stationPayPriceDao = new StationPayPriceDao(stationPayPriceDaoConfig, this);
        cardPlanDao = new CardPlanDao(cardPlanDaoConfig, this);
        fareRulePlanDao = new FareRulePlanDao(fareRulePlanDaoConfig, this);
        zBLineInfoDao = new ZBLineInfoDao(zBLineInfoDaoConfig, this);
        continuousRuleDao = new ContinuousRuleDao(continuousRuleDaoConfig, this);
        publicKeyDao = new PublicKeyDao(publicKeyDaoConfig, this);
        colletGpsInfoDao = new ColletGpsInfoDao(colletGpsInfoDaoConfig, this);
        unionPayEntityDao = new UnionPayEntityDao(unionPayEntityDaoConfig, this);
        unionAidEntityDao = new UnionAidEntityDao(unionAidEntityDaoConfig, this);

        registerDao(JTBscanRecord.class, jTBscanRecordDao);
        registerDao(XdRecord.class, xdRecordDao);
        registerDao(AppParamInfo.class, appParamInfoDao);
        registerDao(ManageCardRecord.class, manageCardRecordDao);
        registerDao(BlackList.class, blackListDao);
        registerDao(BuildConfigParam.class, buildConfigParamDao);
        registerDao(Whitelist.class, whitelistDao);
        registerDao(Black.class, blackDao);
        registerDao(CardRuleInfoAll.class, cardRuleInfoAllDao);
        registerDao(BasicFare.class, basicFareDao);
        registerDao(CardType.class, cardTypeDao);
        registerDao(StationDivide.class, stationDivideDao);
        registerDao(StationName.class, stationNameDao);
        registerDao(FarePlan.class, farePlanDao);
        registerDao(StationPayPrice.class, stationPayPriceDao);
        registerDao(CardPlan.class, cardPlanDao);
        registerDao(FareRulePlan.class, fareRulePlanDao);
        registerDao(ZBLineInfo.class, zBLineInfoDao);
        registerDao(ContinuousRule.class, continuousRuleDao);
        registerDao(PublicKey.class, publicKeyDao);
        registerDao(ColletGpsInfo.class, colletGpsInfoDao);
        registerDao(UnionPayEntity.class, unionPayEntityDao);
        registerDao(UnionAidEntity.class, unionAidEntityDao);
    }
    
    public void clear() {
        jTBscanRecordDaoConfig.clearIdentityScope();
        xdRecordDaoConfig.clearIdentityScope();
        appParamInfoDaoConfig.clearIdentityScope();
        manageCardRecordDaoConfig.clearIdentityScope();
        blackListDaoConfig.clearIdentityScope();
        buildConfigParamDaoConfig.clearIdentityScope();
        whitelistDaoConfig.clearIdentityScope();
        blackDaoConfig.clearIdentityScope();
        cardRuleInfoAllDaoConfig.clearIdentityScope();
        basicFareDaoConfig.clearIdentityScope();
        cardTypeDaoConfig.clearIdentityScope();
        stationDivideDaoConfig.clearIdentityScope();
        stationNameDaoConfig.clearIdentityScope();
        farePlanDaoConfig.clearIdentityScope();
        stationPayPriceDaoConfig.clearIdentityScope();
        cardPlanDaoConfig.clearIdentityScope();
        fareRulePlanDaoConfig.clearIdentityScope();
        zBLineInfoDaoConfig.clearIdentityScope();
        continuousRuleDaoConfig.clearIdentityScope();
        publicKeyDaoConfig.clearIdentityScope();
        colletGpsInfoDaoConfig.clearIdentityScope();
        unionPayEntityDaoConfig.clearIdentityScope();
        unionAidEntityDaoConfig.clearIdentityScope();
    }

    public JTBscanRecordDao getJTBscanRecordDao() {
        return jTBscanRecordDao;
    }

    public XdRecordDao getXdRecordDao() {
        return xdRecordDao;
    }

    public AppParamInfoDao getAppParamInfoDao() {
        return appParamInfoDao;
    }

    public ManageCardRecordDao getManageCardRecordDao() {
        return manageCardRecordDao;
    }

    public BlackListDao getBlackListDao() {
        return blackListDao;
    }

    public BuildConfigParamDao getBuildConfigParamDao() {
        return buildConfigParamDao;
    }

    public WhitelistDao getWhitelistDao() {
        return whitelistDao;
    }

    public BlackDao getBlackDao() {
        return blackDao;
    }

    public CardRuleInfoAllDao getCardRuleInfoAllDao() {
        return cardRuleInfoAllDao;
    }

    public BasicFareDao getBasicFareDao() {
        return basicFareDao;
    }

    public CardTypeDao getCardTypeDao() {
        return cardTypeDao;
    }

    public StationDivideDao getStationDivideDao() {
        return stationDivideDao;
    }

    public StationNameDao getStationNameDao() {
        return stationNameDao;
    }

    public FarePlanDao getFarePlanDao() {
        return farePlanDao;
    }

    public StationPayPriceDao getStationPayPriceDao() {
        return stationPayPriceDao;
    }

    public CardPlanDao getCardPlanDao() {
        return cardPlanDao;
    }

    public FareRulePlanDao getFareRulePlanDao() {
        return fareRulePlanDao;
    }

    public ZBLineInfoDao getZBLineInfoDao() {
        return zBLineInfoDao;
    }

    public ContinuousRuleDao getContinuousRuleDao() {
        return continuousRuleDao;
    }

    public PublicKeyDao getPublicKeyDao() {
        return publicKeyDao;
    }

    public ColletGpsInfoDao getColletGpsInfoDao() {
        return colletGpsInfoDao;
    }

    public UnionPayEntityDao getUnionPayEntityDao() {
        return unionPayEntityDao;
    }

    public UnionAidEntityDao getUnionAidEntityDao() {
        return unionAidEntityDao;
    }

}
