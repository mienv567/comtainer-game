package com.fanwe.hybrid.model;

import com.fanwe.live.model.UserModel;

/**
 * Created by kevin.liu on 2017/2/24.
 */

public class AppAddVideoModel extends BaseActModel{


    /**
     * share : {"shareTitle":"辣就这样吧，瞧我播一下","shareUrl":"http://www.qg8.com//index?c=share&user_id=28&video_id=80","shareImgUrl":"http://192.168.1.63:8081/live-web/login/http://q.qlogo.cn/qqapp/1105588451/DA066AE2CF6D77116043E1C4A8CF7EE1/100","shareKey":80,"shareContent":"辣就这样吧，瞧我播一下Huanyu the Carpenter正在直播,快来一起看~"}
     * hasLianmai : 0
     * podcast : {"sex":1,"videoCount":0,"diamonds":0,"isAgree":0,"useDiamonds":0,"isRemind":1,"nickName":"Huanyu the Carpenter","ticket":0,"refundTicket":0,"authentication":0,"headImage":"http://192.168.1.63:8081/live-web/login/http://q.qlogo.cn/qqapp/1105588451/DA066AE2CF6D77116043E1C4A8CF7EE1/100","userId":28,"userLevel":1,"useableTicket":0,"fansCount":0}
     * push_url : rtmp://pili-publish.qiankeep.com/mala/80?e=1487908389&token=ZUdTZzOrAMrgTac4e2I-w_F2_NsMCU_IrLeE580r:wTFqkGHdZC3LHXKxyU1g0SCSf1w=
     * watchNum : 0
     * group_id : @TGS#2KLCM2MEK
     */

    private ShareBean share;
    private int hasLianmai;
    private UserModel podcast;
    private String push_url;
    private long watchNum;
    private String group_id;

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    public int getHasLianmai() {
        return hasLianmai;
    }

    public void setHasLianmai(int hasLianmai) {
        this.hasLianmai = hasLianmai;
    }

    public UserModel getPodcast() {
        return podcast;
    }

    public void setPodcast(UserModel podcast) {
        this.podcast = podcast;
    }

    public String getPush_url() {
        return push_url;
    }

    public void setPush_url(String push_url) {
        this.push_url = push_url;
    }

    public long getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(long watchNum) {
        this.watchNum = watchNum;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public static class ShareBean {
        /**
         * shareTitle : 辣就这样吧，瞧我播一下
         * shareUrl : http://www.qg8.com//index?c=share&user_id=28&video_id=80
         * shareImgUrl : http://192.168.1.63:8081/live-web/login/http://q.qlogo.cn/qqapp/1105588451/DA066AE2CF6D77116043E1C4A8CF7EE1/100
         * shareKey : 80
         * shareContent : 辣就这样吧，瞧我播一下Huanyu the Carpenter正在直播,快来一起看~
         */

        private String shareTitle;
        private String shareUrl;
        private String shareImgUrl;
        private int shareKey;
        private String shareContent;

        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getShareImgUrl() {
            return shareImgUrl;
        }

        public void setShareImgUrl(String shareImgUrl) {
            this.shareImgUrl = shareImgUrl;
        }

        public int getShareKey() {
            return shareKey;
        }

        public void setShareKey(int shareKey) {
            this.shareKey = shareKey;
        }

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }
    }


}
