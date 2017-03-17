package com.llf.basemodel.base;

/**
 * Created by llf on 2017/3/1.
 * mvp基础View
 */

public interface BaseView {
    void showLoading();
    void stopLoading();
    void showErrorTip(String msg);
}
