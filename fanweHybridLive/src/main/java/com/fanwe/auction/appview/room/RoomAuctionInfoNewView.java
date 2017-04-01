package com.fanwe.auction.appview.room;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.auction.activity.AuctionGoodsDetailActivity;
import com.fanwe.auction.model.App_pai_user_get_videoActModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataInfoModel;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.appview.room.RoomView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.event.EImOnNewMessages;

/**
 * Created by luodong on 2016/9/9.
 */
public class RoomAuctionInfoNewView extends RoomView {
    public RoomAuctionInfoNewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomAuctionInfoNewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomAuctionInfoNewView(Context context) {
        super(context);
    }

    private LinearLayout ll_auction_info;//竞拍信息位置
    private LinearLayout ll_remaining_time;//倒计时位置
    private LinearLayout ll_top_price;
    private TextView tv_top_price;//当前最高价
    private TextView tv_remaining_time;//剩余竞拍时间

    private App_pai_user_get_videoActModel actModel;

    private TimerHandler timerHandler;//mHandler刷新UI界面
    private TimerThread timerThread;//竞拍线程
    private long time;//时间
    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_auction_info;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        ll_auction_info = find(R.id.ll_auction_info);
        if (AppRuntimeWorker.getShow_hide_pai_view() == 1) {
            SDViewUtil.show(ll_auction_info);
        }

        ll_top_price = find(R.id.ll_top_price);
        ll_top_price.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PaiUserGoodsDetailDataInfoModel model = actModel.getDataInfo();
                if (model != null) {
                    int id = model.getId();
                    boolean isCreater = getLiveInfo().isCreater();
                    if (id > 0) {
                        Intent intent = new Intent(getActivity(), AuctionGoodsDetailActivity.class);
                        intent.putExtra(AuctionGoodsDetailActivity.EXTRA_IS_ANCHOR, isCreater);
                        intent.putExtra(AuctionGoodsDetailActivity.EXTRA_ID, String.valueOf(id));
                        getActivity().startActivity(intent);
                    } else {
                        SDToast.showToast("id==0");
                    }
                }
            }
        });
        tv_top_price = find(R.id.tv_top_price);
        ll_remaining_time = find(R.id.ll_remaining_time);
        tv_remaining_time = find(R.id.tv_remaining_time);
    }

    public void bindAuctionDetailInfo(App_pai_user_get_videoActModel actModel) {
        this.actModel = actModel;
        PaiUserGoodsDetailDataInfoModel info = actModel.getDataInfo();
        if (info != null) {
            //接收请求详情成功后显示
            SDViewUtil.show(ll_auction_info);
            SDViewBinder.setTextView(tv_top_price, Integer.toString(info.getLast_pai_diamonds()));
            setCountdownTime(info.getPai_left_time());
        }
    }

    private void setCountdownTime(final long left_time) {
        time = left_time;
        if (timerThread == null) {
            timerThread = new TimerThread();
        }
        if (timerHandler == null) {
            timerHandler = new TimerHandler();
        }
        timerThread.start();
    }

    private class TimerHandler extends Handler{
        private long mTime;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTime = (long)msg.what;
            if (mTime >= 0) {
                final long hour = SDDateUtil.getDuringHours(mTime * 1000);
                final long min = SDDateUtil.getDuringMinutes(mTime * 1000);
                final long sec = SDDateUtil.getDuringSeconds(mTime * 1000);
                tv_remaining_time.setText(Long.toString(hour) + "时" + Long.toString(min) + "分" + Long.toString(sec) + "秒");
                time--;
            } else {
                SDViewUtil.hide(ll_remaining_time);
                tv_remaining_time.setText("已结束");
                timerThread.interrupt();
            }
        }
    }

    private class TimerThread extends Thread{
        //共享变量
        private volatile boolean isStop = false;
        @Override
        public void interrupt(){
            //调用interrupt之前，把isStop置为false
            isStop = true;
            super.interrupt();
        }
        @Override
        public void run(){
            while(!isStop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = (int) time;
                timerHandler.sendMessage(message);
            }
        }
    }

    //接收出价信息时候刷新出价信息
    public void onEventMainThread(EImOnNewMessages event) {
        String peer = event.msg.getConversationPeer();
        if (event.msg.isLocalPost()) {

        } else {
            if (peer.equals(getLiveInfo().getGroupId())) {
                if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_OFFER) {
                    CustomMsgAuctionOffer customMsgAuctionOffer = event.msg.getCustomMsgAuctionOffer();
                    SDViewBinder.setTextView(tv_top_price, Integer.toString(customMsgAuctionOffer.getPai_diamonds()));
                } else if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_FAIL) {
                    //流拍后隐藏该布局
                    SDViewUtil.hide(ll_auction_info);
                } else if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_PAY_SUCCESS) {
                    //付款成功后隐藏该布局
                    SDViewUtil.hide(ll_auction_info);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        timerThread.interrupt();
        super.onDestroy();
    }
}
