package com.szxb.zibo.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.szxb.zibo.config.zibo.line.StationDivide;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STATION_DIVIDE".
*/
public class StationDivideDao extends AbstractDao<StationDivide, Void> {

    public static final String TABLENAME = "STATION_DIVIDE";

    /**
     * Properties of entity StationDivide.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property PriceTypeNum = new Property(0, String.class, "priceTypeNum", false, "PRICE_TYPE_NUM");
        public final static Property PirceAdd = new Property(1, String.class, "pirceAdd", false, "PIRCE_ADD");
        public final static Property Rev1 = new Property(2, String.class, "rev1", false, "REV1");
        public final static Property Rev2 = new Property(3, String.class, "rev2", false, "REV2");
        public final static Property Rev3 = new Property(4, String.class, "rev3", false, "REV3");
        public final static Property Rev4 = new Property(5, String.class, "rev4", false, "REV4");
        public final static Property Rev5 = new Property(6, String.class, "rev5", false, "REV5");
    }


    public StationDivideDao(DaoConfig config) {
        super(config);
    }
    
    public StationDivideDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STATION_DIVIDE\" (" + //
                "\"PRICE_TYPE_NUM\" TEXT," + // 0: priceTypeNum
                "\"PIRCE_ADD\" TEXT," + // 1: pirceAdd
                "\"REV1\" TEXT," + // 2: rev1
                "\"REV2\" TEXT," + // 3: rev2
                "\"REV3\" TEXT," + // 4: rev3
                "\"REV4\" TEXT," + // 5: rev4
                "\"REV5\" TEXT);"); // 6: rev5
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STATION_DIVIDE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, StationDivide entity) {
        stmt.clearBindings();
 
        String priceTypeNum = entity.getPriceTypeNum();
        if (priceTypeNum != null) {
            stmt.bindString(1, priceTypeNum);
        }
 
        String pirceAdd = entity.getPirceAdd();
        if (pirceAdd != null) {
            stmt.bindString(2, pirceAdd);
        }
 
        String rev1 = entity.getRev1();
        if (rev1 != null) {
            stmt.bindString(3, rev1);
        }
 
        String rev2 = entity.getRev2();
        if (rev2 != null) {
            stmt.bindString(4, rev2);
        }
 
        String rev3 = entity.getRev3();
        if (rev3 != null) {
            stmt.bindString(5, rev3);
        }
 
        String rev4 = entity.getRev4();
        if (rev4 != null) {
            stmt.bindString(6, rev4);
        }
 
        String rev5 = entity.getRev5();
        if (rev5 != null) {
            stmt.bindString(7, rev5);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, StationDivide entity) {
        stmt.clearBindings();
 
        String priceTypeNum = entity.getPriceTypeNum();
        if (priceTypeNum != null) {
            stmt.bindString(1, priceTypeNum);
        }
 
        String pirceAdd = entity.getPirceAdd();
        if (pirceAdd != null) {
            stmt.bindString(2, pirceAdd);
        }
 
        String rev1 = entity.getRev1();
        if (rev1 != null) {
            stmt.bindString(3, rev1);
        }
 
        String rev2 = entity.getRev2();
        if (rev2 != null) {
            stmt.bindString(4, rev2);
        }
 
        String rev3 = entity.getRev3();
        if (rev3 != null) {
            stmt.bindString(5, rev3);
        }
 
        String rev4 = entity.getRev4();
        if (rev4 != null) {
            stmt.bindString(6, rev4);
        }
 
        String rev5 = entity.getRev5();
        if (rev5 != null) {
            stmt.bindString(7, rev5);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public StationDivide readEntity(Cursor cursor, int offset) {
        StationDivide entity = new StationDivide( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // priceTypeNum
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // pirceAdd
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // rev1
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // rev2
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // rev3
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // rev4
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // rev5
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, StationDivide entity, int offset) {
        entity.setPriceTypeNum(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setPirceAdd(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRev1(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRev2(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRev3(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRev4(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRev5(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(StationDivide entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(StationDivide entity) {
        return null;
    }

    @Override
    public boolean hasKey(StationDivide entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
