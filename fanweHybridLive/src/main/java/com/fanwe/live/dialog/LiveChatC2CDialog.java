package com.fanwe.live.dialog;

import android.app.Activity;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.appview.LiveChatC2CView;
import com.fanwe.live.event.ELivePrivateChatDialogDissmis;
import com.fanwe.live.model.ItemLiveChatListModel;

/**
 * Created by Administrator on 2016/7/16.
 */
public class LiveChatC2CDialog extends LiveBaseDialog
{
    public LiveChatC2CDialog(Activity activity)
    {
        super(activity);
        init();
    }

    private void init()
    {
        LiveChatC2CView liveChatC2CView = new LiveChatC2CView(getOwnerActivity());
        liveChatC2CView.setClickListener(new LiveChatC2CView.ClickListener()
        {
            @Override
            public void onClickBack()
            {
                dismiss();
            }
        });
        liveChatC2CView.setOnVItemClickListener(new LiveChatC2CView.OnVItemClickListener()
        {
            @Override
            public void onVItemClickListener(ItemLiveChatListModel item, long id)
            {
                LivePrivateChatDialog dialog = new LivePrivateChatDialog(getOwnerActivity(), item.getUserId());
                dialog.showBottom();
            }
        });
        setContentView(liveChatC2CView);
        liveChatC2CView.initDataThread();
        SDViewUtil.setViewHeight(liveChatC2CView, SDViewUtil.getScreenHeight() / 2);

        setCanceledOnTouchOutside(true);
        paddingLeft(0);
        paddingRight(0);
        paddingBottom(0);
    }


    /*私人聊天窗口点击外面关闭的时候关闭当前私聊*/
    public void onEventMainThread(ELivePrivateChatDialogDissmis event)
    {
        dismiss();
    }
}
