package com.pm.nettyService;

import com.pm.mc.chat.netty.pojo.Msg;

public interface IOutMsgReCallBack<T> {
    void messageReceived(T msg);
}
