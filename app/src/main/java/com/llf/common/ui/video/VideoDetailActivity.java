package com.llf.common.ui.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.recycleview.BaseAdapter;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.common.R;
import com.llf.common.api.Apis;
import com.llf.common.entity.VideoEntity;
import com.llf.common.ui.video.contract.VideoContract;
import com.llf.common.ui.video.presenter.VideoPresenter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import llf.videomodel.VideoPlayer;

/**
 * Created by llf on 2017/3/17.
 * 视频播放
 */

public class VideoDetailActivity extends BaseActivity implements VideoContract.View {
    @Bind(R.id.viewPager)
    VideoPlayer mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private VideoContract.Presenter mPresenter;
    private BaseAdapter mAdapter;
    private List<VideoEntity.V9LG4CHORBean> videos = new ArrayList<>();
    private int pageIndex = 0;

    public static void launch(Context context, String url, String title, int pageIndex) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("pageIndex", pageIndex);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        pageIndex = intent.getIntExtra("pageIndex", 0);
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
                        showToast("分享");
//                        ShareDialog.show(VideoDetailActivity.this);
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

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new BaseAdapter<VideoEntity.V9LG4CHORBean>(this, R.layout.item_video, videos) {
            @Override
            public void convert(BaseViewHolder viewHolder, VideoEntity.V9LG4CHORBean item) {
                viewHolder.setText(R.id.title, item.getTitle());
                viewHolder.setText(R.id.source, item.getVideosource());
                ImageView imageView = viewHolder.getView(R.id.cover);
                CircleImageView circleImageView = viewHolder.getView(R.id.topicImg);
                ImageLoaderUtils.loadingImg(VideoDetailActivity.this, imageView, item.getCover());
                ImageLoaderUtils.loadingImg(VideoDetailActivity.this, circleImageView, item.getTopicImg());
            }
        };
        mAdapter.setOnItemClickLitener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder viewHolder) {
                mViewPager.playVideo(videos.get(position).getMp4_url(), videos.get(position).getTitle());
            }

            @Override
            public void onItemLongClick(int position) {
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new VideoPresenter(this);
        mPresenter.loadData(Apis.HOST + Apis.Video + Apis.VIDEO_HOT_ID + "/n/" + pageIndex * 10 + "-10.html");
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
                    LogUtil.d(getClass().getSimpleName() + "onMenuOpened...unable to set icons for overflow menu");
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
        videos.clear();
        super.onDestroy();
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
    public void returnData(List<VideoEntity.V9LG4CHORBean> datas) {
        videos.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }
}
