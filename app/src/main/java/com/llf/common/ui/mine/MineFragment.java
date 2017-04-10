package com.llf.common.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.dialog.ShareDialog;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.common.R;
import com.llf.photopicker.ImgSelConfig;
import com.llf.photopicker.PickPhotoActivity;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by llf on 2017/3/15.
 * 我的
 */

public class MineFragment extends BaseFragment {
    private static final int CHANGE_AVATAIR = 1;

    @Bind(R.id.avatar)
    CircleImageView mAvatar;

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

    @OnClick({R.id.setting, R.id.attention, R.id.track, R.id.share, R.id.night, R.id.service, R.id.wallet, R.id.reply, R.id.avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.attention:
                startActivity(AttentionActivity.class);
                break;
            case R.id.track:
                startActivity(TrackActivity.class);
                break;
            case R.id.share:
                ShareDialog.show(getActivity());
                break;
            case R.id.night:
                showToast("夜间模式");
                break;
            case R.id.service:
                showToast("客服中心");
                break;
            case R.id.wallet:
                showToast("我的钱包");
                break;
            case R.id.reply:
                showToast("反馈");
                break;
            case R.id.avatar:
                PickPhotoActivity.startActivity(this, new ImgSelConfig.Builder().multiSelect(false).build(), CHANGE_AVATAIR);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_AVATAIR && resultCode == Activity.RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(PickPhotoActivity.INTENT_RESULT);
            ImageLoaderUtils.loadingImg(getActivity(), mAvatar, result.get(0));
        }
//        Luban.get(getActivity())
//                .load(File)                     //传人要压缩的图片
//                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
//                .setCompressListener(new OnCompressListener() { //设置回调
//
//                    @Override
//                    public void onStart() {
//                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
//                    }
//                    @Override
//                    public void onSuccess(File file) {
//                        // TODO 压缩成功后调用，返回压缩后的图片文件
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // TODO 当压缩过去出现问题时调用
//                    }
//                }).launch();    //启动压缩
        super.onActivityResult(requestCode, resultCode, data);
    }
}
