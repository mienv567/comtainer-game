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

public class ChatActivity extends BaseTitleActivity {

    private ListView mListView;

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_chat_activity;
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("爸妈");
        titleView.setRightImage(R.mipmap.ic_setting);
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                ChatActivity.this.finish();
            }

            @Override
            public void onClickRight() {
                Intent intent = new Intent(ChatActivity.this, ChatGroupEditActivity.class);
                startActivityForResult(intent, 123);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mListView = (ListView) contentView.findViewById(R.id.lv_chat_content);
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
               if(position%2==0){
                   return View.inflate(ChatActivity.this,R.layout.chat_item_itemin,null);
               }else {
                   return View.inflate(ChatActivity.this,R.layout.chat_item_itemout,null);
               }
            }
        });
    }
}
