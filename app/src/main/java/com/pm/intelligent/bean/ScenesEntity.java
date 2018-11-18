package com.pm.intelligent.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import lombok.NonNull;

/**
 * Created by WeiSir on 2018/8/29.
 */
@Entity
public class ScenesEntity {


    /**
     * projectId : 0
     * hardwareConfId : 69
     * hardwareId : 10
     * hardwareName : 视频监控
     * hardwareInfo : 视频监控开关调节
     * settingValue : {"startTime":"01:00", "endTime":"11:00"}
     */
    @Id
    private Long id;
    private int projectId;
    private int hardwareConfId;
    private String protocolId;
    private int hardwareId;
    @Unique
    private String hardwareName;
    private String hardwareInfo;
    private String settingValue;
    @Transient
    @NonNull
    private SettingValueEntity settingValueEntity;
    @Generated(hash = 710072270)
    public ScenesEntity(Long id, int projectId, int hardwareConfId,
            String protocolId, int hardwareId, String hardwareName,
            String hardwareInfo, String settingValue) {
        this.id = id;
        this.projectId = projectId;
        this.hardwareConfId = hardwareConfId;
        this.protocolId = protocolId;
        this.hardwareId = hardwareId;
        this.hardwareName = hardwareName;
        this.hardwareInfo = hardwareInfo;
        this.settingValue = settingValue;
    }
    @Generated(hash = 972199567)
    public ScenesEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getProjectId() {
        return this.projectId;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    public int getHardwareConfId() {
        return this.hardwareConfId;
    }
    public void setHardwareConfId(int hardwareConfId) {
        this.hardwareConfId = hardwareConfId;
    }
    public String getProtocolId() {
        return this.protocolId;
    }
    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }
    public int getHardwareId() {
        return this.hardwareId;
    }
    public void setHardwareId(int hardwareId) {
        this.hardwareId = hardwareId;
    }
    public String getHardwareName() {
        return this.hardwareName;
    }
    public void setHardwareName(String hardwareName) {
        this.hardwareName = hardwareName;
    }
    public String getHardwareInfo() {
        return this.hardwareInfo;
    }
    public void setHardwareInfo(String hardwareInfo) {
        this.hardwareInfo = hardwareInfo;
    }
    public String getSettingValue() {
        return this.settingValue;
    }
    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public SettingValueEntity getSettingValueEntity() {
        return settingValueEntity;
    }

    public void setSettingValueEntity(SettingValueEntity settingValueEntity) {
        this.settingValueEntity = settingValueEntity;
    }

    @Override
    public String toString() {
        return "ScenesEntity{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", hardwareConfId=" + hardwareConfId +
                ", protocolId='" + protocolId + '\'' +
                ", hardwareId=" + hardwareId +
                ", hardwareName='" + hardwareName + '\'' +
                ", hardwareInfo='" + hardwareInfo + '\'' +
                ", settingValue='" + settingValue + '\'' +
                ", settingValueEntity=" + settingValueEntity +
                '}';
    }
}
