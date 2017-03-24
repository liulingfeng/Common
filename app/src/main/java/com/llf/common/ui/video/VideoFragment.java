package com.llf.common.ui.video;

import com.llf.basemodel.base.BaseFragment;
import com.llf.common.R;

/**
 * Created by llf on 2017/3/15.
 * 视频
 */

public class VideoFragment extends BaseFragment{
    public static VideoFragment getInstance() {
        VideoFragment videoFragment = new VideoFragment();
        return videoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {

    }
}
