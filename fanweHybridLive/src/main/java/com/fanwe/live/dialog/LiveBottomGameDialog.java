package com.fanwe.live.dialog;

import android.app.Activity;

import com.fanwe.library.dialog.SDDialogBase;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.appview.LiveBottomGameView;


public class LiveBottomGameDialog extends SDDialogBase {

    public LiveBottomGameDialog(Activity activity,int roomId) {
        super(activity);
        init(roomId);
    }

    private void init(int roomId) {
        LiveBottomGameView view = new LiveBottomGameView(getOwnerActivity(),this);
        setContentView(view);
        view.setRoomId(roomId);
        SDViewUtil.setViewHeight(view, SDViewUtil.dp2px(80));
        paddingLeft(0);
        paddingRight(0);
        paddingBottom(0);
    }

}
