package com.fanwe.shopping.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.shopping.model.ShopMystoreListItemModel;
import com.fanwe.shopping.view.SwipeLayout;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ShoppingPodCastAdapter extends SDSimpleAdapter<ShopMystoreListItemModel> implements SwipeLayout.OnSwipeStateChangeListener
{

    private String createrId;//主播Id
    private ItemClickListener<ShopMystoreListItemModel> clickToDetailListener;
    private ItemClickListener<ShopMystoreListItemModel> clickAddCartListener;
    private ItemClickListener<ShopMystoreListItemModel> clickPushListener;
    private ItemClickListener<ShopMystoreListItemModel> clickDelGoodListener;
    private UserModel dao = UserModelDao.query();

    public void setClickToDetailListener(ItemClickListener<ShopMystoreListItemModel> clickToDetailListener) {
        this.clickToDetailListener = clickToDetailListener;
    }

    public void setClickAddCartListener(ItemClickListener<ShopMystoreListItemModel> clickAddCartListener) {
        this.clickAddCartListener = clickAddCartListener;
    }

    public void setClickPushListener(ItemClickListener<ShopMystoreListItemModel> clickPushListener) {
        this.clickPushListener = clickPushListener;
    }

    public void setClickDelGoodListener(ItemClickListener<ShopMystoreListItemModel> clickDelGoodListener) {
        this.clickDelGoodListener = clickDelGoodListener;
    }

    public ShoppingPodCastAdapter(List<ShopMystoreListItemModel> listModel, Activity activity, String id)
    {
        super(listModel, activity);
        createrId = id;
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_shopping_pod_cast;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final ShopMystoreListItemModel model)
    {
        SwipeLayout swipeLayout = ViewHolder.get(R.id.swipeLayout,convertView);
        LinearLayout ll_all = ViewHolder.get(R.id.ll_all,convertView);
        ImageView img_pod = ViewHolder.get(R.id.img_pod, convertView);//商品图片
        TextView txv_pod = ViewHolder.get(R.id.txv_pod, convertView);//商品名称
        TextView txv_price = ViewHolder.get(R.id.txv_price, convertView);//商品价格
        TextView txv_add_cart = ViewHolder.get(R.id.txv_add_cart, convertView);//加入购物车
        TextView txv_del = ViewHolder.get(R.id.txv_del, convertView);//商品删除

        SDViewBinder.setImageView(getActivity(),model.getImgs().get(0), img_pod);
        SDViewBinder.setTextView(txv_pod, model.getName());
        SDViewBinder.setTextView(txv_price, "¥ " + model.getPrice());

        if (dao.getUserId().equals(createrId))
        {
            txv_del.setVisibility(View.VISIBLE);
            swipeLayout.setTag(position);
            swipeLayout.setOnSwipeStateChangeListener(this);

            /**
             * 推送
             */
            txv_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickPushListener != null)
                    {
                        clickPushListener.onClick(position, model, view);
                    }
                }
            });
        }else
        {
            SDViewBinder.setTextView(txv_add_cart, "购买");
            txv_del.setVisibility(View.GONE);

            /**
             * 跳转至详情页
             */
            ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickToDetailListener != null)
                    {
                        clickToDetailListener.onClick(position, model, view);
                    }
                }
            });

            /**
             * 加入购物车跳转至详情页
             */
            txv_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickAddCartListener != null)
                    {
                        clickAddCartListener.onClick(position, model, view);
                    }
                }
            });
        }

        /**
         * 删除购物商品
         */
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
