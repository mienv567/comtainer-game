package com.fanwe.live.model;

import java.io.Serializable;

public class RoomUserModel implements Serializable
{

	private UserModel user;
	// 举报按钮，1-显示，0-不显示
	private int show_tipoff;
	// 管理按钮，1,2-显示，0-不显示(1 管理员：举报，禁言，取消;2主播：设置为管理员/取消管理员，管理员列表，禁言，取消)
	private int show_admin;
	// 0-未关注;1-已关注
	private int has_focus;
	// 0-非管理员;1-是管理员
	private int has_admin;

	public UserModel getUser()
	{
		return user;
	}

	public void setUser(UserModel user)
	{
		this.user = user;
	}

	public int getShow_tipoff()
	{
		return show_tipoff;
	}

	public void setShow_tipoff(int show_tipoff)
	{
		this.show_tipoff = show_tipoff;
	}

	public int getShow_admin()
	{
		return show_admin;
	}

	public void setShow_admin(int show_admin)
	{
		this.show_admin = show_admin;
	}

	public int getHas_focus()
	{
		return has_focus;
	}

	public void setHas_focus(int has_focus)
	{
		this.has_focus = has_focus;
	}

	public int getHas_admin()
	{
		return has_admin;
	}

	public void setHas_admin(int has_admin)
	{
		this.has_admin = has_admin;
	}


	/**
	 * sex : 1
	 * videoCount : 0
	 * malaId : 103866
	 * nickName : 孤峰测试1
	 * ticket : 0
	 * authentication : 0
	 * headImage : http://192.168.1.63:8081/live-web/data/images/malaLiveheadPic/headPic7.png
	 * userId : 7
	 * userLevel : 6
	 * isAgree : 1
	 * useDiamonds : 0
	 * isRemind : 1
	 * fansCount : 0
	 */

	private int sex;
	private int videoCount;
	private int malaId;
	private String nickName;
	private int ticket;
	private int authentication;
	private String headImage;
	private String userId;
	private int userLevel;
	private int isAgree;
	private int useDiamonds;
	private int isRemind;
	private int fansCount;

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getVideoCount() {
		return videoCount;
	}

	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}

	public int getMalaId() {
		return malaId;
	}

	public void setMalaId(int malaId) {
		this.malaId = malaId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getTicket() {
		return ticket;
	}

	public void setTicket(int ticket) {
		this.ticket = ticket;
	}

	public int getAuthentication() {
		return authentication;
	}

	public void setAuthentication(int authentication) {
		this.authentication = authentication;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(int isAgree) {
		this.isAgree = isAgree;
	}

	public int getUseDiamonds() {
		return useDiamonds;
	}

	public void setUseDiamonds(int useDiamonds) {
		this.useDiamonds = useDiamonds;
	}

	public int getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(int isRemind) {
		this.isRemind = isRemind;
	}

	public int getFansCount() {
		return fansCount;
	}

	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}
}
