package com.fanwe.shopping.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.R;
import com.fanwe.shopping.model.ShopMystoreListItemModel;
import com.fanwe.shopping.view.SwipeLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ShoppingMystoreAdapter extends SDSimpleAdapter<ShopMystoreListItemModel> implements SwipeLayout.OnSwipeStateChangeListener
{

    private ItemClickListener<ShopMystoreListItemModel> clickEditCartListener;
    private ItemClickListener<ShopMystoreListItemModel> clickDelGoodListener;

    public void setClickEditCartListener(ItemClickListener<ShopMystoreListItemModel> clickEditCartListener) {
        this.clickEditCartListener = clickEditCartListener;
    }

    public void setClickDelGoodListener(ItemClickListener<ShopMystoreListItemModel> clickDelGoodListener) {
        this.clickDelGoodListener = clickDelGoodListener;
    }

    public ShoppingMystoreAdapter(List<ShopMystoreListItemModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_shopping_mystore;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final ShopMystoreListItemModel model)
    {
        SwipeLayout swipeLayout = ViewHolder.get(R.id.swipeLayout,convertView);
        ImageView img_pod = ViewHolder.get(R.id.img_pod, convertView);//商品图片
        TextView txv_pod = ViewHolder.get(R.id.txv_pod, convertView);//商品名称
        TextView txv_price = ViewHolder.get(R.id.txv_price, convertView);//商品价格
        TextView txv_edit_cart = ViewHolder.get(R.id.txv_edit_cart, convertView);//编辑商品
        TextView txv_del = ViewHolder.get(R.id.txv_del, convertView);//商品删除

        swipeLayout.setTag(position);
        swipeLayout.setOnSwipeStateChangeListener(this);

        SDViewBinder.setImageView(getActivity(),model.getImgs().get(0), img_pod);
        SDViewBinder.setTextView(txv_pod, model.getName());
        SDViewBinder.setTextView(txv_price, "¥ " + model.getPrice());

        txv_edit_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickEditCartListener != null)
                {
                    clickEditCartListener.onClick(position, model, view);
                }
            }
        });

        txv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickDelGoodListener != null)
                {
                    clickDelGoodListener.onClick(position, model, view);
                }
            }
        });

    }

    @Override
    public void onOpen(Object tag) {

    }

    @Override
    public void onClose(Object tag) {

    }
}
