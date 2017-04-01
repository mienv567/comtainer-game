package com.fanwe.live.model;

/**
 * 作者：tracy.lee on 2017/1/11 0011 11:29
 */
public class CategoryModel {
    private String category_name;
    private String category_cover_url;
    private String category_header_url;
    private String category_round_cover_url;
    private String category_desc;
    private String category_color;
    private String category_id;
    private int video_num;
    private HeadCategoryModel head;

    public HeadCategoryModel getHead() {
        return head;
    }

    public void setHead(HeadCategoryModel head) {
        this.head = head;
    }

    public String getCategory_color() {
        return category_color;
    }

    public void setCategory_color(String category_color) {
        this.category_color = category_color;
    }

    public String getCategory_cover_url() {
        return category_cover_url;
    }

    public void setCategory_cover_url(String category_cover_url) {
        this.category_cover_url = category_cover_url;
    }

    public String getCategory_desc() {
        return category_desc;
    }

    public void setCategory_desc(String category_desc) {
        this.category_desc = category_desc;
    }

    public String getCategory_header_url() {
        return category_header_url;
    }

    public void setCategory_header_url(String category_header_url) {
        this.category_header_url = category_header_url;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getVideo_num() {
        return video_num;
    }

    public void setVideo_num(int video_num) {
        this.video_num = video_num;
    }

    public String getCategory_round_cover_url() {
        return category_round_cover_url;
    }

    public void setCategory_round_cover_url(String category_round_cover_url) {
        this.category_round_cover_url = category_round_cover_url;
    }
}
