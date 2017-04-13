package com.llf.common.ui.news.contract;

import com.llf.basemodel.base.BaseModel;
import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;
import com.llf.common.entity.NewsEntity;
import com.llf.common.ui.news.NewsModel;

import java.util.List;

/**
 * Created by llf on 2017/3/15.
 * 头条契约类
 */

public interface NewsContract {
    interface View extends BaseView {
        void returnData(List<NewsEntity> datas);
    }

    interface Presenter extends BasePresenter {
        void loadData(int type, int page);
    }

    interface Model extends BaseModel {
        void loadData(String url, int type, NewsModel.OnLoadFirstDataListener listener);
    }
}
