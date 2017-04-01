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
import com.qy.ysys.yishengyishi.views.AddStoreActivity;

import java.util.ArrayList;

/**
 * Created by tony.chen on 2016/12/30.
 */

public class FragMemoirs extends BaseTitleFragment {


    private ViewPager vp_content;
    private CommonTabLayout tabLayout;
    private String[] mTitles = new String[]{"默认", "婚庆回忆", "生活点滴", "旅游记忆", "家人"};
    private ArrayList<CustomTabEntity> mTabEntities;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    public View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_memoirs, null);
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText(getResources().getString(R.string.menu_memoirs));
        titleView.setRightImage(R.mipmap.ic_add);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                Intent intent = new Intent(getActivity(), AddStoreActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected View setTitleViewByView() {
        return new CustomTitleView(getActivity());
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        vp_content = (ViewPager) contentView.findViewById(R.id.vp_memoirs_content);
        Fragment fragMemoirsDefault = new FragMemoirsDefault();
        Fragment fragMemoirsWedding = new FragMemoirsWedding();
        Fragment fragMemoirsLife = new FragMemoirsLife();
        Fragment fragMemoirsTravel = new FragMemoirsTravel();
        Fragment fragMemoirsFamily = new FragMemoirsFamily();
        fragments.add(fragMemoirsDefault);
//        fragments.add(fragMemoirsWedding);
//        fragments.add(fragMemoirsLife);
//        fragments.add(fragMemoirsTravel);
//        fragments.add(fragMemoirsFamily);
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

        tabLayout = (CommonTabLayout) contentView.findViewById(R.id.ctl);
        mTabEntities = new ArrayList<>();
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


    }
}
