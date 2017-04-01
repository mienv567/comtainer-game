package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.hybrid.model.UserTaskItem;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.live.R;

import java.util.List;

public class UserCompletedTaskAdapter extends SDSimpleAdapter<UserTaskItem>
{
    public UserCompletedTaskAdapter(List<UserTaskItem> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_complete_task;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final UserTaskItem model)
    {
        TextView tv_title = get(R.id.tv_title, convertView);
        TextView tv_experience_0 = get(R.id.tv_experience_0, convertView);
        TextView tv_experience_1 = get(R.id.tv_experience_1, convertView);
        tv_title.setText(model.getTask_title());
        if(model.getTask_experience() != null && model.getTask_experience().size() > 0){
            if(!TextUtils.isEmpty(model.getTask_experience().get(0))){
                tv_experience_0.setText(model.getTask_experience().get(0));
            }else{
                tv_experience_0.setVisibility(View.GONE);
            }
            if(model.getTask_experience().size() == 2 && !TextUtils.isEmpty(model.getTask_experience().get(1))){
                tv_experience_1.setText(model.getTask_experience().get(1));
            }else{
                tv_experience_1.setVisibility(View.GONE);
            }
        }else{
            tv_experience_0.setVisibility(View.GONE);
            tv_experience_1.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
