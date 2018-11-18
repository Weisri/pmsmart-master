package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/7/17.
 *
 */

public class AlarmDetialBean {
    String time;
    int Status;

    public AlarmDetialBean(String time, int status) {
        this.time = time;
        Status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "AlarmDetialBean{" +
                "time='" + time + '\'' +
                ", Status=" + Status +
                '}';
    }
}
