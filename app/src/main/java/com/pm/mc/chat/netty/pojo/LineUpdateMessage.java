package com.pm.mc.chat.netty.pojo;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 线路更新信息类
 *
 * @author Liu Xilin
 * @date Created in 2018/08/15 19:02
 */
@Getter
@Builder(toBuilder = true)
public class LineUpdateMessage {
    private int projectId;
    private String lineName;
    private int status;
    private LineInfo lineInfo;
    private List<StationInfo> stationInfoList;

}
