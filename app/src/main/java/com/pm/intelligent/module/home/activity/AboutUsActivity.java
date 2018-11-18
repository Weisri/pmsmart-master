package com.pm.intelligent.module.home.activity;

import android.webkit.WebView;

import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.BaseToolBar;

import butterknife.BindView;

/**
 * Created by WeiSir on 2018/7/16.
 *
 */

public class AboutUsActivity extends BaseActivity implements BaseToolBar.MyToolBarLeftBtnListener {
    @BindView(R.id.toolbar)
    BaseToolBar mToolbar;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected int setLayoutId() {
        return R.layout.abouts_activity_layout;
    }

    @Override
    protected void findViews() {
    mToolbar.setMyToolBarLeftBtnClick(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initDatas() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/about.html");
    }


    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    private int mNetStatus;
    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this,"当前网络不可用");
        }
    }
}
