package com.fanwe.live.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.event.ELRSQuiteLianMai;
import com.fanwe.live.event.ELRSSpeakOperate;
import com.fanwe.live.event.ELRSStopPlay;
import com.fanwe.live.event.ELRSWitchOperate;
import com.fanwe.live.model.LRSUserModel;
import com.fanwe.live.model.UserModel;
import com.sunday.eventbus.SDEventManager;

/**
 * 狼人杀游戏参与者列表
 */
public class LRSGamerListRecyclerAdapter extends SDSimpleRecyclerAdapter<LRSUserModel> {
    private String mUserId;
    private int mStep = LRSManager.GAME_STEP_READY;
    private int mRoomId = -1;
    private String mCreaterId;
    private boolean mCanCheck = false;//整体控制预言家是否可验身份 如果有一个人被验过了 这一轮预言家不可以再验其他人了
    private boolean mCanPoison = false;//控制女巫是否可以再毒人
    private boolean mCanHunter = false; //控制猎人是否可以射
    private boolean mCanVote = false;//控制是否可以投票
    private int mSpeakTime = 0; //连麦倒计时
    private static Handler mSpeakTimeHander = new Handler();
    private ObjectAnimator mAnimator;
    public LRSGamerListRecyclerAdapter(Activity activity) {
        super(activity);
        UserModel model = UserModelDao.query();
        if (model != null) {
            mUserId = model.getUserId();
        }
    }

    public void reSet(){
        mStep = LRSManager.GAME_STEP_READY;
        mCanCheck = false;
        mCanPoison = false;
        mCanHunter = false;
        mCanVote = false;
        cleanSpeakTime();
        stopFingerAnimitor();
    }

    public void setRoomId(int roomId) {
        mRoomId = roomId;
    }

    public void setCreaterId(String createrId){
        mCreaterId = createrId;
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_game_user;
    }

    @Override
    public void bindData(SDRecyclerViewHolder holder, int position, final LRSUserModel model) {
        RelativeLayout lr_user = holder.get(R.id.lr_user);//右边的Layout
        CircleImageView img_user_head = holder.get(R.id.img_user_head);//用户头像
        TextView tv_game_role = holder.get(R.id.tv_game_role);//用户角色
        TextView tv_index = holder.get(R.id.tv_index);//用户编号
        RelativeLayout lr_game_speak = holder.get(R.id.lr_game_speak);//发言Layout
        ImageView img_mic = holder.get(R.id.img_mic);//mic图标
        TextView tv_speak = holder.get(R.id.tv_speak);//发言标题
        TextView tv_speak_time = holder.get(R.id.tv_speak_time);//发言倒计时
        RelativeLayout lr_game_control = holder.get(R.id.lr_game_control);//游戏操作Layout
        ImageView img_game_control = holder.get(R.id.img_game_control);//可进行操作显示
        TextView tv_game_control_process = holder.get(R.id.tv_game_control_process);//操作过程
        ImageView img_lrs_finger = holder.get(R.id.img_lrs_finger);//手指图标
        controllerRight(lr_user, lr_game_control, img_game_control,
                tv_game_control_process, lr_game_speak, tv_speak, tv_speak_time,img_lrs_finger,img_mic,model);
        lr_game_speak.setOnClickListener(new onSpeakTextClickListener());
        lr_game_control.setOnClickListener(new OnControllClickListener(model));
        SDViewBinder.setTextView(tv_index, model.getIndex() + "");
        showRole(model, tv_game_role);
        if(model.getIs_alive() == LRSUserModel.ALIVE_LIVE){
            if(model.getIs_offline() == LRSUserModel.IS_OFFLINE){ //玩家退出游戏头像显示
                img_user_head.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_out));
            }else{
                SDViewBinder.setImageView(getActivity(), model.getHead_image(), img_user_head, R.drawable.ic_default_head);
            }
        }else{
            img_user_head.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_dead));
        }
        if(mUserId.equals(model.getUser_id())){
            img_user_head.setBorderWidth(SDViewUtil.dp2px(1));
        }else{
            img_user_head.setBorderWidth(0);
        }
        img_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveUserInfoDialog dialog = new LiveUserInfoDialog(getActivity(), model.getUser_id());
                dialog.show();
            }
        });
    }

    private class onSpeakTextClickListener implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (mStep){
                case LRSManager.GAME_STEP_LAST_WORDS_ONE:
                    if(!mUserId.equals(mCreaterId)) {
                        LRSManager.getInstance().doMicSpeak(mRoomId, mCreaterId);
                    }
                    setStep(LRSManager.GAME_STEP_LAST_WORDS_TWO);
                    notifyDataSetChanged();
                    SDEventManager.post(new ELRSStopPlay());
                    break;
                case LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_ONE:
                    if(!mUserId.equals(mCreaterId)) {
                        LRSManager.getInstance().doMicSpeak(mRoomId, mCreaterId);
                    }
                    setStep(LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_TWO);
                    notifyDataSetChanged();
                    SDEventManager.post(new ELRSStopPlay());
                    break;
            }
        }
    }

    private class OnControllClickListener implements View.OnClickListener {
        private LRSUserModel mUserModel;

        public OnControllClickListener(LRSUserModel model) {
            mUserModel = model;
        }

        @Override
        public void onClick(View v) {
            switch (((int) v.getTag())) {
                case LRSManager.GAME_CONTROL_KILL:
                    CommonInterface.requestLRSWolfKill(mRoomId, mUserModel.getUser_id(), new AppRequestCallback<BaseActModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {

                        }
                    });
                    break;
                case LRSManager.GAME_CONTROL_CHECK:
                    if(mCanCheck) {
                        mUserModel.setIsBeChecked(true);
                        mCanCheck = false;
                        notifyDataSetChanged();
                    }
                    break;
                case LRSManager.GAME_CONTROL_POISON:
                    if(mCanPoison) {
                        mCanPoison = false;
                        LRSManager.getInstance().dealWitchPoisonProgress(mUserId,mUserModel.getUser_id());
                        notifyDataSetChanged();
                        CommonInterface.requestLRSWitch(mRoomId, LRSManager.WITCH_POISON, mUserModel.getUser_id(), new AppRequestCallback<BaseActModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                if (rootModel.isOk()) {
                                    SDToast.showToast("毒杀" + LRSManager.getInstance().getUserIndex(mUserModel.getUser_id()) + "号玩家成功");
                                    SDEventManager.post(new ELRSWitchOperate(ELRSWitchOperate.DONE_POISON,null));
                                }
                            }
                        });
                    }
                    break;
                case LRSManager.GAME_CONTROL_HUNT:
                    if(mCanHunter){
                        mCanHunter = false;
                        LRSManager.getInstance().dealHunterProgress(mUserId, mUserModel.getUser_id());
                        notifyDataSetChanged();
                        CommonInterface.requestLRSHunter(mRoomId, 2, mUserModel.getUser_id(), new AppRequestCallback<BaseActModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                if(rootModel.isOk()){
                                    SDToast.showToast("射杀" + LRSManager.getInstance().getUserIndex(mUserModel.getUser_id()) + "号玩家成功");
                                }
                            }
                        });
                    }
                    break;
                case LRSManager.GAME_CONTROL_VOTE:
                    if(mCanVote){
                        mCanVote = false;
                        LRSManager.getInstance().dealVoteProgress(mUserId, mUserModel.getUser_id());
                        notifyDataSetChanged();
                        CommonInterface.requestLRSVote(mRoomId, mUserModel.getUser_id(), new AppRequestCallback<BaseActModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                if(rootModel.isOk()){
                                    SDToast.showToast("投票给" + LRSManager.getInstance().getUserIndex(mUserModel.getUser_id()) + "号玩家成功");
                                }
                            }
                        });
                    }
                    break;
            }
        }
    }

    public void setStep(int step) {
        mStep = step;
        switch(step){
            case LRSManager.GAME_STEP_WOLF_PROPHET:
                mCanCheck = true;
                break;
            case LRSManager.GAME_STEP_WITCH:
                mCanPoison = true;
                break;
            case LRSManager.GAME_STEP_HUNTER:
                mCanHunter = true;
                break;
            case LRSManager.GAME_STEP_VOTE:
                mCanVote = true;
                break;
        }
    }

    //控制右边用户显示信息 以及操作
    private void controllerRight(RelativeLayout lr_user, RelativeLayout lr_game_control, ImageView img_game_control,
                                 TextView tv_game_control_process,RelativeLayout lr_game_speak,TextView tv_speak,
                                 TextView tv_speak_time,ImageView img_lrs_finger,ImageView img_mic,LRSUserModel model) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lr_user.getLayoutParams();
        params.width = SDViewUtil.dp2px(35);
        lr_user.setLayoutParams(params);
        lr_user.setBackgroundDrawable(null);
        SDViewUtil.hide(lr_game_control);
        SDViewUtil.hide(tv_game_control_process);
        SDViewUtil.hide(lr_game_speak);
        SDViewUtil.hide(img_lrs_finger);
        if ((model.getIs_alive() == LRSUserModel.ALIVE_NOT_LIVE && !model.isSpeaking())|| model.getIs_offline() == LRSUserModel.IS_OFFLINE) {
            return;
        }
        switch (mStep) {
            case LRSManager.GAME_STEP_READY:
                break;
            case LRSManager.GAME_STEP_WOLF_PROPHET:
                /**
                 * 狼人和预言家行动
                 * 1)不是狼人以及不是预言家的人 会有可操作提示，根据当前用户的角色 提示杀 或者 验 如果当前用户非狼非预言家 就没有操作提示了
                 * 2）如果是狼人 点击杀之后 狼人可以看到杀过程
                 */
                if (model.getIdentity() != LRSUserModel.GAME_ROLE_WOLF) {
                    if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WOLF && LRSManager.getInstance().isSelfALive() && !LRSManager.getInstance().mIsSelfOutGame){
                        params.width = SDViewUtil.dp2px(70);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        SDViewUtil.show(lr_game_control);
                        lr_game_control.setTag(LRSManager.GAME_CONTROL_KILL);
                        img_game_control.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_control_kill));
                    }
                }
                if(model.getIdentity() != LRSUserModel.GAME_ROLE_PROPHET) {
                    if (LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_PROPHET && mCanCheck && LRSManager.getInstance().isSelfALive() && !LRSManager.getInstance().mIsSelfOutGame) {
                        if (model.isBeChecked()) {//如果被验过了 就没有验操作了
                            return;
                        }
                        params.width = SDViewUtil.dp2px(70);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        SDViewUtil.show(lr_game_control);
                        lr_game_control.setTag(LRSManager.GAME_CONTROL_CHECK);
                        img_game_control.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_control_check));
                    }
                }
                if(model.getIdentity() == LRSUserModel.GAME_ROLE_WOLF && LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WOLF && LRSManager.getInstance().isSelfALive()){
                    params.width = SDViewUtil.dp2px(35);
                    lr_user.setLayoutParams(params);
                    lr_user.setBackgroundDrawable(null);
                    if (model.isShowControl()) {
                        SDViewUtil.show(tv_game_control_process);
                        if (!TextUtils.isEmpty(model.getControlContent())) {
                            SDViewBinder.setTextView(tv_game_control_process, model.getControlContent());
                        }
                    } else {
                        SDViewUtil.hide(tv_game_control_process);
                    }
                }
                break;
            case LRSManager.GAME_STEP_WITCH:
                if(model.getIdentity() != LRSUserModel.GAME_ROLE_WITCH){
                    if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WITCH && mCanPoison && LRSManager.getInstance().isSelfALive() && !LRSManager.getInstance().mIsSelfOutGame){
                        params.width = SDViewUtil.dp2px(70);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        SDViewUtil.show(lr_game_control);
                        lr_game_control.setTag(LRSManager.GAME_CONTROL_POISON);
                        img_game_control.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_control_poison));
                    }
                }else if(model.getIdentity() == LRSUserModel.GAME_ROLE_WITCH && LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WITCH && LRSManager.getInstance().isSelfALive()){
                    params.width = SDViewUtil.dp2px(35);
                    lr_user.setLayoutParams(params);
                    lr_user.setBackgroundDrawable(null);
                    if (model.isShowControl()) {
                        SDViewUtil.show(tv_game_control_process);
                        if (!TextUtils.isEmpty(model.getControlContent())) {
                            SDViewBinder.setTextView(tv_game_control_process, model.getControlContent());
                        }
                    } else {
                        SDViewUtil.hide(tv_game_control_process);
                    }
                }
                break;
            case LRSManager.GAME_STEP_HUNTER:
                if(model.getIdentity() != LRSUserModel.GAME_ROLE_HUNTER){
                    if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_HUNTER && mCanHunter && !LRSManager.getInstance().isSelfALive() && !LRSManager.getInstance().mIsSelfOutGame){
                        params.width = SDViewUtil.dp2px(70);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        SDViewUtil.show(lr_game_control);
                        lr_game_control.setTag(LRSManager.GAME_CONTROL_HUNT);
                        img_game_control.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_control_hunt));
                    }
                }else if(model.getIdentity() == LRSUserModel.GAME_ROLE_HUNTER && LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_HUNTER && !LRSManager.getInstance().isSelfALive()){
                    params.width = SDViewUtil.dp2px(35);
                    lr_user.setLayoutParams(params);
                    lr_user.setBackgroundDrawable(null);
                    if (model.isShowControl()) {
                        SDViewUtil.show(tv_game_control_process);
                        if (!TextUtils.isEmpty(model.getControlContent())) {
                            SDViewBinder.setTextView(tv_game_control_process, model.getControlContent());
                        }
                    } else {
                        SDViewUtil.hide(tv_game_control_process);
                    }
                }
                break;
            case LRSManager.GAME_STEP_LAST_WORDS_ONE:
                if(model.isSpeaking()){
                    if(model.getUser_id().equals(mUserId)) {
                        params.width = SDViewUtil.dp2px(150);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        img_mic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_game_mic));
                        SDViewUtil.show(lr_game_speak);
                        SDViewBinder.setTextView(tv_speak, "点击连麦");
                        SDViewUtil.show(tv_speak_time);
                        SDViewUtil.show(img_lrs_finger);
                        playFingerAnimitor(img_lrs_finger);
                        showSpeakTime(tv_speak_time, model);
                    }else{
                        //不是自己 直接显示发遗言 或者 发言中  不需要显示倒计时
                        params.width = SDViewUtil.dp2px(120);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        img_mic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_speaking));
                        SDViewUtil.show(lr_game_speak);
                        SDViewBinder.setTextView(tv_speak, "发遗言");
                        SDViewUtil.hide(tv_speak_time);
                        cleanSpeakTime();
                    }
                }
                break;
            case LRSManager.GAME_STEP_LAST_WORDS_TWO:
                if(model.isSpeaking()){
                    if(model.getUser_id().equals(mUserId)) {
                        params.width = SDViewUtil.dp2px(150);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        img_mic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_speaking));
                        SDViewUtil.show(lr_game_speak);
                        SDViewBinder.setTextView(tv_speak, "发遗言");
                        SDViewUtil.show(tv_speak_time);
                        showSpeakTime(tv_speak_time, model);
                    }else{
                        //不是自己 直接显示发遗言 或者 发言中  不需要显示倒计时
                        params.width = SDViewUtil.dp2px(120);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        img_mic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_speaking));
                        SDViewUtil.show(lr_game_speak);
                        SDViewBinder.setTextView(tv_speak, "发遗言");
                        SDViewUtil.hide(tv_speak_time);
                        cleanSpeakTime();
                    }
                }
                break;
            case LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_ONE:
                if(model.isSpeaking()){
                    if(model.getUser_id().equals(mUserId)) {
                        params.width = SDViewUtil.dp2px(150);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        img_mic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_game_mic));
                        SDViewUtil.show(lr_game_speak);
                        SDViewBinder.setTextView(tv_speak, "点击连麦");
                        SDViewUtil.show(img_lrs_finger);
                        playFingerAnimitor(img_lrs_finger);
                        SDViewUtil.show(tv_speak_time);
                        showSpeakTime(tv_speak_time, model);
                    }else{
                        //不是自己 直接显示发遗言 或者 发言中  不需要显示倒计时
                        params.width = SDViewUtil.dp2px(120);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        img_mic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_speaking));
                        SDViewUtil.show(lr_game_speak);
                        SDViewBinder.setTextView(tv_speak, "发言中");
                        SDViewUtil.hide(tv_speak_time);
                        cleanSpeakTime();
                    }
                }
                break;
            case LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_TWO:
                if(model.isSpeaking()){
                    if(model.getUser_id().equals(mUserId)) {
                        params.width = SDViewUtil.dp2px(150);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        img_mic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_speaking));
                        SDViewUtil.show(lr_game_speak);
                        SDViewBinder.setTextView(tv_speak, "发言中");
                        SDViewUtil.show(tv_speak_time);
                        showSpeakTime(tv_speak_time, model);
                    }else{
                        //不是自己 直接显示发遗言 或者 发言中  不需要显示倒计时
                        params.width = SDViewUtil.dp2px(120);
                        lr_user.setLayoutParams(params);
                        lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                        img_mic.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_speaking));
                        SDViewUtil.show(lr_game_speak);
                        SDViewBinder.setTextView(tv_speak, "发言中");
                        SDViewUtil.hide(tv_speak_time);
                        cleanSpeakTime();
                    }
                }
                break;
            case LRSManager.GAME_STEP_VOTE:
                if(!model.getUser_id().equals(mUserId)&& mCanVote && LRSManager.getInstance().isSelfALive()&& !LRSManager.getInstance().mIsSelfOutGame &&
                        LRSManager.getInstance().userIsGamer(mUserId)){
                    params.width = SDViewUtil.dp2px(70);
                    lr_user.setLayoutParams(params);
                    lr_user.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.layer_blur_live_info));
                    SDViewUtil.show(lr_game_control);
                    lr_game_control.setTag(LRSManager.GAME_CONTROL_VOTE);
                    img_game_control.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lrs_control_vote));
                }else if(model.getUser_id().equals(mUserId)){
                    params.width = SDViewUtil.dp2px(35);
                    lr_user.setLayoutParams(params);
                    lr_user.setBackgroundDrawable(null);
                    if (model.isShowControl()) {
                        SDViewUtil.show(tv_game_control_process);
                        if (!TextUtils.isEmpty(model.getControlContent())) {
                            SDViewBinder.setTextView(tv_game_control_process, model.getControlContent());
                        }
                    } else {
                        SDViewUtil.hide(tv_game_control_process);
                    }
                }
                break;
        }
    }

    public void playFingerAnimitor(ImageView img_lrs_finger){
        stopFingerAnimitor();
        mAnimator = ObjectAnimator.ofFloat(img_lrs_finger, "translationX", 0.0f, -20, 0f, 0f);
        mAnimator.setDuration(800);//动画时间
        mAnimator.setInterpolator(new BounceInterpolator());//实现反复移动的效果
        mAnimator.setRepeatCount(Animation.INFINITE);//设置动画重复次数 - 无限次
        mAnimator.start();//启动动画
    }

    public void stopFingerAnimitor(){
        if(mAnimator != null && mAnimator.isRunning()){
            mAnimator.end();
            mAnimator = null;
        }
    }

    private void showSpeakTime(final TextView tv_speak_time,final LRSUserModel model){
        cleanSpeakTime();
        switch (mStep){
            case LRSManager.GAME_STEP_LAST_WORDS_ONE:
                mSpeakTime = model.getChooseLianMaiTime();
                break;
            case LRSManager.GAME_STEP_LAST_WORDS_TWO:
                mSpeakTime = model.getSpeakTime();
                break;
            case LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_ONE:
                mSpeakTime = model.getChooseLianMaiTime();
                break;
            case LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_TWO:
                mSpeakTime = model.getSpeakTime();
                break;
        }
        if(mSpeakTime >= 0){
            SDViewBinder.setTextView(tv_speak_time, mSpeakTime + "S");
            --mSpeakTime;
            if(mSpeakTime>=0){
                mSpeakTimeHander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SDViewBinder.setTextView(tv_speak_time, mSpeakTime + "S");
                        --mSpeakTime;
                        if (mSpeakTime >= 0) {
                            mSpeakTimeHander.postDelayed(this, 1 * 1000);
                        } else {
                            onSpeakTimeOut(model);
                            cleanSpeakTime();
                        }
                    }
                }, 1 * 1000);
            }else{
                cleanSpeakTime();
            }
        }
    }

    /**
     * 连麦时间已过 如果正处于选择连麦阶段 超过时间就跳过了
     */
    private void onSpeakTimeOut(LRSUserModel model){
        if(mStep == LRSManager.GAME_STEP_LAST_WORDS_ONE){
            model.setSpeaking(false);
            notifyDataSetChanged();
            CommonInterface.requestLRSPassSpeak(mRoomId, null);
            SDEventManager.post(new ELRSQuiteLianMai());
            SDEventManager.post(new ELRSSpeakOperate(""));
        }else if(mStep == LRSManager.GAME_STEP_SPEAK_ONE_BY_ONE_ONE){
            model.setSpeaking(false);
            notifyDataSetChanged();
            CommonInterface.requestLRSPassSpeak(mRoomId, null);
            SDEventManager.post(new ELRSQuiteLianMai());
            SDEventManager.post(new ELRSSpeakOperate(""));
        }else{
            model.setSpeaking(false);
            notifyDataSetChanged();
            SDEventManager.post(new ELRSQuiteLianMai());
            SDEventManager.post(new ELRSSpeakOperate(""));
        }
    }

    public void cleanSpeakTime(){
        mSpeakTimeHander.removeCallbacksAndMessages(null);
        mSpeakTime = 0;
    }

    //显示角色信息
    private void showRole(LRSUserModel model, TextView tv_game_role) {
        if (!TextUtils.isEmpty(mUserId)) {
            /**
             * 1.能看到自己 或者自己是狼 看到狼同伴
             * 2.如果自己是预言家 能看到被验过的人的身份
             */
            if (mUserId.equals(model.getUser_id()) ||
                    (LRSManager.getInstance().getSelfRole() == model.getIdentity() &&
                            LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WOLF)) {
                SDViewBinder.setTextView(tv_game_role, getUserRole(model.getIdentity()));
                Drawable drawable = getUserRoleBackDrawable(model.getIdentity());
                if (drawable != null) {
                    tv_game_role.setBackgroundDrawable(drawable);
                }
                SDViewUtil.show(tv_game_role);
            }else if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_PROPHET && model.isBeChecked()) {
                SDViewBinder.setTextView(tv_game_role, getUserRole(model.getIdentity()));
                Drawable drawable = getUserRoleBackDrawable(model.getIdentity());
                if (drawable != null) {
                    tv_game_role.setBackgroundDrawable(drawable);
                }
                SDViewUtil.show(tv_game_role);
            }else {
                SDViewUtil.hide(tv_game_role);
            }
        }
    }

    private String getUserRole(int identity) {
        String result = "";
        switch (identity) {
            case LRSUserModel.GAME_ROLE_WOLF:
                result = "狼";
                break;
            case LRSUserModel.GAME_ROLE_WITCH:
                result = "巫";
                break;
            case LRSUserModel.GAME_ROLE_PROPHET:
                result = "预";
                break;
            case LRSUserModel.GAME_ROLE_HUNTER:
                result = "猎";
                break;
            case LRSUserModel.GAME_ROLE_CIVILIAN:
                result = "民";
                break;
        }
        return result;
    }

    private Drawable getUserRoleBackDrawable(int identity) {
        Drawable result = null;
        switch (identity) {
            case LRSUserModel.GAME_ROLE_WOLF:
                result = getActivity().getResources().getDrawable(R.drawable.bg_circle_game_wolf);
                break;
            case LRSUserModel.GAME_ROLE_WITCH:
                result = getActivity().getResources().getDrawable(R.drawable.bg_circle_game_witch);
                break;
            case LRSUserModel.GAME_ROLE_PROPHET:
                result = getActivity().getResources().getDrawable(R.drawable.bg_circle_game_prophet);
                break;
            case LRSUserModel.GAME_ROLE_HUNTER:
                result = getActivity().getResources().getDrawable(R.drawable.bg_circle_game_hunter);
                break;
            case LRSUserModel.GAME_ROLE_CIVILIAN:
                result = getActivity().getResources().getDrawable(R.drawable.bg_circle_game_civilian);
                break;
        }
        return result;
    }
}
