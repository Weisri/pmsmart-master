package com.pm.nettyService;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.R;
import com.pm.intelligent.bean.AlarmInfoEntity;
import com.pm.intelligent.module.alarm.activity.AlarmInfoActivity;
import com.pm.intelligent.module.faultTracking.FaultTrackingActivity;
import com.pm.intelligent.module.home.activity.MainActivity;
import com.pm.mc.chat.netty.pojo.Msg;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by WeiSir on 2018/9/4.
 */
@ChannelHandler.Sharable
public class PushMsgHandler extends SimpleChannelInboundHandler<Msg.Message> {

    private static final String TAG = "PushMsgHandler";
    private NettyService session;
    private String channelId = "alarm";
    private String channelName = "报警消息";

    public enum type {
        /**
         * 消息来源类型
         */
        alarm,trouble
    }

    public INettyMsgCallBack mMsgReCallBack;

    public PushMsgHandler(NettyService nettyService) {
       this.session = nettyService;
    }

    public void setMsgReCallBack(INettyMsgCallBack callBack){
        this.mMsgReCallBack = callBack;
    }


    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, final Msg.Message msg)
            throws Exception {

        if (mMsgReCallBack !=  null) {
            mMsgReCallBack.messageReceived(msg);
        }

        //传入信息监听的类
        Logger.d("aaaaaaaaa: " + msg.toString());
        //创建消息channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
        switch (msg.getMessageType()) {
            case ALARM_INFO://报警推送
                Msg.AlarmMessage alarmMessage = msg.getAlarmMessage();
                Logger.t("报警推送消息:").d(alarmMessage.toString());
                String title = alarmMessage.getAlarmType();
                String content = alarmMessage.getAlarmType();
                AlarmInfoEntity alarmInfoEntity = new AlarmInfoEntity();
                alarmInfoEntity.setAlarmName(title);
                sendChatMsg(title, content,type.alarm.ordinal());
                break;
            case TROUBLE_INFO://异常
                Msg.TroubleMessage troubleMessage = msg.getTroubleMessage();
                Logger.t("异常推送消息:").d(troubleMessage.toString());
                String troubleTitle = troubleMessage.getTroubleName();
                String troubleContent = troubleMessage.getTroubleName();
                sendChatMsg(troubleTitle, troubleContent,type.trouble.ordinal());
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendChatMsg(String title, String content,int mesType) {
        Intent resultIntent = null;
        if (mesType == type.alarm.ordinal()) {
            resultIntent = new Intent(MyApplication.getsInstances().getApplicationContext(), AlarmInfoActivity.class);
        } else if (mesType == type.trouble.ordinal()) {
            resultIntent = new Intent(MyApplication.getsInstances().getApplicationContext(), FaultTrackingActivity.class);
        }
        if (resultIntent != null) {
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(MyApplication.getsInstances().getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) MyApplication.getsInstances().getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(MyApplication.getsInstances().getApplicationContext(), "chat")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.avatar)
                .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getsInstances().getApplicationContext().getResources(), R.mipmap.avatar))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setFullScreenIntent(contentIntent, true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(contentIntent);
        RemoteViews remoteViews = new RemoteViews(MyApplication.getsInstances().getPackageName(), R.layout.nofication_layout);
        remoteViews.setTextViewText(R.id.tv_info, title);
        remoteViews.setTextViewText(R.id.tv_notification_time, content);

        int iUniqueId = (int) System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews);
        }
        Notification notification = builder.build();
        //notify id 如果相同，则不管调用几次这个函数,最后，都只出来一个notification
        if (manager != null) {
            manager.notify(iUniqueId, notification);
        }
    }

    /**
     * 创建notification 通道
     *
     * @param channelId
     * @param channelName
     * @param importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) MyApplication.getsInstances().getSystemService(
                NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
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
