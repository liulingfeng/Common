package com.llf.common.ui.mine;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.commonactivity.WebViewActivity;
import com.llf.basemodel.commonwidget.EmptyLayout;
import com.llf.basemodel.recycleview.DefaultItemDecoration;
import com.llf.common.R;
import com.llf.common.entity.JcodeEntity;
import com.llf.common.ui.girl.adapter.GirlAdapter;
import com.llf.common.ui.mine.contact.TrackContract;
import com.llf.common.ui.mine.presenter.TrackPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by llf on 2017/3/31.
 * 影迹
 */

public class TrackActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, TrackContract.View {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.emptyLayout)
    EmptyLayout mEmptyLayout;

    private GirlAdapter mAdapter;
    private List<JcodeEntity> jcodes = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private TrackPresenter mPresenter;
    private int position;
    private static final String HOST = "http://www.jcodecraeer.com";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_track;
    }

    @Override
    protected void initView() {
        mPresenter = new TrackPresenter(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                jcodes.add(toPosition, jcodes.remove(fromPosition));
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                position = viewHolder.getAdapterPosition();
                mPresenter.deleteData(TrackActivity.this, jcodes.get(position).getTitle());
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(TrackActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(TrackActivity.this));
        mAdapter = new GirlAdapter(jcodes, this);
        mAdapter.setOnItemClickLitener(new GirlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebViewActivity.lanuch(TrackActivity.this, HOST + jcodes.get(position).getDetailUrl());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setRefreshing(true);
        mPresenter.loadData(this);
    }

    @OnClick(R.id.floatBtn)
    public void onViewClicked() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        jcodes.clear();
        mPresenter.loadData(this);
    }

    @Override
    public void showLoading() {
        startProgressDialog();
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        showErrorHint(msg);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void returnData(List<JcodeEntity> datas) {
        if (datas.size() == 0) {
            mEmptyLayout.showEmpty();
        }
        mRefreshLayout.setRefreshing(false);
        jcodes.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void retureResult(boolean result) {
        if (result) {
            jcodes.remove(position);
            mAdapter.notifyItemRemoved(position);
        }
    }
}
