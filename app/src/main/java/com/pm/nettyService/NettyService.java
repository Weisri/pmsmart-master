package com.pm.nettyService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.UrlConstant;
import com.pm.intelligent.brocastReciver.NetWorkBroadCastReceiver;
import com.pm.intelligent.brocastReciver.NetWorkListener;
import com.pm.intelligent.utils.SPUtils;
import com.pm.intelligent.utils.rxUtil.RxBus;
import com.pm.mc.chat.netty.pojo.Msg;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * 作者：zhuhuitao on 2018/5/18
 * 邮箱：zhuhuitao_struggle@163.com
 */
public class NettyService extends Service implements NetWorkListener, INettyMsgCallBack {
    private static final String TAG = "NettyService";
    private Bootstrap bootstrap;
    private ChannelFuture future;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private boolean firstStart = false;

    private String mServerIp = UrlConstant.SERVER_IP;
    private int mServerPort = UrlConstant.SERVER_PORT;
    private boolean mLinkStatus = false;
    private SocketChannel socketChannel;
    private NettyClientHandler nettyClientHandler;
    private HeartBeatHandler heartBeatHandler;
    private static Context context;


    private Handler mHandler = new Handler();
    /**
     * 监控网络的广播
     */
    private NetWorkBroadCastReceiver netBroadcastReceiver;
    private ThreadPoolExecutor poolExecutor;
    public boolean isConnect;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    @Override
    public void messageReceived(Msg.Message msg) {
        RxBus.getInstance().post(msg);
    }

    @Override
    public void nettyLinkStatus(NettyStatus status) {
        Logger.e("nettyLinkStatus       " + status.isLink());
        mLinkStatus = status.isLink();
        RxBus.getInstance().post(status);
    }


    public class MyBinder extends Binder {
        public NettyService getNettyService(){
            return NettyService.this;
        }

    }



    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        nettyClientHandler = new NettyClientHandler(NettyService.this);
        heartBeatHandler = new HeartBeatHandler(this);

        nettyClientHandler.setMsgReCallBack(this);
        heartBeatHandler.setMsgReCallBack(this);



        // 建立线程池做连接
        poolExecutor = new ThreadPoolExecutor(3, 5, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128));
        //注册网络状态监听广播
        registerReceiver();
        closeChanel();
        doConnect();

        Logger.d("  server  onCreate" + mServerPort + "     " + mServerIp);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    /**
     * 关闭资源
     */
    private void closeChanel() {
        if (socketChannel != null) {
            if (socketChannel.pipeline().get("ping") != null) {
                socketChannel.pipeline().remove("ping");
            }
            if (socketChannel.pipeline().get("nettyHandler") != null) {
                socketChannel.pipeline().remove(NettyClientHandler.class);
            }
            if (socketChannel.pipeline().get("heartBeatHandler") != null) {
                socketChannel.pipeline().remove(HeartBeatHandler.class);
            }

            ChannelFuture future = socketChannel.close();
            try {
                future.channel().closeFuture().sync();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 将连接加入到线程池中
     */
    private void doConnect() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connect();
            }
        };
        poolExecutor.execute(runnable);
    }

    /**
     * 注册网络广播监听
     */
    private void registerReceiver() {

        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetWorkBroadCastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);

            //网络监听
            netBroadcastReceiver.setNetEvent(this);
        }
    }

    /**
     * 建立连接
     *
     * @return
     */
    private boolean connect() {

        firstStart = true;
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(mServerIp, mServerPort);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel)
                    throws Exception {
                socketChannel.pipeline().addLast("ping", new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS));
//                tcp粘包处理
                socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                //编码器
                socketChannel.pipeline().addLast(new ProtobufDecoder(Msg.Message.getDefaultInstance()));
                socketChannel.pipeline().addLast(new LengthFieldPrepender(2));
                //解码器
                socketChannel.pipeline().addLast(new ProtobufEncoder());

                socketChannel.pipeline().addLast("heartBeatHandler", heartBeatHandler);
                socketChannel.pipeline().addLast("nettyHandler", nettyClientHandler);
            }
        });

        try {
            future = bootstrap.connect(mServerIp, mServerPort).sync();
            if (future.isSuccess()) {
                isConnect = true;
                socketChannel = (SocketChannel) future.channel();
                doLoginAuth();
                Logger.i("与后台服务连接成功");
                connectTimes = 0;
                return true;
            } else {
                reconnect(); //次连接失败做重连处理
                return false;
            }
        } catch (Exception e) {
            reconnect(); //连接失败做重连处理
            e.printStackTrace();
            return false;
        }
    }


    public boolean getLinkStatus(){
        return mLinkStatus;
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMessage(Object msg) {
        if (this.socketChannel != null && isConnect) {
            this.socketChannel.writeAndFlush(msg);
        }
    }

    //登录认证
    private void doLoginAuth() {
        sendMessage(Msg.Message.newBuilder()
                .setMessageType(Msg.MessageType.CLIENT_LOGIN)
                .setLoginMessage(Msg.LoginMessage.newBuilder()
                        .setIccid(UrlConstant.ICCID)
                        .setToken(UrlConstant.TOKEN)
                        .build())
                .build());
        Logger.i("用户登录。。。。。。" + Msg.LoginMessage.newBuilder().getIccid());
    }

    @Override
    public void onDestroy() {
        closeChanel();
        poolExecutor.shutdown();
        super.onDestroy();
        if (netBroadcastReceiver != null) {
            //注销广播
            unregisterReceiver(netBroadcastReceiver);
        }
        //SceneController.getInstence().stopAllAlarm();
    }


    @Override
    public void onNetChange(int netWorkState) {
        Logger.d("网络连接状态" + netWorkState);
        if (socketChannel != null && netWorkState == -1) {
            closeChanel();
        } else {
            if (firstStart)//如果不是第一次启动则重新连接
            {
                doConnect();
            }
        }
    }

    private int connectTimes = 0;

    public void reconnect() {
        Logger.d("重新重连");

        isConnect = false;

        if (SPUtils.getInstance(this).getLoginState() == 2) {
            SPUtils.getInstance(this).setLoginState(1);
            return;
        }
        //如果登陆错误不需要重新连接
        if (SPUtils.getInstance(this).getLoginState() > 1) {
            return;
        }
        closeChanel();
        connectTimes++;
        //如果是第一次启动并且启动次数小于12
        if (firstStart && connectTimes < 12) {
            Logger.d("启动次数小于12:---" + connectTimes);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doConnect();
                }
            }, (connectTimes << 4) * 1000);
        }

    }


    public boolean isConnect() {
        return isConnect;
    }


    public static Context getContext() {
        return context;
    }

}
