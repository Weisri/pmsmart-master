package com.pm.nettyService;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.bean.AlarmInfoEntity;
import com.pm.intelligent.bean.FaultTrackEntity;
import com.pm.intelligent.greendao.AlarmInfoEntityDao;
import com.pm.intelligent.greendao.FaultTrackEntityDao;
import com.pm.intelligent.utils.NotificationUtil;
import com.pm.intelligent.utils.rxUtil.RxBus;
import com.pm.mc.chat.netty.pojo.Msg;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 作者：zhuhuitao on 2018/5/18
 * 邮箱：zhuhuitao_struggle@163.com
 */

@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<Msg.Message> {

    private static final String TAG = "NettyClientHandler";
    private NettyService session;

    public NettyClientHandler(NettyService nettyService) {
        session = nettyService;
    }

    public INettyMsgCallBack mMsgReCallBack;

    public void setMsgReCallBack(INettyMsgCallBack callBack) {
        this.mMsgReCallBack = callBack;
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, final Msg.Message msg)
            throws Exception {

        //传入信息监听的类
//        Logger.d("messageReceived: " + msg.toString());

        if (mMsgReCallBack != null) {
            mMsgReCallBack.messageReceived(msg);
        }

        switch (msg.getMessageType()) {
            case CLIENT_LOGIN://客户登录
                Msg.LoginMessage loginMessage = msg.getLoginMessage();
                Logger.d("messageReceived:客户登录： " + loginMessage.toString());
//                CommonUtils.token = loginMessage.getToken();
                break;
            case ALARM_INFO://实时告警
                Msg.AlarmMessage alarmMessage = msg.getAlarmMessage();
                Logger.t("报警推送消息:").d(alarmMessage.toString());
                String title = alarmMessage.getAlarmType();
                String content = alarmMessage.getAlarmList(0).getAlarmName();
                //存库
                if (!"漏电报警".equals(content) && !"水浸报警".equals(content)) {
                    AlarmInfoEntity alarmInfoEntity = new AlarmInfoEntity();
                    alarmInfoEntity.setAlarmName(content);
                    alarmInfoEntity.setIccid(alarmMessage.getIccid());
                    alarmInfoEntity.setAlarmStatus(0);
                    AlarmInfoEntityDao entityDao = MyApplication.getDaoSession().getAlarmInfoEntityDao();
                    entityDao.insertOrReplace(alarmInfoEntity);
                    //发送
                    NotificationUtil.sendNotification(title, content, PushMsgHandler.type.alarm.ordinal());
                }

                break;
            case TROUBLE_INFO://故障信息
                Msg.TroubleMessage troubleMessage = msg.getTroubleMessage();
                Logger.t("异常推送消息:").d(troubleMessage.toString());
                String troubleTitle = troubleMessage.getTroubleName();
                String troubleContent = troubleMessage.getTroubleName();
                FaultTrackEntity trackEntity = new FaultTrackEntity();
                trackEntity.setIccid(troubleMessage.getIccid());
                trackEntity.setTroubleName(troubleContent);
                trackEntity.setTroubleStatus(0);
                trackEntity.setShelterName(troubleTitle);
                FaultTrackEntityDao faultTrackEntityDao = MyApplication.getDaoSession().getFaultTrackEntityDao();
                faultTrackEntityDao.insertOrReplace(trackEntity);
                NotificationUtil.sendNotification(troubleTitle, troubleContent, NotificationUtil.type.trouble.ordinal());
                break;
            default:
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }


}
