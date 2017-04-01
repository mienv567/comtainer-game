package com.fanwe.live;

import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;

public class LiveInformation
{

    private static LiveInformation instance;

    private String createrId = "";
    private int roomId = 0;
    private String groupId = "";

    private int requestCount = 0;

    public static LiveInformation getInstance()
    {
        if (instance == null)
        {
            synchronized (LiveInformation.class)
            {
                if (instance == null)
                {
                    instance = new LiveInformation();
                }
            }
        }
        return instance;
    }

    public void reduceRequestCount()
    {
        setRequestCount(requestCount - 1);
    }

    public void setRequestCount(int requestCount)
    {
        if (requestCount >= 0 && requestCount <= LiveConstant.VIDEO_VIEW_MAX)
        {
            this.requestCount = requestCount;
        }
    }

    // getter setter

    public int getRequestCount()
    {
        return requestCount;
    }

    public UserModel getUser()
    {
        return UserModelDao.query();
    }

    public boolean isCreater()
    {
        UserModel user = getUser();
        if (user != null)
        {
            return user.getUserId().equals(createrId);
        } else
        {
            return false;
        }
    }

    public int getRoomId()
    {
        return roomId;
    }

    public String getCreaterId()
    {
        return createrId;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void enterRoom(int roomId, String groupId, String createrId)
    {
        this.roomId = roomId;
        this.groupId = groupId;
        this.createrId = createrId;
    }

    public void exitRoom()
    {
        createrId = "";
        roomId = 0;
        groupId = "";
    }

}
