package com.fanwe.live.appview.room;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioGroup;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.LiveLikeModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.live.R;
import com.fanwe.live.appview.LiveGiftView;
import com.fanwe.live.appview.LiveLikeView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_propActModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.UserModel;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

public class RoomSendGiftOrLikeView extends RoomView {

    @ViewInject(R.id.radio_group)
    private RadioGroup radio_group;
    @ViewInject(R.id.send_gift_view)
    private LiveGiftView send_gift_view;
    @ViewInject(R.id.send_like_view)
    private LiveLikeView send_like_view;

    private UserModel userModel;

    public RoomSendGiftOrLikeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomSendGiftOrLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomSendGiftOrLikeView(Context context) {
        super(context);
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_gift_or_like;
    }

    @Override
    protected void baseConstructorInit() {
        register();
        initData();
        bindUserData();
    }

    public void register() {
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_btn_props://
                        send_gift_view.setVisibility(VISIBLE);
                        send_like_view.setVisibility(GONE);
                        break;
                    case R.id.radio_btn_like:
                        send_gift_view.setVisibility(GONE);
                        send_like_view.setVisibility(VISIBLE);
                        break;
                }
            }
        });
    }

    private void initData() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            List<LiveGiftModel> giftModels = initActModel.getProps();
            send_gift_view.setData(giftModels);
            List<LiveLikeModel> likeModels = initActModel.getLikeProps();
            send_like_view.setData(likeModels);
        } else {
            requestData();
        }
    }

    private void requestData() {
        CommonInterface.requestGift(getLiveInfo().getRoomInfo().getCate_id(),
                new AppRequestCallback<App_propActModel>() {
                    @Override
                    protected void onSuccess(SDResponse sdResponse) {
                        send_gift_view.setData(actModel.getClassification());
                        send_like_view.setData(actModel.getLikePros());
                    }
                });
    }

    public void bindUserData() {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<UserModel>() {
            @Override
            public UserModel onBackground() {
                return UserModelDao.query();
            }

            @Override
            public void onMainThread(UserModel result) {
                send_gift_view.SetUserModel(result);
                send_like_view.SetUserModel(result);
                send_gift_view.updateDiamonds(result.getDiamonds());
                send_like_view.updateAvailableLike(result.getLikesClicks());
            }
        });
    }

    @Override
    protected boolean onTouchDownOutside(MotionEvent ev) {
        invisible(true);
        return true;
    }

    @Override
    public boolean onBackPressed() {
        invisible(true);
        return true;
    }
}
