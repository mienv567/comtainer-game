package com.qy.ysys.yishengyishi.model.item;

/**
 * 作者：tracy.lee on 2017/1/22 0022 10:59
 */
public class MemoirInfoImageItem {
    private String imageUploadTime;
    private int imageId;
    private String imageUrl;
    private String imageremark;
    private int isCover;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageremark() {
        return imageremark;
    }

    public void setImageremark(String imageremark) {
        this.imageremark = imageremark;
    }

    public String getImageUploadTime() {
        return imageUploadTime;
    }

    public void setImageUploadTime(String imageUploadTime) {
        this.imageUploadTime = imageUploadTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsCover() {
        return isCover;
    }

    public void setIsCover(int isCover) {
        this.isCover = isCover;
    }
}
