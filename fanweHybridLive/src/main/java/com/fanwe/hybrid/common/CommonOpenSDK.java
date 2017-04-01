package com.fanwe.hybrid.common;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.baofoo.sdk.vip.BaofooPayActivity;
import com.fanwe.hybrid.listner.PayResultListner;
import com.fanwe.hybrid.model.BfappModel;
import com.fanwe.hybrid.model.BfupwapModel;
import com.fanwe.hybrid.model.GooglePlayPayModel;
import com.fanwe.hybrid.model.MalipayModel;
import com.fanwe.hybrid.model.PaySdkModel;
import com.fanwe.hybrid.model.WxappModel;
import com.fanwe.hybrid.wxapp.SDWxappPay;
import com.fanwe.library.pay.alipay.PayResult;
import com.fanwe.library.pay.alipay.SDAlipayer;
import com.fanwe.library.pay.alipay.SDAlipayer.SDAlipayerListener;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.malatv.zhibo.googleutil.GooglePlayPay;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;


/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-1-25 下午4:43:54 类说明
 */
public class CommonOpenSDK {
    /**
     * 宝付SDK
     */
    public static void payBaofoo(PaySdkModel actModel, Activity context, int requestCode, final PayResultListner listner) {
        if (actModel == null) {
            return;
        }

        BfappModel model = actModel.getBfappModel();
        if (model == null) {
            SDToast.showToast(context.getString(R.string.get_pay_params_fail));
            listner.onOther();
            return;
        }

        String respCode = model.getRetCode();

        if (!"0000".equals(respCode)) {
            SDToast.showToast(respCode + model.getRetMsg());
            listner.onOther();
            return;
        }

        String tradeNo = model.getTradeNo();
        if (TextUtils.isEmpty(tradeNo)) {
            SDToast.showToast(context.getString(R.string.tradeNo_is_empty));
            listner.onOther();
            return;
        }

        boolean isDebug = actModel.getBfappModel().getIs_debug() == 1 ? false : true;
        Intent payintent = new Intent(context, BaofooPayActivity.class);
        payintent.putExtra(BaofooPayActivity.PAY_TOKEN, tradeNo);
        payintent.putExtra(BaofooPayActivity.PAY_BUSINESS, isDebug);
        context.startActivityForResult(payintent, requestCode);
    }

    /**
     * 银联SDK
     */
    public static void payUpApp(PaySdkModel actModel, Activity context, final PayResultListner listner) {
        if (actModel == null) {
            return;
        }

        BfupwapModel model = actModel.getBfupwapModel();
        if (model == null) {
            SDToast.showToast(context.getString(R.string.get_pay_params_fail));
            listner.onOther();
            return;
        }

        String tradeNo = model.getTn();
        if (TextUtils.isEmpty(tradeNo)) {
            SDToast.showToast(context.getString(R.string.tn_is_empty));
            listner.onOther();
            return;
        }
        UPPayAssistEx.startPayByJAR(context, PayActivity.class, null, null, tradeNo, "01");
    }

    /**
     * 支付宝sdk支付(新)
     */
    public static void payAlipay(PaySdkModel model, final Activity activity, final PayResultListner listner) {
        if (model == null) {
            return;
        }
        MalipayModel mainpayModel = model.getMalipay();
        if (mainpayModel == null) {
            SDToast.showToast(activity.getString(R.string.get_pay_params_fail));
            listner.onOther();
            return;
        }

        String orderSpec = mainpayModel.getOrder_spec();
        String sign = mainpayModel.getSign();
        String signType = mainpayModel.getSign_type();

        if (TextUtils.isEmpty(orderSpec)) {
            SDToast.showToast(activity.getString(R.string.order_spec_is_empty));
            listner.onOther();
            return;
        }

        if (TextUtils.isEmpty(sign)) {
            SDToast.showToast(activity.getString(R.string.sign_is_empty));
            listner.onOther();
            return;
        }

        if (TextUtils.isEmpty(signType)) {
            SDToast.showToast(activity.getString(R.string.signType_is_empty));
            listner.onOther();
            return;
        }

        SDAlipayer payer = new SDAlipayer(activity);
        payer.setListener(new SDAlipayerListener() {

            @Override
            public void onResult(PayResult result) {
                String info = result.getMemo();
                String status = result.getResultStatus();

                if ("9000".equals(status)) // 支付成功
                {
                    SDToast.showToast(activity.getString(R.string.pay_success));
                    listner.onSuccess();
                } else if ("8000".equals(status)) // 支付结果确认中
                {
                    SDToast.showToast(activity.getString(R.string.pay_result_confirm));
                    listner.onDealing();
                } else if ("4000".equals(status)) {
                    if (!TextUtils.isEmpty(info)) {
                        SDToast.showToast(info);
                    }
                    listner.onFail();
                } else if ("6001".equals(status)) {
                    if (!TextUtils.isEmpty(info)) {
                        SDToast.showToast(info);
                    }
                    listner.onCancel();
                } else if ("6002".equals(status)) {
                    if (!TextUtils.isEmpty(info)) {
                        SDToast.showToast(info);
                    }
                    listner.onNetWork();
                } else {
                    if (!TextUtils.isEmpty(info)) {
                        SDToast.showToast(info);
                    }
                    listner.onOther();
                }
            }

            @Override
            public void onFailure(Exception e, String msg) {
                if (e != null) {
                    listner.onOther();
                    SDToast.showToast(activity.getString(R.string.fail) + e.toString());
                } else {
                    if (!TextUtils.isEmpty(msg)) {
                        listner.onOther();
                        SDToast.showToast(msg);
                    }
                }
            }
        });
        payer.pay(orderSpec, sign, signType);
    }

    /**
     * 微信SDK支付
     */
    public static void payWxPay(PaySdkModel paySdkModel, Activity activity, final PayResultListner listner) {
        if (paySdkModel == null) {
            return;
        }

        WxappModel model = paySdkModel.getWxapp();
        if (model == null) {
            SDToast.showToast(activity.getString(R.string.get_pay_params_fail));
            listner.onOther();
            return;
        }

        String appId = model.getAppid();
        if (TextUtils.isEmpty(appId)) {
            SDToast.showToast(activity.getString(R.string.appId_is_empty));
            listner.onOther();
            return;
        }

        String partnerId = model.getPartnerid();
        if (TextUtils.isEmpty(partnerId)) {
            SDToast.showToast(activity.getString(R.string.partnerId_is_empty));
            listner.onOther();
            return;
        }

        String prepayId = model.getPrepayid();
        if (TextUtils.isEmpty(prepayId)) {
            SDToast.showToast(activity.getString(R.string.prepayId_is_empty));
            listner.onOther();
            return;
        }

        String nonceStr = model.getNoncestr();
        if (TextUtils.isEmpty(nonceStr)) {
            SDToast.showToast(activity.getString(R.string.nonceStr_is_empty));
            listner.onOther();
            return;
        }

        String timeStamp = model.getTimestamp();
        if (TextUtils.isEmpty(timeStamp)) {
            SDToast.showToast(activity.getString(R.string.timeStamp_is_empty));
            listner.onOther();
            return;
        }

        String packageValue = model.getPackagevalue();
        if (TextUtils.isEmpty(packageValue)) {
            SDToast.showToast(activity.getString(R.string.packageValue_is_empty));
            listner.onOther();
            return;
        }

        String sign = model.getSign();
        if (TextUtils.isEmpty(sign)) {
            SDToast.showToast(activity.getString(R.string.sign_is_empty));
            listner.onOther();
            return;
        }

        SDWxappPay.getInstance().setAppId(appId);

        PayReq req = new PayReq();
        req.appId = appId;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        //req.packageValue = "Sign=WXPay";
        req.packageValue = "prepay_id=" + prepayId;
        req.sign = sign;

        SDWxappPay.getInstance().pay(req);
    }

    /**
     * Google Play 支付
     */
    public static void payGooglePlay(PaySdkModel paySdkModel, Activity activity) {
        if (paySdkModel == null) {
            return;
        }

        GooglePlayPayModel model = paySdkModel.getGoogleModel();
        if (model == null) {
            SDToast.showToast(activity.getString(R.string.get_pay_params_fail));
            return;
        }

        if (TextUtils.isEmpty(model.getPayLoad())) {
            SDToast.showToast(activity.getString(R.string.get_pay_params_fail));
            return;
        }

        if (TextUtils.isEmpty(model.getSku())) {
            SDToast.showToast(activity.getString(R.string.get_pay_amount_fail));
            return;
        }
        GooglePlayPay.getInstance().pay(activity, model);
    }



}
