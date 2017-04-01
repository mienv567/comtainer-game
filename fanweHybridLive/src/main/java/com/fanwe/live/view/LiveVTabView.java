package com.fanwe.live.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.fragment.LiveChatFragment;
import com.fanwe.live.fragment.LiveContributionFragment;
import com.fanwe.live.fragment.LivePropsFragment;
import com.fanwe.live.fragment.LiveTaskFragment;

/**
 * Created by yong.zhang on 2017/3/14 0014.
 */
public class LiveVTabView implements View.OnClickListener {

    private View mRootView;

    private FragmentManager mFragmentManager;

    private static final Class<?>[] clazz = new Class<?>[]{
            LiveChatFragment.class, LiveTaskFragment.class,
            LiveContributionFragment.class, LivePropsFragment.class};

    private Fragment[] mFragments = new Fragment[clazz.length];

    private static final int[] mIds = new int[]{
            R.id.tvChat, R.id.tvTask, R.id.tvDevote, R.id.tvGift};

    private TextView[] mTextViews = new TextView[mIds.length];

    private int mCurrentSelectIndex = 0;

    private FrameLayout mTabContainer;
    private final ViewPager mViewPager;

    public LiveVTabView(FrameLayout tabContainer, FragmentManager fragmentManager) {
        this.mTabContainer = tabContainer;
        mRootView = LayoutInflater.from(mTabContainer.getContext()).inflate(R.layout.include_live_v_tab, null);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.view_pager);
        this.mFragmentManager = fragmentManager;
        for (int index = 0; index < mIds.length; index++) {
            mTextViews[index] = (TextView) mRootView.findViewById(mIds[index]);
            mTextViews[index].setOnClickListener(this);
            try {
                mFragments[index] = (Fragment) clazz[index].newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        mViewPager.setAdapter(new MyPagerAdapter(fragmentManager));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchSelectState(mIds[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void show() {
        mTabContainer.setVisibility(View.VISIBLE);
        SDViewUtil.addView(mTabContainer, mRootView);
        switchSelectState(mIds[mCurrentSelectIndex]);
    }

    public void hide() {
        mTabContainer.removeAllViews();
        mTabContainer.setVisibility(View.GONE);
    }

    public void switchSelectState(int id) {
        for (int index = 0; index < mIds.length; index++) {
            if (mIds[index] == id) {
                mCurrentSelectIndex = index;
                mTextViews[index].setSelected(true);
                mViewPager.setCurrentItem(index);
            } else {
                mTextViews[index].setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switchSelectState(v.getId());
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }
    }
}
