package com.fanwe.live.music.effect;

import com.fanwe.fkmusic.FkPlayer;

/**
 * Created by ldh on 16/6/24.
 */
public class PlayCtrl {
    public int bzVol;
    public int micVol;
    public int eqModel;
    public int pitchShift;

    public PlayCtrl() {
        bzVol = 50;
        micVol = 100;
        eqModel = FkPlayer.EQ_T_NONE;
        pitchShift = 0;
    }
}
