package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.PlayBackData;

import org.xutils.view.annotation.ViewInject;

public class ItemLiveCategorySingle extends BaseAppView {

    private LiveRoomModel mLiveRoomModel;

    @ViewInject(R.id.iv_head_image)
    private ImageView iv_head_image;
    @ViewInject(R.id.tv_watch_number)
    private TextView tv_watch_number;
    @ViewInject(R.id.tv_user_name)
    private TextView tv_user_name;
    @ViewInject(R.id.tv_city)
    private TextView tv_city;
    @ViewInject(R.id.iv_gender)
    private ImageView iv_gender;

    public ItemLiveCategorySingle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItemLiveCategorySingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemLiveCategorySingle(Context context) {
        super(context);
        init();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.item_live_category_single);
    }


    public void setModel(LiveRoomModel model) {
        mLiveRoomModel = model;
        if (mLiveRoomModel == null) {
            SDViewUtil.invisible(this);
        } else {
            SDViewBinder.setImageView(getActivity(), iv_head_image, mLiveRoomModel.getHeadImage(), R.drawable.ic_default_head);
            SDViewBinder.setTextView(tv_watch_number, mLiveRoomModel.getVirtualWatchNumber() + "");
            SDViewBinder.setTextView(tv_user_name, mLiveRoomModel.getNickName());
            SDViewBinder.setTextView(tv_city, mLiveRoomModel.getCity());
            if (mLiveRoomModel.getSex() == 1) {
                iv_gender.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_global_male));
            } else {
                iv_gender.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_global_female));
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (mLiveRoomModel != null) {
            if (mLiveRoomModel.getLiveStatus() == 1) {
                JoinLiveData data = new JoinLiveData();
                data.setCreaterId(mLiveRoomModel.getUserId());
                data.setGroupId(mLiveRoomModel.getGroupId());
                data.setLoadingVideoImageUrl(mLiveRoomModel.getHeadImage());
                data.setRoomId(mLiveRoomModel.getRoomId());
                data.setPlay_url(mLiveRoomModel.getPushUrl());
                data.setType(1);
                data.setIsHorizontal(mLiveRoomModel.getIsHorizontal());
                AppRuntimeWorker.joinLive(data, getActivity(), false);
            } else if (mLiveRoomModel.getLiveStatus() == 3) {
                PlayBackData data = new PlayBackData();
                data.setRoomId(mLiveRoomModel.getRoomId());
                data.setPlaybackUrl(mLiveRoomModel.getPlayUrl());
                AppRuntimeWorker.startPlayback(data, getActivity());
            }
        }
    }
}
