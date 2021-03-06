package com.szxb.zibo.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.szxb.zibo.config.zibo.line.CardRuleInfoAll;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CARD_RULE_INFO_ALL".
*/
public class CardRuleInfoAllDao extends AbstractDao<CardRuleInfoAll, Void> {

    public static final String TABLENAME = "CARD_RULE_INFO_ALL";

    /**
     * Properties of entity CardRuleInfoAll.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CardCaseNum = new Property(0, String.class, "cardCaseNum", false, "CARD_CASE_NUM");
        public final static Property CardRuleInfos = new Property(1, String.class, "cardRuleInfos", false, "CARD_RULE_INFOS");
    }


    public CardRuleInfoAllDao(DaoConfig config) {
        super(config);
    }
    
    public CardRuleInfoAllDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CARD_RULE_INFO_ALL\" (" + //
                "\"CARD_CASE_NUM\" TEXT," + // 0: cardCaseNum
                "\"CARD_RULE_INFOS\" TEXT);"); // 1: cardRuleInfos
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CARD_RULE_INFO_ALL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CardRuleInfoAll entity) {
        stmt.clearBindings();
 
        String cardCaseNum = entity.getCardCaseNum();
        if (cardCaseNum != null) {
            stmt.bindString(1, cardCaseNum);
        }
 
        String cardRuleInfos = entity.getCardRuleInfos();
        if (cardRuleInfos != null) {
            stmt.bindString(2, cardRuleInfos);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CardRuleInfoAll entity) {
        stmt.clearBindings();
 
        String cardCaseNum = entity.getCardCaseNum();
        if (cardCaseNum != null) {
            stmt.bindString(1, cardCaseNum);
        }
 
        String cardRuleInfos = entity.getCardRuleInfos();
        if (cardRuleInfos != null) {
            stmt.bindString(2, cardRuleInfos);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public CardRuleInfoAll readEntity(Cursor cursor, int offset) {
        CardRuleInfoAll entity = new CardRuleInfoAll( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // cardCaseNum
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // cardRuleInfos
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CardRuleInfoAll entity, int offset) {
        entity.setCardCaseNum(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCardRuleInfos(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(CardRuleInfoAll entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(CardRuleInfoAll entity) {
        return null;
    }

    @Override
    public boolean hasKey(CardRuleInfoAll entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
