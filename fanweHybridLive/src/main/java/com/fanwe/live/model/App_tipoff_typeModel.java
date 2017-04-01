package com.fanwe.live.model;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-26 下午6:10:04 类说明
 */
public class App_tipoff_typeModel {
    private long id;
    private String description;
    private int isEffect;

    public int getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(int isEffect) {
        this.isEffect = isEffect;
    }

    public void setEffect(int isEffect) {
        this.isEffect = isEffect;
    }
    public boolean isEffect() {
        return isEffect == 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
