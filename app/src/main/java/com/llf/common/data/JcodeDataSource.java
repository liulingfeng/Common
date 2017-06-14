package com.llf.common.data;

import android.support.annotation.NonNull;

import com.llf.common.entity.JcodeEntity;

import java.util.List;

/**
 * Created by llf on 2017/6/14.
 */

public interface JcodeDataSource {
    interface LoadJcodesCallback {
        void onTasksLoaded(List<JcodeEntity> datas);

        void onDataNotAvailable();
    }

    interface GetJcodeCallback {
        void onTaskLoaded(JcodeEntity entity);

        void onDataNotAvailable();
    }

    long saveJcode(@NonNull JcodeEntity entity);

    void deleteAllJcodes();

    int deleteJcode(@NonNull String title);

    void getJcodes(@NonNull LoadJcodesCallback callback);

    void getJcode(@NonNull String title, @NonNull GetJcodeCallback callback);


    void refreshJcodes(@NonNull JcodeEntity entity);
}
