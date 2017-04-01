package com.qy.ysys.yishengyishi.model;

/**
 * Created by tony.chen on 2017/1/5.
 */

public class ModelFramilyChat {
    public static final int NORMAL = 1;
    public static final int CUSTOM = 0;

    private String groupName;

    public ModelFramilyChat(String groupName, int menberCount, int modelType) {
        this.groupName = groupName;
        this.modelType = modelType;
        this.menberCount = menberCount;
    }

    private int modelType;

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public int getMenberCount() {
        return menberCount;
    }

    public void setMenberCount(int menberCount) {
        this.menberCount = menberCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private int menberCount;

    public ModelFramilyChat() {

    }
}
