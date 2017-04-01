package com.fanwe.live.event;

import android.view.MotionEvent;

/**
 * 点击小窗口的用户昵称
 */

public class ETouchSWNickName {
    private String  identifier;
    public ETouchSWNickName(String identifier){
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
