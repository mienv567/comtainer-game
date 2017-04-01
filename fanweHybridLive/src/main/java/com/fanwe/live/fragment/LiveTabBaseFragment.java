package com.fanwe.live.fragment;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LiveTabBaseFragment extends BaseFragment
{
    private static final long DURATION_LOOP = 20 * 1000;

    private SDLooper looper = new SDSimpleLooper();

    public void scrollToTop()
    {

    }

    public void onEventMainThread(EImOnNewMessages event)
    {
        try
        {
            if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_LIVE_STOPPED)
            {
                onMsgLiveStopped(event.msg);
            }
        } catch (Exception e)
        {
            SDToast.showToast(e.toString());
        }
    }

    protected void onMsgLiveStopped(MsgModel msgModel)
    {

    }

    /**
     * 开始定时执行任务，每隔一段时间执行一下onLoopRun()方法
     */
    protected void startLoopRunnable()
    {
        stopLoopRunnable();
        looper.start(DURATION_LOOP, DURATION_LOOP, loopRunnable);
    }

    /**
     * 停止定时任务
     */
    protected void stopLoopRunnable()
    {
        looper.stop();
    }

    private Runnable loopRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            LogUtil.i(getClass().getName() + ":onLoopRun");
            onLoopRun();
        }
    };

    /**
     * 定时执行任务
     */
    protected void onLoopRun()
    {

    }

    @Override
    public void onPause()
    {
        stopLoopRunnable();
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        stopLoopRunnable();
        super.onDestroy();
    }
}
