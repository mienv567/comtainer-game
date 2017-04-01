package com.fanwe.library.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.drawable.SDDrawableManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;

public class SDSimpleTextAdapter<T> extends SDSimpleAdapter<T>
{
	private SDDrawableManager mdDrawableManager = new SDDrawableManager();

	public SDSimpleTextAdapter(List<T> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{
		return R.layout.item_simple_text;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent, T model)
	{
		SDViewUtil.setBackgroundDrawable(convertView, mdDrawableManager.getSelectorWhiteGray(false));
		TextView tvName = get(R.id.item_simple_text_tv_name, convertView);
		SDViewBinder.setTextView(tvName, model.toString());
	}

}
