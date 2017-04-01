package com.fanwe.live.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.WindowManager;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.live.R;
import com.fanwe.live.fragment.FansRankFragment;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;


public class FansRankActivity extends BaseActivity {

    private String[] mTabTitles = {"贡献榜", "勋章榜"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @ViewInject(R.id.segment_tab_layout)
    private SegmentTabLayout segment_tab_layout;
    @ViewInject(R.id.view_pager)
    private ViewPager view_pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fans_rank);
        initFragment();
    }

    private void initFragment() {
        for (int i = 0; i < mTabTitles.length; i++) {
            mFragments.add(FansRankFragment.newInstance(i));
        }
        segment_tab_layout.setTabData(mTabTitles);

        segment_tab_layout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                view_pager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        view_pager.setAdapter(new TopPagerAdapter(getSupportFragmentManager()));
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("FansRankActivity", "onPageSelected: " + position);
                segment_tab_layout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class TopPagerAdapter extends FragmentPagerAdapter {
        public TopPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
