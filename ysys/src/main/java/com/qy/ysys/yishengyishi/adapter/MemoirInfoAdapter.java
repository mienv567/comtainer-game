package com.qy.ysys.yishengyishi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qy.ysys.yishengyishi.BaseRecycleViewAdapter;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.adapter.viewholder.MemoirListViewHolder;
import com.qy.ysys.yishengyishi.model.item.MemoirInfoImageItem;
import com.qy.ysys.yishengyishi.views.ImagePagerActivity;
import com.qy.ysys.yishengyishi.views.MemoirInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：tracy.lee on 2017/1/23 0023 10:32
 */
public class MemoirInfoAdapter extends BaseRecycleViewAdapter {

    private Context mContext;

    public MemoirInfoAdapter(Context context){
        mContext = context;
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memoir_list, parent, false);
        RecyclerView.ViewHolder holder = new MemoirListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MemoirListViewHolder mHolder = (MemoirListViewHolder)holder;
        MemoirInfoImageItem item = (MemoirInfoImageItem)datas.get(position);
        Glide.with(mContext).load(item.getImageUrl()).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                placeholder(R.color.bg_no_photo).
                into(mHolder.mImageView);
        mHolder.mTitle.setVisibility(View.GONE);
        mHolder.mPicNum.setVisibility(View.GONE);
        if(getDatas()!= null && getDatas().size() >0){
            mHolder.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(mHolder.mImageView.getMeasuredWidth(), mHolder.mImageView.getMeasuredHeight());
                    ImagePagerActivity.startImagePagerActivity(((MemoirInfoActivity) mContext), getUrlList(), position, imageSize);

                }
            });
        }
    }

    private List<String> getUrlList(){
        List<String> urls = new ArrayList<>();
        if(getDatas()!= null && getDatas().size() >0){
            for(MemoirInfoImageItem item : (List<MemoirInfoImageItem>)getDatas()){
                urls.add(item.getImageUrl());
            }
        }
        return urls;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
