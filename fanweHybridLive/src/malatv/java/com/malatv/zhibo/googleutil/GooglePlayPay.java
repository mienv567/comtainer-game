package com.malatv.zhibo.googleutil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;

import com.android.vending.billing.IInAppBillingService;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.GooglePlayPayModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_google_play_pay_ActModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin.liu on 2017/3/10.
 */

public class GooglePlayPay {

    private static GooglePlayPay instance;


    // (arbitrary) request code for the purchase flow
    // 在onActivityResult中的请求码，也写死为10001
    public static final int RC_REQUEST = 10001;

    // Billing response codes
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final int BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2;
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;

    // IAB Helper error codes
    public static final int IABHELPER_ERROR_BASE = -1000;
    public static final int IABHELPER_REMOTE_EXCEPTION = -1001;
    public static final int IABHELPER_BAD_RESPONSE = -1002;
    public static final int IABHELPER_VERIFICATION_FAILED = -1003;
    public static final int IABHELPER_SEND_INTENT_FAILED = -1004;
    public static final int IABHELPER_USER_CANCELLED = -1005;
    public static final int IABHELPER_UNKNOWN_PURCHASE_RESPONSE = -1006;
    public static final int IABHELPER_MISSING_TOKEN = -1007;
    public static final int IABHELPER_UNKNOWN_ERROR = -1008;
    public static final int IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009;
    public static final int IABHELPER_INVALID_CONSUMPTION = -1010;
    public static final int IABHELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE = -1011;

    // Keys for the responses from InAppBillingService
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";

    // Item types
    public static final String ITEM_TYPE_INAPP = "inapp";

    // some fields on the getSkuDetails response bundle
    public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    public static final String GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";

    // Connection to the service
    IInAppBillingService mService;
    ServiceConnection mServiceConn;

    private Context mContext = App.getApplication();
    // 是否有google play服务
    private boolean doHaveGooglePlay = false;

    private GooglePlayPay() {
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);

                String packageName = mContext.getPackageName();
                try {
                    // 检查这个API3 在客户手机上是否可用
                    int response = mService.isBillingSupported(3, packageName, ITEM_TYPE_INAPP);
                    if (response != BILLING_RESPONSE_RESULT_OK) {
                        return;
                    } else {
                        LogUtil.i("In-app billing version 3 supported for " + packageName);
                    }
                    doHaveGooglePlay = true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        // 检查用户手机中是否安装了谷歌play的服务
        List<ResolveInfo> intentServices = mContext.getPackageManager().queryIntentServices(serviceIntent, 0);
        if (intentServices != null && !intentServices.isEmpty()) {
            // 如果查到了所需要的服务，就执行绑定
            mContext.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        }
    }


    // 最好在APP刚启动时就绑定google play的服务
    public static GooglePlayPay getInstance() {
        if (instance == null) {
            synchronized (GooglePlayPay.class) {
                if (instance == null) {
                    instance = new GooglePlayPay();
                }
            }
        }
        return instance;
    }

    /***
     * 启动谷歌play的支付方法，但是应该注意，由调用改方法的activity在onActivityResult中处理支付结果，并在返回成功的结果后
     * 再重新请求消耗掉刚才购买的商品
     *
     * @param activity
     * @param model
     */
    public void pay(final Activity activity, final GooglePlayPayModel model) {
        if (mService == null) {
            SDToast.showToast(mContext.getResources().getString(R.string.init_google_play_service));
            return;
        }
        if (!doHaveGooglePlay) {
            SDToast.showToast(mContext.getResources().getString(R.string.missing_google_play_service));
            return;
        }

        Inventory inv = querySkuDetail("gas");
        Purchase gas = inv.getPurchase("gas");

        // Purchasing a new item or subscription re-signup
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), model.getSku(), model.getType(),
                    model.getPayLoad());
            int responseCode = getResponseCodeFromBundle(buyIntentBundle);
            if (responseCode == BILLING_RESPONSE_RESULT_OK) {
                PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
                activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
                        RC_REQUEST, new Intent(),
                        Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0));
            } else if (responseCode == BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                queryInventoryAsy(new QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                        GooglePlayPay.this.onQueryInventoryFinished(result, inventory, model, activity);
                    }
                });
            }
        } catch (RemoteException | IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private Inventory querySkuDetail(String sku) {
        Inventory inv=new Inventory();
        ArrayList<String> skuPartList = new ArrayList<>();
        skuPartList.add(sku);
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, skuPartList);
        try {
            Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(),
                    ITEM_TYPE_INAPP, querySkus);
            ArrayList<String> responseList = skuDetails.getStringArrayList(
                    RESPONSE_GET_SKU_DETAILS_LIST);

            for (String thisResponse : responseList) {
                SkuDetails d = null;
                try {
                    d = new SkuDetails(ITEM_TYPE_INAPP, thisResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                inv.addSkuDetails(d);
            }
            return inv;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return inv;
    }

    // 查到已经有的商品列表之后，需要将gas再消耗掉，然后重新调用支付方法
    private void onQueryInventoryFinished(IabResult result, Inventory inventory, final GooglePlayPayModel model, final Activity activity) {
        if (result.isFailure()) {
            SDToast.showToast("Failed to query inventory: " + result);
            return;
        }
        Purchase gasPurchase = inventory.getPurchase(model.getSku());
        consumeAsyn(gasPurchase, new OnGooglePlayPayAndConsumeDone() {
            @Override
            public void onConsumeDone() {
                pay(activity, model);
            }
        });
    }

    // 查询已有的物品列表
    private void queryInventoryAsy(final QueryInventoryFinishedListener listener) {
        final Handler handler = new Handler();
        (new Thread(new Runnable() {
            public void run() {
                IabResult result = new IabResult(BILLING_RESPONSE_RESULT_OK, "Inventory refresh successful.");
                Inventory inv = new Inventory();
                try {
                    queryPurchases(inv, ITEM_TYPE_INAPP);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final IabResult result_f = result;
                final Inventory inv_f = inv;
                if (listener != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            listener.onQueryInventoryFinished(result_f, inv_f);
                        }
                    });
                }
            }
        })).start();
    }

    private int queryPurchases(Inventory inv, String itemType) throws JSONException, RemoteException {
        // Query purchases
        LogUtil.d("Querying owned items, item type: " + itemType);
        LogUtil.d("Package name: " + mContext.getPackageName());
        boolean verificationFailed = false;
        String continueToken = null;

        do {
            LogUtil.d("Calling getPurchases with continuation token: " + continueToken);
            Bundle ownedItems = mService.getPurchases(3, mContext.getPackageName(),
                    itemType, continueToken);

            int response = getResponseCodeFromBundle(ownedItems);
            LogUtil.d("Owned items response: " + String.valueOf(response));
            if (response != BILLING_RESPONSE_RESULT_OK) {
                LogUtil.d("getPurchases() failed: " + getResponseDesc(response));
                return response;
            }
            if (!ownedItems.containsKey(RESPONSE_INAPP_ITEM_LIST)
                    || !ownedItems.containsKey(RESPONSE_INAPP_PURCHASE_DATA_LIST)
                    || !ownedItems.containsKey(RESPONSE_INAPP_SIGNATURE_LIST)) {
                LogUtil.e("Bundle returned from getPurchases() doesn't contain required fields.");
                return IABHELPER_BAD_RESPONSE;
            }

            ArrayList<String> ownedSkus = ownedItems.getStringArrayList(
                    RESPONSE_INAPP_ITEM_LIST);
            ArrayList<String> purchaseDataList = ownedItems.getStringArrayList(
                    RESPONSE_INAPP_PURCHASE_DATA_LIST);
            ArrayList<String> signatureList = ownedItems.getStringArrayList(
                    RESPONSE_INAPP_SIGNATURE_LIST);

            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku = ownedSkus.get(i);
                Purchase purchase = new Purchase(itemType, purchaseData, signature);
                if (TextUtils.isEmpty(purchase.getToken())) {
                    LogUtil.e("BUG: empty/null token!");
                    LogUtil.d("Purchase data: " + purchaseData);
                }
                // Record ownership and token
                inv.addPurchase(purchase);
            }
            continueToken = ownedItems.getString(INAPP_CONTINUATION_TOKEN);
            LogUtil.d("Continuation token: " + continueToken);
        } while (!TextUtils.isEmpty(continueToken));

        return verificationFailed ? IABHELPER_VERIFICATION_FAILED : BILLING_RESPONSE_RESULT_OK;
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data, final OnGooglePlayPayAndConsumeDone listener) {
        if (requestCode != RC_REQUEST) {
            return;
        }
        if (data == null) {
            SDToast.showToast("购买失败，未返回任何数据！");
            return;
        }
        int responseCode = getResponseCodeFromIntent(data);
        final String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
        final String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);

        if (resultCode == Activity.RESULT_OK && responseCode == BILLING_RESPONSE_RESULT_OK) {
            CommonInterface.requestGooglePlayPay(purchaseData, dataSignature, new AppRequestCallback<App_google_play_pay_ActModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    onServerVerified(purchaseData, dataSignature, listener);
                }

                @Override
                protected void onFinish(SDResponse resp) {
                    // 因为服务器的谷歌支付还没好，所以先随便在onfinish上加上的这个执行方法吧
                    onServerVerified(purchaseData, dataSignature, listener);
                }
            });
        } else {
            listener.onCancel();
        }
    }

    private void onServerVerified(String purchaseData, String dataSignature, OnGooglePlayPayAndConsumeDone listener) {
        Purchase purchase = null;
        try {
            purchase = new Purchase(ITEM_TYPE_INAPP, purchaseData, dataSignature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        consumeAsyn(purchase, listener);
    }

    // 将原来耗时的consume方法用一个thread包一下
    private void consumeAsyn(final Purchase purchase, final OnGooglePlayPayAndConsumeDone listener) {
        new Thread() {
            public void run() {
                try {
                    consume(purchase, listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    // 消耗商品的方法，这个可能有耗时操作
    private void consume(Purchase itemInfo, final OnGooglePlayPayAndConsumeDone listener) throws IabException {
        if (!itemInfo.mItemType.equals(ITEM_TYPE_INAPP)) {
            throw new IabException(IABHELPER_INVALID_CONSUMPTION,
                    "Items of type '" + itemInfo.mItemType + "' can't be consumed.");
        }
        try {
            String token = itemInfo.getToken();
            String sku = itemInfo.getSku();
            if (token == null || token.equals("")) {
                LogUtil.e("Can't consume " + sku + ". No token.");
                throw new IabException(IABHELPER_MISSING_TOKEN, "PurchaseInfo is missing token for sku: "
                        + sku + " " + itemInfo);
            }

            LogUtil.i("Consuming sku: " + sku + ", token: " + token);
            int response = mService.consumePurchase(3, mContext.getPackageName(), token);
            if (response == BILLING_RESPONSE_RESULT_OK) {
                LogUtil.d("Successfully consumed sku: " + sku);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onConsumeDone();
                    }
                });
            } else {
                LogUtil.e("Error consuming consuming sku " + sku + ". " + getResponseDesc(response));
                throw new IabException(response, "Error consuming sku " + sku);
            }
        } catch (RemoteException e) {
            throw new IabException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while consuming. PurchaseInfo: " + itemInfo, e);
        }
    }


    // Workaround to bug where sometimes response codes come as Long instead of Integer
    private int getResponseCodeFromIntent(Intent i) {
        Object o = i.getExtras().get(RESPONSE_CODE);
        if (o == null) {
            LogUtil.e("Intent with no response code, assuming OK (known issue)");
            return BILLING_RESPONSE_RESULT_OK;
        } else if (o instanceof Integer) return ((Integer) o).intValue();
        else if (o instanceof Long) return (int) ((Long) o).longValue();
        else {
            LogUtil.e("Unexpected type for intent response code.");
            LogUtil.e(o.getClass().getName());
            throw new RuntimeException("Unexpected type for intent response code: " + o.getClass().getName());
        }
    }

    private int getResponseCodeFromBundle(Bundle b) {
        Object o = b.get(RESPONSE_CODE);
        if (o == null) {
            LogUtil.i("Bundle with null response code, assuming OK (known issue)");
            return BILLING_RESPONSE_RESULT_OK;
        } else if (o instanceof Integer) return ((Integer) o).intValue();
        else if (o instanceof Long) return (int) ((Long) o).longValue();
        else {
            LogUtil.e("Unexpected type for bundle response code.");
            LogUtil.e(o.getClass().getName());
            throw new RuntimeException("Unexpected type for bundle response code: " + o.getClass().getName());
        }
    }


    // 消耗商品成功的回调
    public static abstract class OnGooglePlayPayAndConsumeDone {
        public abstract void onConsumeDone();

        public void onCancel() {
            // 用户取消操作的时候要执行的操作
        }
    }

    /**
     * Listener that notifies when an inventory query operation completes.
     */
    public interface QueryInventoryFinishedListener {
        /**
         * Called to notify that an inventory query operation completed.
         *
         * @param result The result of the operation.
         * @param inv    The inventory.
         */
        void onQueryInventoryFinished(IabResult result, Inventory inv);
    }


    /**
     * Returns a human-readable description for the given response code.
     *
     * @param code The response code
     * @return A human-readable string explaining the result code.
     * It also includes the result code numerically.
     */
    public static String getResponseDesc(int code) {
        String[] iab_msgs = ("0:OK/1:User Canceled/2:Unknown/" +
                "3:Billing Unavailable/4:Item unavailable/" +
                "5:Developer Error/6:Error/7:Item Already Owned/" +
                "8:Item not owned").split("/");
        String[] iabhelper_msgs = ("0:OK/-1001:Remote exception during initialization/" +
                "-1002:Bad response received/" +
                "-1003:Purchase signature verification failed/" +
                "-1004:Send intent failed/" +
                "-1005:User cancelled/" +
                "-1006:Unknown purchase response/" +
                "-1007:Missing token/" +
                "-1008:Unknown error/" +
                "-1009:Subscriptions not available/" +
                "-1010:Invalid consumption attempt").split("/");

        if (code <= IABHELPER_ERROR_BASE) {
            int index = IABHELPER_ERROR_BASE - code;
            if (index >= 0 && index < iabhelper_msgs.length) return iabhelper_msgs[index];
            else return String.valueOf(code) + ":Unknown IAB Helper Error";
        } else if (code < 0 || code >= iab_msgs.length)
            return String.valueOf(code) + ":Unknown";
        else
            return iab_msgs[code];
    }

}
