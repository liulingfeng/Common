package com.llf.common;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;
import com.llf.common.entity.ApplicationEntity;

/**
 * Created by llf on 2017/5/9.
 */

public interface MainContract {
    interface View extends BaseView {
        void retureResult(String result);
        void retureUpdateResult(ApplicationEntity entity);
    }

    interface Presenter extends BasePresenter {
        void checkUpdate(String url);
        void update(ApplicationEntity entity);
    }
}
