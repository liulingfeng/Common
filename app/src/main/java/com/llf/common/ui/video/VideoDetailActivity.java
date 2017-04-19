package com.llf.common.ui.video;

import android.content.Context;
import android.content.Intent;

import com.llf.basemodel.base.BaseActivity;
import com.llf.common.R;

import butterknife.Bind;
import llf.videomodel.VideoPlayer;

/**
 * Created by llf on 2017/3/17.
 * 视频播放
 */

public class VideoDetailActivity extends BaseActivity {
    @Bind(R.id.viewPager)
    VideoPlayer mViewPager;

    public static void launch(Context context,String url, String title){
        Intent intent = new Intent(context,VideoDetailActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
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
