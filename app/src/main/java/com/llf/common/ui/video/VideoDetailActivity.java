package com.llf.common.ui.video;

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        mViewPager.playVideo("http://ic_main_video_selector.shuihulu.com/TiaoQuVideo/2f84e67a817c4282beb0c7540dcd72d5_1L.mp4", "德玛西亚");
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
