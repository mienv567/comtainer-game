package com.fanwe.hybrid.utils;


import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.event.ERedPointChange;
import com.fanwe.hybrid.model.RedPointModel;
import com.sunday.eventbus.SDEventManager;

public class RedPointUtil {
    public static void postRedPointEvent(RedPointModel actModel){
            if(actModel.getSign() == 0){
                App.mShowSignRedPoint = true;
            }else{
                App.mShowSignRedPoint = false;
            }
            if(actModel.getTask() == 0){
                App.mShowTaskRedPoint = false;
            }else{
                App.mShowTaskRedPoint = true;
            }
            SDEventManager.post(new ERedPointChange());
    }
}
