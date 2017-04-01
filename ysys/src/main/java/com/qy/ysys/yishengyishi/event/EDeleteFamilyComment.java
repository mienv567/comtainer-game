package com.qy.ysys.yishengyishi.event;

/**
 * 作者：tracy.lee on 2017/1/22 0022 18:01
 */
public class EDeleteFamilyComment {
    private int mCirclePosition;
    private int mCommentId;
    public EDeleteFamilyComment(int circlePosition, int commentId){
        mCirclePosition = circlePosition;
        mCommentId = commentId;
    }

    public int getCirclePosition() {
        return mCirclePosition;
    }

    public int getCommentId() {
        return mCommentId;
    }
}
