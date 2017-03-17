package com.llf.common;

import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;

import com.llf.basemodel.base.BaseActivity;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.bottom)
    BottomNavigationView mBottom;

    private long exitTime = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
