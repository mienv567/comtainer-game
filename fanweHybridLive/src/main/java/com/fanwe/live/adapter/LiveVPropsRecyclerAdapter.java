package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.dialog.TaskOrGoodsDetailDialog;
import com.fanwe.live.model.BaseGoodsModel;
import com.fanwe.live.model.GoodsDetailModel;
import com.fanwe.live.model.GoodsTypeModel;

import static com.fanwe.library.utils.SDResourcesUtil.getString;

/**
 * Created by cheng.yuan on 2017/3/24.
 */

public class LiveVPropsRecyclerAdapter extends SDSimpleRecyclerAdapter<BaseGoodsModel> {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;
    private Activity mActivity;

    public LiveVPropsRecyclerAdapter(Activity activity) {
        super(activity);
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if (getData().get(position).isTitle) {
            return TYPE_TITLE;
        }
        return TYPE_CONTENT;
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                return R.layout.item_goods_group;
            case TYPE_CONTENT:
                return R.layout.item_goods_child;
        }
        return R.layout.include_list_empty;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, int position, BaseGoodsModel model) {
        super.bindData(holder, position, model);
        switch (getItemViewType(position)) {
            case TYPE_TITLE:
                GoodsTypeModel typeModel = (GoodsTypeModel) model;
                ImageView iv_goods_type = holder.get(R.id.iv_goods_type);
                TextView tv_goods_type = holder.get(R.id.tv_goods_type);
                switch (typeModel.getTypeId()) {
                    case 1:
                        iv_goods_type.setImageResource(R.drawable.ic_task_props);
                        break;
                    case 2:
                        iv_goods_type.setImageResource(R.drawable.ic_survice_props);
                        break;
                    case 3:
                        iv_goods_type.setImageResource(R.drawable.ic_task_props);
                        break;
                }
                tv_goods_type.setText(typeModel.getName());
                break;
            case TYPE_CONTENT:
                final GoodsDetailModel detailModel = (GoodsDetailModel) model;
                RelativeLayout rl_goods_item = holder.get(R.id.rl_goods_item);
                ImageView iv_goods_pic = holder.get(R.id.iv_goods_pic);
                TextView tv_goods_title = holder.get(R.id.tv_goods_title);
                TextView tv_goods_price = holder.get(R.id.tv_goods_price);
                Button btn_goods_buy = holder.get(R.id.btn_goods_buy);
                Glide.with(mActivity).load(detailModel.getIcon()).into(iv_goods_pic);
                tv_goods_title.setText(detailModel.getTitle());
                tv_goods_price.setText(String.valueOf(detailModel.getCharmCount()));
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showGoodsDetails(detailModel);
                    }
                };
                rl_goods_item.setOnClickListener(listener);
                btn_goods_buy.setOnClickListener(listener);
                break;
        }
    }

    private void showGoodsDetails(GoodsDetailModel childModel) {
        TaskOrGoodsDetailDialog goodsDetailDialog = new TaskOrGoodsDetailDialog(mActivity);
        goodsDetailDialog.setTitle(getString(R.string.goods_details));
        goodsDetailDialog.setContent(childModel.getDesc());
        goodsDetailDialog.setConfirmString(getString(R.string.confirm));
        goodsDetailDialog.show();
    }
}
