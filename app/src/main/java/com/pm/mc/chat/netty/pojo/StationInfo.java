package com.pm.mc.chat.netty.pojo;

import lombok.Builder;
import lombok.Getter;

/**
 * 站点信息实体类
 *
 * @author Liu Xilin
 * @date Created in 2018/08/15 18:58
 */
@Getter
@Builder(toBuilder = true)
public class StationInfo {
    private String stationName;
    private String code;
}
