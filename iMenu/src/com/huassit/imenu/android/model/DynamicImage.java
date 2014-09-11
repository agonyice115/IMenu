package com.huassit.imenu.android.model;

import org.json.JSONObject;

/**
 * 动态封面图片实体类
 * 
 * @author 上官明月
 * 
 */
public class DynamicImage {

	public String memberId;
	public String iconName;
	public String iconLocation;
	public String iconDate;

	public DynamicImage parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			memberId = jsonObject.optString("memberId");
			iconName = jsonObject.optString("dynamicName");
			iconLocation = jsonObject.optString("dynamicLocation");
			iconDate = jsonObject.optString("dynamicDate");
		}
		return this;
	}
}
