package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.RuleItemModel;


public class LiveRechrgePaymentRuleAdapterNew extends SDSimpleRecyclerAdapter<RuleItemModel>
{
    private SDAdapter.ItemClickListener<RuleItemModel> clickPaymentRuleListener;

    public void setClickPaymentRuleListener(SDAdapter.ItemClickListener<RuleItemModel> clickPaymentRuleListener)
    {
        this.clickPaymentRuleListener = clickPaymentRuleListener;
    }

    public LiveRechrgePaymentRuleAdapterNew(Activity activity)
    {
        super(activity);
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, final int position, final RuleItemModel model) {
        super.bindData(holder, position, model);
        RelativeLayout lr_root_view = holder.get(R.id.lr_root_view);
        TextView tv_diamonds = holder.get(R.id.tv_diamonds);
        TextView tv_name = holder.get(R.id.tv_name);
        TextView tv_money = holder.get(R.id.tv_money);

        SDViewBinder.setTextView(tv_diamonds, String.valueOf(model.getDiamonds()));
        SDViewBinder.setTextView(tv_name, model.getName());
        SDViewBinder.setTextView(tv_money, model.getMoneyFormat());

        lr_root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickPaymentRuleListener != null) {
                    clickPaymentRuleListener.onClick(position, model, v);
                }
            }
        });
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_recharge_payment_rule_new;
    }
}
