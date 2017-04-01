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
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.LiveGiftModel;

import java.lang.ref.WeakReference;
import java.util.List;

public class LiveGiftAdapter extends SDSimpleAdapter<LiveGiftModel>
{
    private LiveGiftAdapterListener listener;

    public void setListener(LiveGiftAdapterListener listener)
    {
        this.listener = listener;
    }

    public LiveGiftAdapter(List<LiveGiftModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_gift;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final LiveGiftModel model)
    {
        View ll_live = get(R.id.ll_live_gift,convertView);
        ImageView iv_gift = get(R.id.iv_gift, convertView);
        ImageView iv_much = get(R.id.iv_much, convertView);
        TextView tv_price = get(R.id.tv_price, convertView);
        TextView tv_score = get(R.id.tv_score, convertView);
        TextView tv_gift_num = get(R.id.iv_gift_num,convertView);
        if (model.isSelected())
        {
            ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_selected));
            if(TextUtils.isEmpty(model.getPreviewUrl())){
                SDViewBinder.setImageView(getActivity(), model.getIcon(), iv_gift);
            }else{
                Glide.with(getActivity())
                        .load(model.getPreviewUrl()).dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_gift);
//                SDViewBinder.setImageView(getActivity(), model.getPreviewUrl(), iv_gift);
            }
//            SDViewUtil.hide(iv_much);
        } else
        {
            SDViewBinder.setImageView(getActivity(), model.getIcon(), iv_gift);
            ll_live.setBackgroundDrawable(SDResourcesUtil.getResources().getDrawable(R.drawable.bg_room_gift_not_selected));
            if (model.getIs_much() == 1)
            {
                SDViewUtil.show(iv_much);
            } else
            {
                SDViewUtil.hide(iv_much);
            }
        }
        if(model.getSuperimposedType() == 1){
            if(model.getPropCount() > 0){
                SDViewBinder.setTextView(tv_gift_num, String.valueOf(model.getPropCount()));
                tv_gift_num.setVisibility(View.VISIBLE);
            }else{
                tv_gift_num.setVisibility(View.GONE);
            }
        }else{
            tv_gift_num.setVisibility(View.GONE);
        }
        SDViewBinder.setTextView(tv_price, String.valueOf(model.getDiamonds()));
        find(R.id.iv_diamond, convertView).setVisibility(View.VISIBLE);
        SDViewBinder.setTextView(tv_score, model.getScoreFromat());
        convertView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (listener != null)
                {
                    listener.onClickItem(position, model, LiveGiftAdapter.this);
                }
            }
        });
		model.setHodler(new WeakReference<>(convertView));

        SDViewUtil.show(iv_much);
    }

    public interface LiveGiftAdapterListener
    {
        void onClickItem(int position, LiveGiftModel model, LiveGiftAdapter adapter);
    }

}
