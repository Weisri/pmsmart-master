package com.pm.intelligent.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by WeiSir on 2018/8/29.
 */
@Entity
public class SwitchsEntity {


    /**
     * hardwareId : 17
     * protocolId : 20
     * switchName : 智能电表开关
     * paramState : 0
     * iccid : 10001
     */
    @Id
    private Long id;
    @Unique
    private int hardwareId;
    private String protocolId;
    private String switchName;
    private String paramState;
    private String iccid;

    @Generated(hash = 1927666076)
    public SwitchsEntity(Long id, int hardwareId, String protocolId,
            String switchName, String paramState, String iccid) {
        this.id = id;
        this.hardwareId = hardwareId;
        this.protocolId = protocolId;
        this.switchName = switchName;
        this.paramState = paramState;
        this.iccid = iccid;
    }

    @Generated(hash = 1047856748)
    public SwitchsEntity() {
    }

    public int getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(int hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getParamState() {
        return paramState;
    }

    public void setParamState(String paramState) {
        this.paramState = paramState;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SwitchsEntity{" +
                "hardwareId=" + hardwareId +
                ", protocolId='" + protocolId + '\'' +
                ", switchName='" + switchName + '\'' +
                ", paramState='" + paramState + '\'' +
                ", iccid='" + iccid + '\'' +
                '}';
    }
}
