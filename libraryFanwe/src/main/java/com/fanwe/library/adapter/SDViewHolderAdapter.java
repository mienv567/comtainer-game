package com.fanwe.library.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.adapter.viewholder.SDViewHolder;

public abstract class SDViewHolderAdapter<T> extends SDAdapter<T>
{

	public SDViewHolderAdapter(List<T> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	protected void onUpdateView(int position, View convertView, ViewGroup parent, T model)
	{
		SDViewHolder holder = (SDViewHolder) convertView.getTag();
		holder.updateView(position, convertView, parent, model);
	}

	@Override
	public View onGetView(int position, View convertView, ViewGroup parent)
	{
		SDViewHolder holder = null;
		if (convertView == null)
		{
			holder = onCreateViewHolder(position, convertView, parent);
			holder.setActivity(mActivity);
			holder.setAdapter(this);

			int layoutId = holder.getLayoutId(position, convertView, parent);
			convertView = getLayoutInflater().inflate(layoutId, null);
			holder.initViews(position, convertView, parent);
			convertView.setTag(holder);
		} else
		{
			holder = (SDViewHolder) convertView.getTag();
		}
		holder.bindData(position, convertView, parent, getItem(position));
		return convertView;
	}

	public abstract SDViewHolder onCreateViewHolder(int position, View convertView, ViewGroup parent);

}
