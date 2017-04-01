package com.fanwe.live.model;

import android.text.TextUtils;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.common.SDSelectManager.SDSelectable;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EUserLoginSuccess;
import com.fanwe.live.utils.LiveUtils;
import com.sunday.eventbus.SDEventManager;

import java.io.Serializable;
import java.util.Map;

public class UserModel extends BaseActModel implements SDSelectable, Serializable
{
    private String userId = ""; // 用户id
    private String nickName; // 昵称
    private String signature; // 签名
    private int sex; // 0-未知，1-男，2-女
    private String city; // 所在城市
    private String province;//所在省份
    private String emotionalState;//情感状态
    private String birthday;//生日
    private int isAuthentication;// "0",//是否认证 0指未认证  1指待审核 2指认证 3指审核不通
    private String job;//职业
    private int isEditSex;//是否已编辑性别(只能编辑一次)
    private long focusCount; // 关注数量
    private String headImage; // 头像
    private long fansCount; // 粉丝数量
    private long ticket; // 钱票数量
    private long useableTicket;//可用钱票数量
    private int userLevel; // 用户等级
    private long useDiamonds; // 累计消费的钻石数量
    private long diamonds; // 钻石数量
    private String v_type;// 认证类型 0 未认证 1 普通 2企业
    private String v_icon;// 认证图标
    private String v_explain;// 认证说明
    private String homeUrl;// 主页
    private Map<String, String> item;// 用户其他信息列表
    private int followId; // 是否关注这个粉丝;0:未关注; >0：已关注
    private long videoCount;// 直播数
    private int isAgree;//是否同意直播协议 0 表示不同意 1表示同意
    private int isRemind;//是否接收推送消息 0-不接收，1-接收
    // add
    private String nickNameFormat;
    private int likesCount;//收到的点赞数
    private int likesClicks;//今日可用点赞数

    //认证相关
    private String authenticationType;
    private String authenticationName;
    private String contact;
    private String fromPlatform;
    private String wiki;
    private String identifyNumber;
    private String identifyPositiveImage;
    private String identifyNagativeImage;
    private String identifyHoldImage;
    //竞拍直播添加参数
    private int showUserOrder;//是否显示【我的订单】 0否 1是
    private int userOrder;//我的订单数(观众)
    private int showUserPai;//我的订单数(观众)
    private int userPai;//我的竞拍数（观众）
    private int showPodcastOrder;//是否显示星店订单(主播) 0否 1是
    private int podcastOrder;//星店订单数
    private int showPodcastPai;//是否显示竞拍管理(主播) 0否 1是
    private int podcastPai;//竞拍管理 数量(主播)
    private int showPodcastGoods;//是否显示 商品管理（主播） 0否 1是
    private int podcastGoods;//星店中的商品数量
    private int isUnpush;//0的时候代表开启推送，1的时候代表免推送
    private boolean selected;

    //家族定制添加参数
    private int familyId;//家族ID
    private int familyChieftain;//是否家族长 0：否、1：是
    private long activityTicket; //战斗力值
    private long upScore;//下一级升级经验
    private long currentScore;//目前经验值
    private long downSocre;//上一级经验
    /**
     * userId : 28
     * malaId : 23
     * signature : null
     * authentication : 0
     * money : null
     * loginIp : 192.168.1.106
     * focusCount : 0
     * paypassword : null
     * score : 0
     * point : 0
     * thumbHeadImage : http://q.qlogo.cn/qqapp/1105588451/DA066AE2CF6D77116043E1C4A8CF7EE1/40
     * fansCount : 0
     * ticket : 0
     * activity_ticket : null
     * refundTicket : 0
     * diamonds : 0
     * useDiamonds : 0
     * isOnline : 0
     * loginTime : 2017-02-23 16:35:34
     * logoutTime : null
     * subscribe : 0
     * isRobot : 0
     * apnsCode : null
     * videoCount : 0
     * isBan : 0
     * banTime : null
     */

    private String malaId;
    private int authentication;
    private Object money;
    private String loginIp;
    private Object paypassword;
    private int score;
    private int point;
    private String thumbHeadImage;
    private Object activity_ticket;
    private String refundTicket;
    private int isOnline;
    private String loginTime;
    private Object logoutTime;
    private String subscribe;
    private int isRobot;
    private Object apnsCode;
    private int isBan;
    private Object banTime;


    private int isFocus;
    private int isManage;
    private int canTipoff;
    private int relationship;// 1关注;2拉黑;3取消关注;4删除黑名单

    public static boolean dealLoginSuccess(UserModel user, boolean post)
    {
        boolean result = false;
        if (user != null)
        {
            result = UserModelDao.insertOrUpdate(user);
            if (post)
            {
                EUserLoginSuccess event = new EUserLoginSuccess();
                SDEventManager.post(event);
            }
        }
        return result;
    }

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

    public long getActivityTicket() {
        return activityTicket;
    }

    public void setActivityTicket(long activityTicket) {
        this.activityTicket = activityTicket;
    }

    public long getUseableTicket()
    {
        return useableTicket;
    }

    public void setUseableTicket(long useableTicket)
    {
        this.useableTicket = useableTicket;
    }

    public int getIsRemind()
    {
        return isRemind;
    }

    public void setIsRemind(int isRemind)
    {
        this.isRemind = isRemind;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getEmotionalState()
    {
        return emotionalState;
    }

    public void setEmotionalState(String emotionalState)
    {
        this.emotionalState = emotionalState;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public int getIsAuthentication()
    {
        return isAuthentication;
    }

    public void setIsAuthentication(int isAuthentication)
    {
        this.isAuthentication = isAuthentication;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public int getIsEditSex()
    {
        return isEditSex;
    }

    public void setIsEditSex(int isEditSex)
    {
        this.isEditSex = isEditSex;
    }

    public int getIsAgree()
    {
        return isAgree;
    }

    public void setIsAgree(int isAgree)
    {
        this.isAgree = isAgree;
    }

    public int getSexResId()
    {
        return LiveUtils.getSexImageResId(sex);
    }

    public String getNickNameFormat()
    {
        if (nickNameFormat == null)
        {
            nickNameFormat = "" + nickName + ":";
        }
        return nickNameFormat;
    }

    public void setNickNameFormat(String nickNameFormat)
    {
        this.nickNameFormat = nickNameFormat;
    }

    // add

    public int getLevelImageResId()
    {
        return LiveUtils.getLevelImageResId(userLevel);
    }

    /**
     * 扣除钻石
     *
     * @param price
     */
    public void pay(int price)
    {
        if (price > 0)
        {
            diamonds = diamonds - price;
            if (diamonds < 0)
            {
                diamonds = 0;
            }
        }
    }

    /**
     * 用户所剩的余额够不够支付价格
     *
     * @param price 价格
     * @return true-够支付
     */
    public boolean canPay(int price)
    {
        boolean has = false;
        if (diamonds > 0 && diamonds >= price)
        {
            has = true;
        }
        return has;
    }

    /**
     * 是否高级用户
     *
     * @return
     */
    public boolean isProUser()
    {
        boolean result = false;
        InitActModel model = InitActModelDao.query();
        if (model != null)
        {
            if (userLevel >= model.getJr_user_level() && userLevel < model.getSuper_user_level())
            {
                result = true;
            }
        }
        return result;
    }

    /**
     * 是否为超级用户
     * @return
     */
    public boolean isSuperUser(){
        boolean result = false;
        InitActModel model = InitActModelDao.query();
        if (model != null)
        {
            if (userLevel >= model.getSuper_user_level())
            {
                result = true;
            }
        }
        return result;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public int getFollowId()
    {
        return followId;
    }

    public void setFollowId(int followId)
    {
        this.followId = followId;
    }

    public long getUseDiamonds()
    {
        return useDiamonds;
    }

    public void setUseDiamonds(long useDiamonds)
    {
        this.useDiamonds = useDiamonds;
    }

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public long getFocusCount()
    {
        return focusCount;
    }

    public void setFocusCount(long focusCount)
    {
        this.focusCount = focusCount;
    }

    public long getFansCount()
    {
        return fansCount;
    }

    public void setFansCount(long fansCount)
    {
        this.fansCount = fansCount;
    }

    public long getTicket()
    {
        return ticket;
    }

    public void setTicket(long ticket)
    {
        this.ticket = ticket;
    }

    public long getDiamonds()
    {
        return diamonds;
    }

    public void setDiamonds(long diamonds)
    {
        this.diamonds = diamonds;
    }

    public int getUserLevel()
    {
        return userLevel;
    }

    public void setUserLevel(int userLevel)
    {
        this.userLevel = userLevel;
    }

    public String getHeadImage()
    {
        return headImage;
    }

    public void setHeadImage(String headImage)
    {
        this.headImage = headImage;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        if (userId != null)
        {
            this.userId = userId;
        }
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }

        if (!(o instanceof UserModel))
        {
            return false;
        }

        if (TextUtils.isEmpty(userId))
        {
            return false;
        }

        UserModel model = (UserModel) o;
        if (!userId.equals(model.getUserId()))
        {
            return false;
        }

        return true;
    }

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public String getHomeUrl()
    {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl)
    {
        this.homeUrl = homeUrl;
    }

    public String getV_type()
    {
        return v_type;
    }

    public void setV_type(String v_type)
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

    public String getV_explain()
    {
        return v_explain;
    }

    public void setV_explain(String v_explain)
    {
        this.v_explain = v_explain;
    }

    public Map<String, String> getItem()
    {
        return item;
    }

    public void setItem(Map<String, String> item)
    {
        this.item = item;
    }

    @Override
    public boolean isSelected()
    {
        return selected;
    }

    @Override
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public long getVideoCount()
    {
        return videoCount;
    }

    public void setVideoCount(long videoCount)
    {
        this.videoCount = videoCount;
    }

    public String getAuthenticationName()
    {
        return authenticationName;
    }

    public void setAuthenticationName(String authenticationName)
    {
        this.authenticationName = authenticationName;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getFromPlatform()
    {
        return fromPlatform;
    }

    public void setFromPlatform(String fromPlatform)
    {
        this.fromPlatform = fromPlatform;
    }

    public String getWiki()
    {
        return wiki;
    }

    public void setWiki(String wiki)
    {
        this.wiki = wiki;
    }

    public String getIdentifyPositiveImage()
    {
        return identifyPositiveImage;
    }

    public void setIdentifyPositiveImage(String identifyPositiveImage)
    {
        this.identifyPositiveImage = identifyPositiveImage;
    }

    public String getIdentifyNagativeImage()
    {
        return identifyNagativeImage;
    }

    public void setIdentifyNagativeImage(String identifyNagativeImage)
    {
        this.identifyNagativeImage = identifyNagativeImage;
    }

    public String getIdentifyHoldImage()
    {
        return identifyHoldImage;
    }

    public void setIdentifyHoldImage(String identifyHoldImage)
    {
        this.identifyHoldImage = identifyHoldImage;
    }

    public String getAuthenticationType()
    {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType)
    {
        this.authenticationType = authenticationType;
    }

    public String getIdentifyNumber()
    {
        return identifyNumber;
    }

    public void setIdentifyNumber(String identifyNumber)
    {
        this.identifyNumber = identifyNumber;
    }

    public int getShowUserOrder()
    {
        return showUserOrder;
    }

    public void setShowUserOrder(int showUserOrder)
    {
        this.showUserOrder = showUserOrder;
    }

    public int getUserOrder()
    {
        return userOrder;
    }

    public void setUserOrder(int userOrder)
    {
        this.userOrder = userOrder;
    }

    public int getShowUserPai()
    {
        return showUserPai;
    }

    public void setShowUserPai(int showUserPai)
    {
        this.showUserPai = showUserPai;
    }

    public int getUserPai()
    {
        return userPai;
    }

    public void setUserPai(int userPai)
    {
        this.userPai = userPai;
    }

    public int getShowPodcastOrder()
    {
        return showPodcastOrder;
    }

    public void setShowPodcastOrder(int showPodcastOrder)
    {
        this.showPodcastOrder = showPodcastOrder;
    }

    public int getPodcastOrder()
    {
        return podcastOrder;
    }

    public void setPodcastOrder(int podcastOrder)
    {
        this.podcastOrder = podcastOrder;
    }

    public int getShowPodcastPai()
    {
        return showPodcastPai;
    }

    public void setShowPodcastPai(int showPodcastPai)
    {
        this.showPodcastPai = showPodcastPai;
    }

    public int getPodcastPai()
    {
        return podcastPai;
    }

    public void setPodcastPai(int podcastPai)
    {
        this.podcastPai = podcastPai;
    }

    public int getShowPodcastGoods()
    {
        return showPodcastGoods;
    }

    public void setShowPodcastGoods(int showPodcastGoods)
    {
        this.showPodcastGoods = showPodcastGoods;
    }

    public int getPodcastGoods()
    {
        return podcastGoods;
    }

    public void setPodcastGoods(int podcastGoods)
    {
        this.podcastGoods = podcastGoods;
    }

    public int getFamilyId()
    {
        return familyId;
    }

    public void setFamilyId(int familyId)
    {
        this.familyId = familyId;
    }

    public int getFamilyChieftain()
    {
        return familyChieftain;
    }

    public void setFamilyChieftain(int familyChieftain)
    {
        this.familyChieftain = familyChieftain;
    }

    public int getIsUnpush() {
        return isUnpush;
    }

    public void setIsUnpush(int isUnpush) {
        this.isUnpush = isUnpush;
    }

    public long getUpScore() {
        return upScore;
    }

    public void setUpScore(long upScore) {
        this.upScore = upScore;
    }

    public long getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(long currentScore) {
        this.currentScore = currentScore;
    }

    public long getDownSocre() {
        return downSocre;
    }

    public void setDownSocre(long downSocre) {
        this.downSocre = downSocre;
    }


    public String getMalaId() {
        return malaId;
    }

    public void setMalaId(String malaId) {
        this.malaId = malaId;
    }


    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public Object getMoney() {
        return money;
    }

    public void setMoney(Object money) {
        this.money = money;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }


    public Object getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(Object paypassword) {
        this.paypassword = paypassword;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getThumbHeadImage() {
        return thumbHeadImage;
    }

    public void setThumbHeadImage(String thumbHeadImage) {
        this.thumbHeadImage = thumbHeadImage;
    }


    public Object getActivity_ticket() {
        return activity_ticket;
    }

    public void setActivity_ticket(Object activity_ticket) {
        this.activity_ticket = activity_ticket;
    }

    public String getRefundTicket() {
        return refundTicket;
    }

    public void setRefundTicket(String refundTicket) {
        this.refundTicket = refundTicket;
    }


    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public Object getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Object logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public int getIsRobot() {
        return isRobot;
    }

    public void setIsRobot(int isRobot) {
        this.isRobot = isRobot;
    }

    public Object getApnsCode() {
        return apnsCode;
    }

    public void setApnsCode(Object apnsCode) {
        this.apnsCode = apnsCode;
    }


    public int getIsBan() {
        return isBan;
    }

    public void setIsBan(int isBan) {
        this.isBan = isBan;
    }

    public Object getBanTime() {
        return banTime;
    }

    public void setBanTime(Object banTime) {
        this.banTime = banTime;
    }

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public int getIsManage() {
        return isManage;
    }

    public void setIsManage(int isManage) {
        this.isManage = isManage;
    }

    public int getCanTipoff() {
        return canTipoff;
    }

    public void setCanTipoff(int canTipoff) {
        this.canTipoff = canTipoff;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getLikesClicks() {
        return likesClicks;
    }

    public void setLikesClicks(int likesClicks) {
        this.likesClicks = likesClicks;
    }
}
