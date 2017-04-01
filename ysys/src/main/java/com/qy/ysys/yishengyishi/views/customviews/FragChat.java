package com.qy.ysys.yishengyishi.views.customviews;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.FragmentAdatper;
import com.qy.ysys.yishengyishi.views.ChatGroupAddActivity;

import java.util.ArrayList;

/**
 * Created by tony.chen on 2016/12/30.
 */

public class FragChat extends BaseTitleFragment {
    private ViewPager vp_content;
    private CommonTabLayout tabLayout;
    private String[] mTitles;
    private ArrayList<CustomTabEntity> mTabEntities;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    public View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_chat, null);
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                // 新增加群
                Intent intent = new Intent(getActivity(), ChatGroupAddActivity.class);
                startActivity(intent);
            }
        });

        ViewGroup middleView = titleView.getTitleBarMiddleContentView();
        middleView.removeAllViews();
        View tabLayoutContent = View.inflate(getActivity(), R.layout.item_titlebar_tab_chat, null);
        tabLayout = (CommonTabLayout) tabLayoutContent.findViewById(R.id.tl);
        mTabEntities = new ArrayList<>();
//        mTitles = new String[]{"家群聊", "私聊", "直播"};
        mTitles = new String[]{"家群聊", "私聊"};
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }
        tabLayout.setTabData(mTabEntities);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp_content.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


        middleView.addView(tabLayoutContent);
        titleView.setRightImage(R.mipmap.ic_add);
    }

    @Override
    protected View setTitleViewByView() {
        return new CustomTitleView(getActivity());
    }

    @Override
    protected void initView(View contentView) {
        vp_content = (ViewPager) contentView.findViewById(R.id.vp_chat_content);

        Fragment fragmentFamilyChat = new FragChatFamily();
        Fragment fragmentPrivateChat = new FragChatPrivateChat();
        Fragment fragmentLive = new FragChatLive();
        fragments.add(fragmentFamilyChat);
        fragments.add(fragmentPrivateChat);
//        fragments.add(fragmentLive);

        vp_content.setAdapter(new FragmentAdatper(getActivity().getSupportFragmentManager(), fragments));
        vp_content.setOffscreenPageLimit(1);
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
