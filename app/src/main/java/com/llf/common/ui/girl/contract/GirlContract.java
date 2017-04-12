package com.llf.common.ui.girl.contract;

import android.content.Context;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;
import com.llf.common.entity.JcodeEntity;

import java.util.List;

/**
 * Created by llf on 2017/3/28.
 */

public interface GirlContract {
    interface View extends BaseView {
        void returnData(List<JcodeEntity> datas);
    }

    interface Presenter extends BasePresenter {
        void loadData(String url);

        void addRecord(Context context, JcodeEntity entity);
    }
}
