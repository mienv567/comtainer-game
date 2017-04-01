package com.fanwe.live.activity.room;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.fanwe.auction.appview.AuctionUserRanklistView;
import com.fanwe.auction.appview.room.RoomAuctionInfoView;
import com.fanwe.auction.dialog.AuctionResultsDialog;
import com.fanwe.auction.dialog.room.AuctionSucPaySingleton;
import com.fanwe.auction.model.App_pai_user_get_videoActModel;
import com.fanwe.auction.model.PaiBuyerModel;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionFail;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.umeng.UmengSocialManager;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.listener.SDVisibilityStateListener;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.appview.room.RoomBottomView;
import com.fanwe.live.appview.room.RoomGameMsgView;
import com.fanwe.live.appview.room.RoomGiftGifView;
import com.fanwe.live.appview.room.RoomGiftPlayView;
import com.fanwe.live.appview.room.RoomHeartView;
import com.fanwe.live.appview.room.RoomHeartViewWithCount;
import com.fanwe.live.appview.room.RoomInfoView;
import com.fanwe.live.appview.room.RoomLRSReminderView;
import com.fanwe.live.appview.room.RoomLRSUserView;
import com.fanwe.live.appview.room.RoomMsgView;
import com.fanwe.live.appview.room.RoomPopMsgView;
import com.fanwe.live.appview.room.RoomPrivateRemoveViewerView;
import com.fanwe.live.appview.room.RoomSendMsgView;
import com.fanwe.live.appview.room.RoomStarView;
import com.fanwe.live.appview.room.RoomTipsMsgView;
import com.fanwe.live.appview.room.RoomViewerJoinRoomView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveAddViewerDialog;
import com.fanwe.live.dialog.LiveBottomGameDialog;
import com.fanwe.live.dialog.LiveChatC2CDialog;
import com.fanwe.live.dialog.LiveRedEnvelopeNewDialog;
import com.fanwe.live.event.ERefreshGiftList;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_shareActModel;
import com.fanwe.live.model.LRSGameModel;
import com.fanwe.live.model.RoomResourceModel;
import com.fanwe.live.model.RoomShareModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgRedEnvelope;
import com.fanwe.live.model.custommsg.CustomMsgShare;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.view.CircleProgressBar;
import com.fanwe.live.view.HeartLayout;
import com.fanwe.live.view.SDVerticalScollView;
import com.fanwe.shopping.event.EShoppingCartDialogShowing;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 直播间观众和主播公共部分的界面逻辑实现
 * <p/>
 * Created by Administrator on 2016/8/4.
 */
public class LiveLayoutActivity extends LiveActivity {
    protected View rootView;

    protected RoomInfoView roomInfoView;
    protected RoomLRSUserView roomLRSUserView;
    protected RoomLRSReminderView roomLRSReminderView;
    protected RoomStarView roomStarView;
    protected RoomGiftPlayView roomGiftPlayView;
    protected RoomPopMsgView roomPopMsgView;
    protected RoomTipsMsgView roomTipsMsgView;
    protected RoomViewerJoinRoomView roomViewerJoinRoomView;
    protected RoomMsgView roomMsgView;
    protected RoomGameMsgView roomGameMsgView;
    protected RoomGameMsgView roomGameWolfMsgView;
    protected RoomGameMsgView roomGameProgressMsgView;
    protected RoomGameMsgView roomGameRuleMsgView;
    protected RoomSendMsgView roomSendMsgView;
    protected RoomHeartView roomHeartView;
    protected RoomHeartViewWithCount roomHeartViewWithNumber; // 带数字的点亮的飘屏
    protected RoomGiftGifView roomGiftGifView;
    protected RoomPrivateRemoveViewerView roomPrivateRemoveViewerView;
    protected AuctionUserRanklistView auctionUserRanklistView;//付款排行榜
    protected RoomAuctionInfoView roomAuctionInfoView;//竞拍信息

    private RelativeLayout rl_root_layout; // 根部局
    //    private GLRootView av_video_glview;//视频布局
    private ViewGroup fl_live_room_info; // 房间信息
    private ViewGroup fl_live_lrs_user_info;//狼人杀用户信息
    private ViewGroup fl_live_lrs_reminder;//狼人杀进度提示
    private ViewGroup fl_live_room_star; // 星星展示区
    private ViewGroup fl_live_gift_play; // 礼物播放
    private ViewGroup fl_live_pop_msg; // 弹幕
    private ViewGroup fl_live_tips_msg; // 全屏弹幕
    private ViewGroup fl_live_viewer_join_room; // 加入提示
    private ViewGroup fl_live_msg; // 聊天列表
    private ViewGroup fl_live_msg_game; // 聊天列表 - 游戏公频
    private ViewGroup fl_live_msg_game_wolf; // 聊天列表 - 狼人杀频道
    private ViewGroup fl_live_msg_game_progress; // 聊天列表 - 狼人杀进程
    private ViewGroup fl_live_msg_game_rule; // 聊天列表 - 狼人杀规则
    private ViewGroup fl_live_send_msg; // 发送消息
    protected ViewGroup fl_live_bottom_menu; // 底部菜单

    protected CircleProgressBar circleProgressBar; // 连击点亮进度计数器
    private ViewGroup fl_live_heart; // 点亮
    private ViewGroup fl_live_heart_right; // 带数字的点亮
    private ViewGroup fl_live_gift_gif; // gif
    protected ViewGroup fl_live_auction_info;//竞拍信息
    protected ViewGroup fl_live_auction_rank_list;//付款排行榜


    private App_get_videoActModel roomInfoActModel;

    private AuctionResultsDialog dialogResult;
    public SDVerticalScollView scollView;
    private ViewGroup rl_bottom_close;

    // 七牛sdk相关
    protected GLSurfaceView qn_video_glview; // 七牛视频预览
    protected AspectFrameLayout afl;
    protected boolean mIsActivityPaused = true;

    public void setUserEventListerner(IConferenceUserEventListerner userEventListerner) {
        this.userEventListerner = userEventListerner;
    }

    protected IConferenceUserEventListerner userEventListerner = null;

    public interface IConferenceUserEventListerner {

        void onUserJoinConference(String remoteUserId);


        void onUserLeaveConference(String remoteUserId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AuctionSucPaySingleton.getInstance().onDestoryHandler();
    }

    protected void destroyIM() {
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

    }

    @Override
    protected void baseInit() {
        super.baseInit();
    }


    protected PopupWindow mPopupWindow;

    @Override
    protected void onKeyboardVisibilityChange(boolean visible, int height) {
        if (rl_root_layout != null) {
            if (visible) {
                rl_root_layout.scrollBy(0, height);
            } else {
                rl_root_layout.scrollTo(0, 0);
                rl_root_layout.requestLayout();
                roomSendMsgView.invisible();
            }
        }
        super.onKeyboardVisibilityChange(visible, height);
    }

    protected void initLayout(View view) {
        rootView = view;
//        av_video_glview = (GLRootView) view.findViewById(av_video_glview);
        rl_root_layout = (RelativeLayout) view.findViewById(R.id.rl_root_layout);
        fl_live_room_info = (ViewGroup) view.findViewById(R.id.fl_live_room_info);
        fl_live_lrs_user_info = (ViewGroup) view.findViewById(R.id.fl_live_lrs_user_info);
        fl_live_lrs_reminder = (ViewGroup) view.findViewById(R.id.fl_live_lrs_reminder);
        fl_live_room_star = (ViewGroup) view.findViewById(R.id.fl_live_room_star);
        fl_live_gift_play = (ViewGroup) view.findViewById(R.id.fl_live_gift_play);
        fl_live_pop_msg = (ViewGroup) view.findViewById(R.id.fl_live_pop_msg);
        fl_live_tips_msg = (ViewGroup) view.findViewById(R.id.fl_live_tips_msg);
        fl_live_viewer_join_room = (ViewGroup) view.findViewById(R.id.fl_live_viewer_join_room);
        fl_live_msg = (ViewGroup) view.findViewById(R.id.fl_live_msg);
        fl_live_msg_game = (ViewGroup) view.findViewById(R.id.fl_live_msg_game);
        fl_live_msg_game_wolf = (ViewGroup) view.findViewById(R.id.fl_live_msg_game_wolf);
        fl_live_msg_game_progress = (ViewGroup) view.findViewById(R.id.fl_live_msg_game_progress);
        fl_live_msg_game_rule = (ViewGroup) view.findViewById(R.id.fl_live_msg_game_rule);
        fl_live_send_msg = (ViewGroup) view.findViewById(R.id.fl_live_send_msg);
        fl_live_bottom_menu = (ViewGroup) view.findViewById(R.id.fl_live_bottom_menu);
        fl_live_heart = (ViewGroup) view.findViewById(R.id.fl_live_heart);
        fl_live_heart_right = (ViewGroup) view.findViewById(R.id.fl_live_heart_right); // 点亮飘出带数字的点亮图标
        circleProgressBar = (CircleProgressBar) view.findViewById(R.id.cpb);
        fl_live_gift_gif = (ViewGroup) view.findViewById(R.id.fl_live_gift_gif);
        fl_live_auction_info = (ViewGroup) view.findViewById(R.id.fl_live_auction_info);
        fl_live_auction_rank_list = (ViewGroup) view.findViewById(R.id.fl_live_auction_rank_list);
        // 房间信息
        roomInfoView = new RoomInfoView(this);
        roomInfoView.setClickListener(roomInfoClickListener);
        roomInfoView.setCloseListener(roomInfoCloseListener);
        fl_live_msg_game = (ViewGroup) view.findViewById(R.id.fl_live_msg_game);
        fl_live_msg_game_wolf = (ViewGroup) view.findViewById(R.id.fl_live_msg_game_wolf);
        fl_live_msg_game_progress = (ViewGroup) view.findViewById(R.id.fl_live_msg_game_progress);
        fl_live_msg_game_rule = (ViewGroup) view.findViewById(R.id.fl_live_msg_game_rule);
        addLiveRoomInfo();
        //狼人杀用户信息
        SDViewUtil.setViewHeight(fl_live_lrs_user_info, SDViewUtil.getScreenHeightPercent(0.7f));
        roomLRSUserView = new RoomLRSUserView(this);
        SDViewUtil.setViewWidth(fl_live_lrs_user_info, SDViewUtil.dp2px(220));
        addLiveRoomLRSUserInfo();
        //狼人杀提示信息
        roomLRSReminderView = new RoomLRSReminderView(this);
        addLiveRoomLRSReminder();

        // 星星
        roomStarView = new RoomStarView(this);
        addLiveRoomStar();

        //礼物播放
        roomGiftPlayView = new RoomGiftPlayView(this);
        addLiveGiftPlay();

        //弹幕
        roomPopMsgView = new RoomPopMsgView(this);
        addLivePopMsg();

        // 全屏弹幕
        roomTipsMsgView = new RoomTipsMsgView(this);
        addLiveTipMsg();

        //加入房间提示
        roomViewerJoinRoomView = new RoomViewerJoinRoomView(this);
        addLiveViewerJoinRoom();

        //消息列表
        SDViewUtil.setViewHeight(fl_live_msg, SDViewUtil.getScreenHeightPercent(0.3f));
        SDViewUtil.setViewWidth(fl_live_msg, SDViewUtil.getScreenWidthPercent(0.7f));
        roomMsgView = new RoomMsgView(this);
        addLiveMsg();

        //消息列表 - 游戏公频
        SDViewUtil.setViewHeight(fl_live_msg_game, SDViewUtil.getScreenHeightPercent(0.3f));
        SDViewUtil.setViewWidth(fl_live_msg_game, SDViewUtil.getScreenWidthPercent(0.7f));
        roomGameMsgView = new RoomGameMsgView(this);
        addLiveGameMsg();

        //消息列表 - 游戏狼人频道
        SDViewUtil.setViewHeight(fl_live_msg_game_wolf, SDViewUtil.getScreenHeightPercent(0.3f));
        SDViewUtil.setViewWidth(fl_live_msg_game_wolf, SDViewUtil.getScreenWidthPercent(0.7f));
        roomGameWolfMsgView = new RoomGameMsgView(this);
        addLiveGameWolfMsg();

        //消息列表 - 狼人杀进程
        SDViewUtil.setViewHeight(fl_live_msg_game_progress, SDViewUtil.getScreenHeightPercent(0.3f));
        SDViewUtil.setViewWidth(fl_live_msg_game_progress, SDViewUtil.getScreenWidthPercent(0.7f));
        roomGameProgressMsgView = new RoomGameMsgView(this);
        addLiveGameProgressMsg();

        //消息列表 - 狼人杀规则
        SDViewUtil.setViewHeight(fl_live_msg_game_rule, SDViewUtil.getScreenHeightPercent(0.3f));
        SDViewUtil.setViewWidth(fl_live_msg_game_rule, SDViewUtil.getScreenWidthPercent(0.7f));
        roomGameRuleMsgView = new RoomGameMsgView(this);
        addLiveGameRuleMsg();

        //发消息
        roomSendMsgView = new RoomSendMsgView(this);
        roomSendMsgView.setVisibilityStateListener(new SDVisibilityStateListener() {
            @Override
            public void onVisible(View view) {
                if (getBottomView() != null) {
                    getBottomView().invisible(true);
                }
            }

            @Override
            public void onGone(View view) {
                if (getBottomView() != null) {
                    getBottomView().show(true);
                }
            }

            @Override
            public void onInvisible(View view) {
                if (getBottomView() != null) {
                    getBottomView().show(true);
                }
            }
        });
        addLiveSendMsg();

        //底部菜单
        addLiveBottomMenu();

        //点亮
        SDViewUtil.setViewWidth(fl_live_heart, SDResourcesUtil.getDimensionPixelSize(R.dimen.width_live_bottom_menu) * 3);
        SDViewUtil.setViewHeight(fl_live_heart, SDViewUtil.getScreenWidthPercent(1f));
        roomHeartView = new RoomHeartView(this);
        addLiveHeart();

        // 带数字的点亮图标的显示区域设置
        SDViewUtil.setViewWidth(fl_live_heart_right, SDResourcesUtil.getDimensionPixelSize(R.dimen.width_live_bottom_menu) * 3);
        SDViewUtil.setViewHeight(fl_live_heart_right, SDViewUtil.getScreenWidthPercent(1f));
        roomHeartViewWithNumber = new RoomHeartViewWithCount(this);
        addLiveHeartWithNumber();

        //gif动画
        roomGiftGifView = new RoomGiftGifView(this);
        addLiveGiftGif();
    }


    public RelativeLayout getLiveRootLayout() {
        return rl_root_layout;
    }

    /**
     * 房间信息
     */
    protected void addLiveRoomInfo() {
        replaceView(fl_live_room_info, roomInfoView);
    }

    /**
     * 狼人杀用户信息
     */
    protected void addLiveRoomLRSUserInfo() {
        replaceView(fl_live_lrs_user_info, roomLRSUserView);
        SDViewUtil.hide(roomLRSUserView);
    }

    protected void addLiveRoomLRSReminder() {
        replaceView(fl_live_lrs_reminder, roomLRSReminderView);
    }

    /**
     * 房间信息
     */
    protected void addLiveRoomStar() {
        replaceView(fl_live_room_star, roomStarView);
    }


    /**
     * 礼物播放
     */
    protected void addLiveGiftPlay() {
        replaceView(fl_live_gift_play, roomGiftPlayView);
    }

    /**
     * 弹幕
     */
    protected void addLivePopMsg() {
        replaceView(fl_live_pop_msg, roomPopMsgView);
    }

    /**
     * 全屏弹幕消息
     */
    protected void addLiveTipMsg() {
        replaceView(fl_live_tips_msg, roomTipsMsgView);
    }

    /**
     * 进入提示
     */
    protected void addLiveViewerJoinRoom() {
        replaceView(fl_live_viewer_join_room, roomViewerJoinRoomView);
    }

    /**
     * 聊天列表
     */
    protected void addLiveMsg() {
        replaceView(fl_live_msg, roomMsgView);
    }

    /**
     * 聊天列表 - 游戏公频
     */
    protected void addLiveGameMsg() {
        replaceView(fl_live_msg_game, roomGameMsgView);
        SDViewUtil.hide(roomGameMsgView);
    }

    /**
     * 聊天列表 - 游戏狼人频道
     */
    protected void addLiveGameWolfMsg() {
        replaceView(fl_live_msg_game_wolf, roomGameWolfMsgView);
        SDViewUtil.hide(roomGameWolfMsgView);
    }

    /**
     * 聊天列表 - 游戏进程
     */
    protected void addLiveGameProgressMsg() {
        replaceView(fl_live_msg_game_progress, roomGameProgressMsgView);
        SDViewUtil.hide(roomGameProgressMsgView);
    }

    /**
     * 聊天列表 - 游戏规则
     */
    protected void addLiveGameRuleMsg() {
        replaceView(fl_live_msg_game_rule, roomGameRuleMsgView);
        SDViewUtil.hide(roomGameRuleMsgView);
    }

    /**
     * 发送消息
     */
    protected void addLiveSendMsg() {
        replaceView(fl_live_send_msg, roomSendMsgView);
    }

    /**
     * 底部菜单
     */
    protected void addLiveBottomMenu() {
        //子类实现
    }

    /**
     * 点亮
     */
    protected void addLiveHeart() {
        if (null != roomHeartView) {
            replaceView(fl_live_heart, roomHeartView);
        } else {
            throw new RuntimeException("请先初始化活动房间");
        }

    }


    /**
     * 点亮
     */
    protected void addLiveHeartWithNumber() {
        if (null != roomHeartViewWithNumber) {
            replaceView(fl_live_heart_right, roomHeartViewWithNumber);
        } else {
            throw new RuntimeException("请先初始化活动房间");
        }
    }

    /**
     * gif动画
     */
    protected void addLiveGiftGif() {
        replaceView(fl_live_gift_gif, roomGiftGifView);
    }

    /**
     * 私密直播踢人
     */
    protected void addLivePrivateRemoveViewer() {
        removeView(roomPrivateRemoveViewerView);
        roomPrivateRemoveViewerView = new RoomPrivateRemoveViewerView(this);
        addView(roomPrivateRemoveViewerView);
    }

    /**
     * 结束界面
     */
    protected void addLiveFinish() {
        // 子类实现
    }

    /**
     * 房间信息view点击监听
     */
    private RoomInfoView.ClickListener roomInfoClickListener = new RoomInfoView.ClickListener() {
        @Override
        public void onClickAddViewer(View v) {
            LiveLayoutActivity.this.onClickAddViewer(v);
        }

        @Override
        public void onClickMinusViewer(View v) {
            LiveLayoutActivity.this.onClickMinusViewer(v);
        }
    };

    /**
     * 房间信息view退出监听
     */
    private RoomInfoView.CloseListener roomInfoCloseListener = new RoomInfoView.CloseListener() {

        @Override
        public void onClickCloseViewer(View v) {
            LiveLayoutActivity.this.onClickBottomClose(v);
        }
    };

    /**
     * 私密直播点击加号加人
     *
     * @param v
     */
    protected void onClickAddViewer(View v) {
        LiveAddViewerDialog dialog = new LiveAddViewerDialog(this, getRoomInfo().getPrivate_share());
        dialog.showBottom();
    }

    /**
     * 私密直播点击减号踢人
     *
     * @param v
     */
    protected void onClickMinusViewer(View v) {
        addLivePrivateRemoveViewer();
    }

    @Override
    public App_get_videoActModel getRoomInfo() {
        return roomInfoActModel;
    }


    protected SDRequestHandler requestRoomInfo() {
        //子类实现
        return null;
    }

    public void setRoomInfo(App_get_videoActModel actModel) {
        this.roomInfoActModel = actModel;

        if (TextUtils.isEmpty(getCreaterId())) {
            setCreaterId(actModel.getPodcast().getUserId());
        }
        if (TextUtils.isEmpty(getGroupId())) {
            setGroupId(actModel.getGroupId());
        }
        setTopicId(actModel.getCate_id());
        // 活动房间的点亮图标跟活动类型有关联,需要在获知房间类型后,再根据服务端关于房间类型点亮图标的设定来设置点亮图标
        RoomResourceModel roomResourceModel = actModel.getResource();
        if (null != roomResourceModel) {
            final List<String> list = roomResourceModel.getFloat_pic_url();
            if (null != list && list.size() > 0) {
                final Map<String, Drawable> map = new LinkedHashMap<>();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DisplayImageOptions options = new DisplayImageOptions.Builder()
                                    .cacheOnDisk(true).build();
                            for (int i = 0; i < list.size(); i++) {
//                                FutureTarget<File> future = Glide.with(LiveLayoutActivity.this)
//                                        .load(list.get(i))
//                                        .downloadOnly(70,70);
//
//                                File cacheFile = future.get();
                                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(list.get(i), options);
                                Drawable drawable = new BitmapDrawable(bitmap);
//                                Drawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(cacheFile.getAbsolutePath()));
                                map.put("heart" + i, drawable);
                                roomHeartView.getViewHeart().mapNameUrl.put("heart" + i, list.get(i));
                            }
                            roomHeartView.getViewHeart().init(map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }).start();
            } else {
                HeartLayout.mapNameUrl.clear();
                roomHeartView.getViewHeart().init(null);
            }
        }
         // TODO:  json解析完成之后再重新绑定内容
        this.roomInfoView.bindData(actModel);
//        if (actModel.getWolf() != null) {
//            dealWolfKillGameInfo(actModel.getWolf());
//        }
    }

    protected void onErrorRequestRoomInfo(App_get_videoActModel actModel) {

    }

    protected void onSuccessRequestRoomInfo(App_get_videoActModel actModel) {

    }

    /**
     * 处理进入房间的时候 正在进行狼人杀游戏
     */
    protected void dealWolfKillGameInfo(LRSGameModel model) {

    }

    /**
     * 点击关闭
     *
     * @param v
     */
    protected void onClickBottomClose(View v) {
        //子类实现
    }

    /**
     * 点击打开发送窗口
     *
     * @param v
     */
    protected void onClickBottomOpenSend(View v) {
        roomSendMsgView.show();
        MobclickAgent.onEvent(LiveLayoutActivity.this, "live_send_message");
    }

    @Override
    public void openSendMsg(String content) {
        super.openSendMsg(content);
        roomSendMsgView.show();
        roomSendMsgView.setContent(content);
    }

    protected RoomBottomView getBottomView() {
        //子类实现
        return null;
    }

    /**
     * 点击狼人杀游戏
     *
     * @param v
     */
    protected void onClickBottomLrs(View v) {
        LiveBottomGameDialog mDialog = new LiveBottomGameDialog(this, getRoomId());
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.showBottom();
    }

    /**
     * 点击私聊消息
     *
     * @param v
     */
    protected void onClickBottomMsg(View v) {
        LiveChatC2CDialog dialog = new LiveChatC2CDialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.showBottom();
        MobclickAgent.onEvent(LiveLayoutActivity.this, "live_message");
    }

    /**
     * 打开分享面板
     */
    @Override
    public void openShare(final UMShareListener listener) {
        if (roomInfoActModel == null) {
            return;
        }
        final RoomShareModel shareModel = roomInfoActModel.getShare();
        if (shareModel == null) {
            return;
        }
//        UmengSocialManager.openShare(shareModel.getShareTitle(), shareModel.getShareContent(), shareModel.getShareImageUrl(),
//                shareModel.getShareUrl(), this, new UMShareListener() {
//
//                    @Override
//                    public void onResult(SHARE_MEDIA media) {
//                        String shareKey = shareModel.getShareKey();
//                        CommonInterface.requestShareComplete(media.toString(), shareKey, new AppRequestCallback<App_shareActModel>() {
//
//                            @Override
//                            protected void onSuccess(SDResponse sdResponse) {
//                                EventBus.getDefault().post(new ERefreshGiftList());
//                            }
//                        });
//
//                        if (listener != null) {
//                            listener.onResult(media);
//                        }
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA media, Throwable throwable) {
//                        if (listener != null) {
//                            listener.onError(media, throwable);
//                        }
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA media) {
//                        if (listener != null) {
//                            listener.onCancel(media);
//                        }
//                    }
//                });

        final UMShareListener umShareListener = new UMShareListener() {

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
        };

        View popupContentView = View.inflate(this, R.layout.view_live_share, null);
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
                mPopupWindow.dismiss();
            }
        };

        popupContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
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

        mPopupWindow = new PopupWindow(popupContentView, ViewGroup.LayoutParams.MATCH_PARENT, SDViewUtil.dp2px(80), true);
        mPopupWindow.setAnimationStyle(R.style.path_popwindow_anim_enterorout_window);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        MobclickAgent.onEvent(LiveLayoutActivity.this, "live_share");
    }

    // 新增自定义面板的分享
    public void openShare(int shareMedia, final UMShareListener listener) {
        if (roomInfoActModel == null) {
            return;
        }
        final RoomShareModel shareModel = roomInfoActModel.getShare();
        if (shareModel == null) {
            return;
        }
        UmengSocialManager.openShare(shareMedia, shareModel.getShareTitle(), shareModel.getShareContent(), shareModel.getShareImageUrl(),
                shareModel.getShareUrl(), this, new UMShareListener() {

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
        MobclickAgent.onEvent(LiveLayoutActivity.this, "live_share");
    }

    /**
     * 发送到友盟统计
     */
    private void sendToUMengAnalytics(SHARE_MEDIA media, UserModel user) {
        if (user != null) {
            UMPlatformData platform = new UMPlatformData(convertShareEnumToAnalyticsEnum(media), user.getUserId());
            if (user.getSex() == 1) {
                platform.setGender(UMPlatformData.GENDER.MALE);
            } else {
                platform.setGender(UMPlatformData.GENDER.FEMALE);
            }
            MobclickAgent.onSocialEvent(this, platform);
        }
    }

    /**
     * 将友盟分享的枚举转换成友盟统计的枚举
     *
     * @param share_media
     * @return
     */
    private UMPlatformData.UMedia convertShareEnumToAnalyticsEnum(SHARE_MEDIA share_media) {
        UMPlatformData.UMedia result = UMPlatformData.UMedia.WEIXIN_FRIENDS;
        String shareMedia = share_media.toString();
        if ("QQ".equals(shareMedia)) {
            result = UMPlatformData.UMedia.TENCENT_QQ;
        } else if ("QZONE".equals(shareMedia)) {
            result = UMPlatformData.UMedia.TENCENT_QZONE;
        } else if ("WEIXIN".equals(shareMedia)) {
            result = UMPlatformData.UMedia.WEIXIN_FRIENDS;
        } else if ("WEIXIN_CIRCLE".equals(shareMedia)) {
            result = UMPlatformData.UMedia.WEIXIN_CIRCLE;
        } else if ("SINA".equals(shareMedia)) {
            result = UMPlatformData.UMedia.SINA_WEIBO;
        }
        return result;
    }

    /**
     * 转换分享平台的名称
     *
     * @param share_media
     * @return
     */
    public static String convertEnumToString(SHARE_MEDIA share_media) {
        String result = "";
        String shareMedia = share_media.toString();
        if ("QQ".equals(shareMedia)) {
            result = shareMedia;
        } else if ("QZONE".equals(shareMedia)) {
            result = App.getApplication().getString(R.string.qzone_name);
        } else if ("WEIXIN".equals(shareMedia)) {
            result = App.getApplication().getString(R.string.wexin_name);
        } else if ("WEIXIN_CIRCLE".equals(shareMedia)) {
            result = App.getApplication().getString(R.string.wexin_cricle_name);
        } else if ("SINA".equals(shareMedia)) {
            result = App.getApplication().getString(R.string.sina_name);
        }
        return result;
    }

    /**
     * 发送分享成功消息
     *
     * @param listener
     */
    protected void sendShareSuccessMsg(final SHARE_MEDIA share_media, final TIMValueCallBack<TIMMessage> listener) {
        UserModel userModel = UserModelDao.query();
        if (userModel != null) {
            final CustomMsgShare msg = new CustomMsgShare();
            msg.setDesc(" " +getString(R.string.share_live_to)+ convertEnumToString(share_media));
            IMHelper.sendMsgGroup(getGroupId(), msg, new TIMValueCallBack<TIMMessage>() {
                @Override
                public void onError(int i, String s) {
                    if (listener != null) {
                        listener.onError(i, s);
                    }
                }

                @Override
                public void onSuccess(TIMMessage timMessage) {
                    IMHelper.postMsgLocal(msg, getGroupId());
                    if (listener != null) {
                        listener.onSuccess(timMessage);
                    }
                }
            });
//            sendToUMengAnalytics(share_media,userModel);
        }
    }


    @Override
    public boolean isPrivate() {
        if (getRoomInfo() != null) {
            return getRoomInfo().getIs_private() == 1;
        } else {
            return false;
        }
    }

    @Override
    protected void onMsgRedEnvelope(MsgModel msg) {
        super.onMsgRedEnvelope(msg);
        CustomMsgRedEnvelope customMsgRedEnvelope = msg.getCustomMsgRedEnvelope();

        LiveRedEnvelopeNewDialog dialog = new LiveRedEnvelopeNewDialog(this, customMsgRedEnvelope);
        dialog.showCenter();
    }

    @Override
    protected void onMsgAuction(MsgModel msg) {
        super.onMsgAuction(msg);
        switch (msg.getCustomMsgType()) {
            case LiveConstant.CustomMsgType.MSG_AUCTION_SUCCESS:
                //竞拍成功
                msgAuctionSuccess(msg.getCustomMsgAuctionSuccess());
                break;
            case LiveConstant.CustomMsgType.MSG_AUCTION_NOTIFY_PAY:
                //竞拍通知付款，比如第一名超时未付款，通知下一名付款
                msgAuctionNotifyPay(msg.getCustomMsgAuctionNotifyPay());
                break;
            case LiveConstant.CustomMsgType.MSG_AUCTION_FAIL:
                // 流拍
                msgAuctionFail(msg.getCustomMsgAuctionFail());
                break;
            case LiveConstant.CustomMsgType.MSG_AUCTION_OFFER:
                //推送出价信息
                msgAuctionOffer(msg.getCustomMsgAuctionOffer());
                break;
            case LiveConstant.CustomMsgType.MSG_AUCTION_PAY_SUCCESS:
                // 支付成功
                msgAuctionPaySuccess(msg.getCustomMsgAuctionPaySuccess());
                break;
            case LiveConstant.CustomMsgType.MSG_AUCTION_CREATE_SUCCESS:
                //主播发起竞拍成功
                msgAuctionCreateSuccess(msg.getCustomMsgAuctionCreateSuccess());
                break;
        }
    }

    protected void msgAuctionSuccess(CustomMsgAuctionSuccess customMsgAuctionSuccess) {
        //竞拍成功
        if (dialogResult != null)
            dialogResult.dismiss();

        dialogResult = new AuctionResultsDialog(this, customMsgAuctionSuccess);
        dialogResult.showCenter();

        addLiveAuctionRankList(customMsgAuctionSuccess.getBuyer());

        AuctionSucPaySingleton.getInstance().newInstanceAuctionSucPayDialog(this, customMsgAuctionSuccess.getBuyer());
    }

    protected void msgAuctionNotifyPay(CustomMsgAuctionNotifyPay customMsgAuctionNotifyPay) {
        //竞拍通知付款，比如第一名超时未付款，通知下一名付款

        List<PaiBuyerModel> listBuyers = customMsgAuctionNotifyPay.getBuyer();
        if (listBuyers == null || listBuyers.size() == 0) {
            return;
        }
        addLiveAuctionRankList(listBuyers);

        if (dialogResult != null)
            dialogResult.dismiss();

        dialogResult = new AuctionResultsDialog(this, customMsgAuctionNotifyPay);
        dialogResult.showCenter();

        AuctionSucPaySingleton.getInstance().newInstanceAuctionSucPayDialog(this, customMsgAuctionNotifyPay.getBuyer());
    }

    protected void msgAuctionFail(CustomMsgAuctionFail customMsgAuctionFail) {
        // 流拍
        if (dialogResult != null)
            dialogResult.dismiss();

        dialogResult = new AuctionResultsDialog(this, customMsgAuctionFail);
        dialogResult.showCenter();

        SDViewUtil.removeViewFromParent(roomAuctionInfoView);
        SDViewUtil.removeViewFromParent(auctionUserRanklistView);

        AuctionSucPaySingleton.getInstance().newInstanceAuctionSucPayDialog(this, customMsgAuctionFail.getBuyer());
    }

    protected void msgAuctionOffer(CustomMsgAuctionOffer customMsgAuctionOffer) {
        //推送出价信息
    }

    protected void msgAuctionPaySuccess(CustomMsgAuctionPaySuccess customMsgAuctionPaySuccess) {
        // 支付成功
        if (dialogResult != null)
            dialogResult.dismiss();

        dialogResult = new AuctionResultsDialog(this, customMsgAuctionPaySuccess);
        dialogResult.showCenter();

        SDViewUtil.removeViewFromParent(roomAuctionInfoView);
        SDViewUtil.removeViewFromParent(auctionUserRanklistView);

        AuctionSucPaySingleton.getInstance().newInstanceAuctionSucPayDialog(this, customMsgAuctionPaySuccess.getBuyer());
    }

    protected void msgAuctionCreateSuccess(CustomMsgAuctionCreateSuccess customMsgAuctionCreateSuccess) {
        //主播发起竞拍成功
    }

    //添加竞拍信息
    protected void addLiveAuctionInfo(App_pai_user_get_videoActModel app_pai_user_get_videoActModel) {
        roomAuctionInfoView = new RoomAuctionInfoView(this);
        replaceView(fl_live_auction_info, roomAuctionInfoView);
        roomAuctionInfoView.bindAuctionDetailInfo(app_pai_user_get_videoActModel);
    }

    //添加付款排行榜
    protected void addLiveAuctionRankList(List<PaiBuyerModel> listBuyers) {
        auctionUserRanklistView = new AuctionUserRanklistView(this);
        replaceView(fl_live_auction_rank_list, auctionUserRanklistView);
        auctionUserRanklistView.setBuyers(listBuyers);
    }

    protected void changeAvVideoGlviewLayout() {
        SDViewUtil.hide(roomInfoView);

//        // 置换av_video_glview成qn_video_glview前
//        SDViewUtil.setViewHeight(av_video_glview, SDViewUtil.getScreenHeight() / 3);
//        SDViewUtil.setViewWidth(av_video_glview, SDViewUtil.getScreenWidth() / 3);

        // 置换av_video_glview成qn_video_glview后
        SDViewUtil.setViewHeight(qn_video_glview, SDViewUtil.getScreenHeight() / 3);
        SDViewUtil.setViewWidth(qn_video_glview, SDViewUtil.getScreenWidth() / 3);
    }

    protected void revertAvVideoGlviewLayout() {
        SDViewUtil.show(roomInfoView);
//
//        //        // 置换av_video_glview成qn_video_glview前
//        SDViewUtil.setViewHeight(av_video_glview, SDViewUtil.getScreenHeight());
//        SDViewUtil.setViewWidth(av_video_glview, SDViewUtil.getScreenWidth());

        // 置换av_video_glview成qn_video_glview后
        SDViewUtil.setViewHeight(qn_video_glview, SDViewUtil.getScreenHeight());
        SDViewUtil.setViewWidth(qn_video_glview, SDViewUtil.getScreenWidth());
    }

    //实体产品列表弹出事件
    public void onEventMainThread(EShoppingCartDialogShowing event) {
        if (event.isShowing) {
            changeAvVideoGlviewLayout();
        } else {
            revertAvVideoGlviewLayout();
        }
    }

}
