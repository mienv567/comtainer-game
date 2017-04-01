package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.R;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.CustomMsgViewerQuit;
import com.fanwe.live.view.LiveViewerJoinRoomView;

import java.util.Iterator;
import java.util.LinkedList;

public class RoomViewerJoinRoomView extends RoomLooperBackgroundView<CustomMsgViewerJoin>
{
    public RoomViewerJoinRoomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomViewerJoinRoomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomViewerJoinRoomView(Context context)
    {
        super(context);
    }

    private static final long DURATION_LOOPER = 1000;

    private LiveViewerJoinRoomView view_viewer_join;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_viewer_join_room;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();

        view_viewer_join = find(R.id.view_viewer_join);
    }

    public void onEventMainThread(final EImOnNewMessages event)
    {
        SDHandlerManager.getBackgroundHandler().post(new Runnable()
        {
            @Override
            public void run()
            {
                String peer = event.msg.getConversationPeer();
                if (peer.equals(getLiveInfo().getGroupId()))
                {
                    dealViewerJoinQuitMsg(event);
                }
            }
        });
    }

    private void dealViewerJoinQuitMsg(EImOnNewMessages event)
    {
        if (CustomMsgType.MSG_VIEWER_JOIN == event.msg.getCustomMsgType())
        {
            CustomMsgViewerJoin msg = event.msg.getCustomMsgViewerJoin();
            if (msg.getSender().isSuperUser())
            {
                if (queue.contains(msg))
                {
                    // 不处理
                } else
                {
                    if (msg.equals(view_viewer_join.getMsg()) && view_viewer_join.isPlaying())
                    {
                        // 不处理
                    } else
                    {
                        offerModel(msg);
                    }
                }
            }
        } else if (CustomMsgType.MSG_VIEWER_QUIT == event.msg.getCustomMsgType())
        {
            CustomMsgViewerQuit msg = event.msg.getCustomMsgViewerQuit();
            if (msg.getSender().isProUser())
            {
                // TODO 从队列中移除
                Iterator<CustomMsgViewerJoin> it = queue.iterator();
                while (it.hasNext())
                {
                    CustomMsgViewerJoin item = it.next();
                    if (msg.getSender().equals(item.getSender()))
                    {
                        it.remove();
                    }
                }
            }
        }
    }

    @Override
    protected void startLooper(long period)
    {
        super.startLooper(DURATION_LOOPER);
    }

    @Override
    protected void looperWork(LinkedList<CustomMsgViewerJoin> queue)
    {
        if (view_viewer_join.canPlay())
        {
            view_viewer_join.playMsg(queue.poll());
        }
    }
}
