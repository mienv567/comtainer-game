package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.views.ChatActivity;

/**
 * Created by tony.chen on 2017/1/5.
 */

public class FragChatPrivateChat extends BaseTitleFragment {

    private ListView mListView;

    @Override
    protected void initTitleBar(ITitleView titleView) {

    }

    @Override
    protected boolean shouldShowTitle() {
        return false;
    }

    @Override
    protected View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_chat_private, null);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mListView = (ListView) contentView.findViewById(R.id.lv_chat_private_content);
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
                return View.inflate(getActivity(), R.layout.item_contact, null);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });

    }
}
