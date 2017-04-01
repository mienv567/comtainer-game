package com.fanwe.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.fanwe.live.R;
import com.fanwe.live.adapter.viewholder.ChatViewHolder;
import com.fanwe.live.model.LiveChatMsgVo;
import com.qy.ysys.yishengyishi.utils.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */

public class LiveChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    /**
     * 消息往后叠加的次数远大于选择性删除的次数
     * 故使用ArrayList优于LinkedList
     */
    private ArrayList<LiveChatMsgVo> mData = new ArrayList<>();

    public LiveChatAdapter(List<LiveChatMsgVo> data) {
        setData(data);
    }

    public void setData(List<LiveChatMsgVo> data) {
        mData.clear();
        if (data != null && data.size() > 0) {
            mData.addAll(data);
        }
//        notifyDataSetChanged();
    }

    public void addData(LiveChatMsgVo msg) {
        if (msg != null && !mData.contains(msg)) {
            mData.add(msg);
            notifyDataSetChanged();
        }
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_chat, null));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        LiveChatMsgVo data = mData.get(position);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.tvMessage.getLayoutParams();
        if (data.type == LiveChatMsgVo.TYPE_FREIEND) {
            Context ctx = holder.ivFriend.getContext();
            Glide.with(ctx).load(data.img).transform(new GlideCircleTransform(ctx)).into(holder.ivFriend);

//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tvMessage.getLayoutParams();
//            params.addRule(RelativeLayout.RIGHT_OF, holder.ivFriend.getId());
//            params.addRule(RelativeLayout.LEFT_OF, 0);
//            holder.tvMessage.setLayoutParams(params);
            params.gravity = Gravity.LEFT;
            params.leftMargin = 0;
            params.rightMargin = 100;
//            holder.tvMessage.setGravity(Gravity.LEFT);
        } else if (data.type == LiveChatMsgVo.TYPE_ME) {
            Context ctx = holder.ivMe.getContext();
            Glide.with(ctx).load(data.img).transform(new GlideCircleTransform(ctx)).into(holder.ivMe);

//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tvMessage.getLayoutParams();
//            params.addRule(RelativeLayout.RIGHT_OF, 0);
//            params.addRule(RelativeLayout.LEFT_OF, holder.ivMe.getId());
//            holder.tvMessage.setLayoutParams(params);
            params.leftMargin = 100;
            params.rightMargin = 0;
            params.gravity = Gravity.RIGHT;
//            holder.tvMessage.setGravity(Gravity.RIGHT);
        }
        holder.tvMessage.setLayoutParams(params);
        holder.tvMessage.setText(data.msg);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
