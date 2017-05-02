package com.llf.photopicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.utils.FileUtil;
import com.llf.basemodel.utils.LogUtil;

import java.io.File;

/**
 * Created by llf on 2017/3/10
 */

public class PickPhotoActivity extends BaseActivity implements View.OnClickListener,Callback {
    public static void startActivity(Activity activity, ImgSelConfig config, int RequestCode) {
        Intent intent = new Intent(activity, PickPhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", config);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, RequestCode);
    }

    public static void startActivity(Fragment fragment, ImgSelConfig config, int RequestCode) {
        Intent intent = new Intent(fragment.getActivity(), PickPhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", config);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, RequestCode);
    }

    public static final String INTENT_RESULT = "result";
    private static final int IMAGE_CROP_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private ImgSelConfig config;
    private ImageView back;
    private TextView tvRight;
    private String cropImagePath;//存储裁剪图片的位置

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pick_photo;
    }

    @Override
    protected void initView() {
        Constant.imageList.clear();
        config = (ImgSelConfig) getIntent().getSerializableExtra("config");
        back = (ImageView) findViewById(R.id.img_back);
        tvRight = (TextView) findViewById(R.id.tv_right);
        back.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        /**
         * 6.0系统动态权限申请需要
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_REQUEST_CODE);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, PickPhotoFragment.instance(config), null)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_down_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            Constant.imageList.add(cropImagePath);
            exit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_back) {
            finish();
        } else if (id == R.id.tv_right) {
            exit();
        }
    }

    private void exit() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(INTENT_RESULT, Constant.imageList);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void crop(String imagePath) {
        File file = new File(FileUtil.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");
        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.aspectX);
        intent.putExtra("aspectY", config.aspectY);
        intent.putExtra("outputX", config.outputX);
        intent.putExtra("outputY", config.outputY);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    @Override
    public void onSingleImageSelected(String path) {
        LogUtil.e(path);
        if (config.needCrop) {
            crop(path);
        } else {
            Constant.imageList.add(path);
            exit();
        }
    }

    @Override
    public void onImageSelected(String path) {
        tvRight.setText("确定(" + Constant.imageList.size() + "/" + config.maxNum + ")");
    }

    @Override
    public void onImageUnselected(String path) {
        tvRight.setText("确定(" + Constant.imageList.size() + "/" + config.maxNum + ")");
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            if (config.needCrop) {
                crop(imageFile.getAbsolutePath());
            } else {
                Constant.imageList.add(imageFile.getAbsolutePath());
                exit();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.content, PickPhotoFragment.instance(config), null)
                            .commitAllowingStateLoss();
                } else {
                   finish();
                }
                break;
            default:break;
        }
    }
}
