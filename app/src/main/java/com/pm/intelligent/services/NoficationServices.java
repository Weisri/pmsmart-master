package com.pm.intelligent.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.R;
import com.pm.intelligent.module.home.activity.MainActivity;
import com.pm.intelligent.utils.MessageEvent;
import com.pm.intelligent.utils.rxUtil.RxBus;
import com.pm.mc.chat.netty.pojo.Msg;
import com.pm.nettyService.INettyMsgCallBack;
import com.pm.nettyService.IOutMsgReCallBack;
import com.pm.nettyService.NettyService;
import com.pm.nettyService.NettyStatus;
import com.pm.nettyService.NettyUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by WeiSir on 2018/8/20.
 */
public class NoficationServices extends Service  {
    //    public static WebSocketClient client;
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private ArrayList<String> msgQueen = new ArrayList<String>();
    private boolean isSaveInSrevice = true;
    private MyBinder mBinder = new MyBinder();
    private boolean iscon = true;//用于在broadcast中判断是否是需要重新连接的
    private NotificationManager manager;
    private NotificationCompat.Builder notifyBuilder;
    private Vibrator vibrator;
    private NettyUtil mNettyUtil;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReciver, filter);
        Logger.i("启动notification 服务。。。。。");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //todo 接收服务器消息
        mNettyUtil = NettyUtil.with(Msg.AlarmMessage.class).setCallBack(new IOutMsgReCallBack<Msg.AlarmMessage>() {
            @Override
            public void messageReceived(Msg.AlarmMessage msg) {
                Logger.d("来自notificationservice===========" + msg.toString());
            }
        });

        return START_STICKY;
    }





    //测试
    public void event(MessageEvent message) {
        String type = message.getMessage();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendNotification(type);
            Logger.i(type);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReciver);
    }

    private BroadcastReceiver mReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable() && iscon == false) {
                    Intent serverIntent = new Intent(context, NoficationServices.class);
                    context.startService(serverIntent);
                }
            }
        }
    };



    public class MyBinder extends Binder {
        public void sendTiUI() {
            Message msg = new Message();
            msg.what = MainActivity.ONMESSAGELIST;
            Bundle bundle = new Bundle();
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(msgQueen);
            arrayList.addAll(msgQueen);
            bundle.putParcelableArrayList("list", arrayList);
            msg.setData(bundle);
            //todo
//            MainActivity.uihandler.sendMessage(msg);
            isSaveInSrevice = false;
            msgQueen.clear();
            manager.cancel(121);

        }

        public NoficationServices getNotificationService(){
            return NoficationServices.this;
        }

        public void setIsSaveInSer() {
            isSaveInSrevice = true;
        }
    }

    private void openVibrator() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBinder.setIsSaveInSer();
        return super.onUnbind(intent);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sendNotification(String type) {
        String channelID = "1";
        String channelName = "channel_name";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        }
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Notification.Builder builder = new Notification.Builder(this,channelID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(false)
                .setPriority(Notification.PRIORITY_MAX)
                .setFullScreenIntent(contentIntent, false)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND & Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.nofication_layout);
        remoteViews.setTextViewText(R.id.tv_info, type);

        int iUniqueId = (int) System.currentTimeMillis();
        remoteViews.setTextViewText(R.id.tv_notification_time, iUniqueId + "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews);
        }
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        //notify id 如果相同，则不管调用几次这个函数,最后，都只出来一个notification
        if (manager != null) {
            manager.notify(iUniqueId, notification);
        }
    }
}
