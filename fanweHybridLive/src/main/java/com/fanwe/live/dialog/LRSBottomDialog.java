package com.fanwe.live.dialog;

import android.app.Activity;

import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.appview.LRSBottomView;
import com.fanwe.live.model.custommsg.CustomMsgLRS;


public class LRSBottomDialog extends SDDialogBase {

    public static final int SHOW_FOR_PREPARE = 0 ; //确定是否开启游戏
    public static final int SHOW_FOR_SIGN = 1 ; //报名
    public static final int SHOW_FOR_ENTER = 2 ; //开始游戏
    public static final int SHOW_FOR_PLAYER = 3;//玩家看到正在参与游戏的人数
    public static final int SHOW_FOR_WITCH_SAVE = 4; //女巫救人
    public static final int SHOW_FOR_WITCH_POISON = 5;//女巫毒人
    public static final int SHOW_FOR_HUNTER = 6;//猎人开枪
    private LRSBottomView mLrsBottomView;
    public LRSBottomDialog(Activity activity) {
        super(activity);
        init();
    }

    private void init() {
        mLrsBottomView = new LRSBottomView(getOwnerActivity(),this);
        setContentView(mLrsBottomView);
        SDViewUtil.setViewHeight(mLrsBottomView, SDViewUtil.dp2px(80));
        paddingLeft(0);
        paddingRight(0);
        paddingBottom(0);
    }

    public void setDes(int des){
        if(mLrsBottomView != null){
            mLrsBottomView.setDes(des);
        }
    }

    public int getDes(){
        if(mLrsBottomView != null){
            return mLrsBottomView.getDes();
        }
        return -1;
    }

    public void setRoomId(int roomId){
        if(mLrsBottomView != null){
            mLrsBottomView.setRoomId(roomId);
        }
    }

    public void setEnterNumber(int num,int enterNum){
        if(mLrsBottomView != null){
            mLrsBottomView.showEnterBtn(num, enterNum);
        }
    }

    public void setPlayerNumber(int num){
        if(mLrsBottomView != null){
            mLrsBottomView.showPlayerTitle(num);
        }
    }

    public void setIsCreater(boolean isCreater){
        if(mLrsBottomView != null){
            mLrsBottomView.setIsCreater(isCreater);
        }
    }

    public void setKillUserId(String userId){
        if(mLrsBottomView != null){
            mLrsBottomView.setKillUserId(userId);
        }
    }

    public void setMsg(CustomMsgLRS msg){
        if(mLrsBottomView != null){
            mLrsBottomView.setMsg(msg);
        }
    }

}
