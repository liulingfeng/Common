package com.llf.common.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/29.
 */

public class TranslateBehavior extends FloatingActionButton.Behavior {
    //我们还可以加一个加速器实现弹射效果
    private FastOutLinearInInterpolator folistener=new FastOutLinearInInterpolator();
    public TranslateBehavior(Context context, AttributeSet attr){
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        //开始滑监听---当观察recyclerview开始发生滑动的时候回调
        //nestedScrollAxes滑动关联的方向
        return nestedScrollAxes== ViewGroup.SCROLL_AXIS_VERTICAL||super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }
    //正在滑出来
    boolean isAnimatingOut=false;
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //不断的调用
        //判断滑动的方向 dyConsumed 某个方向的增量
        if(dyConsumed>0&&!isAnimatingOut&&child.getVisibility()==View.VISIBLE){
            //fab划出去
            animateOut(child);
        }else if(dyConsumed<0&&child.getVisibility()!=View.VISIBLE){
            //fab划进来
            animateIn(child);
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }
    //滑进来
    private void animateIn(FloatingActionButton child) {
        child.setVisibility(View.VISIBLE);
        //属性动画
        ViewCompat.animate(child).translationX(0).setInterpolator(folistener).setListener(null).start();
    }
    //滑出去
    private void animateOut(FloatingActionButton child) {
        //属性动画
        //设置监听判断状态
        ViewCompat.animate(child).translationX(child.getHeight()).setInterpolator(folistener).setListener(new ViewPropertyAnimatorListenerAdapter(){
            @Override
            public void onAnimationStart(View view) {
                isAnimatingOut=true;
                super.onAnimationStart(view);
            }

            @Override
            public void onAnimationCancel(View view) {
                isAnimatingOut=false;
                super.onAnimationCancel(view);
            }

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(View.GONE);
                isAnimatingOut=false;
                super.onAnimationEnd(view);
            }
        }).start();
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }
}
