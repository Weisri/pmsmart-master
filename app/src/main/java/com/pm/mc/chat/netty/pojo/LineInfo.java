package com.pm.mc.chat.netty.pojo;

import lombok.Builder;
import lombok.Getter;

/**
 * 线路信息实体类
 *
 * @author Liu Xilin
 * @date Created in 2018/08/28 13:35
 */
@Getter
@Builder(toBuilder = true)
public class LineInfo {
    private String startStation;
    private String endStation;
    private String upStartTime;
    private String upEndTime;
    private String downStartTime;
    private String downEndTime;
    private double ticketPrice;
    private int ticketType;
    private String lineCompany;
}
