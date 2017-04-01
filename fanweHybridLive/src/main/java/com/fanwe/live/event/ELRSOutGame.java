package com.fanwe.live.event;

/**
 * 狼人杀 - 用户退出游戏
 */
public class ELRSOutGame {
    private boolean out;
    public ELRSOutGame(boolean out){
        this.out = out;
    }
    public boolean getOut(){
        return this.out;
    }
}
