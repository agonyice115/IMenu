package com.huassit.imenu.android.model;

import org.json.JSONObject;

/***
 * 退款原因
 * 
 * @author shang_shang
 * 
 */
public class ReturnReason extends BaseModel<ReturnReason> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 退款原因ID */
	public String return_reason_id;
	/** 退款原因 */
	public String return_reason_name;
	/** 状态为是否选中 */
	public boolean isTrue=false;

	@Override
	public ReturnReason parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			return_reason_id = jsonObject.optString("return_reason_id");
			return_reason_name = jsonObject.optString("return_reason_name");
		}
		return this;
	}
}
