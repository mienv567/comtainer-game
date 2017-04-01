package com.qy.ysys.yishengyishi.model;

import java.util.List;

/**
 * Created by tony.chen on 2017/1/8.
 */

public class ModelTreeNodes {
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;

    public List<ModelTreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ModelTreeNode> nodes) {
        this.nodes = nodes;
    }

    private List<ModelTreeNode> nodes;


    public ModelTreeNodes() {

    }
}
