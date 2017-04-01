package com.fanwe.library.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class SDPagerAdapter<T> extends PagerAdapter
{

    private List<T> listModel = new ArrayList<T>();
    protected Activity mActivity;
    protected SDAdapter.ItemClickListener<T> itemClicklistener;
    protected View view;

    public void setView(View view)
    {
        this.view = view;
    }

    public void setItemClicklistener(SDAdapter.ItemClickListener<T> itemClicklistener)
    {
        this.itemClicklistener = itemClicklistener;
    }

    public SDPagerAdapter(List<T> listModel, Activity activity)
    {
        setData(listModel);
        this.mActivity = activity;
    }

    public LayoutInflater getLayoutInflater()
    {
        return mActivity.getLayoutInflater();
    }

    @Override
    public int getCount()
    {
        if (listModel != null)
        {
            return listModel.size();
        } else
        {
            return 0;
        }
    }

    public Activity getActivity()
    {
        return mActivity;
    }

    public boolean isPositionLegal(int position)
    {
        if (position >= 0 && position < listModel.size())
        {
            return true;
        }
        return false;
    }

    public T getItemModel(int position)
    {
        if (isPositionLegal(position))
        {
            return listModel.get(position);
        } else
        {
            return null;
        }
    }

    public void updateData(List<T> listModel)
    {
        setData(listModel);
        this.notifyDataSetChanged();
    }

    public List<T> getData()
    {
        return listModel;
    }

    public void setData(List<T> listModel)
    {
        if (listModel != null)
        {
            this.listModel = listModel;
        } else
        {
            this.listModel.clear();
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view = getView(container, position);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView((View) object);
    }

    public View getView(ViewGroup container, int position)
    {
        return null;
    }
}
