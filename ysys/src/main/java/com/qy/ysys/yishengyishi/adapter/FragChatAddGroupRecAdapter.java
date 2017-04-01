package com.qy.ysys.yishengyishi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.model.ModelFramilyChat;
import com.qy.ysys.yishengyishi.views.customviews.CircleImageView;

import java.util.List;


/**
 * Created by tony.chen on 2017/1/5.
 */

public class FragChatAddGroupRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ModelFramilyChat> mdata;
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

    public FragChatAddGroupRecAdapter(Context context, List<ModelFramilyChat> data) {
        this.mdata = data;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    //RecyclerView显示的子View
    //该方法返回是ViewHolder，当有可复用View时，就不再调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        if (type == ModelFramilyChat.CUSTOM) {
            View v = mInflater.inflate(R.layout.item_chat_addgroup_custom, null);
            return new ViewHolderCustom(v, itemClickListener);
        } else {
            View v = mInflater.inflate(R.layout.item_chat_addgroup, null);
            return new ViewHolderNormal(v, itemClickListener);
        }
    }

    //将数据绑定到子View，会自动复用View
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (mdata.get(position).getModelType() == ModelFramilyChat.NORMAL) {
//            ((ViewHolderNormal) viewHolder).tv_menberCount.setText(mdata.get(position).getMenberCount() + "");
            ((ViewHolderNormal) viewHolder).tv_groupname.setText(mdata.get(position).getGroupName());
//            switch (position % 5) {
//                case 0:
//                    ((ViewHolderNormal) viewHolder).container.setBackgroundResource(R.drawable.bg_chat_roundedrectangle_orange);
//                    break;
//                case 1:
//                    ((ViewHolderNormal) viewHolder).container.setBackgroundResource(R.drawable.bg_chat_roundedrectangle_yellow);
//                    break;
//                case 2:
//                    ((ViewHolderNormal) viewHolder).container.setBackgroundResource(R.drawable.bg_chat_roundedrectangle_green);
//                    break;
//                case 3:
//                    ((ViewHolderNormal) viewHolder).container.setBackgroundResource(R.drawable.bg_chat_roundedrectangle_purple);
//                    break;
//                case 4:
//                    ((ViewHolderNormal) viewHolder).container.setBackgroundResource(R.drawable.bg_chat_roundedrectangle_blue);
//                    break;
//                default:
//                    ((ViewHolderNormal) viewHolder).container.setBackgroundResource(R.drawable.bg_chat_roundedrectangle_orange);
//                    break;
//            }


        } else {
//            ((ViewHolderCustom) viewHolder).iv_head.setBackgroundResource(R.mipmap.ic_selected_male);
            ((ViewHolderCustom) viewHolder).tv_title.setText("自定义");
        }
    }

    //RecyclerView显示数据条数
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mdata.get(position).getModelType();
    }


    class ViewHolderNormal extends RecyclerView.ViewHolder {
        TextView tv_menberCount;
        TextView tv_groupname;
        View container;
        CircleImageView cir_head;
        ItemClickListener itemClickListener;

        //在布局中找到所含有的UI组件
        public ViewHolderNormal(View itemView, ItemClickListener listener) {
            super(itemView);
            itemClickListener = listener;
            tv_menberCount = (TextView) itemView.findViewById(R.id.tv_menbercount);
            tv_groupname = (TextView) itemView.findViewById(R.id.tv_groupname);
            cir_head = (CircleImageView) itemView.findViewById(R.id.cir_head);
            container = itemView.findViewById(R.id.rl_container);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != itemClickListener) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

        }
    }

    class ViewHolderCustom extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_head;
        View container;
        ItemClickListener itemClickListener;


        public ViewHolderCustom(View itemView, ItemClickListener listener) {
            super(itemView);
            itemClickListener = listener;
            container = itemView.findViewById(R.id.rl_container);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_head = (ImageView) itemView.findViewById(R.id.iv_head);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != itemClickListener) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}