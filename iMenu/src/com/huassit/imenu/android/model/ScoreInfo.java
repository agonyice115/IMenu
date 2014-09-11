package com.huassit.imenu.android.model;

import org.json.JSONObject;

public class ScoreInfo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7517766360912027942L;
	/** 总积分 */
	public String total_score;
	/** 获得积分 */
	public String add_score;
	/** 扣除积分 */
	public String minus_score;

	@Override
	public ScoreInfo parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			total_score = jsonObject.optString("total_score");
			add_score = jsonObject.optString("add_score");
			minus_score = jsonObject.optString("minus_score");
		}
		return this;
	}
}
