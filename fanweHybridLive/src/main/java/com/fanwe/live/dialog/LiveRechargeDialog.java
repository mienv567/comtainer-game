package com.fanwe.live.dialog;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.common.CommonOpenSDK;
import com.fanwe.hybrid.constant.Constant.PaymentType;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.listner.PayResultListner;
import com.fanwe.hybrid.model.PaySdkModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.customview.SDGridLinearLayout.ItemClickListener;
import com.fanwe.library.dialog.SDDialogProgress;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveRechargePayAdapter;
import com.fanwe.live.adapter.LiveRechargeRuleAdapter;
import com.fanwe.live.adapter.LiveRechargeRuleAdapter.OnListenerOtherConvertView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EWxPayResultCodeComplete;
import com.fanwe.live.model.App_payActModel;
import com.fanwe.live.model.App_rechargeActModel;
import com.fanwe.live.model.PayItemModel;
import com.fanwe.live.model.RuleItemModel;
import com.fanwe.live.wxapi.WXPayEntryActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-7 上午9:33:34 类说明
 */
public class LiveRechargeDialog extends LiveBaseDialog
{
    @ViewInject(R.id.ll_close)
    private LinearLayout ll_close;

    @ViewInject(R.id.ll_money)
    private LinearLayout ll_money;

    @ViewInject(R.id.gll_money)
    private SDGridLinearLayout gll_money;

    @ViewInject(R.id.tv_account)
    private TextView tv_account;

    @ViewInject(R.id.gll_pay)
    private SDGridLinearLayout gll_pay;

    @ViewInject(R.id.ll_rate)
    private LinearLayout ll_rate;

    @ViewInject(R.id.et_money)
    private EditText et_money;

    @ViewInject(R.id.tv_num)
    private TextView tv_num;

    private List<RuleItemModel> listModelRule = new ArrayList<RuleItemModel>();
    private LiveRechargeRuleAdapter ruleAdapter;
    private List<PayItemModel> listModelPay = new ArrayList<PayItemModel>();
    private LiveRechargePayAdapter payAdapter;
    private App_rechargeActModel app_rechargeActModel;

    public LiveRechargeDialog(Activity activity)
    {
        super(activity);
        init();
    }

    private void init()
    {
        setContentView(R.layout.dialog_live_recharge);
        setCanceledOnTouchOutside(true);
        paddingLeft(60);
        paddingRight(60);
        x.view().inject(this, getContentView());
        register();
        bindAdapter();
        requestRecharge(false);
    }

    private void register()
    {
        ll_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ll_money.getVisibility() == View.VISIBLE)
                {
                    dismiss();
                } else
                {
                    SDViewUtil.hide(ll_rate);
                    SDViewUtil.show(ll_money);
                }
            }
        });

        gll_pay.setItemClickListener(new ItemClickListener()
        {

            @Override
            public void onItemClick(int position, View view, ViewGroup parent)
            {
                PayItemModel payModel = listModelPay.get(position);
                if (payModel != null)
                {
                    if (ll_money.getVisibility() == View.VISIBLE)
                    {
                        RuleItemModel ruleModel = ruleAdapter.getCurrentSelectedModel();
                        if (ruleModel != null)
                        {
                            requestPayRuleId(ruleModel.getId(), payModel.getId());
                        }
                    } else if (ll_rate.getVisibility() == View.VISIBLE)
                    {
                        String str_money = et_money.getText().toString();
                        double dou_money = SDTypeParseUtil.getDouble(str_money);
                        if (dou_money <= 0)
                        {
                            return;
                        } else
                        {
                            requestPayMoney(dou_money, payModel.getId());
                        }

                    }
                }
            }
        });

        et_money.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String str_money = s.toString().trim();
                if (!TextUtils.isEmpty(str_money))
                {
                    int int_money = SDTypeParseUtil.getInt(str_money);
                    if (app_rechargeActModel != null && app_rechargeActModel.getRate() > 0)
                    {
                        int rate = app_rechargeActModel.getRate();
                        int num = int_money * rate;
                        SDViewBinder.setTextView(tv_num, num + "");
                    }
                } else
                {
                    SDViewBinder.setTextView(tv_num, "0");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub

            }
        });
    }

    private void bindAdapter()
    {
        gll_money.setColNumber(2);
        gll_pay.setColNumber(2);
        ruleAdapter = new LiveRechargeRuleAdapter(listModelRule, getOwnerActivity());
        ruleAdapter.setOnListenerOtherConvertView(mOnListenerOtherConvertView);
        gll_money.setAdapter(ruleAdapter);
        payAdapter = new LiveRechargePayAdapter(listModelPay, getOwnerActivity());
        gll_pay.setAdapter(payAdapter);
    }

    private OnListenerOtherConvertView mOnListenerOtherConvertView = new OnListenerOtherConvertView()
    {
        @Override
        public void onListenerOtherConvertView(int position, View convertView, ViewGroup parent, RuleItemModel model)
        {
            SDViewUtil.hide(ll_money);
            SDViewUtil.show(ll_rate);
        }
    };

    private void requestRecharge(final boolean isOnlyRefreshAccout)
    {
        CommonInterface.requestRecharge(new AppRequestCallback<App_rechargeActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    app_rechargeActModel = actModel;
                    if (isOnlyRefreshAccout)
                    {
                        SDViewBinder.setTextView(tv_account, app_rechargeActModel.getDiamonds() + "");
                    } else
                    {
                        SDViewBinder.setTextView(tv_account, app_rechargeActModel.getDiamonds() + "");

                        List<PayItemModel> paylist = actModel.getPay_list();
                        if (paylist != null && paylist.size() > 0)
                        {
                            int size = paylist.size();
                            if (size == 1)
                            {
                                gll_pay.setColNumber(1);
                            }
                            SDViewUtil.updateAdapterByList(listModelPay, paylist, payAdapter, false);
                        }
                        List<RuleItemModel> rulelist = actModel.getRule_list();
                        if (rulelist != null && rulelist.size() > 0)
                        {
                            // 默认选择第一个
                            RuleItemModel model = rulelist.get(0);
                            model.setSelected(true);

                            RuleItemModel model_last = new RuleItemModel();
                            rulelist.add(model_last);

                            SDViewUtil.updateAdapterByList(listModelRule, rulelist, ruleAdapter, false);
                        }
                    }

                }
            }
        });
    }

    private void requestPayMoney(double money, int pay_id)
    {
        requestPay(money, 0, pay_id);
    }

    private void requestPayRuleId(int rule_id, int pay_id)
    {
        requestPay(0, rule_id, pay_id);
    }

    private void requestPay(double money, int rule_id, int pay_id)
    {
        CommonInterface.requestPay(pay_id, rule_id, money, new AppRequestCallback<App_payActModel>()
        {
            private SDDialogProgress dialog = new SDDialogProgress();

            @Override
            protected void onStart()
            {
                super.onStart();
                dialog.setTextMsg(SDResourcesUtil.getString(R.string.is_start_plug_in));
                dialog.show();
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dialog.dismiss();
            }

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    if (actModel.getPay() != null)
                    {
                        if (actModel.getPay().getSdk_code() != null)
                        {
                            PaySdkModel paySdkModel = actModel.getPay().getSdk_code();
                            openSDKPAY(paySdkModel);
                        } else
                        {
                            SDToast.showToast(SDResourcesUtil.getString(R.string.sdk_code_empty));
                        }
                    } else
                    {
                        SDToast.showToast(SDResourcesUtil.getString(R.string.pay_empty));
                    }
                }
            }

            private void openSDKPAY(PaySdkModel model)
            {
                String payCode = model.getPay_sdk_type();
                if (!TextUtils.isEmpty(payCode))
                {
                    if (PaymentType.UPAPP.equalsIgnoreCase(payCode))
                    {
                        CommonOpenSDK.payUpApp(model, getOwnerActivity(), payResultListner);
                    } else if (PaymentType.BAOFOO.equalsIgnoreCase(payCode))
                    {
                        CommonOpenSDK.payBaofoo(model, getOwnerActivity(), 1, payResultListner);
                    } else if (PaymentType.ALIPAY.equalsIgnoreCase(payCode))
                    {
                        CommonOpenSDK.payAlipay(model, getOwnerActivity(), payResultListner);
                    } else if (PaymentType.WXPAY.equals(payCode))
                    {
                        CommonOpenSDK.payWxPay(model, getOwnerActivity(), payResultListner);
                    }
                } else
                {
                    SDToast.showToast(SDResourcesUtil.getString(R.string.paycode_empty));
                }

            }
        });
    }

    private PayResultListner payResultListner = new PayResultListner()
    {
        @Override
        public void onSuccess()
        {
            SDToast.showToast(SDResourcesUtil.getString(R.string.pay_success));
            requestRecharge(true);
            CommonInterface.requestMyUserInfoJava(null);
            dismiss();
        }

        @Override
        public void onOther()
        {

        }

        @Override
        public void onNetWork()
        {

        }

        @Override
        public void onOther(String msg) {

        }

        @Override
        public void onFail()
        {

        }

        @Override
        public void onDealing()
        {

        }

        @Override
        public void onCancel()
        {

        }
    };

    public void onEventMainThread(EWxPayResultCodeComplete event)
    {
        if (event.WxPayResultCode == WXPayEntryActivity.RespErrCode.CODE_SUCCESS)
        {
            requestRecharge(true);
            CommonInterface.requestMyUserInfoJava(null);
            dismiss();
        } else if (event.WxPayResultCode == WXPayEntryActivity.RespErrCode.CODE_FAIL)
        {

        } else if (event.WxPayResultCode == WXPayEntryActivity.RespErrCode.CODE_CANCEL)
        {

        }

    }
}
