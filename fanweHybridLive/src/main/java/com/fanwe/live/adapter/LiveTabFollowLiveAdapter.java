package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.ItemLiveTabFollowSingle;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.PlayBackData;

import java.util.List;

public class LiveTabFollowLiveAdapter extends SDSimpleAdapter<List<LiveRoomModel>>
{
    private ImageView logo; //�
    private String logoUrl; //logo��ַ
    public LiveTabFollowLiveAdapter(List<List<LiveRoomModel>> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_tab_follow_room_new;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, List<LiveRoomModel> model)
    {
        ItemLiveTabFollowSingle item0 = get(R.id.item_view0, convertView);
        ItemLiveTabFollowSingle item1 = get(R.id.item_view1, convertView);

        item0.setModel(SDCollectionUtil.get(model, 0));
        item1.setModel(SDCollectionUtil.get(model, 1));

        item0.setOnClickListener(clickHeadImageListener);
        item1.setOnClickListener(clickHeadImageListener);

    }

    private OnClickListener clickHeadImageListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            ItemLiveTabFollowSingle view = (ItemLiveTabFollowSingle) v;
            LiveRoomModel model = view.getModel();

            if (model.getLiveStatus() == 1)
            {
                JoinLiveData data = new JoinLiveData();
                data.setCreaterId(model.getUserId());
                data.setGroupId(model.getGroupId());
                data.setLoadingVideoImageUrl(model.getHeadImage());
                data.setRoomId(model.getRoomId());
                data.setType(1);
                AppRuntimeWorker.joinLive(data, getActivity(),false);
            } else if (model.getLiveStatus() == 3)
            {
                PlayBackData data = new PlayBackData();
                data.setRoomId(model.getRoomId());
                data.setPlaybackUrl(model.getPlayUrl());
                AppRuntimeWorker.startPlayback(data, getActivity());
            }
        }
    };

}
