package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * Created by tony.chen on 2017/1/18.
 */

public class Video_add_commitGroupId extends BaseActModel {
    private static final long serialVersionUID = 1L;
    private int status;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }
}
