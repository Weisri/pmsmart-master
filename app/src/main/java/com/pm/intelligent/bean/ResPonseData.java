package com.pm.intelligent.bean;

/**
 * Created by Administrator on 2018/9/6.
 */

public class ResPonseData<D> {

    private int status;
    private String msg;
    private D data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
