package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.hybrid.app.App;
import com.fanwe.library.adapter.SDPagerAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.LiveBannerModel;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class LiveTabHotBannerPagerAdapter extends SDPagerAdapter<LiveBannerModel>
{

    public LiveTabHotBannerPagerAdapter(List<LiveBannerModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public View getView(final ViewGroup container, final int position)
    {
        View view = mActivity.getLayoutInflater().inflate(R.layout.item_live_tab_hot_banner_pager, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_image);

        final LiveBannerModel model = getItemModel(position);
        SDViewBinder.setImageView(App.getApplication(),model.getImage(), iv);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (itemClicklistener != null)
                {
                    itemClicklistener.onClick(position, model, v);
                }
            }
        });

        return view;
    }
}
