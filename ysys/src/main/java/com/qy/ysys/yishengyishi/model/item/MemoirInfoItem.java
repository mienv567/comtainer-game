package com.qy.ysys.yishengyishi.model.item;

import java.util.List;

/**
 * 作者：tracy.lee on 2017/1/23 0023 11:42
 */
public class MemoirInfoItem extends MemoirListItem {
    private List<MemoirInfoImageItem> images;

    public List<MemoirInfoImageItem> getImages() {
        return images;
    }

    public void setImages(List<MemoirInfoImageItem> images) {
        this.images = images;
    }
}
