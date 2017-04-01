package com.fanwe.live.adapter;

import android.app.Activity;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.library.adapter.SDPagerAdapter;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveGiftAdapter.LiveGiftAdapterListener;
import com.fanwe.live.model.LiveGiftModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveGiftPagerAdapter extends SDPagerAdapter<List<LiveGiftModel>>
{
    private ArrayMap<Integer, LiveGiftAdapter> mMapPositionAdapter = new ArrayMap<>();
    private LiveGiftAdapterListener listener;
    private int colNumber = 4;

    public void setListener(LiveGiftAdapterListener listener)
    {
        this.listener = listener;
    }

    public LiveGiftPagerAdapter(List<List<LiveGiftModel>> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    public void clickAdapter(LiveGiftAdapter adapter)
    {
        for (LiveGiftAdapter value : mMapPositionAdapter.values())
        {
            if (!value.equals(adapter))
            {
                value.getSelectManager().clearSelected();
            }
        }
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    @Override
    public View getView(ViewGroup container, int position)
    {
        View view = getLayoutInflater().inflate(R.layout.item_pager_grid_linear, container, false);

        SDGridLinearLayout ll = (SDGridLinearLayout) view.findViewById(R.id.ll_content);
        ll.setColNumber(colNumber);
        ll.setVerticalStrokeWidth(1);
        ll.setVerticalStrokeColor(getActivity().getResources().getColor(R.color.division));

        ll.setHorizontalStrokeWidth(1);
        ll.setHorizontalStrokeColor(getActivity().getResources().getColor(R.color.division));
        ll.setStrokeCreater(strokeCreater);
        List<LiveGiftModel> listModel = getItemModel(position);
        LiveGiftAdapter adapter = new LiveGiftAdapter(listModel, mActivity);
        adapter.setListener(listener);
        ll.setAdapter(adapter);
        mMapPositionAdapter.put(position, adapter);

        return view;
    }

    private SDGridLinearLayout.StrokeCreater strokeCreater = new SDGridLinearLayout.StrokeCreater()
    {

        @Override
        public View createVertical()
        {
            View view = new View(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, SDViewUtil.dp2px(90));
            view.setLayoutParams(params);
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.division));
            return view;
        }

        @Override
        public View createHorizontal()
        {
            View view = new View(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            view.setLayoutParams(params);
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.division));
            return view;
        }
    };

}
