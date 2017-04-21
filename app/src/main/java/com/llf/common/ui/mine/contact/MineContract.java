package com.llf.common.ui.mine.contact;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

/**
 * Created by llf on 2017/4/21.
 *
 */

public interface MineContract {
    interface View extends BaseView {
        void retureResult(String result);
    }

    interface Presenter extends BasePresenter {
        void checkUpdate(String url);
    }
}
