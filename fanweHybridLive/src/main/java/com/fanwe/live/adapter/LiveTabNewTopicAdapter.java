package com.fanwe.live.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDViewHolderAdapter;
import com.fanwe.library.adapter.viewholder.SDViewHolder;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveHotTopicActivity;
import com.fanwe.live.activity.LiveTopicRoomActivity;
import com.fanwe.live.model.LiveTopicModel;

import java.util.List;

public class LiveTabNewTopicAdapter extends SDViewHolderAdapter<LiveTopicModel>
{

    public LiveTabNewTopicAdapter(List<LiveTopicModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public SDViewHolder onCreateViewHolder(int position, View convertView, ViewGroup parent) {
        return new ViewHolder();
    }

//    @Override
//    public int getLayoutId(int position, View convertView, ViewGroup parent)
//    {
//        return R.layout.item_live_tab_new_topic;
//    }

//    @Override
//    public void bindData(int position, View convertView, ViewGroup parent, LiveTopicModel model)
//    {
//        LogUtil.e("进来了 ： " + model.getTitle() + " : " + model.getUrl());
//        TextView tv_title = get(R.id.tv_title, convertView);
//        SDViewBinder.setTextView(tv_title, model.getTitle());
//        ImageView img = get(R.id.img,convertView);
//        SDViewBinder.setImageView(getActivity(),model.getUrl(),img,R.drawable.ic_default_head);
//        tv_title.setTag(model);
//        tv_title.setOnClickListener(clickTopicListener);
//    }

//    private View.OnClickListener clickTopicListener = new View.OnClickListener()
//    {
//        @Override
//        public void onClick(View v)
//        {
//            LiveTopicModel model = (LiveTopicModel) v.getTag();
//
//            if (model.getCateId() > 0)
//            {
//                Intent intent = new Intent(mActivity, LiveTopicRoomActivity.class);
//                intent.putExtra(LiveTopicRoomActivity.EXTRA_TOPIC_ID, model.getCateId());
//                intent.putExtra(LiveTopicRoomActivity.EXTRA_TOPIC_TITLE, model.getTitle());
//                mActivity.startActivity(intent);
//            } else
//            {
//                Intent intent = new Intent(mActivity, LiveHotTopicActivity.class);
//                mActivity.startActivity(intent);
//            }
//        }
//    };

    public static class ViewHolder extends SDViewHolder {

        ImageView imageView;
        TextView textView;

        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent) {
            return R.layout.item_live_tab_new_topic;
        }

        @Override
        public void initViews(int position, View convertView, ViewGroup parent) {
            imageView = find(R.id.img,convertView);
            textView = find(R.id.tv_title,convertView);
        }

        @Override
        public void bindData(int position, View convertView, ViewGroup parent, Object itemModel) {
            final LiveTopicModel model = (LiveTopicModel) itemModel;
            SDViewBinder.setTextView(textView, model.getTitle());
            SDViewBinder.setImageView(getActivity(), model.getUrl(), imageView, R.drawable.ic_default_head);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getCate_id() > 0)
                    {
                        Intent intent = new Intent(getActivity(), LiveTopicRoomActivity.class);
                        intent.putExtra(LiveTopicRoomActivity.EXTRA_TOPIC_ID, model.getCate_id());
                        intent.putExtra(LiveTopicRoomActivity.EXTRA_TOPIC_TITLE, model.getTitle());
                        getActivity().startActivity(intent);
                    } else
                    {
                        Intent intent = new Intent(getActivity(), LiveHotTopicActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
            });
        }
    }

}
