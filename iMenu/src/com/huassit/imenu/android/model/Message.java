package com.huassit.imenu.android.model;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * 消息实体类
 * 
 * @author shang_guan
 * 
 */
public class Message extends BaseModel implements Serializable {
	/** 系统消息ID */
	public String system_message_id;
	/** 消息标题 */
	public String title;
	/** 摘要 */
	public String summary;
	/** 消息时间 */
	public String date_modified;
	/** 1-已读/0-未读 */
	public String is_open;
	/** 消息内容 */
	public String content;

	@Override
	public Message parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			system_message_id = jsonObject.optString("system_message_id");
			title = jsonObject.optString("title");
			summary = jsonObject.optString("summary");
			date_modified = jsonObject.optString("date_modified");
			is_open = jsonObject.optString("is_open");
			content = jsonObject.optString("content");
		}

		return this;
	}
}
