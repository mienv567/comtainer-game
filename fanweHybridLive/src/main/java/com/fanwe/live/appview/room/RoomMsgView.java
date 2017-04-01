package com.fanwe.live.appview.room;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveMsgRecyclerAdapter;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.event.ERequestFollowSuccess;
import com.fanwe.live.model.custommsg.CustomMsgLight;
import com.fanwe.live.model.custommsg.CustomMsgLiveMsg;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.MsgModel;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import java.util.List;

/**
 * 聊天消息
 */
public class RoomMsgView extends RoomView
{

    public RoomMsgView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomMsgView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomMsgView(Context context)
    {
        super(context);
    }

    private static final int MAX_MSG_NUMBER = 200;

    /**
     * 当直播间的人数超过这个数量，则不再显示普通用户加入信息
     */
    private static final int MAX_VIEWER_JOIN_TIP_NUMBER = 200;

    private RecyclerView lv_content;
    private LiveMsgRecyclerAdapter adapter;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_msg;
    }

    @Override
    protected void baseConstructorInit()
    {
        super.baseConstructorInit();
        lv_content = find(R.id.lv_content);

        adapter = new LiveMsgRecyclerAdapter(getActivity());
        lv_content.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_content.setAdapter(adapter);

        initLiveMsg();
    }

    public void initLiveMsg()
    {
        InitActModel model = InitActModelDao.query();
        if (model != null)
        {
            List<CustomMsgLiveMsg> listMsg = model.getListmsg();
            if (!SDCollectionUtil.isEmpty(listMsg))
            {
                for (CustomMsgLiveMsg msg : listMsg)
                {
                    MsgModel msgModel = msg.parseToMsgModel();
                    if (msgModel != null)
                    {
                        adapter.getData().add(msgModel);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void clearRoomMsg()
    {
        adapter.clearData();
    }

    public void onEventMainThread(ERequestFollowSuccess event)
    {
        if (event.userId.equals(getLiveInfo().getCreaterId()))
        {
            if (event.actModel.getRelationship() == 1)
            {
                final CustomMsgLiveMsg msg = new CustomMsgLiveMsg();
                msg.setDesc(msg.getSender().getNickName() + " "+ SDResourcesUtil.getString(R.string.followed_anchor));
                IMHelper.sendMsgGroup(getLiveInfo().getGroupId(), msg, new TIMValueCallBack<TIMMessage>()
                {
                    @Override
                    public void onError(int i, String s)
                    {
                    }

                    @Override
                    public void onSuccess(TIMMessage timMessage)
                    {
                        IMHelper.postMsgLocal(msg, getLiveInfo().getGroupId());
                    }
                });
            }
        }
    }

    public void onEventMainThread(final EImOnNewMessages event)
    {
        String peer = event.msg.getConversationPeer();
        if (peer != null && peer.equals(getLiveInfo().getGroupId()))
        {
            MsgModel msg = event.msg;
            if (msg.isLiveMsg())
            {
                if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_LIGHT)
                {
                    CustomMsgLight msgLight = msg.getCustomMsgLight();
                    if (msgLight.getShowMsg() == 0)
                    {
                        return;
                    }
                }
                else if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_VIEWER_JOIN)
                {
                    CustomMsgViewerJoin msgViewerJoin = msg.getCustomMsgViewerJoin();
                    if (!msgViewerJoin.getSender().isProUser())
                    {
//                        if (QavsdkControl.getInstance().getMemberCount() > MAX_VIEWER_JOIN_TIP_NUMBER)
//                        {
//                            return;
//                        }
                    }
                }
                addModel(msg);
            }
        }
    }

    private void addModel(MsgModel model)
    {
        adapter.appendData(model);
        removeBeyondModel();
        if (lv_content.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
        {
            lv_content.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    private void removeBeyondModel()
    {
        int size = adapter.getItemCount();
        if (size > MAX_MSG_NUMBER)
        {
            adapter.removeData(0);
            LogUtil.i("removeBeyondModel:" + adapter.getItemCount());
        }
    }
}
