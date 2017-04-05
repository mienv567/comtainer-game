package com.fanwe.live.appview.room;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.LiveLikeModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveActivity;
import com.fanwe.live.appview.LiveSendGiftView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_pop_propActModel;
import com.fanwe.live.model.Deal_send_propActModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.custommsg.CustomMsgPrivateGift;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

public class RoomSendGiftView extends RoomView {

    private LiveSendGiftView view_send_gift;

    public RoomSendGiftView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomSendGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomSendGiftView(Context context) {
        super(context);
    }

    @Override
    protected int onCreateContentView() {
        if(isLandscape()){
            return R.layout.frag_live_send_gift_h;
        } else {
            return R.layout.frag_live_send_gift;
        }
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();

        RadioGroup radio_group = (RadioGroup) findViewById(R.id.radio_group);

        view_send_gift = (LiveSendGiftView) findViewById(R.id.view_send_gift);
        view_send_gift.setClickListener(new LiveSendGiftView.ClickListener() {
            @Override
            public void onClickSend(SDSelectManager.SDSelectable model, int count, int is_plus) {
                if (view_send_gift.isShownGift()) {
                    LiveGiftModel giftModel = (LiveGiftModel) model;
                    sendGift(giftModel, count, is_plus, getLiveInfo().getRoomId(), getLiveInfo().getGroupId());
                } else {
                    LiveLikeModel likeModel = (LiveLikeModel) model;
                    CommonInterface.requestThumbsUp(getLiveInfo().getCreaterId(), new AppRequestCallback<BaseActModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            if (rootModel.isOk()) {
                                SDToast.showToast("点赞成功");
                            } else {
                                SDToast.showToast("点赞失败");
                            }
                        }
                    });

                }
            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_btn_props:
                        view_send_gift.showGiftAdapter();
                        break;
                    case R.id.radio_btn_like:
                        view_send_gift.showLikeAdapter();
                        break;
                }
            }
        });

        invisible();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(isLandscape()){
            setShowAnimator(SDAnimationUtil.translateInRight(this));
            setHideAnimator(SDAnimationUtil.translateOutRight(this));
        } else {
            setShowAnimator(SDAnimationUtil.translateInBottom(this));
            setHideAnimator(SDAnimationUtil.translateOutBottom(this));
        }
    }

    public boolean isLandscape(){
        return getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    private void sendGift(final LiveGiftModel giftModel, final int giftNumber, int is_plus, int roomId, String groupId) {
        if (giftModel != null) {
            if (giftModel.getIs_much() != 1) {
                SDToast.showToast(SDResourcesUtil.getString(R.string.send_complete));
            }

            if (getLiveInfo() == null || getLiveInfo().getRoomInfo() == null) {
                return;
            }

            if (getLiveInfo().getRoomInfo().getLive_in() == 0) {
                //私聊发礼物接口
                final String createrId = getLiveInfo().getCreaterId();
                if (createrId != null) {
                    CommonInterface.requestSendGiftPrivateJava(giftModel.getPropId(), giftNumber, createrId, roomId, groupId, new AppRequestCallback<Deal_send_propActModel>() {
                        @Override
                        protected void onSuccess(SDResponse resp) {
                            if (rootModel.isOk()) {
                                view_send_gift.sendGiftSuccess(giftModel);

                                // 发送私聊消息给主播
                                final CustomMsgPrivateGift msg = new CustomMsgPrivateGift();
                                msg.fillData(actModel);
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        IMHelper.sendMsgC2C(createrId, msg, new TIMValueCallBack<TIMMessage>() {
                                            @Override
                                            public void onError(int i, String s) {
                                            }

                                            @Override
                                            public void onSuccess(TIMMessage timMessage) {
                                                // 如果私聊界面不是每次都加载的话要post一条来刷新界面
                                                // IMHelper.postMsgLocal(msg, createrId);
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            } else {
                AppRequestParams params = CommonInterface.requestSendGiftParams(giftModel.getPropId(), giftNumber, is_plus, getLiveInfo().getRoomId());
                AppHttpUtil.getInstance().post(params, new AppRequestCallback<App_pop_propActModel>() {
                    @Override
                    protected void onSuccess(SDResponse resp) {
                        // 扣费
                        if (rootModel.isOk()) {
                            //                            if(giftNumber>1){
                            view_send_gift.sendGiftSuccess(giftModel, giftNumber);
                            //                            }else {
                            //                                view_send_gift.sendGiftSuccess(giftModel);
                            //                            }
                        }
                    }

                    @Override
                    protected void onError(SDResponse resp) {
                        CommonInterface.requestMyUserInfoJava(null);
                    }
                });
            }
        }
    }

    public void requestData(LiveActivity activity) {
        if (view_send_gift != null) {
            view_send_gift.requestData(activity.getTopicId());
        }
    }

    public void bindUserData() {
        if (view_send_gift != null) {
            view_send_gift.bindUserData();
        }
    }

    @Override
    protected boolean onTouchDownOutside(MotionEvent ev) {
        invisible(true);
        return true;
    }

    @Override
    public boolean onBackPressed() {
        invisible(true);
        return true;
    }
}
