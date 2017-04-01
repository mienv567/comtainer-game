package com.qy.ysys.yishengyishi.event;

import com.qy.ysys.yishengyishi.model.CommentConfig;

/**
 * 朋友圈评论
 * 作者：tracy.lee on 2017/1/22 0022 17:42
 */
public class EShowFamilyET {
    private CommentConfig mConfig;
    public EShowFamilyET(CommentConfig config){
        mConfig = config;
    }

    public CommentConfig getConfig() {
        return mConfig;
    }

    public void setConfig(CommentConfig mConfig) {
        this.mConfig = mConfig;
    }
}
