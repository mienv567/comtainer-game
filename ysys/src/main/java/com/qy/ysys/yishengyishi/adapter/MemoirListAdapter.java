package com.qy.ysys.yishengyishi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qy.ysys.yishengyishi.BaseRecycleViewAdapter;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.viewholder.MemoirListViewHolder;
import com.qy.ysys.yishengyishi.model.item.MemoirListItem;
import com.qy.ysys.yishengyishi.views.MemoirInfoActivity;

/**
 * 作者：tracy.lee on 2017/1/23 0023 10:32
 */
public class MemoirListAdapter extends BaseRecycleViewAdapter {

    private Context mContext;

    public MemoirListAdapter(Activity context){
        mContext = context;
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memoir_list, parent, false);
        RecyclerView.ViewHolder holder = new MemoirListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MemoirListViewHolder mHolder = (MemoirListViewHolder)holder;
        final MemoirListItem item = (MemoirListItem)datas.get(position);
        Glide.with(mContext).load(item.getCoverPath()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).into(mHolder.mImageView);
        mHolder.mTitle.setText(item.getFmName());
        mHolder.mPicNum.setText(item.getImageNum()+"张");
        mHolder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MemoirInfoActivity.class);
                intent.putExtra(MemoirInfoActivity.EXTRA_FM_ID,item.getFmId());
                intent.putExtra(MemoirInfoActivity.EXTRA_COVER_IMG,item.getCoverPath());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
