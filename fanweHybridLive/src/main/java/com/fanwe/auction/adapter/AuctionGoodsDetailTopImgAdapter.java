package com.fanwe.auction.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDPagerAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AuctionGoodsDetailTopImgAdapter extends SDPagerAdapter<String>
{
    public AuctionGoodsDetailTopImgAdapter(List<String> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public View getView(final ViewGroup container, final int position)
    {
        View view = mActivity.getLayoutInflater().inflate(R.layout.item_live_tab_hot_banner_pager, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_image);

        final String url = getItemModel(position);
        SDViewBinder.setImageView(getActivity(),url, iv);

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (itemClicklistener != null)
                {
                    itemClicklistener.onClick(position, url, v);
                }
            }
        });

        return view;
    }
}
