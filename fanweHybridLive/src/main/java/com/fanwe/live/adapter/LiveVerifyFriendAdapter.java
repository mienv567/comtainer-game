package com.fanwe.live.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.live.R;
import com.fanwe.live.model.LiveVerifyInfoVo;


import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */
public class LiveVerifyFriendAdapter extends RecyclerView.Adapter<LiveVerifyFriendAdapter.VerifyFriendViewHolder> implements View.OnClickListener {

    /**
     * 消息往后叠加的次数远大于选择性删除的次数
     * 故使用ArrayList优于LinkedList
     */
    private ArrayList<LiveVerifyInfoVo> mData = null;

    public LiveVerifyFriendAdapter(ArrayList<LiveVerifyInfoVo> data) {
        this.mData = data;
    }

    @Override
    public VerifyFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VerifyFriendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_verify_friend, null));
    }

    @Override
    public void onBindViewHolder(VerifyFriendViewHolder holder, int position) {
        LiveVerifyInfoVo verifyInfo = mData.get(position);
        holder.tvName.setText(verifyInfo.name);
        holder.tvAccept.setTag(holder.tvAccept.getId(), position);
        holder.tvAccept.setOnClickListener(LiveVerifyFriendAdapter.this);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(v.getId());
        if (mData != null && mData.size() > position) {
            EventBus.getDefault().post(mData.get(position));
        }
    }

    static class VerifyFriendViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;

        public TextView tvAccept;

        public VerifyFriendViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAccept = (TextView) itemView.findViewById(R.id.tvAccept);
        }
    }
}
