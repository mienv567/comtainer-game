package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.common.CommonOpenLoginSDK;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.event.EJsWxBackInfo;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.SDVisibilityHandler;
import com.fanwe.library.utils.UrlLinkBuilder;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_InitH5Model;
import com.fanwe.live.model.App_ProfitBindingActModel;
import com.fanwe.live.model.App_profitActModel;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by shibx on 2016/7/18.
 */
public class LiveUserProfitActivity extends BaseTitleActivity implements SDDialogCustom.SDDialogCustomListener{

//    @ViewInject(R.id.tv_useable_ticket)
//    private TextView tv_useable_ticket;//钱票
    @ViewInject(R.id.tv_reward)
    private TextView tv_reward;//红包
//    @ViewInject(R.id.tv_profit_unit)
//    private TextView tv_profit_unit;

    @ViewInject(R.id.ll_income_auction_content)
    private LinearLayout ll_income_auction_content;
    @ViewInject(R.id.ll_income_sell_content)
    private LinearLayout ll_income_sell_content;
    @ViewInject(R.id.ll_income_auction_summary)
    private LinearLayout ll_income_auction_summary;
    @ViewInject(R.id.ll_income_auction_detail)
    private LinearLayout ll_income_auction_detail;
    @ViewInject(R.id.iv_auction_arrow)
    private ImageView iv_auction_arrow;
    @ViewInject(R.id.tv_auction_income)
    private TextView tv_auction_income;
    @ViewInject(R.id.tv_auction_income_detail)
    private TextView tv_auction_income_detail;
    @ViewInject(R.id.tv_auction_income_wait)
    private TextView tv_auction_income_wait;
    @ViewInject(R.id.ll_income_sell_summary)
    private LinearLayout ll_income_sell_summary;
    @ViewInject(R.id.ll_income_sell_detail)
    private LinearLayout ll_income_sell_detail;
    @ViewInject(R.id.iv_sell_arrow)
    private ImageView iv_sell_arrow;
    @ViewInject(R.id.tv_sell_income)
    private TextView tv_sell_income;
    @ViewInject(R.id.tv_sell_income_detail)
    private TextView tv_sell_income_detail;
    @ViewInject(R.id.tv_sell_income_wait)
    private TextView tv_sell_income_wait;
    @ViewInject(R.id.tv_auction_manage)
    private TextView tv_auction_manage;
    @ViewInject(R.id.tv_sell_detail)
    private TextView tv_sell_detail;
    @ViewInject(R.id.tv_do_exchange)
    private TextView tv_do_exchange;//兑换
    @ViewInject(R.id.tv_take_reward)
    private TextView tv_take_reward;//领取劳务费
    @ViewInject(R.id.tv_get_rule)
    private TextView tv_get_rule;//领取规则
    @ViewInject(R.id.tv_month_profit)
    private TextView tv_month_profit;//本月收益
    @ViewInject(R.id.tv_total_profit)
    private TextView tv_total_profit;//累计收益
    private int subscribe ;//是否关注微信公众号
    private int mobile_exist;//是否绑定手机
    private int binding_wx;//是否绑定微信

    private String subscription; //公众号名称

    private SDDialogConfirm mDialog;

    private SDVisibilityHandler mViewHandller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_user_profit);
        init();
    }

    private void init() {
        initTitle();
        mViewHandller = new SDVisibilityHandler();
//        tv_profit_unit.setText("获得"+ AppRuntimeWorker.getTicketName());
//        tv_take_reward.setTextColor(SDDrawable.getStateListColor(getResources().getColor(R.color.main_color), getResources().getColor(R.color.white)));
        tv_do_exchange.setTextColor(SDDrawable.getStateListColor(getResources().getColor(R.color.main_color),getResources().getColor(R.color.white)));
        tv_auction_manage.setTextColor(SDDrawable.getStateListColor(getResources().getColor(R.color.main_color),getResources().getColor(R.color.white)));
        tv_sell_detail.setTextColor(SDDrawable.getStateListColor(getResources().getColor(R.color.main_color),getResources().getColor(R.color.white)));
        tv_get_rule.setText(Html.fromHtml("<u>"+getString(R.string.profit_rule)+"</u>"));
        ll_income_auction_summary.setOnClickListener(this);
        ll_income_sell_summary.setOnClickListener(this);
        tv_auction_manage.setOnClickListener(this);
        tv_sell_detail.setOnClickListener(this);
        tv_do_exchange.setOnClickListener(this);
        tv_take_reward.setOnClickListener(this);
        tv_get_rule.setOnClickListener(this);
    }

    private void requestProfit() {
        showProgressDialog("");
        CommonInterface.requestProfit(new AppRequestCallback<App_profitActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if(rootModel.isOk()) {

//                    tv_useable_ticket.setText(actModel.getUseableTicket());
                    tv_reward.setText(getString(R.string.money_kind)+actModel.getMoney());
                    tv_month_profit.setText(getString(R.string.money_kind)+actModel.getMonth_return());
                    tv_total_profit.setText(getString(R.string.money_kind)+actModel.getTotal_return());
                    subscribe = actModel.getSubscribe();
                    mobile_exist = actModel.getMobile_exist();
                    binding_wx = actModel.getBinding_wx();
                    subscription = actModel.getSubscription();
                    if (ApkConstant.DEBUG)
                    {
                        if(actModel.getShow_pai_ticket() == 1) {
                            ll_income_auction_content.setVisibility(View.VISIBLE);
                            //显示数据
                            tv_auction_income.setText(actModel.getPai_ticket());
                            tv_auction_income_detail.setText(actModel.getPai_ticket());
                            tv_auction_income_wait.setText(actModel.getPai_wait_ticket());
                        }
                        if(actModel.getShow_goods_ticket() == 1) {
                            ll_income_sell_content.setVisibility(View.VISIBLE);
                            tv_sell_income.setText(actModel.getGoods_ticket());
                            tv_sell_income_detail.setText(actModel.getGoods_ticket());
                            tv_sell_income_wait.setText(actModel.getGoods_wait_ticket());
                        }
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
                if(resp.getThrowable() != null) {
                    showConfirmDialog(getString(R.string.network_error_not_operate),getString(R.string.leave),"",true);
                }
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop(getString(R.string.my_profit));
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot(getString(R.string.bill));
        mTitle.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestProfit();
        MobclickAgent.onPageStart("收益界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("收益界面");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()) {
            case R.id.ll_income_auction_summary :
                showAuctionIncomeDetail();
                break;
            case R.id.ll_income_sell_summary :
                showSellIncomeDetail();
                break;
            case R.id.tv_auction_manage :
                openIncomeDetail(false);
                break;
            case R.id.tv_sell_detail :
                openIncomeDetail(true);
                break;
            case R.id.tv_do_exchange :
                doExchange();
                break ;
            case R.id.tv_take_reward :
                doBinding();
                break;
            case R.id.tv_get_rule:
                doGetRule();
                break;
            default:
                break;
        }
    }

    /**
     * 查看详细页面
     * @param isSell false为拍卖详情  true为销售详情
     */
    private void openIncomeDetail(boolean isSell) {
        UrlLinkBuilder builder = new UrlLinkBuilder(ApkConstant.SERVER_URL_API);
        builder.add("ctl", "user_center");
        builder.add("act","income");
        if(isSell) {
            builder.add("is_pai","1");
        }
        String url = builder.build();
        Intent intent = new Intent(this, LiveWebViewActivity.class);
        intent.putExtra(LiveWebViewActivity.EXTRA_URL, url);
        startActivity(intent);
    }

    private void showAuctionIncomeDetail() {
        if(ll_income_auction_detail.getVisibility() == View.GONE) {
            iv_auction_arrow.setImageResource(R.drawable.ic_arrow_down_gray);
            SDViewUtil.show(ll_income_auction_detail);
        } else {
            iv_auction_arrow.setImageResource(R.drawable.ic_arrow_right_gray);
            SDViewUtil.hide(ll_income_auction_detail);
        }
    }

    private void showSellIncomeDetail() {
        if(ll_income_sell_detail.getVisibility() == View.GONE) {
            iv_sell_arrow.setImageResource(R.drawable.ic_arrow_down_gray);
            SDViewUtil.show(ll_income_sell_detail);
        } else {
            iv_sell_arrow.setImageResource(R.drawable.ic_arrow_right_gray);
            SDViewUtil.hide(ll_income_sell_detail);
        }
    }


    private void doExchange() {
        Intent intent = new Intent(this,LiveUserProfitExchangeActivity.class);
        startActivity(intent);
    }

    private void doBinding() {
        if(binding_wx != 1) {
            CommonOpenLoginSDK.loginWx(this);
            tv_take_reward.setEnabled(false);
        } else if(mobile_exist != 1) {
            Intent intent = new Intent(this,LiveMobileBindActivity.class);
            startActivity(intent);
        } else if(subscribe != 1) {
            SDOtherUtil.copyText(subscription);
            SDToast.showToast(getString(R.string.already_copy_pub_account)+"："+subscription);
            showConfirmDialog(getString(R.string.weixin_search_follow)+"：“"+subscription+"”"+getString(R.string.pub_account_get_red_packet),getString(R.string.know_it),"",false);
        } else {
            showConfirmDialog(getString(R.string.please_go_to_pub_account),getString(R.string.know_it),"",false);
        }
    }

    private void doGetRule(){
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            App_InitH5Model h5Model = initActModel.getH5_url();
            if (h5Model != null) {
                String url = h5Model.getUrl_red_envelope_rule();
                Intent intent = new Intent(LiveUserProfitActivity.this, LiveWebViewActivity.class);
                intent.putExtra(LiveWebViewActivity.EXTRA_URL, url);
                startActivity(intent);
            }
        }
    }

    private void showConfirmDialog(String content,String confirmText,String cancelText,boolean addListener) {
        if(mDialog == null) {
            mDialog = new SDDialogConfirm(this);
            mDialog.setTextGravity(Gravity.CENTER);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
        }
        if(addListener) {
            mDialog.setmListener(this);
        }else {
            mDialog.setmListener(null);
        }
        mDialog.setTextContent(content);
        mDialog.setTextCancel(cancelText);
        mDialog.setTextConfirm(confirmText);
        mDialog.show();
    }

    public void onEventMainThread(final EJsWxBackInfo event) {
        String json = event.json;
        tv_take_reward.setEnabled(true);
        if (!TextUtils.isEmpty(json)) {
            requestWeiXinBinding(json);
        }
    }

    private void requestWeiXinBinding(String json) {
        String code = JSON.parseObject(json).getString("code");
        CommonInterface.requestBindingWz(code, new AppRequestCallback<App_ProfitBindingActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if(rootModel.isOk()) {
                    subscribe = actModel.getSubscribe();
                    mobile_exist = actModel.getMobile_exist();
                    binding_wx = actModel.getBinding_wx();
                }
            }
        });
    }

    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        tookRecord();
    }

    private void tookRecord() {
        Intent intent = new Intent(this,LiveUserProfitRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickCancel(View v, SDDialogCustom dialog) {

    }

    @Override
    public void onClickConfirm(View v, SDDialogCustom dialog) {
        finish();
    }

    @Override
    public void onDismiss(SDDialogCustom dialog) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog != null) {
            mDialog = null;
        }
    }
}
