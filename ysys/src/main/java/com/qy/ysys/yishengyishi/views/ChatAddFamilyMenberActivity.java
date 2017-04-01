package com.qy.ysys.yishengyishi.views;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.utils.HandlerManager;
import com.qy.ysys.yishengyishi.utils.ToastUtil;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

public class ChatAddFamilyMenberActivity extends BaseTitleActivity {

    private ListView mListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("添加成员");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setRightImage(R.mipmap.ic_add);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                ChatAddFamilyMenberActivity.this.finish();
            }

            @Override
            public void onClickRight() {
                ChatAddFamilyMenberActivity.this.finish();
            }
        });
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_fragchat_add_familymenber;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srfl);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                HandlerManager.getMainHamdler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mListView = (ListView) contentView.findViewById(R.id.lv_addmember_content);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return View.inflate(ChatAddFamilyMenberActivity.this, R.layout.item_addfamilymenber, null);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
