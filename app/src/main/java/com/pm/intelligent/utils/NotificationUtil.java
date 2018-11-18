package com.pm.intelligent.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.R;
import com.pm.intelligent.module.alarm.activity.AlarmInfoActivity;
import com.pm.intelligent.module.faultTracking.FaultTrackingActivity;
import com.pm.intelligent.module.home.activity.MainActivity;
import com.pm.nettyService.NettyService;
import com.pm.nettyService.PushMsgHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.internal.Utils;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by WeiSir on 2018/9/12.
 */
public class NotificationUtil {
    private static final String TAG = "PushMsgHandler";
    private NettyService session;
    private static String channelId = "alarm";
    private static String channelName = "报警消息";

    public enum type {
        /**
         * 消息来源类型
         */
        alarm, trouble
    }

    public static void sendChatMsg(String title, String content, int mesType) {
        Logger.i("zoule sendChatMsg" + title + content + mesType);
        //创建消息channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        } else {

        }

        Intent resultIntent = null;
        if (mesType == PushMsgHandler.type.alarm.ordinal()) {
            resultIntent = new Intent(MyApplication.getsInstances().getApplicationContext(), AlarmInfoActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if (mesType == PushMsgHandler.type.trouble.ordinal()) {
            resultIntent = new Intent(MyApplication.getsInstances().getApplicationContext(), FaultTrackingActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(MyApplication.getsInstances().getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) MyApplication.getsInstances().getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(MyApplication.getsInstances().getApplicationContext(), channelId)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.avatar)
                    .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getsInstances().getApplicationContext().getResources(), R.mipmap.avatar))
                    .setAutoCancel(true)
//                    .setFullScreenIntent(contentIntent, true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(contentIntent);
        }

        RemoteViews remoteViews = new RemoteViews(MyApplication.getsInstances().getPackageName(), R.layout.nofication_layout);
        remoteViews.setTextViewText(R.id.tv_info, title);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        remoteViews.setTextViewText(R.id.tv_notification_time, simpleDateFormat.format(date));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews);
        }
        int iUniqueId = (int) System.currentTimeMillis();
        Notification notification = builder.build();
        //iUniqueId id 如果相同，则不管调用几次这个函数,最后，都只出来一个notification
        if (manager != null) {
            manager.notify(iUniqueId, notification);
        }
    }

    public static void sendNotification(String title, String content, int mesType) {
        Logger.i("zoule sendChatMsg" + title + content + mesType);

        NotificationManager mNotificationManager = (NotificationManager) MyApplication.getsInstances().getApplicationContext().getSystemService(MyApplication.getsInstances().getApplicationContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        //判断是否是8.0Android.O
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel(channelName,
                    "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);
            chan1.setLightColor(Color.GREEN);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(chan1);
//            createNotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            mBuilder = new NotificationCompat.Builder(MyApplication.getsInstances().getApplicationContext(), channelName);
        } else {
            mBuilder = new NotificationCompat.Builder(MyApplication.getsInstances().getApplicationContext());
        }
        Intent notificationIntent = new Intent(MyApplication.getsInstances().getApplicationContext(), AlarmInfoActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(MyApplication.getsInstances().getApplicationContext(), 0,
                notificationIntent, 0);
        mBuilder.setContentIntent(intent) //设置通知栏点击意图
                .setGroupSummary(false)
                .setGroup("group")
//                .setNumber(++pushNum) //设置通知集合的数量
                .setTicker(title + ":" + content) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.mipmap.avatar);//设置通知小ICON
        RemoteViews remoteViews = new RemoteViews(MyApplication.getsInstances().getPackageName(), R.layout.nofication_layout);
        remoteViews.setTextViewText(R.id.tv_info, title);
        remoteViews.setTextViewText(R.id.tv_describe, content);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        remoteViews.setTextViewText(R.id.tv_notification_time, simpleDateFormat.format(date));
        mBuilder.setCustomContentView(remoteViews);

        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        int iUniqueId = (int) System.currentTimeMillis();
        mNotificationManager.notify(iUniqueId, notify);
    }

    /**
     * 创建notification 通道
     *
     * @param channelId
     * @param channelName
     * @param importance
     */
    private static void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = null;
        NotificationManager notificationManager = (NotificationManager) MyApplication.getsInstances().getSystemService(
                NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, channelName, importance);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

    }
}
