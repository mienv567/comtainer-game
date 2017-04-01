package com.fanwe.live.adapter.viewholder;

import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgShare;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * 分享消息
 */
public class MsgShareViewHolder extends MsgViewHolder
{
    public MsgShareViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgShare msg = (CustomMsgShare) customMsg;

        appendUserInfoNotFormat(msg.getSender(), LiveConstant.LiveMsgType.MSG_SHARE);

        // 内容
        String text = msg.getDesc();
        int textColor = SDResourcesUtil.getColor(R.color.live_share);
        appendContent(text, textColor);


        setUserInfoClickListener(tv_content);
    }
}
