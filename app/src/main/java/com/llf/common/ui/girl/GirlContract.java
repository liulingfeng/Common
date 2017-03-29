package com.llf.common.ui.girl;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;
import com.llf.common.entity.JcodeEntity;

import java.util.List;

/**
 * Created by llf on 2017/3/28.
 */

public class GirlContract {
    interface View extends BaseView {
        void returnData(List<JcodeEntity> datas);
    }

    interface Presenter extends BasePresenter {
        void loadData(String url);
    }
}
