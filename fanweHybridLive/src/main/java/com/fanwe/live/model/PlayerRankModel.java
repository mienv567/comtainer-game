package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * Created by cheng.yuan on 2017/3/30.
 */

public class PlayerRankModel extends BaseActModel {

    public int score;
    public int isUp;
    public UserInfo userInfo;

    public static class UserInfo {
        /**
         * level : 7
         * malaId : 64
         * nickName : 感觉很无赖
         * userId : 69
         * thumbHeadImage : http://q.qlogo.cn/qqapp/1105654638/C7243373E5FD403B3397B85BEF202861/40
         * signature : null
         */

        public int level;
        public int malaId;
        public String nickName;
        public int userId;
        public String thumbHeadImage;
        public String signature;
    }
}
