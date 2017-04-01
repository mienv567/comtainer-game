package com.fanwe.hybrid.model;


import java.util.List;

/**
 * 任务实体
 */
public class UserTaskItem {
    public static final int TYPE_MAIN = 0; //主线
    public static final int TYPE_DAY = 1; //日常
    public static final int TYPE_OTHER = 2; //其他
    public static final int STATE_NOT_DO = 0;//任务没完成
    public static final int STATE_NOT_GET = 1;//任务完成没领取
    public static final int STATE_DONE = 2;//任务完成并且已经领取
    public static final int GO_TO_MAIN = 0;//去主界面
    public static final int GO_TO_PODCAST = 1;//开播界面
    public static final int GO_TO_INVITE = 2;//邀请界面
    public static final int GO_TO_RECHARGE = 3;//充值界面
    public static final int GO_TO_ME = 4;//完善个人资料界面
    private String task_id;
    private String task_title;
    private List<String> task_experience;
    private int task_type;
    private int task_state;
    private int task_to_do;
    private String task_detail;
    private List<UserTaskItem> sub_task;

    public List<UserTaskItem> getSub_task() {
        return sub_task;
    }

    public void setSub_task(List<UserTaskItem> sub_task) {
        this.sub_task = sub_task;
    }

    public String getTask_detail() {
        return task_detail;
    }

    public void setTask_detail(String task_detail) {
        this.task_detail = task_detail;
    }

    public List<String> getTask_experience() {
        return task_experience;
    }

    public void setTask_experience(List<String> task_experience) {
        this.task_experience = task_experience;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public int getTask_state() {
        return task_state;
    }

    public void setTask_state(int task_state) {
        this.task_state = task_state;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public int getTask_to_do() {
        return task_to_do;
    }

    public void setTask_to_do(int task_to_do) {
        this.task_to_do = task_to_do;
    }

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }
}
