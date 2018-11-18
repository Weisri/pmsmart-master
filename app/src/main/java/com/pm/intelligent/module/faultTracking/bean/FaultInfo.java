package com.pm.intelligent.module.faultTracking.bean;

import java.io.Serializable;

/**
 * Created by pumin on 2018/8/11.
 */

public class FaultInfo implements Serializable{
    private String date;
    private String time;
    private String stationName;
    private String faultType;
    private String faultStaues;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getFaultStaues() {
        return faultStaues;
    }

    public void setFaultStaues(String faultStaues) {
        this.faultStaues = faultStaues;
    }

}
