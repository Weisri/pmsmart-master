package com.pm.intelligent.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by WeiSir on 2018/8/28.
 */

@Entity
public class AlarmInfoEntity implements Serializable {
       private static final long serialVersionUID=451684614;
    /**
     * alarmId : 3
     * alarmType : 破坏报警
     * alarmName : 破坏故障
     * alarmTime : 1527669382000
     * updateTime : 1535188530000
     * iccid : 0150
     * alarmStatus : 2
     * shelterName : 宝安西乡
     * shelterDirection : null
     * projectName : 武汉项目
     */
   @Id
   private Long id;
   @Unique
    private int alarmId;
    private String alarmType;
    private String alarmName;
    private Date alarmTime;
    private Date updateTime;
    private String iccid;
    private int alarmStatus;
    private String shelterName;
    private String shelterDirection;
@Generated(hash = 1812076540)
public AlarmInfoEntity(Long id, int alarmId, String alarmType, String alarmName,
        Date alarmTime, Date updateTime, String iccid, int alarmStatus,
        String shelterName, String shelterDirection) {
    this.id = id;
    this.alarmId = alarmId;
    this.alarmType = alarmType;
    this.alarmName = alarmName;
    this.alarmTime = alarmTime;
    this.updateTime = updateTime;
    this.iccid = iccid;
    this.alarmStatus = alarmStatus;
    this.shelterName = shelterName;
    this.shelterDirection = shelterDirection;
}
@Generated(hash = 963594864)
public AlarmInfoEntity() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public int getAlarmId() {
    return this.alarmId;
}
public void setAlarmId(int alarmId) {
    this.alarmId = alarmId;
}
public String getAlarmType() {
    return this.alarmType;
}
public void setAlarmType(String alarmType) {
    this.alarmType = alarmType;
}
public String getAlarmName() {
    return this.alarmName;
}
public void setAlarmName(String alarmName) {
    this.alarmName = alarmName;
}
public Date getAlarmTime() {
    return this.alarmTime;
}
public void setAlarmTime(Date alarmTime) {
    this.alarmTime = alarmTime;
}
public Date getUpdateTime() {
    return this.updateTime;
}
public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
}
public String getIccid() {
    return this.iccid;
}
public void setIccid(String iccid) {
    this.iccid = iccid;
}
public int getAlarmStatus() {
    return this.alarmStatus;
}
public void setAlarmStatus(int alarmStatus) {
    this.alarmStatus = alarmStatus;
}
public String getShelterName() {
    return this.shelterName;
}
public void setShelterName(String shelterName) {
    this.shelterName = shelterName;
}
public String getShelterDirection() {
    return this.shelterDirection;
}
public void setShelterDirection(String shelterDirection) {
    this.shelterDirection = shelterDirection;
}
   
}
