package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanwe.library.adapter.SDViewHolderAdapter;
import com.fanwe.library.adapter.viewholder.SDViewHolder;
import com.fanwe.library.event.EOnClick;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.LivePlaybackModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.PlayBackData;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class LiveTabFollowAdapter extends SDViewHolderAdapter<Object>
{
    private static SparseArray<Integer> arrFirstPositon = new SparseArray<>();

    public LiveTabFollowAdapter(List<Object> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public void setData(List<Object> list)
    {
        arrFirstPositon.clear();
        super.setData(list);
    }

    @Override
    public SDViewHolder onCreateViewHolder(int position, View convertView, ViewGroup parent)
    {
        int type = getItemViewType(position);
        SDViewHolder viewHolder = null;
        switch (type)
        {
            case 0:
                viewHolder = new ViewHolderNoLive();
                break;
            case 1:
                viewHolder = new ViewHolderLiveRoom();
                break;
            case 2:
                viewHolder = new ViewHolderPlayback();
                break;
            default:

                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position)
    {
        Object model = getItem(position);

        if (model instanceof TypeNoLiveRoomModel)
        {
            return 0;
        } else if (model instanceof LiveRoomModel)
        {
            return 1;
        } else if (model instanceof LivePlaybackModel)
        {
            return 2;
        }

        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 3;
    }


    public static class TypeNoLiveRoomModel
    {

    }

    public static class ViewHolderNoLive extends SDViewHolder
    {

        TextView tv_tab_live_follow_goto_live;
        ImageView iv_no_live;

        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent)
        {
            return R.layout.item_live_tab_follow_no_live;
        }

        @Override
        public void initViews(int position, View convertView, ViewGroup parent)
        {
            tv_tab_live_follow_goto_live = find(R.id.tv_tab_live_follow_goto_live, convertView);
            iv_no_live = find(R.id.iv_no_live, convertView);
        }

        @Override
        public void bindData(int position, View convertView, ViewGroup parent, Object itemModel)
        {
            tv_tab_live_follow_goto_live.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    EOnClick event = new EOnClick(v);
                    SDEventManager.post(event);
                }
            });
        }
    }

    /**
     * 直播间
     */
    public static class ViewHolderLiveRoom extends LiveTabHotAdapter.ViewHolder
    {

        View ll_top;
        ImageView iv_ringgirl_logo; //活动logo
        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent)
        {
            return R.layout.item_live_tab_follow_room;
        }

        @Override
        public void bindData(int position, View convertView, ViewGroup parent, Object itemModel)
        {
            ll_top = find(R.id.ll_top, convertView);

            // 如果返回的数据logo_url字段不为空,则动态请求并显示活动logo
            iv_ringgirl_logo = find(R.id.iv_ringgirl_logo, convertView);
            String logoUrl = ((LiveRoomModel) itemModel).getLogoUrl();
            if(!TextUtils.isEmpty(logoUrl)){
                iv_ringgirl_logo.setVisibility(View.VISIBLE);
                SDViewBinder.setImageView(getActivity(),logoUrl, iv_ringgirl_logo);
            }else{
                iv_ringgirl_logo.setVisibility(View.GONE);
            }            int type = getAdapter().getItemViewType(position);
            Integer typePosition = arrFirstPositon.get(type);
            if (typePosition == null)
            {
                arrFirstPositon.put(type, position);
            }

            typePosition = arrFirstPositon.get(type);
            if (typePosition != null && typePosition == position)
            {
                SDViewUtil.show(ll_top);
            } else
            {
                SDViewUtil.hide(ll_top);
            }

            super.bindData(position, convertView, parent, itemModel);
        }
    }


    /**
     * 回放
     */
    public static class ViewHolderPlayback extends SDViewHolder
    {

        View ll_top;

        LinearLayout ll_content;
        ImageView iv_head;
        ImageView iv_head_small;
        TextView tv_nickname;
        TextView tv_create_time;
        TextView tv_watch_number;
        TextView tv_topic;

        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent)
        {
            return R.layout.item_live_tab_follow_playback;
        }

        @Override
        public void initViews(int position, View convertView, ViewGroup parent)
        {
            ll_top = find(R.id.ll_top, convertView);
            ll_content = find(R.id.ll_content, convertView);
            iv_head = find(R.id.iv_head, convertView);
            iv_head_small = find(R.id.iv_head_small, convertView);
            tv_nickname = find(R.id.tv_nickname, convertView);
            tv_create_time = find(R.id.tv_create_time, convertView);
            tv_watch_number = find(R.id.tv_watch_number, convertView);
            tv_topic = find(R.id.tv_topic, convertView);
        }

        @Override
        public void bindData(int position, View convertView, ViewGroup parent, Object itemModel)
        {
            int type = getAdapter().getItemViewType(position);
            Integer typePosition = arrFirstPositon.get(type);
            if (typePosition == null)
            {
                arrFirstPositon.put(type, position);
            }

            typePosition = arrFirstPositon.get(type);
            if (typePosition != null && typePosition == position)
            {
                SDViewUtil.show(ll_top);
            } else
            {
                SDViewUtil.hide(ll_top);
            }

            final LivePlaybackModel model = (LivePlaybackModel) itemModel;
            SDViewBinder.setImageView(getActivity(),model.getHead_image(), iv_head,R.drawable.ic_default_head);
            if (!TextUtils.isEmpty(model.getV_icon()))
            {
                SDViewUtil.show(iv_head_small);
                SDViewBinder.setImageView(getActivity(),model.getV_icon(), iv_head_small,R.drawable.ic_default_head);
            } else
            {
                SDViewUtil.hide(iv_head_small);
            }

            SDViewBinder.setTextView(tv_nickname, model.getNick_name());
            SDViewBinder.setTextView(tv_create_time, model.getBegin_time_format());
            SDViewBinder.setTextView(tv_watch_number, model.getWatch_number_format());
            SDViewBinder.setTextView(tv_topic, model.getTitle());

            ll_content.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PlayBackData data = new PlayBackData();
                    data.setRoomId(model.getRoom_id());
                    data.setPlaybackUrl(model.getPlayback_url());
                    AppRuntimeWorker.startPlayback(data, getActivity());
                }
            });
        }
    }


}
