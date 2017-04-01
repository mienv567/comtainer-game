package com.fanwe.hybrid.model;

/**
 * 红点判断
 */
public class RedPointModel extends BaseActModel{
    private int sign;//显示为1，说明已经签到了
    private int task;//0-表示没有可领取的任务 1-表示存在没有领取奖励的任务
    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
