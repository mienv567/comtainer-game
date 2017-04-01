package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.R;
import com.fanwe.live.appview.ILiveGiftView;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.fanwe.live.model.custommsg.ILiveGiftMsg;
import com.fanwe.live.view.LiveGiftPlayView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 礼物播放
 *
 * @author Administrator
 * @date 2016-5-16 下午1:16:27
 */
public class RoomGiftPlayView extends RoomView {
    public RoomGiftPlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomGiftPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomGiftPlayView(Context context) {
        super(context);
    }

    private static final long DURATION_LOOPER = 200;

    private LiveGiftPlayView view_gift_play0;
    private LiveGiftPlayView view_gift_play1;

    private List<ILiveGiftView> listPlayView;
    private CopyOnWriteArrayList<ILiveGiftMsg> listMsg;
    private SDLooper looper;

    private ConcurrentHashMap<ILiveGiftMsg, ILiveGiftMsg> mapGiftNumber;

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_gift_play;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        view_gift_play0 = find(R.id.view_gift_play0);
        view_gift_play1 = find(R.id.view_gift_play1);

        listPlayView = new ArrayList<>();
        listMsg = new CopyOnWriteArrayList<>();
        looper = new SDSimpleLooper();
        mapGiftNumber = new ConcurrentHashMap<>();

        listPlayView.add(view_gift_play0);
        listPlayView.add(view_gift_play1);
    }

    private void looperWork() {
        boolean foundMsg = false;
        for (ILiveGiftView view : listPlayView) {
            if (view.canPlay()) {
                // 如果当前view满足播放条件，开始遍历msg列表，寻找可以播放的msg
                for (ILiveGiftMsg msg : listMsg) {
                    if (msg.isTaked()) {
                        continue;
                    } else {
                        // 如果本条msg还没被播放过，遍历view列表，寻找是否已经有包含本条msg的view
                        ILiveGiftView currentMsgView = null;
                        for (ILiveGiftView otherView : listPlayView) {
                            if (otherView.containsMsg(msg)) {
                                currentMsgView = otherView;
                                break;
                            }
                        }
                        if (currentMsgView != null) {
                            // 如果找到包含本条msg的view
                            if (view != currentMsgView) {
                                // 这个view不是自己，跳过此条消息
                                continue;
                            } else {
                                // 如果这个view是自己
                                foundMsg = view.playMsg(msg);
                            }
                        } else {
                            // 如果没找到包含本条msg的view
                            foundMsg = view.playMsg(msg);
                        }
                    }
                }
            }
        }

        if (!foundMsg) {
            boolean isAllViewFree = true;
            for (ILiveGiftView view : listPlayView) {
                if (view.isPlaying()) {
                    isAllViewFree = false;
                    break;
                }
            }
            if (isAllViewFree) {
                looper.stop();
            }
        }
    }

    private void addMsg(ILiveGiftMsg newMsg) {
        if (newMsg != null) {
            if (!newMsg.canPlay()) {
                return;
            }

            if (newMsg.isPlusMode()) {
                if (newMsg.needPlus()) {
                    ILiveGiftMsg mapMsg = mapGiftNumber.get(newMsg);
                    if (mapMsg != null) {
                        newMsg.setShowNum(mapMsg.getShowNum() + 1);
                        mapGiftNumber.remove(newMsg);
                    }
                }
                mapGiftNumber.put(newMsg, newMsg);
                LogUtil.i("map size:" + mapGiftNumber.size());
            }

            for(ILiveGiftMsg msg : listMsg){
                if(msg.isTaked()){
                    listMsg.remove(msg);
                }
            }

            listMsg.add(newMsg);

            if (!looper.isRunning()) {
                looper.start(DURATION_LOOPER, new Runnable() {

                    @Override
                    public void run() {
                        looperWork();
                    }
                });
            }
        }
    }

    public void onEvent(EImOnNewMessages event) {
        String peer = event.msg.getConversationPeer();
        if (event.msg.isLocalPost()) {

        } else {
            if (peer.equals(getLiveInfo().getGroupId())) {
                if (CustomMsgType.MSG_GIFT == event.msg.getCustomMsgType()) {
                    final CustomMsgGift msg = event.msg.getCustomMsgGift();
                    msg.setIs_plus(1);
                    msg.setIs_much(1);
                    if(msg.getNum() <= 1){
                        this.post(new Runnable() {
                            @Override
                            public void run() {
                                addMsg(msg);
                            }
                        });
                    }else{
                        final List<ILiveGiftMsg> giftList = new ArrayList<>();
                        for(int i=0;i<msg.getNum();i++){
                            CustomMsgGift singleMsg = new CustomMsgGift();
                            singleMsg.setProp_id(msg.getProp_id());
                            singleMsg.setAnimated_url(msg.getAnimated_url());
                            singleMsg.setIcon(msg.getIcon());
                            singleMsg.setNum(msg.getNum());
                            singleMsg.setIs_plus(msg.getIs_plus());
                            singleMsg.setIs_much(msg.getIs_much());
                            singleMsg.setIs_animated(msg.getIs_animated());
                            singleMsg.setAnim_type(msg.getAnim_type());
                            singleMsg.setTotalTicket(msg.getTotalTicket());
                            singleMsg.setToUserId(msg.getToUserId());
                            singleMsg.setFonts_color(msg.getFonts_color());
                            singleMsg.setDesc(msg.getDesc());
                            singleMsg.setDesc2(msg.getDesc2());
                            singleMsg.setDesc3(msg.getDesc3());
                            singleMsg.setPreview_url(msg.getPreview_url());
                            singleMsg.setTop_title(msg.getTop_title());
                            singleMsg.setTotalActivityTicket(msg.getTotalActivityTicket());
                            singleMsg.setShowNum(1);
                            singleMsg.setTaked(msg.isTaked());
                            singleMsg.setType(msg.getType());
                            singleMsg.setSender(msg.getSender());
                            singleMsg.setDeviceType(msg.getDeviceType());
                            if (singleMsg.isPlusMode()) {
                                if (singleMsg.needPlus()) {
                                    ILiveGiftMsg mapMsg = mapGiftNumber.get(singleMsg);
                                    if (mapMsg != null) {
                                        singleMsg.setShowNum(mapMsg.getShowNum() + 1);
                                        mapGiftNumber.remove(singleMsg);
                                    }
                                }
                                mapGiftNumber.put(singleMsg, singleMsg);
                                LogUtil.i("map size:" + mapGiftNumber.size());
                            }
                            giftList.add(singleMsg);
                        }
                        for(ILiveGiftMsg msg_ : listMsg){
                            if(msg_.isTaked()){
                                listMsg.remove(msg_);
                            }
                        }
                        this.post(new Runnable() {
                            @Override
                            public void run() {
                                listMsg.addAll(giftList);
                                if (!looper.isRunning()) {
                                    looper.start(DURATION_LOOPER, new Runnable() {

                                        @Override
                                        public void run() {
                                            looperWork();
                                        }
                                    });
                                }
                            }
                        });
                    }
                } else if (CustomMsgType.MSG_AUCTION_OFFER == event.msg.getCustomMsgType()) {
                    final CustomMsgAuctionOffer msg = event.msg.getCustomMsgAuctionOffer();
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            addMsg(msg);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        looper.stop();
        super.onDestroy();
    }

}
