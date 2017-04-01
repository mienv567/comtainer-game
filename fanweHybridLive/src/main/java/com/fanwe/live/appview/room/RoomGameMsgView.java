package com.fanwe.live.appview.room;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveMsgRecyclerAdapter;
import com.fanwe.live.dialog.LRSVoteDialog;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.custommsg.CustomMsgLRSProgress;
import com.fanwe.live.model.custommsg.CustomMsgLRSRule;
import com.fanwe.live.model.custommsg.CustomMsgLight;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.MsgModel;

import java.util.List;

/**
 * 聊天消息 - 游戏相关 公频 狼人频道
 */
public class RoomGameMsgView extends RoomView
{

    public RoomGameMsgView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomGameMsgView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomGameMsgView(Context context)
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
    private String mGroupId;
    private boolean mIsForGameProgress = false;
    private boolean mIsForGameRule = false;
    private LRSVoteDialog mDialog;
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
        adapter.setIsForLRS(true);
        lv_content.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_content.setAdapter(adapter);

    }

    public void initData(List<String> list){
        for(String str : list){
            CustomMsgLRSProgress cus = new CustomMsgLRSProgress();
            cus.setDesc(str);
            adapter.getData().add(cus.parseToMsgModel());
        }
        adapter.notifyDataSetChanged();
    }

    public void initRule(String rule){
        mIsForGameRule = true;
        CustomMsgLRSRule cus = new CustomMsgLRSRule();
        cus.setDesc(rule);
        adapter.getData().add(cus.parseToMsgModel());
        adapter.notifyDataSetChanged();
    }

    public void setGroupId(String groupId){
        mGroupId = groupId;
    }

    public void setIsForGameProgress(boolean isForGameProgress){
        mIsForGameProgress = isForGameProgress;
    }

    public void clearRoomMsg()
    {
        adapter.clearData();
    }


    public void onEventMainThread(final EImOnNewMessages event)
    {
        if(mIsForGameRule){
            return;
        }
        if(TextUtils.isEmpty(mGroupId)){
            return;
        }
        String peer = event.msg.getConversationPeer();
        if (peer != null && peer.equals(mGroupId))
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
                        // 方便调试
//                        if (QavsdkControl.getInstance().getMemberCount() > MAX_VIEWER_JOIN_TIP_NUMBER)
//                        {
//                            return;
//                        }
                    }
                }
                if(mIsForGameProgress){
                    if(msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_LRS_PROGRESS){
                        addModel(msg);
                        CustomMsgLRSProgress msgLRSProgress = msg.getCustomMsgLRSProgress();
                        if(msgLRSProgress.getWindow() == 1){
                            dissmissVoteDialog();
                            mDialog = new LRSVoteDialog(getActivity(),msgLRSProgress.getDesc());
                            mDialog.show();
                        }
                    }
                }else{
                    addModel(msg);
                }
            }
        }
    }

    public void dissmissVoteDialog(){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
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
