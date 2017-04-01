package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.live.R;
import com.fanwe.live.control.LRSManager;
import com.fanwe.live.dialog.LiveBottomGameDialog;

import org.xutils.view.annotation.ViewInject;

/**
 * 狼人杀游戏底部弹出view
 */
public class LiveBottomGameView extends BaseAppView
{

    @ViewInject(R.id.ll_wolf_game)
    private LinearLayout ll_wolf_game;
    private int mRoomId;
    private LiveBottomGameDialog mDialog;
    public LiveBottomGameView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveBottomGameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveBottomGameView(Context context,LiveBottomGameDialog dialog)
    {
        super(context);
        mDialog = dialog;
        init();
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_live_bottom_game);
        initListener();
    }

    private void initListener(){
        ll_wolf_game.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialog != null ){
                    mDialog.dismiss();
                }
                LRSManager.getInstance().showPrepareBottomDialog(getActivity(), mRoomId);
            }
        });
    }

    public void setRoomId(int roomId){
        mRoomId = roomId;
    }

}
