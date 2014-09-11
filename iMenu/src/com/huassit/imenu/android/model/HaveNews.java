package com.huassit.imenu.android.model;

import org.json.JSONObject;

/**
 * 是否有新消息实体类
 * 
 * @author shangshang
 * 
 */
@SuppressWarnings("rawtypes")
public class HaveNews extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 是否有新消息，1-有，2-无 */
	public String have_new;
	/** 消息数量 */
	public String new_count;

	@Override
	public HaveNews parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			have_new = jsonObject.optString("have_new");
			new_count = jsonObject.optString("new_count");
		}
		return this;
	}

}
