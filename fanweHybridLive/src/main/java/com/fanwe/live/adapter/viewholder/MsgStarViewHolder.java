package com.fanwe.live.adapter.viewholder;

import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * 点亮满99星星动画
 */
public class MsgStarViewHolder extends MsgViewHolder
{
    public MsgStarViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {

        appendUserInfoNotFormat(customMsg.getSender(), LiveConstant.LiveMsgType.MSG_LIGHT);

        // 内容
        String text = " "+SDResourcesUtil.getString(R.string.light_99_times);
        int textColor = SDResourcesUtil.getColor(R.color.live_light);
        appendContent(text, textColor);


        setUserInfoClickListener(tv_content);
    }
}
