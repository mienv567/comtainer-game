package com.fanwe.shopping.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import com.fanwe.auction.appview.ShoppingAddGoodsView;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.shopping.model.ShopMystoreListItemModel;

/**
 * Created by shibx on 2016/9/22.
 */

public class ShoppingAddGoodsEmptyActivity extends BaseActivity {

    private ShoppingAddGoodsView mAddGoodsView;//新增购物商品

    public static final String EXTRA_MODEL = "extra_model";

    //全屏参数
    private ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_empty_ll_content);
        init();
    }


    private void init() {
        addView();
    }

    private void addView() {
        ShopMystoreListItemModel model = (ShopMystoreListItemModel) getIntent().getSerializableExtra(EXTRA_MODEL);
        mAddGoodsView = new ShoppingAddGoodsView(this,model);
        SDViewUtil.replaceView(find(R.id.ll_content), mAddGoodsView,layoutParams);
    }
}
