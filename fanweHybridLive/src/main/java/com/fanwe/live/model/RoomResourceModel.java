package com.fanwe.live.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @version V1.0 <描述当前版本功能>
 * @author: Tony 活动房间信息实体
 * email:chenchenyanrong@163.com
 * @date: 2016-09-09 14:43
 */
public class RoomResourceModel implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<String > float_pic_url= new ArrayList<String>(); // 点赞图标url的集合
    private String logo_url; // 活动logo的url
    private String activity_ticket_ranking_url ; // 战力值页面跳转地址

    public List<String> getFloat_pic_url() {
        return float_pic_url;
    }

    public void setFloat_pic_url(List<String> float_pic_url) {
        this.float_pic_url = float_pic_url;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getActivity_ticket_ranking_url() {
        return activity_ticket_ranking_url;
    }

    public void setActivity_ticket_ranking_url(String activity_ticket_ranking_url) {
        this.activity_ticket_ranking_url = activity_ticket_ranking_url;
    }
}
