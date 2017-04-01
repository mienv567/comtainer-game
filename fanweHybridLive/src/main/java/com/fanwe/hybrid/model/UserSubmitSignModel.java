package com.fanwe.hybrid.model;

import java.util.List;

/**
 * 用户提交签到
 */
public class UserSubmitSignModel extends BaseActModel{
    private List<UserSignInReward> reward;
    private int sign_count;//连续签到天数
    public List<UserSignInReward> getReward() {
        return reward;
    }

    public void setReward(List<UserSignInReward> reward) {
        this.reward = reward;
    }

    public int getSign_count() {
        return sign_count;
    }

    public void setSign_count(int sign_count) {
        this.sign_count = sign_count;
    }
}
