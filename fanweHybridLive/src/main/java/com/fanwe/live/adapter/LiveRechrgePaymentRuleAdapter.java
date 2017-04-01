package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.RuleItemModel;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class LiveRechrgePaymentRuleAdapter extends SDSimpleAdapter<RuleItemModel>
{
    private ItemClickListener<RuleItemModel> clickPaymentRuleListener;

    public void setClickPaymentRuleListener(ItemClickListener<RuleItemModel> clickPaymentRuleListener)
    {
        this.clickPaymentRuleListener = clickPaymentRuleListener;
    }

    public LiveRechrgePaymentRuleAdapter(List<RuleItemModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_recharge_payment_rule;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final RuleItemModel model)
    {
        TextView tv_diamonds = get(R.id.tv_diamonds, convertView);
        TextView tv_name = get(R.id.tv_name, convertView);
        TextView tv_money = get(R.id.tv_money, convertView);

        SDViewBinder.setTextView(tv_diamonds, String.valueOf(model.getDiamonds()));
        SDViewBinder.setTextView(tv_name, model.getName());
        SDViewBinder.setTextView(tv_money, model.getMoneyFormat());

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (clickPaymentRuleListener != null)
                {
                    clickPaymentRuleListener.onClick(position, model, v);
                }
            }
        });
    }
}
