package com.fanwe.shopping.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.fanwe.shopping.adapter.ShoppingPodCastAdapter;
import com.fanwe.shopping.model.App_shop_mystoreActModel;
import com.fanwe.shopping.model.ShopMystoreListItemModel;
import com.fanwe.shopping.view.SwipeLayoutManager;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ShoppingPodCastView extends BaseAppView
{

    @ViewInject(R.id.list_pod_cast)
    private SDProgressPullToRefreshListView listView;
    @ViewInject(R.id.ll_nothing)
    private LinearLayout ll_nothing;
    @ViewInject(R.id.img_shop_cart)
    private ImageView img_shop_cart;
    @ViewInject(R.id.btn_store)
    private Button btn_store;

    private ShoppingPodCastAdapter adapter;
    private List<ShopMystoreListItemModel> listModel;
    private String createrId;//主播Id

    public ShoppingPodCastView(Context context, String id)
    {
        super(context);
        createrId = id;
        init();
    }

    public ShoppingPodCastView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ShoppingPodCastView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_pod_cast);
        register();
        bindAdapterPodCast();
        refreshViewer();
    }

    private void register()
    {
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
        {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                refreshViewer();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {

            }
        });

        img_shop_cart.setOnClickListener(this);
        btn_store.setOnClickListener(this);
    }

    private void bindAdapterPodCast()
    {
        listModel = new ArrayList<ShopMystoreListItemModel>();
        adapter = new ShoppingPodCastAdapter(listModel, getActivity(),createrId);
        listView.setAdapter(adapter);

        /**
         * 侧滑监听
         */
        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    //如果垂直滑动，则需要关闭已经打开的layout
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount)
            {

            }
        });

        /**
         * 跳转至详情页
         */
        adapter.setClickToDetailListener(new SDAdapter.ItemClickListener<ShopMystoreListItemModel>() {
            @Override
            public void onClick(int position, ShopMystoreListItemModel item, View view) {
                requestToDetail();
            }
        });

        /**
         * 加入购物车跳转至详情页
         */
        adapter.setClickAddCartListener(new SDAdapter.ItemClickListener<ShopMystoreListItemModel>() {
            @Override
            public void onClick(int position, ShopMystoreListItemModel item, View view) {
                requestShopAddCart();
            }
        });

        /**
         * 推送
         */
        adapter.setClickPushListener(new SDAdapter.ItemClickListener<ShopMystoreListItemModel>() {
            @Override
            public void onClick(int position, ShopMystoreListItemModel item, View view) {
                requestPush();
            }
        });

        /**
         * 删除购物商品
         */
        adapter.setClickDelGoodListener(new SDAdapter.ItemClickListener<ShopMystoreListItemModel>()
        {
            @Override
            public void onClick(int position, ShopMystoreListItemModel item, View view)
            {
                String proId = item.getId();
                requestShopDelGood(proId);
            }
        });
    }

    /**
     * 跳转至详情页
     */
    private void requestToDetail()
    {
        SDToast.showToast("跳转至详情页成功");
    }

    /**
     * 加入购物车跳转至详情页
     */
    private void requestShopAddCart()
    {
        SDToast.showToast("加入购物车成功");
    }

    /**
     * 推送
     */
    private void requestPush()
    {
        SDToast.showToast("推送成功");
    }

    /**
     * 删除购物商品
     * @param id
     */
    private void requestShopDelGood(String id)
    {
        ShoppingCommonInterface.requestShopDelGoods(Integer.parseInt(id), new AppRequestCallback<BaseActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                SDToast.showToast("删除成功");
                //关闭
                SwipeLayoutManager.getInstance().closeCurrentLayout();
                adapter.notifyDataSetChanged();
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
    public void refreshViewer()
    {
        ShoppingCommonInterface.requestShopMystore(Integer.parseInt(createrId), new AppRequestCallback<App_shop_mystoreActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    if (actModel.getList() != null)
                    {
                        SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, false);
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                listView.onRefreshComplete();
            }
        });

    }

    private void showContent()
    {
        UserModel dao = UserModelDao.query();
        if (!dao.getUserId().equals(createrId))
        {
            btn_store.setVisibility(VISIBLE);
        }
        ll_nothing.setVisibility(GONE);
        listView.setVisibility(VISIBLE);
    }

    private void showNothing()
    {
        ll_nothing.setVisibility(VISIBLE);
        listView.setVisibility(GONE);
        btn_store.setVisibility(GONE);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.img_shop_cart:
                SDToast.showToast("加入购物车！");
                break;
            case R.id.btn_store:
                SDToast.showToast("进入主播星店！");
                break;
        }
    }
}
