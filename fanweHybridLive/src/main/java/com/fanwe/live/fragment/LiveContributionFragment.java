package com.fanwe.live.fragment;

import android.support.v4.app.Fragment;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.R;
import com.flyco.tablayout.SegmentTabLayout;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

public class LiveContributionFragment extends BaseFragment {

    private String[] mTitles = {"贡献日榜", "贡献周榜", "贡献月榜", "贡献总榜"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @ViewInject(R.id.segment_tab_layout)
    private SegmentTabLayout segment_tab_layout;
    /*@ViewInject(R.id.view_pager_top_contribution)
    private ViewPager view_pager_top_contribution;*/

    @Override
    protected int onCreateContentView() {
        return R.layout.fragment_live_contribution;
    }

    @Override
    protected void init() {
        super.init();
        LogUtil.d("LiveContribution init--");
        for (int i = 0; i < mTitles.length; i++) {
            mFragments.add(LiveFansRankFragment.newInstance(i));
        }

        segment_tab_layout.setTabData(mTitles, getActivity(), R.id.fl_container, mFragments);
        /*segment_tab_layout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                view_pager_top_contribution.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        view_pager_top_contribution.setAdapter(new TopPagerAdapter(getActivity().getSupportFragmentManager()));
        view_pager_top_contribution.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("LiveContribution", "onPageSelected: " + position);
                segment_tab_layout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        view_pager_top_contribution.setCurrentItem(1);
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
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }*/
    }

}
