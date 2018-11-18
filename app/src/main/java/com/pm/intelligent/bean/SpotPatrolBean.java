package com.pm.intelligent.bean;

import lombok.ToString;

/**
 * Created by WeiSir on 2018/9/13.
 */
@ToString

public class SpotPatrolBean {
    private String hardwareType;//硬件名
    private int  checkStatus; // 检查的状态
    private String shelterName; //所属站牌
    private String iccid; //站牌id

    public String getHardwareType() {
        return hardwareType;
    }

    public void setHardwareType(String hardwareType) {
        this.hardwareType = hardwareType;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
}
