package com.fanwe.library.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.view.select.SDSelectViewAuto;

public class SDTabUnderline extends SDSelectViewAuto
{

    public TextView mTvTitle;
    public ImageView iv_below_title;
    public ImageView mIvUnderline;


    public SDTabUnderline(Context context)
    {
        super(context);
        init();
    }

    public SDTabUnderline(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    @Override
    protected void init()
    {
        setContentView(R.layout.view_tab_underline);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        iv_below_title = (ImageView) findViewById(R.id.iv_below_title);
        mIvUnderline = (ImageView) findViewById(R.id.iv_underline);
        addAutoView(mIvUnderline, iv_below_title, mTvTitle);

        setDefaultConfig();
        onNormal();
    }

    @Override
    public void setDefaultConfig()
    {
        getViewConfig(mTvTitle).setTextColorNormal(Color.GRAY).setTextColorSelected(mLibraryConfig.getMainColor());
        getViewConfig(mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(mLibraryConfig.getMainColor());
        super.setDefaultConfig();
    }

    public void setTextTitle(String title)
    {
        SDViewBinder.setTextView(mTvTitle, title);
    }

}
