package com.fanwe.auction.dialog.room;

import android.app.Activity;

import com.fanwe.auction.dialog.AuctionSucPayDialog;
import com.fanwe.auction.model.PaiBuyerModel;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.looper.impl.SDHandlerLooper;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class AuctionSucPaySingleton
{
    private AuctionSucPayDialog auctionSucPayDialog;

    public AuctionSucPayDialog getAuctionSucPayDialog()
    {
        return auctionSucPayDialog;
    }

    public AuctionSucPayDialog newInstanceAuctionSucPayDialog(Activity activity, List<PaiBuyerModel> list)
    {
        UserModel user = UserModelDao.query();
        if (user == null)
        {
            return null;
        }
        PaiBuyerModel p = null;
        if (list != null && list.size() > 0)
        {
            for (int i = 0; i < list.size(); i++)
            {
                PaiBuyerModel paiBuyerModel = list.get(i);
                if (user.getUserId().equals(paiBuyerModel.getUser_id()) && paiBuyerModel.getType() == 1)
                {
                    //未付款存在倒计时的人和本地登录的人一样
                    p = paiBuyerModel;
                    break;
                }
            }
            if (p != null)
            {
                auctionSucPayDialog = new AuctionSucPayDialog(activity, p);
                startHandlerRank(p.getLeft_time());
            } else
            {
                startHandlerRank(0);
            }
        }
        return auctionSucPayDialog;
    }

    private static AuctionSucPaySingleton auctionSucPaySingleton;

    public static AuctionSucPaySingleton getInstance()
    {
        if (auctionSucPaySingleton == null)
        {
            auctionSucPaySingleton = new AuctionSucPaySingleton();
        }
        return auctionSucPaySingleton;
    }

    private AuctionSucPaySingleton()
    {

    }

    private SDHandlerLooper handler = new SDHandlerLooper(SDHandlerManager.getMainHandler());

    public void onDestoryHandler()
    {
        if (handler != null)
        {
            handler.stop();
        }
        auctionSucPayDialog = null;
    }

    public void startHandlerRank(final long left_time)
    {
        handler.start(0, 1000, new Runnable()
        {
            long time = left_time;

            @Override
            public void run()
            {
                if (time >= 0)
                {
                    final long day = SDDateUtil.getDuringDay(time * 1000);
                    final long hour = SDDateUtil.getDuringHours(time * 1000);
                    final long min = SDDateUtil.getDuringMinutes(time * 1000);
                    final long sec = SDDateUtil.getDuringSeconds(time * 1000);
                    if (onTimerResultCallBack != null)
                    {
                        onTimerResultCallBack.timerResultCallBac(day, hour, min, sec);
                    }
                    time--;
                } else
                {
                    handler.stop();
                }
            }
        });
    }

    private OnTimerResultCallBack onTimerResultCallBack;

    public void addAuctionSucPaySingleton(OnTimerResultCallBack onTimerResultCallBack)
    {
        this.onTimerResultCallBack = onTimerResultCallBack;
    }

    public interface OnTimerResultCallBack
    {
        void timerResultCallBac(long day, long hour, long min, long sec);
    }
}
