package com.llf.common;

import android.os.Build;
import android.os.StrictMode;

import com.amitshekhar.DebugDB;
import com.llf.basemodel.base.BaseApplication;
import com.llf.basemodel.utils.AppInfoUtil;

/**
 * Created by llf on 2017/3/10.
 */

public class App extends BaseApplication {
    //用于判断应用是否被强杀
    public static int mAppStatus = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 解决7.0无法使用file://格式的URI的第二种方法
         */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        if (AppInfoUtil.isApkInDebug(this)) {
            DebugDB.getAddressLog();
        }
    }

    /**
     * 内存紧张时会走这个方法
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //1.清理内存中的图片2.清理掉Activity只保留Root Activity
        switch (level) {
            case TRIM_MEMORY_COMPLETE:
                //表示 App 退出到后台，并且已经处于 LRU List 比较考靠前的位置
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL:
                //表示 App 正在正常运行，但是系统已经开始根据 LRU List 的缓存规则杀掉了一部分缓存的进程
                break;
        }
    }
}
