package com.fanwe.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.fanwe.auction.activity.AuctionCreateEmptyActivity;
import com.fanwe.shopping.appview.ShoppingMystoreView;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;

public class ShoppingMystoreActivity extends BaseTitleActivity {

    @ViewInject(R.id.fl_content)
    private FrameLayout fl_content;
    private ShoppingMystoreView shoppingMystoreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_mystore);

        initTitle();
        addAuctionShopMystoreView();
    }

    private void addAuctionShopMystoreView() {
        shoppingMystoreView = new ShoppingMystoreView(ShoppingMystoreActivity.this);
        SDViewUtil.replaceView(fl_content, shoppingMystoreView);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("我的小店");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("添加");
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        super.onCLickRight_SDTitleSimple(v, index);
        Intent intent = new Intent(this, ShoppingAddGoodsEmptyActivity.class);
        startActivity(intent);
    }
}
