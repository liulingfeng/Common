package com.llf.common.ui.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.dialog.ShareDialog;
import com.llf.common.R;

import java.lang.reflect.Method;

import butterknife.Bind;
import llf.videomodel.VideoPlayer;

/**
 * Created by llf on 2017/3/17.
 * 视频播放
 */

public class VideoDetailActivity extends BaseActivity {
    @Bind(R.id.viewPager)
    VideoPlayer mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    public static void launch(Context context, String url, String title) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        mViewPager.playVideo(intent.getStringExtra("url"), intent.getStringExtra("title"));

        mToolbar.setContentInsetStartWithNavigation(0);
        mToolbar.inflateMenu(R.menu.menu_video_detail);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        showToast("搜索");
                        break;
                    case R.id.love:
                        showToast("喜欢");
                        break;
                    case R.id.share:
                        ShareDialog.show(VideoDetailActivity.this);
                        break;
                    case R.id.report:
                        showToast("举报成功");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.isFullScreen()) {
            mViewPager.setProtrait();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //一定要记得销毁View
        if (mViewPager != null) {
            mViewPager.destroyVideo();
            mViewPager = null;
        }
        super.onDestroy();
    }
}
