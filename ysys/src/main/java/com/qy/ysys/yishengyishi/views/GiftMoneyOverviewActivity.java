package com.qy.ysys.yishengyishi.views;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;

public class GiftMoneyOverviewActivity extends BaseTitleActivity {

    private ListView mListView;

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_giftmoney_overview;
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("礼金收入");
        titleView.setRightImage(R.mipmap.ic_add);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                Intent intent = new Intent(GiftMoneyOverviewActivity.this, GiftMoneyEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mListView = (ListView) contentView.findViewById(R.id.lv_giftmoney_overview_content);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 2;
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
                return View.inflate(GiftMoneyOverviewActivity.this, R.layout.item_giftmoney_overview, null);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
