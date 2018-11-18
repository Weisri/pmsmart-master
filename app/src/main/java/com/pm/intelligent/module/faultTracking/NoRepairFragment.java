package com.pm.intelligent.module.faultTracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.orhanobut.logger.Logger;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseFragment;
import com.pm.intelligent.bean.FaultTrackEntity;
import com.pm.intelligent.module.faultTracking.adapter.FaultTrackingListAdapter;
import com.pm.intelligent.module.faultTracking.dao.FaultTrackingDao;
import com.wall_e.multiStatusLayout.MultiStatusLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pumin on 2018/8/11.
 */

public class NoRepairFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.lv_fault_tracking)
    ListView lvFaultTracking;
    Unbinder unbinder;
    @BindView(R.id.status_layout_fault)
    MultiStatusLayout statusLayoutFault;
    private List<FaultTrackEntity> mDataList = new ArrayList<>();
    private String name;
    private FaultTrackingListAdapter adapter;

    @Override
    protected int setView() {
        return R.layout.fragment_fault_tracking;
    }

    @Override
    protected void initView(View view) {

        lvFaultTracking.setOnItemClickListener(this);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        if (name.equals("未修复")) {
            mDataList = FaultTrackingDao.deUnique(0);
//            Logger.t("查询未修复").d(mDataList);
        }
        if (name.equals("修复中")) {
            mDataList = FaultTrackingDao.deUnique(1);
//            Logger.t("查询修复中").d(mDataList);
        }
        if (name.equals("已修复")) {
            mDataList = FaultTrackingDao.deUnique(2);
//            Logger.t("查询已修复").d(mDataList);
        }
        if (mDataList.isEmpty() || null == mDataList) {
            statusLayoutFault.showEmpty();
        } else {
            adapter = new FaultTrackingListAdapter(mDataList, getContext());
            lvFaultTracking.setAdapter(adapter);
        }

    }

    //复用
    public static Fragment getFuYong(String string) {
        NoRepairFragment myFragmentFuYong = new NoRepairFragment();
        Bundle bundle = new Bundle();
        bundle.putString("string", string);
        myFragmentFuYong.setArguments(bundle);
        return myFragmentFuYong;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // ActivityUtil.INSTANCE.startActivity();
        Intent intent = new Intent(getActivity(), NoRepairDetailActivity.class);
        intent.putExtra("faultInfoBean", mDataList.get(i));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
