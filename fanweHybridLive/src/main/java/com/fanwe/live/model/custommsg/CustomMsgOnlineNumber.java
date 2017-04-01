package com.fanwe.live.model.custommsg;

import com.fanwe.live.model.UserModel;

import java.util.List;

/**
 * Created by kevin.liu on 2017/3/6.
 */
public class CustomMsgOnlineNumber extends CustomMsg{

    private List<UserModel> viewerList;

    private int count;
    private int has_next;
    private int groupNum; // 观看总人数
    private List<Integer> watch_number_list; //观看人数集合
    public List<UserModel> getViewerList()
    {
        return viewerList;
    }

    public void setViewerList(List<UserModel> viewerList)
    {
        this.viewerList = viewerList;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public int getHas_next()
    {
        return has_next;
    }

    public void setHas_next(int has_next)
    {
        this.has_next = has_next;
    }

    public int getGroupNum()
    {
        return groupNum;
    }

    public void setGroupNum(int groupNum)
    {
        this.groupNum = groupNum;
    }

    public List<Integer> getWatch_number_list() {
        return watch_number_list;
    }

    public void setWatch_number_list(List<Integer> watch_number_list) {
        this.watch_number_list = watch_number_list;
    }
}
