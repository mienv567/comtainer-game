package com.fanwe.live.adapter.viewholder.privatechat;

import android.view.View;
import android.widget.TextView;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgPrivateGift;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MsgGiftRightViewHolder extends MsgGiftLeftViewHolder
{
    public TextView tv_score;

    public MsgGiftRightViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);
        tv_score = find(R.id.tv_score);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgPrivateGift msg = (CustomMsgPrivateGift) customMsg;

        // 图片
        SDViewBinder.setImageView(getAdapter().getActivity(),msg.getProp_icon(), iv_gift);
        SDViewBinder.setTextView(tv_msg, msg.getFrom_msg());
        SDViewBinder.setTextView(tv_score, msg.getFrom_score());
    }
}
