package com.fanwe.live.adapter;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.event.EGoToHotTab;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.UserSubmitSignModel;
import com.fanwe.hybrid.model.UserTaskItem;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveCreateRoomActivity;
import com.fanwe.live.activity.LiveCreaterAgreementActivity;
import com.fanwe.live.activity.LiveMainActivity;
import com.fanwe.live.activity.LiveRechargeActivity;
import com.fanwe.live.activity.LiveUserEditActivity;
import com.fanwe.live.activity.UserInviteActivity;
import com.fanwe.live.activity.UserTaskActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.UserTaskDetailDialog;
import com.fanwe.live.model.UserModel;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

public class UserTaskAdapter extends BaseExpandableListAdapter {
    private List<UserTaskItem> mList;
    private Activity mActivity;

    public UserTaskAdapter(Activity activity){
        mActivity = activity;
    }



    public void setList(List<UserTaskItem> list) {
        mList = list;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition < mList.size()){
            UserTaskItem item = mList.get(groupPosition);
            if(item != null){
                if(item.getSub_task() != null && item.getSub_task().size() > 0){
                    return item.getSub_task().size();
                }
            }
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        UserTaskItem item = mList.get(groupPosition);
        if(item != null){
            if(item.getSub_task() != null && item.getSub_task().size() > 0){
                UserTaskItem sub = item.getSub_task().get(childPosition);
                if(sub != null){
                    return sub;
                }
            }
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        final MainTaskItemHolder holder;
        if(convertView == null){
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_main_task,null);
            holder = createMainTaskItemHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (MainTaskItemHolder)convertView.getTag();
        }
        final UserTaskItem item = mList.get(groupPosition);
        holder.tv_title.setText(item.getTask_title());
        if(item.getTask_experience() != null && item.getTask_experience().size() > 0){
            holder.tv_experience.setText(getExperienceFormatString(item.getTask_experience()));
        }else{
            holder.tv_experience.setVisibility(View.GONE);
        }
        hideAllText(holder);
        if(item.getSub_task() != null && item.getSub_task().size() > 0 ){
            holder.img_arrow.setVisibility(View.VISIBLE);
            if (isExpanded) {
                holder.img_arrow.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_arrow_up_gray));
            } else {
                holder.img_arrow.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_arrow_down_gray));
            }
        }else{
            holder.img_arrow.setVisibility(View.GONE);
            judgeByState(holder,item);
        }
        return convertView;
    }

    private String getExperienceFormatString(List<String> list){
        String result = "";
        if(list != null && list.size() > 0){
            for(String str : list){
                result += str + ",";
            }
        }
        if(!TextUtils.isEmpty(result)){
            result = result.substring(0,result.lastIndexOf(","));
        }
        return result;
    }

    private String renameTitle(UserTaskItem item){
        String result = item.getTask_title();
        switch (item.getTask_type()){
            case UserTaskItem.TYPE_MAIN:
                result += "(主线)";
                break;
            case UserTaskItem.TYPE_DAY:
                result += "(日常)";
                break;
            case UserTaskItem.TYPE_OTHER:
                result += "(其他)";
                break;
        }
        return result;
    }

    private void hideAllText(TaskItemHolder holder){
        holder.tv_detail.setVisibility(View.GONE);
        holder.tv_go_to_work.setVisibility(View.GONE);
        holder.tv_get.setVisibility(View.GONE);
        holder.tv_done.setVisibility(View.GONE);
    }

    /**
     * 存在没有领取奖励的任务
     * @return
     */
    public boolean hasTaskCanGet(){
        boolean result = false;
        if(mList != null && mList.size() > 0) {
            for (UserTaskItem item : mList) {
                if (item.getTask_state() == UserTaskItem.STATE_NOT_GET) {
                    result = true;
                    break;
                }
                List<UserTaskItem> subList = item.getSub_task();
                if (subList != null && subList.size() > 0) {
                    for (UserTaskItem sub : subList) {
                        if (sub.getTask_state() == UserTaskItem.STATE_NOT_GET) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    private void judgeByState(final TaskItemHolder holder,final UserTaskItem item){
        switch (item.getTask_state()){
            case UserTaskItem.STATE_NOT_DO:
                if(!TextUtils.isEmpty(item.getTask_detail())){
                    holder.tv_detail.setVisibility(View.VISIBLE);
                    holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserTaskDetailDialog dialog = new UserTaskDetailDialog(mActivity,item);
                            dialog.show();
                        }
                    });
                }else{
                    holder.tv_go_to_work.setVisibility(View.VISIBLE);
                    holder.tv_go_to_work.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToWork(item);
                        }
                    });
                }
                break;
            case UserTaskItem.STATE_NOT_GET:
                holder.tv_get.setVisibility(View.VISIBLE);
                holder.tv_get.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //弹框显示奖励 修改对应实体的状态 如果是主线任务 完成之后刷新一次
                        CommonInterface.requestGetTaskReward(item.getTask_id(),item.getTask_type(), new AppRequestCallback<UserSubmitSignModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                if (rootModel.isOk()) {
                                    if (item.getTask_type() == UserTaskItem.TYPE_MAIN) {
                                        ((UserTaskActivity) mActivity).requestData();
                                    } else {
                                        item.setTask_state(UserTaskItem.STATE_DONE);
                                        notifyDataSetChanged();
                                    }
                                    SDToast.showToast(SDResourcesUtil.getString(R.string.already_get_task_reward)+holder.tv_experience.getText());

//                                    UserSignSuccessDialog dialog = new UserSignSuccessDialog(mActivity, actModel);
//                                    dialog.show();
//                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                        @Override
//                                        public void onDismiss(DialogInterface dialog) {
//
//                                        }
//                                    });
                                }
                            }
                        });
                    }
                });
                break;
            case UserTaskItem.STATE_DONE:
                holder.tv_done.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void goToWork(UserTaskItem item){
        Intent intent;
        switch (item.getTask_to_do()){
            case UserTaskItem.GO_TO_MAIN:
                intent = new Intent(mActivity, LiveMainActivity.class);
                mActivity.startActivity(intent);
                SDEventManager.post(new EGoToHotTab());
                break;
            case UserTaskItem.GO_TO_PODCAST:
                final UserModel userModel = UserModelDao.query();
                if (userModel.getIsAgree() == 1) {
                    intent = new Intent(mActivity, LiveCreateRoomActivity.class);
                    mActivity.startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LiveCreaterAgreementActivity.class);
                    mActivity.startActivity(intent);
                }
                break;
            case UserTaskItem.GO_TO_INVITE:
                intent = new Intent(mActivity, UserInviteActivity.class);
                mActivity.startActivity(intent);
                break;
            case UserTaskItem.GO_TO_RECHARGE:
                intent = new Intent(mActivity, LiveRechargeActivity.class);
                mActivity.startActivity(intent);
                break;
            case UserTaskItem.GO_TO_ME:
                intent = new Intent(mActivity, LiveUserEditActivity.class);
                mActivity.startActivity(intent);
                break;
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TaskItemHolder holder;
        if(convertView == null){
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_sub_task,null);
            holder = createSubTaskItemHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (TaskItemHolder)convertView.getTag();
        }
        UserTaskItem mainItem = mList.get(groupPosition);
        if(mainItem.getSub_task() != null && mainItem.getSub_task().size() > 0){
            final UserTaskItem subItem = mainItem.getSub_task().get(childPosition);
            holder.tv_title.setText(subItem.getTask_title());
            if(subItem.getTask_experience() != null && subItem.getTask_experience().size() > 0){
                holder.tv_experience.setText(getExperienceFormatString(subItem.getTask_experience()));
            }else{
                holder.tv_experience.setVisibility(View.GONE);
            }
            hideAllText(holder);
            judgeByState(holder, subItem);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }



    private MainTaskItemHolder createMainTaskItemHolder(View convertView){
        MainTaskItemHolder holder = new MainTaskItemHolder();
        holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        holder.tv_experience = (TextView) convertView.findViewById(R.id.tv_experience);
        holder.img_arrow = (ImageView) convertView.findViewById(R.id.img_arrow);
        holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
        holder.tv_go_to_work = (TextView) convertView.findViewById(R.id.tv_go_to_work);
        holder.tv_get = (TextView) convertView.findViewById(R.id.tv_get);
        holder.tv_done = (TextView) convertView.findViewById(R.id.tv_done);
        return holder;
    }

    private TaskItemHolder createSubTaskItemHolder(View convertView){
        TaskItemHolder holder = new TaskItemHolder();
        holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        holder.tv_experience = (TextView) convertView.findViewById(R.id.tv_experience);
        holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
        holder.tv_go_to_work = (TextView) convertView.findViewById(R.id.tv_go_to_work);
        holder.tv_get = (TextView) convertView.findViewById(R.id.tv_get);
        holder.tv_done = (TextView) convertView.findViewById(R.id.tv_done);
        return holder;
    }

    static class MainTaskItemHolder extends TaskItemHolder{
        public ImageView img_arrow;//箭头向上
    }

    static class TaskItemHolder{
        public TextView tv_title; //标题
        public TextView tv_experience; //经验
        public TextView tv_detail;//详情
        public TextView tv_go_to_work;//去完成
        public TextView tv_get;//领取
        public TextView tv_done;//已完成
    }

}
