package com.llf.common.ui.video;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.recycleview.BaseAdapter;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.common.App;
import com.llf.common.R;
import com.llf.common.WelcomeActivity;
import com.llf.common.api.Apis;
import com.llf.common.entity.VideoEntity;
import com.llf.common.ui.video.contract.VideoContract;
import com.llf.common.ui.video.presenter.VideoPresenter;
import com.llf.common.widget.YouTubeVideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by llf on 2017/7/5.
 * 类似于YouTuBe的Activity
 */

@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class YouTuBeActivity extends BaseActivity implements TextureView.SurfaceTextureListener, VideoContract.View, YouTubeVideoView.Callback, MediaPlayer.OnPreparedListener {
    private static final String TAG = "YouTuBeActivity";

    @Bind(R.id.video_view)
    TextureView mVideoView;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.youtube_view)
    YouTubeVideoView mYoutubeView;

    private MediaPlayer mMediaPlayer;
    private BaseAdapter mAdapter;
    private List<VideoEntity.V9LG4CHORBean> videos = new ArrayList<>();
    private VideoContract.Presenter mPresenter;
    private int pageIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (App.mAppStatus == -1) {
            startActivity(WelcomeActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_youtube;
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void initView() {
        mVideoView.setSurfaceTextureListener(this);
        mMediaPlayer = MediaPlayer.create(this, R.raw.test_video);
        mMediaPlayer.setOnPreparedListener(this);
        mYoutubeView.setCallback(this);

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
                ImageLoaderUtils.loadingImg(YouTuBeActivity.this, imageView, item.getCover());
                ImageLoaderUtils.loadingImg(YouTuBeActivity.this, circleImageView, item.getTopicImg());
            }
        };
        mAdapter.setOnItemClickLitener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BaseViewHolder viewHolder) {

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
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mMediaPlayer.setSurface(new Surface(surface));
        mMediaPlayer.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        finish();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
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

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.setLooping(true);
    }

    @Override
    public void onVideoViewHide() {
        mMediaPlayer.pause();
    }

    @Override
    public void onVideoClick() {
        if(mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
        else
            mMediaPlayer.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mYoutubeView.getNowStateScale() == 1f){
            mYoutubeView.goMin();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
