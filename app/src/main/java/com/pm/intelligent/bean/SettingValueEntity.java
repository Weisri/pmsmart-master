package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/10/15.
 */
public class SettingValueEntity {


    /**
     * startTime : 01:00
     * endTime : 11:00
     */

    private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SettingValueEntity{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
