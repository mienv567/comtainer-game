package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanwe.hybrid.model.LiveLikeModel;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class LiveLikeAdapter extends SDSimpleAdapter<LiveLikeModel>
{
    private LiveLikeAdapterListener listener;

    public void setListener(LiveLikeAdapterListener listener)
    {
        this.listener = listener;
    }

    public LiveLikeAdapter(List<LiveLikeModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_gift;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final LiveLikeModel model)
    {
        View ll_live = get(R.id.ll_live_gift,convertView);
        ImageView iv_gift = get(R.id.iv_gift, convertView);
        ImageView iv_much = get(R.id.iv_much, convertView);
        TextView tv_price = get(R.id.tv_price, convertView);
        TextView tv_score = get(R.id.tv_score, convertView);
        TextView tv_gift_num = get(R.id.iv_gift_num,convertView);
        LogUtils.logI("icon -- " + model.getIcon());
        if (model.isSelected())
        {
            ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_selected));
            if(TextUtils.isEmpty(model.getIcon())){
                SDViewBinder.setImageView(getActivity(), model.getIcon(), iv_gift);
            }else{
                Glide.with(getActivity())
                        .load(model.getIcon()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_gift);
                SDViewBinder.setImageView(getActivity(), model.getIcon(), iv_gift);
            }
            SDViewUtil.hide(iv_much);
        } else
        {
            SDViewBinder.setImageView(getActivity(), model.getIcon(), iv_gift);
            ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_not_selected));
//            if (model.getIs_much() == 1)
//            {
//                SDViewUtil.show(iv_much);
//            } else
//            {
                SDViewUtil.hide(iv_much);
//            }
        }
//        if(model.getSuperimposedType() == 1){
//            if(model.getPropCount() > 0){
//                SDViewBinder.setTextView(tv_gift_num, String.valueOf(model.getPropCount()));
//                tv_gift_num.setVisibility(View.VISIBLE);
//            }else{
//                tv_gift_num.setVisibility(View.GONE);
//            }
//        }else{
            tv_gift_num.setVisibility(View.GONE);
//        }
        find(R.id.iv_diamond, convertView).setVisibility(View.INVISIBLE);
        SDViewBinder.setTextView(tv_price, String.valueOf(model.getName()));
        SDViewBinder.setTextView(tv_score, "");
        convertView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (listener != null)
                {
                    listener.onClickItem(position, model, LiveLikeAdapter.this);
                }
            }
        });
		model.setHodler(new WeakReference<>(convertView));
    }

    public interface LiveLikeAdapterListener
    {
        void onClickItem(int position, LiveLikeModel model, LiveLikeAdapter adapter);
    }

}
