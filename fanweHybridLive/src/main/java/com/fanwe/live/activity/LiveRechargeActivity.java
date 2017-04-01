package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.common.CommonOpenSDK;
import com.fanwe.hybrid.constant.Constant;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.listner.PayResultListner;
import com.fanwe.hybrid.model.PaySdkModel;
import com.fanwe.library.adapter.SDAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.dialog.SDDialogProgress;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveRechargePaymentAdapterNew;
import com.fanwe.live.adapter.LiveRechrgePaymentRuleAdapterNew;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_payActModel;
import com.fanwe.live.model.App_rechargeActModel;
import com.fanwe.live.model.PayItemModel;
import com.fanwe.live.model.PayModel;
import com.fanwe.live.model.RuleItemModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.SDProgressPullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.malatv.zhibo.googleutil.GooglePlayPay;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 充值界面
 * Created by Administrator on 2016/7/6.
 */
public class LiveRechargeActivity extends BaseTitleActivity {

    @ViewInject(R.id.ptrsv_content)
    private SDProgressPullToRefreshScrollView ptrsv_content;
    @ViewInject(R.id.tv_user_money)
    private TextView tv_user_money;
    @ViewInject(R.id.ll_payment)
    private SDRecyclerView ll_payment;
    @ViewInject(R.id.ll_payment_rule)
    private SDRecyclerView ll_payment_rule;
    @ViewInject(R.id.et_money)
    private EditText et_money;
    //    @ViewInject(R.id.tv_money_to_diamonds)
//    private TextView tv_money_to_diamonds;
    @ViewInject(R.id.tv_exchange)
    private TextView tv_exchange;
    @ViewInject(R.id.ll_other_ticket_exchange)
    private View ll_other_ticket_exchange;
    @ViewInject(R.id.tv_user_id)
    private TextView tv_user_id;
    @ViewInject(R.id.google_play_recharge)
    private Button google_play_recharge;
    private LiveRechargePaymentAdapterNew adapterPayment;
    private List<PayItemModel> listPayment = new ArrayList<>();

    private LiveRechrgePaymentRuleAdapterNew adapterPaymentRule;
    private List<RuleItemModel> listPaymentRule = new ArrayList<>();
    private final int PAYMENT_HEIGHT = SDViewUtil.dp2px(70);
    private final int PAYMENT_RULE_HEIGHT = SDViewUtil.dp2px(90);
    private int paymentId;
    private int paymentRuleId;
    private int exchangeMoney;
    private PayItemModel mLocalPay;
    private int mLocalPayId = -99;
    /**
     * 兑换比例
     */
    private int exchangeRate = 1;


    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_recharge;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTitle.setMiddleTextTop(getString(R.string.recharge));

        //支付方式
        adapterPayment = new LiveRechargePaymentAdapterNew(this);
        ll_payment.setGridVertical(3);
        adapterPayment.setClickPaymentListener(new SDAdapter.ItemClickListener<PayItemModel>() {
            @Override
            public void onClick(int position, PayItemModel item, View view) {
                if (item.getId() == mLocalPayId) {
                    doExchange();
                } else {
                    adapterPayment.getSelectManager().performClick(item);
                }
            }
        });
        ll_payment.setAdapter(adapterPayment);

        //支付金额
        adapterPaymentRule = new LiveRechrgePaymentRuleAdapterNew(this);
        ll_payment_rule.setGridVertical(3);
        adapterPaymentRule.setClickPaymentRuleListener(new SDAdapter.ItemClickListener<RuleItemModel>() {
            @Override
            public void onClick(int position, RuleItemModel item, View view) {
                paymentRuleId = item.getId();
                clickPaymentRule(item);
            }
        });
        ll_payment_rule.setAdapter(adapterPaymentRule);


        //其他金额
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                exchangeMoney = SDTypeParseUtil.getInt(s.toString());
//                tv_money_to_diamonds.setText(SDFormatUtil.formatMoneyChina(exchangeMoney * exchangeRate));
            }
        });

        //兑换
        tv_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickExchange();
            }
        });
        // 谷歌play充值
        google_play_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickGooglePay();
            }
        });
        initUser();
        initPullToRefresh();
        initLocalPay();
    }

    private void clickGooglePay() {
        PaySdkModel paySdkModel = new PaySdkModel();
        Map<String, Object> config = new HashMap<>();
        config.put("sku", "gas");
        config.put("payLoad", "LiuHuanyu");
        paySdkModel.setConfig(config);

        CommonOpenSDK.payGooglePlay(paySdkModel, LiveRechargeActivity.this);
    }

    private void doExchange() {
        Intent intent = new Intent(this, LiveUserProfitExchangeActivity.class);
        startActivity(intent);
    }

    private void initLocalPay() {
        mLocalPay = new PayItemModel();
        mLocalPay.setId(mLocalPayId);
        mLocalPay.setName(getString(R.string.pay_by_mala));
    }

    private void initUser() {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            SDViewBinder.setTextView(tv_user_id, userModel.getNickName() + "(" + userModel.getUserId() + ")");
        }
    }

    /**
     * 指定金额支付
     *
     * @param model
     */
    private void clickPaymentRule(RuleItemModel model) {
        if (!validatePayment()) {
            return;
        }

        exchangeMoney = 0;
        requestPay();
    }

    /**
     * 输入金额支付
     */
    private void clickExchange() {
        paymentRuleId = 0;
        if (!validatePayment()) {
            return;
        }

        if (exchangeMoney <= 0) {
            SDToast.showToast(getString(R.string.please_input_conversion_money));
            return;
        }

        requestPay();
    }

    private void requestPay() {
        CommonInterface.requestPay(paymentId, paymentRuleId, exchangeMoney, new AppRequestCallback<App_payActModel>() {
            private SDDialogProgress dialog = new SDDialogProgress();

            @Override
            protected void onStart() {
                super.onStart();
                dialog.setTextMsg(getString(R.string.is_start_plug_in));
                dialog.show();
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dialog.dismiss();
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    PayModel payModel = actModel.getPay();
                    if (payModel != null) {
                        PaySdkModel paySdkModel = payModel.getSdk_code();
                        if (paySdkModel != null) {
                            String payCode = paySdkModel.getPay_sdk_type();
                            if (!TextUtils.isEmpty(payCode)) {
                                if (Constant.PaymentType.UPAPP.equalsIgnoreCase(payCode)) {
                                    CommonOpenSDK.payUpApp(paySdkModel, mActivity, payResultListner);
                                } else if (Constant.PaymentType.BAOFOO.equalsIgnoreCase(payCode)) {
                                    CommonOpenSDK.payBaofoo(paySdkModel, mActivity, 1, payResultListner);
                                } else if (Constant.PaymentType.ALIPAY.equalsIgnoreCase(payCode)) {
                                    CommonOpenSDK.payAlipay(paySdkModel, mActivity, payResultListner);
                                } else if (Constant.PaymentType.WXPAY.equals(payCode)) {
                                    CommonOpenSDK.payWxPay(paySdkModel, mActivity, payResultListner);
                                }
                            } else {
                                SDToast.showToast(getString(R.string.paycode_empty));
                            }
                        } else {
                            SDToast.showToast(getString(R.string.sdk_code_empty));
                        }
                    } else {
                        SDToast.showToast(getString(R.string.pay_empty));
                    }
                }
            }
        });
    }

    private PayResultListner payResultListner = new PayResultListner() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    et_money.setText("");
                }
            });
        }

        @Override
        public void onDealing() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onNetWork() {

        }

        @Override
        public void onOther(String msg) {

        }

        @Override
        public void onOther() {

        }
    };

    private boolean validatePayment() {
        PayItemModel payment = adapterPayment.getSelectManager().getSelectedItem();
        if (payment == null) {
            SDToast.showToast(getString(R.string.please_choose_pay_way));
            return false;
        }
        paymentId = payment.getId();

        return true;
    }

    private void initPullToRefresh() {
        ptrsv_content.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ptrsv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    private void judgePaymentHeight(List list, boolean isForRule) {
        if (list != null && list.size() > 0) {
            int size = list.size();
            int lineCount = (size % 3 == 0) ? size / 3 : size / 3 + 1;
            if (isForRule) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, lineCount * PAYMENT_RULE_HEIGHT);
                ll_payment_rule.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, lineCount * PAYMENT_HEIGHT);
                ll_payment.setLayoutParams(params);
            }
        }
    }

    private void insertLocalPayment(List<PayItemModel> list) {
        if (list != null && list.size() > 0) {
            list.add(mLocalPay);
        }
    }

    private void requestData() {
        CommonInterface.requestRecharge(new AppRequestCallback<App_rechargeActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    exchangeRate = actModel.getRate();

                    if (actModel.getShow_other() == 1) {
                        SDViewUtil.show(ll_other_ticket_exchange);
                    } else {
                        SDViewUtil.hide(ll_other_ticket_exchange);
                    }
                    List<PayItemModel> list = actModel.getPay_list();
                    insertLocalPayment(list);
                    SDViewBinder.setTextView(tv_user_money, String.valueOf(actModel.getDiamonds()));
                    adapterPayment.updateData(list);
                    adapterPaymentRule.updateData(actModel.getRule_list());
                    judgePaymentHeight(list, false);
                    judgePaymentHeight(actModel.getRule_list(), true);
                    int defaultPayIndex = -1;
                    List<PayItemModel> listPay = list;
                    if (listPay != null) {
                        int i = 0;
                        for (PayItemModel pay : listPay) {
                            if (paymentId == pay.getId()) {
                                defaultPayIndex = i;
                                break;
                            }
                            i++;
                        }
                        if (defaultPayIndex < 0) {
                            defaultPayIndex = 0;
                            paymentId = 0;
                        }
                    }
                    adapterPayment.getSelectManager().setSelected(defaultPayIndex, true);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                ptrsv_content.onRefreshComplete();
                super.onFinish(resp);
            }
        });
    }

    @Override
    protected void onResume() {
        requestData();
        super.onResume();
        MobclickAgent.onPageStart("充值界面");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("充值界面");
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        GooglePlayPay.getInstance().handleActivityResult(requestCode, resultCode, data, new GooglePlayPay.OnGooglePlayPayAndConsumeDone() {
            @Override
            public void onConsumeDone() {
                // 回调已经回到了主线程，此处进行各种更改UI的操作
                SDToast.showToast("充值成功！");
            }

            @Override
            public void onCancel() {
                SDToast.showToast("您已经取消充值！");
            }
        });
    }
}
