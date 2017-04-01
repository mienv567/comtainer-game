package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.ItemLiveTabRecommendFollowSingle;
import com.fanwe.live.model.UserModel;

import java.util.List;

public class LiveTabFollowRecommendAdapter extends SDSimpleAdapter<List<UserModel>>
{

    public LiveTabFollowRecommendAdapter(List<List<UserModel>> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_tab_follow_recommend_room_new;
    }

    public String getSelectedUserIds(){
        String result = "";
        for(List<UserModel> list : listModel){
            for(UserModel userModel : list){
                if(userModel.isSelected()){
                    result += userModel.getUserId()+",";
                }
            }
        }
        if(!TextUtils.isEmpty(result)){
            result = result.substring(0,result.length()-1);
        }
        return result;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, List<UserModel> model)
    {
        ItemLiveTabRecommendFollowSingle item0 = get(R.id.item_view0, convertView);
        ItemLiveTabRecommendFollowSingle item1 = get(R.id.item_view1, convertView);
        ItemLiveTabRecommendFollowSingle item2 = get(R.id.item_view2, convertView);
        item0.setModel(SDCollectionUtil.get(model, 0));
        item1.setModel(SDCollectionUtil.get(model, 1));
        item2.setModel(SDCollectionUtil.get(model, 2));

        item0.setOnClickListener(clickHeadImageListener);
        item1.setOnClickListener(clickHeadImageListener);
        item2.setOnClickListener(clickHeadImageListener);
    }

    private OnClickListener clickHeadImageListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            ItemLiveTabRecommendFollowSingle view = (ItemLiveTabRecommendFollowSingle) v;
            UserModel model = view.getModel();
            model.setSelected(!model.isSelected());
            notifyDataSetChanged();
        }
    };

}
