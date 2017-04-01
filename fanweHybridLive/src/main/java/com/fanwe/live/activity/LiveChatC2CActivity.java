package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.LiveChatC2CView;
import com.fanwe.live.model.ItemLiveChatListModel;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/7/18.
 */
public class LiveChatC2CActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_chat_c2c);
        init();
    }

    @Override
    protected void initSystemBar() {
        super.initSystemBar();
        SDViewUtil.setStatusBarTintResource(this, R.color.statusbar_bg);
    }
    /**
     * 加载聊天fragment
     */
    private void init()
    {
//        mTitle.setMiddleTextTop("消息");
        LiveChatC2CView view = new LiveChatC2CView(this);
        view.setClickListener(new LiveChatC2CView.ClickListener()
        {

            @Override
            public void onClickBack()
            {
                finish();
            }
        });
        view.setOnVItemClickListener(new LiveChatC2CView.OnVItemClickListener()
        {
            @Override
            public void onVItemClickListener(ItemLiveChatListModel item, long id)
            {
                Intent intent = new Intent(LiveChatC2CActivity.this, LivePrivateChatActivity.class);
                intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, item.getUserId());
                startActivity(intent);
            }
        });
        SDViewUtil.replaceView(find(R.id.ll_content), view);
        //传入数据
        view.initDataThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("消息界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("消息界面");
        MobclickAgent.onPause(this);
    }
}
