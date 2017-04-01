package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.auction.event.ECreateAuctionSuccess;
import com.fanwe.auction.model.App_pai_user_get_videoActModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataInfoModel;
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
import com.sunday.eventbus.SDEventManager;

/**
 * Created by Administrator on 2016/8/4.
 */
public class RoomCreaterBottomView extends RoomBottomView {

    public RoomCreaterBottomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomCreaterBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomCreaterBottomView(Context context) {
        super(context);
    }

    private View rl_bottom_open_send;
    private View rl_bottom_podcast_order;
    private View rl_bottom_create_auction;
    private View rl_bottom_msg;
    private View rl_bottom_lrs;
    private View rl_bottom_lrs_channel;
    private View rl_bottom_game_pass;
    private View rl_bottom_game_out;
    private View rl_bottom_game_open_close;
    private View rl_bottom_music;
    private View rl_bottom_more;
    private ImageView img_bottom_lrs_channel;
    private ImageView img_game_open_close;
    private LinearLayout ll_channel_layout;//频道layout
    private TextView tv_lrs_channel_public; //公频
    private TextView tv_lrs_channel_wolf; //狼人
    private View division_wolf;//狼人后面的分割线
    private TextView tv_lrs_channel_progress; //进程
    private TextView tv_lrs_channel_rule; //规则
//    private View rl_bottom_close;

    private ClickListener clickListener;
    private boolean mIsLRSPublicChannel = true;//判断狼人杀频道资源是否为公共频道 - 如果是  隐藏可切换界面
    private boolean mIsGameClose = true; //判断是否显示为收起图标
    private int mCurrentChannel = 0;// 0 - 3 表示当前所在频道
    private String mUserId;//当前用户Id
    private PassMicTipsPop mPopWindow;
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_creater_bottom;
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
        rl_bottom_create_auction = find(R.id.rl_bottom_create_auction);
        rl_bottom_msg = find(R.id.rl_bottom_msg);
        rl_bottom_lrs = find(R.id.rl_bottom_lrs);
        rl_bottom_lrs_channel = find(R.id.rl_bottom_lrs_channel);
        img_bottom_lrs_channel = find(R.id.img_bottom_lrs_channel);
        rl_bottom_game_pass = find(R.id.rl_bottom_game_pass);
        rl_bottom_game_out = find(R.id.rl_bottom_game_out);
        rl_bottom_game_open_close = find(R.id.rl_bottom_game_open_close);
        img_game_open_close = find(R.id.img_game_open_close);
        rl_bottom_music = find(R.id.rl_bottom_music);
        rl_bottom_more = find(R.id.rl_bottom_more);
//        rl_bottom_close = find(R.id.rl_bottom_close);
        tv_unread_number = find(R.id.tv_unread_number);
        ll_channel_layout = find(R.id.ll_channel_layout);
        tv_lrs_channel_public = find(R.id.tv_lrs_channel_public);
        tv_lrs_channel_wolf = find(R.id.tv_lrs_channel_wolf);
        division_wolf = find(R.id.division_wolf);
        tv_lrs_channel_progress = find(R.id.tv_lrs_channel_progress);
        tv_lrs_channel_rule = find(R.id.tv_lrs_channel_rule);
        rl_bottom_open_send.setOnClickListener(this);
        rl_bottom_podcast_order.setOnClickListener(this);
        rl_bottom_create_auction.setOnClickListener(this);
        rl_bottom_msg.setOnClickListener(this);
        rl_bottom_lrs.setOnClickListener(this);
        rl_bottom_lrs_channel.setOnClickListener(this);
        rl_bottom_music.setOnClickListener(this);
        rl_bottom_more.setOnClickListener(this);
        rl_bottom_game_pass.setOnClickListener(this);
        rl_bottom_game_out.setOnClickListener(this);
        rl_bottom_game_open_close.setOnClickListener(this);
        tv_lrs_channel_public.setOnClickListener(this);
        tv_lrs_channel_wolf.setOnClickListener(this);
        tv_lrs_channel_progress.setOnClickListener(this);
        tv_lrs_channel_rule.setOnClickListener(this);
        mPopWindow = new PassMicTipsPop(getActivity());
        //        rl_bottom_close.setOnClickListener(this);

        if (AppRuntimeWorker.getShow_hide_pai_view() == 1) {
            //总开关,开启竞拍创建按钮
            SDViewUtil.show(rl_bottom_create_auction);
            if (AppRuntimeWorker.getPai_real_btn() == 1) {
                //实物开关(星店订单属于实物)
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

    public View getViewBottomMore() {
        return rl_bottom_more;
    }

    public void showChannelView(){
        SDViewUtil.show(ll_channel_layout);
    }

    public void hideChannelView(){
        SDViewUtil.hide(ll_channel_layout);
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
        } else if (v == rl_bottom_create_auction) {
            if (clickListener != null) {
                clickListener.onCLickBottomCreateAuction(v);
            }
        } else if (v == rl_bottom_msg) {
            if (clickListener != null) {
                clickListener.onClickBottomMsg(v);
            }
        } else if (v == rl_bottom_lrs) {
            if (clickListener != null) {
                clickListener.onClickBottomLrs(v);
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
        }else if (v == rl_bottom_music) {
            if (clickListener != null) {
                clickListener.onClickBottomMusic(v);
            }
        } else if (v == rl_bottom_more) {
            if (clickListener != null) {
                clickListener.onClickBottomMore(v);
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

    private void judgeOpenOrCloseShow(){
        if(mIsGameClose){
            mIsGameClose = false;
            img_game_open_close.setImageDrawable(getResources().getDrawable(R.drawable.ic_game_open));
        }else{
            mIsGameClose = true;
            img_game_open_close.setImageDrawable(getResources().getDrawable(R.drawable.ic_game_close));
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

    public interface ClickListener {
        void onClickBottomOpenSend(View v);

        void onClickBottomPodcastOrder(View v);

        void onCLickBottomCreateAuction(View v);

        void onClickBottomMsg(View v);

        void onClickBottomLrs(View v);

        void onClickBottomLrsChannel(View v);

        void onClickBottomGamePass(View v);

        void onClickBottomGameOut(View v);

        void onClickBottomGameOpenClose(View v,boolean isShow);

        void onClickBottomMusic(View v);

        void onClickBottomMore(View v);

        void onClickBottomClose(View v);

        void onClickChannelPublic(View v);

        void onClickChannelWolf(View v);

        void onClickChannelProgress(View v);

        void onClickChannelRule(View v);
    }

    public void bindData(App_pai_user_get_videoActModel actModel) {
        PaiUserGoodsDetailDataInfoModel data = actModel.getDataInfo();
        if (data != null) {
            //处于竞拍状态，隐藏创建按钮
            if (data.getStatus() == 0) {
                hideRlBottomPodcastOrderView();
            } else {
                showRlBottomPodcastOrderView();
            }
        }
    }

    public void showGamePass(){
        SDViewUtil.show(rl_bottom_game_pass);
        if(mPopWindow != null){
            rl_bottom_game_pass.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPopWindow.showPopTips(rl_bottom_game_pass, SDViewUtil.dp2px(5), 0);
                }
            }, 100);
        }
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

    //主播接收到创建竞拍商品成功后，隐藏创建按钮
    public void onEventMainThread(ECreateAuctionSuccess event) {
        hideRlBottomPodcastOrderView();
    }

    public void onEventMainThread(ELRSGameStateChange event){
        if(event.isGaming){
            judgeWolfChannel();
            SDViewUtil.show(rl_bottom_lrs_channel);
            if(LRSManager.getInstance().userIsGamer(mUserId)) {
                SDViewUtil.show(rl_bottom_game_out);
            }
            SDViewUtil.show(rl_bottom_game_open_close);
            SDViewUtil.hide(rl_bottom_lrs);
        }else{
            SDViewUtil.hide(rl_bottom_lrs_channel);
            SDViewUtil.hide(rl_bottom_game_out);
            SDViewUtil.hide(rl_bottom_game_open_close);
            SDViewUtil.show(rl_bottom_lrs);
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

    public void onEventMainThread(EImOnNewMessages event) {
        String peer = event.msg.getConversationPeer();
        if (event.msg.isLocalPost()) {

        } else {
            if (peer.equals(getLiveInfo().getGroupId())) {
                if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_FAIL) {
                    //接收流拍事件的时候重新显示创建竞拍按钮
                    showRlBottomPodcastOrderView();
                } else if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_PAY_SUCCESS) {
                    //接收支付成功事件的时候重新显示创建竞拍按钮
                    showRlBottomPodcastOrderView();
                }
            }
        }
    }

    public void hideRlBottomPodcastOrderView() {
        if (rl_bottom_create_auction != null) {
            SDViewUtil.hide(rl_bottom_create_auction);
        }
    }

    public void showRlBottomPodcastOrderView() {
        if (rl_bottom_create_auction != null) {
            SDViewUtil.show(rl_bottom_create_auction);
        }
    }
}
