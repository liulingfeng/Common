package com.llf.basemodel.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.llf.basemodel.R;
import com.llf.basemodel.dialog.DialogTools;
import com.llf.basemodel.utils.AppManager;
import com.llf.basemodel.utils.ToastUtil;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by llf on 2017/3/1.
 * 基础的Activity
 */

public abstract class BaseActivity extends AppCompatActivity {
    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        AppManager.instance.addActivity(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        this.initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //当模式为singleTop和SingleInstance会回调到这里
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.instance.removeActivity(this);
        ButterKnife.unbind(this);
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
        BaseApplication.getRefWatcher(this).watch(this);
    }

    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转界面并关闭当前界面
     *
     * @param clazz 目标Activity
     */
    protected void startThenKill(Class<?> clazz) {
        startThenKill(clazz, null);
    }

    /**
     * @param clazz  目标Activity
     * @param bundle 数据
     */
    protected void startThenKill(Class<?> clazz, Bundle bundle) {
        startActivity(clazz, bundle);
        finish();
    }

    /**
     * 5.0以上系统状态栏透明,国产手机默认透明
     */
    protected void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 读取状态栏的高度
     * @param context
     * @return
     */

    protected int getStatusBarHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
        } else {
            return 0;
        }
    }
    /**
     * 开启加载效果
     */
    public void startProgressDialog() {
        DialogTools.showWaittingDialog(this);
    }

    /**
     * 关闭加载
     */
    public void stopProgressDialog() {
        DialogTools.closeWaittingDialog();
    }

    /**
     * 显示错误的dialog
     */
    public void showErrorTip(String errorContent) {
        View errorView = LayoutInflater.from(this).inflate(R.layout.app_error_tip, null);
        TextView tvContent = (TextView) errorView.findViewById(R.id.content);
        tvContent.setText(errorContent);
        new ToastUtil(errorView);
    }

    /**
     * 显示普通的toast
     *
     * @return
     */
    public void showToast(String str) {
        ToastUtil.sToastUtil.shortDuration(str).setToastBackground(Color.WHITE, R.drawable.toast_radius).show();
    }

    //获取布局
    protected abstract int getLayoutId();

    //初始化布局和监听
    protected abstract void initView();
}
