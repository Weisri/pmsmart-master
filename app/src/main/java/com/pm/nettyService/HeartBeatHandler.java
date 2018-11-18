package com.pm.nettyService;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.UrlConstant;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.SPUtils;
import com.pm.mc.chat.netty.pojo.Msg;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelHandlerAdapter {
    private static final String TAG = "HeartBeatHandler";
    private NettyService nettyService;

    public HeartBeatHandler(NettyService nettyService){
        this.nettyService = nettyService ;
    }

    private INettyMsgCallBack mMsgReCallBack;

    private NettyStatus mNettyStatus = new NettyStatus();

    public void setMsgReCallBack(INettyMsgCallBack callBack){
        this.mMsgReCallBack = callBack;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server

                Msg.Message build = Msg.Message.newBuilder()
                        .setMessageType(Msg.MessageType.HEAT_BEAT_CLIENT)
                        .setPingMessage(Msg.PingMessage.newBuilder()
                                .setIccid(UrlConstant.ICCID)//TODO  UserName
                                .setToken(UrlConstant.TOKEN) //todo PassWord
                                .build())
                        .build();
                Logger.d(build.toString());
                ctx.writeAndFlush(build);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Logger.d("连接上了");
        mNettyStatus.setLink(true);
        if (mMsgReCallBack!= null) {
            mMsgReCallBack.nettyLinkStatus(mNettyStatus);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Logger.d("掉线，重连");
        mNettyStatus.setLink(false);
        if (mMsgReCallBack!= null) {
            mMsgReCallBack.nettyLinkStatus(mNettyStatus);
        }
        nettyService.reconnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
