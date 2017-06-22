package com.llf.common.ui.news.presenter;

import com.llf.basemodel.utils.LogUtil;
import com.llf.basemodel.utils.OkHttpUtils;
import com.llf.common.api.Apis;
import com.llf.common.entity.NewsDetialEntity;
import com.llf.common.tools.NewsJsonUtils;
import com.llf.common.ui.news.contract.NewsDetailContract;

/**
 * Created by llf on 2017/4/13.
 */

public class NewsDetailPresenter implements NewsDetailContract.Presenter {
    private NewsDetailContract.View mView;

    public NewsDetailPresenter(NewsDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadContent(final String s) {
        mView.showLoading();
        String detailUrl = getDetailUrl(s);
        OkHttpUtils.get(detailUrl, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtil.d("新闻详情" + response);
                mView.stopLoading();
                try {
                    NewsDetialEntity newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, s);
                    mView.showContent(newsDetailBean.getBody());
                } catch (NullPointerException e) {
                    LogUtil.d("空指针");
                }
            }

            @Override
            public void onFailure(Exception e) {
                mView.stopLoading();
            }
        });
    }

    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Apis.NEW_DETAIL);
        sb.append(docId).append(Apis.END_DETAIL_URL);
        return sb.toString();
    }
}
