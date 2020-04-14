package com.szxb.zibo.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 23): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 23;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        JTBscanRecordDao.createTable(db, ifNotExists);
        XdRecordDao.createTable(db, ifNotExists);
        AppParamInfoDao.createTable(db, ifNotExists);
        ManageCardRecordDao.createTable(db, ifNotExists);
        BlackListDao.createTable(db, ifNotExists);
        BuildConfigParamDao.createTable(db, ifNotExists);
        WhitelistDao.createTable(db, ifNotExists);
        BlackDao.createTable(db, ifNotExists);
        CardRuleInfoAllDao.createTable(db, ifNotExists);
        BasicFareDao.createTable(db, ifNotExists);
        CardTypeDao.createTable(db, ifNotExists);
        StationDivideDao.createTable(db, ifNotExists);
        StationNameDao.createTable(db, ifNotExists);
        FarePlanDao.createTable(db, ifNotExists);
        StationPayPriceDao.createTable(db, ifNotExists);
        CardPlanDao.createTable(db, ifNotExists);
        FareRulePlanDao.createTable(db, ifNotExists);
        ZBLineInfoDao.createTable(db, ifNotExists);
        ContinuousRuleDao.createTable(db, ifNotExists);
        PublicKeyDao.createTable(db, ifNotExists);
        ColletGpsInfoDao.createTable(db, ifNotExists);
        UnionPayEntityDao.createTable(db, ifNotExists);
        UnionAidEntityDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        JTBscanRecordDao.dropTable(db, ifExists);
        XdRecordDao.dropTable(db, ifExists);
        AppParamInfoDao.dropTable(db, ifExists);
        ManageCardRecordDao.dropTable(db, ifExists);
        BlackListDao.dropTable(db, ifExists);
        BuildConfigParamDao.dropTable(db, ifExists);
        WhitelistDao.dropTable(db, ifExists);
        BlackDao.dropTable(db, ifExists);
        CardRuleInfoAllDao.dropTable(db, ifExists);
        BasicFareDao.dropTable(db, ifExists);
        CardTypeDao.dropTable(db, ifExists);
        StationDivideDao.dropTable(db, ifExists);
        StationNameDao.dropTable(db, ifExists);
        FarePlanDao.dropTable(db, ifExists);
        StationPayPriceDao.dropTable(db, ifExists);
        CardPlanDao.dropTable(db, ifExists);
        FareRulePlanDao.dropTable(db, ifExists);
        ZBLineInfoDao.dropTable(db, ifExists);
        ContinuousRuleDao.dropTable(db, ifExists);
        PublicKeyDao.dropTable(db, ifExists);
        ColletGpsInfoDao.dropTable(db, ifExists);
        UnionPayEntityDao.dropTable(db, ifExists);
        UnionAidEntityDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DevOpenHelper}.
     */
    public static DaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(JTBscanRecordDao.class);
        registerDaoClass(XdRecordDao.class);
        registerDaoClass(AppParamInfoDao.class);
        registerDaoClass(ManageCardRecordDao.class);
        registerDaoClass(BlackListDao.class);
        registerDaoClass(BuildConfigParamDao.class);
        registerDaoClass(WhitelistDao.class);
        registerDaoClass(BlackDao.class);
        registerDaoClass(CardRuleInfoAllDao.class);
        registerDaoClass(BasicFareDao.class);
        registerDaoClass(CardTypeDao.class);
        registerDaoClass(StationDivideDao.class);
        registerDaoClass(StationNameDao.class);
        registerDaoClass(FarePlanDao.class);
        registerDaoClass(StationPayPriceDao.class);
        registerDaoClass(CardPlanDao.class);
        registerDaoClass(FareRulePlanDao.class);
        registerDaoClass(ZBLineInfoDao.class);
        registerDaoClass(ContinuousRuleDao.class);
        registerDaoClass(PublicKeyDao.class);
        registerDaoClass(ColletGpsInfoDao.class);
        registerDaoClass(UnionPayEntityDao.class);
        registerDaoClass(UnionAidEntityDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}