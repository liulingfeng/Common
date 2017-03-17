package com.llf.basemodel.image;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.llf.basemodel.R;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.SettingUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2017/3/9.
 * 广告轮播
 */

public class BannelView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "BannelView";

    private Context mContext;

    private ViewPager mViewPager;//实现轮播图的ViewPager

    private TextView mTitle;//标题

    private LinearLayout mIndicatorLayout; // 指示器

    private Handler handler;//每几秒后执行下一张的切换

    private int WHEEL = 100; // 转动

    private int WHEEL_WAIT = 101; // 等待

    private List<View> mViews = new ArrayList<>(); //需要轮播的View，数量为轮播图数量+2

    private ImageView[] mIndicators;    //指示器小圆点

    private boolean isScrolling = false; // 滚动框是否滚动着

    private boolean isCycle = true; // 是否循环，默认为true

    private boolean isWheel = true; // 是否自动轮播，默认为true

    private int delay = 4000; // 默认轮播时间

    private int mCurrentPosition = 0; // 轮播当前位置

    private long releaseTime = 0; // 手指松开、页面不滚动时间，防止手机松开后短时间进行切换

    private ViewPagerAdapter mAdapter;

    private ImageCycleViewListener mImageCycleViewListener;

    private List<Info> infos;//数据集合

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mContext != null && isWheel) {
                long now = System.currentTimeMillis();
                // 检测上一次滑动时间与本次之间是否有触击(手滑动)操作，有的话等待下次轮播
                if (now - releaseTime > delay - 500) {
                    handler.sendEmptyMessage(WHEEL);
                } else {
                    handler.sendEmptyMessage(WHEEL_WAIT);
                }
            }
        }
    };

    public BannelView(Context context) {
        this(context, null);
    }

    public BannelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_bannel, this, true);
        mViewPager = (ViewPager) findViewById(R.id.viewPage);
        mTitle = (TextView) findViewById(R.id.cycle_title);
        mIndicatorLayout = (LinearLayout) findViewById(R.id.bannel_indicator);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == WHEEL && mViews.size() > 0) {
                    if (!isScrolling) {
                        //当前为非滚动状态，切换到下一页
                        int posttion = (mCurrentPosition + 1) % mViews.size();
                        mViewPager.setCurrentItem(posttion, true);
                    }
                    releaseTime = System.currentTimeMillis();
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, delay);
                    return;
                }
                if (msg.what == WHEEL_WAIT && mViews.size() > 0) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, delay);
                }
            }
        };
    }

    public void setData(List<Info> list, ImageCycleViewListener listener) {
        setData(list, listener, 0);
    }

    /**
     * 初始化viewpager
     *
     * @param list         要显示的数据
     * @param showPosition 默认显示位置
     */
    public void setData(List<Info> list, ImageCycleViewListener listener,
                        int showPosition) {
        if (list == null || list.size() == 0) {
            //没有数据时隐藏整个布局
            this.setVisibility(View.GONE);
            return;
        }
        mViews.clear();
        infos = list;
        if (isCycle) {
            //添加轮播图View，数量为集合数+2
            // 将最后一个View添加进来
            mViews.add(getImageView(mContext, infos.get(infos.size() - 1).getUrl()));
            for (int i = 0; i < infos.size(); i++) {
                mViews.add(getImageView(mContext, infos.get(i).getUrl()));
            }
            // 将第一个View添加进来
            mViews.add(getImageView(mContext, infos.get(0).getUrl()));
        } else {
            //只添加对应数量的View
            for (int i = 0; i < infos.size(); i++) {
                mViews.add(getImageView(mContext, infos.get(i).getUrl()));
            }
        }

        mImageCycleViewListener = listener;
        int ivSize = mViews.size();
        // 设置指示器
        mIndicators = new ImageView[ivSize];
        if (isCycle)
            mIndicators = new ImageView[ivSize - 2];
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < mIndicators.length; i++) {
            mIndicators[i] = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(SettingUtil.dip2px(mContext, 10), 0, SettingUtil.dip2px(mContext, 10), 0);
            mIndicators[i].setLayoutParams(lp);
            mIndicators[i].setBackgroundResource(R.drawable.selector_guide_bg);
            mIndicators[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            mIndicatorLayout.addView(mIndicators[i]);
        }
        mAdapter = new ViewPagerAdapter();
        // 默认指向第一项，下方viewPager.setCurrentItem将触发重新计算指示器指向
        setIndicator(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
        if (showPosition < 0 || showPosition >= mViews.size())
            showPosition = 0;
        if (isCycle) {
            showPosition = showPosition + 1;
        }
        mViewPager.setCurrentItem(showPosition);
        setWheel(true);//设置轮播
    }

    /**
     * 获取轮播图View
     *
     * @param context
     * @param url
     */
    private View getImageView(Context context, String url) {
        RelativeLayout rl = new RelativeLayout(context);
        //添加一个ImageView，并加载图片
        ImageView imageView = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParams);
        ImageLoaderUtils.loadingImg(context,imageView,url);
        //在Imageview前添加一个半透明的黑色背景，防止文字和图片混在一起
        ImageView backGround = new ImageView(context);
        backGround.setLayoutParams(layoutParams);
        backGround.setBackgroundColor(Color.parseColor("#44222222"));
        rl.addView(imageView);
        rl.addView(backGround);
        return rl;
    }

    /**
     * 设置指示器
     *
     * @param selectedPosition 默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        setText(mTitle, infos.get(selectedPosition).getTitle());
        try {
            for (int i = 0; i < mIndicators.length; i++) {
                mIndicators[i].setSelected(false);
            }
            if (mIndicators.length > selectedPosition)
                mIndicators[selectedPosition].setSelected(true);
        } catch (Exception e) {
            Log.i(TAG, "指示器路径不正确");
        }
    }

    /**
     * 页面适配器 返回对应的view
     */
    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            View v = mViews.get(position);
            if (mImageCycleViewListener != null) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImageCycleViewListener.onImageClick(infos.get(mCurrentPosition - 1));
                    }
                });
            }
            container.addView(v);
            return v;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onPageScrolled(
            int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int arg0) {
        int max = mViews.size() - 1;
        int position = arg0;
        mCurrentPosition = arg0;
        if (isCycle) {
            if (arg0 == 0) {
                //滚动到mView的1个（界面上的最后一个），将mCurrentPosition设置为max - 1
                mCurrentPosition = max - 1;
            } else if (arg0 == max) {
                //滚动到mView的最后一个（界面上的第一个），将mCurrentPosition设置为1
                mCurrentPosition = 1;
            }
            position = mCurrentPosition - 1;
        }
        setIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) { // viewPager在滚动
            isScrolling = true;
            return;
        } else if (state == 0) { // viewPager滚动结束
            releaseTime = System.currentTimeMillis();
            //跳转到第mCurrentPosition个页面（没有动画效果，实际效果页面上没变化）
            mViewPager.setCurrentItem(mCurrentPosition, false);
        }
        isScrolling = false;
    }

    /**
     * 为textview设置文字
     *
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, String text) {
        if (text != null && textView != null) textView.setText(text);
    }

    /**
     * 是否循环，默认开启。必须在setData前调用
     *
     * @param isCycle 是否循环
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 是否处于循环状态
     *
     * @return
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * 设置是否自动轮播，默认自动轮播,轮播一定是循环的
     *
     * @param isWheel
     */
    public void setWheel(boolean isWheel) {
        this.isWheel = isWheel;
        isCycle = true;
        if (isWheel) {
            handler.postDelayed(runnable, delay);
        }
    }

    /**
     * 刷新数据，当外部视图更新后，通知刷新数据
     */
    public void refreshData() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    /**
     * 是否处于轮播状态
     *
     * @return
     */
    public boolean isWheel() {
        return isWheel;
    }

    /**
     * 设置轮播暂停时间,单位毫秒（默认4000毫秒）
     *
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param info
         */
        void onImageClick(Info info);
    }
}
