package com.qy.ysys.yishengyishi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.model.item.UserInfo;
import com.qy.ysys.yishengyishi.views.customviews.CircleImageView;

import java.util.List;


/**
 * Created by tony.chen on 2017/1/5.
 */

public class FragEditGroupRecAdapter extends RecyclerView.Adapter<FragEditGroupRecAdapter.EditGroupViewHolder> {

    private List<UserInfo> mdata;
    private Context mContext;
    private LayoutInflater mInflater;

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private ItemClickListener itemClickListener;

    public interface ItemClickListener {
        public void onItemClick(View view, int postion);

    }

    public FragEditGroupRecAdapter(Context context, List<UserInfo> data) {
        this.mdata = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public EditGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_editgroup_member, null);
        return new EditGroupViewHolder(v, itemClickListener);
    }

    @Override
    public void onBindViewHolder(EditGroupViewHolder holder, int position) {
        holder.tv_userName.setText(mdata.get(position).getName());
        if (mdata.get(position).getName().equals("add")) {
            holder.tv_userName.setText("添加成员");
            holder.ic_head.setBackgroundResource(R.mipmap.ic_add_small);
        } else if (mdata.get(position).getName().equals("minus")) {
            holder.tv_userName.setText("删除成员");
            holder.ic_head.setBackgroundResource(R.mipmap.ic_jianhao_small);
        } else {
            holder.ic_head.setBackgroundResource(R.mipmap.ic_head);
        }

    }


    //RecyclerView显示数据条数
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class EditGroupViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ic_head;
        TextView tv_userName;

        public EditGroupViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            itemClickListener = listener;
            ic_head = (CircleImageView) itemView.findViewById(R.id.cir_head);
            tv_userName = (TextView) itemView.findViewById(R.id.tv_username);

        }
    }
}