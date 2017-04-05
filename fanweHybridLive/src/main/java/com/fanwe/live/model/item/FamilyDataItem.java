package com.fanwe.live.model.item;

import com.fanwe.live.model.MultipleItem;

import java.util.List;

/**
 * 作者：tracy.lee on 2017/1/19 0019 21:47
 */
public class FamilyDataItem extends MultipleItem {

    public int id;
    public String userId;
    public String headImage;
    public String name;
    public String place;
    public String content;
    public String happenTime;
    public List<String> images;
    public List<FamilyCommentItem> comments;

    public FamilyDataItem() {
        super(MultipleItem.BOX_BAR, 2);
    }

    public boolean hasComment() {
        if (comments != null && comments.size() > 0) {
            return true;
        }
        return false;
    }
}
