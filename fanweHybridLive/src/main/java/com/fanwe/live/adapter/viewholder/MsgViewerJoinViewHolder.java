package com.fanwe.live.adapter.viewholder;

import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * 普通用户加入提示
 */
public class MsgViewerJoinViewHolder extends MsgTextViewHolder
{
    public MsgViewerJoinViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {

        appendUserInfoNotFormat(customMsg.getSender());

        // 内容
        String text = " "+SDResourcesUtil.getString(R.string.coming);
        int textColor = SDResourcesUtil.getColor(R.color.live_msg_text_viewer);
        appendContent(text, textColor);
        setUserInfoClickListener(tv_content);
    }
}
