package com.pm.intelligent.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.pm.intelligent.R;
import com.pm.intelligent.widget.BaseToolBar;

import butterknife.BindView;

/**
 * Created by WeiSir on 2018/9/19.
 */
public class ToolBarActivity extends BaseActivity  {

    private BaseToolBar toolbar;
    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_base_toolbar;
    }

    @Override
    protected void findViews() {
        toolbar = this.findViewById(R.id.base_toolbar);

    }

    @Override
    protected void initViews() {
        toolbar.setMyToolBarLeftBtnClick(new BaseToolBar.MyToolBarLeftBtnListener() {
            @Override
            public void imageLeftBtnclick() {
                finish();
            }
        });
    }

    @Override
    protected void initDatas() {

    }

    protected void setToolTitle(String title) {
        toolbar.setTitle(title);

    }

    protected void setChildView(int layout) {
        View view = LayoutInflater.from(this).inflate(layout, null, false);
        setChildView(view);
    }

    protected void setChildView(View view) {
        container.removeAllViews();
        container.addView(view);
    }



}
