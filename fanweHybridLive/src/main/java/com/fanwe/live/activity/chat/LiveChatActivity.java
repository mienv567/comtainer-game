package com.fanwe.live.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveChatAdapter;
import com.fanwe.live.model.LiveChatFriendVo;
import com.fanwe.live.model.LiveChatMsgVo;
import com.fanwe.live.pop.EmojiPanel;
import com.fanwe.live.pop.TalkPanel;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */
public abstract class LiveChatActivity extends BaseActivity implements TextView.OnEditorActionListener, SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.tvTitle)
    private TextView tvTitle;

    @ViewInject(R.id.ivTalk)
    private ImageView ivTalk;

    @ViewInject(R.id.ivEmoji)
    private ImageView ivEmoji;

    @ViewInject(R.id.ivMore)
    private ImageView ivMore;

    @ViewInject(R.id.etMessage)
    private EditText etMessage;

    @ViewInject(R.id.rvMessage)
    private RecyclerView rvMessage;

    @ViewInject(R.id.srlMessage)
    private SwipeRefreshLayout srlMessage;

    private LiveChatAdapter mLiveChatAapter;

    private ArrayList<LiveChatMsgVo> mMsgList = new ArrayList<>();

    private EmojiPanel mEmojiPanel;

    private TalkPanel mTalkPanel;

    protected LiveChatFriendVo mFriend;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_chat;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        ivTalk.setOnClickListener(this);
        ivEmoji.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        etMessage.setOnEditorActionListener(this);
        srlMessage.setOnRefreshListener(this);

        setTitle();
    }

    private void setTitle() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        Bundle args = intent.getExtras();
        if (args == null || args.size() <= 0) {
            return;
        }
        if (!args.containsKey(LiveChatFriendVo.class.getName())) {
            return;
        }
        mFriend = (LiveChatFriendVo) args.getSerializable(LiveChatFriendVo.class.getName());
        if (mFriend == null) {
            return;
        }
        tvTitle.setText(mFriend.name);
    }

    protected void setMsgList(List<LiveChatMsgVo> data) {
        if(data != null && data.size() > 0){
            mMsgList.addAll(data);
        }
        mLiveChatAapter = new LiveChatAdapter(mMsgList);
        rvMessage.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvMessage.setAdapter(mLiveChatAapter);
    }

    protected void showEmojiPanel(View v) {
        if (mEmojiPanel == null) {
            mEmojiPanel = new EmojiPanel();
        }
        mEmojiPanel.show(v);
    }

    protected void showTalkPanel(View v) {
        if (mTalkPanel == null) {
            mTalkPanel = new TalkPanel();
        }
        mTalkPanel.show(v);
    }

    protected void sendMsg() {
        String msg = etMessage.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        etMessage.setText("");
        EventBus.getDefault().post(msg);
    }

    protected void sendMsgOver() {

    }

    protected void refreshOver() {
        srlMessage.setRefreshing(false);
    }
}
