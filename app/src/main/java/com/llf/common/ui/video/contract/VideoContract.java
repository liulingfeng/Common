package com.llf.common.ui.video.contract;

import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;
import com.llf.common.entity.VideoEntity;
import java.util.List;

/**
 * Created by llf on 2017/4/11.
 *
 */

public interface VideoContract {
    interface View extends BaseView {
        void returnData(List<VideoEntity.V9LG4CHORBean> datas);
    }

    interface Presenter extends BasePresenter {
        void loadData(String url);
    }
}
