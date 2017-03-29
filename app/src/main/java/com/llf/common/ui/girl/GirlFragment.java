package com.llf.common.ui.girl;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.commonactivity.WebViewActivity;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.recycleview.BaseAdapter;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.recycleview.DefaultItemDecoration;
import com.llf.basemodel.recycleview.EndLessOnScrollListener;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.common.R;
import com.llf.common.entity.JcodeEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by llf on 2017/3/15.
 * 妹妹
 */

public class GirlFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, GirlContract.View {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private BaseAdapter mAdapter;
    private List<JcodeEntity> jcodes = new ArrayList<>();
    private GirlPresenter mPresenter;
    private int pageIndex = 1;
    private static final String HOST = "http://www.jcodecraeer.com";

    public static GirlFragment getInstance() {
        GirlFragment girlFragment = new GirlFragment();
        return girlFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girl;
    }

    @Override
    protected void initView() {
        mPresenter = new GirlPresenter(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(getActivity()));
        mAdapter = new BaseAdapter<JcodeEntity>(getActivity(), R.layout.item_jcode, jcodes) {
            @Override
            public void convert(BaseViewHolder viewHolder, JcodeEntity item) {
                ImageView imageView = viewHolder.getView(R.id.cover);
                viewHolder.setText(R.id.title, item.getTitle());
                viewHolder.setText(R.id.content, item.getContent());
                viewHolder.setText(R.id.author, item.getAuthor());
                viewHolder.setText(R.id.seeNum, item.getWatch());
                viewHolder.setText(R.id.commentNum, item.getComments());
                viewHolder.setText(R.id.loveNum, item.getLike());
                ImageLoaderUtils.loadingImg(getActivity(), imageView, HOST + item.getImgUrl());
                ImageLoaderUtils.loadingImg(getActivity(), (CircleImageView) viewHolder.getView(R.id.avatar), HOST + item.getAuthorImg());
            }
        };
        mAdapter.addFooterView(R.layout.layout_footer);
        mAdapter.setOnItemClickLitener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebViewActivity.lanuch(getActivity(), HOST + jcodes.get(position).getDetailUrl());
            }

            @Override
            public void onItemLongClick(int position) {
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore() {
                pageIndex++;
                mAdapter.setFooterVisible(View.VISIBLE);
                mPresenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);
            }
        });
    }

    @Override
    protected void lazyFetchData() {
        mRefreshLayout.setRefreshing(true);
        mPresenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);
    }

    @OnClick(R.id.floatBtn)
    public void onViewClicked() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        jcodes.clear();
        mPresenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);
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
    public void returnData(List<JcodeEntity> datas) {
        if (pageIndex == 1) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.setFooterVisible(View.GONE);
        }
        jcodes.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }
}
