package com.fanwe.live.appview.room;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.blocker.SDBlocker;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EEnterRoomComplete;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.custommsg.CustomMsgLight;
import com.fanwe.live.view.HeartLayout;

import java.util.LinkedList;

/**
 * 心心
 */
public class RoomHeartView extends RoomLooperMainView<CustomMsgLight> {

    public RoomHeartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomHeartView(Context context) {
        super(context);
    }

    private static final long DURATION_LOOPER = 400;

    private HeartLayout view_heart;
    private SDBlocker blocker;
    private String conversationId = "";
    private boolean isFirst = true;

    public HeartLayout getViewHeart() {
        return view_heart;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_heart;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        view_heart = find(R.id.view_heart);

        blocker = new SDBlocker(100);
    }

    public void onEventMainThread(EEnterRoomComplete event) {
        if (event.roomId == getLiveInfo().getRoomId()) {
            isFirst = true;
        }
    }

    public void addHeart() {
        if (blocker.block()) {
            return;
        }
        sendLightMsg();
    }


    private void sendLightMsg() {
        final CustomMsgLight msg = new CustomMsgLight();

        String name = view_heart.randomImageName();
        msg.setImageName(name);
        if (isFirst) {
            isFirst = false;
            msg.setShowMsg(1);
            CommonInterface.requestLike(getLiveInfo().getRoomId(), new AppRequestCallback<BaseActModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    if (actModel.isOk()) {
                        LogUtil.i("request like success");
                    }
                }
            });
//            IMHelper.sendMsgGroup(getLiveInfo().getGroupId(), msg, new TIMValueCallBack<TIMMessage>() {
//
//                @Override
//                public void onSuccess(TIMMessage timMessage) {
//                    conversationId = timMessage.getConversation().getPeer();
//                    IMHelper.postMsgLocal(msg, conversationId);
//                }
//
//                @Override
//                public void onError(int code, String desc) {
//                }
//            });
            CommonInterface.requestLight(getLiveInfo().getGroupId(), getLiveInfo().getRoomId(), 1, new AppRequestCallback<BaseActModel>() {
                @Override
                protected void onSuccess(SDResponse sdResponse) {
                    conversationId = getLiveInfo().getGroupId();
//                    IMHelper.postMsgLocal(msg, conversationId);
                }
            });
        } else {
            msg.setShowMsg(0);
            if(!TextUtils.isEmpty(conversationId)) {
                IMHelper.postMsgLocal(msg, conversationId);
            }
        }

    }

    @Override
    protected void startLooper(long period) {
        super.startLooper(DURATION_LOOPER);
    }

    @Override
    protected void looperWork(LinkedList<CustomMsgLight> queue) {
        CustomMsgLight msg = queue.poll();
        if (msg != null&&msg.getShowMsg()<=1) {
            view_heart.addHeart(msg.getImageName());
        }
    }

    public void onEventMainThread(EImOnNewMessages event) {
        try {
            String peer = event.msg.getConversationPeer();
            if (peer.equals(getLiveInfo().getGroupId())) {
                if (CustomMsgType.MSG_LIGHT == event.msg.getCustomMsgType()) {
                    CustomMsgLight msg = event.msg.getCustomMsgLight();
                    offerModel(msg);
                }
            }
        } catch (Exception e) {
            SDToast.showToast(e.toString());
        }
    }

}
