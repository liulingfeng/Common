package com.llf.basemodel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by llf on 2017/3/2.
 * 设置一些基础的东西(1.dp,sp,px转化2.获取手机宽高3.缓存)
 */

public class SettingUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * context.getResources().getDisplayMetrics().density 1dp对应多少px，加0.5是为了四舍五入
     */
    public static int dip2px(Context context, float dpValue) {
        //这个得到的不应该叫做密度，应该是密度的一个比例，在160dpi手机上这个值是1,dpi屏幕像素密度（是基准）
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        //scaledDensity为字体缩放比例
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 缓存相关
     */
    public static void setTagString(Context context, String tag, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("cache", Context.MODE_PRIVATE).edit();
        editor.putString(tag, value);
        editor.commit();
    }

    public static String getTagString(Context context, String tag) {
        SharedPreferences sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        return sp.getString(tag, "");
    }

    /**
     * 默认的缓存文件
     */
    public static void setTagString(Activity activity, String tag, String value) {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(tag, value);
        editor.commit();
    }

    public static String getTagString(Activity activity, String tag) {
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        return sp.getString(tag, "");
    }
}
