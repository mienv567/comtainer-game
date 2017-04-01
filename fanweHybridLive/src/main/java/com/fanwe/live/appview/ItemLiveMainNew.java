package com.fanwe.live.appview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.LiveRoomModel;

/**
 * Created by kevin.liu on 2017/3/17.
 */
public class ItemLiveMainNew extends BaseAppView {

    private ImageView iv_head_image;
    private LiveRoomModel model;


    public ItemLiveMainNew(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItemLiveMainNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemLiveMainNew(Context context) {
        super(context);
        init();
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.item_live_main_new_content);
        iv_head_image = find(R.id.iv_head_image);
    }


    public void setModel(LiveRoomModel roomModel) {
        this.model = roomModel;
        if (roomModel != null && !TextUtils.isEmpty(roomModel.getLogoUrl())) {
            SDViewBinder.setImageView(getActivity(), iv_head_image, roomModel.getLogoUrl(), R.drawable.ic_logo);
        }
    }

    public void onClick(View v) {
        super.onClick(v);
        if (model != null) {
            JoinLiveData data = new JoinLiveData();
            data.setCreaterId(model.getUserId());
            data.setGroupId(model.getGroupId());
            data.setLoadingVideoImageUrl(model.getHeadImage());
            data.setRoomId(model.getRoomId());
            data.setPlay_url(model.getPushUrl());
            data.setIsHorizontal(model.getIsHorizontal());
            data.setType(1);
            AppRuntimeWorker.joinLive(data, getActivity(), false);
        }
    }


}
