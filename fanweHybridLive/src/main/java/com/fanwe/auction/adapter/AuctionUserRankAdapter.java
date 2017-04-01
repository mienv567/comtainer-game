package com.fanwe.auction.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.auction.appview.room.RoomTimerTextView;
import com.fanwe.auction.model.PaiBuyerModel;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class AuctionUserRankAdapter extends SDSimpleAdapter<PaiBuyerModel>
{
    public AuctionUserRankAdapter(List<PaiBuyerModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_view_auction_user_rank_list;
    }

    @Override
    public void bindData(final int position, final View convertView, ViewGroup parent, final PaiBuyerModel model)
    {
        ImageView iv_rank_number = get(R.id.iv_rank_number, convertView);
        ImageView iv_head_image = get(R.id.iv_head_image, convertView);
        TextView tv_nick_name = get(R.id.tv_nick_name, convertView);
        TextView tv_auction_price = get(R.id.tv_auction_price, convertView);
        RoomTimerTextView tv_status = get(R.id.tv_status, convertView);
        SDViewBinder.setImageView(getActivity(),model.getHead_image(), iv_head_image);
        tv_nick_name.setText(model.getNick_name());
        tv_auction_price.setText(model.getPai_diamonds());
        tv_status.setTime(model.getType(), model.getLeft_time());
        if (position == 0)
        {
            iv_rank_number.setImageResource(R.drawable.bg_auction_numberone);
        } else if (position == 1)
        {
            iv_rank_number.setImageResource(R.drawable.bg_auction_numbertwo);
        } else if (position == 2)
        {
            iv_rank_number.setImageResource(R.drawable.bg_auction_numberthree);
        }
        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                itemClickListener.onClick(position, model, convertView);
            }
        });
    }
}
