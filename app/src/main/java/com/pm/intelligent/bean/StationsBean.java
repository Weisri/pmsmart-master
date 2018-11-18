package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/6/25.
 */

public class StationsBean {
    private String stationName;
    private String stationUp;
    private String stationDwon;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationUp() {
        return stationUp;
    }

    public void setStationUp(String stationUp) {
        this.stationUp = stationUp;
    }

    public String getStationDwon() {
        return stationDwon;
    }

    public void setStationDwon(String stationDwon) {
        this.stationDwon = stationDwon;
    }

    @Override
    public String toString() {
        return "StationsBean{" +
                "stationName='" + stationName + '\'' +
                ", stationUp='" + stationUp + '\'' +
                ", stationDwon='" + stationDwon + '\'' +
                '}';
    }
}
