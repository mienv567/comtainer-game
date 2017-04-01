package com.fanwe.hybrid.model;

import java.util.List;

/**
 * 用户任务
 */
public class UserTaskModel extends BaseActModel{
    private List<UserTaskItem> main_task;

    public List<UserTaskItem> getMain_task() {
        return main_task;
    }

    public void setMain_task(List<UserTaskItem> main_task) {
        this.main_task = main_task;
    }
}
