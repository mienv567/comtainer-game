package com.fanwe.live.event;

/**
 * 狼人杀游戏开始以及结束都要通知到这个事件
 */
public class ELRSGameStateChange {
    public boolean isGaming;//正在玩游戏  true - 正在玩  false 游戏结束
    public boolean isViewer;//判断是否为观众 游戏开始有进来的 为 true
    public ELRSGameStateChange(boolean isGaming){
        this.isGaming = isGaming;
    }

    public boolean isGaming(){
        return this.isGaming;
    }

    public boolean isViewer() {
        return isViewer;
    }

    public void setIsViewer(boolean isViewer) {
        this.isViewer = isViewer;
    }
}
