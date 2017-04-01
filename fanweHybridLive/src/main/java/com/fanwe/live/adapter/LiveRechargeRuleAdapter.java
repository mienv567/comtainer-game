package com.fanwe.live.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.model.RuleItemModel;
import com.fanwe.live.R;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-7 上午11:58:35 类说明
 */
public class LiveRechargeRuleAdapter extends SDSimpleAdapter<RuleItemModel>
{
	// 默认选择第一个
	private int mCurSelected = 0;

	public RuleItemModel getCurrentSelectedModel()
	{
		if (listModel != null && listModel.size() > 0 && mCurSelected >= 0)
		{
			return listModel.get(mCurSelected);
		} else
		{
			return null;
		}
	}

	public LiveRechargeRuleAdapter(List<RuleItemModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public int getItemViewType(int position)
	{
		if (listModel != null && listModel.size() > 0 && listModel.size() - 1 == position)
		{
			return 1;
		} else
		{
			return 0;
		}

	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{
		int layout = 0;
		switch (getItemViewType(position))
		{
		case 0:
			layout = R.layout.item_live_recharge_rule;
			break;
		case 1:
			layout = R.layout.item_live_recharge_rule_other;
			break;

		}
		return layout;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent, RuleItemModel model)
	{
		if (getItemViewType(position) == 1)
		{
			bindRuleOtherData(position, convertView, parent, model);
		} else
		{
			bindRuleData(position, convertView, parent, model);
		}

	}

	private void bindRuleData(final int position, View convertView, ViewGroup parent, final RuleItemModel model)
	{
		final LinearLayout ll_selected = ViewHolder.get(R.id.ll_selected, convertView);

		ll_selected.setSelected(model.isSelected());
		ll_selected.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (ll_selected.isSelected())
				{
					return;
				}

				model.setSelected(true);
				ll_selected.setSelected(true);
				if (mCurSelected != -1)
				{
					listModel.get(mCurSelected).setSelected(false);
				}

				mCurSelected = position;
				notifyDataSetChanged();
			}
		});
		TextView tv_diamonds = ViewHolder.get(R.id.tv_diamonds, convertView);
		TextView tv_money = ViewHolder.get(R.id.tv_money, convertView);
		SDViewBinder.setTextView(tv_diamonds, model.getDiamonds() + "");
		SDViewBinder.setTextView(tv_money, model.getMoneyFormat());

		if (model.isSelected())
		{
			tv_diamonds.setTextColor(SDResourcesUtil.getColor(R.color.white));
		} else
		{
			tv_diamonds.setTextColor(SDResourcesUtil.getColor(R.color.text_black));
		}
	}

	private void bindRuleOtherData(final int position, final View convertView, final ViewGroup parent, final RuleItemModel model)
	{
		convertView.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mOnListenerOtherConvertView != null)
				{
					mOnListenerOtherConvertView.onListenerOtherConvertView(position, convertView, parent, model);
				}
			}
		});
	}

	public OnListenerOtherConvertView mOnListenerOtherConvertView;

	public OnListenerOtherConvertView getOnListenerOtherConvertView()
	{
		return mOnListenerOtherConvertView;
	}

	public void setOnListenerOtherConvertView(OnListenerOtherConvertView onListenerOtherConvertView)
	{
		this.mOnListenerOtherConvertView = onListenerOtherConvertView;
	}

	public interface OnListenerOtherConvertView
	{
		void onListenerOtherConvertView(int position, View convertView, ViewGroup parent, RuleItemModel model);
	}
}
