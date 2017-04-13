package com.llf.common.ui.news.contract;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

/**
 * Created by Administrator on 2017/4/13.
 */

public interface NewsDetailContract {
    interface View extends BaseView {
        void showContent(String s);
    }

    interface Presenter extends BasePresenter {
        void loadContent(String s);
    }
}
