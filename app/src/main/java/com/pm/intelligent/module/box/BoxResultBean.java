package com.pm.intelligent.module.box;

import java.util.Date;

/**
 * Created by WeiSir on 2018/9/25.
 */

public class BoxResultBean {


    /**
     * memoryPer : 72
     * netSignal : 3
     * boxState : 0
     * reportNumber : 89757
     * boxName : 盒子卫士
     * startTime : 1539683514000
     * endTime : 1539687349000
     */
    private int boxStatusId;
    private int memoryPer;
    private int netSignal;
    private int boxState;
    private String reportNumber;
    private String boxName;
    private int currentState;
    private Date startTime;
    private Date endTime;

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public int getBoxStatusId() {
        return boxStatusId;
    }

    public void setBoxStatusId(int boxStatusId) {
        this.boxStatusId = boxStatusId;
    }

    public int getMemoryPer() {
        return memoryPer;
    }

    public void setMemoryPer(int memoryPer) {
        this.memoryPer = memoryPer;
    }

    public int getNetSignal() {
        return netSignal;
    }

    public void setNetSignal(int netSignal) {
        this.netSignal = netSignal;
    }

    public int getBoxState() {
        return boxState;
    }

    public void setBoxState(int boxState) {
        this.boxState = boxState;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "BoxResultBean{" +
                "memoryPer=" + memoryPer +
                ", netSignal=" + netSignal +
                ", boxState=" + boxState +
                ", reportNumber='" + reportNumber + '\'' +
                ", boxName='" + boxName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
