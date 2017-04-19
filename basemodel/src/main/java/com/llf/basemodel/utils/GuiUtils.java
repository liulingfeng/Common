package com.llf.basemodel.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by llf on 2017/4/6.
 *
 */

public class GuiUtils {
    // 圆圈爆炸效果显示
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealShow(
            final Context context, final View view,
            final int startRadius, @ColorRes final int color,
            final OnRevealAnimationListener listener) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());

        // 设置圆形显示动画
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, finalRadius);
        anim.setDuration(600);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.VISIBLE);
                listener.onRevealShow();
            }

            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setBackgroundColor(ContextCompat.getColor(context, color));
            }
        });

        anim.start();
    }

    // 圆圈凝聚效果
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealHide(
            final Context context, final View view,
            final int finalRadius, @ColorRes final int color,
            final OnRevealAnimationListener listener
    ) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        int initialRadius = view.getWidth();
        // 与入场动画的区别就是圆圈起始和终止的半径相反
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
        anim.setDuration(600);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setBackgroundColor(ContextCompat.getColor(context, color));
            }

            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.onRevealHide();
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    public interface OnRevealAnimationListener {
        void onRevealHide();
        void onRevealShow();
    }
}
