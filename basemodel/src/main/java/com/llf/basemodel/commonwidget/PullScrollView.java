package com.llf.basemodel.commonwidget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by llf on 2017/8/2.
 * 带头部的，可下拉，上拉回弹的ScrollView
 */

public class PullScrollView extends ScrollView {
    //底部图片View
    private View mHeaderView;
    //头部图片的初始化位置
    private Rect mHeadInitRect = new Rect();
    //底部View
    private View mContentView;
    //contentView的初始化位置
    private Rect mContentInitRect = new Rect();
    //初始点击位置
    private Point mTouchPoint = new Point();
    /**
     * 阻尼系数
     */
    private static final float SCROLL_RATIO = 0.5f;

    private int mHeaderCurTop, mHeaderCurBottom, mContentTop, mContentBottom;

    private boolean mIsMoving;
    boolean mEnableMoving = false;
    boolean mIsLayout = false;//是否在初始状态

    public PullScrollView(Context context) {
        this(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        mContentView = getChildAt(0);
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mHeaderView == null){
            new NullPointerException("需要设置头部view");
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) event.getY() - mTouchPoint.y;
                deltaY = deltaY > mHeaderView.getHeight() ? mHeaderView.getHeight() : deltaY;
                if (deltaY > 0 && deltaY >= getScrollY() && mIsLayout) {
                    float headerMoveHeight = deltaY * 0.5f * SCROLL_RATIO;
                    mHeaderCurTop = (int) (mHeadInitRect.top + headerMoveHeight);
                    mHeaderCurBottom = (int) (mHeadInitRect.bottom + headerMoveHeight);

                    float contentMoveHeight = deltaY * SCROLL_RATIO;
                    mContentTop = (int) (mContentInitRect.top + contentMoveHeight);
                    mContentBottom = (int) (mContentInitRect.bottom + contentMoveHeight);

                    if (mContentTop <= mHeaderCurBottom) {
                        mHeaderView.layout(mHeadInitRect.left, mHeaderCurTop, mHeadInitRect.right, mHeaderCurBottom);
                        mContentView.layout(mContentInitRect.left, mContentTop, mContentInitRect.right, mContentBottom);
                        mIsMoving = true;
                        mEnableMoving = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //反弹
                if (mIsMoving) {
                    mHeaderView.layout(mHeadInitRect.left, mHeadInitRect.top, mHeadInitRect.right, mHeadInitRect.bottom);
                    TranslateAnimation headAnim = new TranslateAnimation(0, 0, mHeaderCurTop - mHeadInitRect.top, 0);
                    headAnim.setDuration(200);
                    mHeaderView.startAnimation(headAnim);

                    mContentView.layout(mContentInitRect.left, mContentInitRect.top, mContentInitRect.right, mContentInitRect.bottom);
                    TranslateAnimation contentAnim = new TranslateAnimation(0, 0, mContentTop - mContentInitRect.top, 0);
                    contentAnim.setDuration(200);
                    mContentView.startAnimation(contentAnim);
                    mIsMoving = false;
                    mEnableMoving = false;
                }
                break;
        }
        return mEnableMoving || super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(mHeaderView == null){
            new NullPointerException("需要设置头部view");
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //如果当前不是从初始化位置开始滚动的话，就不让用户拖拽
            if (getScrollY() == 0) {
                mIsLayout = true;
            }
            //保存原始位置
            mTouchPoint.set((int) event.getX(), (int) event.getY());
            mHeadInitRect.set(mHeaderView.getLeft(), mHeaderView.getTop(), mHeaderView.getRight(), mHeaderView.getBottom());
            mContentInitRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(), mContentView.getBottom());
            mIsMoving = false;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //如果当前的事件是我们要处理的事件时，比如现在的下拉，这时候，我们就不能让子控件来处理这个事件
            //这里就需要把它截获，不传给子控件，更不能让子控件消费这个事件
            //不然子控件的行为就可能与我们的相冲突
            int deltaY = (int) event.getY() - mTouchPoint.y;
            deltaY = deltaY > mHeaderView.getHeight() ? mHeaderView.getHeight() : deltaY;
            if (deltaY > 0 && deltaY >= getScrollY()) {
                onTouchEvent(event);
                return true;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setmHeaderView(View view) {
        mHeaderView = view;
    }
}
