package com.llf.common.ui.mine;

import android.view.View;

import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.dialog.ShareDialog;
import com.llf.common.R;

import butterknife.OnClick;

/**
 * Created by llf on 2017/3/15.
 * 我的
 */

public class MineFragment extends BaseFragment {

    public static MineFragment getInstance() {
        MineFragment mineFragment = new MineFragment();
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick({R.id.setting, R.id.attention, R.id.track, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.attention:
                break;
            case R.id.track:
                startActivity(TrackActivity.class);
                break;
            case R.id.share:
                ShareDialog.show(getActivity());
                break;
            case R.id.night:
                break;
            case R.id.service:
                break;
            case R.id.wallet:
                break;
            case R.id.reply:
                break;
        }
    }
}
