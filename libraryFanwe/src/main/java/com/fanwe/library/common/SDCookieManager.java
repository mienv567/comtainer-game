package com.fanwe.library.common;

import android.app.Application;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.net.HttpCookie;
import java.util.List;


/**
 * Created by Administrator on 2016/7/13.
 */
public class SDCookieManager
{
    private static SDCookieManager instance;

    public static SDCookieManager getInstance()
    {
        if (instance == null)
        {
            instance = new SDCookieManager();
        }
        return instance;
    }

    public void init(Application application)
    {
        CookieSyncManager.createInstance(application);
    }

    public String getCookie(String url)
    {
        return CookieManager.getInstance().getCookie(url);
    }

    public void setCookie(String url, String value)
    {
        if (url != null && value != null)
        {
            CookieManager.getInstance().setCookie(url, value);
        }
    }

    public void setCookie(String url, HttpCookie cookie)
    {
        if (cookie != null)
        {
            setCookie(url, cookie.getName() + "=" + cookie.getValue());
        }
    }

    public void setCookie(String url, List<HttpCookie> listCookie)
    {
        if (listCookie != null && !listCookie.isEmpty())
        {
            for (HttpCookie cookie : listCookie)
            {
                setCookie(url, cookie);
            }
        }
    }

    /**
     * 将webview cookie持久化到本地
     */
    public void flush()
    {
        CookieSyncManager.getInstance().sync();
    }

}
