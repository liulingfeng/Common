package com.llf.common.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.llf.basemodel.utils.LogUtil;

import java.io.File;

/**
 * Created by llf on 2016/10/21.
 * 下载完成后广播接收
 */

public class InstallReceiver extends BroadcastReceiver {
    private static String TAG = "InstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            LogUtil.d(TAG + "收到下载完成的广播");
            //下载失败也会发送下载完成
//            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            installApk(context, downloadApkId);
            install(context);
        }
    }

    // 安装Apk
//    private void installApk(Context context, long downloadApkId) {
//        // 获取存储ID
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        long id = sp.getLong(DownloadUtil.DOWNLOAD_APK_ID_PREFS, -1L);
//
//        if (downloadApkId == id) {
//            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            //打开一个下载完成的任务
//            Intent install = new Intent(Intent.ACTION_VIEW);
//            Uri downloadFileUri = manager.getUriForDownloadedFile(downloadApkId);
//            if (downloadFileUri != null) {
//                try {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    } else {
//                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    }
//                    install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
//                    context.startActivity(install);
//                } catch (Exception e) {
//                    LogUtil.e(TAG + "没有这个Activity");
//                }
//            } else {
//                manager.remove(id);
//            }
//        }
//    }


    /**
     * 通过隐式意图调用系统安装程序安装APK
     * 适配了7.0
     */
    public static void install(Context context) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "xiuqu.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.llf.common.provider666", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}
