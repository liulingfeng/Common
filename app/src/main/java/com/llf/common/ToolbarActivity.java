package com.llf.common;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.llf.basemodel.base.BaseActivity;

import butterknife.Bind;

import static com.llf.common.R.id.toolbar;

/**
 * Created by llf on 2017/3/2.
 */

public class ToolbarActivity extends BaseActivity{
    @Bind(toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_toolbar;
    }

    @Override
    protected void initView() {
        mToolbar.setContentInsetStartWithNavigation(0);
        mToolbar.inflateMenu(R.menu.menu_search);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        showToast("搜索");
                        break;
                    case R.id.rank:
                        showToast("排列");
                        break;
                    case R.id.clear:
                        showToast("清空");
                        break;
                    case R.id.report:
                        showToast("举报");
                        break;
                }
                return false;
            }
        });
    }

}
