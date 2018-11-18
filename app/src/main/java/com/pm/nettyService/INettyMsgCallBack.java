package com.pm.nettyService;

import com.pm.mc.chat.netty.pojo.Msg;

public interface INettyMsgCallBack {
    void messageReceived(Msg.Message msg);
    void nettyLinkStatus(NettyStatus status);
}
