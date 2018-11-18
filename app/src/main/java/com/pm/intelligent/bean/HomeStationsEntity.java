package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/8/21.
 */
public class HomeStationsEntity {

    public HomeStationsEntity(int iccid, String shelterName, int type) {
        this.iccid = iccid;
        this.shelterName = shelterName;
        this.type = type;
    }

    /**
     * iccid : 1
     * shelterName : 龙华(上行)
     * type :0
     */

    private int iccid;
    private String shelterName;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIccid() {
        return iccid;
    }

    public void setIccid(int iccid) {
        this.iccid = iccid;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }
}
