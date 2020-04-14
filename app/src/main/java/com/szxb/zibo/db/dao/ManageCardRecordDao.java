package com.szxb.zibo.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.szxb.zibo.record.ManageCardRecord;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MANAGE_CARD_RECORD".
*/
public class ManageCardRecordDao extends AbstractDao<ManageCardRecord, Long> {

    public static final String TABLENAME = "MANAGE_CARD_RECORD";

    /**
     * Properties of entity ManageCardRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property BizType = new Property(1, String.class, "bizType", false, "BIZ_TYPE");
        public final static Property TransSeqId = new Property(2, String.class, "transSeqId", false, "TRANS_SEQ_ID");
        public final static Property SignType = new Property(3, String.class, "signType", false, "SIGN_TYPE");
        public final static Property TransCityCode = new Property(4, String.class, "transCityCode", false, "TRANS_CITY_CODE");
        public final static Property Acquirer = new Property(5, String.class, "acquirer", false, "ACQUIRER");
        public final static Property TerminalId = new Property(6, String.class, "terminalId", false, "TERMINAL_ID");
        public final static Property PsamCardId = new Property(7, String.class, "psamCardId", false, "PSAM_CARD_ID");
        public final static Property PsamTerminalId = new Property(8, String.class, "psamTerminalId", false, "PSAM_TERMINAL_ID");
        public final static Property LineId = new Property(9, String.class, "lineId", false, "LINE_ID");
        public final static Property TransTime = new Property(10, String.class, "transTime", false, "TRANS_TIME");
        public final static Property RefeNo = new Property(11, String.class, "refeNo", false, "REFE_NO");
        public final static Property VeguckeNO = new Property(12, String.class, "veguckeNO", false, "VEGUCKE_NO");
        public final static Property CardId = new Property(13, String.class, "cardId", false, "CARD_ID");
        public final static Property JobNO = new Property(14, String.class, "jobNO", false, "JOB_NO");
        public final static Property UpStatus = new Property(15, int.class, "UpStatus", false, "UP_STATUS");
        public final static Property CreateTime = new Property(16, int.class, "createTime", false, "CREATE_TIME");
    }


    public ManageCardRecordDao(DaoConfig config) {
        super(config);
    }
    
    public ManageCardRecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MANAGE_CARD_RECORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BIZ_TYPE\" TEXT," + // 1: bizType
                "\"TRANS_SEQ_ID\" TEXT," + // 2: transSeqId
                "\"SIGN_TYPE\" TEXT," + // 3: signType
                "\"TRANS_CITY_CODE\" TEXT," + // 4: transCityCode
                "\"ACQUIRER\" TEXT," + // 5: acquirer
                "\"TERMINAL_ID\" TEXT," + // 6: terminalId
                "\"PSAM_CARD_ID\" TEXT," + // 7: psamCardId
                "\"PSAM_TERMINAL_ID\" TEXT," + // 8: psamTerminalId
                "\"LINE_ID\" TEXT," + // 9: lineId
                "\"TRANS_TIME\" TEXT," + // 10: transTime
                "\"REFE_NO\" TEXT," + // 11: refeNo
                "\"VEGUCKE_NO\" TEXT," + // 12: veguckeNO
                "\"CARD_ID\" TEXT," + // 13: cardId
                "\"JOB_NO\" TEXT," + // 14: jobNO
                "\"UP_STATUS\" INTEGER NOT NULL ," + // 15: UpStatus
                "\"CREATE_TIME\" INTEGER NOT NULL );"); // 16: createTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MANAGE_CARD_RECORD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ManageCardRecord entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bizType = entity.getBizType();
        if (bizType != null) {
            stmt.bindString(2, bizType);
        }
 
        String transSeqId = entity.getTransSeqId();
        if (transSeqId != null) {
            stmt.bindString(3, transSeqId);
        }
 
        String signType = entity.getSignType();
        if (signType != null) {
            stmt.bindString(4, signType);
        }
 
        String transCityCode = entity.getTransCityCode();
        if (transCityCode != null) {
            stmt.bindString(5, transCityCode);
        }
 
        String acquirer = entity.getAcquirer();
        if (acquirer != null) {
            stmt.bindString(6, acquirer);
        }
 
        String terminalId = entity.getTerminalId();
        if (terminalId != null) {
            stmt.bindString(7, terminalId);
        }
 
        String psamCardId = entity.getPsamCardId();
        if (psamCardId != null) {
            stmt.bindString(8, psamCardId);
        }
 
        String psamTerminalId = entity.getPsamTerminalId();
        if (psamTerminalId != null) {
            stmt.bindString(9, psamTerminalId);
        }
 
        String lineId = entity.getLineId();
        if (lineId != null) {
            stmt.bindString(10, lineId);
        }
 
        String transTime = entity.getTransTime();
        if (transTime != null) {
            stmt.bindString(11, transTime);
        }
 
        String refeNo = entity.getRefeNo();
        if (refeNo != null) {
            stmt.bindString(12, refeNo);
        }
 
        String veguckeNO = entity.getVeguckeNO();
        if (veguckeNO != null) {
            stmt.bindString(13, veguckeNO);
        }
 
        String cardId = entity.getCardId();
        if (cardId != null) {
            stmt.bindString(14, cardId);
        }
 
        String jobNO = entity.getJobNO();
        if (jobNO != null) {
            stmt.bindString(15, jobNO);
        }
        stmt.bindLong(16, entity.getUpStatus());
        stmt.bindLong(17, entity.getCreateTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ManageCardRecord entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bizType = entity.getBizType();
        if (bizType != null) {
            stmt.bindString(2, bizType);
        }
 
        String transSeqId = entity.getTransSeqId();
        if (transSeqId != null) {
            stmt.bindString(3, transSeqId);
        }
 
        String signType = entity.getSignType();
        if (signType != null) {
            stmt.bindString(4, signType);
        }
 
        String transCityCode = entity.getTransCityCode();
        if (transCityCode != null) {
            stmt.bindString(5, transCityCode);
        }
 
        String acquirer = entity.getAcquirer();
        if (acquirer != null) {
            stmt.bindString(6, acquirer);
        }
 
        String terminalId = entity.getTerminalId();
        if (terminalId != null) {
            stmt.bindString(7, terminalId);
        }
 
        String psamCardId = entity.getPsamCardId();
        if (psamCardId != null) {
            stmt.bindString(8, psamCardId);
        }
 
        String psamTerminalId = entity.getPsamTerminalId();
        if (psamTerminalId != null) {
            stmt.bindString(9, psamTerminalId);
        }
 
        String lineId = entity.getLineId();
        if (lineId != null) {
            stmt.bindString(10, lineId);
        }
 
        String transTime = entity.getTransTime();
        if (transTime != null) {
            stmt.bindString(11, transTime);
        }
 
        String refeNo = entity.getRefeNo();
        if (refeNo != null) {
            stmt.bindString(12, refeNo);
        }
 
        String veguckeNO = entity.getVeguckeNO();
        if (veguckeNO != null) {
            stmt.bindString(13, veguckeNO);
        }
 
        String cardId = entity.getCardId();
        if (cardId != null) {
            stmt.bindString(14, cardId);
        }
 
        String jobNO = entity.getJobNO();
        if (jobNO != null) {
            stmt.bindString(15, jobNO);
        }
        stmt.bindLong(16, entity.getUpStatus());
        stmt.bindLong(17, entity.getCreateTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ManageCardRecord readEntity(Cursor cursor, int offset) {
        ManageCardRecord entity = new ManageCardRecord( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bizType
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // transSeqId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // signType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // transCityCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // acquirer
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // terminalId
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // psamCardId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // psamTerminalId
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // lineId
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // transTime
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // refeNo
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // veguckeNO
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // cardId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // jobNO
            cursor.getInt(offset + 15), // UpStatus
            cursor.getInt(offset + 16) // createTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ManageCardRecord entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBizType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTransSeqId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSignType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTransCityCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAcquirer(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTerminalId(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPsamCardId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPsamTerminalId(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLineId(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setTransTime(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRefeNo(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setVeguckeNO(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCardId(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setJobNO(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setUpStatus(cursor.getInt(offset + 15));
        entity.setCreateTime(cursor.getInt(offset + 16));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ManageCardRecord entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ManageCardRecord entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ManageCardRecord entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
