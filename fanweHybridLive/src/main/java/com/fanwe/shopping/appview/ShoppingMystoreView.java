package com.fanwe.shopping.appview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.fanwe.shopping.activity.ShoppingAddGoodsEmptyActivity;
import com.fanwe.shopping.adapter.ShoppingMystoreAdapter;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.SDProgressPullToRefreshListView;
import com.fanwe.shopping.common.ShoppingCommonInterface;
import com.fanwe.shopping.model.App_shop_mystoreActModel;
import com.fanwe.shopping.model.ShopMystoreListItemModel;
import com.fanwe.shopping.view.SwipeLayoutManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ShoppingMystoreView extends BaseAppView {
    @ViewInject(R.id.lv_store)
    private SDProgressPullToRefreshListView lv_store;
    private ShoppingMystoreAdapter adapter;
    private List<ShopMystoreListItemModel> listModel;

    public ShoppingMystoreView(Context context) {
        super(context);
        init();
    }

    public ShoppingMystoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShoppingMystoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.view_shopping_mystore);
        register();
        bindAdapterPodCast();
        refreshViewer();
    }

    private void register() {
        lv_store.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_store.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshViewer();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }

    private void bindAdapterPodCast() {
        listModel = new ArrayList<ShopMystoreListItemModel>();
        adapter = new ShoppingMystoreAdapter(listModel, getActivity());
        lv_store.setAdapter(adapter);

        /**
         * 侧滑监听
         */
        lv_store.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果垂直滑动，则需要关闭已经打开的layout
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        /**
         * 编辑购物商品
         */
        adapter.setClickEditCartListener(new SDAdapter.ItemClickListener<ShopMystoreListItemModel>() {
            @Override
            public void onClick(int position, ShopMystoreListItemModel item, View view) {
                requestShopEditCart(item);
            }
        });

        /**
         * 删除购物商品
         */
        adapter.setClickDelGoodListener(new SDAdapter.ItemClickListener<ShopMystoreListItemModel>() {
            @Override
            public void onClick(int position, ShopMystoreListItemModel item, View view) {
                String proId = item.getId();
                requestShopDelGood(position,item,proId);
            }
        });
    }

    /**
     * 编辑购物商品
     * @param item
     */
    private void requestShopEditCart(ShopMystoreListItemModel item) {
        Intent intent = new Intent(getActivity(), ShoppingAddGoodsEmptyActivity.class);
        //传递商品model
        intent.putExtra(ShoppingAddGoodsEmptyActivity.EXTRA_MODEL,item);
        getActivity().startActivity(intent);
    }

    /**
     * 删除购物商品
     *
     * @param position
     * @param id
     */
    private void requestShopDelGood(final int position, final ShopMystoreListItemModel item, String id) {
        ShoppingCommonInterface.requestShopDelGoods(Integer.parseInt(id), new AppRequestCallback<BaseActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                SDToast.showToast("删除成功");
                //关闭
                SwipeLayoutManager.getInstance().closeCurrentLayout();
                adapter.removeData(item);
            }
            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
            }
        });
    }

    /**
     * 获取商品列表数据
     */
    public void refreshViewer() {
        UserModel dao = UserModelDao.query();
        ShoppingCommonInterface.requestShopMystore(Integer.parseInt(dao.getUserId()), new AppRequestCallback<App_shop_mystoreActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (rootModel.isOk()) {
                    if (actModel.getList() != null) {
                        SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, false);
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                lv_store.onRefreshComplete();
            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }
}
