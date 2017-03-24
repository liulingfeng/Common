package com.llf.common.ui.girl;

import com.llf.basemodel.base.BaseFragment;
import com.llf.common.R;

/**
 * Created by llf on 2017/3/15.
 * 妹妹
 */

public class GirlFragment extends BaseFragment{
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

    }

    @Override
    protected void lazyFetchData() {

    }
}
