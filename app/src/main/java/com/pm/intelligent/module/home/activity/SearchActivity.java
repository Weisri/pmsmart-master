package com.pm.intelligent.module.home.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.pm.intelligent.MyApplication;
import com.pm.intelligent.R;
import com.pm.intelligent.base.BaseActivity;
import com.pm.intelligent.bean.HomeShelterEntity;
import com.pm.intelligent.bean.SearchHistory;
import com.pm.intelligent.greendao.DaoSession;
import com.pm.intelligent.greendao.HomeShelterEntityDao;
import com.pm.intelligent.greendao.SearchHistoryDao;
import com.pm.intelligent.module.home.adapter.MyRecyclerAdapter;
import com.pm.intelligent.utils.ActivityUtil;
import com.pm.intelligent.utils.CommonUtils;
import com.pm.intelligent.utils.ToastUtils;
import com.pm.intelligent.widget.BaseToolBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by WeiSir on 2018/7/4.
 */

public class SearchActivity extends BaseActivity implements
        BaseToolBar.MyToolBarEditTextListener,
        BaseToolBar.MyToolBarSearchListener,
        BaseToolBar.MyToolBarLeftBtnListener, MyRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.home_toolbar)
    BaseToolBar homeToolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rec_list)
    RecyclerView recList;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    private EditText mEditext;
    private String mKeyword = null;
    private MyRecyclerAdapter myRecyclerAdapter;
    private SearchHistoryDao mSearchHistoryDao;
    private HomeShelterEntityDao mHomeShelterDao;
    private List<String> mHistoryList = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.home_activity_search_layout;
    }

    @Override
    protected void findViews() {
        mEditext = this.findViewById(R.id.toolbar_editText);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mEditext.setHint(R.string.enter_station_name);
    }

    @Override
    protected void initViews() {
        mContext = this;
        DaoSession daoSession = MyApplication.getDaoSession();
        mSearchHistoryDao = daoSession.getSearchHistoryDao();
        mHomeShelterDao = daoSession.getHomeShelterEntityDao();
        homeToolbar.setMyToolBarEditTextListener(this);
        homeToolbar.setMyToolBarSearchClick(this);
        homeToolbar.setMyToolBarLeftBtnClick(this);
        myRecyclerAdapter = new MyRecyclerAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recList.setLayoutManager(manager);
        recList.setAdapter(myRecyclerAdapter);
        myRecyclerAdapter.setItemClickListener(this);
    }

    @Override
    protected void initDatas() {
        initHistory();
    }

    /**
     * 进入加载历史列表
     */
    private void initHistory() {
        if (mHistoryList.size() != 0) {
            mHistoryList.clear();
            tvTitle.setVisibility(View.VISIBLE);
            tvClear.setVisibility(View.VISIBLE);
        }
        myRecyclerAdapter.setmHistoryList(getHistory(mHistoryList));
        myRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * 查出历史记录
     * @return list
     */
    public List<String> getHistory(List<String> list) {
        QueryBuilder<SearchHistory> qb = mSearchHistoryDao.queryBuilder();
        for (SearchHistory s : qb.list()) {
            list.add(s.getText());
        }
        //倒序输出list
        Collections.reverse(list);
        return list;
    }

    /**
     * 将搜索关键字插入历史记录
     * @param keyword
     */
    private void insertHistory(String keyword) {
        try {
            SearchHistory history = new SearchHistory(keyword);
            mSearchHistoryDao.insertOrReplace(history);
            Log.e("INSERT INTO DB", "insertHistory: 插入♂");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("INSERT INTO DB", "insertHistory: 插入失败");
        }
    }

    /**
     * 一键清空历史纪录
     */
    private void doDeleteAllDB() {
        try {
            mSearchHistoryDao.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DELETE ALL DB", "doDeleteAllDB: 删除失败");
        }
    }

    /**
     * 联想搜索
     *
     * @param searchList
     * @param keyword
     * @return 返回搜索集合
     */
    private void doDBSearch(List<String> searchList, String keyword) {
//        /**
//         * 查询历史记录
//         */
//        List<SearchHistory> histories = mSearchHistoryDao.queryBuilder()
//                .where(SearchHistoryDao.Properties.Text.like("%" + keyword + "%")).list();
//        for (SearchHistory history : histories) {
//            searchList.add(history.getText());
//        }
        /**
         * 查询本地站台列表
         */
        List<HomeShelterEntity> shelters = mHomeShelterDao.queryBuilder()
                .where(HomeShelterEntityDao.Properties.ShelterName.like("%" + keyword + "%")).list();
        Logger.d("查询数据库所有数据：" + mHomeShelterDao.loadAll().toString());
        for (HomeShelterEntity shelter : shelters) {
            searchList.add(shelter.getShelterName());
        }
    }

    /**
     * 条件查询
     */
    private void doUnique(String keyword) {
        HomeShelterEntity entity = mHomeShelterDao.queryBuilder()
                .where(HomeShelterEntityDao.Properties.ShelterName.eq(keyword)).unique();
        if (entity == null) {
            ToastUtils.showShort(this, "未查询到该站台");
        } else {
            Logger.t("搜索查询结果").d(entity.toString());
//            EventBus.getDefault().postSticky(entity);
            insertHistory(keyword);
            Intent i=new Intent();
            i.putExtra("homeShelterEntity", entity);
            setResult(4,i);
            ActivityUtil.INSTANCE.startActivity(MainActivity.class);
            finish();
        }
    }


    /**
     * 搜索点击回调
     *
     * @param content
     */
    @Override
    public void searchBtnclick(String content) {
        mKeyword = mEditext.getText().toString().trim();
        if (!TextUtils.isEmpty(mKeyword)) {
            doUnique(mKeyword);
        }

    }

    @OnClick(R.id.tv_clear)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                //清除历史记录
                doDeleteAllDB();
                mHistoryList.clear();
                myRecyclerAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void imageLeftBtnclick() {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mKeyword = mEditext.getText().toString().trim();
        //模糊搜索
        if (mKeyword.isEmpty()) {
            tvTitle.setVisibility(View.VISIBLE);
            tvClear.setVisibility(View.VISIBLE);
        } else if (!mKeyword.isEmpty()) {
            tvTitle.setVisibility(View.GONE);
            tvClear.setVisibility(View.GONE);
            mHistoryList.clear();
            doDBSearch(mHistoryList, mKeyword);
            myRecyclerAdapter.setmHistoryList(mHistoryList);
            myRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mKeyword.isEmpty()) {
            initHistory();
        }
    }

    private int mNetStatus;
    @Override
    public void onNetChange(int netWorkState) {
        this.mNetStatus = netWorkState;
        if (mNetStatus == -1) {
            ToastUtils.showLong(this, "当前网络不可用");
        }
    }

    @Override
    public void OnItemClick(View view, int position) {
        mKeyword = mHistoryList.get(position);
        mEditext.setText(mKeyword);
    }
}
