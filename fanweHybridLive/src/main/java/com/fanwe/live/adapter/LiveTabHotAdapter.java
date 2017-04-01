package com.fanwe.live.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDViewHolderAdapter;
import com.fanwe.library.adapter.viewholder.SDViewHolder;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveTopicRoomActivity;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.PlayBackData;

import java.util.List;

public class LiveTabHotAdapter extends SDViewHolderAdapter<LiveRoomModel> {

    public LiveTabHotAdapter(List<LiveRoomModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public SDViewHolder onCreateViewHolder(int position, View convertView, ViewGroup parent) {
        return new ViewHolder();
    }


    public static class ViewHolder extends SDViewHolder {

        ImageView iv_head;
        ImageView iv_head_small;
        TextView tv_nickname;
        TextView tv_city;
        TextView tv_watch_number;
        ImageView iv_room_image;
        TextView tv_topic;
        TextView tv_live_state;
        ImageView ic_ringgirl_logo;
//        LinearLayout ll_askfocus; // 求关注的按钮

        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent) {
            return R.layout.item_live_tab_hot;
        }

        @Override
        public void initViews(int position, View convertView, ViewGroup parent) {
            iv_head = find(R.id.iv_head, convertView);
            iv_head_small = find(R.id.iv_head_small, convertView);
            tv_nickname = find(R.id.tv_nickname, convertView);
            tv_city = find(R.id.tv_city, convertView);
            tv_watch_number = find(R.id.tv_watch_number, convertView);
            iv_room_image = find(R.id.iv_room_image, convertView);
            tv_topic = find(R.id.tv_topic, convertView);
            tv_live_state = find(R.id.tv_live_state, convertView);
            ic_ringgirl_logo = find(R.id.iv_ringgirl_logo, convertView);
//            ll_askfocus = find(R.id.ll_askfocus, convertView);
        }

        @Override
        public void bindData(int position, View convertView, ViewGroup parent, Object itemModel) {
            final LiveRoomModel model = (LiveRoomModel) itemModel;

            SDViewBinder.setImageView(getActivity(),model.getHeadImage(), iv_head,R.drawable.ic_default_head);
            if (!TextUtils.isEmpty(model.getV_icon())) {
                SDViewUtil.show(iv_head_small);
                SDViewBinder.setImageView(getActivity(),model.getV_icon(), iv_head_small,R.drawable.ic_default_head);
            } else {
                SDViewUtil.hide(iv_head_small);
            }
            SDViewBinder.setTextViewsVisibility(tv_live_state, model.getLiveState());
            SDViewBinder.setTextView(tv_nickname, model.getNickName());
            SDViewBinder.setTextView(tv_city, model.getCity(), SDResourcesUtil.getString(R.string.live_city_default));
            SDViewBinder.setTextView(tv_watch_number, model.getWatchNumber());
            SDViewBinder.setImageView(getActivity(),model.getHeadImage(), iv_room_image);

            if (model.getCateId() > 0) {
                tv_topic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LiveTopicRoomActivity.class);
                        intent.putExtra(LiveTopicRoomActivity.EXTRA_TOPIC_ID, model.getCateId());
                        intent.putExtra(LiveTopicRoomActivity.EXTRA_TOPIC_TITLE, model.getTitle());
                        getActivity().startActivity(intent);
                    }
                });
                SDViewBinder.setTextView(tv_topic, model.getTitle());
                SDViewUtil.show(tv_topic);
            } else {
                SDViewUtil.hide(tv_topic);
            }


            String logoUrl = model.getLogoUrl();
            if (!TextUtils.isEmpty(logoUrl)) {
                ic_ringgirl_logo.setVisibility(View.VISIBLE);
                SDViewBinder.setImageView(getActivity(),logoUrl, ic_ringgirl_logo);
            } else {
                ic_ringgirl_logo.setVisibility(View.GONE);
            }
            iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, model.getUserId());
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, model.getHeadImage());
                    getActivity().startActivity(intent);
                }
            });
            iv_room_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getLiveStatus() == 1) {
                        JoinLiveData data = new JoinLiveData();
                        data.setCreaterId(model.getUserId());
                        data.setGroupId(model.getGroupId());
                        data.setLoadingVideoImageUrl(model.getHeadImage());
                        data.setRoomId(model.getRoomId());
                        data.setType(0);
                        data.setVideoType(model.getVideoType());

                        if (model.getVideoType() == 0) {
                            AppRuntimeWorker.joinLive(data, getActivity(),false);
                        } else {
                            AppRuntimeWorker.joinPullLive(data, getActivity(),false);
                        }
                    } else if (model.getLiveStatus() == 3) {
                        PlayBackData data = new PlayBackData();
                        data.setRoomId(model.getRoomId());
                        data.setPlaybackUrl(model.getPlayUrl());
                        AppRuntimeWorker.startPlayback(data, getActivity());
                    }
                }
            });

//            ll_askfocus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SDToast.showToast("当前用户id" + model.getUserId());
//                }
//            });
        }
    }
}
