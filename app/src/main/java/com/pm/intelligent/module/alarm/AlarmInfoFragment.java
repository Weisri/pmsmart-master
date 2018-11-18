package com.pm.intelligent.module.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseFragment;
import com.pm.intelligent.bean.AlarmInfoEntity;
import com.pm.intelligent.module.alarm.activity.AlarmInfoDetialActivity;
import com.pm.intelligent.module.alarm.adapter.AlarmExListAdapter;
import com.pm.intelligent.module.alarm.dao.AlarmInfoDao;
import com.wall_e.multiStatusLayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by WeiSir on 2018/6/27.
 */

public class AlarmInfoFragment extends BaseFragment implements
        ExpandableListView.OnChildClickListener {

    @BindView(R.id.list_info)
    ExpandableListView listInfo;
    @BindView(R.id.status_layout_alarm)
    MultiStatusLayout statusLayout;
    Unbinder unbinder;

    private Activity mActivity;
    private AlarmExListAdapter mExadapter;
    private List<String> mGroupList = new ArrayList<>();
    private List<List> mChildList = new ArrayList<>();
    private String name;

    @Override
    protected int setView() {
        return R.layout.monitor_fragment_realtime_layout;
    }

    @Override
    protected void initView(View view) {
//        getDatas();

        listInfo.setOnChildClickListener(this);
        mExadapter = new AlarmExListAdapter(mActivity, mGroupList, mChildList);
        listInfo.setAdapter(mExadapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            name = arguments.getString("string");
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (name.equals("待处理")) {
            getDataFromDB(0);
        }
        if (name.equals("处理中")) {
            getDataFromDB(1);
        }
        if (name.equals("已处理")) {
            getDataFromDB(2);
        }
    }

    private void getDatas() {
        mGroupList.add("漏电报警");
        mGroupList.add("破坏报警");
        mGroupList.add("LCD机箱报警");
        mGroupList.add("水浸报警");
        mGroupList.add("烟雾报警");
        mGroupList.add("断电报警");
        mGroupList.add("断网报警");
        mGroupList.add("门禁报警");
        mGroupList.add("环境超标报警");
        mGroupList.add("盒子报警");
        mGroupList.add("非法内容报警");
        mGroupList.add("SD卡报警");
    }

    /**
     * select data from db
     * @param status
     */
    private void getDataFromDB(int status) {
        //漏电报警
        List<AlarmInfoEntity> a = AlarmInfoDao.doUnique(status, "漏电报警");
        if (!a.isEmpty()) {
            mGroupList.add("漏电报警");
            mChildList.add(a);
        }
        //破坏报警
        List<AlarmInfoEntity> b = AlarmInfoDao.doUnique(status, "破坏报警");
        if (!b.isEmpty()) {
            mGroupList.add("破坏报警");
            mChildList.add(b);
        }

        //LCD机箱报警
        List<AlarmInfoEntity> c = AlarmInfoDao.doUnique(status, "机箱报警");
        if (!c .isEmpty()) {
            mGroupList.add("机箱报警");
            mChildList.add(c);
        }
        //水浸报警
        List<AlarmInfoEntity> d = AlarmInfoDao.doUnique(status, "水浸报警");
        if (!d.isEmpty()) {
            mGroupList.add("水浸报警");
            mChildList.add(d);
        }
        //烟雾报警
        List<AlarmInfoEntity> e = AlarmInfoDao.doUnique(status, "烟雾报警");
        if (!e .isEmpty()) {
            mGroupList.add("烟雾报警");
            mChildList.add(e);
        }
        //断电报警
        List<AlarmInfoEntity> f = AlarmInfoDao.doUnique(status, "断电报警");
        if (!f.isEmpty()) {
            mGroupList.add("断电报警");
            mChildList.add(f);
        }
        //断网报警
        List<AlarmInfoEntity> g = AlarmInfoDao.doUnique(status, "断网报警");
        if (!g.isEmpty()) {
            mGroupList.add("断网报警");
            mChildList.add(g);
        }
        //门禁报警
        List<AlarmInfoEntity> h = AlarmInfoDao.doUnique(status, "门禁报警");
        if (!h.isEmpty()) {
            mGroupList.add("门禁报警");
            mChildList.add(h);
        }
        //环境超标报警
        List<AlarmInfoEntity> i = AlarmInfoDao.doUnique(status, "环境超标报警");
        if (!i.isEmpty()) {
            mGroupList.add("环境超标报警");
            mChildList.add(i);
        }
        // 盒子报警
        List<AlarmInfoEntity> k = AlarmInfoDao.doUnique(status, "盒子报警");
        if (!k.isEmpty()) {
            mGroupList.add("盒子报警");
            mChildList.add(k);
        }
        // 非法内容报警
        List<AlarmInfoEntity> l = AlarmInfoDao.doUnique(status, "非法内容报警");
        if (!l.isEmpty()) {
            mGroupList.add("非法内容报警");
            mChildList.add(l);
        }
        // sd卡报警
        List<AlarmInfoEntity> m = AlarmInfoDao.doUnique(status, "sd卡报警");
        if (!m.isEmpty()) {
            mGroupList.add("SD卡报警");
            mChildList.add(m);
        }
        if (mChildList == null || mChildList.isEmpty()) {
            statusLayout.showEmpty();
        } else {
            mExadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    //复用
    public static Fragment getFuYong(String string) {
        AlarmInfoFragment myFragmentFuYong = new AlarmInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("string", string);
        myFragmentFuYong.setArguments(bundle);
        return myFragmentFuYong;
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(mActivity, AlarmInfoDetialActivity.class);
        AlarmInfoEntity item = (AlarmInfoEntity) mChildList.get(groupPosition).get(childPosition);
        intent.putExtra("alarmInfoBean", item);
        startActivity(intent);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
