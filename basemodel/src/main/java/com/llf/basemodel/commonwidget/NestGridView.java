package com.llf.basemodel.commonwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决滑动冲突的GrideView
 */
public class NestGridView extends GridView{
	/**
	 * @param context
	 * @param attrs
	 */
	public NestGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public NestGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	 @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
             int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                             MeasureSpec.AT_MOST);
             super.onMeasure(widthMeasureSpec, expandSpec);
     }
	 
}

