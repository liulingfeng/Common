package com.llf.common.ui.news.classfi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.recycleview.BaseAdapter;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.recycleview.DefaultItemDecoration;
import com.llf.basemodel.recycleview.EndLessOnScrollListener;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.common.R;
import com.llf.common.api.Apis;
import com.llf.common.entity.NewsEntity;
import com.llf.common.ui.news.NewsFragment;
import com.llf.common.ui.news.contract.NewsContract;
import com.llf.common.ui.news.presenter.NewsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by llf on 2017/3/15.
 * 新闻头条
 */

public class NewsClassfiFragment extends BaseFragment implements NewsContract.View, SwipeRefreshLayout.OnRefreshListener {
    public static NewsClassfiFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        NewsClassfiFragment fragment = new NewsClassfiFragment();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private BaseAdapter mAdapter;
    private NewsContract.Presenter mPresenter;
    private int pageIndex = 0;
    private int type = NewsFragment.ONE;
    private List<NewsEntity> newDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_classfi;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getArguments().getInt("type");
    }

    @Override
    protected void initView() {
        mPresenter = new NewsPresenter(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(getActivity()));
        mAdapter = new BaseAdapter<NewsEntity>(getActivity(), R.layout.item_news, newDatas) {
            @Override
            public void convert(BaseViewHolder viewHolder, NewsEntity item) {
                ImageView imageView = viewHolder.getView(R.id.ivNews);
                viewHolder.setText(R.id.tvTitle, item.getTitle());
                viewHolder.setText(R.id.tvDesc, item.getDigest());
                ImageLoaderUtils.loadingImg(getActivity(), imageView, item.getImgsrc());
            }
        };
        mAdapter.addFooterView(R.layout.layout_footer);
        mAdapter.setOnItemClickLitener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemLongClick(int position) {
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                pageIndex += Apis.PAZE_SIZE;
                mAdapter.setFooterVisible(View.VISIBLE);
                mPresenter.loadData(type, pageIndex);
            }
        });
    }

    @Override
    protected void lazyFetchData() {
        mRefreshLayout.setRefreshing(true);
        mPresenter.loadData(type, pageIndex);
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
    public void returnData(List<NewsEntity> datas) {
        LogUtil.e(datas.size() + "");
        if (pageIndex == 0) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.setFooterVisible(View.GONE);
        }
        newDatas.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        newDatas.clear();
        mPresenter.loadData(type, pageIndex);
    }
}
