package com.szxb.zibo.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.szxb.zibo.config.zibo.line.CardPlan;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CARD_PLAN".
*/
public class CardPlanDao extends AbstractDao<CardPlan, Void> {

    public static final String TABLENAME = "CARD_PLAN";

    /**
     * Properties of entity CardPlan.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CardCaseNum = new Property(0, String.class, "cardCaseNum", false, "CARD_CASE_NUM");
        public final static Property CardType = new Property(1, String.class, "cardType", false, "CARD_TYPE");
        public final static Property ChildCardType = new Property(2, String.class, "childCardType", false, "CHILD_CARD_TYPE");
        public final static Property ParentCardType = new Property(3, String.class, "parentCardType", false, "PARENT_CARD_TYPE");
        public final static Property CardPayRuleNum = new Property(4, String.class, "cardPayRuleNum", false, "CARD_PAY_RULE_NUM");
        public final static Property Rev = new Property(5, String.class, "rev", false, "REV");
        public final static Property CardName = new Property(6, String.class, "cardName", false, "CARD_NAME");
        public final static Property NeedCheckBlack = new Property(7, String.class, "needCheckBlack", false, "NEED_CHECK_BLACK");
        public final static Property NeedCheckStartTime = new Property(8, String.class, "needCheckStartTime", false, "NEED_CHECK_START_TIME");
        public final static Property NeedCheckEndTime = new Property(9, String.class, "needCheckEndTime", false, "NEED_CHECK_END_TIME");
        public final static Property BalanceType = new Property(10, String.class, "balanceType", false, "BALANCE_TYPE");
        public final static Property RechargeTag = new Property(11, String.class, "rechargeTag", false, "RECHARGE_TAG");
        public final static Property Overdraw = new Property(12, String.class, "overdraw", false, "OVERDRAW");
        public final static Property VoiceType = new Property(13, String.class, "voiceType", false, "VOICE_TYPE");
        public final static Property RetrueMoney = new Property(14, String.class, "retrueMoney", false, "RETRUE_MONEY");
        public final static Property RetrueMoneyTag = new Property(15, String.class, "retrueMoneyTag", false, "RETRUE_MONEY_TAG");
        public final static Property RetrueMonthTag = new Property(16, String.class, "retrueMonthTag", false, "RETRUE_MONTH_TAG");
        public final static Property Recover = new Property(17, String.class, "recover", false, "RECOVER");
        public final static Property ServiceCharge = new Property(18, String.class, "serviceCharge", false, "SERVICE_CHARGE");
        public final static Property RentMoney = new Property(19, String.class, "rentMoney", false, "RENT_MONEY");
        public final static Property RentType = new Property(20, String.class, "rentType", false, "RENT_TYPE");
        public final static Property Registered = new Property(21, String.class, "registered", false, "REGISTERED");
        public final static Property GiveWallet = new Property(22, String.class, "giveWallet", false, "GIVE_WALLET");
        public final static Property MinMoney = new Property(23, String.class, "minMoney", false, "MIN_MONEY");
        public final static Property GiveInterval = new Property(24, String.class, "giveInterval", false, "GIVE_INTERVAL");
        public final static Property UseInterval = new Property(25, String.class, "useInterval", false, "USE_INTERVAL");
    }


    public CardPlanDao(DaoConfig config) {
        super(config);
    }
    
    public CardPlanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CARD_PLAN\" (" + //
                "\"CARD_CASE_NUM\" TEXT," + // 0: cardCaseNum
                "\"CARD_TYPE\" TEXT," + // 1: cardType
                "\"CHILD_CARD_TYPE\" TEXT," + // 2: childCardType
                "\"PARENT_CARD_TYPE\" TEXT," + // 3: parentCardType
                "\"CARD_PAY_RULE_NUM\" TEXT," + // 4: cardPayRuleNum
                "\"REV\" TEXT," + // 5: rev
                "\"CARD_NAME\" TEXT," + // 6: cardName
                "\"NEED_CHECK_BLACK\" TEXT," + // 7: needCheckBlack
                "\"NEED_CHECK_START_TIME\" TEXT," + // 8: needCheckStartTime
                "\"NEED_CHECK_END_TIME\" TEXT," + // 9: needCheckEndTime
                "\"BALANCE_TYPE\" TEXT," + // 10: balanceType
                "\"RECHARGE_TAG\" TEXT," + // 11: rechargeTag
                "\"OVERDRAW\" TEXT," + // 12: overdraw
                "\"VOICE_TYPE\" TEXT," + // 13: voiceType
                "\"RETRUE_MONEY\" TEXT," + // 14: retrueMoney
                "\"RETRUE_MONEY_TAG\" TEXT," + // 15: retrueMoneyTag
                "\"RETRUE_MONTH_TAG\" TEXT," + // 16: retrueMonthTag
                "\"RECOVER\" TEXT," + // 17: recover
                "\"SERVICE_CHARGE\" TEXT," + // 18: serviceCharge
                "\"RENT_MONEY\" TEXT," + // 19: rentMoney
                "\"RENT_TYPE\" TEXT," + // 20: rentType
                "\"REGISTERED\" TEXT," + // 21: registered
                "\"GIVE_WALLET\" TEXT," + // 22: giveWallet
                "\"MIN_MONEY\" TEXT," + // 23: minMoney
                "\"GIVE_INTERVAL\" TEXT," + // 24: giveInterval
                "\"USE_INTERVAL\" TEXT);"); // 25: useInterval
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CARD_PLAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CardPlan entity) {
        stmt.clearBindings();
 
        String cardCaseNum = entity.getCardCaseNum();
        if (cardCaseNum != null) {
            stmt.bindString(1, cardCaseNum);
        }
 
        String cardType = entity.getCardType();
        if (cardType != null) {
            stmt.bindString(2, cardType);
        }
 
        String childCardType = entity.getChildCardType();
        if (childCardType != null) {
            stmt.bindString(3, childCardType);
        }
 
        String parentCardType = entity.getParentCardType();
        if (parentCardType != null) {
            stmt.bindString(4, parentCardType);
        }
 
        String cardPayRuleNum = entity.getCardPayRuleNum();
        if (cardPayRuleNum != null) {
            stmt.bindString(5, cardPayRuleNum);
        }
 
        String rev = entity.getRev();
        if (rev != null) {
            stmt.bindString(6, rev);
        }
 
        String cardName = entity.getCardName();
        if (cardName != null) {
            stmt.bindString(7, cardName);
        }
 
        String needCheckBlack = entity.getNeedCheckBlack();
        if (needCheckBlack != null) {
            stmt.bindString(8, needCheckBlack);
        }
 
        String needCheckStartTime = entity.getNeedCheckStartTime();
        if (needCheckStartTime != null) {
            stmt.bindString(9, needCheckStartTime);
        }
 
        String needCheckEndTime = entity.getNeedCheckEndTime();
        if (needCheckEndTime != null) {
            stmt.bindString(10, needCheckEndTime);
        }
 
        String balanceType = entity.getBalanceType();
        if (balanceType != null) {
            stmt.bindString(11, balanceType);
        }
 
        String rechargeTag = entity.getRechargeTag();
        if (rechargeTag != null) {
            stmt.bindString(12, rechargeTag);
        }
 
        String overdraw = entity.getOverdraw();
        if (overdraw != null) {
            stmt.bindString(13, overdraw);
        }
 
        String voiceType = entity.getVoiceType();
        if (voiceType != null) {
            stmt.bindString(14, voiceType);
        }
 
        String retrueMoney = entity.getRetrueMoney();
        if (retrueMoney != null) {
            stmt.bindString(15, retrueMoney);
        }
 
        String retrueMoneyTag = entity.getRetrueMoneyTag();
        if (retrueMoneyTag != null) {
            stmt.bindString(16, retrueMoneyTag);
        }
 
        String retrueMonthTag = entity.getRetrueMonthTag();
        if (retrueMonthTag != null) {
            stmt.bindString(17, retrueMonthTag);
        }
 
        String recover = entity.getRecover();
        if (recover != null) {
            stmt.bindString(18, recover);
        }
 
        String serviceCharge = entity.getServiceCharge();
        if (serviceCharge != null) {
            stmt.bindString(19, serviceCharge);
        }
 
        String rentMoney = entity.getRentMoney();
        if (rentMoney != null) {
            stmt.bindString(20, rentMoney);
        }
 
        String rentType = entity.getRentType();
        if (rentType != null) {
            stmt.bindString(21, rentType);
        }
 
        String registered = entity.getRegistered();
        if (registered != null) {
            stmt.bindString(22, registered);
        }
 
        String giveWallet = entity.getGiveWallet();
        if (giveWallet != null) {
            stmt.bindString(23, giveWallet);
        }
 
        String minMoney = entity.getMinMoney();
        if (minMoney != null) {
            stmt.bindString(24, minMoney);
        }
 
        String giveInterval = entity.getGiveInterval();
        if (giveInterval != null) {
            stmt.bindString(25, giveInterval);
        }
 
        String useInterval = entity.getUseInterval();
        if (useInterval != null) {
            stmt.bindString(26, useInterval);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CardPlan entity) {
        stmt.clearBindings();
 
        String cardCaseNum = entity.getCardCaseNum();
        if (cardCaseNum != null) {
            stmt.bindString(1, cardCaseNum);
        }
 
        String cardType = entity.getCardType();
        if (cardType != null) {
            stmt.bindString(2, cardType);
        }
 
        String childCardType = entity.getChildCardType();
        if (childCardType != null) {
            stmt.bindString(3, childCardType);
        }
 
        String parentCardType = entity.getParentCardType();
        if (parentCardType != null) {
            stmt.bindString(4, parentCardType);
        }
 
        String cardPayRuleNum = entity.getCardPayRuleNum();
        if (cardPayRuleNum != null) {
            stmt.bindString(5, cardPayRuleNum);
        }
 
        String rev = entity.getRev();
        if (rev != null) {
            stmt.bindString(6, rev);
        }
 
        String cardName = entity.getCardName();
        if (cardName != null) {
            stmt.bindString(7, cardName);
        }
 
        String needCheckBlack = entity.getNeedCheckBlack();
        if (needCheckBlack != null) {
            stmt.bindString(8, needCheckBlack);
        }
 
        String needCheckStartTime = entity.getNeedCheckStartTime();
        if (needCheckStartTime != null) {
            stmt.bindString(9, needCheckStartTime);
        }
 
        String needCheckEndTime = entity.getNeedCheckEndTime();
        if (needCheckEndTime != null) {
            stmt.bindString(10, needCheckEndTime);
        }
 
        String balanceType = entity.getBalanceType();
        if (balanceType != null) {
            stmt.bindString(11, balanceType);
        }
 
        String rechargeTag = entity.getRechargeTag();
        if (rechargeTag != null) {
            stmt.bindString(12, rechargeTag);
        }
 
        String overdraw = entity.getOverdraw();
        if (overdraw != null) {
            stmt.bindString(13, overdraw);
        }
 
        String voiceType = entity.getVoiceType();
        if (voiceType != null) {
            stmt.bindString(14, voiceType);
        }
 
        String retrueMoney = entity.getRetrueMoney();
        if (retrueMoney != null) {
            stmt.bindString(15, retrueMoney);
        }
 
        String retrueMoneyTag = entity.getRetrueMoneyTag();
        if (retrueMoneyTag != null) {
            stmt.bindString(16, retrueMoneyTag);
        }
 
        String retrueMonthTag = entity.getRetrueMonthTag();
        if (retrueMonthTag != null) {
            stmt.bindString(17, retrueMonthTag);
        }
 
        String recover = entity.getRecover();
        if (recover != null) {
            stmt.bindString(18, recover);
        }
 
        String serviceCharge = entity.getServiceCharge();
        if (serviceCharge != null) {
            stmt.bindString(19, serviceCharge);
        }
 
        String rentMoney = entity.getRentMoney();
        if (rentMoney != null) {
            stmt.bindString(20, rentMoney);
        }
 
        String rentType = entity.getRentType();
        if (rentType != null) {
            stmt.bindString(21, rentType);
        }
 
        String registered = entity.getRegistered();
        if (registered != null) {
            stmt.bindString(22, registered);
        }
 
        String giveWallet = entity.getGiveWallet();
        if (giveWallet != null) {
            stmt.bindString(23, giveWallet);
        }
 
        String minMoney = entity.getMinMoney();
        if (minMoney != null) {
            stmt.bindString(24, minMoney);
        }
 
        String giveInterval = entity.getGiveInterval();
        if (giveInterval != null) {
            stmt.bindString(25, giveInterval);
        }
 
        String useInterval = entity.getUseInterval();
        if (useInterval != null) {
            stmt.bindString(26, useInterval);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public CardPlan readEntity(Cursor cursor, int offset) {
        CardPlan entity = new CardPlan( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // cardCaseNum
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // cardType
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // childCardType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // parentCardType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // cardPayRuleNum
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // rev
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // cardName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // needCheckBlack
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // needCheckStartTime
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // needCheckEndTime
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // balanceType
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // rechargeTag
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // overdraw
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // voiceType
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // retrueMoney
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // retrueMoneyTag
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // retrueMonthTag
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // recover
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // serviceCharge
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // rentMoney
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // rentType
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // registered
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // giveWallet
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // minMoney
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // giveInterval
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25) // useInterval
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CardPlan entity, int offset) {
        entity.setCardCaseNum(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCardType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setChildCardType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setParentCardType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCardPayRuleNum(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRev(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCardName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setNeedCheckBlack(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setNeedCheckStartTime(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setNeedCheckEndTime(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setBalanceType(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRechargeTag(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setOverdraw(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setVoiceType(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setRetrueMoney(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setRetrueMoneyTag(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setRetrueMonthTag(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setRecover(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setServiceCharge(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setRentMoney(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setRentType(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setRegistered(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setGiveWallet(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setMinMoney(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setGiveInterval(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setUseInterval(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(CardPlan entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(CardPlan entity) {
        return null;
    }

    @Override
    public boolean hasKey(CardPlan entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
