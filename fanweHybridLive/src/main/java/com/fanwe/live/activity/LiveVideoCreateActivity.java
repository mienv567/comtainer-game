package com.fanwe.live.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.activity.room.LiveActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.CreateLiveData;
import com.fanwe.live.model.Video_add_videoActModel;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by yong.zhang on 2017/3/18 0018.
 */
public class LiveVideoCreateActivity extends BaseActivity {

    @ViewInject(R.id.et_room_title)
    private EditText etRoomTitle;

    @ViewInject(R.id.et_player_number)
    private EditText etPlayerNumber;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_video_create;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        findViewById(R.id.tv_create_video_h).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputText(true);
            }
        });
        findViewById(R.id.tv_create_video_v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputText(false);
            }
        });
    }

    public void onClickClose(View v) {
        finish();
    }

//    public void onClickCreateH(View v) {
//        checkInputText(true);
//    }
//
//    public void onClickCreateV(View v) {
//        checkInputText(false);
//    }

    private void checkInputText(final boolean isHorizontal) {
        final String roomTitle = etRoomTitle.getText().toString().trim();
        if (TextUtils.isEmpty(roomTitle)) {
            SDToast.showToast("房间标题不能个为空");
            return;
        }
        final String playerNumber = etRoomTitle.getText().toString().trim();
        if (TextUtils.isEmpty(playerNumber)) {
            SDToast.showToast("选手编号不能个为空");
            return;
        }
        requestCreateVideo(roomTitle, playerNumber, isHorizontal);
    }

    /**
     * 请求创建直播间
     * 1. 拿到房间id
     * 1. 请求创建im群组
     * 1. 提交im群组给服务器
     * 1. 跳转到直播界面
     */
    private void requestCreateVideo(final String roomTitle,
                                    final String playerNumber, final boolean isHorizontal) {
        CommonInterface.requestAddVideo(roomTitle, 0, null, null, "weixin", 0, 0,
                playerNumber, isHorizontal ? 1 : 0, new AppRequestCallback<Video_add_videoActModel>() {
                    @Override
                    protected void onSuccess(SDResponse resp) {
                        dismissProgressDialog();
                        if (rootModel.isOk()) {

                            CreateLiveData data = new CreateLiveData();
                            data.setRoomId(actModel.getRoomId());
                            data.setVideoType(actModel.getVideo_type());
                            data.setPush_url(actModel.getPushUrl());
                            data.setIsHorizontal(isHorizontal ? 1 : 0);
                            // 因为java后台接口合并
                            data.setVideoActModel(actModel);

                            data.setRtcRole(LiveActivity.RTC_ROLE_ANCHOR);
                            if (TextUtils.isEmpty(actModel.getGroupId())) {
                                Log.i("invite", "开房但是无法获取到群组id");
                                return;
                            }
                            data.setGroup_id(actModel.getGroupId());
                            Log.i("invite", "开房群组id=" + actModel.getGroupId());
                            AppRuntimeWorker.createLive(data, LiveVideoCreateActivity.this);
                            //LiveCreateRoomActivity.this.finish();
                        }
                    }

                    @Override
                    protected void onError(SDResponse resp) {
//                SDToast.showToast("请求房间id失败,httpCode=" + resp.getHttpCode() + " result=" + resp.getResult());
                        Log.i("invite", "add_video errorclass = " + resp.getThrowable().getClass().getSimpleName());
                        SDToast.showToast(
                                getString(R.string.create_room_error));
                        dismissProgressDialog();
                        super.onError(resp);
                    }

                    @Override
                    protected void onFinish(SDResponse resp) {
                        super.onFinish(resp);
                    }
                });
    }
}
