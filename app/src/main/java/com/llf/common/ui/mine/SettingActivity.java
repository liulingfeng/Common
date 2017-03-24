package com.llf.common.ui.mine;

import android.support.v7.widget.Toolbar;

import com.llf.basemodel.base.BaseActivity;
import com.llf.common.R;

import butterknife.Bind;

/**
 * Created by llf on 2017/3/14.
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        mToolbar.setContentInsetStartWithNavigation(0);
    }
}
