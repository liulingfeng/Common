package com.llf.common.db;

import com.llf.common.entity.NewsEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by llf on 2017/3/22.
 */

public interface IDBManager {
    Observable<Boolean> insertNew(NewsEntity newsEntity);
    Observable<Boolean> deleteNew(String newId);
    Observable<List<NewsEntity>> queryAllNew();
    Observable<List<NewsEntity>> queryNew(String title);
    Observable<Boolean> updateNew(NewsEntity newsEntity);
    Observable<Boolean> exitNew(String newId);
}
