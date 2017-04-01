package com.fanwe.live.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.jshandler.LiveJsHandler;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.webview.CustomWebView;
import com.fanwe.library.webview.DefaultWebViewClient;
import com.fanwe.live.R;
import com.umeng.analytics.MobclickAgent;

import org.xutils.http.cookie.DbCookieStore;
import org.xutils.view.annotation.ViewInject;

import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Created by Administrator on 2016/7/9 0009.
 */
public class LiveWebViewActivity extends BaseTitleActivity
{
    // webview 要加载的链接
    public static final String EXTRA_URL = "extra_url";
    //统计的界面名称
    public static final String EXTRA_TITLE = "extra_title";
    //要显示的HTML内容
    public static final String EXTRA_HTML_CONTENT = "extra_html_content";

    private String url;
    private String httmContent;
    private String mEtitle;//统计所需要的界面名称
    @ViewInject(R.id.webview)
    private CustomWebView customWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_edit_data);
        init();
    }

    private void init()
    {
        customWebView.addJavascriptInterface(new LiveJsHandler(this));
        customWebView.getSettings().setBuiltInZoomControls(true);
        getIntentInfo();
        initWebView();
    }

    private void getIntentInfo()
    {
        if (getIntent().hasExtra(EXTRA_URL))
        {
            url = getIntent().getExtras().getString(EXTRA_URL);
        }
        if (getIntent().hasExtra(EXTRA_HTML_CONTENT))
        {
            httmContent = getIntent().getExtras().getString(EXTRA_HTML_CONTENT);
        }
        mEtitle = getIntent().getExtras().getString(EXTRA_TITLE);
    }

    private void initWebView()
    {
        customWebView.setWebViewClientListener(new DefaultWebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {

            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                mTitle.setMiddleTextTop(view.getTitle());
            }
        });

        customWebView.setWebChromeClientListener(new WebChromeClient()
        {

            @Override
            public void onReceivedTitle(WebView view, String title)
            {
                mTitle.setMiddleTextTop(view.getTitle());
            }
        });

        if (!TextUtils.isEmpty(url))
        {
            initCookies();
            customWebView.get(url);
        } else if (!TextUtils.isEmpty(httmContent))
        {
            customWebView.loadData(httmContent, "text/html", "utf-8");
        }
    }

    /**
     * 同步一下cookie
     */
    public void initCookies()
    {
        if (!TextUtils.isEmpty(url))
        {
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();//移除
            try
            {
                URI uri = new URI(url);
                List<HttpCookie> list = DbCookieStore.INSTANCE.get(uri);
                for (HttpCookie httpCookie : list)
                {
                    String name = httpCookie.getName();
                    String value = httpCookie.getValue();
                    String cookieString = name + "=" + value;
                    cookieManager.setCookie(url, cookieString);
                }
            } catch (Exception e)
            {

            }
            CookieSyncManager.getInstance().sync();
        }

    }


    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v)
    {
        if (customWebView.canGoBack())
        {
            customWebView.goBack();
        } else
        {
            finish();
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        customWebView.reload();
    }

    @Override
    public void onBackPressed()
    {
        if (customWebView.canGoBack())
        {
            customWebView.goBack();
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(mEtitle)) {
            MobclickAgent.onPageStart(mEtitle);
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!TextUtils.isEmpty(mEtitle)) {
            MobclickAgent.onPageEnd(mEtitle);
            MobclickAgent.onPause(this);
        }
    }
}
