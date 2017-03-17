package com.llf.common.ui.news;

import com.llf.common.entity.NewsEntity;
import java.util.List;

/**
 * Created by llf on 2017/3/15.
 */

public class NewsModel implements NewsContract.Model{
    @Override
    public void loadData(String url, int type, OnLoadFirstDataListener listener) {

    }

    public interface OnLoadFirstDataListener{
        void  onSuccess(List<NewsEntity> list);
        void  onFailure(String str,Exception e);
    }
}
