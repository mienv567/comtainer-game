package com.fanwe.live.appview.room;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LRSGamerListRecyclerAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ELRSGameStateChange;
import com.fanwe.live.event.ELRSHunterOperate;
import com.fanwe.live.event.ELRSQuiteLianMai;
import com.fanwe.live.event.ELRSSpeakOperate;
import com.fanwe.live.event.ELRSSpeakPass;
import com.fanwe.live.event.ELRSWitchOperate;
import com.fanwe.live.model.LRSUserModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgLRS;
import com.sunday.eventbus.SDEventManager;

import java.util.ArrayList;

/**
 * 狼人杀用户view
 */
public class RoomLRSUserView extends RoomView {
    public RoomLRSUserView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomLRSUserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomLRSUserView(Context context) {
        super(context);
    }

    private SDRecyclerView list_view;
    private LRSGamerListRecyclerAdapter adapter;
    private String mUserId;
    private static Handler mWitchAndHunterTime = new Handler();

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_room_lrs_user;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        UserModel model = UserModelDao.query();
        if(model != null){
            mUserId = model.getUserId();
        }
        list_view = find(R.id.list_view);
        adapter = new LRSGamerListRecyclerAdapter(getActivity());
        list_view.setAdapter(adapter);
        adapter.setRoomId(getLiveInfo().getRoomId());
        adapter.setCreaterId(getLiveInfo().getCreaterId());
    }

    public void showOrHide(boolean isShow){
        if(isShow){
            SDViewUtil.show(list_view);
        }else{
            SDViewUtil.hide(list_view);
        }
    }

    public void cleanData(){
        if(adapter != null){
            adapter.reSet();
            adapter.updateData(new ArrayList<LRSUserModel>());
        }
    }

    public void initData(){
        adapter.updateData(LRSManager.getInstance().getGameUsers());
    }

    public void onEventMainThread(EImOnNewMessages event) {
        if(LiveConstant.CustomMsgType.MSG_LRS == event.msg.getCustomMsgType()){
            if(!LRSManager.getInstance().mIsGaming){
                return;
            }
            CustomMsgLRS msg1 = event.msg.getCustomMsgLRS();
            switch (msg1.getStep()){
                case 4://获得用户列表
                    if(!getLiveInfo().isCreater()) {
                        LRSManager.getInstance().playGameMusic(LRSManager.GAME_STEP_READY, null);
                    }
                    if(LRSManager.getInstance().getGameUsers().size() <= 0) {
                        LRSManager.getInstance().setGameUsers(msg1.getMembers());
                    }
                    adapter.updateData(LRSManager.getInstance().getGameUsers());
                    break;
                case 5://狼人和预言家活动时间
                    LRSManager.getInstance().mIsDark = true;
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    LRSManager.getInstance().cleanControlContent();
                    if(!getLiveInfo().isCreater()) {
                        LRSManager.getInstance().playGameMusic(LRSManager.GAME_STEP_WOLF_PROPHET, null);
                    }
                    SDEventManager.post(new ELRSSpeakOperate(""));
                    adapter.setStep(LRSManager.GAME_STEP_WOLF_PROPHET);
                    adapter.notifyDataSetChanged();
                    break;
                case 7://狼人投票过程
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    LRSManager.getInstance().dealWolfKillProgress(msg1.getUser_id(),msg1.getKill_user_id());
                    adapter.notifyDataSetChanged();
                    break;
                case 6://女巫活动
                    if(LRSManager.getInstance().getSelfRole() != LRSUserModel.GAME_ROLE_WITCH){
                        adapter.setStep(LRSManager.GAME_STEP_WITCH);
                        adapter.notifyDataSetChanged();
                    }
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    LRSManager.getInstance().cleanControlContent();
                    if(!getLiveInfo().isCreater() && LRSManager.getInstance().userIsGamer(mUserId)) {
                        LRSManager.getInstance().playGameMusic(LRSManager.GAME_STEP_WITCH, null);
                    }
                    if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WITCH){
                        //先处理 解药逻辑 再处理 毒药逻辑
                        if(msg1.getAntidote() == 1 && !msg1.getWolf_kill_user_id().equals("0")){
                            LRSManager.getInstance().showWitchBottomDialogForSave(getActivity(), getLiveInfo().getRoomId(), msg1);
                            mWitchAndHunterTime.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (LRSManager.getInstance().isShowing()) {
                                        LRSManager.getInstance().dismiss();
                                        CommonInterface.requestLRSWItchFinish(getLiveInfo().getRoomId());
                                    }
                                }
                            }, msg1.getWait1() * 1000);
                        }else if(msg1.getPoison() == 1){
                            LRSManager.getInstance().showWitchButtomDialogForPoison(getActivity(),getLiveInfo().getRoomId(),msg1);
                            mWitchAndHunterTime.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (LRSManager.getInstance().isShowing()) {
                                        LRSManager.getInstance().dismiss();
                                        CommonInterface.requestLRSWItchFinish(getLiveInfo().getRoomId());
                                    }
                                }
                            }, msg1.getWait2() * 1000);
                        }
                    }
                    break;
                case 8://猎人活动
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    LRSManager.getInstance().cleanControlContent();
                    if(!getLiveInfo().isCreater() && LRSManager.getInstance().userIsGamer(mUserId)) {
                        LRSManager.getInstance().playGameMusic(LRSManager.GAME_STEP_HUNTER, null);
                    }
                    if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_HUNTER){
                        LRSManager.getInstance().showHunterButtomDialog(getActivity(),msg1);
                        mWitchAndHunterTime.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (LRSManager.getInstance().isShowing()) {
                                    LRSManager.getInstance().dismiss();
                                    CommonInterface.requestLRSHunter(getLiveInfo().getRoomId(), 1, null, null);
                                }
                            }
                        }, msg1.getWait1() * 1000);
                    }
                    break;
                case 13://获得死亡信息
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    LRSManager.getInstance().dealDeadUsers(msg1.getDead());
//                    adapter.setStep(LRSManager.GAME_STEP_DEAD);
                    adapter.notifyDataSetChanged();
                    break;
                case 14://天亮了
                    LRSManager.getInstance().mIsDark = false;
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    if(!getLiveInfo().isCreater()) {
                        LRSManager.getInstance().playGameMusic(LRSManager.GAME_STEP_DAYBREAK, null);
                    }
                    adapter.setStep(LRSManager.GAME_STEP_DAYBREAK);
                    adapter.notifyDataSetChanged();
                    break;
                case 9://遗言环节
                    LRSManager.getInstance().cleanControlContent();
                    if(!getLiveInfo().isCreater()) {
                        LRSManager.getInstance().playGameMusic(LRSManager.GAME_STEP_LAST_WORDS_ONE, msg1.getUser_id());
                    }
                    if(mUserId.equals(msg1.getUser_id())){
                        LRSManager.getInstance().mIsSelfLastWords = true;
                    }else{
                        SDEventManager.post(new ELRSQuiteLianMai());//ios主播没法关闭之前连麦的 所以如果不是自己发言 通知关闭自己的连麦以防万一
                        LRSManager.getInstance().mIsSelfLastWords = false;
                    }
                    SDEventManager.post(new ELRSSpeakOperate(msg1.getUser_id()));
                    LRSManager.getInstance().dealSpeak(msg1.getUser_id(), msg1.getPass_wait(), msg1.getWait());
                    adapter.setStep(LRSManager.GAME_STEP_LAST_WORDS_ONE);
                    adapter.notifyDataSetChanged();
                    break;
                case 10://挨个发言时间
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    LRSManager.getInstance().cleanControlContent();
                    if(!getLiveInfo().isCreater()) {
                        LRSManager.getInstance().playGameMusic(LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_ONE, msg1.getUser_id());
                    }
                    if(!mUserId.equals(msg1.getUser_id())){
                        SDEventManager.post(new ELRSQuiteLianMai());//ios主播没法关闭之前连麦的 所以如果不是自己发言 通知关闭自己的连麦以防万一
                    }
                    SDEventManager.post(new ELRSSpeakOperate(msg1.getUser_id()));
                    LRSManager.getInstance().dealSpeak(msg1.getUser_id(), msg1.getPass_wait(), msg1.getWait());
                    adapter.setStep(LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_ONE);
                    adapter.notifyDataSetChanged();
                    break;
                case 11://投票环节
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    LRSManager.getInstance().cleanControlContent();
                    if(!getLiveInfo().isCreater() && LRSManager.getInstance().userIsGamer(mUserId)) {
                        LRSManager.getInstance().playGameMusic(LRSManager.GAME_STEP_VOTE, null);
                    }
                    adapter.setStep(LRSManager.GAME_STEP_VOTE);
                    adapter.notifyDataSetChanged();
                    SDEventManager.post(new ELRSQuiteLianMai());
                    break;
                case 15://有人退出游戏
                    LRSManager.getInstance().mIsSelfLastWords = false;
                    LRSManager.getInstance().dealUserOutGame(msg1.getUser_id());
//                    adapter.setStep(LRSManager.GAME_STEP_OUT);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void cleanWitchAndHunterHandler(){
        mWitchAndHunterTime.removeCallbacksAndMessages(null);
    }

    public void onEventMainThread(ELRSWitchOperate event){
        switch (event.getStep()){
            case ELRSWitchOperate.DONE_SAVE:
                cleanWitchAndHunterHandler();
                if(event.getMsg() != null){
                    if(event.getMsg().getPoison() == 1){
                        if(!event.getSave()){
                            LRSManager.getInstance().dealDeadUser(event.getMsg().getWolf_kill_user_id());
                            adapter.notifyDataSetChanged();
                        }
                        LRSManager.getInstance().showWitchButtomDialogForPoison(getActivity(),getLiveInfo().getRoomId(),event.getMsg());
                        mWitchAndHunterTime.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (LRSManager.getInstance().isShowing()) {
                                    LRSManager.getInstance().dismiss();
                                    CommonInterface.requestLRSWItchFinish(getLiveInfo().getRoomId());
                                }
                            }
                        }, event.getMsg().getWait2() * 1000);
                    }else{
                        LRSManager.getInstance().dismiss();
                        CommonInterface.requestLRSWItchFinish(getLiveInfo().getRoomId());
                    }
                }
                break;
            case ELRSWitchOperate.SURE_POISON:
                adapter.setStep(LRSManager.GAME_STEP_WITCH);
                adapter.notifyDataSetChanged();
                cleanWitchAndHunterHandler();
                LRSManager.getInstance().dismiss();
                break;
            case ELRSWitchOperate.DONE_POISON:
                cleanWitchAndHunterHandler();
                LRSManager.getInstance().dismiss();
                CommonInterface.requestLRSWItchFinish(getLiveInfo().getRoomId());
                break;
        }
    }

    public void onEventMainThread(ELRSHunterOperate event){
        LRSManager.getInstance().dismiss();
        switch (event.getStep()){
            case ELRSHunterOperate.NOT_HUNTE:
                cleanWitchAndHunterHandler();
                CommonInterface.requestLRSHunter(getLiveInfo().getRoomId(), 1, null, null);
                break;
            case ELRSHunterOperate.HUNTE:
                cleanWitchAndHunterHandler();
                adapter.setStep(LRSManager.GAME_STEP_HUNTER);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public void onEventMainThread(ELRSGameStateChange event){
        if(!event.isGaming()){
            adapter.notifyDataSetChanged();
        }
    }

    public void onEventMainThread(ELRSSpeakPass event){
        if(adapter != null){
            adapter.cleanSpeakTime();
        }
    }

}
