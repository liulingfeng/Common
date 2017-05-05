package com.llf.basemodel.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by llf on 2017/3/1.
 * 通用的FragmentAdapter
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    BaseFragment[] fragmentList;
    private String[] mTitles;

    public BaseFragmentAdapter(FragmentManager fm,BaseFragment[] fragmentList) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
    }

    public BaseFragmentAdapter(FragmentManager fm, BaseFragment[] fragmentList, String[] mTitles) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
        this.mTitles = mTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles == null ? "" : mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList[position];
    }

    @Override
    public int getCount() {
        if(fragmentList == null) return 0;
        return fragmentList.length;
    }

    /**
     * 这边并没有创建销毁过程，只创建一次
     * @param container
     * @param position
     * @return
     */
    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragmentList[position];
        fm.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }
}
