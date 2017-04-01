package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.span.builder.SDSpannableStringBuilder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.model.ItemLiveChatListModel;
import com.fanwe.live.view.LivePrivateChatSpanTextView;
import com.tencent.TIMConversation;

import java.util.ArrayList;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-15 下午2:14:28 类说明
 */
public class LiveChatUserAdapter extends SDSimpleAdapter<ItemLiveChatListModel>
{
    public LiveChatUserAdapter(ArrayList<ItemLiveChatListModel> friendlistModel, Activity liveChatUserNoFocusFragment)
    {
        super(friendlistModel, liveChatUserNoFocusFragment);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_chat_user;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, final ItemLiveChatListModel model)
    {
        ImageView civ_v_icon = ViewHolder.get(R.id.civ_v_icon, convertView);
        SDViewUtil.hide(civ_v_icon);

        ImageView civ_head_image = ViewHolder.get(R.id.civ_head_image, convertView);
        TextView tv_nick_name = ViewHolder.get(R.id.tv_nick_name, convertView);
        LivePrivateChatSpanTextView tv_content = ViewHolder.get(R.id.tv_content, convertView);
        ImageView iv_global_male = ViewHolder.get(R.id.iv_global_male, convertView);
        ImageView iv_rank = ViewHolder.get(R.id.iv_rank, convertView);
        TextView tv_time = ViewHolder.get(R.id.tv_time, convertView);
        TextView tv_unreadnum = ViewHolder.get(R.id.tv_unreadnum, convertView);
        SDViewUtil.hide(tv_unreadnum);

        SDViewBinder.setTextView(tv_time, model.getTimestampFormat());
        if (!TextUtils.isEmpty(model.getText()))
        {
            SDSpannableStringBuilder sp = new SDSpannableStringBuilder();
            sp.append(model.getText());
            tv_content.setText(sp);
        }

        TIMConversation peer = IMHelper.getConversationC2C(model.getPeer());
        if (peer.getUnreadMessageNum() > 0)
        {
            SDViewUtil.show(tv_unreadnum);
            SDViewBinder.setTextView(tv_unreadnum, Long.toString(peer.getUnreadMessageNum()));
        } else
        {
            SDViewUtil.hide(tv_unreadnum);
        }

        SDViewBinder.setTextView(tv_nick_name, model.getNickName());
        iv_global_male.setImageResource(model.getSexResId());
        iv_rank.setImageResource(model.getLevelImageResId());
        SDViewBinder.setImageView(getActivity(),model.getHeadImage(), civ_head_image,R.drawable.ic_default_head);

        if (!TextUtils.isEmpty(model.getV_icon()))
        {
            SDViewUtil.show(civ_v_icon);
            SDViewBinder.setImageView(getActivity(),model.getV_icon(), civ_v_icon);
        } else
        {
            SDViewUtil.hide(civ_v_icon);
        }
    }


    public void sortListModel(int position, ItemLiveChatListModel model)
    {
        listModel.remove(position);
        listModel.add(0, model);
        notifyDataSetChanged();
    }
}
