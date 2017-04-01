package com.fanwe.shopping.model;

/**
 * Created by luodong on 2016/9/18.
 */
public class ShopGoodsDetailModel
{
    private int id;
    private int podcast_id;
    private String name;
    private String imgs;
    private String price;
    private String url;
    private String description;
    private String kdr_cost;

    public int getPodcast_id() {
        return podcast_id;
    }

    public void setPodcast_id(int podcast_id) {
        this.podcast_id = podcast_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKdr_cost() {
        return kdr_cost;
    }

    public void setKdr_cost(String kdr_cost) {
        this.kdr_cost = kdr_cost;
    }
}
