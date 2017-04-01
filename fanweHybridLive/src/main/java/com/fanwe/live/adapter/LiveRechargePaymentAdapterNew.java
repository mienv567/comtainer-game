package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.PayItemModel;

public class LiveRechargePaymentAdapterNew extends SDSimpleRecyclerAdapter<PayItemModel>
{

    private SDAdapter.ItemClickListener<PayItemModel> clickPaymentListener;

    public void setClickPaymentListener(SDAdapter.ItemClickListener<PayItemModel> clickPaymentListener)
    {
        this.clickPaymentListener = clickPaymentListener;
    }

    public LiveRechargePaymentAdapterNew(Activity activity)
    {
        super(activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_recharge_payment_new;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, final int position, final PayItemModel model) {
        super.bindData(holder, position, model);
        RelativeLayout lr_root_view = holder.get(R.id.lr_root_view);
        ImageView iv_image = holder.get(R.id.iv_image);
        TextView tv_name = holder.get(R.id.tv_name);
        ImageView iv_selected = holder.get(R.id.iv_selected);

        SDViewBinder.setImageView(getActivity(), model.getLogo(), iv_image);
        SDViewBinder.setTextView(tv_name, model.getName());
        if (model.isSelected())
        {
            lr_root_view.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_payment_bg_corner_5dp_selected));
            iv_selected.setImageResource(R.drawable.ic_recharge_pay_selected);
            SDViewUtil.show(iv_selected);
        } else
        {
            lr_root_view.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_payment_bg_corner_5dp));
            SDViewUtil.hide(iv_selected);
//            iv_selected.setImageResource(R.drawable.ic_live_pop_choose_off);
        }

        lr_root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickPaymentListener != null) {
                    clickPaymentListener.onClick(position, model, v);
                }
            }
        });
    }

}
