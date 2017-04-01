package com.fanwe.auction.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.fanwe.auction.appview.AuctionRealGoodsView;
import com.fanwe.auction.appview.AuctionVirtualGoodsView;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * Created by shibx on 2016/8/5.
 */
public class AuctionCreateAuctionActivity extends BaseActivity {

    private AuctionVirtualGoodsView mVirtualGoodsView;
    private AuctionRealGoodsView mRealGoodsView;

    /** 设置商品类型(int) 1为实体商品 0为虚拟商品*/
    public static final String EXTRA_VIEW_FLAG = "extra_view_flag";

    /** 实体商品的商品ID(int)*/
    public static final String EXTRA_GOODS_ID = "extra_goods_id";

    private int mFlag;
    //全屏参数
    private ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_auction_create_vir_auction);
        getExtraData();
        init();

    }

    private void getExtraData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mFlag = bundle.getInt(EXTRA_VIEW_FLAG);
        }
    }

    private void init() {
        addView();
    }

    private void addView() {
        if(mFlag == 1) {
            mRealGoodsView = new AuctionRealGoodsView(this);
            SDViewUtil.replaceView(find(R.id.ll_act_auction_create), mRealGoodsView,layoutParams);
        } else {
            mVirtualGoodsView = new AuctionVirtualGoodsView(this);
            SDViewUtil.replaceView(find(R.id.ll_act_auction_create), mVirtualGoodsView,layoutParams);
        }
    }
}
