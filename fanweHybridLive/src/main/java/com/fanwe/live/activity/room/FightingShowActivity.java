package com.fanwe.live.activity.room;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.live.R;
import com.fanwe.live.fragment.LiveContLocalFragment;

public class FightingShowActivity extends BaseTitleActivity {

    private WebView mWebView;
    private String jumpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fighting_show);
        jumpUrl = getIntent().getStringExtra(LiveContLocalFragment.EXTRA_ACTTICKET_URL);
        init();
    }

    private void init() {
        if (null == jumpUrl || "".equals(jumpUrl)) {
            finish();
        }
        mTitle.setMiddleTextTop(getString(R.string.ranking_list));
        mWebView = (WebView) findViewById(R.id.wv_fighting);
        WebSettings webSettings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        mWebView.loadUrl(jumpUrl);
        //设置Web视图
        mWebView.setWebViewClient(new webViewClient());

    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
