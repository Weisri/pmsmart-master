package com.pm.intelligent.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

/**
 * 首页 GridView item动态布局设置工具类
 * 通过UI设计图，以屏幕高度为限制标准，算出各个比例，然后动态设置item的宽高以及padding
 */
public class HomeGridItemLayoutUtil {

    public static final String TAG = "HomeGridItemLayoutUtil";

    /**
     * 容器item的宽
     */
    private int mBcW;
    /**
     * 容器item的高
     */
    private int mBcH;
    /**
     * 左边空白的宽度
     */
    private int mLBlankW;
    /**
     * 上边空白的宽度
     */
    private int mTBlankW;
    /**
     * 右边空白的宽度
     */
    private int mRBlankW;


    private HomeGridItemLayoutUtil(Context context) {
        initData(context);
    }

    public static HomeGridItemLayoutUtil create(Context context) {
        return new HomeGridItemLayoutUtil(context);
    }



    private void initData(Context context) {
        setThreeItem(context);
    }





    /**
     * 0.75  0.77  1.28 三个比例是通过UI设计图计算出来的
     */
    private void setTwoItem(Context context){
        //      1,  获取屏幕的宽和高
        //屏幕高
        int srH = ScreenUtils.getScreenHeight(context);
        //屏幕宽
        int srW = ScreenUtils.getScreenWidth(context);

//        2，五个容器item高度之和 占用整个界面高度的百分比,通过百分比算出 单个内容item的高度
        mBcH = (int) (srH * 0.75 / 5);

//        3，通过单个大item的高度，算出内容item的高度
        //内容的item高
        int scH = (int) (mBcH * 0.77);

//        4，通过内容item的高度以及 item的宽高比  算出，宽度。
        //内容的item宽
        int scW = (int) (scH * 1.28);

//        5，通过内容item的宽度，以及整个页面宽度，算出上左右空白宽度。
        mBcW = srW / 2;
        mLBlankW = (int) ((mBcW - scW) * 0.7);
        mRBlankW = mBcW - scW - mLBlankW;
        mTBlankW = mBcH - scH;

    }




    private void setThreeItem(Context context){
        //      1,  获取屏幕的宽和高
        //屏幕高
        int srH = ScreenUtils.getScreenHeight(context);
        //屏幕宽
        int srW = ScreenUtils.getScreenWidth(context);

        if (srW >= srH){
            setThreeMainHeightItem(srH,srW);
        }else {
            setThreeMainWidthItem(srH,srW);
        }
    }


    private void setThreeMainWidthItem(int srH,  int srW){
        mBcW = srW/3;

        int scW = (int) (mBcW*0.75);



        mBcH = (int) (srH * 0.68 / 3);


        int allBlank =  srW - 3*scW;
        mLBlankW = allBlank/6;
        mRBlankW = mLBlankW;

        mTBlankW = mBcH - scW;
    }


    private void setThreeMainHeightItem(int srH,  int srW){

//        2，五个容器item高度之和 占用整个界面高度的百分比,通过百分比算出 单个内容item的高度
        mBcH = (int) (srH * 0.68 / 3);

//        3，通过单个大item的高度，算出内容item的高度
        //内容的item高
        int scH = (int) (mBcH * 0.7);

//        4，通过内容item的高度以及 item的宽高比  算出，宽度。
        //内容的item宽
        int scW = scH;

//        5，通过内容item的宽度，以及整个页面宽度，算出上左右空白宽度。
        mBcW = srW / 3;
        int allBlank =  srW - 3*scH;
        mLBlankW = allBlank/6;
        mRBlankW = mLBlankW;

//        mLBlankW = (int) ((mBcW - scW) * 0.7);
//        mRBlankW = mBcW - scW - mLBlankW;
        mTBlankW = mBcH - scH;
    }


    public void layoutItem(int pos, View convertView) {
//        6，设置布局
        if (mBcW > 0 && mBcH > 0) {
            convertView.setLayoutParams(new AbsListView.LayoutParams(mBcW, mBcH));
//            if (pos % 2 == 0) {
//                convertView.setPadding(mLBlankW, mTBlankW, mRBlankW, 0);
//            } else {
//                convertView.setPadding(mRBlankW, mTBlankW, mLBlankW, 0);
//            }
            convertView.setPadding(mLBlankW, mTBlankW, mRBlankW, 0);
        } else {
            Log.e(TAG, "设置布局的相关数据没有初始化成功！");
        }
    }
}
