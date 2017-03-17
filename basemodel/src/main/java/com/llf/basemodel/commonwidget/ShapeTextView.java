package com.llf.basemodel.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.llf.basemodel.R;

/**
 * Created by guof on 2016/8/3.
 */

public class ShapeTextView extends AppCompatTextView {

    public ShapeTextView(Context context) {
        super(context);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    /**
     * 实现自定义圆角背景
     * 支持
     * 1.四边圆角
     * 2.指定边圆角
     * 3.支持填充色以及边框色
     * 4.支持按下效果
     */

    public ShapeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    //填充色
    private int solidColor = 0;
    //边框色
    private int strokeColor = 0;
    //按下填充色
    private int solidTouchColor = 0;
    //按下边框色
    private int strokeTouchColor = 0;
    //边框宽度
    private int strokeWith = 0;
    private int shapeTpe = 0;
    //按下字体色
    private int textTouchColor = 0;
    //字体色
    private int textColor = 0;

    //圆角的半径
    float radius = 0;
    float topLeftRadius = 0;
    float topRightRadius = 0;
    float bottomLeftRadius = 0;
    float bottomRightRadius = 0;


    public void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShapeTextView, 0, 0);

        solidColor = ta.getInteger(R.styleable.ShapeTextView_solidColor, 0x00000000);
        strokeColor = ta.getInteger(R.styleable.ShapeTextView_strokeColor, 0x00000000);

        solidTouchColor = ta.getInteger(R.styleable.ShapeTextView_solidTouchColor, 0x00000000);
        strokeTouchColor = ta.getInteger(R.styleable.ShapeTextView_strokeTouchColor, 0x00000000);
        textTouchColor = ta.getInteger(R.styleable.ShapeTextView_textTouchColor, 0x00000000);
        textColor = getCurrentTextColor();
        strokeWith = ta.getInteger(R.styleable.ShapeTextView_strokeWith, 0);

        radius = ta.getDimension(R.styleable.ShapeTextView_radius, 0);
        topLeftRadius = ta.getDimension(R.styleable.ShapeTextView_topLeftRadius, 0);
        topRightRadius = ta.getDimension(R.styleable.ShapeTextView_topRightRadius, 0);
        bottomLeftRadius = ta.getDimension(R.styleable.ShapeTextView_bottomLeftRadius, 0);
        bottomRightRadius = ta.getDimension(R.styleable.ShapeTextView_bottomRightRadius, 0);
        shapeTpe = ta.getInt(R.styleable.ShapeTextView_shapeTpe, GradientDrawable.RECTANGLE);
        ta.recycle();


        if (strokeColor != 0 && strokeWith > 0) {
            paintStroke = new Paint();
            paintStroke.setColor(strokeColor);
            paintStroke.setStyle(Paint.Style.STROKE);
            paintStroke.setStrokeWidth(strokeWith);
            paintStroke.setAntiAlias(true);
        }

        paint = new Paint();
        paint.setColor(solidColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        if (radius == 0 && shapeTpe == GradientDrawable.RECTANGLE) {
            path = new Path();
            //顺时针绘制 左上 右上 右下 左下
            radii = new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius};
        }
    }

    private final RectF mRect = new RectF();
    //创建一个画笔
    private Paint paintStroke;
    private Paint paint;
    private Path path;
    float[] radii;


    public void setAllColor(int soilColor, int strokeColor, int textColor) {

        this.solidColor = soilColor;
        this.strokeColor = strokeColor;
        this.textColor = textColor;

        if (solidColor != 0)
            paint.setColor(solidColor);

        if (paintStroke != null)
            paintStroke.setColor(strokeColor);

        setTextColor(textColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRect.set(strokeWith, strokeWith, getWidth() - strokeWith, getHeight() - strokeWith);
        if (path != null)
            path.addRoundRect(mRect, radii, Path.Direction.CW);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.set(strokeWith, strokeWith, getWidth() - strokeWith, getHeight() - strokeWith);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRect.set(strokeWith, strokeWith, getWidth() - strokeWith, getHeight() - strokeWith);
        if (shapeTpe == GradientDrawable.RECTANGLE) {
            if (radius == 0) {
                canvas.drawPath(path, paint);
                if (paintStroke != null)
                    canvas.drawPath(path, paintStroke);
            } else {
                canvas.drawRoundRect(mRect, radius, radius, paint);
                if (paintStroke != null)
                    canvas.drawRoundRect(mRect, radius, radius, paintStroke);
            }
        } else {
            canvas.drawOval(mRect, paint);
            if (paintStroke != null)
                canvas.drawOval(mRect, paintStroke);
        }
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (solidTouchColor != 0 || strokeTouchColor != 0 || textTouchColor != 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    drowBackgroud(true);
            } else {
                drowBackgroud(false);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置按下颜色值
     */
    private void drowBackgroud(boolean isTouch) {
        if (isTouch) {
            if (solidTouchColor != 0)
                paint.setColor(solidTouchColor);
            if (paintStroke != null)
                paintStroke.setColor(strokeTouchColor);
            if (textTouchColor != 0)
                setTextColor(textTouchColor);
        } else {
            if (solidColor != 0)
                paint.setColor(solidColor);

            if (paintStroke != null)
                paintStroke.setColor(strokeColor);

            if (textTouchColor != 0)
                setTextColor(textColor);
        }

        invalidate();
    }

}
