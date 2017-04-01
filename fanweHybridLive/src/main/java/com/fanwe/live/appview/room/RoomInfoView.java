package com.fanwe.live.appview.room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.listener.SDIterateListener;
import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDAnimationUtil;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveContActivity;
import com.fanwe.live.activity.room.FightingShowActivity;
import com.fanwe.live.adapter.LiveViewerListRecyclerAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveHotProgressDialog;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.event.EExitRoomComplete;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ELRSHunterOperate;
import com.fanwe.live.event.ELRSWitchOperate;
import com.fanwe.live.event.ERequestFollowSuccess;
import com.fanwe.live.event.ERequestHotSuccess;
import com.fanwe.live.fragment.LiveContLocalFragment;
import com.fanwe.live.fragment.LiveContTotalFragment;
import com.fanwe.live.model.AppRoomHotScoreMonitorActModel;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_monitorActModel;
import com.fanwe.live.model.App_viewerActModel;
import com.fanwe.live.model.LRSUserModel;
import com.fanwe.live.model.RoomResourceModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.fanwe.live.model.custommsg.CustomMsgLRS;
import com.fanwe.live.model.custommsg.CustomMsgOnlineNumber;
import com.fanwe.live.model.custommsg.CustomMsgPopMsg;
import com.fanwe.live.model.custommsg.CustomMsgRedEnvelope;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.CustomMsgViewerQuit;
import com.fanwe.live.utils.LiveUtils;
import com.sunday.eventbus.SDEventManager;
import com.umeng.analytics.MobclickAgent;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 直播间顶部view
 */
public class RoomInfoView extends RoomView {
    public interface CloseListener {
        void onClickCloseViewer(View v);
    }

    public void setCloseListener(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    private CloseListener closeListener;


    public RoomInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomInfoView(Context context) {
        super(context);
    }

    /**
     * 默认事件拦截间隔
     */
    private static final long DURATION_INTERCEPT = 100;

    private View ll_click_creater;

    public ImageView getIv_head_image() {
        return iv_head_image;
    }

    private ImageView iv_head_image;
    private ImageView iv_level;
    protected TextView tv_video_type;
    private TextView tv_viewer_number;
    private View view_operate_viewer;
    private View view_add_viewer;
    private View view_minus_viewer;
    private SDRecyclerView hlv_viewer;
    private LinearLayout ll_user_number;
    private LinearLayout ll_ticket;
    private RelativeLayout rl_fighting; // 活动房间战斗值的容器
    private TextView tv_ticket;
    private TextView tv_socre_name;//热度名称
    private TextView tv_user_number;

    private LinearLayout ll_follow;
    private TextView tv_creater_leave;
    private LinearLayout ll_ringgirl_logo; //活动的图标的视图
    private ImageView iv_ringgirl_logo; // 活动logo
    private TextView tv_fighting; // 活动房间,战斗力值
    private RelativeLayout rl_game_time; //狼人杀游戏倒计时layout
    private TextView tv_game_time;//狼人杀游戏倒计时
    private TextView tv_game_time_des;//狼人杀倒计时描述
    private RelativeLayout rl_game_time_viewer; //狼人杀游戏倒计时layout
    private TextView tv_game_time_viewer;//狼人杀游戏倒计时
    private TextView tv_game_time_des_viewer;//狼人杀倒计时描述
    private boolean mCanShowGameTime = true; //控制是否可以显示倒计时
    private LiveViewerListRecyclerAdapter adapter;
    private CustomMsgOnlineNumber viewerActModel;
    private int viewerNumber;
    private long ticket;
    private long fighting; // 活动房间战斗力值
    private int hasFollow;
    private TextView tv_creater_time;//主播开播经历时间
    private int page;
    private SDLooper looperMonitor;
    private SDLooper looperViewer;
    private SDLooper mScoreLooperMonitor;
    private SDLooper mCreaterTimeLooper;
    private Random random;

    private String jumpUrl; // 战斗力值排行按钮点击后进入h5页面的url
    private ClickListener clickListener;
    private String headImgUrl;//头像地址
    private RelativeLayout rl_bottom_close;
    private boolean mFinishShow = false; //结束播放了
    private int mTime = 0;
    private static final int REFRESH_VIEWER_DURANTION = 5 * 1000; // 刷新观众间隔时间
    private int mGameTime = 0; //狼人杀游戏显示的倒计时
    private static Handler mGameTimeHandler = new Handler();
    private String mUserId;
    private String currentMissionId;

    /**
     * 房间为非活动房间是隐藏活动logo的显示
     *
     * @param
     */
    public void showActivityLogo(String logoUrl) {
        if (null == logoUrl) {
            ll_ringgirl_logo.setVisibility(View.GONE);
        } else {
            ll_ringgirl_logo.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(logoUrl).into(new SimpleTarget<GlideDrawable>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    ll_ringgirl_logo.setVisibility(View.VISIBLE);
                    iv_ringgirl_logo.setVisibility(View.VISIBLE);
                    iv_ringgirl_logo.setBackground(resource);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    iv_ringgirl_logo.setVisibility(View.GONE);
                }
            });

        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_room_info_ringgirl;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        ll_click_creater = find(R.id.ll_click_creater);
        iv_head_image = find(R.id.iv_head_image);
        iv_level = find(R.id.iv_level);
        tv_video_type = find(R.id.tv_video_type);
        tv_viewer_number = find(R.id.tv_viewer_number);
        view_operate_viewer = find(R.id.view_operate_viewer);
        view_add_viewer = find(R.id.view_add_viewer);
        view_minus_viewer = find(R.id.view_minus_viewer);
        rl_game_time = find(R.id.rl_game_time);
        tv_game_time = find(R.id.tv_game_time);
        tv_game_time_des = find(R.id.tv_game_time_des);
        rl_game_time_viewer = find(R.id.rl_game_time_viewer);
        tv_game_time_viewer = find(R.id.tv_game_time_viewer);
        tv_game_time_des_viewer = find(R.id.tv_game_time_des_viewer);
        hlv_viewer = find(R.id.hlv_viewer);
        ll_ticket = find(R.id.ll_ticket);
        tv_ticket = find(R.id.tv_ticket);
        tv_socre_name = find(R.id.tv_score_name);
        tv_user_number = find(R.id.tv_user_number);
        ll_follow = find(R.id.ll_follow);
        tv_creater_leave = find(R.id.tv_creater_leave);
        ll_ringgirl_logo = find(R.id.ll_ringgirl_logo);
        ll_user_number = find(R.id.ll_user_number);
        rl_fighting = find(R.id.rl_fighting);
        iv_ringgirl_logo = find(R.id.iv_ringgirl_logo);
        tv_fighting = find(R.id.tv_fighting);
        tv_creater_time = find(R.id.tv_creater_time);
        TextPaint tp = tv_creater_time.getPaint();
        tp.setFakeBoldText(true);
        looperMonitor = new SDSimpleLooper();
        looperViewer = new SDSimpleLooper();
        mScoreLooperMonitor = new SDSimpleLooper();
        mCreaterTimeLooper = new SDSimpleLooper();
        random = new Random();
        rl_bottom_close = find(R.id.rl_bottom_close);
        rl_bottom_close.setOnClickListener(this);
        view_add_viewer.setOnClickListener(this);
        view_minus_viewer.setOnClickListener(this);
        ll_follow.setOnClickListener(this);
        ll_ticket.setOnClickListener(this);
        tv_socre_name.setOnClickListener(this);
        ll_click_creater.setOnClickListener(this);
        rl_fighting.setOnClickListener(this);
        hlv_viewer.setLinearHorizontal();
        adapter = new LiveViewerListRecyclerAdapter(getActivity());
        hlv_viewer.setAdapter(adapter);
        initUserId();
        //        if(!getLiveInfo().isCreater()){
        //            SDViewUtil.show(ll_user_number);
        //        }
    }

    private void initUserId() {
        UserModel model = UserModelDao.query();
        if (model != null) {
            mUserId = model.getUserId();
        }
    }

    public void setTextVideoType(String text) {
        tv_video_type.setText(text);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == view_add_viewer) {
            if (clickListener != null) {
                clickListener.onClickAddViewer(v);
            }
        } else if (v == view_minus_viewer) {
            if (clickListener != null) {
                clickListener.onClickMinusViewer(v);
            }
        } else if (v == ll_follow) {
            clickFollow();
            ll_follow.setOnClickListener(null);
            MobclickAgent.onEvent(getActivity(), "live_follow");
        } else if (v == ll_ticket) {
            clickTicket();
            MobclickAgent.onEvent(getActivity(), "live_ticket");
        } else if (v == tv_socre_name) {
            clickScore(getLiveInfo().getRoomId());
            MobclickAgent.onEvent(getActivity(), "live_score");
        } else if (v == ll_click_creater) {
            String id = getLiveInfo().getCreaterId();
            clickHeadImage(id);
        } else if (v == rl_bottom_close) {
            if (closeListener != null) {
                closeListener.onClickCloseViewer(v);
                MobclickAgent.onEvent(getActivity(), "live_close");
            }
        } else if (v == rl_fighting) {
            clickFighting();
        }
    }


    /**
     * 设置主播播放时长
     *
     * @param time
     */
    public void setCreaterTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            SDViewUtil.show(tv_creater_time);
            tv_creater_time.setText(time);
        }
    }

    @Override
    public void setPlayback(boolean playback) {
        super.setPlayback(playback);
        if (playback) {
            SDViewUtil.hide(tv_socre_name);
        }
    }

    public void showOperateViewerView() {
        SDViewUtil.show(view_operate_viewer);
    }

    public void hideOperateViewerView() {
        SDViewUtil.hide(view_operate_viewer);
    }

    /**
     * 私密直播是否显示加人和踢人按钮
     *
     * @param actModel
     */
    public void bindShowOperateViewer(App_get_videoActModel actModel) {
        if (getLiveInfo().isCreater()) {
            if (getLiveInfo().isPrivate()) {
                SDViewUtil.show(view_operate_viewer);
            } else {
                SDViewUtil.hide(view_operate_viewer);
            }
        } else {
            if (getLiveInfo().isPrivate()) {
                if (actModel != null) {
                    UserModel createrModel = actModel.getPodcast();
                    if (createrModel != null) {
                        if (actModel.getIsManage() == 1) {
                            SDViewUtil.show(view_operate_viewer);
                        } else {
                            SDViewUtil.hide(view_operate_viewer);
                        }
                    }
                }
            } else {
                SDViewUtil.hide(view_operate_viewer);
            }
        }
    }

    /**
     * 是否显示主播离开提示
     *
     * @param actModel
     */
    public void bindShowCreaterLeave(App_get_videoActModel actModel) {
        if (actModel != null) {
            if (actModel.getLive_in() == 1) {
                if (getLiveInfo().isCreater()) {
                    SDViewUtil.hide(tv_creater_leave);
                } else {
                    if (actModel.getOnline_status() == 0) {
                        SDViewUtil.show(tv_creater_leave);
                    } else {
                        SDViewUtil.hide(tv_creater_leave);
                    }
                }
            } else {
                SDViewUtil.hide(tv_creater_leave);
            }
        }
    }

    /**
     * 绑定数据
     *
     * @param actModel
     */
    public void bindData(App_get_videoActModel actModel) {
        if (actModel != null) {
            initActivityRoom(actModel);
            requestMonitor();
            requestMonitorForHotScore();
            refreshCreaterTime();
            bindShowOperateViewer(actModel);
            bindShowCreaterLeave(actModel);

            // 直播类型在1.4版本的需求需要改成主播昵称,如果昵称过长需要截断
            String createrNickName = "";
            try {
                createrNickName = actModel.getPodcast().getNickName();
            } catch (Exception e) {
            }
            if (!TextUtils.isEmpty(createrNickName)) {
                if (createrNickName.length() >= 7) {
                    createrNickName = createrNickName.substring(0, 5);
                    createrNickName = createrNickName + "...";
                }
                setTextVideoType(createrNickName);
            }
            //
            //            if (isPlayback()) {
            //                setTextVideoType("精彩回放");
            //            }

            UserModel createrModel = actModel.getPodcast();
            if (createrModel != null) {
                UserModel creater = createrModel;
                bindUserData(creater);
                if (!creater.equals(UserModelDao.query())) {
                    ll_follow.setScaleX(1.0f);
                    ll_follow.setScaleY(1.0f);
                    bindHasFollow(actModel.getHas_focus(), false);
                } else {
                    SDViewUtil.hide(ll_follow);
                }
            }
            updateViewerNumber(actModel.getViewer_num());
            SDViewBinder.setTextView(tv_socre_name, actModel.getRanking());
        }
    }


    /**
     * 如果是活动房间,需要初始化
     */
    private void initActivityRoom(App_get_videoActModel actModel) {
        RoomResourceModel roomResourceModel = actModel.getResource();
        if (null != roomResourceModel) {
            ll_ringgirl_logo.setVisibility(View.VISIBLE);
            String logo_url = roomResourceModel.getLogo_url();
            List<String> list = roomResourceModel.getFloat_pic_url();
            String activityTicketJumpUrl = roomResourceModel.getActivity_ticket_ranking_url();
            if (!TextUtils.isEmpty(activityTicketJumpUrl)) {
                jumpUrl = activityTicketJumpUrl;
            }
            long activityTicket = actModel.getPodcast().getActivityTicket();
            if (null != logo_url && !"".equals(logo_url)) {
                showActivityLogo(logo_url);
            } else {
                showActivityLogo(null);
            }
            if (activityTicket < 0) {
                activityTicket = 0;
            }
            rl_fighting.setVisibility(View.VISIBLE);
            tv_fighting.setVisibility(View.VISIBLE);
            updateFighting(activityTicket);
        } else {
            ll_ringgirl_logo.setVisibility(View.GONE);
        }
    }

    /**
     * 开始心跳
     */
    public void requestMonitor() {
        if (!getLiveInfo().isCreater()) {
            return;
        }

        long time = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            time = model.getMonitor_second() * 1000;
        }
        if (time <= 0) {
            time = 30 * 1000;
        }

        looperMonitor.start(time, new Runnable() {

            @Override
            public void run() {
                //                int videoCount = QavsdkControl.getInstance().getAVRoomManager().getVideoCount();
                //                int memberCount = QavsdkControl.getInstance().getMemberCount
                // 七牛临时

                CommonInterface.requestMonitor(getLiveInfo().getRoomId(), getLiveInfo().getCurrentGuestId(),
                        currentMissionId, new AppRequestCallback<App_monitorActModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                LogUtil.i("Monitor onSuccess---");
                                App_monitorActModel.MissionProcess missionProcess = actModel.missionProcess;
                                if (missionProcess != null) {
                                    currentMissionId = missionProcess.currentMissionId;
                                }
                            }
                        });
            }
        });
    }

    /**
     * 开播时间
     */
    public void refreshCreaterTime() {
        if (!getLiveInfo().isCreater()) {
            return;
        }
        mCreaterTimeLooper.start(1000, new Runnable() {
            @Override
            public void run() {
                if (!mFinishShow) {
                    setCreaterTime(LiveUtils.formatShowTime(mTime));
                    mTime++;
                }
            }
        });
    }

    public void isFinishShow() {
        mFinishShow = true;
    }

    /**
     * 热度心跳
     */
    public void requestMonitorForHotScore() {
        if (isPlayback()) {
            return;
        }
        long time = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            time = model.getRefresh_ranking_time() * 1000;
        }
        if (time <= 0) {
            time = 30 * 1000;
        }

        mScoreLooperMonitor.start(time, new Runnable() {

            @Override
            public void run() {
                CommonInterface.requestHotScoreMonitor(getLiveInfo().getRoomId(), new AppRequestCallback<AppRoomHotScoreMonitorActModel>() {
                    @Override
                    protected void onSuccess(SDResponse resp) {
                        ERequestHotSuccess event = new ERequestHotSuccess();
                        event.scoreName = actModel.ranking;
                        event.score = actModel.score;
                        SDEventManager.post(event);
                    }
                });
            }
        });
    }

    protected void clickFollow() {
        CommonInterface.requestFollow(getLiveInfo().getCreaterId(), new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    bindHasFollow(actModel.getRelationship(), true);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                ll_follow.setOnClickListener(RoomInfoView.this);
            }
        });
    }

    protected void clickTicket() {
        Intent intent = new Intent(getActivity(), LiveContActivity.class);
        intent.putExtra(LiveContLocalFragment.EXTRA_ROOM_ID, getLiveInfo().getRoomId());
        intent.putExtra(LiveContTotalFragment.EXTRA_USER_ID, getLiveInfo().getCreaterId());
        getActivity().startActivity(intent);
    }

    /**
     * 查看战斗力值
     */
    protected void clickFighting() {
        if (null != jumpUrl && !"".equals(jumpUrl)) {
            Intent intent = new Intent(getActivity(), FightingShowActivity.class);
            intent.putExtra(LiveContLocalFragment.EXTRA_ACTTICKET_URL, jumpUrl);
            getActivity().startActivity(intent);
        }
    }

    private void bindUserData(UserModel user) {
        setHeadImgUrl(user.getHeadImage());
        SDViewBinder.setImageView(getActivity(), user.getHeadImage(), iv_head_image, R.drawable.ic_default_head);
        if (TextUtils.isEmpty(user.getV_icon())) {
            SDViewUtil.hide(iv_level);
        } else {
            SDViewUtil.show(iv_level);
            SDViewBinder.setImageView(getActivity(), user.getV_icon(), iv_level);
        }
        SDViewBinder.setTextView(tv_user_number, String.valueOf(user.getUserId()));
        updateTicket(user.getTicket());
    }

    private void setHeadImgUrl(String url) {
        this.headImgUrl = url;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    /**
     * 更新钱票数量，只有数量变大的时候才会更新
     *
     * @param t
     */
    private void updateTicketUp(long t) {
        if (t > this.ticket) {
            updateTicket(t);
        }
    }

    /**
     * 更新战斗力值，只有数量变大的时候才会更新
     *
     * @param t
     */
    private void updateFightingtUp(long t) {
        if (t > this.fighting) {
            updateFighting(t);
        }
    }

    /**
     * 更新钱票数量
     *
     * @param t
     */
    private void updateTicket(long t) {
        if (t < 0) {
            t = 0;
        }
        this.ticket = t;
        SDViewBinder.setTextView(tv_ticket, String.valueOf(ticket));
    }

    /**
     * 更新战斗力值，只有数量变大的时候才会更新
     *
     * @param t
     */
    private void updateFighting(long t) {
        if (t < 0) {
            t = 0;
        }
        this.fighting = t;
        SDViewBinder.setTextView(tv_fighting, String.valueOf(fighting));
    }

    private void updateViewerNumber(int number) {
        this.viewerNumber = number;

        if (viewerNumber < 0) {
            viewerNumber = 0;
        }
        SDViewBinder.setTextView(tv_viewer_number, String.valueOf(viewerNumber));
    }

    public int getVierwerNumber() {
        return viewerNumber;
    }

    protected void clickHeadImage(String to_userid) {
        // 显示用户信息窗口
        LiveUserInfoDialog dialog = new LiveUserInfoDialog(getActivity(), to_userid);
        dialog.show();
    }

    protected void clickScore(int roomId) {
        //显示热度信息窗口
        LiveHotProgressDialog dialog = new LiveHotProgressDialog(getActivity(), roomId);
        dialog.show();
    }

    private void loadMoreViewer() {
        if (viewerActModel != null) {
            if (viewerActModel.getHas_next() == 1) {
                page++;
                requestViewer(true);
            } else {
                // 没有更多数据了
            }
        } else {
            refreshViewer();
        }
    }

    /**
     * 刷新观众列表
     */
    @Deprecated
    public void refreshViewer() {
        page = 1;
        startRefreshTimer();
    }

    /**
     * 请求观众列表
     *
     * @param isLoadMore
     */
    @Deprecated
    protected void requestViewer(final boolean isLoadMore) {
        // 加载观看者数量
        CommonInterface.requestViewerList(getLiveInfo().getGroupId(), page, getVierwerNumber(), new AppRequestCallback<App_viewerActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    //                    onRequestViewerSuccess(actModel, isLoadMore);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
            }
        });
    }

    protected void onRequestViewerSuccess(CustomMsgOnlineNumber actModel, final boolean isLoadMore) {
        this.viewerActModel = actModel;
        // 观众人数
        //        updateViewerNumber(actModel.getWatchNumber());
        updateViewerNumberByList(actModel.getWatch_number_list(), actModel.getGroupNum());
        List<UserModel> listViewer = actModel.getViewerList();
        removeCreater(listViewer);
        if (isLoadMore) {
            adapter.appendData(listViewer);
        } else {
            updateViewerData(listViewer);
            //            adapter2.updateData(listViewer);
        }
    }

    /**
     * 第一次刷新头像 前五个依次刷新 之后一起刷新  - 对主播而言
     *
     * @param listViewer
     */
    private void updateViewerData(final List<UserModel> listViewer) {
        if (getLiveInfo().isCreater()) {
            if (adapter.getItemCount() > 0) {
                adapter.updateData(listViewer);
            } else {
                if (listViewer != null && listViewer.size() >= 5) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 5; i++) {
                                final List<UserModel> oneList = listViewer.subList(0, i + 1);
                                tv_viewer_number.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.updateData(oneList);
                                    }
                                });
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                }
                            }
                            tv_viewer_number.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.updateData(listViewer);
                                }
                            });
                        }
                    }).start();
                }
            }
        } else {
            adapter.updateData(listViewer);
        }
    }


    /**
     * 通过返回的集合 间隔变化用户数 周期为1s
     *
     * @param list
     * @param watchNumber
     */
    private void updateViewerNumberByList(final List<Integer> list, int watchNumber) {
        if (list != null && list.size() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (final Integer obj : list) {
                        tv_viewer_number.post(new Runnable() {
                            @Override
                            public void run() {
                                if (obj != null) {
                                    updateViewerNumber(obj);
                                }
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }).start();
        } else {
            updateViewerNumber(watchNumber);
        }
    }

    @Deprecated
    protected void startRefreshTimer() {
        long period = REFRESH_VIEWER_DURANTION;
        //        if (getLiveInfo().isCreater()) {
        //            period = 20 * 1000;
        //        } else {
        //            if (viewerNumber < 50) {
        //                period = (random.nextInt(8) + 8) * 1000;
        //            } else if (viewerNumber < 100) {
        //                period = (random.nextInt(11) + 20) * 1000;
        //            } else if (viewerNumber < 1000) {
        //                period = (random.nextInt(21) + 60) * 1000;
        //            } else {
        //                looperViewer.stop();
        //            }
        //        }

        //        if (period > 0) {
        looperViewer.start(period, new Runnable() {

            @Override
            public void run() {
                requestViewer(false);
            }
        });
        //        }
    }

    private void removeCreater(List<UserModel> listViewer) {
        if (getLiveInfo().getRoomInfo() != null) {
            if (getLiveInfo().getRoomInfo().getLive_in() == 1) {
                SDCollectionUtil.iterate(listViewer, new SDIterateListener<UserModel>() {
                    @Override
                    public boolean next(int i, UserModel item, Iterator<UserModel> it) {
                        if (item.getUserId().equals(getLiveInfo().getCreaterId())) {
                            it.remove();
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }

    public void onEventMainThread(EImOnNewMessages event) {
        String peer = event.msg.getConversationPeer();
        if (peer.equals(getLiveInfo().getGroupId())) {
            if (LiveConstant.CustomMsgType.MSG_GIFT == event.msg.getCustomMsgType()) {
                CustomMsgGift msg = event.msg.getCustomMsgGift();
                updateTicketUp(msg.getTotalTicket());
                updateFightingtUp(msg.getTotalActivityTicket());
            } else if (LiveConstant.CustomMsgType.MSG_POP_MSG == event.msg.getCustomMsgType()) {
                CustomMsgPopMsg msg = event.msg.getCustomMsgPopMsg();
                //                updateTicketUp(msg.getTotalTicket()); // 1.5 规定发送弹幕不更新麻辣币
                updateFightingtUp(msg.getTotal_activity_ticket());
            } else if (LiveConstant.CustomMsgType.MSG_RED_ENVELOPE == event.msg.getCustomMsgType()) {
                CustomMsgRedEnvelope msg = event.msg.getCustomMsgRedEnvelope();
                updateTicketUp(msg.getTotal_ticket());
                updateFightingtUp(msg.getTotal_activity_ticket());
            } else if (LiveConstant.CustomMsgType.MSG_VIEWER_JOIN == event.msg.getCustomMsgType()) {
                CustomMsgViewerJoin msg = event.msg.getCustomMsgViewerJoin();
                UserModel modelJoin = msg.getSender();
                if (!adapter.getData().contains(modelJoin)) {
                    insertUser(modelJoin);
                }
                updateViewerNumber(++viewerNumber);
            } else if (LiveConstant.CustomMsgType.MSG_VIEWER_QUIT == event.msg.getCustomMsgType()) {
                CustomMsgViewerQuit msg = event.msg.getCustomMsgViewerQuit();
                UserModel modelQuit = msg.getSender();
                adapter.removeData(modelQuit);
                updateViewerNumber(--viewerNumber);
            } else if (LiveConstant.CustomMsgType.MSG_CREATER_LEAVE == event.msg.getCustomMsgType()) {
                SDViewUtil.show(tv_creater_leave);
            } else if (LiveConstant.CustomMsgType.MSG_CREATER_COME_BACK == event.msg.getCustomMsgType()) {
                SDViewUtil.hide(tv_creater_leave);
            } else if (LiveConstant.CustomMsgType.MSG_ONLINE_NUM == event.msg.getCustomMsgType()) {
                CustomMsgOnlineNumber customMsgOnlineNumber = event.msg.getCustomMsgOnlineNumber();
                onRequestViewerSuccess(customMsgOnlineNumber, false);
            }
        }
        if (LiveConstant.CustomMsgType.MSG_LRS == event.msg.getCustomMsgType()) {
            if (LRSManager.getInstance().mIsGaming) {
                showGameTime(event.msg.getCustomMsgLRS());
            }
        }
    }

    public void showOrHideGameTime(boolean isShow) {
        if (isShow) {
            mCanShowGameTime = true;
        } else {
            mCanShowGameTime = false;
            if (getLiveInfo().isCreater()) {
                SDViewUtil.hide(rl_game_time);
            } else {
                SDViewUtil.hide(rl_game_time_viewer);
            }
        }
    }

    public void onEventMainThread(ELRSWitchOperate event) {
        switch (event.getStep()) {
            case ELRSWitchOperate.SURE_POISON:
                doShowGameTime(event.getMsg(), 6);
                break;
        }
    }

    public void onEventMainThread(ELRSHunterOperate event) {
        switch (event.getStep()) {
            case ELRSHunterOperate.HUNTE:
                doShowGameTime(event.getMsg(), 8);
                break;
        }
    }

    private void showGameTime(final CustomMsgLRS msg) {
        if (msg != null) {
            switch (msg.getStep()) {
                case 4:
                    doShowGameTime(msg, 4);
                    break;
                case 5:
                    doShowGameTime(msg, 5);
                    break;
                case 6:
                    if (LRSManager.getInstance().getSelfRole() != LRSUserModel.GAME_ROLE_WITCH) {
                        doShowGameTime(msg, 6);
                    }
                    break;
                case 8:
                    if (LRSManager.getInstance().getSelfRole() != LRSUserModel.GAME_ROLE_HUNTER) {
                        doShowGameTime(msg, 8);
                    }
                    break;
                case 9://遗言时间
                case 10://轮流发言
                    doShowGameTime(msg, msg.getStep());
                    break;
                case 11:
                    doShowGameTime(msg, 11);
                    break;
                case 12:
                    cleanGameTime();
                    break;
            }
        }
    }

    private String getTimeDes(int step, CustomMsgLRS msg) {
        String result = "";
        switch (step) {
            case 4:
                result = "天黑倒计时";
                break;
            case 5:
                if (LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WOLF) {
                    result = "杀人倒计时";
                } else if (LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_PROPHET) {
                    result = "验人倒计时";
                } else {
                    result = "狼人预言家活动倒计时";
                }
                break;
            case 6:
                if (LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WITCH) {
                    result = "女巫活动倒计时";
                } else {
                    result = "女巫活动时间";
                }
                break;
            case 8:
                if (LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_HUNTER) {
                    result = "猎人活动倒计时";
                } else {
                    result = "猎人活动时间";
                }
                break;
            case 9:
                if (msg.getUser_id().equals(mUserId)) {
                    result = "现在是您的遗言时间";
                } else {
                    result = LRSManager.getInstance().getUserIndex(msg.getUser_id()) + "号遗言时间";
                }
                break;
            case 10:
                if (msg.getUser_id().equals(mUserId)) {
                    result = "现在是您的发言时间";
                } else {
                    result = LRSManager.getInstance().getUserIndex(msg.getUser_id()) + "号发言时间";
                }
                break;
            case 11:
                result = "投票倒计时";
                break;
        }
        return result;
    }

    private void doShowGameTime(final CustomMsgLRS msg, int step) {
        cleanGameTime();
        String timeDes = getTimeDes(step, msg);
        switch (step) {
            case 6://女巫倒计时时间
                if (LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WITCH) {
                    mGameTime = msg.getWait3();
                } else {
                    mGameTime = msg.getWait1() + msg.getWait2() + msg.getWait3();
                }
                break;
            case 8://猎人倒计时时间
                if (LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_HUNTER) {
                    mGameTime = msg.getWait2();
                } else {
                    mGameTime = msg.getWait1() + msg.getWait2();
                }
                break;
            case 9://遗言
            case 10://轮流发言
                mGameTime = msg.getWait() + msg.getPass_wait();
                break;
            default:
                mGameTime = msg.getWait();
                break;
        }
        if (mGameTime >= 0) {
            if (mCanShowGameTime) {
                if (getLiveInfo().isCreater()) {
                    SDViewUtil.show(rl_game_time);
                } else {
                    SDViewUtil.show(rl_game_time_viewer);
                }
            } else {
                if (getLiveInfo().isCreater()) {
                    SDViewUtil.hide(rl_game_time);
                } else {
                    SDViewUtil.hide(rl_game_time_viewer);
                }
            }
            if (step == 6 && LRSManager.getInstance().getSelfRole() != LRSUserModel.GAME_ROLE_WITCH) {
                if (getLiveInfo().isCreater()) {
                    SDViewUtil.hide(tv_game_time);
                } else {
                    SDViewUtil.hide(tv_game_time_viewer);
                }
            } else if (step == 8 && LRSManager.getInstance().getSelfRole() != LRSUserModel.GAME_ROLE_HUNTER) {
                if (getLiveInfo().isCreater()) {
                    SDViewUtil.hide(tv_game_time);
                } else {
                    SDViewUtil.hide(tv_game_time_viewer);
                }
            } else if (step == 9 || step == 10) { //遗言和发言
                if (getLiveInfo().isCreater()) {
                    SDViewUtil.hide(tv_game_time);
                } else {
                    SDViewUtil.hide(tv_game_time_viewer);
                }
            } else {
                if (getLiveInfo().isCreater()) {
                    SDViewUtil.show(tv_game_time);
                } else {
                    SDViewUtil.show(tv_game_time_viewer);
                }
            }
            if (getLiveInfo().isCreater()) {
                SDViewBinder.setTextView(tv_game_time_des, timeDes);
                SDViewBinder.setTextView(tv_game_time, mGameTime + "S");
            } else {
                SDViewBinder.setTextView(tv_game_time_des_viewer, timeDes);
                SDViewBinder.setTextView(tv_game_time_viewer, mGameTime + "S");
            }
            --mGameTime;
            if (mGameTime >= 0) {
                mGameTimeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCanShowGameTime) {
                            if (getLiveInfo().isCreater()) {
                                SDViewUtil.show(rl_game_time);
                            } else {
                                SDViewUtil.show(rl_game_time_viewer);
                            }
                        } else {
                            if (getLiveInfo().isCreater()) {
                                SDViewUtil.hide(rl_game_time);
                            } else {
                                SDViewUtil.hide(rl_game_time_viewer);
                            }
                        }
                        if (getLiveInfo().isCreater()) {
                            SDViewBinder.setTextView(tv_game_time, mGameTime + "S");
                        } else {
                            SDViewBinder.setTextView(tv_game_time_viewer, mGameTime + "S");
                        }
                        --mGameTime;
                        if (mGameTime >= 0) {
                            mGameTimeHandler.postDelayed(this, 1 * 1000);
                        } else {
                            cleanGameTime();
                        }
                    }
                }, 1 * 1000);
            } else {
                cleanGameTime();
            }
        }
    }

    private void cleanGameTime() {
        mGameTimeHandler.removeCallbacksAndMessages(null);
        if (getLiveInfo().isCreater()) {
            SDViewUtil.hide(rl_game_time);
        } else {
            SDViewUtil.hide(rl_game_time_viewer);
        }
        mGameTime = 0;
    }

    private void insertUser(UserModel userModel) {
        UserModel item = null;
        int count = adapter.getItemCount();
        if (count > 50) {
            count = 50;
        }
        int level = userModel.getUserLevel();
        int differ = 0;
        int position = 0;
        for (int i = count - 1; i >= 0; i--) {
            item = adapter.getItemData(i);
            differ = level - item.getUserLevel();

            if (differ > 0) {
                continue;
            } else {
                position = i + 1;
                break;
            }
        }
        LogUtil.i("insert:" + position);

        adapter.insertData(position, userModel);
    }

    public void onEventMainThread(EExitRoomComplete event) {
        looperMonitor.stop();
        looperViewer.stop();
        mScoreLooperMonitor.stop();
        mCreaterTimeLooper.stop();
    }

    public void onEventMainThread(ERequestFollowSuccess event) {
        if (event.userId.equals(getLiveInfo().getCreaterId())) {
            bindHasFollow(event.actModel.getRelationship(), true);
        }
    }

    public void onEventMainThread(ERequestHotSuccess event) {
        SDViewBinder.setTextView(tv_socre_name, event.scoreName);
    }

    public int getHasFollow() {
        return hasFollow;
    }

    private void bindHasFollow(int hasFollow, boolean anim) {
        this.hasFollow = hasFollow;
        if (hasFollow == 1) {
            ll_follow.setOnClickListener(null);
            if (anim) {
                AnimatorSet animatorSet = SDAnimationUtil.scale(ll_follow, 1.0f, 0.0f);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        SDViewUtil.hide(ll_follow);
                    }
                });
                animatorSet.start();
            } else {
                SDViewUtil.hide(ll_follow);
            }
        } else {
            ll_follow.setOnClickListener(this);
            if (anim) {
                AnimatorSet animatorSet = SDAnimationUtil.scale(ll_follow, 0.0f, 1.0f);
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        SDViewUtil.show(ll_follow);
                    }
                });
                animatorSet.start();
            } else {
                SDViewUtil.show(ll_follow);
            }
        }
    }

    @Override
    public void onDestroy() {
        looperMonitor.stop();
        looperViewer.stop();
        mScoreLooperMonitor.stop();
        mCreaterTimeLooper.stop();
        super.onDestroy();
    }

    public interface ClickListener {
        void onClickAddViewer(View v);

        void onClickMinusViewer(View v);
    }

}
