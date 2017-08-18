package com.llf.basemodel.commonwidget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by llf on 2017/8/2.
 * 可下拉，上拉回弹的ScrollView
 */

public class CustomScrollView extends ScrollView {
    private View rootView;

    public CustomScrollView(Context context) {
        this(context, null);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setSaveEnabled(true);
    }
    @Override
    protected void onFinishInflate() {
        rootView = getChildAt(0);
        super.onFinishInflate();
    }

    private int mpreY = 0;
    private Rect mNormalRect;//记录原始位置
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float curY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                int delta = (int) ((curY - mpreY) * 0.25);
                if (delta > 0) {
                    rootView.layout(rootView.getLeft(), rootView.getTop() + delta, rootView.getRight(), rootView.getBottom() + delta);
                }
            }
            break;

            case MotionEvent.ACTION_DOWN:
                if (rootView != null)
                    mNormalRect = new Rect(rootView.getLeft(), rootView.getTop(), rootView.getRight(), rootView.getBottom());
                break;

            case MotionEvent.ACTION_UP:
                int curTop = rootView.getTop();
                rootView.layout( mNormalRect.left, mNormalRect.top, mNormalRect.right, mNormalRect.bottom);
                TranslateAnimation animation = new TranslateAnimation(0, 0, curTop - mNormalRect.top, 0);
                animation.setDuration(200);
                rootView.startAnimation(animation);
                break;
        }
        mpreY = (int) curY;
        return super.onTouchEvent(event);
    }
}
