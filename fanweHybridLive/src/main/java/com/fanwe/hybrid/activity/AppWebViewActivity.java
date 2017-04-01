package com.fanwe.hybrid.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.event.EFinshiActivity;
import com.fanwe.hybrid.jshandler.AppJsHandler;
import com.fanwe.library.webview.CustomWebView;
import com.fanwe.library.webview.DefaultWebViewClient;
import com.fanwe.live.R;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-25 上午10:27:09 类说明
 */
public class AppWebViewActivity extends BaseTitleActivity
{
    public static final String EXTRA_CODE = "open_url_type";
    /**
     * webview 要加载的链接
     */
    public static final String EXTRA_URL = "extra_url";
    /**
     * 统计所需要的界面名称
     */
    public static final String EXTRA_TITLE = "extra_title";
    /**
     * 要显示的HTML内容
     */
    public static final String EXTRA_HTML_CONTENT = "extra_html_content";

    public static final String EXTRA_FINISH_TO_MAIN = "extra_finish_to_mai";

    /**
     * (boolean)
     */
    public static final String EXTRA_IS_SCALE_TO_SHOW_ALL = "extra_is_scale_to_show_all";

    private int mCurrentExtraCode;
    private boolean isScaleToShowAll;
    private String mUrl;
    private String mHttmContent;
    private String mEtitle;
    @ViewInject(R.id.webview)
    private CustomWebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_app_webview);
        x.view().inject(this);
        init();
    }

    private void init()
    {
        getIntentInfo();
        initWebView();
    }

    private void getIntentInfo()
    {
        if (getIntent().hasExtra(EXTRA_FINISH_TO_MAIN))
        {
            getIntent().getExtras().getBoolean(EXTRA_FINISH_TO_MAIN);
        }

        if (getIntent().hasExtra(EXTRA_CODE))
        {
            mCurrentExtraCode = getIntent().getExtras().getInt(EXTRA_CODE);
        }

        mUrl = getIntent().getExtras().getString(EXTRA_URL);
        mHttmContent = getIntent().getExtras().getString(EXTRA_HTML_CONTENT);
        isScaleToShowAll = getIntent().getBooleanExtra(EXTRA_IS_SCALE_TO_SHOW_ALL, false);
        mEtitle = getIntent().getExtras().getString(EXTRA_TITLE);
    }

    private void initWebView()
    {
        mWeb.addJavascriptInterface(new AppJsHandler(this));
        mWeb.setScaleToShowAll(isScaleToShowAll);

        final String wap_url = ApkConstant.SERVER_URL;
        mWeb.setWebViewClient(new DefaultWebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                showDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                dimissDialog();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.contains(wap_url))
                {
                    if (url.contains("?"))
                    {
                        url = url + "&app=1";
                    } else
                    {
                        url = url + "?app=1";
                    }
                    view.loadUrl(url);
                    return true;
                } else
                {
                    return super.shouldOverrideUrlLoading(view, url);
                }

            }
        });

        mWeb.setWebChromeClient(new WebChromeClient()
        {

            @Override
            public void onReceivedTitle(WebView view, String title)
            {
                mTitle.setMiddleTextTop(view.getTitle());
            }

        });

        if (!TextUtils.isEmpty(mUrl))
        {
            mWeb.get(mUrl);
        } else if (!TextUtils.isEmpty(mHttmContent))
        {
            mWeb.loadData(mHttmContent, "text/html", "utf-8");
        }
    }

    public void onEventMainThread(EFinshiActivity event)
    {
        String url = event.url;
        if (!TextUtils.isEmpty(url))
        {
            String domain = ApkConstant.SERVER_URL;
            if (url.contains("http://"))
            {

            } else
            {
                url = "http://" + domain + url + "&show_prog=1";
            }
            Intent intent = new Intent();
            intent.putExtra(MainActivity.EXTRA_URL, url);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void finish()
    {
        if (getIntent().hasExtra(EXTRA_FINISH_TO_MAIN))
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if (mCurrentExtraCode == 2)
        {
            setResult(Activity.RESULT_OK);
        }

        super.finish();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // mWeb.onPause();
        // mWeb.pauseTimers();
        if(!TextUtils.isEmpty(mEtitle)){
            MobclickAgent.onPageEnd(mEtitle);
            MobclickAgent.onPause(this);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // mWeb.onResume();
        // mWeb.resumeTimers();
        if(!TextUtils.isEmpty(mEtitle)) {
            MobclickAgent.onPageStart(mEtitle);
            MobclickAgent.onResume(this);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // mWeb.loadUrl("about:blank");
        // mWeb.stopLoading();
        // mWeb.setWebChromeClient(null);
        // mWeb.setWebViewClient(null);
        // mWeb.destroy();
        // mWeb = null;
    }
}
