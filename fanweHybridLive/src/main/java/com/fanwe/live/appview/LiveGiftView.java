package com.fanwe.live.appview;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.adapter.GridItemDecoration;
import com.fanwe.live.adapter.LiveGiftRecyclerAdapter;
import com.fanwe.live.appview.room.RoomView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_pop_propActModel;
import com.fanwe.live.model.Deal_send_propActModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgPrivateGift;
import com.ta.util.netstate.TANetWorkUtil;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class LiveGiftView extends RoomView {

    @ViewInject(R.id.recyclerView_gift)
    private RecyclerView recyclerView_gift;
    @ViewInject(R.id.ll_charge)
    private LinearLayout ll_charge;
    @ViewInject(R.id.tv_diamonds)
    private TextView tv_diamonds;
    @ViewInject(R.id.tv_send_gift)
    private TextView tv_send_gift;

    private List<LiveGiftModel> giftModels;
    private LiveGiftModel selectedGiftModel;
    private LiveGiftRecyclerAdapter mAdapter;
    private UserModel user;

    public LiveGiftView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LiveGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveGiftView(Context context) {
        super(context);
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.layout_live_send_gift;
    }

    @Override
    protected void baseConstructorInit() {
        giftModels = new ArrayList<>();
        initRecyclerView();
        register();
    }

    private void initRecyclerView() {
        PagingScrollHelper scrollHelper = new PagingScrollHelper();
        HorizontalPageLayoutManager layoutManager = new HorizontalPageLayoutManager(2, 4);
        recyclerView_gift.setLayoutManager(layoutManager);
        recyclerView_gift.addItemDecoration(new GridItemDecoration(getContext(), 1,
                getResources().getColor(R.color.division)));
        scrollHelper.setUpRecycleView(recyclerView_gift);
        scrollHelper.updateLayoutManger();
        ((DefaultItemAnimator) recyclerView_gift.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new LiveGiftRecyclerAdapter(getActivity());
        recyclerView_gift.setAdapter(mAdapter);
    }

    private void register() {
        ll_charge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_send_gift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSendGift();
            }
        });
    }

    public void setData(List<LiveGiftModel> giftModels) {
        this.giftModels = giftModels;
        mAdapter.setData(giftModels);
    }

    public void updateDiamonds(long diamonds) {
        tv_diamonds.setText(String.valueOf(diamonds));
    }

    private void clickSendGift() {
        if (validateSend()) {
            if (getLiveInfo() == null || getLiveInfo().getRoomInfo() == null) {
                return;
            }
            final String createrId = getLiveInfo().getCreaterId();
            String groupId = getLiveInfo().getGroupId();
            int roomId = getLiveInfo().getRoomId();
            if (getLiveInfo().getRoomInfo().getLive_in() == 0) {
                //私聊发礼物接口
                if (createrId != null) {
                    CommonInterface.requestSendGiftPrivateJava(selectedGiftModel.getPropId(), 1,
                            createrId, roomId, groupId, new AppRequestCallback<Deal_send_propActModel>() {
                        @Override
                        protected void onSuccess(SDResponse resp) {
                            if (actModel.isOk()) {
                                sendGiftSuccess(selectedGiftModel);
                                // 发送私聊消息给主播
                                final CustomMsgPrivateGift msg = new CustomMsgPrivateGift();
                                msg.fillData(actModel);
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        IMHelper.sendMsgC2C(createrId, msg, new TIMValueCallBack<TIMMessage>() {
                                            @Override
                                            public void onError(int i, String s) {
                                            }

                                            @Override
                                            public void onSuccess(TIMMessage timMessage) {
                                                // 如果私聊界面不是每次都加载的话要post一条来刷新界面
                                                // IMHelper.postMsgLocal(msg, createrId);
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            } else {
                AppRequestParams params = CommonInterface
                        .requestSendGiftParams(selectedGiftModel.getPropId(), 1, 0, roomId);
                AppHttpUtil.getInstance().post(params, new AppRequestCallback<App_pop_propActModel>() {
                    @Override
                    protected void onSuccess(SDResponse resp) {
                        // 扣费
                        if (actModel.isOk()) {
                            sendGiftSuccess(selectedGiftModel);
                        }
                    }

                    @Override
                    protected void onError(SDResponse resp) {
                        CommonInterface.requestMyUserInfoJava(null);
                    }
                });
            }
        }
    }

    private void sendGiftSuccess(final LiveGiftModel giftModel) {
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
                updateDiamonds(result.getDiamonds());
            }
        });
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
        if (giftModels != null) {
            for (LiveGiftModel item : giftModels) {
                if (item.isSelected()) {
                    selectedGiftModel = item;
                    break;
                }
            }
        }
    }

    public void SetUserModel(UserModel result) {
        user = result;
    }
}
