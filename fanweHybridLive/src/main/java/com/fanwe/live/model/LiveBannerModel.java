package com.fanwe.live.model;

import android.app.Activity;
import android.content.Intent;

import com.fanwe.hybrid.activity.AppWebViewActivity;

public class LiveBannerModel
{

    private int type;
    private String url;
    private String image;

    public Intent parseType(Activity activity)
    {
        Intent intent = null;
        switch (type)
        {
            case 0:
                intent = new Intent(activity, AppWebViewActivity.class);
                intent.putExtra(AppWebViewActivity.EXTRA_URL, url);
                intent.putExtra(AppWebViewActivity.EXTRA_TITLE,"Banner界面");
                break;
            default:
                break;
        }

        return intent;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

}
