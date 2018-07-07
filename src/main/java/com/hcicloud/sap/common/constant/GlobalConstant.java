package com.hcicloud.sap.common.constant;

import java.util.HashMap;
import java.util.Map;

public class GlobalConstant {

	public static final String SESSION_INFO = "sessionInfo";

	// 启用
	public static final Integer ENABLE = 1;
	// 禁用
	public static final Integer DISABLE = 0;

	// 默认
	public static final Integer DEFAULT = 1;
	// 非默认
	public static final Integer NOT_DEFAULT = 0;

	public static final Map<String, String> statelist = new HashMap<String, String>() {
		{
			put("1", "启用");
			put("0", "停用");
		}
	};
	public static final Map<String, String> MediaTypelist = new HashMap<String, String>() {
		{
			put("1", "音频");
			put("2", "文本");
			put("3", "视频");
		}
	};
	//日志类型列表
	public static final Map<String, String> logTypeList = new HashMap<String, String>(){
		{
			put("dologin", "登录");
			put("logout", "登出");
			put("add", "增加");
			put("edit", "编辑");
			put("delete", "删除");
		}
	};
	
	public static final Map<String, String> actionTypeList = new HashMap<String, String>(){
		{
			put("/userGroup", "用户组");
			put("/user", "用户");
			put("/rules", "规则集");
			put("/rulesType", "规则集类别");
			put("/ruleType", "字段设置");
			put("/", "系统");
			put("/systemParam", "系统参数");
			put("/autoRule","自动质检规则");
			put("/autoRules","自动质检规则集");
			put("/standardSpeech","标准话术管理");
		}
	};
	
	public static final Long AUDIO_FORMAT_ID = Long.valueOf(1);
	public static final Long AUDIO_EXT_ID = Long.valueOf(2);
	public static final Long VOICE_FILE_DIR_ID = Long.valueOf(3);
	public static final Long INDEX_SERVER_ID = Long.valueOf(4);
	public static final Long ASR_TRANS_SERVER_ID = Long.valueOf(5);
	public static final Long HOME_STAT_PEROID_ID = Long.valueOf(6);
	public static final Long VOICE_EXTRACT_WORK_ID = Long.valueOf(7);
	public static final Long VOICE_CHANNEL_ID = Long.valueOf(8);
	public static final Long VOICE_EXTRACTED_ID = Long.valueOf(9);
	public static final Long VOICE_SOURCE_ID = Long.valueOf(10);
	public static final Long FILE_DELETE = Long.valueOf(11);
	
	public static final String INITPWD = "e10adc3949ba59abbe56e057f20f883e";
	
	public static final Integer USER_ONLY = 0;
	public static final Integer AGENT_ONLY = 1;
	public static final Integer BOTH_USER_AND_AGENT = 2;
	/**
	 * 在线质检的redis  key
	 */
	public static final String RECORDDATA = "record_data";
	public static final String HISTORY = "history";
	public static final String ALREADY_ASSIGNED = "已分配";
	public static final String NOT_ASSIGNED = "未分配";
	public static final String NO_DISTRIBUTION = "不予分配";
	public static final String HAVE_QUALITY_INSPECTION = "已质检";
	public static final String NO_QUALITY_INSPECTION = "未质检";
	
	/**
	 * 上传录音的录音状态
	 */
	public static final String IS_TRANSFERRING = "正在转写中";
	public static final String ALREADY_TRANSFERRED = "转写已完成";
	
	
}
