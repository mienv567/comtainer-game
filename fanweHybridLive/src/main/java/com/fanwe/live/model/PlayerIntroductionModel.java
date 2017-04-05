package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActListModel;

import java.io.Serializable;

/**
 * Created by cheng.yuan on 2017/4/1.
 */

public class PlayerIntroductionModel extends BaseActListModel implements Serializable {
    public int id;
    public int userId;
    public int playerNum;
    public String introduce;
    public String introduceVideo;
    public String livingEnvironment;
    public Object missionName;
    public Object missionStatus;
    public Object firstFansUserId;
    public Object firstFansNickName;
    public int charmScore;
    public int likesCount;
    public Object insideMissionScore;
    public String headImage;
    public String nickName;
    public int sex;

    @Override
    public String toString() {
        return "PlayerIntroductionModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", playerNum=" + playerNum +
                ", introduce='" + introduce + '\'' +
                ", introduceVideo='" + introduceVideo + '\'' +
                ", livingEnvironment='" + livingEnvironment + '\'' +
                ", missionName=" + missionName +
                ", missionStatus=" + missionStatus +
                ", firstFansUserId=" + firstFansUserId +
                ", firstFansNickName=" + firstFansNickName +
                ", charmScore=" + charmScore +
                ", likesCount=" + likesCount +
                ", insideMissionScore=" + insideMissionScore +
                ", headImage='" + headImage + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                '}';
    }
}
