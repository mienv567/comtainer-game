package com.fanwe.auction.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;

/**
 * Created by shibx on 2016/8/9.
 */
public class AuctionRealGoodsView extends BaseAppView{

    private TextView tv_deposit;
    private TextView tv_increase;
    private EditText et_auction_time;
    private EditText et_auction_delay;
    private EditText et_auction_delay_times;

    private ImageView iv_item_goods_pic;
    private TextView tv_item_goods_des;
    private TextView tv_item_goods_price;

    private SDTitleSimple mTitle;


    public AuctionRealGoodsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AuctionRealGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AuctionRealGoodsView(Context context) {
        super(context);
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        setContentView(R.layout.view_auction_create_real_goods);
        initView();
        requestData();
    }

    private void initView() {
        initTitle();
        tv_deposit = find(R.id.tv_deposit);
        tv_increase = find(R.id.tv_increase);
        et_auction_time = find(R.id.et_auction_time);
        et_auction_delay = find(R.id.et_auction_delay);
        et_auction_delay_times = find(R.id.et_auction_delay_times);
        iv_item_goods_pic = find(R.id.iv_item_goods_pic);
        tv_item_goods_des = find(R.id.tv_item_goods_des);
        tv_item_goods_price = find(R.id.tv_item_goods_price);
    }

    private void initTitle() {
        mTitle = find(R.id.title);
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
        mTitle.setMiddleTextTop("星店商品拍卖设置");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("发布");
        SDViewUtil.setTextViewColorResId(mTitle.getItemRight(0).mTvBot,R.color.main_color);

        mTitle.setmListener(new SDTitleSimple.SDTitleSimpleListener() {
            @Override
            public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
                //退出，有提示
                new SDDialogConfirm(getActivity()).setTextContent("是否放弃新增竞拍商品？").setTextGravity(Gravity.CENTER).setTextCancel("取消").setTextConfirm("确定").setmListener(new SDDialogCustom.SDDialogCustomListener() {
                    @Override
                    public void onClickCancel(View v, SDDialogCustom dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog) {
                        getActivity().finish();
                    }

                    @Override
                    public void onDismiss(SDDialogCustom dialog) {

                    }
                }).show();
            }

            @Override
            public void onCLickMiddle_SDTitleSimple(SDTitleItem v) {

            }

            @Override
            public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
                //发布

            }
        });
    }

    private void requestData() {
//        int good_id = getActivity().getIntent().getExtras().getInt(AuctionCreateEmptyActivity.EXTRA_GOODS_ID);
        tv_deposit.setText("2499");
        tv_increase.setText("999");
        SDViewBinder.setImageView(getActivity(),"",iv_item_goods_pic);
        tv_item_goods_des.setText("这商品是真的");
        tv_item_goods_price.setText("4");
    }
}
