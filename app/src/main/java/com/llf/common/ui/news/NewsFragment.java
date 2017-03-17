package com.llf.common.ui.news;

import com.llf.basemodel.base.BaseFragment;
import com.llf.common.R;

/**
 * Created by llf on 2017/3/15.
 * 新闻
 */

public class NewsFragment extends BaseFragment{
    public static final int ONE=0;
    public static final int TWO=1;
    public static final int THREE=2;
    public static final int FOUR=3;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {

    }
}
