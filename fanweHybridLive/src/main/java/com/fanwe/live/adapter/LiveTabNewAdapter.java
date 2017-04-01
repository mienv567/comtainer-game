package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.ItemLiveTabNewSingle;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.PlayBackData;

import java.util.List;

public class LiveTabNewAdapter extends SDSimpleAdapter<List<LiveRoomModel>>
{
    private ImageView logo; //�
    private String logoUrl; //logo��ַ
    public LiveTabNewAdapter(List<List<LiveRoomModel>> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_tab_new;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, List<LiveRoomModel> model)
    {
        ItemLiveTabNewSingle item0 = get(R.id.item_view0, convertView);
        ItemLiveTabNewSingle item1 = get(R.id.item_view1, convertView);
        ItemLiveTabNewSingle item2 = get(R.id.item_view2, convertView);

        item0.setModel(SDCollectionUtil.get(model, 0));
        item1.setModel(SDCollectionUtil.get(model, 1));
        item2.setModel(SDCollectionUtil.get(model, 2));

        item0.setOnClickListener(clickHeadImageListener);
        item1.setOnClickListener(clickHeadImageListener);
        item2.setOnClickListener(clickHeadImageListener);

    }

    private View.OnClickListener clickHeadImageListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            ItemLiveTabNewSingle view = (ItemLiveTabNewSingle) v;
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
