package com.llf.basemodel.recycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.llf.basemodel.utils.SettingUtil;

/**
 * Created by llf on 2017/3/2.
 * 自定义分割线
 * http://www.jianshu.com/p/b46a4ff7c10a
 */

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int dividerHeight;

    public DefaultItemDecoration(Context context) {
        dividerHeight = SettingUtil.dip2px(context, 4);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#F2F2F2"));
    }

    /**
     * 可以实现类似绘制背景的效果，内容在上面
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    /**
     * 以绘制在内容的上面，覆盖内容
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 可以实现类似padding的效果
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.bottom = dividerHeight;//留出画线的距离
    }
}
