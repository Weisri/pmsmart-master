package com.pm.intelligent.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.pm.intelligent.bean.AlarmInfoEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ALARM_INFO_ENTITY".
*/
public class AlarmInfoEntityDao extends AbstractDao<AlarmInfoEntity, Long> {

    public static final String TABLENAME = "ALARM_INFO_ENTITY";

    /**
     * Properties of entity AlarmInfoEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AlarmId = new Property(1, int.class, "alarmId", false, "ALARM_ID");
        public final static Property AlarmType = new Property(2, String.class, "alarmType", false, "ALARM_TYPE");
        public final static Property AlarmName = new Property(3, String.class, "alarmName", false, "ALARM_NAME");
        public final static Property AlarmTime = new Property(4, java.util.Date.class, "alarmTime", false, "ALARM_TIME");
        public final static Property UpdateTime = new Property(5, java.util.Date.class, "updateTime", false, "UPDATE_TIME");
        public final static Property Iccid = new Property(6, String.class, "iccid", false, "ICCID");
        public final static Property AlarmStatus = new Property(7, int.class, "alarmStatus", false, "ALARM_STATUS");
        public final static Property ShelterName = new Property(8, String.class, "shelterName", false, "SHELTER_NAME");
        public final static Property ShelterDirection = new Property(9, String.class, "shelterDirection", false, "SHELTER_DIRECTION");
    }


    public AlarmInfoEntityDao(DaoConfig config) {
        super(config);
    }
    
    public AlarmInfoEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ALARM_INFO_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ALARM_ID\" INTEGER NOT NULL UNIQUE ," + // 1: alarmId
                "\"ALARM_TYPE\" TEXT," + // 2: alarmType
                "\"ALARM_NAME\" TEXT," + // 3: alarmName
                "\"ALARM_TIME\" INTEGER," + // 4: alarmTime
                "\"UPDATE_TIME\" INTEGER," + // 5: updateTime
                "\"ICCID\" TEXT," + // 6: iccid
                "\"ALARM_STATUS\" INTEGER NOT NULL ," + // 7: alarmStatus
                "\"SHELTER_NAME\" TEXT," + // 8: shelterName
                "\"SHELTER_DIRECTION\" TEXT);"); // 9: shelterDirection
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ALARM_INFO_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AlarmInfoEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getAlarmId());
 
        String alarmType = entity.getAlarmType();
        if (alarmType != null) {
            stmt.bindString(3, alarmType);
        }
 
        String alarmName = entity.getAlarmName();
        if (alarmName != null) {
            stmt.bindString(4, alarmName);
        }
 
        java.util.Date alarmTime = entity.getAlarmTime();
        if (alarmTime != null) {
            stmt.bindLong(5, alarmTime.getTime());
        }
 
        java.util.Date updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(6, updateTime.getTime());
        }
 
        String iccid = entity.getIccid();
        if (iccid != null) {
            stmt.bindString(7, iccid);
        }
        stmt.bindLong(8, entity.getAlarmStatus());
 
        String shelterName = entity.getShelterName();
        if (shelterName != null) {
            stmt.bindString(9, shelterName);
        }
 
        String shelterDirection = entity.getShelterDirection();
        if (shelterDirection != null) {
            stmt.bindString(10, shelterDirection);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AlarmInfoEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getAlarmId());
 
        String alarmType = entity.getAlarmType();
        if (alarmType != null) {
            stmt.bindString(3, alarmType);
        }
 
        String alarmName = entity.getAlarmName();
        if (alarmName != null) {
            stmt.bindString(4, alarmName);
        }
 
        java.util.Date alarmTime = entity.getAlarmTime();
        if (alarmTime != null) {
            stmt.bindLong(5, alarmTime.getTime());
        }
 
        java.util.Date updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(6, updateTime.getTime());
        }
 
        String iccid = entity.getIccid();
        if (iccid != null) {
            stmt.bindString(7, iccid);
        }
        stmt.bindLong(8, entity.getAlarmStatus());
 
        String shelterName = entity.getShelterName();
        if (shelterName != null) {
            stmt.bindString(9, shelterName);
        }
 
        String shelterDirection = entity.getShelterDirection();
        if (shelterDirection != null) {
            stmt.bindString(10, shelterDirection);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AlarmInfoEntity readEntity(Cursor cursor, int offset) {
        AlarmInfoEntity entity = new AlarmInfoEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // alarmId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // alarmType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // alarmName
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // alarmTime
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // updateTime
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // iccid
            cursor.getInt(offset + 7), // alarmStatus
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // shelterName
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // shelterDirection
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AlarmInfoEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAlarmId(cursor.getInt(offset + 1));
        entity.setAlarmType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAlarmName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAlarmTime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setUpdateTime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setIccid(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAlarmStatus(cursor.getInt(offset + 7));
        entity.setShelterName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setShelterDirection(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AlarmInfoEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AlarmInfoEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AlarmInfoEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
