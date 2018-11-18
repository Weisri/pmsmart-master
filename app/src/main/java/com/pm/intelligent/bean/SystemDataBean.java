package com.pm.intelligent.bean;

/**
 * Created by 78122 on 2018/8/20.
 */

public class SystemDataBean {

    /**
     * systemId : 1
     * systemName : 报站系统
     * systemStatus : 1
     */

    private int systemId;
    private String systemName;
    private int systemStatus;

    public SystemDataBean(String systemName, int systemStatus) {
        this.systemName = systemName;
        this.systemStatus = systemStatus;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public int getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(int systemStatus) {
        this.systemStatus = systemStatus;
    }

    @Override
    public String toString() {
        return "SystemDataBean{" +
                "systemId=" + systemId +
                ", systemName='" + systemName + '\'' +
                ", systemStatus=" + systemStatus +
                '}';
    }
}
