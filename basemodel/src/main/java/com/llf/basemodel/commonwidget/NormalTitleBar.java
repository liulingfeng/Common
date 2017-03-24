package com.llf.basemodel.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.llf.basemodel.R;

/**
 * 通用的头部
 */
public class NormalTitleBar extends RelativeLayout {
    private TextView ivBack,tvTitle, tvRight;

    public NormalTitleBar(Context context) {
        this(context, null);
    }

    public NormalTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public NormalTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NormalTitleBar, defStyleAttr, 0);
        String title = a.getString(R.styleable.NormalTitleBar_content);
        String rightTitle = a.getString(R.styleable.NormalTitleBar_rightContent);
        Boolean leftState = a.getBoolean(R.styleable.NormalTitleBar_leftState,true);
        int color = a.getColor(R.styleable.NormalTitleBar_backgroudColor, Color.parseColor("#F5494C"));
        a.recycle();
        View.inflate(context, R.layout.view_head, this);
        RelativeLayout headBg = (RelativeLayout)findViewById(R.id.head_bg);
        ivBack = (TextView) findViewById(R.id.tv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvTitle.setText(title);
        tvRight.setText(rightTitle);
        ivBack.setVisibility(leftState == true?View.VISIBLE:View.INVISIBLE);
        headBg.setBackgroundColor(color);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
        //view有id才会保存状态
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 管理返回按钮
     */
    public void setBackVisibility(boolean visible) {
        ivBack.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题栏左侧字符串
     * @param tvLeftText
     */
    public void setTvLeft(String tvLeftText){
        ivBack.setText(tvLeftText);
    }

    public void setTitleText(String string) {
        tvTitle.setText(string);
    }

    public void setTitleText(int string) {
        tvTitle.setText(string);
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * 右标题
     */
    public void setRightTitleVisibility(boolean visible) {
        tvRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightTitle(String text) {
        tvRight.setText(text);
    }

    /*
     * 点击事件
     */
    public void setOnBackListener(OnClickListener listener) {
        ivBack.setOnClickListener(listener);
    }

    public void setOnRightTextListener(OnClickListener listener) {
        tvRight.setOnClickListener(listener);
    }
}
