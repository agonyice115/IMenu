package com.huassit.imenu.android.model;

import org.json.JSONObject;

/**
 * 退款详情
 */
public class ReturnInfo extends BaseModel<ReturnInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 退款ID */
	public String return_id;
	/** 退款原因ID */
	public String return_reason_id;
	/** 退款原因详情 */
	public String return_reason_description;
	/** 退款方式ID */
	public String return_action_id;
	/** 退款创建时间 */
	public String date_added;

	@Override
	public ReturnInfo parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			return_id = jsonObject.optString("return_id");
			return_reason_id = jsonObject.optString("return_reason_id");
			return_reason_description = jsonObject
					.optString("return_reason_description");
			return_action_id = jsonObject.optString("return_action_id");
			date_added = jsonObject.optString("date_added");
		}
		return this;
	}
}
