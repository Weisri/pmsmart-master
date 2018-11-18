package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/11/4.
 */
public class SeatHeatBean {
    private String value;
    private String time;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SeatHeatBean{" +
                "value='" + value + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
