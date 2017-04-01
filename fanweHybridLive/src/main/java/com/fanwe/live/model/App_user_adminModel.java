package com.fanwe.live.model;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-5-31 上午11:19:00 类说明
 */
public class App_user_adminModel
{
    private String id;
    private String nick_name;
    private String head_image;
    private String user_id;
    private int sex;
    private int user_level;

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public int getUser_level()
    {
        return user_level;
    }

    public void setUser_level(int user_level)
    {
        this.user_level = user_level;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNick_name()
    {
        return nick_name;
    }

    public void setNick_name(String nick_name)
    {
        this.nick_name = nick_name;
    }

    public String getHead_image()
    {
        return head_image;
    }

    public void setHead_image(String head_image)
    {
        this.head_image = head_image;
    }

    /**
     * managerId : 7
     * createTime : 2017-03-01 20:03:53
     * thumbHeadImage : headPic/headPic_th7
     * name : 孤峰测试1
     * level : 6
     */

    private int managerId;
    private String createTime;
    private String thumbHeadImage;
    private String name;
    private int level;

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getThumbHeadImage() {
        return thumbHeadImage;
    }

    public void setThumbHeadImage(String thumbHeadImage) {
        this.thumbHeadImage = thumbHeadImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
