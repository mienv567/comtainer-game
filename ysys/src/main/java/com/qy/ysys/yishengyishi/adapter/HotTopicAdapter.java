package com.qy.ysys.yishengyishi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.viewholder.HotTopicHolder;

import java.util.List;

/**
 * Created by kevin.liu on 2017/3/24.
 */

public class HotTopicAdapter extends RecyclerView.Adapter {
    List data;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.adapter_hot_topic_item, null);
        return new HotTopicHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public void setData(List data) {
        this.data = data;
    }
}
