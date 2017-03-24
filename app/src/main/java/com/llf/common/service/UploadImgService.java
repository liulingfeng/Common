package com.llf.common.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by llf on 2017/3/24.
 * 上传图片的service
 */

public class UploadImgService extends IntentService {
    private static final String ACTION_UPLOAD_IMG = "com.llf.basemodel.intentservice.action.UPLOAD_IMAGE";
    public static final String EXTRA_IMG_PATH = "com.llf.basemodel.intentservice.extra.IMG_PATH";

    public static void startUploadImg(Context context, String path) {
        Intent intent = new Intent(context, UploadImgService.class);
        intent.setAction(ACTION_UPLOAD_IMG);
        intent.putExtra(EXTRA_IMG_PATH, path);
        context.startService(intent);
    }

    public UploadImgService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final String action = intent.getAction();
        if (ACTION_UPLOAD_IMG.equals(action)) {
            final String path = intent.getStringExtra(EXTRA_IMG_PATH);
            handleUploadImg(path);
        }
    }

    private void handleUploadImg(String path) {
       //上传图片
//            Intent intent = new Intent(IntentServiceActivity.UPLOAD_RESULT);
//            intent.putExtra(EXTRA_IMG_PATH, path);
//            sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
