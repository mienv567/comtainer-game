package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.io.Serializable;

/**
 * Created by yong.zhang on 2017/3/16 0016.
 */
public class AppTaskModel extends BaseActModel {

    public MissionInfo missionInfo;

    public MissionProcess missionProcess;

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }

    public void setMissionInfo(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public static class MissionInfo implements Serializable {

        public String missionId;

        public int groupId;

        public String title;

        public String description;

        public String limitTime;

        public int score;
        public String preMissionId;

        public int type;
        public int medal;
    }

    public static class MissionProcess implements Serializable {

        public String startTime;
        public int status;
        public String processId;
        public int userId;
        public int currentMissionId;
        public int missionGroupId;
        public String currentMissionTitle;
    }
}