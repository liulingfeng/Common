package com.llf.basemodel.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by llf on 2017/2/28.
 * 日志打印工具
 */

public class LogUtil {
    private static boolean isDebug = true; //一次性关闭所有log
    private static final String TAG = "LogUtil";

    public static void init() {
        //改变默认TAG，Logger.t(TAG)可以局部改变TAG
        Logger.init(TAG);
    }

    /**
     * log.i
     *
     * @param msg
     */
    public static void i(String msg) {
        if (isDebug) {
            Logger.i(msg);
        }
    }

    /**
     * log.d
     *
     * @param msg
     */
    public static void d(String msg) {
        if (isDebug) {
            Logger.d(msg);
        }
    }

    /**
     * log.e
     *
     * @param msg
     */
    public static void e(String msg) {
        if (isDebug) {
            Logger.e(msg);
        }
    }

    /**
     * log.w
     *
     * @param msg
     */
    public static void w(String msg) {
        if (isDebug) {
            Logger.w(msg);
        }
    }

    /**
     * log.v
     *
     * @param msg
     */
    public static void v(String msg) {
        if (isDebug) {
            Logger.v(msg);
        }
    }

    /**
     * log.wtf
     *
     * @param msg
     */
    public static void wtf(String msg) {
        if (isDebug) {
            Logger.wtf(msg);
        }
    }

    /**
     * log.json
     *
     * @param msg
     */
    public static void json(String msg) {
        if (isDebug) {
            Logger.json(msg);
        }
    }

    /**
     * log.xml
     *
     * @param msg
     */
    public static void xml(String msg) {
        if (isDebug) {
            Logger.xml(msg);
        }
    }
}
