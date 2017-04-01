package com.fanwe.live.appview;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.LiveLikeModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.blocker.SDBlocker;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.customview.SDSlidingPlayView;
import com.fanwe.library.looper.SDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveGiftAdapter;
import com.fanwe.live.adapter.LiveGiftPagerAdapter;
import com.fanwe.live.adapter.LiveLikeAdapter;
import com.fanwe.live.adapter.LiveLikePagerAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveRechargeDialog;
import com.fanwe.live.event.ERefreshGiftList;
import com.fanwe.live.event.EUpdateUserInfo;
import com.fanwe.live.model.App_propActModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.StringUtils;
import com.ta.util.netstate.TANetWorkUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/7/21.
 */
public class LiveSendGiftView extends BaseAppView implements ILivePrivateChatMoreView {
    private static final long DURATION_CONTINUE = 5 * 1000;
    private static final long DURATION_COUNT = 100;

    private SDSlidingPlayView spv_content;
    private View ll_charge;
    private TextView tv_diamonds;
    private TextView tv_send;
    private View view_continue_send;
    private View rl_big_circle; // 连送大圆
    private View rl_smallfirst; // 连送礼物第一个小圆
    private View rl_smallsecond; // 连送礼物第一个小圆
    private View rl_smallthird; // 连送礼物第一个小圆
    private View rl_smallfourth; // 连送礼物第一个小圆

    private View iv_arrow;

    //    private View view_click_continue_send;
    //    private TextView tv_continue_number;
    //    private TextView tv_count_down_number;

    private LiveGiftPagerAdapter adapterPager;
    private List<LiveGiftModel> listModel;

    private LiveLikePagerAdapter likeAdapter;
    private List<LiveLikeModel> likeList;

    private SDSelectManager.SDSelectable selectedGiftModel;
    private SDLooper looper = new SDSimpleLooper();
    private long countDownNumber = DURATION_CONTINUE / DURATION_COUNT;
    private int clickNumber = 0;
    /**
     * 是否连发模式
     */
    private boolean isContinueMode;

    private SDBlocker blocker = new SDBlocker(300);

    private UserModel user;

    private ClickListener clickListener;
    private ContinueListener continueListener;
    private int mTopicId = 0;
    private boolean mIsPrivateChat;
    //
    private int countPerList = 8;
    private int colNumber = 4;

    public LiveSendGiftView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveSendGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveSendGiftView(Context context) {
        super(context);
        init();
    }

    public void setIsPrivateChat() {
        mIsPrivateChat = true;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setContinueListener(ContinueListener continueListener) {
        this.continueListener = continueListener;
    }

    @Override
    protected void init() {
        super.init();
        mEventBySelf = true;
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            countPerList = 6;
            colNumber = 2;
        }
        setContentView(R.layout.view_live_send_gift_h);

        spv_content = find(R.id.spv_content);
        spv_content.showDivision();
        ll_charge = find(R.id.ll_charge);
        tv_diamonds = find(R.id.tv_diamonds);
        tv_send = find(R.id.tv_send);
        view_continue_send = find(R.id.view_continue_send);
        rl_big_circle = find(R.id.rl_big_circle);
        rl_smallfirst = find(R.id.rl_smallfirst);
        rl_smallsecond = find(R.id.rl_smallsecond);
        rl_smallthird = find(R.id.rl_smallthird);
        rl_smallfourth = find(R.id.rl_smallfourth);
        //        view_click_continue_send = find(R.id.view_click_continue_send);
        //        tv_continue_number = find(R.id.tv_continue_number);
        //        tv_count_down_number = find(R.id.tv_count_down_number);
        iv_arrow = find(R.id.iv_arrow);

        listModel = new ArrayList<>();

        register();
        bindUserData();
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

    private void updateDiamonds(UserModel user) {
        if (user != null) {
            this.user = user;
            //TODO 钻石数量换成金币银币
//            SDViewBinder.setTextView(tv_diamonds, String.valueOf(user.getDiamonds()));
        }
    }

    public void sendGiftSuccess(final LiveGiftModel giftModel) {
        if (giftModel != null) {
            refreshGiftNum(giftModel, 1);
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

    // 一次发送多个礼物成功
    public void sendGiftSuccess(final LiveGiftModel giftModel, final int count) {
        if (giftModel != null) {
            refreshGiftNum(giftModel, count);
            int reduceCount = count;
            if (giftModel.getDiamonds() > 0) { //需要付钱的背包礼物  数量不够扣钻石 数量够不扣除钻石
                if (giftModel.getPropCount() < count) {
                    reduceCount = count - giftModel.getPropCount();
                } else {
                    reduceCount = 0;
                }
            }
            final int realPayCount = reduceCount;
            SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<UserModel>() {
                @Override
                public UserModel onBackground() {
                    UserModel user = UserModelDao.query();
                    user.pay(giftModel.getDiamonds() * realPayCount);
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

    private void register() {
        initSDSlidingPlayView();

        // 1.4 版本连送控件
        rl_big_circle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickContinueSend();

            }
        });

        rl_smallfirst.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickContinueSend(66);

            }
        });

        rl_smallsecond.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickContinueSend(188);

            }
        });

        rl_smallthird.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickContinueSend(520);

            }
        });
        rl_smallfourth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickContinueSend(1314);
            }
        });


        ll_charge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickCharge();
            }
        });
        tv_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickSend();
            }
        });

        //        view_click_continue_send.setOnClickListener(new View.OnClickListener()
        //        {
        //
        //            @Override
        //            public void onClick(View v)
        //            {
        //                clickContinueSend();
        //            }
        //        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    // 1.4 一键送多个礼物
    protected void clickContinueSend(int count) {
        if (blocker.block()) {
            return;
        }

        if (validateSend(count)) {
            if (clickListener != null) {
                clickListener.onClickSend(selectedGiftModel, count, 1);
            }
        }
    }


    protected void clickContinueSend() {
        if (blocker.block()) {
            return;
        }

        if (validateSend()) {
            int giftNumber = 1;
            int is_plus = 0;
            clickNumber++;
            startCountDownNumberLooper();
            if (clickNumber > 1) {
                is_plus = 1;
            } else {
                is_plus = 0;
            }

            if (clickListener != null) {
                clickListener.onClickSend(selectedGiftModel, 1, is_plus);
            }
        }
    }

    private void startCountDownNumberLooper() {
        resetCountDownNumber();
        looper.start(DURATION_COUNT, countDownNumberRunnable);
    }

    private Runnable countDownNumberRunnable = new Runnable() {

        @Override
        public void run() {
            countDownNumber--;
            if (countDownNumber <= 0) {
                resetClick();
            } else {
                //                if (clickNumber > 0)
                //                {
                //                    tv_continue_number.setText("X" + clickNumber);
                //                }
                //                tv_count_down_number.setText(String.valueOf(countDownNumber));
            }
        }
    };

    protected void clickCharge() {
        LiveRechargeDialog dialog = new LiveRechargeDialog(getActivity());
        dialog.showCenter();
    }

    private void resetClick() {
        if (isContinueMode) {
            isContinueMode = false;
            if (continueListener != null) {
                continueListener.onContinueFinish((LiveGiftModel) selectedGiftModel, clickNumber);
            }
        }

        looper.stop();
        clickNumber = 0;
        //        tv_continue_number.setText("");
        SDViewUtil.hide(view_continue_send);
        SDViewUtil.show(tv_send);
    }

    private void resetCountDownNumber() {
        countDownNumber = DURATION_CONTINUE / DURATION_COUNT;
    }

    private void findSelectedGiftModel() {
        selectedGiftModel = null;
        if(isShownGift()){
            if (listModel != null) {
                for (LiveGiftModel item : listModel) {
                    if (item.isSelected()) {
                        selectedGiftModel = item;
                        break;
                    }
                }
            }
        } else {
            if (likeList != null) {
                for (LiveLikeModel item : likeList) {
                    if (item.isSelected()) {
                        selectedGiftModel = item;
                        break;
                    }
                }
            }
        }
    }


    /**
     * 刷新可用礼物的数量
     */
    private void refreshGiftNum(LiveGiftModel model, int count) {
        if (listModel != null && model.getSuperimposedType() == 1) {
            for (LiveGiftModel item : listModel) {
                if (item.getPropId() == model.getPropId()) {
                    if (item.getHodler() != null && item.getHodler().get() != null) {
                        TextView tv_gift_num = (TextView) item.getHodler().get().findViewById(R.id.iv_gift_num);
                        int newCount = model.getPropCount() - count;
                        if (newCount <= 0) {
                            newCount = 0;
                            tv_gift_num.setVisibility(View.GONE);
                        } else {
                            tv_gift_num.setVisibility(View.VISIBLE);
                        }
                        item.setPropCount(newCount);
                        SDViewBinder.setTextView(tv_gift_num, String.valueOf(newCount));
                    }
                    break;
                }
            }
        }
    }

    private void initSDSlidingPlayView() {
        spv_content.setNormalImageResId(R.drawable.ic_point_normal_white);
        spv_content.setSelectedImageResId(R.drawable.ic_point_selected_main_color);
        SDViewUtil.setViewMarginBottom(spv_content.vpg_content, SDViewUtil.dp2px(10));
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

    private void preLoadLikeGif(List<LiveLikeModel> list) {
        if (list != null && list.size() > 0) {
            for (LiveLikeModel model : list) {
                if (!TextUtils.isEmpty(model.getIcon())) {
                    Glide.with(getActivity()).load(model.getIcon()).downloadOnly(500, 500);
                }
            }
        }
    }

    public void requestData(int topicId) {
        if (listModel.isEmpty()) {
            mTopicId = topicId;
            InitActModel initActModel = InitActModelDao.query();
            // TODO 不从init里面拿数据
            //            listModel = initActModel.getProps();
            if (!listModel.isEmpty()) {
                preLoadGif(listModel);
                List<List<LiveGiftModel>> listModelPager = SDCollectionUtil.splitList(listModel, countPerList);
                adapterPager = new LiveGiftPagerAdapter(listModelPager, getActivity());
                adapterPager.setColNumber(colNumber);
                adapterPager.setListener(new LiveGiftAdapter.LiveGiftAdapterListener() {

                    @Override
                    public void onClickItem(int position, LiveGiftModel model, LiveGiftAdapter adapter) {
                        adapter.getSelectManager().performClick(position);
                        adapterPager.clickAdapter(adapter);
                        resetClick();
                    }
                });
                spv_content.setAdapter(adapterPager);
            } else {
                CommonInterface.requestGift(mTopicId, new AppRequestCallback<App_propActModel>() {
                    @Override
                    protected void onSuccess(SDResponse resp) {
                        if (actModel.isOk()) {
                            listModel = actModel.getClassification();
                            preLoadGif(listModel);
                            showGiftAdapter();

                            likeList = actModel.getLikePros();
                            preLoadLikeGif(likeList);
                        } else {

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
                if (actModel.isOk()) {
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
                } else {

                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }


    @Override
    protected void onAttachedToWindow() {
        mEventBySelf = true;
        super.onAttachedToWindow();
    }


    private boolean validateSend() {
        if (!TANetWorkUtil.isNetworkAvailable(getActivity())) {
            SDToast.showToast(SDResourcesUtil.getString(R.string.no_internet));
            return false;
        }

        findSelectedGiftModel();
        if (selectedGiftModel == null) {
            if(isShownGift()){
                SDToast.showToast(SDResourcesUtil.getString(R.string.please_choose_gift));
            } else {
                SDToast.showToast("请选择点赞类型");
            }
            return false;
        }

        if (user == null) {
            return false;
        }

        if(isShownGift()){
            return validateGiftModel((LiveGiftModel) selectedGiftModel);
        } else {
            return validateLikeModel((LiveLikeModel) selectedGiftModel);
        }
    }

    private boolean validateGiftModel(LiveGiftModel model){
        //判断可用数量以及可用余额
        if (model.getSuperimposedType() == 1) {
            if (model.getPropCount() <= 0) {
                if (model.getDiamonds() == 0) { //活动或者热度礼物
                    if (!TextUtils.isEmpty(model.getTips())) {
                        SDToast.showToast(model.getTips());
                    }
                    return false;
                } else {
                    if (!user.canPay(model.getDiamonds())) {
                        SDToast.showToast(SDResourcesUtil.getString(R.string.balance_not_enough));
                        return false;
                    }
                }
            }
        } else {
            if (!user.canPay(model.getDiamonds())) {
                SDToast.showToast(SDResourcesUtil.getString(R.string.balance_not_enough));
                return false;
            }
        }
        return true;
    }

    private boolean validateLikeModel(LiveLikeModel model){
        return true;
    }

    private boolean validateSend(int count) {
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
//        if (selectedGiftModel.getSuperimposedType() == 1) {
//            if (selectedGiftModel.getPropCount() <= 0) {
//                if (selectedGiftModel.getDiamonds() == 0) { //活动或者热度礼物
//                    if (!TextUtils.isEmpty(selectedGiftModel.getTips())) {
//                        SDToast.showToast(selectedGiftModel.getTips());
//                    }
//                    return false;
//                } else {
//                    if (!user.canPay(selectedGiftModel.getDiamonds() * count)) {
//                        SDToast.showToast(SDResourcesUtil.getString(R.string.balance_not_enough));
//                        return false;
//                    }
//                }
//            } else if (selectedGiftModel.getPropCount() < count) {
//                if (selectedGiftModel.getDiamonds() == 0) { //活动或者热度礼物
//                    SDToast.showToast(SDResourcesUtil.getString(R.string.current_gift_num_not_enough));
//                    return false;
//                } else {
//                    if (!user.canPay(selectedGiftModel.getDiamonds() * (count - selectedGiftModel.getPropCount()))) {
//                        SDToast.showToast(SDResourcesUtil.getString(R.string.balance_not_enough));
//                        return false;
//                    }
//                }
//            }
//        } else {
//            if (!user.canPay(selectedGiftModel.getDiamonds() * count)) {
//                SDToast.showToast(SDResourcesUtil.getString(R.string.balance_not_enough));
//                return false;
//            }
//        }
//        return true;
        if(isShownGift()){
            return validateGiftModel((LiveGiftModel) selectedGiftModel);
        } else {
            return validateLikeModel((LiveLikeModel) selectedGiftModel);
        }
    }

    private void clickSend() {
        if (validateSend()) {
            if(isShownGift()){
                LiveGiftModel model = (LiveGiftModel) selectedGiftModel;
                if (model.getIs_much() == 1 && !mIsPrivateChat) {
                    SDViewUtil.hide(tv_send);
                    SDViewUtil.show(view_continue_send);
                    clickContinueSend();
                    isContinueMode = true;
                } else {
                    //通知发送按钮被点击接口
                    if (clickListener != null) {
                        clickListener.onClickSend(model, 1, 0);
                    }
                }
            } else {
                LiveLikeModel model = (LiveLikeModel) selectedGiftModel;
                if (clickListener != null) {
                    clickListener.onClickSend(model, 1, 0);
                }
            }
        }
    }

    public void onEventMainThread(EUpdateUserInfo event) {
        bindUserData();
    }

    public void onEventMainThread(ERefreshGiftList event) {
        refreshGiftList();
    }

    @Override
    public void setContentMatchParent() {
        SDViewUtil.setViewHeightWeightContent(spv_content, 1);
        spv_content.setContentMatchParent();
    }

    @Override
    public void setContentWrapContent() {
        SDViewUtil.setViewHeightWrapContent(spv_content);
        spv_content.setContentWrapContent();
    }

    public interface ClickListener {
        void onClickSend(SDSelectManager.SDSelectable model, int count, int is_plus);
    }

    public interface ContinueListener {
        void onContinueFinish(LiveGiftModel model, int clickNumber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void showLikeAdapter() {
        List<List<LiveLikeModel>> listModelPager = SDCollectionUtil.splitList(likeList, countPerList);
        if (likeAdapter == null) {
            likeAdapter = new LiveLikePagerAdapter(listModelPager, getActivity());
        }
        likeAdapter.setColNumber(colNumber);
        likeAdapter.setListener(new LiveLikeAdapter.LiveLikeAdapterListener() {

            @Override
            public void onClickItem(int position, LiveLikeModel model, LiveLikeAdapter adapter) {
                adapter.getSelectManager().performClick(position);
                likeAdapter.clickAdapter(adapter);
                resetClick();
            }
        });
        spv_content.setAdapter(likeAdapter);

        if(iv_arrow != null){
            iv_arrow.setVisibility(INVISIBLE);
        }
        tv_diamonds.setText(StringUtils.formatLikeNumber(3));
    }

    public void showGiftAdapter() {
        List<List<LiveGiftModel>> listModelPager = SDCollectionUtil.splitList(listModel, countPerList);
        if (adapterPager == null) {
            adapterPager = new LiveGiftPagerAdapter(listModelPager, getActivity());
        }
        adapterPager.setColNumber(colNumber);
        adapterPager.setListener(new LiveGiftAdapter.LiveGiftAdapterListener() {

            @Override
            public void onClickItem(int position, LiveGiftModel model, LiveGiftAdapter adapter) {
                adapter.getSelectManager().performClick(position);
                adapterPager.clickAdapter(adapter);
                resetClick();
            }
        });
        spv_content.setAdapter(adapterPager);

        if(isLandscape()){
            iv_arrow.setVisibility(INVISIBLE);
        } else {
            iv_arrow.setVisibility(VISIBLE);
        }
        tv_diamonds.setText(StringUtils.formatAccountBalance(1, 2, isLandscape()));
    }

    private boolean isLandscape(){
        return getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    public boolean isShownGift(){
        return spv_content.getViewPager().getAdapter() == adapterPager;
    }
}
