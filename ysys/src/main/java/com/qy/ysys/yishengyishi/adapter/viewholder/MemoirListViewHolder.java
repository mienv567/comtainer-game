package com.qy.ysys.yishengyishi.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;

/**
 * 作者：tracy.lee on 2017/1/23 0023 10:34
 */
public class MemoirListViewHolder extends RecyclerView.ViewHolder{
    public LinearLayout mRootLayout;
    public ImageView mImageView;
    public TextView mTitle;
    public TextView mPicNum;

    public MemoirListViewHolder(View itemView) {
        super(itemView);
        mRootLayout = (LinearLayout) itemView.findViewById(R.id.ll_root_layout);
        mImageView = (ImageView)itemView.findViewById(R.id.iv_img);
        mTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mPicNum = (TextView) itemView.findViewById(R.id.tv_pic_num);

    }



}
