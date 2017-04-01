package com.fanwe.live.model;

public class LiveRoomModel
{
    private String headImage;
    private String nickName;
    private String city;
    private String watchNumber;
    private int userLevel;
    private int roomType; // 1-私密直播;3-直播

    private String userId;
    private int roomId;
    private String title;// 话题
    private int cateId;//话题id
    private int liveStatus; //0-结束;1-正在直播;2-创建中;3-回看
    private int videoType; // 0-互动直播;1-直播
    private int v_type;
    private String v_icon;
 // 搞活动时如果有该属性的值则显示logo图标,如果为空就不需要显示
    private String logoUrl;
    private String playUrl;
    private CategoryModel category;
    private int sex; //1-男 0 - 女
    private int virtualWatchNumber;
    /**
     * robotNum : null
     * createTime : null
     * sort : null
     * rtmpPlayUrl : rtmp://pili-live-rtmp.qiankeep.com/mala/104
     * monitorTime : 2017-02-25 10:41:46
     * endTime : null
     * onlineStatus : 1
     * pushUrl : rtmp://pili-publish.qiankeep.com/mala/104?e=1487990509691&token=ZUdTZzOrAMrgTac4e2I-w_F2_NsMCU_IrLeE580r:TcIiXy5okIAe_jtnmGhi4jKcpT8=
     * isDelete : null
     * watchNumber : 0
     * userId : 13
     * province : ???
     * maxWatchNumber : 0
     * isAborted : null
     * privateKey : null
     * isNew : 1
     * isDelVod : null
     * categoryId : 2
     * beginTime : 2017-02-25 10:41:46
     * isHot : 1
     * voteNumber : 0
     * groupId : @TGS#26QEZ3MEK
     * isPlayback : null
     * topicId : 2
     * tipoffCount : null
     * destroyGroupStatus : 1
     * videoVid : null
     * shareType : null
     */

    private Object robotNum;
    private Object createTime;
    private Object sort;
    private String rtmpPlayUrl;
    private String monitorTime;
    private Object endTime;
    private int onlineStatus;
    private String pushUrl;
    private Object isDelete;
    private String province;
    private int maxWatchNumber;
    private Object isAborted;
    private Object privateKey;
    private int isNew;
    private Object isDelVod;
    private int categoryId;
    private String beginTime;
    private int isHot;
    private int charmValue;
    private String groupId;
    private Object isPlayback;
    private int topicId;
    private Object tipoffCount;
    private int destroyGroupStatus;
    private Object videoVid;
    private Object shareType;

    private int isHorizontal;  //是否横屏 1：是  0：否

    public int getIsHorizontal() {
        return isHorizontal;
    }

    public void setIsHorizontal(int isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String url) {
        this.logoUrl = url;
    }
    //add
    public String getLiveState()
    {
        String result = "";
        switch (liveStatus)
        {
            case 1:
                result = "直播中";
                break;
            case 3:
                result = "回看";
                break;
            default:
                break;
        }
        return result;
    }


    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public int getLiveStatus()
    {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus)
    {
        this.liveStatus = liveStatus;
    }

    public int getVideoType()
    {
        return videoType;
    }

    public void setVideoType(int videoType)
    {
        this.videoType = videoType;
    }

    public int getCateId()
    {
        return cateId;
    }

    public void setCateId(int cateId)
    {
        this.cateId = cateId;
    }


    public String getHeadImage()
    {
        return headImage;
    }

    public void setHeadImage(String headImage)
    {
        this.headImage = headImage;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getWatchNumber()
    {
        return watchNumber;
    }

    public void setWatchNumber(String watchNumber)
    {
        this.watchNumber = watchNumber;
    }

    public int getRoomType()
    {
        return roomType;
    }

    public void setRoomType(int roomType)
    {
        this.roomType = roomType;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getRoomId()
    {
        return roomId;
    }

    public void setRoomId(int roomId)
    {
        this.roomId = roomId;
    }

    public int getUserLevel()
    {
        return userLevel;
    }

    public void setUserLevel(int userLevel)
    {
        this.userLevel = userLevel;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getV_type()
    {
        return v_type;
    }

    public void setV_type(int v_type)
    {
        this.v_type = v_type;
    }

    public String getV_icon()
    {
        return v_icon;
    }

    public void setV_icon(String v_icon)
    {
        this.v_icon = v_icon;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getVirtualWatchNumber() {
        return virtualWatchNumber;
    }

    public void setVirtualWatchNumber(int virtualWatchNumber) {
        this.virtualWatchNumber = virtualWatchNumber;
    }

    public Object getRobotNum() {
        return robotNum;
    }

    public void setRobotNum(Object robotNum) {
        this.robotNum = robotNum;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }

    public String getRtmpPlayUrl() {
        return rtmpPlayUrl;
    }

    public void setRtmpPlayUrl(String rtmpPlayUrl) {
        this.rtmpPlayUrl = rtmpPlayUrl;
    }

    public String getMonitorTime() {
        return monitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        this.monitorTime = monitorTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public Object getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Object isDelete) {
        this.isDelete = isDelete;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getMaxWatchNumber() {
        return maxWatchNumber;
    }

    public void setMaxWatchNumber(int maxWatchNumber) {
        this.maxWatchNumber = maxWatchNumber;
    }

    public Object getIsAborted() {
        return isAborted;
    }

    public void setIsAborted(Object isAborted) {
        this.isAborted = isAborted;
    }

    public Object getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(Object privateKey) {
        this.privateKey = privateKey;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public Object getIsDelVod() {
        return isDelVod;
    }

    public void setIsDelVod(Object isDelVod) {
        this.isDelVod = isDelVod;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getCharmValue() {
        return charmValue;
    }

    public void setCharmValue(int charmValue) {
        this.charmValue = charmValue;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Object getIsPlayback() {
        return isPlayback;
    }

    public void setIsPlayback(Object isPlayback) {
        this.isPlayback = isPlayback;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public Object getTipoffCount() {
        return tipoffCount;
    }

    public void setTipoffCount(Object tipoffCount) {
        this.tipoffCount = tipoffCount;
    }

    public int getDestroyGroupStatus() {
        return destroyGroupStatus;
    }

    public void setDestroyGroupStatus(int destroyGroupStatus) {
        this.destroyGroupStatus = destroyGroupStatus;
    }

    public Object getVideoVid() {
        return videoVid;
    }

    public void setVideoVid(Object videoVid) {
        this.videoVid = videoVid;
    }

    public Object getShareType() {
        return shareType;
    }

    public void setShareType(Object shareType) {
        this.shareType = shareType;
    }
}
