package com.llf.common.ui.news.classfi;

import com.llf.basemodel.base.BaseFragment;
import com.llf.common.R;
import com.llf.common.entity.NewsEntity;
import com.llf.common.ui.news.NewsContract;
import com.llf.common.ui.news.NewsFragment;
import com.llf.common.ui.news.NewsPresenter;
import java.util.List;

/**
 * Created by llf on 2017/3/15.
 * 新闻头条
 */

public class TopNewsFragment extends BaseFragment implements NewsContract.View{
    private NewsContract.Presenter mPresenter;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_top_news;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {
        mPresenter = new NewsPresenter(this);
        mPresenter.loadData(NewsFragment.ONE,page);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void returnData(List<NewsEntity> datas) {

    }

}
