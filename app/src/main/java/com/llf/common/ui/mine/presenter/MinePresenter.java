package com.llf.common.ui.mine.presenter;

import com.llf.basemodel.utils.AppInfoUtil;
import com.llf.basemodel.utils.DownloadUtil;
import com.llf.basemodel.utils.JsonUtils;
import com.llf.basemodel.utils.OkHttpUtils;
import com.llf.common.App;
import com.llf.common.entity.ApplicationEntity;
import com.llf.common.ui.mine.contact.MineContract;

/**
 * Created by llf on 2017/4/21.
 */

public class MinePresenter implements MineContract.Presenter {
    private MineContract.View mView;

    public MinePresenter(MineContract.View view) {
        this.mView = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void checkUpdate(String url) {
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                ApplicationEntity entity = JsonUtils.deserialize(response, ApplicationEntity.class);
                if (AppInfoUtil.getVersionCode(App.instance) < Integer.parseInt(entity.getVersion())) {
                    DownloadUtil.downloadApk(App.instance, entity.getInstall_url(), entity.getName(), entity.getChangelog(), "xiuqu.apk");
                } else {
                    mView.returnResult("当前已是最新版本");
                }
            }

            @Override
            public void onFailure(Exception e) {
                mView.returnResult(e.getMessage());
            }
        });
    }
}
