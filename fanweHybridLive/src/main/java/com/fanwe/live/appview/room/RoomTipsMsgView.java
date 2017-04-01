package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.R;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.listeners.OnMarqueeListener;
import com.fanwe.live.model.custommsg.CustomMsgTipsMsg;
import com.fanwe.live.view.LiveTipsMsgView;

import java.util.LinkedList;

/**
 * 全局tips消息
 *
 * @author Administrator
 * @date 2016-5-20 下午5:21:09
 */
public class RoomTipsMsgView extends RoomLooperBackgroundView<CustomMsgTipsMsg> {

    public void setListener(OnMarqueeListener listener) {
        this.listener = listener;
        view_pop_msg0.setListener(listener);
    }

    private OnMarqueeListener listener;

    public RoomTipsMsgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomTipsMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomTipsMsgView(Context context) {
        super(context);
    }


    private static final long DURATION_LOOPER = 500;

    private LiveTipsMsgView view_pop_msg0;


    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_tips_msg;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        view_pop_msg0 = find(R.id.view_tips_msg0);
    }

    public void onEventMainThread(final EImOnNewMessages event) {
        SDHandlerManager.getBackgroundHandler().post(new Runnable() {
            @Override
            public void run() {
                if (event.msg.isLocalPost()) {

                } else {
                    if (CustomMsgType.MSG_TIPS_MSG == event.msg.getCustomMsgType()) {
                        CustomMsgTipsMsg msg = event.msg.getCustomMsgTipsMsg();
                        offerModel(msg);
                    }
                }
            }
        });
    }

    @Override
    protected void startLooper(long period) {
        super.startLooper(DURATION_LOOPER);
    }

    @Override
    protected void looperWork(LinkedList<CustomMsgTipsMsg> queue) {

        if (view_pop_msg0.canPlay()) {
            final CustomMsgTipsMsg msg = queue.poll();
            view_pop_msg0.playMsg(msg);
            if (listener != null && msg.getStatus() == 1) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onMarqueeStart(msg.getStatus());
                    }
                });
            }
        }

    }
}
