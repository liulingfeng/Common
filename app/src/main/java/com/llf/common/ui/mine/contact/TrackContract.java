package com.llf.common.ui.mine.contact;

import android.content.Context;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;
import com.llf.common.entity.JcodeEntity;

import java.util.List;

/**
 * Created by llf on 2017/3/31.
 */

public interface TrackContract {
    interface View extends BaseView {
        void returnData(List<JcodeEntity> datas);
        void returnResult(boolean result);
    }

    interface Presenter extends BasePresenter {
        void loadData(Context context);
        void deleteData(Context context, String title);
    }
}
