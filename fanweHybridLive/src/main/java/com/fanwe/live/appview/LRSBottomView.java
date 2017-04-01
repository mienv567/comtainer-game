package com.fanwe.live.appview;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LRSBottomDialog;
import com.fanwe.live.event.ELRSHunterOperate;
import com.fanwe.live.event.ELRSWitchOperate;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgLRS;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

/**
 * 狼人杀游戏底部弹出view
 */
public class LRSBottomView extends BaseAppView
{
    @ViewInject(R.id.ll_prepare_lrs)
    private LinearLayout ll_prepare_lrs;
    @ViewInject(R.id.tv_prepare_cancel)
    private TextView tv_prepare_cancel;
    @ViewInject(R.id.tv_prepare_confirm)
    private TextView tv_prepare_confirm;
    @ViewInject(R.id.ll_sign_lrs)
    private LinearLayout ll_sign_lrs;
    @ViewInject(R.id.tv_sign_cancel)
    private TextView tv_sign_cancel;
    @ViewInject(R.id.tv_sign_confirm)
    private TextView tv_sign_confirm;
    @ViewInject(R.id.ll_enter_lrs)
    private LinearLayout ll_enter_lrs;
    @ViewInject(R.id.tv_enter_title)
    private TextView tv_enter_title;
    @ViewInject(R.id.tv_out_confirm)
    private TextView tv_out_confirm;
    @ViewInject(R.id.tv_enter_confirm)
    private TextView tv_enter_confirm;
    @ViewInject(R.id.tv_enter_gray)
    private TextView tv_enter_gray;
    @ViewInject(R.id.ll_player_lrs)
    private LinearLayout ll_player_lrs;
    @ViewInject(R.id.tv_player_title)
    private TextView tv_player_title;
    @ViewInject(R.id.tv_player_out_confirm)
    private TextView tv_player_out_confirm;
    @ViewInject(R.id.ll_witch_save_lrs)
    private LinearLayout ll_witch_save_lrs;
    @ViewInject(R.id.tv_witch_save_title)
    private TextView tv_witch_save_title;
    @ViewInject(R.id.tv_witch_save_cancel)
    private TextView tv_witch_save_cancel;
    @ViewInject(R.id.tv_witch_save_confirm)
    private TextView tv_witch_save_confirm;
    @ViewInject(R.id.ll_witch_kill_lrs)
    private LinearLayout ll_witch_kill_lrs;
    @ViewInject(R.id.tv_witch_kill_cancel)
    private TextView tv_witch_kill_cancel;
    @ViewInject(R.id.tv_witch_kill_confirm)
    private TextView tv_witch_kill_confirm;
    @ViewInject(R.id.ll_hunter_lrs)
    private LinearLayout ll_hunter_lrs;
    @ViewInject(R.id.tv_hunter_cancel)
    private TextView tv_hunter_cancel;
    @ViewInject(R.id.tv_hunter_confirm)
    private TextView tv_hunter_confirm;
    private int mDes;
    private int mRoomId = -1;
    private boolean mIsCreater;
    private String mKillUserId;//被狼人杀害的玩家id
    private String mUserId;
    private CustomMsgLRS mCustomMsgLRS;
    private LRSBottomDialog mDialog;
    private Context mContext;
    public LRSBottomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LRSBottomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LRSBottomView(Context context,LRSBottomDialog dialog)
    {
        super(context);
        mDialog = dialog;
        mContext = context;
        init();
    }

    @Override
    protected void init()
    {
        super.init();
        UserModel model = UserModelDao.query();
        if(model != null){
            mUserId = model.getUserId();
        }
        setContentView(R.layout.view_lrs_bottom_control);
        initListener();
    }

    private void initListener(){
        tv_prepare_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialog != null ){
                    mDialog.dismiss();
                }
            }
        });
        tv_prepare_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRoomId != -1){
                    CommonInterface.requestLRSPrepare(mRoomId, new AppRequestCallback<BaseActModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            if (mDialog != null) {
                                mDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        tv_sign_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                LRSManager.getInstance().mIsJoinGame = false;
                if(mIsCreater){
                    LRSManager.getInstance().mIsShowingEnter = true;
                    LRSManager.getInstance().showEnterBottomDialog((Activity) mContext, 0, 12, mRoomId);
                }
            }
        });
        tv_sign_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRoomId != -1){
                    if(mIsCreater) {
                        LRSManager.getInstance().mIsShowingEnter = true;
                    }
                    LRSManager.getInstance().mIsJoinGame = true;
                    CommonInterface.requestLRSSign(mRoomId, new AppRequestCallback<BaseActModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            if (actModel.isOk()) {
                                if (mDialog != null) {
                                    mDialog.dismiss();
                                }
                            } else {
                                if (!TextUtils.isEmpty(actModel.getError())) {
                                    SDToast.showToast(actModel.getError());
                                }
                            }
                        }
                    });
                }
            }
        });
        tv_out_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LRSManager.getInstance().mIsCreaterOutGame = true;
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                CommonInterface.requestLRSFinish(mRoomId);
            }
        });
        tv_enter_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRoomId != -1){
                    CommonInterface.requestLRSEnter(mRoomId, new AppRequestCallback<BaseActModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            if (mDialog != null) {
                                mDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        tv_player_out_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LRSManager.getInstance().mIsJoinGame = false;
                CommonInterface.requestLRSOutBeforeStartGame(mRoomId, new AppRequestCallback<BaseActModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
                    }
                });
            }
        });
        tv_witch_save_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ELRSWitchOperate event = new ELRSWitchOperate(ELRSWitchOperate.DONE_SAVE,mCustomMsgLRS);
                event.setSave(false);
                SDEventManager.post(event);
            }
        });
        tv_witch_save_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRoomId != -1 && !TextUtils.isEmpty(mKillUserId)){
                    CommonInterface.requestLRSWitch(mRoomId, LRSManager.WITCH_SAVE, mKillUserId, new AppRequestCallback<BaseActModel>() {
                        @Override
                        protected void onSuccess(SDResponse sdResponse) {
                            ELRSWitchOperate event = new ELRSWitchOperate(ELRSWitchOperate.DONE_SAVE,mCustomMsgLRS);
                            event.setSave(true);
                            SDEventManager.post(event);
                        }
                    });
                }
            }
        });
        tv_witch_kill_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SDEventManager.post(new ELRSWitchOperate(ELRSWitchOperate.DONE_POISON,mCustomMsgLRS));
            }
        });
        tv_witch_kill_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SDEventManager.post(new ELRSWitchOperate(ELRSWitchOperate.SURE_POISON, mCustomMsgLRS));
            }
        });
        tv_hunter_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SDEventManager.post(new ELRSHunterOperate(ELRSHunterOperate.NOT_HUNTE, mCustomMsgLRS));
            }
        });
        tv_hunter_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SDEventManager.post(new ELRSHunterOperate(ELRSHunterOperate.HUNTE, mCustomMsgLRS));
            }
        });
    }

    public void setRoomId(int roomId){
        mRoomId = roomId;
    }

    public void setMsg(CustomMsgLRS msg){
        mCustomMsgLRS = msg;
    }

    public void setKillUserId(String userId){
        mKillUserId = userId;
    }

    public void setDes(int des){
        mDes = des;
        hideAll();
        switch (mDes){
            case LRSBottomDialog.SHOW_FOR_PREPARE:
                SDViewUtil.show(ll_prepare_lrs);
                break;
            case LRSBottomDialog.SHOW_FOR_SIGN:
                SDViewUtil.show(ll_sign_lrs);
                break;
            case LRSBottomDialog.SHOW_FOR_ENTER:
                SDViewUtil.show(ll_enter_lrs);
                break;
            case LRSBottomDialog.SHOW_FOR_PLAYER:
                SDViewUtil.show(ll_player_lrs);
                break;
            case LRSBottomDialog.SHOW_FOR_WITCH_SAVE:
                if(mKillUserId.equals(mUserId)){
                    tv_witch_save_title.setText(getResources().getString(R.string.lrs_witch_save_self));
                }else{
                    tv_witch_save_title.setText(String.format(getResources().getString(R.string.lrs_witch_save),LRSManager.getInstance().getUserIndex(mKillUserId)+""));
                }
                SDViewUtil.show(ll_witch_save_lrs);
                break;
            case LRSBottomDialog.SHOW_FOR_WITCH_POISON:
                SDViewUtil.show(ll_witch_kill_lrs);
                break;
            case LRSBottomDialog.SHOW_FOR_HUNTER:
                SDViewUtil.show(ll_hunter_lrs);
                break;
        }
    }

    public int getDes(){
        return mDes;
    }

    /**
     * @param num 目前人数
     * @param enterNum 进入游戏最低规定人数
     */
    public void showEnterBtn(int num,int enterNum){
        if(num < enterNum){
            SDViewUtil.hide(tv_enter_confirm);
            SDViewUtil.show(tv_enter_gray);
            setEnterTitleNotEnough(num);
        }else{
            SDViewUtil.hide(tv_enter_gray);
            SDViewUtil.show(tv_enter_confirm);
            setEnterTitle(num);
        }
    }

    /**
     * 玩家显示报名人数
     * @param num
     */
    public void showPlayerTitle(int num){
        tv_player_title.setText(String.format(getResources().getString(R.string.lrs_player),num));
    }


    public void setEnterTitleNotEnough(int num){
        tv_enter_title.setText(String.format(getResources().getString(R.string.lrs_enter_game_not_enough),num));
    }

    public void setEnterTitle(int num){
        tv_enter_title.setText(String.format(getResources().getString(R.string.lrs_enter_game),num));
    }

    public void setIsCreater(boolean isCreater){
        mIsCreater = isCreater;
    }

    private void hideAll(){
        SDViewUtil.hide(ll_prepare_lrs);
        SDViewUtil.hide(ll_sign_lrs);
        SDViewUtil.hide(ll_enter_lrs);
        SDViewUtil.hide(ll_player_lrs);
        SDViewUtil.hide(ll_witch_save_lrs);
        SDViewUtil.hide(ll_witch_kill_lrs);
        SDViewUtil.hide(ll_hunter_lrs);
    }

}
