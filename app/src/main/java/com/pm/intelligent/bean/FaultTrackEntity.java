package com.pm.intelligent.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by WeiSir on 2018/8/27.
 */

@Entity
public class FaultTrackEntity implements Serializable {
    public static final long serialVersionUID = 123456789;

    /**
     * troubleId : 10
     * troubleName : LED屏故障
     * troubleStatus : 2
     * iccid : null
     * troubleTime : 1535186862000
     * updateTime : null
     * shelterName : 欣园1
     * projectName : 龙华项目
     */
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private int troubleId;
    private String troubleName;
    private int troubleStatus;
    private String iccid;
    private Date troubleTime;
    private Date updateTime;
    private String shelterName;
    private String projectName;
    @Generated(hash = 1174640887)
    public FaultTrackEntity(Long id, int troubleId, String troubleName,
            int troubleStatus, String iccid, Date troubleTime, Date updateTime,
            String shelterName, String projectName) {
        this.id = id;
        this.troubleId = troubleId;
        this.troubleName = troubleName;
        this.troubleStatus = troubleStatus;
        this.iccid = iccid;
        this.troubleTime = troubleTime;
        this.updateTime = updateTime;
        this.shelterName = shelterName;
        this.projectName = projectName;
    }
    @Generated(hash = 1098307932)
    public FaultTrackEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getTroubleId() {
        return this.troubleId;
    }
    public void setTroubleId(int troubleId) {
        this.troubleId = troubleId;
    }
    public String getTroubleName() {
        return this.troubleName;
    }
    public void setTroubleName(String troubleName) {
        this.troubleName = troubleName;
    }
    public int getTroubleStatus() {
        return this.troubleStatus;
    }
    public void setTroubleStatus(int troubleStatus) {
        this.troubleStatus = troubleStatus;
    }
    public String getIccid() {
        return this.iccid;
    }
    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
    public Date getTroubleTime() {
        return this.troubleTime;
    }
    public void setTroubleTime(Date troubleTime) {
        this.troubleTime = troubleTime;
    }
    public Date getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getShelterName() {
        return this.shelterName;
    }
    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }
    public String getProjectName() {
        return this.projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "FaultTrackEntity{" +
                "id=" + id +
                ", troubleId=" + troubleId +
                ", troubleName='" + troubleName + '\'' +
                ", troubleStatus=" + troubleStatus +
                ", iccid='" + iccid + '\'' +
                ", troubleTime=" + troubleTime +
                ", updateTime=" + updateTime +
                ", shelterName='" + shelterName + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
