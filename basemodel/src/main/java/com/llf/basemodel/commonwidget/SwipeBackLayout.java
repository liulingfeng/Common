package com.llf.basemodel.commonwidget;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by llf on 2017/3/9.
 * 滑动退出
 */

public class SwipeBackLayout extends LinearLayout {
    private View DragView;
    private ViewDragHelper mViewDragHelper;
    private Point originPlace = new Point();
    private Point mCurArrivePoint = new Point();
    private int width;
    private int mCurEdgeFlag = ViewDragHelper.EDGE_LEFT;
    private OnFinishScroll mFinishScroll;//滚动监听

    public SwipeBackLayout(Context context) {
        this(context, null);
    }


    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

            //这两个方法返回大于0的值才能正常的捕获,用于子控件中有可点击的控件
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                mCurArrivePoint.x = left;
                if (mCurEdgeFlag != ViewDragHelper.EDGE_BOTTOM) {
                    int newLeft = Math.min(width, Math.max(left,0));
                    return newLeft;
                } else return 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                mCurArrivePoint.y = top;
                if (mCurEdgeFlag == ViewDragHelper.EDGE_BOTTOM) {
                    return top;
                } else return 0;
            }

            //绕过tryCaptureView获得拖拽
            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
                    mViewDragHelper.captureChildView(DragView, pointerId);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == DragView) {
                    if (mCurArrivePoint.x < width / 2) {
                        mViewDragHelper.settleCapturedViewAt(originPlace.x, originPlace.y);
                    } else {
                        mViewDragHelper.settleCapturedViewAt(getWidth(), originPlace.y);
                    }
                }
                mCurArrivePoint.x = 0;
                mCurArrivePoint.y = 0;
                invalidate();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);

                switch (mCurEdgeFlag) {
                    case ViewDragHelper.EDGE_LEFT:
                        if (left >= getWidth()) {
                            if (mFinishScroll != null) {
                                mFinishScroll.complete();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    //如果滚动没有结束，刷新View，继续滚动
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        originPlace.x = DragView.getLeft();
        originPlace.y = DragView.getTop();
        width = DragView.getMeasuredWidth();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        DragView = getChildAt(0);
    }

    public void setOnFinishScroll(OnFinishScroll finishScroll) {
        this.mFinishScroll = finishScroll;
    }

    public interface OnFinishScroll {
        void complete();
    }
}
