package com.pm.intelligent.module.hardware;

import java.util.List;

/**
 * Created by WeiSir on 2018/7/12.
 */

public class HardWareGroupBean {
    private String hardwareName;
    private List<HardWareItemBean> hardwareParamVos;

    public String getHardwareName() {
        return hardwareName;
    }

    public void setHardwareName(String hardwareName) {
        this.hardwareName = hardwareName;
    }

    public List<HardWareItemBean> getHardwareParamVos() {
        return hardwareParamVos;
    }

    public void setHardwareParamVos(List<HardWareItemBean> hardwareParamVos) {
        this.hardwareParamVos = hardwareParamVos;
    }

    @Override
    public String toString() {
        return "HardWareGroupBean{" +
                "hardwareName='" + hardwareName + '\'' +
                ", hardwareParamVos=" + hardwareParamVos +
                '}';
    }
}
