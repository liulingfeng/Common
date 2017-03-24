package com.llf.common;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.llf.basemodel.utils.GUIUtils;
import com.llf.common.ui.video.VideoDetailActivity;

/**
 * Created by llf on 2017/3/20.
 * 弧形动画切换
 */

public class CircleTransfActivity extends Activity implements View.OnClickListener {
    private View revealView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_transf);

        revealView = findViewById(R.id.reveal_view);
        findViewById(R.id.circle).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int cx = (location[0] + (view.getWidth() / 2));
        int cy = location[1] + view.getHeight() / 2;
        hideNavigationStatus();
        GUIUtils.showRevealEffect(revealView, cx, cy, revealAnimationListener);
    }

    private void hideNavigationStatus() {

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }


    Animator.AnimatorListener revealAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            Intent i = new Intent(CircleTransfActivity.this, VideoDetailActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };
}
