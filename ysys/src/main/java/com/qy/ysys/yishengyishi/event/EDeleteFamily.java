package com.qy.ysys.yishengyishi.event;

/**
 * 作者：tracy.lee on 2017/1/22 0022 18:36
 */
public class EDeleteFamily {
    private int mZoneId;
    public EDeleteFamily(int zoneId){
        mZoneId = zoneId;
    }

    public int getZoneId(){
        return mZoneId;
    }
}
