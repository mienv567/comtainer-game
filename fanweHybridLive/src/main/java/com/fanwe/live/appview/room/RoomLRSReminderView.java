package com.fanwe.live.appview.room;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.LRSUserModel;
import com.fanwe.live.model.custommsg.CustomMsgLRS;

/**
 * 狼人杀提示view
 */
public class RoomLRSReminderView extends RoomView {

    public RoomLRSReminderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomLRSReminderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomLRSReminderView(Context context) {
        super(context);
    }

    private ImageView img_reminder;
    private int disappearTime = 2 * 1000;
    private static Handler mDisAppearHandler = new Handler();
    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_room_lrs_reminder;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        img_reminder = find(R.id.img_reminder);
    }

    public void onEventMainThread(EImOnNewMessages event) {
        if(LiveConstant.CustomMsgType.MSG_LRS == event.msg.getCustomMsgType()){
            if(!LRSManager.getInstance().mIsGaming){
                return;
            }
            CustomMsgLRS msg1 = event.msg.getCustomMsgLRS();
            switch (msg1.getStep()){
                case 4://天黑了 准备游戏了
                    mDisAppearHandler.removeCallbacksAndMessages(null);
                    img_reminder.setImageDrawable(getResources().getDrawable(R.drawable.ic_lrs_reminder_ready));
                    SDViewUtil.show(img_reminder);
                    mDisAppearHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SDViewUtil.hide(img_reminder);
                        }
                    },disappearTime);
                    break;
                case 5://天黑了 狼人和预言家开始行动了
                    mDisAppearHandler.removeCallbacksAndMessages(null);
                    if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_WOLF){
                        img_reminder.setImageDrawable(getResources().getDrawable(R.drawable.ic_lrs_reminder_wolf));
                        SDViewUtil.show(img_reminder);
                    }else if(LRSManager.getInstance().getSelfRole() == LRSUserModel.GAME_ROLE_PROPHET){
                        img_reminder.setImageDrawable(getResources().getDrawable(R.drawable.ic_lrs_reminder_prophet));
                        SDViewUtil.show(img_reminder);
                    }else{
                        img_reminder.setImageDrawable(getResources().getDrawable(R.drawable.ic_lrs_reminder_normal));
                        SDViewUtil.show(img_reminder);
                    }
                    mDisAppearHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SDViewUtil.hide(img_reminder);
                        }
                    },disappearTime);
                    break;
                case 14://天亮了
                    mDisAppearHandler.removeCallbacksAndMessages(null);
                    img_reminder.setImageDrawable(getResources().getDrawable(R.drawable.ic_lrs_reminder_daybreak));
                    SDViewUtil.show(img_reminder);
                    mDisAppearHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SDViewUtil.hide(img_reminder);
                        }
                    }, disappearTime);
                    break;
            }
        }
    }

}
