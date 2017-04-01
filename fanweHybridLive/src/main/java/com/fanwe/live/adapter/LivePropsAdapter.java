package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.live.R;
import com.fanwe.live.dialog.TaskOrGoodsDetailDialog;
import com.fanwe.live.model.GoodsDetailModel;
import com.fanwe.live.model.GoodsTypeModel;

import java.util.List;

import static com.fanwe.library.utils.SDResourcesUtil.getString;

/**
 * Created by cheng.yuan on 2017/3/16.
 * 竖屏界面下商品/道具列表
 */

public class LivePropsAdapter extends BaseExpandableListAdapter {

    private Activity mActivity;

    private List<GoodsTypeModel> mGoodsTypeModels;

    public LivePropsAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setGoodsTypeModels(List<GoodsTypeModel> goodsTypeModels) {
        mGoodsTypeModels = goodsTypeModels;
    }

    @Override
    public int getGroupCount() {
        return mGoodsTypeModels == null ? 0 : mGoodsTypeModels.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (mGoodsTypeModels != null && i < mGoodsTypeModels.size()) {
            GoodsTypeModel typeModel = mGoodsTypeModels.get(i);
            if (typeModel != null && typeModel.getGoods() != null) {
                return typeModel.getGoods().size();
            }
        }
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return mGoodsTypeModels.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        if (i < mGoodsTypeModels.size()) {
            GoodsTypeModel typeModel = mGoodsTypeModels.get(i);
            if (typeModel != null && typeModel.getGoods() != null) {
                return typeModel.getGoods().get(i1);
            }
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GoodsGroupHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_goods_group, parent, false);
            holder = new GoodsGroupHolder();
            holder.iv_goods_type = (ImageView) convertView.findViewById(R.id.iv_goods_type);
            holder.tv_goods_type = (TextView) convertView.findViewById(R.id.tv_goods_type);
            holder.iv_goods_type_arrow = (ImageView) convertView.findViewById(R.id.iv_goods_type_arrow);
            convertView.setTag(holder);
        } else {
            holder = (GoodsGroupHolder) convertView.getTag();
        }
        GoodsTypeModel typeModel = mGoodsTypeModels.get(groupPosition);
        switch (typeModel.getTypeId()) {
            case 1:
                holder.iv_goods_type.setImageResource(R.drawable.ic_task_props);
                break;
            case 2:
                holder.iv_goods_type.setImageResource(R.drawable.ic_survice_props);
                break;
            case 3:
                holder.iv_goods_type.setImageResource(R.drawable.ic_task_props);
                break;
        }
        holder.tv_goods_type.setText(typeModel.getName());
        if (isExpanded) {
            holder.iv_goods_type_arrow.setImageResource(R.drawable.ic_live_arrow_right);
        } else {
            holder.iv_goods_type_arrow.setImageResource(R.drawable.ic_live_arrow_down);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        GoodsChildHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_goods_child, parent, false);
            holder = new GoodsChildHolder();
            holder.iv_goods_pic = (ImageView) convertView.findViewById(R.id.iv_goods_pic);
            holder.tv_goods_title = (TextView) convertView.findViewById(R.id.tv_goods_title);
            holder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
            holder.btn_goods_buy = (Button) convertView.findViewById(R.id.btn_goods_buy);
            convertView.setTag(holder);
        } else {
            holder = (GoodsChildHolder) convertView.getTag();
        }
        List<GoodsDetailModel> goodsList = mGoodsTypeModels.get(groupPosition).getGoods();
        final GoodsDetailModel childModel = goodsList.get(childPosition);
        Glide.with(mActivity).load(childModel.getIcon()).into(holder.iv_goods_pic);
        holder.tv_goods_title.setText(childModel.getTitle());
        holder.tv_goods_price.setText(String.valueOf(childModel.getCharmCount()));
        holder.btn_goods_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGoodsDetails(childModel);
            }
        });
        return convertView;
    }

    private void showGoodsDetails(GoodsDetailModel childModel) {
        TaskOrGoodsDetailDialog goodsDetailDialog = new TaskOrGoodsDetailDialog(mActivity);
        goodsDetailDialog.setTitle(getString(R.string.goods_details));
        goodsDetailDialog.setContent(childModel.getDesc());
        goodsDetailDialog.setConfirmString(getString(R.string.confirm));
        goodsDetailDialog.show();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GoodsGroupHolder {
        ImageView iv_goods_type;//type 图标
        TextView tv_goods_type;//type标题
        ImageView iv_goods_type_arrow;//箭头
    }

    private class GoodsChildHolder {
        ImageView iv_goods_pic;//商品图片
        TextView tv_goods_title;//商品名称
        TextView tv_goods_price;//商品价格
        Button btn_goods_buy;//购买按钮
    }
}
