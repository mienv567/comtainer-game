package com.fanwe.live.model;

public class JoinLiveData {
    private int type;// 直播的类型，仅用于观众时候需要传入0-热门;1-最新;2-关注(int)
    private int roomId;
    private String groupId;
    private String createrId;
    private String loadingVideoImageUrl;
    private String city; // 城市，上下拉的时候过滤
    private int sex;//性别，0-全部，1-男，2-女。用于上下拉的时候过滤
    private int cate_id;//话题id，用于上下拉的时候过滤
    private String privateKey;
    private int is_small_screen;//是否小屏
    private int videoType; //0-互动直播，1-直播

    private int isHorizontal;  //是否横屏 1：是  0：否

    public int getIsHorizontal() {
        return isHorizontal;
    }

    public void setIsHorizontal(int isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    // 七牛相关
    private String play_url;

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public int getVideoType() {
        return videoType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getLoadingVideoImageUrl() {
        return loadingVideoImageUrl;
    }

    public void setLoadingVideoImageUrl(String loadingVideoImageUrl) {
        this.loadingVideoImageUrl = loadingVideoImageUrl;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public int getIs_small_screen() {
        return is_small_screen;
    }

    public void setIs_small_screen(int is_small_screen) {
        this.is_small_screen = is_small_screen;
    }
}
