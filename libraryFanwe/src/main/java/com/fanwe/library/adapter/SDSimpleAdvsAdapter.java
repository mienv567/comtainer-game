package com.fanwe.library.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.R;
import com.fanwe.library.utils.SDViewBinder;

import java.util.List;

public class SDSimpleAdvsAdapter<T> extends SDPagerAdapter<T>
{

    public SDSimpleAdvsAdapter(List<T> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public View getView(final ViewGroup container, final int position)
    {
        final View view = getLayoutInflater().inflate(R.layout.item_simple_adv, null);
        final ImageView ivAdv = (ImageView) view.findViewById(R.id.iv_image);

        T model = getItemModel(position);
        if (model != null)
        {
            String url = model.toString();
            SDViewBinder.setImageView(getActivity(),url, ivAdv);
        }
        return view;
    }

}
