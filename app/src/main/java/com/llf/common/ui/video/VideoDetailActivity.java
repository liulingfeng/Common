package com.llf.common.ui.video;

import com.llf.basemodel.base.BaseActivity;
import com.llf.common.R;
import com.llf.common.db.NewsDao;
import com.llf.common.entity.NewsEntity;
import butterknife.Bind;
import llf.videomodel.VideoPlayer;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by llf on 2017/3/17.
 * 视频播放
 */

public class VideoDetailActivity extends BaseActivity {
    @Bind(R.id.viewPager)
    VideoPlayer mViewPager;

    private NewsDao mNewsDao;
    private Subscription subscription;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        mViewPager.playVideo("http://ic_main_video_selector.shuihulu.com/TiaoQuVideo/2f84e67a817c4282beb0c7540dcd72d5_1L.mp4", "德玛西亚");

        mNewsDao = new NewsDao(this);

        subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (mNewsDao.insertRecord(new NewsEntity()) != 0) {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean)
                            showToast("添加成功");
                    }
                });
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
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
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
