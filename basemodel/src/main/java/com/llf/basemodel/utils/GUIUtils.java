package com.llf.basemodel.utils;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by llf on 2017/3/20.
 */

public class GUIUtils {
    public static int getStatusBarHeight(Context c) {
        int result = 0;
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = c.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void showRevealEffect(final View v, int centerX, int centerY, Animator.AnimatorListener lis) {
        v.setVisibility(View.VISIBLE);
        int height = v.getHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(
                    v, centerX, centerY, 0, height);
            anim.setDuration(800);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.addListener(lis);
            anim.start();
        }
    }
}
