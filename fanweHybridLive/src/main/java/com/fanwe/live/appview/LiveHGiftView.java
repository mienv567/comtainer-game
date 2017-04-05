package com.fanwe.live.appview;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.adapter.GridItemDecoration;
import com.fanwe.live.adapter.LiveGiftRecyclerAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.ERefreshGiftList;
import com.fanwe.live.event.EUpdateUserInfo;
import com.fanwe.live.model.App_propActModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.UserModel;
import com.ta.util.netstate.TANetWorkUtil;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class LiveHGiftView extends BaseAppView {

    @ViewInject(R.id.recyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.ll_charge)
    private LinearLayout mLlRecharge;
    @ViewInject(R.id.tv_send)
    private TextView mTvSend;
    @ViewInject(R.id.tv_diamonds)
    private TextView tv_diamonds;

    private UserModel user;
    private List<LiveGiftModel> listModel = new ArrayList<>();
    private LiveGiftModel selectedGiftModel;
    private ClickListener clickListener;
    private int mTopicId;
    private LiveGiftRecyclerAdapter mAdapter;

    public LiveHGiftView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LiveHGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveHGiftView(Context context) {
        super(context);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.layout_live_send_gift;
    }

    @Override
    protected void baseConstructorInit() {
        super.baseConstructorInit();
        mEventBySelf = true;

        initRecyclerView();

        mLlRecharge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/3/27  
            }
        });
        mTvSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSend();
            }
        });

        bindUserData();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initRecyclerView() {
        PagingScrollHelper scrollHelper = new PagingScrollHelper();
        HorizontalPageLayoutManager layoutManager = new HorizontalPageLayoutManager(3, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridItemDecoration(getContext(), 1,
                getResources().getColor(R.color.division)));
        scrollHelper.setUpRecycleView(mRecyclerView);
        scrollHelper.updateLayoutManger();
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new LiveGiftRecyclerAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void bindUserData() {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<UserModel>() {
            @Override
            public UserModel onBackground() {
                return UserModelDao.query();
            }

            @Override
            public void onMainThread(UserModel result) {
                updateDiamonds(result);
            }
        });
    }

    public void requestData(int topicId) {
        if (listModel.isEmpty()) {
            mTopicId = topicId;
            //InitActModel initActModel = InitActModelDao.query();
            // TODO 不从init里面拿数据
            //            listModel = initActModel.getProps();
            if (!listModel.isEmpty()) {
                preLoadGif(listModel);
                mAdapter.setData(listModel);
            } else {
                CommonInterface.requestGift(mTopicId, new AppRequestCallback<App_propActModel>() {
                    @Override
                    protected void onSuccess(SDResponse resp) {
                        if (rootModel.isOk()) {
                            listModel = actModel.getClassification();
                            preLoadGif(listModel);
                            mAdapter.setData(listModel);
                        }
                    }

                    @Override
                    protected void onError(SDResponse resp) {
                        super.onError(resp);
                    }
                });
            }
        }
    }

    private void refreshGiftList() {
        CommonInterface.requestGift(mTopicId, new AppRequestCallback<App_propActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (rootModel.isOk()) {
                    List<LiveGiftModel> newListModel = actModel.getClassification();
                    preLoadGif(newListModel);
                    if (listModel != null && listModel.size() > 0 && newListModel != null && newListModel.size() > 0) {
                        for (LiveGiftModel model : newListModel) {
                            if (model.getSuperimposedType() == 1) {
                                for (LiveGiftModel item : listModel) {
                                    if (item.getSuperimposedType() == 1 && item.getPropId() == model.getPropId()) {
                                        if (item.getHodler() != null && item.getHodler().get() != null) {
                                            TextView tv_gift_num = (TextView) item.getHodler().get().findViewById(R.id.iv_gift_num);
                                            item.setPropCount(model.getPropCount());
                                            SDViewBinder.setTextView(tv_gift_num, String.valueOf(model.getPropCount()));
                                            if (model.getPropCount() > 0) {
                                                tv_gift_num.setVisibility(View.VISIBLE);
                                            } else {
                                                tv_gift_num.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    /**
     * 预加载gif资源
     */
    private void preLoadGif(List<LiveGiftModel> list) {
        if (list != null && list.size() > 0) {
            for (LiveGiftModel model : list) {
                if (!TextUtils.isEmpty(model.getPreviewUrl())) {
                    Glide.with(getActivity()).load(model.getPreviewUrl()).downloadOnly(500, 500);
                }
            }
        }
    }

    private void updateDiamonds(UserModel user) {
        if (user != null) {
            this.user = user;
            SDViewBinder.setTextView(tv_diamonds, String.valueOf(user.getDiamonds()));
        }
    }

    private void clickSend() {
        if (validateSend() && clickListener != null) {
            clickListener.onClickSend(selectedGiftModel, 1, 0);
        }
    }

    private boolean validateSend() {
        if (!TANetWorkUtil.isNetworkAvailable(getActivity())) {
            SDToast.showToast(SDResourcesUtil.getString(R.string.no_internet));
            return false;
        }

        findSelectedGiftModel();
        if (selectedGiftModel == null) {
            SDToast.showToast(SDResourcesUtil.getString(R.string.please_choose_gift));
            return false;
        }

        if (user == null) {
            return false;
        }

        //判断可用数量以及可用余额
        if (selectedGiftModel.getSuperimposedType() == 1) {
            if (selectedGiftModel.getPropCount() <= 0) {
                if (selectedGiftModel.getDiamonds() == 0) { //活动或者热度礼物
                    if (!TextUtils.isEmpty(selectedGiftModel.getTips())) {
                        SDToast.showToast(selectedGiftModel.getTips());
                    }
                    return false;
                } else {
                    if (!user.canPay(selectedGiftModel.getDiamonds())) {
                        SDToast.showToast(SDResourcesUtil.getString(R.string.balance_not_enough));
                        return false;
                    }
                }
            }
        } else {
            if (!user.canPay(selectedGiftModel.getDiamonds())) {
                SDToast.showToast(SDResourcesUtil.getString(R.string.balance_not_enough));
                return false;
            }
        }
        return true;
    }

    private void findSelectedGiftModel() {
        selectedGiftModel = null;
        if (listModel != null) {
            for (LiveGiftModel item : listModel) {
                if (item.isSelected()) {
                    selectedGiftModel = item;
                    break;
                }
            }
        }
    }

    public void sendGiftSuccess(final LiveGiftModel giftModel) {
        if (giftModel != null) {
            SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<UserModel>() {
                @Override
                public UserModel onBackground() {
                    UserModel user = UserModelDao.query();
                    user.pay(giftModel.getDiamonds());
                    UserModelDao.insertOrUpdate(user);
                    return user;
                }

                @Override
                public void onMainThread(UserModel result) {
                    updateDiamonds(result);
                }
            });
        }
    }

    public interface ClickListener {
        void onClickSend(LiveGiftModel model, int count, int is_plus);
    }

    @Override
    protected void onAttachedToWindow() {
        mEventBySelf = true;
        super.onAttachedToWindow();
    }

    public void onEventMainThread(EUpdateUserInfo event) {
        bindUserData();
    }

    public void onEventMainThread(ERefreshGiftList event) {
        refreshGiftList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
