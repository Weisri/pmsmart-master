package com.pm.intelligent.bean;

/**
 * Created by WeiSir on 2018/7/2.
 */

public class PatrolResultBean {
    //站台名
    private String mStationName;
    //站台
    private String mFinish;
    //异常信息
    private String mErrorInfo;
    /**
     * 站牌唯一标识
     */
    private String iccid;
    /**
     * 检查状态
     */
    private int checkStatus;

    public String getmStationName() {
        return mStationName;
    }

    public void setmStationName(String mStationName) {
        this.mStationName = mStationName;
    }

    public String getmFinish() {
        return mFinish;
    }

    public void setmFinish(String mFinish) {
        this.mFinish = mFinish;
    }

    public String getmErrorInfo() {
        return mErrorInfo;
    }

    public void setmErrorInfo(String mErrorInfo) {
        this.mErrorInfo = mErrorInfo;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    @Override
    public String toString() {
        return "PatrolResultBean{" +
                "mStationName='" + mStationName + '\'' +
                ", mFinish='" + mFinish + '\'' +
                ", mErrorInfo='" + mErrorInfo + '\'' +
                ", iccid='" + iccid + '\'' +
                ", checkStatus=" + checkStatus +
                '}';
    }
}
