package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/7/12.
 *
 */


public class BoxDataBean {
    private String name;
    private String info;
    private String status;

    public BoxDataBean(String name, String info,String status) {
        this.name = name;
        this.info = info;
        this.status = status;

    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BoxDataBean{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
