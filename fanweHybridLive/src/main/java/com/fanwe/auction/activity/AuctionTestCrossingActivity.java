package com.fanwe.auction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.auction.appview.AuctionUserRanklistView;
import com.fanwe.auction.model.PaiBuyerModel;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.looper.impl.SDHandlerLooper;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.live.R;
import com.fanwe.auction.appview.room.RoomTimerTextView;
import com.fanwe.shopping.activity.ShoppingMystoreActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class AuctionTestCrossingActivity extends BaseTitleActivity
{
    @ViewInject(R.id.btn_auction_detail)
    private Button btn_auction_detail;
    @ViewInject(R.id.btn_dialog)
    private Button btn_dialog;
    @ViewInject(R.id.tv_status)
    private RoomTimerTextView tv_status;
    @ViewInject(R.id.ll_test)
    private LinearLayout ll_test;

    //倒计时测试
    private TimerHandler timerHandler;//mHandler刷新UI界面
    private TimerThread timerThread;//竞拍线程
    private long time;//时间

    //竞拍倒计时
    private SDHandlerLooper handler = new SDHandlerLooper(SDHandlerManager.getMainHandler());

    @ViewInject(R.id.tv_new_time)
    private TextView tv_new_time;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_crossing);
        x.view().inject(this);
        init();
        addViews();

        setTestTime();
    }

    private void setTestTime() {
        setTimerThread(7200*1000);
        setHandlerTime(7200*1000);
    }

    private void init()
    {
        btn_auction_detail.setOnClickListener(this);
        btn_dialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.btn_auction_detail:
                clickBtnAuctionDetail();
                break;
            case R.id.btn_dialog:
                clickBtnShop();
                break;
        }
    }

    private void clickBtnShop() {
        Intent intent = new Intent(this, ShoppingMystoreActivity.class);
        startActivity(intent);
//        AuctionPodCastDialog dialog = new AuctionPodCastDialog(AuctionTestCrossingActivity.this);
//        dialog.showBottom();
    }

    private void addViews() {
        List<PaiBuyerModel> list = new ArrayList<>();
        PaiBuyerModel model = new PaiBuyerModel();
        model.setLeft_time(60);
        model.setPai_diamonds("99999999");
        model.setType(1);
        list.add(model);
        PaiBuyerModel model2 = new PaiBuyerModel();
        model2.setLeft_time(0);
        model2.setPai_diamonds("88888888");
        model2.setType(0);
        list.add(model2);
        PaiBuyerModel model3 = new PaiBuyerModel();
        model3.setLeft_time(0);
        model3.setPai_diamonds("77777777");
        model3.setType(0);
        list.add(model3);
        AuctionUserRanklistView auctionUserRanklistView = new AuctionUserRanklistView(this);
        auctionUserRanklistView.setBuyers(list);
        replaceView(ll_test, auctionUserRanklistView);
    }

    private void clickBtnAuctionDetail()
    {
        Intent intent = new Intent(this, AuctionGoodsDetailActivity.class);
        startActivity(intent);
    }

    //倒计时测试
    private void setTimerThread(final long left_time) {
        time = left_time;
        if (timerThread == null) {
            timerThread = new TimerThread();
        }
        if (timerHandler == null) {
            timerHandler = new TimerHandler();
        }
        timerThread.start();
    }

    private class TimerHandler extends Handler {
        private long mTime;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTime = (long)msg.what;
            if (mTime >= 0) {
                final long hour = SDDateUtil.getDuringHours(mTime * 1000);
                final long min = SDDateUtil.getDuringMinutes(mTime * 1000);
                final long sec = SDDateUtil.getDuringSeconds(mTime * 1000);
                tv_new_time.setText(Long.toString(hour) + "时" + Long.toString(min) + "分" + Long.toString(sec) + "秒newThread");
                time--;
            } else {
//                SDViewUtil.hide(ll_remaining_time);
                tv_new_time.setText("已结束");
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





    private void setHandlerTime(final long left_time)
    {
        handler.start(0, 1000, new Runnable()
        {
            long time = left_time;
            @Override
            public void run()
            {
                if (time >= 0)
                {
                    final long hour = SDDateUtil.getDuringHours(time * 1000);
                    final long min = SDDateUtil.getDuringMinutes(time * 1000);
                    final long sec = SDDateUtil.getDuringSeconds(time * 1000);
                    tv_time.setText(Long.toString(hour) + "时" + Long.toString(min) + "分" + Long.toString(sec) + "秒handler");
                    time--;
                } else
                {
//                    SDViewUtil.hide(tv_time);
                    tv_time.setText("已结束");
                    handler.stop();
                }
            }
        });
    }


}
