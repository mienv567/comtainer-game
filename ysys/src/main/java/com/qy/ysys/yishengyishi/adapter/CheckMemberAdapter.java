package com.qy.ysys.yishengyishi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.qy.ysys.yishengyishi.model.ModelTreeNodes;

/**
 * Created by tony.chen on 2017/1/9.
 */

public class CheckMemberAdapter extends RecyclerView.Adapter<CheckMemberAdapter.CheckMemberViewHolder> {
    private ModelTreeNodes modelTreeNodes;
    private Context context;

    public ItemOnclicListener getItemOnclicListener() {
        return itemOnclicListener;
    }

    public void setItemOnclicListener(ItemOnclicListener itemOnclicListener) {
        this.itemOnclicListener = itemOnclicListener;
    }

    private ItemOnclicListener itemOnclicListener;

    public interface ItemOnclicListener {
        void onclick(View v, int position);
    }

    public CheckMemberAdapter(ModelTreeNodes modelTreeNodes, Context context) {
        this.modelTreeNodes = modelTreeNodes;
        this.context = context;
    }

    @Override
    public CheckMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CheckMemberViewHolder holder, int position) {

    }

    class CheckMemberViewHolder extends RecyclerView.ViewHolder {


        public CheckMemberViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return modelTreeNodes.getNodes().size();
    }
}
