package com.llf.common.ui.news;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.base.BaseFragmentAdapter;
import com.llf.common.R;
import com.llf.common.ui.news.classfi.NewsClassfiFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by llf on 2017/3/15.
 * 新闻
 */

public class NewsFragment extends BaseFragment {
    public static final int ONE = 0;
    public static final int TWO = 1;
    public static final int THREE = 2;
    public static final int FOUR = 3;

    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private String[] titles = {"头条", "NBA", "汽车", "笑话"};
    private BaseFragment[] mFragments;
    private BaseFragmentAdapter mAdapter;

    public static NewsFragment getInstance() {
        NewsFragment newsFragment = new NewsFragment();
        return newsFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        mFragments = new BaseFragment[4];
        mFragments[0] = NewsClassfiFragment.newInstance(ONE);
        mFragments[1] = NewsClassfiFragment.newInstance(TWO);
        mFragments[2] = NewsClassfiFragment.newInstance(THREE);
        mFragments[3] = NewsClassfiFragment.newInstance(FOUR);
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mAdapter = new BaseFragmentAdapter(getChildFragmentManager(), mFragments, titles);
        mViewPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mViewPager);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了");
            }
        });
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
