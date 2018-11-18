package com.pm.intelligent.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by WeiSir on 2018/8/29.
 */
@Entity
public class AdjustsEntity {


    /**
     * paramValue : 52
     * protocolId : 15
     * hardwareConfId : 17
     * hardwareInfo : LED双色屏亮度
     */
    @Id
    private Long id;
    private String paramValue;
    private String protocolId;
    @Unique
    private int hardwareConfId;
    private String hardwareInfo;

    @Generated(hash = 430535372)
    public AdjustsEntity(Long id, String paramValue, String protocolId,
            int hardwareConfId, String hardwareInfo) {
        this.id = id;
        this.paramValue = paramValue;
        this.protocolId = protocolId;
        this.hardwareConfId = hardwareConfId;
        this.hardwareInfo = hardwareInfo;
    }

    @Generated(hash = 885367675)
    public AdjustsEntity() {
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public int getHardwareConfId() {
        return hardwareConfId;
    }

    public void setHardwareConfId(int hardwareConfId) {
        this.hardwareConfId = hardwareConfId;
    }

    public String getHardwareInfo() {
        return hardwareInfo;
    }

    public void setHardwareInfo(String hardwareInfo) {
        this.hardwareInfo = hardwareInfo;
    }

    @Override
    public String toString() {
        return "AdjustsEntity{" +
                "paramValue='" + paramValue + '\'' +
                ", protocolId='" + protocolId + '\'' +
                ", hardwareConfId=" + hardwareConfId +
                ", hardwareInfo='" + hardwareInfo + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
