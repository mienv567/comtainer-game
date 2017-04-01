package com.fanwe.auction.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.auction.dialog.room.AuctionSucPaySingleton;
import com.fanwe.auction.model.PaiBuyerModel;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionFail;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 竞拍结果对话框
 * Created by Administrator on 2016/8/16.
 */
public class AuctionResultsDialog extends SDDialogBase
{

    @ViewInject(R.id.img_act_result)
    private ImageView img_act_result;//竞拍成功或失败图片
    @ViewInject(R.id.ll_name)
    private LinearLayout ll_name;
    @ViewInject(R.id.ll_pay)
    private LinearLayout ll_pay;
    @ViewInject(R.id.ll_act_fail)
    private LinearLayout ll_act_fail;
    @ViewInject(R.id.txv_name)
    private TextView txv_name;//竞拍成功观众姓名
    @ViewInject(R.id.txv_price)
    private TextView txv_price;//竞拍最后成交金额
    @ViewInject(R.id.txv_pay)
    private TextView txv_pay;//竞拍支付
    @ViewInject(R.id.txv_act_fail)
    private TextView txv_act_fail;//竞拍失败提示语

    /**
     * 竞拍类型
     */
    private int type;

    /**
     * 竞拍成功
     */
    private CustomMsgAuctionSuccess customMsgAuctionSuccess;

    /**
     * 竞拍通知付款，比如第一名超时未付款，通知下一名付款
     */
    private CustomMsgAuctionNotifyPay customMsgAuctionNotifyPay;

    /**
     * 流拍
     */
    private CustomMsgAuctionFail customMsgAuctionFail;

    /**
     * 支付成功
     */
    private CustomMsgAuctionPaySuccess customMsgAuctionPaySuccess;

    public AuctionResultsDialog(Activity activity)
    {
        super(activity);
        init();
    }

    /**
     * 竞拍成功
     *
     * @param activity
     * @param customMsgAuctionSuccess
     */
    public AuctionResultsDialog(Activity activity, CustomMsgAuctionSuccess customMsgAuctionSuccess)
    {
        super(activity);
        this.customMsgAuctionSuccess = customMsgAuctionSuccess;
        this.type = customMsgAuctionSuccess.getType();
        init();
    }

    /**
     * 竞拍通知付款，比如第一名超时未付款，通知下一名付款
     *
     * @param activity
     * @param customMsgAuctionNotifyPay
     */
    public AuctionResultsDialog(Activity activity, CustomMsgAuctionNotifyPay customMsgAuctionNotifyPay)
    {
        super(activity);
        this.customMsgAuctionNotifyPay = customMsgAuctionNotifyPay;
        this.type = customMsgAuctionNotifyPay.getType();
        init();
    }

    /**
     * 流拍
     *
     * @param activity
     * @param customMsgAuctionFail
     */
    public AuctionResultsDialog(Activity activity, CustomMsgAuctionFail customMsgAuctionFail)
    {
        super(activity);
        this.customMsgAuctionFail = customMsgAuctionFail;
        this.type = customMsgAuctionFail.getType();
        init();
    }

    /**
     * 支付成功
     *
     * @param activity
     * @param customMsgAuctionPaySuccess
     */
    public AuctionResultsDialog(Activity activity, CustomMsgAuctionPaySuccess customMsgAuctionPaySuccess)
    {
        super(activity);
        this.customMsgAuctionPaySuccess = customMsgAuctionPaySuccess;
        this.type = customMsgAuctionPaySuccess.getType();
        init();
    }

    private void init()
    {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_auction_results);
        paddings(0);
        x.view().inject(this, getContentView());

        UserModel dao = UserModelDao.query();

        switch (type)
        {
            case LiveConstant.CustomMsgType.MSG_AUCTION_SUCCESS:
                List<PaiBuyerModel> buyer = customMsgAuctionSuccess.getBuyer();
                if (buyer != null && buyer.size() > 0)
                {
                    //竞拍成功取第一条数据如果是自己则显示付款其他显示谁中标了
                    final PaiBuyerModel item = buyer.get(0);
                    if (dao.getUserId().equals(item.getUser_id()))
                    {
                        img_act_result.setImageResource(R.drawable.ic_auction_suc);
                        ll_name.setVisibility(View.GONE);
                        ll_pay.setVisibility(View.VISIBLE);
                        ll_act_fail.setVisibility(View.GONE);
                        txv_pay.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                //付款
                                showPayDialog(item);
                            }
                        });
                        break;
                    } else
                    {
                        img_act_result.setImageResource(R.drawable.ic_auction_suc);
                        ll_name.setVisibility(View.VISIBLE);
                        ll_pay.setVisibility(View.GONE);
                        ll_act_fail.setVisibility(View.GONE);
                        txv_name.setText(customMsgAuctionSuccess.getBuyer().get(0).getNick_name());
                        txv_price.setText(customMsgAuctionSuccess.getBuyer().get(0).getPai_diamonds());
                    }
                }
                break;
            case LiveConstant.CustomMsgType.MSG_AUCTION_PAY_SUCCESS://支付成功
                img_act_result.setImageResource(R.drawable.ic_pay_suc);
                ll_name.setVisibility(View.VISIBLE);
                ll_pay.setVisibility(View.GONE);
                ll_act_fail.setVisibility(View.GONE);
                if (customMsgAuctionPaySuccess.getBuyer() != null && customMsgAuctionPaySuccess.getBuyer().size() > 0)
                {
                    for (int i = 0; i < customMsgAuctionPaySuccess.getBuyer().size(); i++)
                    {
                        if (customMsgAuctionPaySuccess.getBuyer().get(i).getType() == 4)//4 付款完成
                        {
                            txv_name.setText(customMsgAuctionPaySuccess.getBuyer().get(i).getNick_name());
                            txv_price.setText(customMsgAuctionPaySuccess.getBuyer().get(i).getPai_diamonds());
                        }
                    }
                }
                break;
            case LiveConstant.CustomMsgType.MSG_AUCTION_FAIL://流拍
                img_act_result.setImageResource(R.drawable.ic_auction_fail);
                ll_name.setVisibility(View.GONE);
                ll_pay.setVisibility(View.GONE);
                ll_act_fail.setVisibility(View.VISIBLE);
                if (customMsgAuctionFail.getOut_type() == 0)//竞拍类型 0无人出价 1无人付款
                    txv_act_fail.setText("无人出价");
                else if (customMsgAuctionFail.getOut_type() == 1)
                    txv_act_fail.setText("前三名领先者超时未付款");
                break;
            case LiveConstant.CustomMsgType.MSG_AUCTION_NOTIFY_PAY://竞拍通知付款，比如第一名超时未付款，通知下一名付款
                for (int i = 0; i < customMsgAuctionNotifyPay.getBuyer().size(); i++)
                {
                    Log.i("mynet","userId = "+customMsgAuctionNotifyPay.getBuyer().get(i).getUser_id()+ "::::: Type = "+customMsgAuctionNotifyPay.getBuyer().get(i).getType());
                    if (dao.getUserId().equals(customMsgAuctionNotifyPay.getBuyer().get(i).getUser_id()) &&
                            customMsgAuctionNotifyPay.getBuyer().get(i).getType() == 1)
                    {//0 出局 1待付款 2排队中 3超时出局 4 付款完成
                        final PaiBuyerModel buyerModel = customMsgAuctionNotifyPay.getBuyer().get(i);
                        img_act_result.setImageResource(R.drawable.ic_auction_suc);
                        ll_name.setVisibility(View.GONE);
                        ll_pay.setVisibility(View.VISIBLE);
                        ll_act_fail.setVisibility(View.GONE);
                        txv_pay.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                showPayDialog(buyerModel);
                            }
                        });
                        break;
                    } else if (dao.getUserId().equals(customMsgAuctionNotifyPay.getBuyer().get(i).getUser_id()) &&
                            customMsgAuctionNotifyPay.getBuyer().get(i).getType() == 3)
                    {
                        img_act_result.setImageResource(R.drawable.ic_auction_fail);
                        ll_name.setVisibility(View.GONE);
                        ll_pay.setVisibility(View.GONE);
                        ll_act_fail.setVisibility(View.VISIBLE);
                        txv_act_fail.setText("您参与的竞拍超时未付款");
                        break;
                    } else if(customMsgAuctionNotifyPay.getBuyer().get(i).getType() == 1)
                    {
                        img_act_result.setImageResource(R.drawable.ic_auction_suc);
                        ll_name.setVisibility(View.VISIBLE);
                        ll_pay.setVisibility(View.GONE);
                        ll_act_fail.setVisibility(View.GONE);
                        txv_name.setText(customMsgAuctionNotifyPay.getBuyer().get(i).getNick_name());
                        txv_price.setText(customMsgAuctionNotifyPay.getBuyer().get(i).getPai_diamonds());
                        break;
                    }
                }
                break;
        }
    }

    private void showPayDialog(PaiBuyerModel buyerModel)
    {
        AuctionSucPayDialog dialog = AuctionSucPaySingleton.getInstance().getAuctionSucPayDialog();
        if (dialog != null)
        {
            dialog.showBottom();
        }
    }
}
