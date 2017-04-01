package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

public class App_monitorActModel extends BaseActModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * isNew : 0
	 * missionProcess : {"startTime":"2017-03-22 12:18:38","processId":20,"status":1,"userId":68,"currentMissionId":13,"missionGroupId":7,"currentMissionTitle":"再来al"}
	 * isOpen : 1
	 */

	public int isNew;
	public MissionProcess missionProcess;
	public int isOpen;

	public static class MissionProcess {
		/**
		 * startTime : 2017-03-22 12:18:38
		 * processId : 20
		 * status : 1
		 * userId : 68
		 * currentMissionId : 13
		 * missionGroupId : 7
		 * currentMissionTitle : 再来al
		 */

		public String startTime;
		public int processId;
		public int status;
		public String userId;
		public String currentMissionId;
		public String missionGroupId;
		public String currentMissionTitle;
	}
}
