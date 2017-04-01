package com.fanwe.live.event;

import java.util.List;

import com.tencent.TIMGroupTipsElemMemberInfo;

/**
 * 群成员资料变更， 如被设置禁言
 * 
 * @author Administrator
 * @date 2016-5-12 下午1:13:42
 */
public class EImOnMemberInfoUpdate
{
	public String groupId;
	public List<TIMGroupTipsElemMemberInfo> listMemberInfos;

}
