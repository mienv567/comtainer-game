package com.fanwe.live.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.umeng.UmengSocialManager;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveVideoActivity;
import com.fanwe.live.appview.room.RoomMsgView;
import com.fanwe.live.appview.room.RoomSendGiftView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveRechargeDialog;
import com.fanwe.live.event.ERefreshGiftList;
import com.fanwe.live.event.ERoomInfoReady;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_shareActModel;
import com.fanwe.live.model.RoomShareModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgShare;
import com.fanwe.live.model.custommsg.CustomMsgText;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

import static com.fanwe.live.activity.room.LiveLayoutActivity.convertEnumToString;

public class LiveChatFragment extends BaseFragment implements TextView.OnEditorActionListener {

    //主播信息
    @ViewInject(R.id.rl_creator_info)
    private RelativeLayout rl_creator_info;
    //主播头像
    @ViewInject(R.id.iv_head)
    private CircleImageView iv_head;
    //主播昵称
    @ViewInject(R.id.tv_nick_name)
    private TextView tv_nick_name;
    //人气值
    @ViewInject(R.id.popular_number)
    private TextView popular_number;
    //关注数
    @ViewInject(R.id.follow_number)
    private TextView follow_number;
    //关注按钮
    @ViewInject(R.id.tv_follow)
    private TextView tv_follow;
    //隐藏箭头
    @ViewInject(R.id.iv_hide_creater_info)
    private ImageView iv_hide_creater_info;

    //聊天消息显示窗
    @ViewInject(R.id.ll_chat_bottom)
    private LinearLayout ll_chat_bottom;
    //聊天消息显示窗
    @ViewInject(R.id.fl_live_msg)
    private FrameLayout fl_live_msg;
    //分享按钮
    @ViewInject(R.id.iv_share)
    private ImageView iv_share;
    //消息编辑框
    @ViewInject(R.id.tv_msg)
    private TextView tv_msg;
    //礼物按钮
    @ViewInject(R.id.iv_gift)
    private ImageView iv_gift;
    //充值按钮
    @ViewInject(R.id.iv_recharge)
    private ImageView iv_recharge;
    //礼物弹出视图
    @ViewInject(R.id.fl_live_send_gift)
    private FrameLayout fl_live_send_gift;

    private LiveVideoActivity mActivity;
    private PopupWindow sharePopupWindow;
    private PopupWindow msgEditPopupWindow;

    protected UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            sendShareSuccessMsg(share_media, null);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
        }
    };
    private RoomSendGiftView roomSendGiftView;

    @Override
    protected int onCreateContentView() {
        return R.layout.fragment_live_chat;
    }

    @Override
    protected void init() {
        mActivity = (LiveVideoActivity) getActivity();

        RoomMsgView roomMsgView = new RoomMsgView(getContext());
        SDViewUtil.replaceView(fl_live_msg, roomMsgView);

        roomSendGiftView = new RoomSendGiftView(mActivity);
        roomSendGiftView.requestData(mActivity);
        roomSendGiftView.bindUserData();
        MobclickAgent.onEvent(mActivity, "live_gift");

        initListener();
    }

    private void initListener() {
        iv_share.setOnClickListener(this);
        iv_gift.setOnClickListener(this);
        iv_recharge.setOnClickListener(this);
        tv_msg.setOnClickListener(this);
    }

    public void onEventMainThread(ERoomInfoReady event) {
        LogUtil.d("room info is ready");
        UserModel creatorModel = mActivity.getRoomInfo().getPodcast();
        if (creatorModel.getRelationship() == 0) {//未关注
            rl_creator_info.setVisibility(View.VISIBLE);
            SDViewBinder.setImageView(mActivity, creatorModel.getHeadImage(), iv_head, R.drawable.ic_default_head);
            SDViewBinder.setTextView(tv_nick_name, creatorModel.getNickName());
            SDViewBinder.setTextView(popular_number, getString(R.string.popular_number_is, creatorModel.getFansCount()));
            SDViewBinder.setTextView(follow_number, getString(R.string.follow_number_is, creatorModel.getFocusCount()));
            tv_follow.setEnabled(true);
            iv_hide_creater_info.setOnClickListener(this);
            tv_follow.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_share:
                showShareMenu();
                break;
            case R.id.iv_gift:
                showGiftView();
                break;
            case R.id.iv_recharge:
                showRechargeDialog();
                break;
            case R.id.iv_hide_creater_info:
                hideCreaterInfo();
                break;
            case R.id.tv_follow:
                followCreator();
                break;
            case R.id.tv_msg:
                showMsgEditPopup();
                break;
        }
    }

    private void followCreator() {
        CommonInterface.requestFollow(mActivity.getCreaterId(), new AppRequestCallback<App_followActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    SDToast.showToast("关注成功");
                    hideCreaterInfo();
                } else {
                    SDToast.showToast(actModel.getError());
                }
            }
        });
    }

    private void hideCreaterInfo() {
        ObjectAnimator animator = SDAnimationUtil.translateOutRight(rl_creator_info);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rl_creator_info.setVisibility(View.GONE);
            }
        });
        animator.setDuration(300).start();
    }

    private void sendMessage(TextView view) {
        String strContent = view.getText().toString().trim();
        String groupId = mActivity.getGroupId();
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(strContent)) {
            return;
        }
        final CustomMsgText msg = new CustomMsgText();
        msg.setText(strContent);

        IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>() {

            @Override
            public void onSuccess(TIMMessage timMessage) {
                IMHelper.postMsgLocal(msg, timMessage.getConversation().getPeer());
            }

            @Override
            public void onError(int code, String desc) {
                if (code == 80001) {
                    SDToast.showToast(getResources().getString(R.string.live_dirty));
                }
            }
        });

        view.setText("");
    }

    private void showRechargeDialog() {
        LiveRechargeDialog dialog = new LiveRechargeDialog(mActivity);
        dialog.showCenter();
    }


    /**
     * 显示送礼的窗口
     */
    private void showGiftView() {
        SDViewUtil.replaceView(fl_live_send_gift, roomSendGiftView);
        roomSendGiftView.show(true);
    }

    /**
     * 显示分享面板
     */
    private void showShareMenu() {
        View popupContentView = View.inflate(mActivity, R.layout.view_live_share, null);
        final View share_qq = popupContentView.findViewById(R.id.ll_share_qq);
        final View share_qzoom = popupContentView.findViewById(R.id.ll_share_qzoom);
        final View share_weixin = popupContentView.findViewById(R.id.ll_share_weixin);
        final View share_pengyouquan = popupContentView.findViewById(R.id.ll_share_pengyouquan);
        final View share_xinlang = popupContentView.findViewById(R.id.ll_share_xinlang);

        View.OnClickListener shareViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == share_qq) {
                    openShare(0, umShareListener);
                } else if (v == share_qzoom) {
                    openShare(1, umShareListener);
                } else if (v == share_weixin) {
                    openShare(2, umShareListener);
                } else if (v == share_pengyouquan) {
                    openShare(3, umShareListener);
                } else if (v == share_xinlang) {
                    openShare(4, umShareListener);
                }
                sharePopupWindow.dismiss();
            }
        };

        popupContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    sharePopupWindow.dismiss();
                    sharePopupWindow = null;
                    return true;
                }
                return false;
            }
        });

        share_qq.setOnClickListener(shareViewListener);
        share_qzoom.setOnClickListener(shareViewListener);
        share_weixin.setOnClickListener(shareViewListener);
        share_pengyouquan.setOnClickListener(shareViewListener);
        share_xinlang.setOnClickListener(shareViewListener);

        sharePopupWindow = new PopupWindow(popupContentView, ViewGroup.LayoutParams.MATCH_PARENT, SDViewUtil.dp2px(80), true);
        sharePopupWindow.setAnimationStyle(R.style.path_popwindow_anim_enterorout_window);
        sharePopupWindow.setTouchable(true);
        sharePopupWindow.setOutsideTouchable(true);
        sharePopupWindow.getContentView().setFocusableInTouchMode(true);
        sharePopupWindow.getContentView().setFocusable(true);
        sharePopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePopupWindow.dismiss();
            }
        });

        sharePopupWindow.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    // 新增自定义面板的分享
    public void openShare(int shareMedia, final UMShareListener listener) {
        App_get_videoActModel roomInfoActModel = mActivity.getRoomInfo();
        if (roomInfoActModel == null) {
            return;
        }
        final RoomShareModel shareModel = roomInfoActModel.getShare();
        if (shareModel == null) {
            return;
        }
        UmengSocialManager.openShare(shareMedia, shareModel.getShareTitle(), shareModel.getShareContent(), shareModel.getShareImageUrl(),
                shareModel.getShareUrl(), mActivity, new UMShareListener() {

                    @Override
                    public void onResult(SHARE_MEDIA media) {
                        String shareKey = shareModel.getShareKey();
                        CommonInterface.requestShareComplete(media.toString(), shareKey, new AppRequestCallback<App_shareActModel>() {

                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                EventBus.getDefault().post(new ERefreshGiftList());
                            }
                        });

                        if (listener != null) {
                            listener.onResult(media);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA media, Throwable throwable) {
                        if (listener != null) {
                            listener.onError(media, throwable);
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA media) {
                        if (listener != null) {
                            listener.onCancel(media);
                        }
                    }
                });
        MobclickAgent.onEvent(mActivity, "live_share");
    }

    /**
     * 发送分享成功消息
     */
    protected void sendShareSuccessMsg(final SHARE_MEDIA share_media, final TIMValueCallBack<TIMMessage> listener) {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            final CustomMsgShare msg = new CustomMsgShare();
            msg.setDesc(" " + getString(R.string.share_live_to) + convertEnumToString(share_media));
            IMHelper.sendMsgGroup(mActivity.getGroupId(), msg, new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                    if (listener != null) {
                        listener.onError(i, s);
                    }
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    IMHelper.postMsgLocal(msg, mActivity.getGroupId());
                    if (listener != null) {
                        listener.onSuccess(timMessage);
                    }
                }
            });
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_SEND:
                sendMessage(v);
                SDViewUtil.hideInputMethod(v);
                break;
        }
        return false;
    }

    private void showMsgEditPopup() {
        View popupContentView = View.inflate(mActivity, R.layout.view_live_send_msg, null);
        ImageView iv_share_pop = (ImageView) popupContentView.findViewById(R.id.iv_share_pop);
        final EditText et_msg_pop = (EditText) popupContentView.findViewById(R.id.et_msg_pop);
        ImageView iv_gift_pop = (ImageView) popupContentView.findViewById(R.id.iv_gift_pop);
        ImageView iv_recharge_pop = (ImageView) popupContentView.findViewById(R.id.iv_recharge_pop);
        msgEditPopupWindow = new PopupWindow(popupContentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        msgEditPopupWindow.setFocusable(true);
        // 设置允许在外点击消失
        msgEditPopupWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        msgEditPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //必须加这两行，不然不会显示在键盘上方
        msgEditPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        msgEditPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // PopupWindow的显示及位置设置
        msgEditPopupWindow.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        SDViewUtil.showInputMethod(et_msg_pop);

        View.OnClickListener msgPopClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgEditPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.iv_share_pop:
                        showShareMenu();
                        break;
                    case R.id.iv_gift_pop:
                        showGiftView();
                        break;
                    case R.id.iv_recharge_pop:
                        showRechargeDialog();
                        break;
                }
            }
        };
        iv_share_pop.setOnClickListener(msgPopClickListener);
        iv_gift_pop.setOnClickListener(msgPopClickListener);
        iv_recharge_pop.setOnClickListener(msgPopClickListener);
        et_msg_pop.setOnEditorActionListener(this);
        msgEditPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                LogUtil.d("dismiss--");
                SDViewUtil.hideInputMethod(et_msg_pop);
            }
        });
        popupContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    msgEditPopupWindow.dismiss();
                    msgEditPopupWindow = null;
                    return true;
                }
                return false;
            }
        });
    }

}
