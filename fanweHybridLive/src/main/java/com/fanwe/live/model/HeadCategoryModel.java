package com.fanwe.live.model;

/**
 * 作者：tracy.lee on 2017/1/11 0011 11:31
 */
public class HeadCategoryModel {
    public static final int GO_TO_ROOM = 1;
    public static final int GO_TO_CATEGORY = 0;
    private int type;
    private String url;//封面
    private String round_url;//圆角封面
    private String head_image;
    private String nick_name;
    private String thumb_head_image;
    private String v_type;
    private String v_icon;
    private String user_id;
    private String group_id;
    private String city;
    private String title;
    private String cate_id;
    private String live_in;
    private String video_type;
    private String room_type;
    private String virtual_watch_number;
    private int room_id;
    private String category_id;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getLive_in() {
        return live_in;
    }

    public void setLive_in(String live_in) {
        this.live_in = live_in;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getThumb_head_image() {
        return thumb_head_image;
    }

    public void setThumb_head_image(String thumb_head_image) {
        this.thumb_head_image = thumb_head_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getV_icon() {
        return v_icon;
    }

    public void setV_icon(String v_icon) {
        this.v_icon = v_icon;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getVirtual_watch_number() {
        return virtual_watch_number;
    }

    public void setVirtual_watch_number(String virtual_watch_number) {
        this.virtual_watch_number = virtual_watch_number;
    }

    public String getRound_url() {
        return round_url;
    }

    public void setRound_url(String round_url) {
        this.round_url = round_url;
    }
}
