package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;

/**
 * Created by Administrator on 2016/8/5.
 */
public class RoomBottomView extends RoomView
{
    protected TextView tv_unread_number;

    public RoomBottomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomBottomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomBottomView(Context context)
    {
        super(context);
    }

    public void onEventMainThread(ERefreshMsgUnReaded event)
    {
        setUnreadMessageModel(event.model);
    }

    public void setUnreadMessageModel(TotalConversationUnreadMessageModel model)
    {
        if (tv_unread_number != null)
        {
            SDViewUtil.hide(tv_unread_number);
            if (model != null && model.getTotalUnreadNum() > 0)
            {
                SDViewUtil.show(tv_unread_number);
                tv_unread_number.setText(model.getStr_totalUnreadNum());
            }
        }
    }
}
