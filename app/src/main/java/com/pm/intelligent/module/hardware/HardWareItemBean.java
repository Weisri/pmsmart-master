package com.pm.intelligent.module.hardware;

/**
 * Created by WeiSir on 2018/7/12.
 */
public class HardWareItemBean {


    /**
     * hardwareInfo : 电流
     * hardwareConfId : 52
     * paramValue : 1
     * paramState : 1
     */

    private String hardwareInfo;
    private int hardwareConfId;
    private String paramValue;
    private String paramState;

    public String getHardwareInfo() {
        return hardwareInfo;
    }

    public void setHardwareInfo(String hardwareInfo) {
        this.hardwareInfo = hardwareInfo;
    }

    public int getHardwareConfId() {
        return hardwareConfId;
    }

    public void setHardwareConfId(int hardwareConfId) {
        this.hardwareConfId = hardwareConfId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamState() {
        return paramState;
    }

    public void setParamState(String paramState) {
        this.paramState = paramState;
    }

    @Override
    public String toString() {
        return "HardWareItemBean{" +
                "hardwareInfo='" + hardwareInfo + '\'' +
                ", hardwareConfId=" + hardwareConfId +
                ", paramValue='" + paramValue + '\'' +
                ", paramState='" + paramState + '\'' +
                '}';
    }
}
