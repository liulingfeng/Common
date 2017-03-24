package com.llf.common.db;

import com.llf.common.entity.NewsEntity;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by llf on 2017/3/22.
 * rx数据库操作
 */

public class DBManager implements IDBManager {
    public static DBManager manager = new DBManager();
    private NewsDao dao;

    private DBManager(){

    }
    public DBManager getInstance(){
        return manager;
    }
    @Override
    public Observable<Boolean> insertNew(final NewsEntity newsEntity) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                exitNew(newsEntity.getDocid())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if(!aBoolean){

                                }
                            }
                        });
            }
        }).subscribeOn(Schedulers.io());
        return null;
    }

    @Override
    public Observable<Boolean> deleteNew(String newId) {
        return null;
    }

    @Override
    public Observable<List<NewsEntity>> queryAllNew() {
        return null;
    }

    @Override
    public Observable<List<NewsEntity>> queryNew(String title) {
        return null;
    }

    @Override
    public Observable<Boolean> updateNew(NewsEntity newsEntity) {
        return null;
    }

    @Override
    public Observable<Boolean> exitNew(String newId) {
        return null;
    }
}
