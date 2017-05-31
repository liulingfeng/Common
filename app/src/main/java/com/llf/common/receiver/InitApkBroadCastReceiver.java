package com.llf.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.llf.basemodel.utils.LogUtil;

/**
 * Created by llf on 2017/5/24.
 * 安装完成的广播接收器
 */

public class InitApkBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            LogUtil.e("监听到系统广播添加"+intent.getDataString());

        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            LogUtil.e("监听到系统广播移除"+intent.getDataString());
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            LogUtil.e("监听到系统广播替换"+intent.getDataString());
        }
    }
}
