package com.fanwe.live.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.library.adapter.SDFragmentPagerAdapter;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.common.SDSelectManager.SDSelectManagerListener;
import com.fanwe.library.config.SDConfig;
import com.fanwe.library.customview.SDViewPager;
import com.fanwe.library.event.EOnClick;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDTabUnderline;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.event.EReSelectTabLive;
import com.fanwe.live.event.EReSelectTabLiveBottom;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.event.ESelectLiveFinish;
import com.fanwe.live.event.EStartContextComplete;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;
import com.sunday.eventbus.SDEventManager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播列表
 *
 * @author Administrator
 * @date 2016-7-2 上午11:28:44
 */
public class LiveTabLiveFragment extends BaseFragment
{

    @ViewInject(R.id.ll_search)
    private View ll_search;

    @ViewInject(R.id.ll_private_chat_list)
    private View ll_private_chat_list;

    @ViewInject(R.id.tv_total_unreadnum)
    private TextView tv_total_unreadnum;

    @ViewInject(R.id.tab_live_follow)
    private SDTabUnderline tab_live_follow;
    @ViewInject(R.id.tab_live_hot)
    private SDTabUnderline tab_live_hot;
    @ViewInject(R.id.tab_live_new)
    private SDTabUnderline tab_live_new;

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    private SparseArray<LiveTabBaseFragment> arrFragment = new SparseArray<>();

    private SDSelectViewManager<SDTabUnderline> selectViewManager = new SDSelectViewManager<>();

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_tab_live;
    }

    @Override
    protected void init()
    {
        super.init();

        ll_search.setOnClickListener(this);
        ll_private_chat_list.setOnClickListener(this);

        initSDViewPager();
        initTabs();
    }

    private void initSDViewPager()
    {
        vpg_content.setOffscreenPageLimit(2);
        List<String> listModel = new ArrayList<>();
        listModel.add("");
        listModel.add("");
        listModel.add("");

        vpg_content.addOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int position)
            {
                if (selectViewManager.getSelectedIndex() != position)
                {
                    selectViewManager.setSelected(position, true);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });
        vpg_content.setAdapter(new LivePagerAdapter(listModel, getActivity(), getChildFragmentManager()));

    }

    private class LivePagerAdapter extends SDFragmentPagerAdapter<String>
    {

        public LivePagerAdapter(List<String> listModel, Activity activity, FragmentManager fm)
        {
            super(listModel, activity, fm);
        }

        @Override
        public Fragment getItemFragment(int position, String model)
        {
            LiveTabBaseFragment fragment = null;
            switch (position)
            {
                case 0:
                    fragment = new LiveTabFollowFragment();
                    break;
                case 1:
                    fragment = new LiveTabHotFragment();
                    break;
                case 2:
                    fragment = new LiveTabNewFragment();
                    break;

                default:
                    break;
            }
            arrFragment.put(position, fragment);
            return fragment;
        }
    }

    public void onEventMainThread(ESelectLiveFinish event)
    {
        updateTabHotText();
    }

    private void updateTabHotText()
    {
        String text = SDConfig.getInstance().getString(R.string.config_live_select_city, "");
        if (TextUtils.isEmpty(text))
        {
            text = LiveConstant.LIVE_HOT_CITY;
        }
        tab_live_hot.setTextTitle(text);
    }

    private void initTabs()
    {
        tab_live_follow.setTextTitle(SDResourcesUtil.getString(R.string.follow));
        tab_live_follow.getViewConfig(tab_live_follow.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT)
                .setBackgroundColorSelected(SDResourcesUtil.getColor(R.color.white));
        tab_live_follow.getViewConfig(tab_live_follow.mTvTitle).setTextColorNormal(SDResourcesUtil.getColor(R.color.text_title_bar)).setTextColorSelected(SDResourcesUtil.getColor(R.color.text_title_bar));

        updateTabHotText();
//        int width = SDViewUtil.dp2px(10);
//        SDViewUtil.setViewWidth(tab_live_hot.iv_below_title, width);
//        SDViewUtil.setViewHeight(tab_live_hot.iv_below_title, width);
        tab_live_hot.getViewConfig(tab_live_hot.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(Color.TRANSPARENT);
        tab_live_hot.getViewConfig(tab_live_hot.mTvTitle).setTextColorNormal(SDResourcesUtil.getColor(R.color.text_title_bar)).setTextColorSelected(SDResourcesUtil.getColor(R.color.text_title_bar));
        tab_live_hot.getViewConfig(tab_live_hot.iv_below_title).setImageNormalResId(0).setImageSelectedResId(R.drawable.ic_arrow_down_white);

        tab_live_new.setTextTitle(SDResourcesUtil.getString(R.string.newest));
        tab_live_new.getViewConfig(tab_live_new.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(SDResourcesUtil.getColor(R.color.white));
        tab_live_new.getViewConfig(tab_live_new.mTvTitle).setTextColorNormal(SDResourcesUtil.getColor(R.color.text_title_bar)).setTextColorSelected(SDResourcesUtil.getColor(R.color.text_title_bar));

        SDTabUnderline[] items = new SDTabUnderline[]{tab_live_follow, tab_live_hot, tab_live_new};

        selectViewManager.setReSelectListener(new SDSelectManager.ReSelectListener<SDTabUnderline>()
        {
            @Override
            public void onSelected(int index, SDTabUnderline item)
            {
                EReSelectTabLive event = new EReSelectTabLive();
                event.index = index;
                SDEventManager.post(event);
            }
        });

        selectViewManager.setListener(new SDSelectManagerListener<SDTabUnderline>()
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
                        clickTabFollow();
                        MobclickAgent.onEvent(getActivity(),"main_follow");
                        break;
                    case 1:
                        clickTabHot();
                        MobclickAgent.onEvent(getActivity(),"main_hot");
                        break;
                    case 2:
                        clickTabNew();
                        MobclickAgent.onEvent(getActivity(),"main_new");
                        break;

                    default:
                        break;
                }
            }
        });
        selectViewManager.setItems(items);
        selectViewManager.setSelected(1, true);
    }

    protected void clickTabFollow()
    {
        vpg_content.setCurrentItem(0);
    }

    protected void clickTabHot()
    {
        vpg_content.setCurrentItem(1);
    }

    protected void clickTabNew()
    {
        vpg_content.setCurrentItem(2);
    }

    @Override
    public void onClick(View v)
    {
        if (v == ll_search)
        {
            clickSearch();
            MobclickAgent.onEvent(getActivity(),"search");
        } else if (v == ll_private_chat_list)
        {
            clickChatList();
            MobclickAgent.onEvent(getActivity(), "message");
        }
        super.onClick(v);
    }

    /**
     * 私聊列表
     */
    private void clickChatList()
    {
        Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
        startActivity(intent);
    }

    /**
     * 搜索
     */
    private void clickSearch()
    {
        Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        initUnReadNum();
        super.onResume();
        MobclickAgent.onPageStart("直播主界面");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("直播主界面");
    }

    public void onEventMainThread(EReSelectTabLiveBottom event)
    {
        if (event.index == 0)
        {
            int index = vpg_content.getCurrentItem();
            LiveTabBaseFragment fragment = arrFragment.get(index);
            if (fragment != null)
            {
                fragment.scrollToTop();
            }
        }
    }


    public void onEventMainThread(EOnClick event)
    {
        if (R.id.tv_tab_live_follow_goto_live == event.view.getId())
        {
            clickTabHot();
        }
    }

    private void initUnReadNum()
    {
        TotalConversationUnreadMessageModel model = IMHelper.getC2CTotalUnreadMessageModel();
        setUnReadNumModel(model);
    }

    public void onEventMainThread(ERefreshMsgUnReaded event)
    {
        TotalConversationUnreadMessageModel model = event.model;
        setUnReadNumModel(model);
    }

    //SDK启动成功接收事件获取未读数量
    public void onEventMainThread(EStartContextComplete event)
    {
        initUnReadNum();
    }

    private void setUnReadNumModel(TotalConversationUnreadMessageModel model)
    {
        if(tv_total_unreadnum != null) {
            SDViewUtil.hide(tv_total_unreadnum);
            if (model != null && model.getTotalUnreadNum() > 0) {
                SDViewUtil.show(tv_total_unreadnum);
                tv_total_unreadnum.setText(model.getStr_totalUnreadNum());
            }
        }
    }
}
