package com.llf.common.ui.news.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.common.App;
import com.llf.common.R;
import com.llf.common.WelcomeActivity;
import com.llf.common.entity.NewsEntity;
import com.llf.common.ui.news.contract.NewsDetailContract;
import com.llf.common.ui.news.presenter.NewsDetailPresenter;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.Bind;

/**
 * Created by llf on 2017/3/24.
 * 新闻详情
 */

public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View {
    private static final String TAG = "NewsDetailActivity";

    public static void Launch(Activity mContext, BaseViewHolder holder, NewsEntity entity) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra("news", entity);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
                holder.getView(R.id.ivNews), mContext.getString(R.string.transition_news_img));
        ActivityCompat.startActivity(mContext, intent, options.toBundle());
    }

    @Bind(R.id.ivImage)
    ImageView mIvImage;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.htNewsContent)
    HtmlTextView mHtNewsContent;

    private NewsEntity mEntity;//详情数据
    private NewsDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (App.mAppStatus == -1) {
            startActivity(WelcomeActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView() {
        mPresenter = new NewsDetailPresenter(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
        mEntity = (NewsEntity) getIntent().getSerializableExtra("news");
        mCollapsingToolbar.setTitle(mEntity.getTitle());
        ImageLoaderUtils.loadingImg(getApplicationContext(), mIvImage, mEntity.getImgsrc());
        mPresenter.loadContent(mEntity.getDocid());
    }

    /**
     * 在singleTop模式下会回调
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void showLoading() {
        startProgressDialog();
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        showErrorHint(msg);
    }

    @Override
    public void showContent(String s) {
        try {
            mHtNewsContent.setHtmlFromString(s, new HtmlTextView.LocalImageGetter());
        } catch (Exception e) {
            LogUtil.d("数据为空");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
