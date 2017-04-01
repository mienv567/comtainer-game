package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * Created by kevin.liu on 2017/2/21.
 */

public class App_UserSimpleModel extends BaseActModel{


    /**
     * age :
     * emotionalState : 你猜
     * headImage : http://q.qlogo.cn/qqapp/1105588451/DA066AE2CF6D77116043E1C4A8CF7EE1/100
     * homeUrl : http://q.qlogo.cn/qqapp/1105588451/DA066AE2CF6D77116043E1C4A8CF7EE1/100
     * job : 主播
     * malaId : 103869
     * nickName : Huanyu the Carpenter
     * sex : 1
     * thumbHeadImage : http://q.qlogo.cn/qqapp/1105588451/DA066AE2CF6D77116043E1C4A8CF7EE1/40
     * userId : 10
     * userLevel : 1
     */

    private String age;
    private String emotionalState;
    private String headImage;
    private String homeUrl;
    private String job;
    private int malaId;
    private String nickName;
    private int sex;
    private String thumbHeadImage;
    private int userId;
    private int userLevel;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(String emotionalState) {
        this.emotionalState = emotionalState;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getMalaId() {
        return malaId;
    }

    public void setMalaId(int malaId) {
        this.malaId = malaId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getThumbHeadImage() {
        return thumbHeadImage;
    }

    public void setThumbHeadImage(String thumbHeadImage) {
        this.thumbHeadImage = thumbHeadImage;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }
}
