package com.pm.intelligent;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.widget.MySinkingView;


/**
 * Created by pumin on 2018/8/1.
 */

public class AnimTest extends BaseActivity implements Handler.Callback{
    private MySinkingView mSinkingView;

    private float percent = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.act_anim_test;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViews() {
        mSinkingView = (MySinkingView) findViewById(R.id.sinking);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                test();
            }
        });
        percent = 0.00f;
        mSinkingView.setPercent(percent);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    private void test() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                percent = 0;
                while (percent <= 1) {
                    mSinkingView.setPercent(percent);
                    percent += 0.01f;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                percent = 0.83f;
                mSinkingView.setPercent(percent);
                // mSinkingView.clear();
            }
        });
        thread.start();
    }

}
