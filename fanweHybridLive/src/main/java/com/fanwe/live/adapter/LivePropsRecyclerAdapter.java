package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.LiveTaskDetailDialog;
import com.fanwe.live.model.GoodsDetailModel;

/**
 * Created by Yuan on 2017/3/18.
 * 邮箱：44004606@qq.com
 */

public class LivePropsRecyclerAdapter extends SDSimpleRecyclerAdapter<GoodsDetailModel> {
    LiveActivity activity;
    private LiveTaskDetailDialog mDetailDialog;

    public LivePropsRecyclerAdapter(Activity activity) {
        super(activity);
        this.activity = (LiveActivity) activity;
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_goods;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, int position, final GoodsDetailModel model) {
        final RelativeLayout rl_goods_item = holder.find(R.id.rl_goods_item);
        ImageView iv_gift = holder.find(R.id.iv_gift);
        TextView tv_good_title = holder.find(R.id.tv_good_title);
        TextView tv_good_price = holder.find(R.id.tv_good_price);
        TextView tv_buy_good = holder.find(R.id.tv_buy_good);
        if (tv_buy_good.getTag() == null) {
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPropsDetail(model, rl_goods_item);
                }
            };
            tv_buy_good.setOnClickListener(clickListener);
            tv_buy_good.setTag(clickListener);
            rl_goods_item.setOnClickListener(clickListener);
        }
        SDViewBinder.setImageView(getActivity(), model.getIcon(), iv_gift, R.drawable.ic_default_head);
        tv_good_title.setText(model.getTitle());
        tv_good_price.setText("￥" + model.getCharmCount());
    }

    private void showPropsDetail(final GoodsDetailModel model, View v) {
        if (mDetailDialog == null) {
            mDetailDialog = new LiveTaskDetailDialog(activity);
        }
        mDetailDialog.setTitle(model.getTitle());
        mDetailDialog.setAction("确认购买");
        mDetailDialog.setContent(model.getDesc());
        mDetailDialog.setOnClickListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailDialog.dismiss();
                requestBuyGoods(model);
            }
        });
        mDetailDialog.showLeft(v);
    }

    private void requestBuyGoods(GoodsDetailModel model) {
        CommonInterface.requestGoodsBuy(activity.getRoomId(), model.getGoodsId(), new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (rootModel.getStatus() == 1) {
                    SDToast.showToast("购买成功");
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }
}
