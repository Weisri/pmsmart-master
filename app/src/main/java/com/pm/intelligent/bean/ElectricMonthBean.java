package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/10/16.
 */
public class ElectricMonthBean {


    /**
     * month : 一月
     * electricTotal : 30
     */

    private String month;
    private int electricTotal;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getElectricTotal() {
        return electricTotal;
    }

    public void setElectricTotal(int electricTotal) {
        this.electricTotal = electricTotal;
    }

    @Override
    public String toString() {
        return "ElectricMonthBean{" +
                "month='" + month + '\'' +
                ", electricTotal=" + electricTotal +
                '}';
    }
}
