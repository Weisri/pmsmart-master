package com.pm.nettyService;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.module.home.activity.MainActivity;
import com.pm.intelligent.utils.rxUtil.RxBus;
import com.pm.mc.chat.netty.pojo.Msg;

import java.lang.reflect.Method;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Netty网络的工具
 */
public class NettyUtil {

    /**
     * 服务管理类
     */
    private static ServiceManager serviceManager = null;

    /**
     * 接受消息的回调类
     */
    private IOutMsgReCallBack mMsgReceiveCallBack = null;


    private IOutLinkStatusCallBack mOutLinkStatusCallBack = null;



    /**
     * 判断需要返回的数据类型的class
     */
    private Class mReturnType = null;

    private NettyUtil(){

    }

    private NettyUtil(Class clazz) {
        mReturnType = clazz;
    }



    /**
     * 创建NettyUtil
     * @return
     */
    public static NettyUtil with() {
        return new NettyUtil();
    }

    /**
     * 创建NettyUtil
     * @param clazz  判断需要返回的数据类型的class 可传
     * @return
     */
    public static NettyUtil with(Class clazz) {
        return new NettyUtil(clazz);
    }




    /**
     * 设置是否需要回调
     * @param callBack
     * @return
     */
    public NettyUtil setCallBack(IOutMsgReCallBack callBack) {
        mMsgReceiveCallBack = callBack;
        registerRxBus();
        return this;
    }

    /**
     * 网络状态的回调
     * @param linkStatusCallBack
     * @return
     */
    public NettyUtil setLinkStatusCallBack(IOutLinkStatusCallBack linkStatusCallBack){
        mOutLinkStatusCallBack = linkStatusCallBack;
        return this;
    }


    /**
     * 返回网络状态
     * @return
     */
    public boolean getLinkStatus(){
        return serviceManager.getNettyService().getLinkStatus();
    }


    /**
     * 如果设置了CallBack避免内存溢出
     */
    public void unBind() {
        mMsgReceiveCallBack = null;
    }


    private void registerRxBus() {
        Disposable subscribe = RxBus.getInstance().toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object obj) throws Exception {
                        dealMsg(obj);
                    }
                });
    }

    /**
     * 对消息进行处理，如果有class,通过class获取数据类名
     * 以类名进行方法名称的拼接，通过反射调用对应的方法，
     * 回调回去
     * @param obj
     */
    private void dealMsg(Object obj) {
        if (obj instanceof Msg.Message) {

            if (mMsgReceiveCallBack != null) {
                try {
                    Msg.Message msg = (Msg.Message) obj;
                    String invokeName = getInvokeName();
                    if (!TextUtils.isEmpty(invokeName)) {
                        Logger.d(" =====" + invokeName);
                        Method method = Msg.Message.class.getMethod(invokeName);
                        Object invoke = method.invoke(msg);
                        mMsgReceiveCallBack.messageReceived(invoke);
                    } else {
                        mMsgReceiveCallBack.messageReceived(msg);
                    }
                } catch (Exception e) {
                    Logger.d("NettyUtil == registerRxBus ==  accept" + e.getMessage());
                    mMsgReceiveCallBack.messageReceived(obj);
                }
            }
        }else if (obj instanceof NettyStatus){
            if (mOutLinkStatusCallBack != null) {
                mOutLinkStatusCallBack.nettyLinkStatus(((NettyStatus) obj).isLink());
            }
        }

    }

    /**
     * 动态拼接方法名称
     * @return
     */
    private String getInvokeName() {
        if (mReturnType != null) {
            String simpleName = mReturnType.getSimpleName();
            if (!TextUtils.isEmpty(simpleName)) {
                StringBuilder sb = new StringBuilder();
                sb.append("get").append(simpleName);
                return sb.toString();
            }
        }
        return "";
    }


    public void sendMsg(Msg.Message msg) {
        serviceManager.getNettyService().sendMessage(msg);
    }


    public static void init(MainActivity activity,String ip, int port) {
        serviceManager = serviceManager.registerInstance(activity,ip,port);
    }

}
