package com.fanwe.live.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.live.R;
import com.fanwe.live.model.LiveSearchHistoryVo;
import com.fanwe.live.model.LiveVerifyInfoVo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */
public class LiveSearchHistoryAdapter extends RecyclerView.Adapter<LiveSearchHistoryAdapter.SearchHistoryViewHolder> implements View.OnClickListener {

    /**
     * 消息往后叠加的次数远大于选择性删除的次数
     * 故使用ArrayList优于LinkedList
     */
    private List<LiveSearchHistoryVo> mData = null;

    public LiveSearchHistoryAdapter(List<LiveSearchHistoryVo> data) {
        this.mData = data;
    }

    @Override
    public SearchHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_search_history, null));
    }

    @Override
    public void onBindViewHolder(SearchHistoryViewHolder holder, int position) {
        LiveSearchHistoryVo searchInfo = mData.get(position);
        holder.tvContent.setText(searchInfo.name);
        holder.tvContent.setTag(holder.tvContent.getId(), position);
        holder.tvContent.setOnClickListener(LiveSearchHistoryAdapter.this);
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

    static class SearchHistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvContent;

        public SearchHistoryViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }
    }
}
