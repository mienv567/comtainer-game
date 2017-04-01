package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

public class App_end_videoActModel extends BaseActModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int watch_number; // 观看人数
	private int vote_number; // 获得钱票
	private int has_delvideo; // 1-显示删除视频按钮，0-不显示
	private long time_len;//播放时长 秒
	private int add_funs;//获得粉丝数
	private int add_shared;//分享次数
	private float add_rmb;//收益
	public int getWatch_number()
	{
		return watch_number;
	}

	public void setWatch_number(int watch_number)
	{
		this.watch_number = watch_number;
	}

	public int getVote_number()
	{
		return vote_number;
	}

	public void setVote_number(int vote_number)
	{
		this.vote_number = vote_number;
	}

	public int getHas_delvideo()
	{
		return has_delvideo;
	}

	public void setHas_delvideo(int has_delvideo)
	{
		this.has_delvideo = has_delvideo;
	}

	public int getAdd_funs() {
		return add_funs;
	}

	public void setAdd_funs(int add_funs) {
		this.add_funs = add_funs;
	}

	public int getAdd_shared() {
		return add_shared;
	}

	public void setAdd_shared(int add_shared) {
		this.add_shared = add_shared;
	}

	public long getTime_len() {
		return time_len;
	}

	public void setTime_len(long time_len) {
		this.time_len = time_len;
	}

	public float getAdd_rmb() {
		return add_rmb;
	}

	public void setAdd_rmb(float add_rmb) {
		this.add_rmb = add_rmb;
	}
}
