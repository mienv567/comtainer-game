package com.fanwe.live.model;

import java.io.Serializable;

/**
 * Created by yong.zhang on 2017/3/29 0029.
 */

public class LiveChatMsgVo implements Serializable {

    public final static int TYPE_FREIEND = 1;

    public final static int TYPE_ME = 2;

    public int type;

    public String msg;

    public String img;
}
