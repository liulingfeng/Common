package com.llf.common.ui.mine.presenter;

import android.content.Context;
import com.llf.common.db.JcodeDao;
import com.llf.common.entity.JcodeEntity;
import com.llf.common.ui.mine.contact.TrackContract;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by llf on 2017/3/31.
 *
 */

public class TrackPresenter implements TrackContract.Presenter {
    private TrackContract.View mView;
    private JcodeDao mJcodeDao;

    public TrackPresenter(TrackContract.View view) {
        this.mView = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadData(Context context) {
        mJcodeDao = new JcodeDao(context);
        Observable.create(new Observable.OnSubscribe<List<JcodeEntity>>() {
            @Override
            public void call(Subscriber<? super List<JcodeEntity>> subscriber) {
                subscriber.onNext(mJcodeDao.queryRecords());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<JcodeEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showErrorTip(e.getMessage());
                    }

                    @Override
                    public void onNext(List<JcodeEntity> jcodeEntities) {
                        mView.returnData(jcodeEntities);
                    }
                });
    }

    @Override
    public void deleteData(Context context, final String title) {
        mJcodeDao = new JcodeDao(context);
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(mJcodeDao.deleteRecord(title) == -1 ? false : true);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showErrorTip(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean result) {
                        mView.retureResult(result);
                    }
                });
    }
}
