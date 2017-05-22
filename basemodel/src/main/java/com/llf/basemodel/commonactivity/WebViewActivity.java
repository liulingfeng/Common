package com.llf.basemodel.commonactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.llf.basemodel.R;
import com.llf.basemodel.base.BaseActivity;
import com.llf.basemodel.utils.NetWorkUtil;

/**
 * Created by llf on 2016/10/21.
 * WebView加载网页
 * http://www.jianshu.com/p/d2f5ae6b4927
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    public static void lanuch(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    private WebView mWebView;
    private Toolbar mToolbar;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        url = getIntent().getStringExtra("url");
        mWebView = (WebView) findViewById(R.id.webView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(this);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹出alert
        mWebView.requestFocusFromTouch();//支持获取手势焦点，输入用户名、密码或其他
        if (NetWorkUtil.isNetworkConnected(this)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setSupportZoom(false);  //不支持缩放
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");

        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        //辅助处理请求，点击链接在本browser中打开
        mWebView.setWebViewClient(new WebViewClient() {
            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//等待证书响应
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(WebViewActivity.this, "没有安装微信，请选择其他支付方式", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                startProgressDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url != null && url.contains("http://www.jcodecraeer.com")) {
                    String fun = "javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";
                    view.loadUrl(fun);
                    String fun2 = "javascript:function hideOther() {getClass(document,'header')[0].style.display='none';getClass(document,'footer')[0].style.display='none'}";
                    view.loadUrl(fun2);
                    view.loadUrl("javascript:hideOther()");
                }
                mWebView.setVisibility(View.VISIBLE);
                stopProgressDialog();
            }

        });

        //辅助处理js的对话框，网站图标，网站title，加载进度
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(WebViewActivity.this, message, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mWebView.loadUrl(url);
    }

    /**
     * 默认按返回键会直接退出app
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        //处理长时间执行js动画导致耗电
        mWebView.getSettings().setJavaScriptEnabled(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.clearCache(true);
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
