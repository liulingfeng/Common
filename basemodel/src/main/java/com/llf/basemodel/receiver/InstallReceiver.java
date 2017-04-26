package com.llf.basemodel.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.llf.basemodel.utils.DownloadUtil;
import com.llf.basemodel.utils.LogUtil;

/**
 * Created by llf on 2016/10/21.
 * 下载完成后广播接收
 */

public class InstallReceiver extends BroadcastReceiver{
    private static String TAG = "InstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            LogUtil.e("收到下载完成的广播");
            //下载失败也会发送下载完成
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadApkId);
        }
    }

    // 安装Apk
    private void installApk(Context context, long downloadApkId) {
        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long id = sp.getLong(DownloadUtil.DOWNLOAD_APK_ID_PREFS, -1L);

        if (downloadApkId == id) {
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //打开一个下载完成的任务
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = manager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                manager.remove(id);
            }
        }
    }
}
