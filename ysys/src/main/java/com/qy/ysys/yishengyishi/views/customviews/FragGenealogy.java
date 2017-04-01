package com.qy.ysys.yishengyishi.views.customviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.FragmentAdatper;
import com.qy.ysys.yishengyishi.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by tony.chen on 2016/12/30.
 */

public class FragGenealogy extends BaseTitleFragment {
    private String[] mTitles = {"家谱图", "家谱录"};
    private ViewPager vp_content;
    private SegmentTabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    public View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_genealogy, null);
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setLeftImage(R.mipmap.ic_refresh);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
//                ToastUtil.showToast("click refresh");
            }

            @Override
            public void onClickRight() {

            }
        });

        ViewGroup middleContentView = titleView.getTitleBarMiddleContentView();
        LinearLayout tabLayoutContainer = (LinearLayout) View.inflate(getActivity(), R.layout.item_titlebar_tab_genealogy, null);
        tabLayout = (SegmentTabLayout) tabLayoutContainer.findViewById(R.id.tl);
        tabLayout.setTabData(mTitles);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp_content.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        middleContentView.removeAllViews();
        middleContentView.addView(tabLayoutContainer);

    }

    @Override
    protected View setTitleViewByView() {
        return new CustomTitleView(getActivity());
    }


    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        Fragment fragGenealogyTree = new FragGenealogyTree();
        FragGenealogyDirectory fragGenealogyDirectory = new FragGenealogyDirectory();
        fragments.add(fragGenealogyTree);
        fragments.add(fragGenealogyDirectory);

        vp_content = (ViewPager) contentView.findViewById(R.id.vp_chat_content);
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
