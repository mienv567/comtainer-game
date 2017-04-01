package com.fanwe.live.model;

import com.fanwe.library.common.SDSelectManager.SDSelectable;

import java.io.Serializable;

public class LRSUserModel implements SDSelectable, Serializable
{
    //狼人1|女巫2|先知3|猎人4|平民5
    public static final int GAME_ROLE_NONE = 0; //表示观众
    public static final int GAME_ROLE_WOLF = 1;
    public static final int GAME_ROLE_WITCH = 2;
    public static final int GAME_ROLE_PROPHET = 3;
    public static final int GAME_ROLE_HUNTER = 4;
    public static final int GAME_ROLE_CIVILIAN = 5;
    public static final int ALIVE_LIVE = 1;
    public static final int ALIVE_NOT_LIVE = 0;
    public static final int IS_LINE = 0;
    public static final int IS_OFFLINE = 1;
    private String user_id; // 用户id
    private String head_image;//用户头像
    private String nick_name;//昵称
    private int identity;//角色
    private int index;//玩家顺序
    private int is_alive = 1; //玩家状态 1表示活着 0 表示死了
    private int is_offline = 0;//玩家状态 0表示在游戏中 1表示退出游戏了
    private boolean showControl = false;//是否显示操作
    private String controlContent;//操作内容 例如"杀7号"
    private boolean isBeChecked  = false;//是否被验过身份  如果是 则对预言家可见
    private boolean speaking = false;//是否正在发言 - 遗言和正常发言
    private int chooseLianMaiTime;//选择连麦时间
    private int speakTime;//发言时间
    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {

    }


    public int getIs_offline() {
        return is_offline;
    }

    public void setIs_offline(int is_offline) {
        this.is_offline = is_offline;
    }

    public int getIs_alive() {
        return is_alive;
    }

    public void setIs_alive(int is_alive) {
        this.is_alive = is_alive;
    }

    public String getControlContent() {
        return controlContent;
    }

    public void setControlContent(String controlContent) {
        this.controlContent = controlContent;
    }

    public boolean isShowControl() {
        return showControl;
    }

    public void setShowControl(boolean showControl) {
        this.showControl = showControl;
    }

    public boolean isBeChecked() {
        return isBeChecked;
    }

    public void setIsBeChecked(boolean isBeChecked) {
        this.isBeChecked = isBeChecked;
    }

    public boolean isSpeaking() {
        return speaking;
    }

    public void setSpeaking(boolean speaking) {
        this.speaking = speaking;
    }

    public int getSpeakTime() {
        return speakTime;
    }

    public void setSpeakTime(int speakTime) {
        this.speakTime = speakTime;
    }

    public int getChooseLianMaiTime() {
        return chooseLianMaiTime;
    }

    public void setChooseLianMaiTime(int chooseLianMaiTime) {
        this.chooseLianMaiTime = chooseLianMaiTime;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }
}
