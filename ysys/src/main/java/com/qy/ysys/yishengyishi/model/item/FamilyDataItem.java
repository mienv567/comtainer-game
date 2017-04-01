package com.qy.ysys.yishengyishi.model.item;

import java.util.List;

/**
 * 作者：tracy.lee on 2017/1/19 0019 21:47
 */
public class FamilyDataItem {
    private int id;
    private int userId;
    private String headImage;
    private String name;
    private String content;
    private String place;
    private String video;
    private String happenTime;
    private String createTime;
    private List<String> images;
    private List<FamilyPraiseItem> praises;
    private List<FamilyCommentItem> comments;
    private boolean isExpand;
    public List<FamilyCommentItem> getComments() {
        return comments;
    }



    public void setComments(List<FamilyCommentItem> comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(String happenTime) {
        this.happenTime = happenTime;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<FamilyPraiseItem> getPraises() {
        return praises;
    }

    public void setPraises(List<FamilyPraiseItem> praises) {
        this.praises = praises;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public boolean hasFavort(){
        if(praises!=null && praises.size()>0){
            return true;
        }
        return false;
    }

    public boolean hasComment(){
        if(comments!=null && comments.size()>0){
            return true;
        }
        return false;
    }


    public int getCurUserFavortId(int curUserId){
        int favortid = -1;
        if(hasFavort()){
            for(FamilyPraiseItem item : praises){
                if(curUserId == item.getUserId()){
                    favortid = item.getId();
                    return favortid;
                }
            }
        }
        return favortid;
    }
}
