package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.fragment.LiveTabHotFragment;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LiveTopicRoomActivity extends BaseTitleActivity
{

    /**
     * 话题的名字(String)
     */
    public static final String EXTRA_TOPIC_TITLE = "extra_topic_title";
    /**
     * 话题的id(int)
     */
    public static final String EXTRA_TOPIC_ID = "extra_topic_id";

    @ViewInject(R.id.view_create_topic_room)
    private View view_create_topic_room;


    private String strTopic;
    private int topicId;

    @Override
    protected void onNewIntent(Intent intent)
    {
        setIntent(intent);
        init(null);
        super.onNewIntent(intent);
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.act_live_topic_room;
    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);
        strTopic = getIntent().getStringExtra(EXTRA_TOPIC_TITLE);
        topicId = getIntent().getIntExtra(EXTRA_TOPIC_ID, 0);

        if (topicId <= 0)
        {
            SDToast.showToast(getString(R.string.topic_id_empty));
            finish();
            return;
        }

        initTitle();


        LiveTabHotFragment fragment = new LiveTabHotFragment();
        fragment.getArguments().putInt(LiveTabHotFragment.EXTRA_TOPIC_ID, topicId);
        getSDFragmentManager().replace(R.id.fl_content, fragment);

        view_create_topic_room.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), LiveCreateRoomActivity.class);
                intent.putExtra(LiveCreateRoomActivity.EXTRA_CATE_ID, topicId);
                intent.putExtra(LiveCreateRoomActivity.EXTRA_TITLE, strTopic);
                startActivity(intent);
            }
        });
    }

    private void initTitle()
    {
        if (strTopic != null)
        {
            mTitle.setMiddleTextTop(strTopic);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("话题相关房间界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("话题相关房间界面");
        MobclickAgent.onPause(this);
    }
}
