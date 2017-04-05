package com.fanwe.live.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabFollowAdapter;
import com.fanwe.live.adapter.LiveTabFollowLiveAdapter;
import com.fanwe.live.adapter.LiveTabFollowRecommendAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.Index_focus_videoActModel;
import com.fanwe.live.model.LivePlaybackModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgLiveStopped;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.view.SDProgressPullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 关注直播列表
 *
 * @author Administrator
 * @date 2016-7-2 上午11:28:13
 */
public class LiveTabFollowFragment extends LiveTabBaseFragment
{

    @ViewInject(R.id.lv_content)
    protected SDProgressPullToRefreshScrollView lv_content;

    @ViewInject(R.id.lv_content_0)
    protected ListView lv_content_0;

    @ViewInject(R.id.lv_content_1)
    protected ListView lv_content_1;

    @ViewInject(R.id.lv_content_2)
    protected ListView lv_content_2;

    @ViewInject(R.id.ll_top)
    protected LinearLayout ll_top;

    @ViewInject(R.id.ll_recommend_top)
    protected LinearLayout ll_recommend_top;

    @ViewInject(R.id.tv_ilike)
    protected TextView tv_ilike;

    protected List<Object> listModel = new ArrayList<>();

    protected LiveTabFollowRecommendAdapter adapter0;
    protected LiveTabFollowLiveAdapter adapter1;
    protected LiveTabFollowAdapter adapter2;
    protected List<LiveRoomModel> listRoom;
    protected List<UserModel> listFollow;
    protected List<LivePlaybackModel> listPlayback;
    private final static int PLAY_TOP_HEIGHT = 35; //回放标题栏高度
    private final static int PLAY_ITEM_HEIGHT = 60; //回放每一行高度
    private final static int NO_LIVE_ITEM_HEIGHT = 120; //关注的人没有在直播 显示banner高度
    private final static int LIVE_ITEM_HEIGHT = 180; //关注的每一行高度
    private final static int RECOMMEND_ITEM_HEIGHT = 150;//推荐关注的每一行高度
    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_tab_follow;
    }


    @Override
    protected void init()
    {
        super.init();
        adapter0 = new LiveTabFollowRecommendAdapter(new ArrayList<List<UserModel>>(),getActivity());
        adapter1 = new LiveTabFollowLiveAdapter(new ArrayList<List<LiveRoomModel>>(), getActivity());
        adapter2 = new LiveTabFollowAdapter(listModel, getActivity());
        lv_content_0.setAdapter(adapter0);
        lv_content_1.setAdapter(adapter1);
        lv_content_2.setAdapter(adapter2);
        initPullToRefresh();
        initListener();
    }

    private void initListener(){
        tv_ilike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter0 !=null){
                    String selectdUserIds = adapter0.getSelectedUserIds();
                    if(TextUtils.isEmpty(selectdUserIds)){
                        SDToast.showToast(getString(R.string.please_choose_user_follow));
                    }else{
                        CommonInterface.requestMutilFollow(selectdUserIds, new AppRequestCallback<BaseActModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                if(rootModel.isOk()){
                                    requestData();
                                }
                            }
                        });
                    }

                }
            }
        });
    }

    protected void initPullToRefresh()
    {
        lv_content.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }
        });
        requestData();
    }

    @Override
    public void onResume()
    {
        requestData();
        super.onResume();
        MobclickAgent.onPageStart("直播-关注列表界面");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("直播-关注列表界面");
    }

    protected void requestData()
    {
        CommonInterface.requestFocusVideo(new AppRequestCallback<Index_focus_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.isOk()) {
                    synchronized (LiveTabFollowFragment.this) {
                        listRoom = actModel.getList();
                        listFollow = actModel.getRecommend_follow_list();
                        listPlayback = actModel.getPlayback();
                        orderData();
                    }
                    listModel = convertToNotConstainLiveAndFollowModels(listModel);
                    adapter2.updateData(listModel);
                    judgeHeight(actModel.getList(), initSelectedForUserModels(actModel.getRecommend_follow_list()));
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                lv_content.onRefreshComplete();
                super.onFinish(resp);
            }
        });
    }

    private List<UserModel> initSelectedForUserModels(List<UserModel> userModels){
        if(userModels != null && userModels.size()>0){
            for(UserModel userModel : userModels){
                userModel.setSelected(true);
            }
        }
        return userModels;
    }

    /**
     * 计算高度
     * @param list
     */
    private void judgeHeight(List<LiveRoomModel> list,List<UserModel> userList){
        LinearLayout.LayoutParams params0 = (LinearLayout.LayoutParams) lv_content_0.getLayoutParams();
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) lv_content_1.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) lv_content_2.getLayoutParams();
        if(list != null && list.size() > 0){
            List<LiveRoomModel> newLiveList = list;
            adapter1.updateData(SDCollectionUtil.splitList(newLiveList, 2));
            ll_top.setVisibility(View.VISIBLE);
            ll_recommend_top.setVisibility(View.GONE);
            tv_ilike.setVisibility(View.GONE);
            int row = (newLiveList.size() % 2 == 0) ? (newLiveList.size() / 2) : (newLiveList.size() / 2) + 1;
            params0.height = 0;
            params1.height = row  * SDViewUtil.dp2px(LIVE_ITEM_HEIGHT);
            params2.height = listModel.size() * SDViewUtil.dp2px(PLAY_ITEM_HEIGHT) + SDViewUtil.dp2px(PLAY_TOP_HEIGHT);
        }else if(userList != null && userList.size()>0) {
            List<UserModel> newUserList = userList;
            adapter0.updateData(SDCollectionUtil.splitList(newUserList, 3));
            ll_top.setVisibility(View.GONE);
            ll_recommend_top.setVisibility(View.VISIBLE);
            tv_ilike.setVisibility(View.VISIBLE);
            int row = (newUserList.size() % 3 == 0) ? (newUserList.size() / 3) : (newUserList.size() / 3) + 1;
            params0.height = row  * SDViewUtil.dp2px(RECOMMEND_ITEM_HEIGHT);
            params1.height = 0;
            params2.height = listModel.size() * SDViewUtil.dp2px(PLAY_ITEM_HEIGHT) + SDViewUtil.dp2px(PLAY_TOP_HEIGHT);
        }else{
            ll_top.setVisibility(View.GONE);
            ll_recommend_top.setVisibility(View.GONE);
            tv_ilike.setVisibility(View.GONE);
            params0.height = 0 ;
            params1.height = 0 ;
            params2.height = (listModel.size() - 1) * SDViewUtil.dp2px(PLAY_ITEM_HEIGHT) + + SDViewUtil.dp2px(PLAY_TOP_HEIGHT) + SDViewUtil.dp2px(NO_LIVE_ITEM_HEIGHT);
        }
        lv_content_0.setLayoutParams(params0);
        lv_content_1.setLayoutParams(params1);
        lv_content_2.setLayoutParams(params2);
    }

    private  List<Object> convertToNotConstainLiveAndFollowModels(List<Object> listModel){
        List<Object> objList = new ArrayList<>();
        for(Object obj : listModel){
            if(!(obj instanceof LiveRoomModel) && !(obj instanceof UserModel)){
                objList.add(obj);
            }
        }
        return objList;
    }

    private  List<LiveRoomModel> convertToLiveRoomModels(List<Object> listModel){
        List<LiveRoomModel> result = new ArrayList<>();
        for(Object obj : listModel){
            if(obj instanceof LiveRoomModel){
                result.add((LiveRoomModel)obj);
            }
        }
        return result;
    }

    private  List<UserModel> convertToRecommendUserModels(List<Object> listModel){
        List<UserModel> result = new ArrayList<>();
        for(Object obj : listModel){
            if(obj instanceof UserModel){
                result.add((UserModel)obj);
            }
        }
        return result;
    }

    protected void orderData()
    {
        listModel.clear();
        if (SDCollectionUtil.isEmpty(listRoom) && SDCollectionUtil.isEmpty(listFollow))
        {
            listModel.add(new LiveTabFollowAdapter.TypeNoLiveRoomModel());
        } else
        {
            for (LiveRoomModel room : listRoom)
            {
                listModel.add(room);
            }
            for (UserModel user : listFollow)
            {
                listModel.add(user);
            }
        }
        if (!SDCollectionUtil.isEmpty(listPlayback))
        {
            for (LivePlaybackModel playback : listPlayback)
            {
                listModel.add(playback);
            }
        }
    }

    @Override
    public void scrollToTop()
    {
//        lv_content.getRefreshableView().setSelection(0);
    }

    @Override
    protected void onMsgLiveStopped(final MsgModel msgModel)
    {
        SDHandlerManager.getBackgroundHandler().post(new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (LiveTabFollowFragment.this)
                {
                    if (SDCollectionUtil.isEmpty(listRoom))
                    {
                        return;
                    }
                    CustomMsgLiveStopped customMsg = msgModel.getCustomMsgLiveStopped();
                    if (customMsg != null)
                    {
                        int roomId = customMsg.getRoom_id();
                        Iterator<LiveRoomModel> it = listRoom.iterator();
                        while (it.hasNext())
                        {
                            LiveRoomModel item = it.next();
                            if (roomId == item.getRoomId())
                            {
                                it.remove();
                                orderData();
                                SDHandlerManager.getMainHandler().post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        List<Object> objList = convertToNotConstainLiveAndFollowModels(listModel);
                                        adapter2.updateData(objList);
                                        judgeHeight(convertToLiveRoomModels(listModel),initSelectedForUserModels(convertToRecommendUserModels(listModel)));
                                    }
                                });
                                break;
                            }
                        }
                    }
                }
            }
        });
        super.onMsgLiveStopped(msgModel);
    }

}
