package com.llf.basemodel.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2017/3/6.
 * 获取应用信息
 */

public class AppInfoUtil {
    private AppInfoUtil() { }


    /**
     * 获取应用程序信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取应用程序版本名称
     */
    public static String getVersionName(Context context) {
        return null == getPackageInfo(context) ? null : getPackageInfo(context).versionName;
    }


    /**
     * 获取应用程序版本号
     */
    public static int getVersionCode(Context context) {
        return null == getPackageInfo(context) ? null : getPackageInfo(context).versionCode;
    }


    /**
     * 获取应用程序包名
     */
    public static String getPackageName(Context context) {
        return null == getPackageInfo(context) ? null : getPackageInfo(context).packageName;
    }


    /**
     * 判断当前应用程序是否处于后台
     * <pre>需要权限：&lt;uses-permission android:name="android.permission.GET_TASKS" /&gt;  </pre>
     */
    public static boolean isApplicationToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取当前运行的进程名
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    /**
     * 获取当前运行的所有进程名
     */
    public static List<String> getProcessName(Context context, String packageName) {
        List<String> list = new ArrayList<String>();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.processName.startsWith(packageName)) {
                list.add(appProcess.processName);
            }
        }
        return list;
    }


    /**
     * 获取当前运行界面的包名
     */
    public static String getTopPackageName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
        return cn.getPackageName();
    }
}
