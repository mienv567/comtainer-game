package com.qy.ysys.yishengyishi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.model.ModelTreeNode;

import java.util.List;

/**
 * Created by tony.chen on 2017/1/10.
 */

public class EditTreeNodeAdapter extends RecyclerView.Adapter<EditTreeNodeAdapter.OverViewViewHolder> {

    private List<ModelTreeNode> mData;
    private Context mContext;

    public void setItemClickListener(EditTreeNodeAdapter.ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private EditTreeNodeAdapter.ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int index);
    }

    public EditTreeNodeAdapter(List<ModelTreeNode> list, Context context) {
        mContext = context;
        mData = list;

    }

    @Override
    public OverViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(mContext, R.layout.item_edittreeadapter, null);
        return new OverViewViewHolder(v, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(OverViewViewHolder holder, final int position) {
        ModelTreeNode modelTreeNode = mData.get(position);
        int gender = modelTreeNode.getGender();
        if (gender == 0) {
            holder.iv_background.setImageResource(R.mipmap.ic_male_liang);
            holder.tv_name.setBackgroundColor(Color.parseColor("#1295DA"));
        } else {
            holder.iv_background.setImageResource(R.mipmap.ic_female_liang);
            holder.tv_name.setBackgroundColor(Color.parseColor("#D71E06"));
        }
        holder.tv_name.setText(mData.get(position).getName());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemClickListener) {
                    mItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class OverViewViewHolder extends RecyclerView.ViewHolder {
        private EditTreeNodeAdapter.ItemClickListener itemClickListener;

        private View container;
        private ImageView iv_background;
        private TextView tv_name;

        public OverViewViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);
            iv_background = (ImageView) itemView.findViewById(R.id.iv_overview_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_overview_name);
            container = itemView.findViewById(R.id.rl_container);
            itemClickListener = clickListener;

        }
    }
}
