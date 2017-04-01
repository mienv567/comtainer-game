package com.qy.ysys.yishengyishi.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.HotTopicAdapter;
import com.qy.ysys.yishengyishi.widgets.SDRecyclerView;


import java.util.List;

/**
 * Created by kevin.liu on 2017/3/24.
 */


public class HeadHotTopicHolder extends RecyclerView.ViewHolder {
    public SDRecyclerView sdr_hot_topic;
    private HotTopicAdapter adapter;

    public HeadHotTopicHolder(View itemView) {
        super(itemView);
        sdr_hot_topic = (SDRecyclerView) itemView.findViewById(R.id.sdr_hot_topic);
        sdr_hot_topic.setGridVertical(2);
        adapter = new HotTopicAdapter();
        sdr_hot_topic.setAdapter(adapter);
    }

    public void setData(List data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

}
