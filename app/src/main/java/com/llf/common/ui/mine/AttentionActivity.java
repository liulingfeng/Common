package com.llf.common.ui.mine;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.llf.basemodel.utils.GuiUtils;
import com.llf.common.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by llf on 2017/4/6.
 * 关注
 */

public class AttentionActivity extends AppCompatActivity {
    private static final String TAG = "AttentionActivity";

    @Bind(R.id.container)
    RelativeLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);

        mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    animateRevealShow();
            }
        });
    }

    // 动画展示
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        GuiUtils.animateRevealShow(
                this, mContainer,
                0, R.color.colorPrimary,
                new GuiUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {

                    }

                    @Override
                    public void onRevealShow() {

                    }
                });
    }

    //动画消失
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealHide() {
        GuiUtils.animateRevealHide(
                this, mContainer,
                0, R.color.colorPrimary,
                new GuiUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {
                        defaultBackPressed();
                    }

                    @Override
                    public void onRevealShow() {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            animateRevealHide();
    }

    // 默认回退
    private void defaultBackPressed() {
        super.onBackPressed();
    }
}
