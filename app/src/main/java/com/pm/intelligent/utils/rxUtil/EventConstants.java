package com.pm.intelligent.utils.rxUtil;

/**
 * 作者：zhuhuitao on 2018/5/18
 * 邮箱：zhuhuitao_struggle@163.com
 */


public class EventConstants {
    public static final String COMPLETE_INFO = "complete_info";
    private String event;
    public EventConstants(String event){
        this.event = event ;
    }
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
