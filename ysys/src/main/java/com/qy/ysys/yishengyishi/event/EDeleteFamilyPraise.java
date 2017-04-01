package com.qy.ysys.yishengyishi.event;

/**
 * 作者：tracy.lee on 2017/1/22 0022 18:43
 */
public class EDeleteFamilyPraise {

    private int mCirclePosition;
    private int mPraiseId;

    public EDeleteFamilyPraise(int circlePosition, int praiseId){
        mCirclePosition = circlePosition;
        mPraiseId = praiseId;
    }

    public int getCirclePosition(){
        return mCirclePosition;
    }

    public int getPraiseId(){
        return mPraiseId;
    }
}
