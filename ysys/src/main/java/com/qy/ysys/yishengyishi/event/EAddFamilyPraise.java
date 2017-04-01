package com.qy.ysys.yishengyishi.event;

/**
 * 作者：tracy.lee on 2017/1/22 0022 18:42
 */
public class EAddFamilyPraise {
    private int mCirclePosition;
    private int mZoneId;
    public EAddFamilyPraise(int circlePosition,int zoneId){
        mCirclePosition = circlePosition;
        mZoneId = zoneId;
    }

    public int getCirclePosition(){
        return mCirclePosition;
    }

    public int getZoneId(){
        return mZoneId;
    }
}
