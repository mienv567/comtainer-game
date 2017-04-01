package com.fanwe.live.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.activity.chat.LiveAddFriendEventActivity;
import com.fanwe.live.activity.chat.LiveChatEventActivity;
import com.fanwe.live.activity.chat.LiveVerifyFriendEventActivity;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by kevin.liu on 2017/3/23.
 */

public class LiveChatRoomFragment extends BaseFragment {


    @ViewInject(R.id.iv_left)
    ImageView iv_left;

    @ViewInject(R.id.tv_title)
    TextView tv_title;

    @ViewInject(R.id.tv_right)
    TextView tv_right;

    @ViewInject(R.id.item_add_friend)
    View item_add_friend;

    @ViewInject(R.id.item_friend_list)
    View item_friend_list;

    @ViewInject(R.id.item_official_group)
    View item_official_group;


    @Override
    protected int onCreateContentView() {
        return R.layout.frag_chat_room;
    }

    @Override
    protected void init() {
        tv_title.setText("聊天室");
        tv_right.setVisibility(View.GONE);
        iv_left.setVisibility(View.GONE);
        item_add_friend.findViewById(R.id.iv_image).setBackgroundResource(R.drawable.ic_friend_chat);
        item_friend_list.findViewById(R.id.iv_image).setBackgroundResource(R.drawable.ic_friend_list);
        item_official_group.findViewById(R.id.iv_image).setBackgroundResource(R.drawable.ic_official_group);
        ((TextView) item_add_friend.findViewById(R.id.tv_content)).setText(R.string.add_friend);
        ((TextView) item_friend_list.findViewById(R.id.tv_content)).setText(R.string.friend_list);
        ((TextView) item_official_group.findViewById(R.id.tv_content)).setText(R.string.official_group);

        findViewById(R.id.item_add_friend).setOnClickListener(this);
        findViewById(R.id.item_friend_list).setOnClickListener(this);
        findViewById(R.id.item_official_group).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_add_friend:
                startActivity(new Intent(getActivity(), LiveAddFriendEventActivity.class));
                break;
            case R.id.item_friend_list:
                startActivity(new Intent(getActivity(), LiveChatEventActivity.class));
                break;
            case R.id.item_official_group:
                startActivity(new Intent(getActivity(), LiveVerifyFriendEventActivity.class));
                break;
        }

    }
}
