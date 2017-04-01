package com.fanwe.live.adapter.viewholder;

import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgText;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * 文字消息
 */
public class MsgTextViewHolder extends MsgViewHolder
{
    private boolean isForLrs;

    public MsgTextViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);
    }

    public MsgTextViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter,boolean isForLrs)
    {
        super(itemView, adapter);
        this.isForLrs = isForLrs;
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        appendUserInfo(customMsg.getSender(),isForLrs);

        // 内容
        int textColor = 0;
        if (customMsg.getSender().getUserId().equals(LiveInformation.getInstance().getCreaterId()))
        {
            // 主播
            textColor = SDResourcesUtil.getColor(R.color.live_msg_text_creater);
        } else
        {
            textColor = SDResourcesUtil.getColor(R.color.live_msg_text_viewer);
        }
        appendContent(" " +getText(), textColor);
        setUserInfoClickListener(tv_content);
    }

    protected String getText()
    {
        CustomMsgText msg = (CustomMsgText) customMsg;
        return msg.getText();
    }

}
