package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * Created by kevin.liu on 2017/2/28.
 */
public class App_get_review_ActModel extends BaseActModel {


    /**
     * groupId : @TGS#a2IT5CNEJ
     * podcast : {"sex":1,"videoCount":0,"diamonds":0,"malaId":33,"isAgree":1,"useDiamonds":0,"isRemind":1,"nickName":"浦饭の幽助","ticket":0,"refundTicket":0,"authentication":0,"headImage":"http://192.168.1.63:8081/live-web/login/http://q.qlogo.cn/qqapp/1105588451/2414BDFF9322A2827DDD4109CE11DF55/100","userId":38,"userLevel":1,"useableTicket":0,"fansCount":0}
     * reviewUrl : http://pili-media.qiankeep.com/recordings/z1.mala.182/0_1488442496.m3u8
     */

    private String groupId;
    private UserModel podcast;
    private String reviewUrl;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public UserModel getPodcast() {
        return podcast;
    }

    public void setPodcast(UserModel podcast) {
        this.podcast = podcast;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }


}
