package com.huassit.imenu.android.model;

import org.json.JSONObject;

public class Feedback extends BaseModel<Feedback> {

	public String feedback_id;
	public String feedback_name;
	public String parent_id;
	public String sort_order;
	public String feedback_type;
	public String level;
	public boolean isSelect = false;

	@Override
	public Feedback parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			feedback_id = jsonObject.optString("feedback_id");
			feedback_name = jsonObject.optString("feedback_name");
			parent_id = jsonObject.optString("parent_id");
			sort_order = jsonObject.optString("sort_order");
			feedback_type = jsonObject.optString("feedback_type");
			level = jsonObject.optString("level");
		}
		return this;
	}
}
