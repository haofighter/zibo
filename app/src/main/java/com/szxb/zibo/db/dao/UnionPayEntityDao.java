package com.szxb.zibo.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.szxb.zibo.moudle.function.unionpay.entity.UnionPayEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "UNION_PAY_ENTITY".
*/
public class UnionPayEntityDao extends AbstractDao<UnionPayEntity, Long> {

    public static final String TABLENAME = "UNION_PAY_ENTITY";

    /**
     * Properties of entity UnionPayEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MchId = new Property(1, String.class, "mchId", false, "MCH_ID");
        public final static Property UnionPosSn = new Property(2, String.class, "unionPosSn", false, "UNION_POS_SN");
        public final static Property PosSn = new Property(3, String.class, "posSn", false, "POS_SN");
        public final static Property BusNo = new Property(4, String.class, "busNo", false, "BUS_NO");
        public final static Property TotalFee = new Property(5, int.class, "totalFee", false, "TOTAL_FEE");
        public final static Property PayFee = new Property(6, int.class, "payFee", false, "PAY_FEE");
        public final static Property ResCode = new Property(7, String.class, "resCode", false, "RES_CODE");
        public final static Property Time = new Property(8, String.class, "time", false, "TIME");
        public final static Property TradeSeq = new Property(9, int.class, "tradeSeq", false, "TRADE_SEQ");
        public final static Property MainCardNo = new Property(10, String.class, "mainCardNo", false, "MAIN_CARD_NO");
        public final static Property BatchNum = new Property(11, String.class, "batchNum", false, "BATCH_NUM");
        public final static Property Bus_line_name = new Property(12, String.class, "bus_line_name", false, "BUS_LINE_NAME");
        public final static Property Bus_line_no = new Property(13, String.class, "bus_line_no", false, "BUS_LINE_NO");
        public final static Property DriverNum = new Property(14, String.class, "driverNum", false, "DRIVER_NUM");
        public final static Property Unitno = new Property(15, String.class, "unitno", false, "UNITNO");
        public final static Property UpStatus = new Property(16, Integer.class, "upStatus", false, "UP_STATUS");
        public final static Property UniqueFlag = new Property(17, String.class, "uniqueFlag", false, "UNIQUE_FLAG");
        public final static Property Tlv55 = new Property(18, String.class, "tlv55", false, "TLV55");
        public final static Property SingleData = new Property(19, String.class, "singleData", false, "SINGLE_DATA");
        public final static Property Reserve_1 = new Property(20, String.class, "reserve_1", false, "RESERVE_1");
        public final static Property Reserve_2 = new Property(21, String.class, "reserve_2", false, "RESERVE_2");
        public final static Property Reserve_3 = new Property(22, String.class, "reserve_3", false, "RESERVE_3");
        public final static Property Reserve_4 = new Property(23, String.class, "reserve_4", false, "RESERVE_4");
        public final static Property PaySendDate = new Property(24, String.class, "paySendDate", false, "PAY_SEND_DATE");
        public final static Property IsODA = new Property(25, boolean.class, "isODA", false, "IS_ODA");
        public final static Property IsTrade = new Property(26, boolean.class, "isTrade", false, "IS_TRADE");
        public final static Property RepeatCount = new Property(27, int.class, "repeatCount", false, "REPEAT_COUNT");
        public final static Property RepeatTotleFree = new Property(28, int.class, "repeatTotleFree", false, "REPEAT_TOTLE_FREE");
        public final static Property SendPayTime = new Property(29, long.class, "sendPayTime", false, "SEND_PAY_TIME");
        public final static Property PayStatus = new Property(30, String.class, "payStatus", false, "PAY_STATUS");
        public final static Property CardNum = new Property(31, String.class, "cardNum", false, "CARD_NUM");
        public final static Property CardData = new Property(32, String.class, "cardData", false, "CARD_DATA");
        public final static Property AllDriver = new Property(33, String.class, "allDriver", false, "ALL_DRIVER");
        public final static Property TranType = new Property(34, String.class, "tranType", false, "TRAN_TYPE");
        public final static Property Reserve = new Property(35, String.class, "reserve", false, "RESERVE");
        public final static Property Biztype = new Property(36, String.class, "biztype", false, "BIZTYPE");
        public final static Property Acquirer = new Property(37, String.class, "acquirer", false, "ACQUIRER");
        public final static Property Conductorid = new Property(38, String.class, "conductorid", false, "CONDUCTORID");
        public final static Property Currency = new Property(39, String.class, "currency", false, "CURRENCY");
        public final static Property Transdata = new Property(40, String.class, "transdata", false, "TRANSDATA");
        public final static Property Creattime = new Property(41, Long.class, "creattime", false, "CREATTIME");
    }


    public UnionPayEntityDao(DaoConfig config) {
        super(config);
    }
    
    public UnionPayEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"UNION_PAY_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"MCH_ID\" TEXT," + // 1: mchId
                "\"UNION_POS_SN\" TEXT," + // 2: unionPosSn
                "\"POS_SN\" TEXT," + // 3: posSn
                "\"BUS_NO\" TEXT," + // 4: busNo
                "\"TOTAL_FEE\" INTEGER NOT NULL ," + // 5: totalFee
                "\"PAY_FEE\" INTEGER NOT NULL ," + // 6: payFee
                "\"RES_CODE\" TEXT," + // 7: resCode
                "\"TIME\" TEXT," + // 8: time
                "\"TRADE_SEQ\" INTEGER NOT NULL ," + // 9: tradeSeq
                "\"MAIN_CARD_NO\" TEXT," + // 10: mainCardNo
                "\"BATCH_NUM\" TEXT," + // 11: batchNum
                "\"BUS_LINE_NAME\" TEXT," + // 12: bus_line_name
                "\"BUS_LINE_NO\" TEXT," + // 13: bus_line_no
                "\"DRIVER_NUM\" TEXT," + // 14: driverNum
                "\"UNITNO\" TEXT," + // 15: unitno
                "\"UP_STATUS\" INTEGER," + // 16: upStatus
                "\"UNIQUE_FLAG\" TEXT," + // 17: uniqueFlag
                "\"TLV55\" TEXT," + // 18: tlv55
                "\"SINGLE_DATA\" TEXT," + // 19: singleData
                "\"RESERVE_1\" TEXT," + // 20: reserve_1
                "\"RESERVE_2\" TEXT," + // 21: reserve_2
                "\"RESERVE_3\" TEXT," + // 22: reserve_3
                "\"RESERVE_4\" TEXT," + // 23: reserve_4
                "\"PAY_SEND_DATE\" TEXT," + // 24: paySendDate
                "\"IS_ODA\" INTEGER NOT NULL ," + // 25: isODA
                "\"IS_TRADE\" INTEGER NOT NULL ," + // 26: isTrade
                "\"REPEAT_COUNT\" INTEGER NOT NULL ," + // 27: repeatCount
                "\"REPEAT_TOTLE_FREE\" INTEGER NOT NULL ," + // 28: repeatTotleFree
                "\"SEND_PAY_TIME\" INTEGER NOT NULL ," + // 29: sendPayTime
                "\"PAY_STATUS\" TEXT," + // 30: payStatus
                "\"CARD_NUM\" TEXT," + // 31: cardNum
                "\"CARD_DATA\" TEXT," + // 32: cardData
                "\"ALL_DRIVER\" TEXT," + // 33: allDriver
                "\"TRAN_TYPE\" TEXT," + // 34: tranType
                "\"RESERVE\" TEXT," + // 35: reserve
                "\"BIZTYPE\" TEXT," + // 36: biztype
                "\"ACQUIRER\" TEXT," + // 37: acquirer
                "\"CONDUCTORID\" TEXT," + // 38: conductorid
                "\"CURRENCY\" TEXT," + // 39: currency
                "\"TRANSDATA\" TEXT," + // 40: transdata
                "\"CREATTIME\" INTEGER);"); // 41: creattime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"UNION_PAY_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UnionPayEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String mchId = entity.getMchId();
        if (mchId != null) {
            stmt.bindString(2, mchId);
        }
 
        String unionPosSn = entity.getUnionPosSn();
        if (unionPosSn != null) {
            stmt.bindString(3, unionPosSn);
        }
 
        String posSn = entity.getPosSn();
        if (posSn != null) {
            stmt.bindString(4, posSn);
        }
 
        String busNo = entity.getBusNo();
        if (busNo != null) {
            stmt.bindString(5, busNo);
        }
        stmt.bindLong(6, entity.getTotalFee());
        stmt.bindLong(7, entity.getPayFee());
 
        String resCode = entity.getResCode();
        if (resCode != null) {
            stmt.bindString(8, resCode);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(9, time);
        }
        stmt.bindLong(10, entity.getTradeSeq());
 
        String mainCardNo = entity.getMainCardNo();
        if (mainCardNo != null) {
            stmt.bindString(11, mainCardNo);
        }
 
        String batchNum = entity.getBatchNum();
        if (batchNum != null) {
            stmt.bindString(12, batchNum);
        }
 
        String bus_line_name = entity.getBus_line_name();
        if (bus_line_name != null) {
            stmt.bindString(13, bus_line_name);
        }
 
        String bus_line_no = entity.getBus_line_no();
        if (bus_line_no != null) {
            stmt.bindString(14, bus_line_no);
        }
 
        String driverNum = entity.getDriverNum();
        if (driverNum != null) {
            stmt.bindString(15, driverNum);
        }
 
        String unitno = entity.getUnitno();
        if (unitno != null) {
            stmt.bindString(16, unitno);
        }
 
        Integer upStatus = entity.getUpStatus();
        if (upStatus != null) {
            stmt.bindLong(17, upStatus);
        }
 
        String uniqueFlag = entity.getUniqueFlag();
        if (uniqueFlag != null) {
            stmt.bindString(18, uniqueFlag);
        }
 
        String tlv55 = entity.getTlv55();
        if (tlv55 != null) {
            stmt.bindString(19, tlv55);
        }
 
        String singleData = entity.getSingleData();
        if (singleData != null) {
            stmt.bindString(20, singleData);
        }
 
        String reserve_1 = entity.getReserve_1();
        if (reserve_1 != null) {
            stmt.bindString(21, reserve_1);
        }
 
        String reserve_2 = entity.getReserve_2();
        if (reserve_2 != null) {
            stmt.bindString(22, reserve_2);
        }
 
        String reserve_3 = entity.getReserve_3();
        if (reserve_3 != null) {
            stmt.bindString(23, reserve_3);
        }
 
        String reserve_4 = entity.getReserve_4();
        if (reserve_4 != null) {
            stmt.bindString(24, reserve_4);
        }
 
        String paySendDate = entity.getPaySendDate();
        if (paySendDate != null) {
            stmt.bindString(25, paySendDate);
        }
        stmt.bindLong(26, entity.getIsODA() ? 1L: 0L);
        stmt.bindLong(27, entity.getIsTrade() ? 1L: 0L);
        stmt.bindLong(28, entity.getRepeatCount());
        stmt.bindLong(29, entity.getRepeatTotleFree());
        stmt.bindLong(30, entity.getSendPayTime());
 
        String payStatus = entity.getPayStatus();
        if (payStatus != null) {
            stmt.bindString(31, payStatus);
        }
 
        String cardNum = entity.getCardNum();
        if (cardNum != null) {
            stmt.bindString(32, cardNum);
        }
 
        String cardData = entity.getCardData();
        if (cardData != null) {
            stmt.bindString(33, cardData);
        }
 
        String allDriver = entity.getAllDriver();
        if (allDriver != null) {
            stmt.bindString(34, allDriver);
        }
 
        String tranType = entity.getTranType();
        if (tranType != null) {
            stmt.bindString(35, tranType);
        }
 
        String reserve = entity.getReserve();
        if (reserve != null) {
            stmt.bindString(36, reserve);
        }
 
        String biztype = entity.getBiztype();
        if (biztype != null) {
            stmt.bindString(37, biztype);
        }
 
        String acquirer = entity.getAcquirer();
        if (acquirer != null) {
            stmt.bindString(38, acquirer);
        }
 
        String conductorid = entity.getConductorid();
        if (conductorid != null) {
            stmt.bindString(39, conductorid);
        }
 
        String currency = entity.getCurrency();
        if (currency != null) {
            stmt.bindString(40, currency);
        }
 
        String transdata = entity.getTransdata();
        if (transdata != null) {
            stmt.bindString(41, transdata);
        }
 
        Long creattime = entity.getCreattime();
        if (creattime != null) {
            stmt.bindLong(42, creattime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UnionPayEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String mchId = entity.getMchId();
        if (mchId != null) {
            stmt.bindString(2, mchId);
        }
 
        String unionPosSn = entity.getUnionPosSn();
        if (unionPosSn != null) {
            stmt.bindString(3, unionPosSn);
        }
 
        String posSn = entity.getPosSn();
        if (posSn != null) {
            stmt.bindString(4, posSn);
        }
 
        String busNo = entity.getBusNo();
        if (busNo != null) {
            stmt.bindString(5, busNo);
        }
        stmt.bindLong(6, entity.getTotalFee());
        stmt.bindLong(7, entity.getPayFee());
 
        String resCode = entity.getResCode();
        if (resCode != null) {
            stmt.bindString(8, resCode);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(9, time);
        }
        stmt.bindLong(10, entity.getTradeSeq());
 
        String mainCardNo = entity.getMainCardNo();
        if (mainCardNo != null) {
            stmt.bindString(11, mainCardNo);
        }
 
        String batchNum = entity.getBatchNum();
        if (batchNum != null) {
            stmt.bindString(12, batchNum);
        }
 
        String bus_line_name = entity.getBus_line_name();
        if (bus_line_name != null) {
            stmt.bindString(13, bus_line_name);
        }
 
        String bus_line_no = entity.getBus_line_no();
        if (bus_line_no != null) {
            stmt.bindString(14, bus_line_no);
        }
 
        String driverNum = entity.getDriverNum();
        if (driverNum != null) {
            stmt.bindString(15, driverNum);
        }
 
        String unitno = entity.getUnitno();
        if (unitno != null) {
            stmt.bindString(16, unitno);
        }
 
        Integer upStatus = entity.getUpStatus();
        if (upStatus != null) {
            stmt.bindLong(17, upStatus);
        }
 
        String uniqueFlag = entity.getUniqueFlag();
        if (uniqueFlag != null) {
            stmt.bindString(18, uniqueFlag);
        }
 
        String tlv55 = entity.getTlv55();
        if (tlv55 != null) {
            stmt.bindString(19, tlv55);
        }
 
        String singleData = entity.getSingleData();
        if (singleData != null) {
            stmt.bindString(20, singleData);
        }
 
        String reserve_1 = entity.getReserve_1();
        if (reserve_1 != null) {
            stmt.bindString(21, reserve_1);
        }
 
        String reserve_2 = entity.getReserve_2();
        if (reserve_2 != null) {
            stmt.bindString(22, reserve_2);
        }
 
        String reserve_3 = entity.getReserve_3();
        if (reserve_3 != null) {
            stmt.bindString(23, reserve_3);
        }
 
        String reserve_4 = entity.getReserve_4();
        if (reserve_4 != null) {
            stmt.bindString(24, reserve_4);
        }
 
        String paySendDate = entity.getPaySendDate();
        if (paySendDate != null) {
            stmt.bindString(25, paySendDate);
        }
        stmt.bindLong(26, entity.getIsODA() ? 1L: 0L);
        stmt.bindLong(27, entity.getIsTrade() ? 1L: 0L);
        stmt.bindLong(28, entity.getRepeatCount());
        stmt.bindLong(29, entity.getRepeatTotleFree());
        stmt.bindLong(30, entity.getSendPayTime());
 
        String payStatus = entity.getPayStatus();
        if (payStatus != null) {
            stmt.bindString(31, payStatus);
        }
 
        String cardNum = entity.getCardNum();
        if (cardNum != null) {
            stmt.bindString(32, cardNum);
        }
 
        String cardData = entity.getCardData();
        if (cardData != null) {
            stmt.bindString(33, cardData);
        }
 
        String allDriver = entity.getAllDriver();
        if (allDriver != null) {
            stmt.bindString(34, allDriver);
        }
 
        String tranType = entity.getTranType();
        if (tranType != null) {
            stmt.bindString(35, tranType);
        }
 
        String reserve = entity.getReserve();
        if (reserve != null) {
            stmt.bindString(36, reserve);
        }
 
        String biztype = entity.getBiztype();
        if (biztype != null) {
            stmt.bindString(37, biztype);
        }
 
        String acquirer = entity.getAcquirer();
        if (acquirer != null) {
            stmt.bindString(38, acquirer);
        }
 
        String conductorid = entity.getConductorid();
        if (conductorid != null) {
            stmt.bindString(39, conductorid);
        }
 
        String currency = entity.getCurrency();
        if (currency != null) {
            stmt.bindString(40, currency);
        }
 
        String transdata = entity.getTransdata();
        if (transdata != null) {
            stmt.bindString(41, transdata);
        }
 
        Long creattime = entity.getCreattime();
        if (creattime != null) {
            stmt.bindLong(42, creattime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UnionPayEntity readEntity(Cursor cursor, int offset) {
        UnionPayEntity entity = new UnionPayEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // mchId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // unionPosSn
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // posSn
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // busNo
            cursor.getInt(offset + 5), // totalFee
            cursor.getInt(offset + 6), // payFee
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // resCode
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // time
            cursor.getInt(offset + 9), // tradeSeq
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // mainCardNo
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // batchNum
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // bus_line_name
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // bus_line_no
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // driverNum
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // unitno
            cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16), // upStatus
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // uniqueFlag
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // tlv55
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // singleData
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // reserve_1
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // reserve_2
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // reserve_3
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // reserve_4
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // paySendDate
            cursor.getShort(offset + 25) != 0, // isODA
            cursor.getShort(offset + 26) != 0, // isTrade
            cursor.getInt(offset + 27), // repeatCount
            cursor.getInt(offset + 28), // repeatTotleFree
            cursor.getLong(offset + 29), // sendPayTime
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // payStatus
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // cardNum
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // cardData
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // allDriver
            cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34), // tranType
            cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35), // reserve
            cursor.isNull(offset + 36) ? null : cursor.getString(offset + 36), // biztype
            cursor.isNull(offset + 37) ? null : cursor.getString(offset + 37), // acquirer
            cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38), // conductorid
            cursor.isNull(offset + 39) ? null : cursor.getString(offset + 39), // currency
            cursor.isNull(offset + 40) ? null : cursor.getString(offset + 40), // transdata
            cursor.isNull(offset + 41) ? null : cursor.getLong(offset + 41) // creattime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UnionPayEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMchId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUnionPosSn(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPosSn(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBusNo(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTotalFee(cursor.getInt(offset + 5));
        entity.setPayFee(cursor.getInt(offset + 6));
        entity.setResCode(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTime(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTradeSeq(cursor.getInt(offset + 9));
        entity.setMainCardNo(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setBatchNum(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setBus_line_name(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setBus_line_no(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setDriverNum(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setUnitno(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setUpStatus(cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16));
        entity.setUniqueFlag(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setTlv55(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setSingleData(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setReserve_1(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setReserve_2(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setReserve_3(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setReserve_4(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setPaySendDate(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setIsODA(cursor.getShort(offset + 25) != 0);
        entity.setIsTrade(cursor.getShort(offset + 26) != 0);
        entity.setRepeatCount(cursor.getInt(offset + 27));
        entity.setRepeatTotleFree(cursor.getInt(offset + 28));
        entity.setSendPayTime(cursor.getLong(offset + 29));
        entity.setPayStatus(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setCardNum(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setCardData(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setAllDriver(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setTranType(cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
        entity.setReserve(cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35));
        entity.setBiztype(cursor.isNull(offset + 36) ? null : cursor.getString(offset + 36));
        entity.setAcquirer(cursor.isNull(offset + 37) ? null : cursor.getString(offset + 37));
        entity.setConductorid(cursor.isNull(offset + 38) ? null : cursor.getString(offset + 38));
        entity.setCurrency(cursor.isNull(offset + 39) ? null : cursor.getString(offset + 39));
        entity.setTransdata(cursor.isNull(offset + 40) ? null : cursor.getString(offset + 40));
        entity.setCreattime(cursor.isNull(offset + 41) ? null : cursor.getLong(offset + 41));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UnionPayEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UnionPayEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UnionPayEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
