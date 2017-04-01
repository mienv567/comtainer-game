package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.auction.dialog.room.AuctionSucPaySingleton;
import com.fanwe.auction.model.PaiBuyerModel;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionFail;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ELRSChannelChange;
import com.fanwe.live.event.ELRSGameStateChange;
import com.fanwe.live.event.ELRSOutGame;
import com.fanwe.live.event.ELRSSpeakOperate;
import com.fanwe.live.model.LRSUserModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.pop.PassMicTipsPop;
import com.fanwe.live.view.LiveUnReadNumTextView;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

/**
 * 观众底部菜单
 * Created by Administrator on 2016/8/6.
 */
public class RoomViewerBottomView extends RoomBottomView {
    public RoomViewerBottomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomViewerBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomViewerBottomView(Context context) {
        super(context);
    }

    private View rl_bottom_open_send;
    private View rl_bottom_podcast_order;//星店订单
    private View rl_bottom_viewer_auction_btn;//竞拍订单
    private LiveUnReadNumTextView tv_pai_num;//数量
    private View rl_bottom_msg;
    private View rl_bottom_share;
    private View rl_bottom_invite_video;
    private View rl_bottom_gift;
    private View rl_bottom_game_pass;
    private View rl_bottom_game_out;
    private View rl_bottom_game_open_close;
    private View rl_bottom_lrs_channel;
    private ImageView img_bottom_lrs_channel;
    private ImageView img_game_open_close;
    private boolean mIsGameClose = true; //判断是否显示为收起图标
    private boolean mIsLRSPublicChannel = true;//判断狼人杀频道资源是否为公共频道 - 如果是  隐藏可切换界面
    private ClickListener clickListener;
    private LinearLayout ll_channel_layout;//频道layout
    private TextView tv_lrs_channel_public; //公频
    private TextView tv_lrs_channel_wolf; //狼人
    private View division_wolf;//狼人后面的分割线
    private TextView tv_lrs_channel_progress; //进程
    private TextView tv_lrs_channel_rule; //规则
    private boolean mCanShowVideo = false; //判断是否可以显示连麦按钮
    private int mCurrentChannel = 0;// 0 - 3 表示当前所在频道
    private String mUserId;//当前用户Id
    private PassMicTipsPop mPopWindow;
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_viewer_bottom;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        UserModel model = UserModelDao.query();
        if (model != null) {
            mUserId = model.getUserId();
        }
        rl_bottom_open_send = find(R.id.rl_bottom_open_send);

        rl_bottom_podcast_order = find(R.id.rl_bottom_podcast_order);
        rl_bottom_viewer_auction_btn = find(R.id.rl_bottom_viewer_auction_btn);
        tv_pai_num = find(R.id.tv_pai_num);
        //竞拍单默认设置1
        tv_pai_num.setText("1");

        rl_bottom_msg = find(R.id.rl_bottom_msg);
        rl_bottom_share = find(R.id.rl_bottom_share);
        rl_bottom_invite_video = find(R.id.rl_bottom_invite_video);
        rl_bottom_gift = find(R.id.rl_bottom_gift);
//        rl_bottom_close = find(R.id.rl_bottom_close);
        tv_unread_number = find(R.id.tv_unread_number);
        rl_bottom_game_pass = find(R.id.rl_bottom_game_pass);
        rl_bottom_game_out = find(R.id.rl_bottom_game_out);
        rl_bottom_game_open_close = find(R.id.rl_bottom_game_open_close);
        rl_bottom_lrs_channel = find(R.id.rl_bottom_lrs_channel);
        img_game_open_close = find(R.id.img_game_open_close);
        img_bottom_lrs_channel = find(R.id.img_bottom_lrs_channel);
        ll_channel_layout = find(R.id.ll_channel_layout);
        tv_lrs_channel_public = find(R.id.tv_lrs_channel_public);
        tv_lrs_channel_wolf = find(R.id.tv_lrs_channel_wolf);
        division_wolf = find(R.id.division_wolf);
        tv_lrs_channel_progress = find(R.id.tv_lrs_channel_progress);
        tv_lrs_channel_rule = find(R.id.tv_lrs_channel_rule);
        rl_bottom_open_send.setOnClickListener(this);
        rl_bottom_podcast_order.setOnClickListener(this);
        rl_bottom_viewer_auction_btn.setOnClickListener(this);
        rl_bottom_msg.setOnClickListener(this);
        rl_bottom_share.setOnClickListener(this);
        rl_bottom_invite_video.setOnClickListener(this);
        rl_bottom_gift.setOnClickListener(this);
        rl_bottom_game_pass.setOnClickListener(this);
        rl_bottom_game_out.setOnClickListener(this);
        rl_bottom_game_open_close.setOnClickListener(this);
        rl_bottom_lrs_channel.setOnClickListener(this);
        tv_lrs_channel_public.setOnClickListener(this);
        tv_lrs_channel_wolf.setOnClickListener(this);
        tv_lrs_channel_progress.setOnClickListener(this);
        tv_lrs_channel_rule.setOnClickListener(this);
        mPopWindow = new PassMicTipsPop(getActivity());
//        rl_bottom_close.setOnClickListener(this);

        //开启竞拍和实物总开关
        if (AppRuntimeWorker.getShow_hide_pai_view() == 1) {
            //开启虚拟开关
            if (AppRuntimeWorker.getPai_real_btn() == 1) {
                SDViewUtil.show(rl_bottom_podcast_order);
            }
        }
        initChannelShow();
        setUnreadMessageModel(IMHelper.getC2CTotalUnreadMessageModel());
    }

    public void initChannelShow(){
        tv_lrs_channel_public.setTextColor(getResources().getColor(R.color.main_color));
        mCurrentChannel = 0;
    }

    public void reSet(){
        mIsLRSPublicChannel = true;
        mIsGameClose = true;
        mCurrentChannel = 0;
        tv_lrs_channel_public.setTextColor(getResources().getColor(R.color.main_color));
        tv_lrs_channel_wolf.setTextColor(getResources().getColor(R.color.white));
        tv_lrs_channel_progress.setTextColor(getResources().getColor(R.color.white));
        tv_lrs_channel_rule.setTextColor(getResources().getColor(R.color.white));
    }

    public void showClick(boolean show) {
        if (show) {
            SDViewUtil.show(rl_bottom_open_send);
            SDViewUtil.show(rl_bottom_msg);
            SDViewUtil.show(rl_bottom_share);
            SDViewUtil.show(rl_bottom_gift);
        } else {
            SDViewUtil.invisible(rl_bottom_open_send);
            SDViewUtil.invisible(rl_bottom_msg);
            SDViewUtil.invisible(rl_bottom_share);
            SDViewUtil.invisible(rl_bottom_gift);
        }
    }

    public void showChannelView(){
        SDViewUtil.show(ll_channel_layout);
    }

    public void hideChannelView(){
        SDViewUtil.hide(ll_channel_layout);
    }

    public void showGamePass(){
        SDViewUtil.show(rl_bottom_game_pass);
        rl_bottom_game_pass.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPopWindow.showPopTips(rl_bottom_game_pass, SDViewUtil.dp2px(5), 0);
            }
        }, 100);
    }

    public void hideGamePass(){
        if(mPopWindow != null) {
            mPopWindow.dismiss();
        }
        SDViewUtil.hide(rl_bottom_game_pass);
    }

    public void performChannelClick(){
        rl_bottom_lrs_channel.performClick();
    }

    public void showInviteVideoView() {
        mCanShowVideo = true;
        if(LRSManager.getInstance().mIsGaming){
            SDViewUtil.hide(rl_bottom_invite_video);
        }else {
            SDViewUtil.show(rl_bottom_invite_video);
        }
    }

    public void hideInviteVideoView() {
        mCanShowVideo = false;
        SDViewUtil.hide(rl_bottom_invite_video);
    }

    /**
     * 回放房间不需要发送礼物的选项
     */
    public void hideSendGiftView(){
        SDViewUtil.hide(rl_bottom_gift);
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == rl_bottom_open_send) {
            if (clickListener != null) {
                clickListener.onClickBottomOpenSend(v);
            }
        } else if (v == rl_bottom_podcast_order) {
            if (clickListener != null) {
                clickListener.onClickBottomPodcastOrder(v);
            }
        } else if (v == rl_bottom_viewer_auction_btn) {
            if (clickListener != null) {
                clickListener.onCLickBottomViewerAuctionBtn(v);
            }
        } else if (v == rl_bottom_msg) {
            if (clickListener != null) {
                clickListener.onClickBottomMsg(v);
            }
        } else if (v == rl_bottom_share) {
            if (clickListener != null) {
                clickListener.onClickBottomShare(v);
            }
        } else if (v == rl_bottom_invite_video) {
            if (clickListener != null) {
                clickListener.onClickBottomInviteVideo(v);
            }
        } else if (v == rl_bottom_gift) {
            if (clickListener != null) {
                clickListener.onClickBottomGift(v);
            }
        }else if(v == rl_bottom_lrs_channel){
            judgeChannelShow();
            if (clickListener != null) {
                clickListener.onClickBottomLrsChannel(v);
            }
        }else if(v == rl_bottom_game_pass){
            if(clickListener != null){
                clickListener.onClickBottomGamePass(v);
            }
            hideGamePass();
        }else if(v == rl_bottom_game_out){
            if(clickListener != null){
                clickListener.onClickBottomGameOut(v);
            }
        }else if(v == rl_bottom_game_open_close){
            judgeOpenOrCloseShow();
            if(clickListener != null){
                clickListener.onClickBottomGameOpenClose(v,mIsGameClose);
            }
        }else if(v == tv_lrs_channel_public){
            judgeChannelTextShow(tv_lrs_channel_public);
            if (clickListener != null) {
                clickListener.onClickChannelPublic(v);
            }
        }else if(v == tv_lrs_channel_wolf){
            judgeChannelTextShow(tv_lrs_channel_wolf);
            if (clickListener != null) {
                clickListener.onClickChannelWolf(v);
            }
        }else if(v == tv_lrs_channel_progress){
            judgeChannelTextShow(tv_lrs_channel_progress);
            if (clickListener != null) {
                clickListener.onClickChannelProgress(v);
            }
        }else if(v == tv_lrs_channel_rule){
            judgeChannelTextShow(tv_lrs_channel_rule);
            if (clickListener != null) {
                clickListener.onClickChannelRule(v);
            }
        }
//        else if (v == rl_bottom_close)
//        {
//            if (clickListener != null)
//            {
//                clickListener.onClickBottomClose(v);
//            }
//        }
    }

    public interface ClickListener {
        void onClickBottomOpenSend(View v);

        void onClickBottomPodcastOrder(View v);

        void onCLickBottomViewerAuctionBtn(View v);

        void onClickBottomMsg(View v);

        void onClickBottomShare(View v);

        void onClickBottomInviteVideo(View v);

        void onClickBottomGift(View v);

        void onClickBottomClose(View v);

        void onClickBottomLrsChannel(View v);

        void onClickBottomGamePass(View v);

        void onClickBottomGameOut(View v);

        void onClickBottomGameOpenClose(View v,boolean isShow);

        void onClickChannelPublic(View v);

        void onClickChannelWolf(View v);

        void onClickChannelProgress(View v);

        void onClickChannelRule(View v);
    }

    public void onEventMainThread(ELRSGameStateChange event){
        if(event.isGaming){
            judgeWolfChannel();
            SDViewUtil.show(rl_bottom_lrs_channel);
            if(event.isViewer()){
                SDViewUtil.hide(rl_bottom_game_out);
            }else if(LRSManager.getInstance().userIsGamer(mUserId)) {
                SDViewUtil.show(rl_bottom_game_out);
            }
            SDViewUtil.show(rl_bottom_game_open_close);
            SDViewUtil.hide(rl_bottom_invite_video);
        }else{
            SDViewUtil.hide(rl_bottom_lrs_channel);
            SDViewUtil.hide(rl_bottom_game_out);
            SDViewUtil.hide(rl_bottom_game_open_close);
            if(mCanShowVideo){
                SDViewUtil.show(rl_bottom_invite_video);
            }else{
                SDViewUtil.hide(rl_bottom_invite_video);
            }
            hideChannelView();
        }
    }

    public void onEventMainThread(ELRSOutGame event){
        if(event.getOut()){
            SDViewUtil.hide(rl_bottom_game_out);
            hideGamePass();
        }
    }

    public void onEventMainThread(ELRSSpeakOperate event){
        if(event.getUserId().equals(mUserId)){
            showGamePass();
        }else{
            hideGamePass();
        }
    }

    private void judgeWolfChannel(){
        if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WOLF){
            SDViewUtil.show(tv_lrs_channel_wolf);
            SDViewUtil.show(division_wolf);
        }else{
            SDViewUtil.hide(tv_lrs_channel_wolf);
            SDViewUtil.hide(division_wolf);
        }
    }

    private void judgeChannelShow(){
        if(mIsLRSPublicChannel){
            mIsLRSPublicChannel = false;
            img_bottom_lrs_channel.setImageDrawable(getResources().getDrawable(R.drawable.ic_live_bottom_game_wolf));
            showChannelView();
            String groupId;
            if(mCurrentChannel == 0){
                groupId = LRSManager.getInstance().mGameGroupId;
            }else{
                groupId = LRSManager.getInstance().mWolfGroupId;
            }
            ELRSChannelChange event = new ELRSChannelChange(groupId,false);
            event.setChannelIndex(mCurrentChannel);
            SDEventManager.post(event);
        }else{
            mIsLRSPublicChannel = true;
            img_bottom_lrs_channel.setImageDrawable(getResources().getDrawable(R.drawable.ic_live_bottom_game_public));
            hideChannelView();
            ELRSChannelChange event = new ELRSChannelChange(getLiveInfo().getGroupId(), true);
            SDEventManager.post(event);
        }
    }

    private void setAllChannelNormal(){
        tv_lrs_channel_public.setTextColor(getResources().getColor(R.color.white));
        tv_lrs_channel_wolf.setTextColor(getResources().getColor(R.color.white));
        tv_lrs_channel_progress.setTextColor(getResources().getColor(R.color.white));
        tv_lrs_channel_rule.setTextColor(getResources().getColor(R.color.white));
    }

    private void judgeChannelTextShow(View v){
        switch (v.getId()){
            case R.id.tv_lrs_channel_public:
                if(mCurrentChannel != 0){
                    setAllChannelNormal();
                    tv_lrs_channel_public.setTextColor(getResources().getColor(R.color.main_color));
                    mCurrentChannel = 0;
                    ELRSChannelChange event = new ELRSChannelChange(LRSManager.getInstance().mGameGroupId,false);
                    event.setChannelIndex(0);
                    SDEventManager.post(event);
                }
                break;
            case R.id.tv_lrs_channel_wolf:
                if(mCurrentChannel != 1){
                    setAllChannelNormal();
                    tv_lrs_channel_wolf.setTextColor(getResources().getColor(R.color.main_color));
                    mCurrentChannel = 1;
                    ELRSChannelChange event = new ELRSChannelChange(LRSManager.getInstance().mWolfGroupId,false);
                    event.setChannelIndex(1);
                    SDEventManager.post(event);
                }
                break;
            case R.id.tv_lrs_channel_progress:
                if(mCurrentChannel != 2){
                    setAllChannelNormal();
                    tv_lrs_channel_progress.setTextColor(getResources().getColor(R.color.main_color));
                    mCurrentChannel = 2;
                    ELRSChannelChange event = new ELRSChannelChange(LRSManager.getInstance().mGameGroupId,false);
                    event.setChannelIndex(2);
                    SDEventManager.post(event);
                }
                break;
            case R.id.tv_lrs_channel_rule:
                if(mCurrentChannel != 3){
                    setAllChannelNormal();
                    tv_lrs_channel_rule.setTextColor(getResources().getColor(R.color.main_color));
                    mCurrentChannel = 3;
                    ELRSChannelChange event = new ELRSChannelChange(LRSManager.getInstance().mGameGroupId,false);
                    event.setChannelIndex(3);
                    SDEventManager.post(event);
                }
                break;
        }
    }

    private void judgeOpenOrCloseShow(){
        if(mIsGameClose){
            mIsGameClose = false;
            img_game_open_close.setImageDrawable(getResources().getDrawable(R.drawable.ic_game_open));
        }else{
            mIsGameClose = true;
            img_game_open_close.setImageDrawable(getResources().getDrawable(R.drawable.ic_game_close));
        }
    }

    //竞拍者接收消息显示隐藏付款入口
    public void onEventMainThread(EImOnNewMessages event) {
        String peer = event.msg.getConversationPeer();
        if (event.msg.isLocalPost()) {

        } else {
            if (peer.equals(getLiveInfo().getGroupId())) {
                if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_SUCCESS) {
                    CustomMsgAuctionSuccess customMsgAuctionSuccess = event.msg.getCustomMsgAuctionSuccess();
                    showHideCustomMsgAuction(customMsgAuctionSuccess.getBuyer());
                }
                if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_NOTIFY_PAY) {
                    CustomMsgAuctionNotifyPay customMsgAuctionNotifyPay = event.msg.getCustomMsgAuctionNotifyPay();
                    showHideCustomMsgAuction(customMsgAuctionNotifyPay.getBuyer());
                }
                if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_PAY_SUCCESS) {
                    CustomMsgAuctionPaySuccess customMsgAuctionPaySuccess = event.msg.getCustomMsgAuctionPaySuccess();
                    showHideCustomMsgAuction(customMsgAuctionPaySuccess.getBuyer());
                }
                if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_FAIL) {
                    CustomMsgAuctionFail customMsgAuctionFail = event.msg.getCustomMsgAuctionFail();
                    showHideCustomMsgAuction(customMsgAuctionFail.getBuyer());
                }
            }
        }
    }

    public void hideCustomMsgAuction() {
        SDViewUtil.hide(rl_bottom_viewer_auction_btn);
    }

    public void showHideCustomMsgAuction(List<PaiBuyerModel> list) {
        //存在付款时间初始化付款dialog
        AuctionSucPaySingleton.getInstance().newInstanceAuctionSucPayDialog(getActivity(), list);

        SDViewUtil.hide(rl_bottom_viewer_auction_btn);
        UserModel user = UserModelDao.query();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                PaiBuyerModel paiBuyerModel = list.get(i);
                if (user.getUserId().equals(paiBuyerModel.getUser_id())) {
                    if (paiBuyerModel.getType() == 1) {
                        SDViewUtil.show(rl_bottom_viewer_auction_btn);
                    } else {
                        SDViewUtil.hide(rl_bottom_viewer_auction_btn);
                    }
                }
            }
        }
    }
}
