package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.library.blocker.SDBlocker;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.R;
import com.fanwe.live.event.EEnterRoomComplete;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.custommsg.CustomMsgLightRight;
import com.fanwe.live.view.HeartLayout;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import java.util.LinkedList;

/**
 * 带数字心心
 */
public class RoomHeartViewWithCount extends  RoomLooperMainView<CustomMsgLightRight> {

    public RoomHeartViewWithCount(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomHeartViewWithCount(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomHeartViewWithCount(Context context) {
        super(context);
    }

    private static final long DURATION_LOOPER = 400;

    private HeartLayout view_heart_right;
    private SDBlocker blocker;

    private boolean isFirst = true;

    public HeartLayout getViewHeart() {
        return view_heart_right;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_heart_right;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        view_heart_right = find(R.id.view_heart_right);

        blocker = new SDBlocker(100);
    }

    public void onEventMainThread(EEnterRoomComplete event) {
        if (event.roomId == getLiveInfo().getRoomId()) {
            isFirst = true;
        }
    }

    public void addHeart(int count) {
//        if (blocker.block()) {
//            return;
//        }
        if(count == 99) {
            sendLightMsg(count);
        }
    }

    private void sendLightMsg(int count) {
        final CustomMsgLightRight msg = new CustomMsgLightRight();

        String name = view_heart_right.randomImageName();
        msg.setImageName(name);
        msg.setCount(count);
        msg.setShowMsg(2);

        IMHelper.sendMsgGroup(getLiveInfo().getGroupId(), msg, new TIMValueCallBack<TIMMessage>() {

            @Override
            public void onSuccess(TIMMessage timMessage) {
                IMHelper.postMsgLocal(msg, timMessage.getConversation().getPeer());
            }

            @Override
            public void onError(int code, String desc) {
            }
        });

    }

    @Override
    protected void startLooper(long period) {
        super.startLooper(DURATION_LOOPER);
    }

    @Override
    protected void looperWork(LinkedList<CustomMsgLightRight> queue) {
        CustomMsgLightRight msg = queue.poll();
        if (msg != null&&msg.getType()==CustomMsgType.MSG_CUSTOM_LIGHT) {
            view_heart_right.addHeart(msg.getImageName(),msg.getCount()+"");
        }
    }

    public void onEventMainThread(EImOnNewMessages event) {
        try {
            String peer = event.msg.getConversationPeer();
            if (peer.equals(getLiveInfo().getGroupId())) {
                if (CustomMsgType.MSG_CUSTOM_LIGHT == event.msg.getCustomMsgType()) {
                    CustomMsgLightRight msg = event.msg.getCustomMsgLightRight();
                    offerModel(msg);
                }
            }
        } catch (Exception e) {
            SDToast.showToast(e.toString());
        }
    }

}
