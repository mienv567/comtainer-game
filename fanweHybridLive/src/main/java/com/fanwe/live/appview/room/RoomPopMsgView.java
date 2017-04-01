package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.R;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgPopMsg;
import com.fanwe.live.model.custommsg.CustomMsgText;
import com.fanwe.live.utils.LogUtils;
import com.fanwe.live.view.LivePopMsgView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 弹幕消息
 *
 * @author Administrator
 * @date 2016-5-20 下午5:21:09
 */
public class RoomPopMsgView extends RoomLooperBackgroundView<CustomMsgPopMsg>
{
    public RoomPopMsgView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomPopMsgView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomPopMsgView(Context context)
    {
        super(context);
    }

    private static final long DURATION_LOOPER = 500;

    private LivePopMsgView view_pop_msg0;
    private LivePopMsgView view_pop_msg1;

    private List<LivePopMsgView> listView;


    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_pop_msg;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        view_pop_msg0 = find(R.id.view_pop_msg0);
        view_pop_msg1 = find(R.id.view_pop_msg1);

        listView = new ArrayList<>();

        listView.add(view_pop_msg0);
        listView.add(view_pop_msg1);
    }

    public void onEventMainThread(final EImOnNewMessages event)
    {
        SDHandlerManager.getBackgroundHandler().post(new Runnable()
        {
            @Override
            public void run()
            {
                String peer = event.msg.getConversationPeer();
//                if (!event.msg.isLocalPost())
//                {
//
//                } else
//                {
                    if (peer.equals(getLiveInfo().getGroupId()))
                    {

                        if (CustomMsgType.MSG_POP_MSG == event.msg.getCustomMsgType())
                        {
                            CustomMsgPopMsg msg = event.msg.getCustomMsgPopMsg();
                            offerModel(msg);
                        } else if (CustomMsgType.MSG_TEXT == event.msg.getCustomMsgType()) {
                            CustomMsgText txt = event.msg.getCustomMsgText();
                            CustomMsgPopMsg msg = new CustomMsgPopMsg();
                            msg.setDesc(txt.getText());
                            msg.setSender(txt.getSender());
                            msg.setType(CustomMsgType.MSG_TEXT);
                            offerModel(msg);
                        }
                    }
//                }
            }
        });
    }

    @Override
    protected void startLooper(long period)
    {
        super.startLooper(DURATION_LOOPER);
    }

    @Override
    public void looperWork(LinkedList<CustomMsgPopMsg> queue)
    {
        for (LivePopMsgView item : listView)
        {
            if (item.canPlay())
            {
                item.playMsg(queue.poll());
            }
        }
    }

}
