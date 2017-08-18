package com.llf.common.ui.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.base.BaseFragmentAdapter;
import com.llf.common.R;
import com.llf.common.ui.news.classfi.NewsClassfiFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by llf on 2017/3/15.
 * 新闻
 */

public class NewsFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "NewsFragment";

    public static final int ONE = 0;
    public static final int TWO = 1;
    public static final int THREE = 2;
    public static final int FOUR = 3;

    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    private String[] titles = {"头条", "NBA", "汽车", "笑话"};
    private BaseFragment[] mFragments;
    private BaseFragmentAdapter mAdapter;
    private int currentPosition = 0;

    public static NewsFragment getInstance() {
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(new Bundle());
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
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.toolbar)
    public void onViewClicked() {
        ((NewsClassfiFragment)mFragments[currentPosition]).slideToTop();
    }
}
