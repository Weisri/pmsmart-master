package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/8/28.
 */
public class FaultDetailBean {
    private String tvInfo;
    private String tvTime;

    public FaultDetailBean(String tvInfo, String tvTime) {
        this.tvInfo = tvInfo;
        this.tvTime = tvTime;
    }

    public String getTvInfo() {
        return tvInfo;
    }

    public void setTvInfo(String tvInfo) {
        this.tvInfo = tvInfo;
    }

    public String getTvTime() {
        return tvTime;
    }

    public void setTvTime(String tvTime) {
        this.tvTime = tvTime;
    }
}
