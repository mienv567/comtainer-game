package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabNewTopicAdapter;
import com.fanwe.live.model.LiveTopicModel;
import com.fanwe.live.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class LiveTabNewHeaderView extends BaseAppView
{

    private HorizontalListView listview;
    private LiveTabNewTopicAdapter adapter;
    private List<LiveTopicModel> listModel = new ArrayList<LiveTopicModel>();

    public LiveTabNewHeaderView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabNewHeaderView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveTabNewHeaderView(Context context)
    {
        super(context);
        init();
    }

    @Override
    protected void init()
    {
        super.init();
        setContentView(R.layout.view_live_tab_new_header);
        listview = find(R.id.list_view);

        adapter = new LiveTabNewTopicAdapter(listModel, getActivity());
        listview.setAdapter(adapter);
    }

    public void setListLiveTopicModel(final List<LiveTopicModel> listModel)
    {
        post(new Runnable() {
            @Override
            public void run() {
                adapter.updateData(listModel);
            }
        });

    }

}
