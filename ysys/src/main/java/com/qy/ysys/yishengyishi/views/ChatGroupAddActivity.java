package com.qy.ysys.yishengyishi.views;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.FragChatAddGroupRecAdapter;
import com.qy.ysys.yishengyishi.model.ModelFramilyChat;
import com.qy.ysys.yishengyishi.utils.UiUtils;
import com.qy.ysys.yishengyishi.views.customviews.BaseTitleActivity;
import com.qy.ysys.yishengyishi.views.customviews.ITitleBarOnClickListener;
import com.qy.ysys.yishengyishi.views.customviews.ITitleView;
import com.qy.ysys.yishengyishi.views.customviews.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ChatGroupAddActivity extends BaseTitleActivity {


    private RecyclerView recycleView;
    private List<ModelFramilyChat> listData = new ArrayList<>();

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText("新增群");
        titleView.setLeftImage(R.mipmap.ic_back);
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                ChatGroupAddActivity.this.finish();
            }

            @Override
            public void onClickRight() {

            }
        });
    }

    @Override
    protected int setContentViewByLayoutID() {
        return R.layout.act_chatgroup_add;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);

        listData.add(new ModelFramilyChat("岳父母", 9, ModelFramilyChat.NORMAL));
        listData.add(new ModelFramilyChat("家族", 10, ModelFramilyChat.NORMAL));
        listData.add(new ModelFramilyChat("亲友", 11, ModelFramilyChat.NORMAL));
        listData.add(new ModelFramilyChat("外祖父母", 12, ModelFramilyChat.NORMAL));
        listData.add(new ModelFramilyChat("", 0, ModelFramilyChat.CUSTOM));

        recycleView = (RecyclerView) contentView.findViewById(R.id.rcy_content);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recycleView.addItemDecoration(new SpaceItemDecoration(UiUtils.dip2px(20)));
        recycleView.setLayoutManager(gridLayoutManager);
        FragChatAddGroupRecAdapter addGroupRecAdapter = new FragChatAddGroupRecAdapter(this, listData);
        addGroupRecAdapter.setItemClickListener(new FragChatAddGroupRecAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(ChatGroupAddActivity.this, ChatAddFamilyMenberActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recycleView.setAdapter(addGroupRecAdapter);


    }
}
