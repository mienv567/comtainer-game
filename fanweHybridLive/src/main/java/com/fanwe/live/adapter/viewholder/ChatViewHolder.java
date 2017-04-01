package com.fanwe.live.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */
public class ChatViewHolder extends RecyclerView.ViewHolder {

    @ViewInject(R.id.ivFriend)
    public ImageView ivFriend;

    @ViewInject(R.id.ivMe)
    public ImageView ivMe;

    @ViewInject(R.id.tvMessage)
    public TextView tvMessage;

    public ChatViewHolder(View itemView) {
        super(itemView);

        x.view().inject(itemView);
        ivFriend = (ImageView) itemView.findViewById(R.id.ivFriend);
        ivMe = (ImageView) itemView.findViewById(R.id.ivMe);
        tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
    }
}
