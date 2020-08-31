package com.szxb.zibo.db.manage;

import android.content.Context;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.szxb.zibo.db.dao.AppParamInfoDao;
import com.szxb.zibo.db.dao.BasicFareDao;
import com.szxb.zibo.db.dao.BlackDao;
import com.szxb.zibo.db.dao.BlackListDao;
import com.szxb.zibo.db.dao.BuildConfigParamDao;
import com.szxb.zibo.db.dao.CardPlanDao;
import com.szxb.zibo.db.dao.CardRuleInfoAllDao;
import com.szxb.zibo.db.dao.CardTypeDao;
import com.szxb.zibo.db.dao.ContinuousRuleDao;
import com.szxb.zibo.db.dao.DaoMaster;
import com.szxb.zibo.db.dao.FarePlanDao;
import com.szxb.zibo.db.dao.FareRulePlanDao;
import com.szxb.zibo.db.dao.JTBscanRecordDao;
import com.szxb.zibo.db.dao.ManageCardRecordDao;
import com.szxb.zibo.db.dao.PublicKeyDao;
import com.szxb.zibo.db.dao.StationDivideDao;
import com.szxb.zibo.db.dao.StationNameDao;
import com.szxb.zibo.db.dao.StationPayPriceDao;
import com.szxb.zibo.db.dao.UnionAidEntityDao;
import com.szxb.zibo.db.dao.UnionPayEntityDao;
import com.szxb.zibo.db.dao.WhitelistDao;
import com.szxb.zibo.db.dao.XdRecordDao;
import com.szxb.zibo.db.dao.ZBLineInfoDao;
import com.szxb.zibo.record.AppParamInfo;

import org.greenrobot.greendao.database.Database;


public class DBHelper extends DaoMaster.OpenHelper {

    DBHelper(Context context, String name) {
        super(context, name);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        update(db, oldVersion, newVersion);
    }


    private void update(Database db, int oldVersion, int newVersion) {
        //把需要管理的数据库表DAO作为最后一个参数传入到方法中
        MigrationHelper.migrate(
                db, new MigrationHelper.ReCreateAllTableListener() {
                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                },
                BasicFareDao.class,
                BlackDao.class,
                BlackListDao.class,
                BuildConfigParamDao.class,
                CardPlanDao.class,
                CardRuleInfoAllDao.class,
                CardTypeDao.class,
                ContinuousRuleDao.class,
                FarePlanDao.class,
                FareRulePlanDao.class,
                JTBscanRecordDao.class,
                ManageCardRecordDao.class,
                PublicKeyDao.class,
                StationDivideDao.class,
                StationNameDao.class,
                StationPayPriceDao.class,
                UnionAidEntityDao.class,
                UnionPayEntityDao.class,
                WhitelistDao.class,
                XdRecordDao.class,
                ZBLineInfoDao.class,
                AppParamInfoDao.class
        );
    }
}
