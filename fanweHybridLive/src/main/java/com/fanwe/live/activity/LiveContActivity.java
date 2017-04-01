package com.fanwe.live.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.common.SDSelectManager.SDSelectManagerListener;
import com.fanwe.library.view.SDTabUnderline;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.fragment.LiveContLocalFragment;
import com.fanwe.live.fragment.LiveContTotalFragment;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-2 下午5:10:51 类说明
 */
public class LiveContActivity extends BaseTitleActivity
{

    @ViewInject(R.id.tab_left)
    private SDTabUnderline mTabLeft;

    @ViewInject(R.id.tab_right)
    private SDTabUnderline mTabRight;

    private SDSelectViewManager<SDTabUnderline> mSelectManager = new SDSelectViewManager<SDTabUnderline>();

    private int mSelectTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cont);
        x.view().inject(this);
        init();
    }

    private void init()
    {
        initTitle();
        register();
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(AppRuntimeWorker.getTicketName() + getString(R.string.contribution_list));
    }

    private void register()
    {
        addTab();
    }

    private void addTab()
    {
        mTabLeft.setTextTitle(getString(R.string.this_live));
        mTabLeft.getViewConfig(mTabLeft.mTvTitle).setTextColorNormalResId(R.color.text_black).setTextColorSelectedResId(R.color.main_color);
        mTabLeft.getViewConfig(mTabLeft.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelectedResId(R.color.main_color);

        mTabRight.setTextTitle(getString(R.string.total_rank));
        mTabRight.getViewConfig(mTabRight.mTvTitle).setTextColorNormalResId(R.color.text_black).setTextColorSelectedResId(R.color.main_color);
        mTabRight.getViewConfig(mTabRight.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT)
                .setBackgroundColorSelectedResId(R.color.main_color);

        mSelectManager.setListener(new SDSelectManagerListener<SDTabUnderline>()
        {

            @Override
            public void onNormal(int index, SDTabUnderline item)
            {
            }

            @Override
            public void onSelected(int index, SDTabUnderline item)
            {
                switch (index)
                {
                    case 0:
                        click0();
                        break;
                    case 1:
                        click1();
                        break;
                    default:
                        break;
                }
            }
        });

        mSelectManager.setItems(new SDTabUnderline[]{mTabLeft, mTabRight});
        mSelectManager.performClick(mSelectTabIndex);
    }

    /**
     * 本场直播贡献排行
     */
    protected void click0()
    {
        getSDFragmentManager().toggle(R.id.ll_content, null, LiveContLocalFragment.class);
    }

    /**
     * 总贡献排行
     */
    protected void click1()
    {
        getSDFragmentManager().toggle(R.id.ll_content, null, LiveContTotalFragment.class);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
