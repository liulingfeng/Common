package com.llf.common.ui.video.presenter;

import com.llf.basemodel.utils.JsonUtils;
import com.llf.basemodel.utils.OkHttpUtils;
import com.llf.common.entity.VideoEntity;
import com.llf.common.ui.video.contract.VideoContract;


/**
 * Created by llf on 2017/4/11.
 * 浏览器能访问接口，客户端禁止访问是请求头的问题，需要加请求头
 */

public class VideoPresenter implements VideoContract.Presenter{
    private VideoContract.View mView;

    public VideoPresenter(VideoContract.View view){
        this.mView = view;
    }
    @Override
    public void start() {

    }

    @Override
    public void loadData(String url) {
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallback<String>() {
            @Override
           public void onSuccess(String response) {
                VideoEntity entity = JsonUtils.deserialize(response,VideoEntity.class);
                mView.returnData(entity.getTag());
            }

            @Override
            public void onFailure(Exception e) {
                mView.showErrorTip(e.getMessage());
            }
        });
    }
}
