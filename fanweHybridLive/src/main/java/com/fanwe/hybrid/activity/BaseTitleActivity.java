package com.fanwe.hybrid.activity;

import android.view.View;

import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.title.SDTitleSimple.SDTitleSimpleListener;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

public class BaseTitleActivity extends BaseActivity implements SDTitleSimpleListener
{
    protected SDTitleSimple mTitle;

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);

        mTitle = (SDTitleSimple) findViewById(R.id.title);
        mTitle.setLeftImageLeft(R.drawable.ic_back);
        mTitle.setmListener(this);
    }


    @Override
    protected void initSystemBar() {
        super.initSystemBar();
        SDViewUtil.setStatusBarTintResource(this, R.color.bg_main);
    }

    @Override
    protected int onCreateTitleViewResId()
    {
        return R.layout.include_title_simple;
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v)
    {
        finish();
    }

    @Override
    public void onCLickMiddle_SDTitleSimple(SDTitleItem v)
    {

    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
    {

    }

}
