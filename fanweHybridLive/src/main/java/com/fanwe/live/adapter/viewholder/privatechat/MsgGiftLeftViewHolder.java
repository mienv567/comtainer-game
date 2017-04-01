package com.fanwe.live.adapter.viewholder.privatechat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.app.App;
import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgPrivateGift;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MsgGiftLeftViewHolder extends PrivateChatViewHolder
{
    public ImageView iv_gift;
    public TextView tv_msg;

    public MsgGiftLeftViewHolder(View itemView, SDRecyclerAdapter<MsgModel> adapter)
    {
        super(itemView, adapter);
        iv_gift = find(R.id.iv_gift);
        tv_msg = find(R.id.tv_msg);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgPrivateGift msg = (CustomMsgPrivateGift) customMsg;

        // 图片
        SDViewBinder.setImageView(App.getApplication(),msg.getProp_icon(), iv_gift);
        SDViewBinder.setTextView(tv_msg, msg.getTo_msg());
    }
}
