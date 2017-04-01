package com.qy.ysys.yishengyishi.views.customviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.qy.ysys.yishengyishi.AppImpl;
import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.R2;
import com.qy.ysys.yishengyishi.adapter.CircleAdapter;
import com.qy.ysys.yishengyishi.event.EAddFamilyPraise;
import com.qy.ysys.yishengyishi.event.EDeleteFamily;
import com.qy.ysys.yishengyishi.event.EDeleteFamilyComment;
import com.qy.ysys.yishengyishi.event.EDeleteFamilyPraise;
import com.qy.ysys.yishengyishi.event.ENeedRefreshFamily;
import com.qy.ysys.yishengyishi.event.EShowFamilyET;
import com.qy.ysys.yishengyishi.httputils.RequestInterface;
import com.qy.ysys.yishengyishi.model.BaseModel;
import com.qy.ysys.yishengyishi.model.CommentConfig;
import com.qy.ysys.yishengyishi.model.FamilyDataListModel;
import com.qy.ysys.yishengyishi.model.item.FamilyCommentItem;
import com.qy.ysys.yishengyishi.model.item.FamilyDataItem;
import com.qy.ysys.yishengyishi.model.item.FamilyPraiseItem;
import com.qy.ysys.yishengyishi.model.item.UserInfo;
import com.qy.ysys.yishengyishi.utils.UiUtils;
import com.qy.ysys.yishengyishi.views.EditStoreActivity;
import com.qy.ysys.yishengyishi.widgets.SDPullToRefreshRecyclerView;
import com.qy.ysys.yishengyishi.widgets.SDRecyclerView;
import com.qy.ysys.yishengyishi.widgets.dialog.CommentPopupWindow;

import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tony.chen on 2016/12/30.
 */

public class FragFamily extends BaseTitleFragment implements CommentPopupWindow.LiveCommentResult {

    protected PopupWindow mPopupWindow;
    @BindView(R2.id.bodyLayout)
    RelativeLayout bodyLayout;
    @BindView(R2.id.recyclerView)
    SDPullToRefreshRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CommentConfig commentConfig;
    private CircleAdapter circleAdapter;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;
    private UserInfo mUserInfo;
    private CommentPopupWindow commentPopupWindow;


    @Override
    public View setFragmentContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.frag_family, null);
        return inflate;
    }

    @Override
    protected void initTitleBar(ITitleView titleView) {
        titleView.setTitleText(getResources().getString(R.string.menu_box_bar));
        titleView.setLeftImage(R.mipmap.ic_share);
        titleView.setRightTitle(getResources().getString(R.string.republic));
        titleView.setTitleBarOnClickListener(new ITitleBarOnClickListener() {
            @Override
            public void onClickLeft() {
                openShare();
            }

            @Override
            public void onClickRight() {
                // 编写故事
                Intent intent = new Intent(getActivity(), EditStoreActivity.class);
                startActivity(intent);
            }
        });
    }

    public void openShare() {
        View popupContentView = View.inflate(getActivity(), R.layout.view_share, null);
        final View share_qq = popupContentView.findViewById(R.id.ll_share_qq);
        final View share_qzoom = popupContentView.findViewById(R.id.ll_share_qqzome);
        final View share_weixin = popupContentView.findViewById(R.id.ll_share_weixin);
        final View share_pengyouquan = popupContentView.findViewById(R.id.ll_share_pengyouquan);
        final View share_xinlang = popupContentView.findViewById(R.id.ll_share_xinlang);

        View.OnClickListener shareViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == share_qq) {
                } else if (v == share_qzoom) {
//                        openShare(1, umShareListener);
                } else if (v == share_weixin) {
//                        openShare(2, umShareListener);
                } else if (v == share_pengyouquan) {
//                        openShare(3, umShareListener);
                } else if (v == share_xinlang) {
//                        openShare(4, umShareListener);
                }
                mPopupWindow.dismiss();
            }
        };

        popupContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                    return true;
                }
                return false;
            }
        });

        share_qq.setOnClickListener(shareViewListener);
        share_qzoom.setOnClickListener(shareViewListener);
        share_weixin.setOnClickListener(shareViewListener);
        share_pengyouquan.setOnClickListener(shareViewListener);
        share_xinlang.setOnClickListener(shareViewListener);

        mPopupWindow = new PopupWindow(popupContentView, ViewGroup.LayoutParams.MATCH_PARENT, UiUtils.dip2px(240), true);
        mPopupWindow.setAnimationStyle(R.style.path_popwindow_anim_enterorout_window);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected View setTitleViewByView() {
        return new CustomTitleView(getActivity());
    }

    private void initUserInfo() {
        mUserInfo = AppImpl.getCurrentUser();
    }

    @Override
    protected void initView(View contentView) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initUserInfo();
        initRecycleView();
        initEditer();
    }

    private void initEditer() {
        commentPopupWindow = new CommentPopupWindow(getActivity(), this);
    }

    private void initRecycleView() {
        recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<SDRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SDRecyclerView> pullToRefreshBase) {
                RequestInterface.queryFamilyList(1, RequestInterface.PAGE_SIZE, new Callback<FamilyDataListModel>() {
                    @Override
                    public void onResponse(Call<FamilyDataListModel> call, Response<FamilyDataListModel> response) {
                        recyclerView.onRefreshComplete();
                        if (response.body().isSuccess()) {
                            circleAdapter.setDatas(response.body().getReturnObj());
                            circleAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<FamilyDataListModel> call, Throwable t) {
                        recyclerView.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SDRecyclerView> pullToRefreshBase) {
                int count = circleAdapter.getItemCount() + RequestInterface.PAGE_SIZE;
                int page = (count % RequestInterface.PAGE_SIZE == 0) ? (count / RequestInterface.PAGE_SIZE) : (count / RequestInterface.PAGE_SIZE) + 1;
                RequestInterface.queryFamilyList(page, RequestInterface.PAGE_SIZE, new Callback<FamilyDataListModel>() {
                    @Override
                    public void onResponse(Call<FamilyDataListModel> call, Response<FamilyDataListModel> response) {
                        recyclerView.onRefreshComplete();
                        if (response.body().isSuccess()) {
                            circleAdapter.getDatas().addAll(response.body().getReturnObj());
                            circleAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<FamilyDataListModel> call, Throwable t) {
                        recyclerView.onRefreshComplete();
                    }
                });
            }
        });


        circleAdapter = new CircleAdapter(getActivity());
        recyclerView.getRefreshableView().setAdapter(circleAdapter);
        recyclerView.setRefreshing(true);
        layoutManager = recyclerView.getRefreshableView().getLinearLayoutManager();
    }

    public void onEventMainThread(EDeleteFamilyComment event) {
        RequestInterface.deleteComment(event.getCommentId(), new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
        update2DeleteComment(event.getCirclePosition(), event.getCommentId());
    }

    public void onEventMainThread(EDeleteFamily event) {
        RequestInterface.deleteFamilyZone(event.getZoneId(), new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
        update2DeleteCircle(event.getZoneId());
    }

    public void onEventMainThread(EAddFamilyPraise event) {
        //需要服务器提供新增的点赞id  取消点赞的时候用
        RequestInterface.praiseFamilyZone(event.getZoneId(), new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
        update2AddFavorite(event.getCirclePosition(), createCurUserFavortItem());
    }

    public void onEventMainThread(EDeleteFamilyPraise event) {
        RequestInterface.deletePraiseFamilyZone(event.getPraiseId(), new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
        update2DeleteFavort(event.getCirclePosition(), event.getPraiseId());
    }

    FamilyPraiseItem createCurUserFavortItem() {
        FamilyPraiseItem item = new FamilyPraiseItem();
        item.setUserId(mUserInfo.getId());
        item.setUserName(mUserInfo.getName());
        return item;
    }

    public void update2DeleteCircle(int circleId) {
        List<FamilyDataItem> circleItems = circleAdapter.getDatas();
        for (int i = 0; i < circleItems.size(); i++) {
            if (circleId == (circleItems.get(i).getId())) {
                circleItems.remove(i);
                circleAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    public void update2AddFavorite(int circlePosition, FamilyPraiseItem addItem) {
        if (addItem != null) {
            FamilyDataItem item = (FamilyDataItem) circleAdapter.getDatas().get(circlePosition);
            item.getPraises().add(addItem);
            circleAdapter.notifyDataSetChanged();
        }
    }

    public void update2DeleteFavort(int circlePosition, int favortId) {
        FamilyDataItem item = (FamilyDataItem) circleAdapter.getDatas().get(circlePosition);
        List<FamilyPraiseItem> items = item.getPraises();
        for (int i = 0; i < items.size(); i++) {
            if (favortId == (items.get(i).getId())) {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }

    public void update2DeleteComment(int circlePosition, int commentId) {
        FamilyDataItem item = (FamilyDataItem) circleAdapter.getDatas().get(circlePosition);
        List<FamilyCommentItem> items = item.getComments();
        for (int i = 0; i < items.size(); i++) {
            if (commentId == (items.get(i).getId())) {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    public void update2AddComment(int circlePosition, FamilyCommentItem addItem) {
        if (addItem != null) {
            FamilyDataItem item = (FamilyDataItem) circleAdapter.getDatas().get(circlePosition);
            item.getComments().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
    }

    /**
     * 创建发布评论
     *
     * @return
     */
    public FamilyCommentItem createPublicComment(String content) {
        FamilyCommentItem item = new FamilyCommentItem();
        item.setComment(content);
        item.setUserId(mUserInfo.getId());
        item.setUserName(mUserInfo.getName());
        return item;
    }

    /**
     * 创建回复评论
     *
     * @return
     */
    public FamilyCommentItem createReplyComment(UserInfo replyUser, String content) {
        FamilyCommentItem item = new FamilyCommentItem();
        item.setComment(content);
        item.setUserId(mUserInfo.getId());
        item.setUserName(mUserInfo.getName());
        item.setReplyUserId(replyUser.getId());
        item.setReplyUserName(replyUser.getName());
        return item;
    }

    /**
     * 计算视图变化
     */
    private void calculateOffsetAndScroll() {
        int statusBarH = getStatusBarHeight();//状态栏高度
        int screenH = bodyLayout.getRootView().getHeight();
        screenHeight = screenH;//应用屏幕的高度
        //偏移listview
        if (layoutManager != null && commentConfig != null) {
            layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE, getListviewOffset(commentConfig));
        }
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - UiUtils.dip2px(46);
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        Log.i("FragFamily", "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = AppImpl.getApplication().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = AppImpl.getApplication().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;

        if (View.VISIBLE == visibility) {
            commentPopupWindow.showWindowAtView(getView());
            calculateOffsetAndScroll();
            //弹出键盘
        } else if (View.GONE == visibility) {
            //隐藏键盘
            commentPopupWindow.hideWindow(getView(), true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onEvent(ENeedRefreshFamily event) {
        recyclerView.setRefreshing(true);
    }

    public void onEventMainThread(EShowFamilyET event) {
        updateEditTextBodyVisible(View.VISIBLE, event.getConfig());
    }

    @Override
    public void onResult(boolean confirmed, final String content) {
        int replyUserId = 0;
        if (commentConfig.replyUser != null) {
            replyUserId = commentConfig.replyUser.getId();
        }
        final CommentConfig mCommentConfig = commentConfig;
        RequestInterface.publishComment(commentConfig.zoneId, replyUserId, content, new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                //需要提供新增的评论id 否则无法通知服务器删除评论
                if (response.body().isSuccess()) {
                    FamilyCommentItem newItem = null;
                    if (mCommentConfig.commentType == CommentConfig.Type.PUBLIC) {
                        newItem = createPublicComment(content);
                    } else {
                        newItem = createReplyComment(mCommentConfig.replyUser, content);
                    }
                    update2AddComment(mCommentConfig.circlePosition, newItem);
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {

            }
        });
        updateEditTextBodyVisible(View.GONE, null);
    }
}
