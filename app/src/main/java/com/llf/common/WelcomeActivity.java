package com.llf.common;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import com.llf.basemodel.base.BaseActivity;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by llf on 2017/5/5.
 * 欢迎页
 */

public class WelcomeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    public static final int PERMISSION = 100;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        /**
         * 6.0系统动态权限申请需要
         */
        String[] params = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(WelcomeActivity.this, params)) {
            skip();
        } else {
            EasyPermissions.requestPermissions(WelcomeActivity.this, "应用需要权限才能安全运行", PERMISSION, params);
        }
    }

    private void skip() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startThenKill(MainActivity.class);
                WelcomeActivity.this.overridePendingTransition(R.anim.scale_in, R.anim.shrink_out);
            }
        }, 1000 * 2);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        skip();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case PERMISSION:
                //引导用户跳转到设置界面
                new AppSettingsDialog.Builder(WelcomeActivity.this, "希望您通过权限")
                        .setTitle("权限设置")
                        .setPositiveButton("设置")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setRequestCode(PERMISSION)
                        .build()
                        .show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
