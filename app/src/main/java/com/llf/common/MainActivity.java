package com.llf.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.base.BaseFragmentAdapter;
import com.llf.basemodel.dialog.UpdateDialog;
import com.llf.basemodel.utils.AppInfoUtil;
import com.llf.basemodel.utils.LogUtil;
import com.llf.common.entity.ApplicationEntity;
import com.llf.common.ui.girl.GirlFragment;
import com.llf.common.ui.mine.MineFragment;
import com.llf.common.ui.news.NewsFragment;
import com.llf.common.ui.video.VideoFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, MainContract.View {
    private static final String TAG = "MainActivity";

    @Bind(R.id.journalism)
    Button mNews;
    @Bind(R.id.video)
    Button mVideo;
    @Bind(R.id.girl)
    Button mGirl;
    @Bind(R.id.mine)
    Button mMine;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    private String[] mTitles;
    private BaseFragment[] fragments;
    int currentTabPosition = 0;
    public static final String CURRENT_TAB_POSITION = "HOME_CURRENT_TAB_POSITION";
    private MainContract.Presenter mPresenter;

    static {
        //vector支持selector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentTabPosition = savedInstanceState.getInt(CURRENT_TAB_POSITION);
            mViewPager.setCurrentItem(currentTabPosition);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mPresenter = new MainPresenter(this);
        mTitles = getResources().getStringArray(R.array.main_titles);
        fragments = new BaseFragment[mTitles.length];
        fragments[0] = NewsFragment.getInstance();
        fragments[1] = VideoFragment.getInstance();
        fragments[2] = GirlFragment.getInstance();
        fragments[3] = MineFragment.getInstance();
        BaseFragmentAdapter mAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mNews.setSelected(true);
        mPresenter.checkUpdate("http://api.fir.im/apps/latest/58f87d50959d6904280005a3?api_token=9f2408863ff25abccca986e5d4d9d6ba");
    }


    /**
     * 存储瞬间的UI状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //奔溃前保存位置，方法执行在onStop之前
        outState.putInt(CURRENT_TAB_POSITION, currentTabPosition);
        super.onSaveInstanceState(outState);
    }

    /**
     * 这个方法在onStart()之后
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //仅当activity为task根才生效
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //分发到fragment的onActivityResult，用于解决qq分享接收不到回调
        BaseFragment fragment = fragments[3];
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 用于存储持久化数据
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            //释放资源
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Empty
    }

    @Override
    public void onPageSelected(int position) {
        resetTab();
        switch (position) {
            case 0:
                mNews.setSelected(true);
                break;
            case 1:
                mVideo.setSelected(true);
                break;
            case 2:
                mGirl.setSelected(true);
                break;
            case 3:
                mMine.setSelected(true);
                break;
            default:
                //其他
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Empty
    }

    @OnClick({R.id.journalism, R.id.video, R.id.girl, R.id.mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.journalism:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.video:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.girl:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.mine:
                mViewPager.setCurrentItem(3, false);
                break;
            default:
                break;
        }
    }

    private void resetTab() {
        mNews.setSelected(false);
        mVideo.setSelected(false);
        mGirl.setSelected(false);
        mMine.setSelected(false);
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
    public void showErrorTip(String msg) {
        showErrorHint(msg);
    }

    @Override
    public void returnResult(String result) {
        showToast(result);
    }

    @Override
    public void returnUpdateResult(final ApplicationEntity entity) {
        try {
            if (AppInfoUtil.getVersionCode(App.instance) < Integer.parseInt(entity.getVersion())) {
                String content = String.format("最新版本：%1$s\napp名字：%2$s\n\n更新内容\n%3$s", entity.getVersionShort(), entity.getName(), entity.getChangelog());
                UpdateDialog.show(MainActivity.this, content, new UpdateDialog.OnUpdate() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void ok() {
                        mPresenter.update(entity);
                    }
                });
            }
        } catch (Exception e) {
            LogUtil.d(TAG + "数字转化出错");
        }
    }
}
