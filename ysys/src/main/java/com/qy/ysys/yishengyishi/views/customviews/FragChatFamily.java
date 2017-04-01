package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.FragChatFamilyRecAdapter;
import com.qy.ysys.yishengyishi.model.ModelFramilyChat;
import com.qy.ysys.yishengyishi.utils.UiUtils;
import com.qy.ysys.yishengyishi.views.ChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony.chen on 2017/1/5.
 */

public class FragChatFamily extends BaseTitleFragment {

    private RecyclerView rcy_content;
    private List<ModelFramilyChat> listData = new ArrayList<>();

    @Override
    protected void initTitleBar(ITitleView titleView) {

    }

    @Override
    protected boolean shouldShowTitle() {
        return false;
    }

    @Override
    public View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_chat_family, null);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);

        listData.add(new ModelFramilyChat("爸妈", 8,ModelFramilyChat.NORMAL));
//        listData.add(new ModelFramilyChat("岳父母", 9,ModelFramilyChat.NORMAL));
//        listData.add(new ModelFramilyChat("家族", 10,ModelFramilyChat.NORMAL));
//        listData.add(new ModelFramilyChat("亲友", 11,ModelFramilyChat.NORMAL));
//        listData.add(new ModelFramilyChat("外祖父母", 12,ModelFramilyChat.NORMAL));
//        listData.add(new ModelFramilyChat("", 0,ModelFramilyChat.CUSTOM));

        rcy_content = (RecyclerView) contentView.findViewById(R.id.rcy_content);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcy_content.addItemDecoration(new SpaceItemDecoration(UiUtils.dip2px(20)));
        rcy_content.setLayoutManager(gridLayoutManager);
        FragChatFamilyRecAdapter fragChatFamilyRecAdapter = new FragChatFamilyRecAdapter(getActivity(), listData);
        fragChatFamilyRecAdapter.setItemClickListener(new FragChatFamilyRecAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
               // 点击进群组聊天
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }

        });
        rcy_content.setAdapter(fragChatFamilyRecAdapter);


    }
}
