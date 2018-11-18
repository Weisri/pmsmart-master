package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/10/16.
 */
public class ElectricWeekBean {

    /**
     * week : 星期一
     * electricTotal : 0
     */

    private String week;
    private int electricTotal;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getElectricTotal() {
        return electricTotal;
    }

    public void setElectricTotal(int electricTotal) {
        this.electricTotal = electricTotal;
    }

    @Override
    public String toString() {
        return "ElectricWeekBean{" +
                "week='" + week + '\'' +
                ", electricTotal=" + electricTotal +
                '}';
    }
}
