package com.fanwe.live.event;

import java.util.List;

import com.tencent.TIMGroupTipsType;

/**
 * 群成员变更通知，包括加入群，退出群，踢出群，设置管理员等， 根据type参数确定变更通知类型
 * 
 * @author Administrator
 * @date 2016-5-12 下午1:16:20
 */
public class EImOnMemberUpdate
{
	public String groupId;
	public TIMGroupTipsType type;
	public List<String> listMember;

}
