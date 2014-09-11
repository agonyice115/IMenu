package com.huassit.imenu.android.model;

import java.io.Serializable;

import org.json.JSONObject;

public class MemberMenu extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String menu_id;
	public String menu_name;
	public String member_menu_image_id;
	public String image_name;
	public String image_location;
	public String goods_count;
	public String comment_count;
	public String member_menu_image_date;
	public String dynamic_id;
	public Member member_info;

	@Override
	public MemberMenu parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			menu_id = jsonObject.optString("menu_id");
			dynamic_id = jsonObject.optString("dynamic_id");
			image_name = jsonObject.optString("image_name");
			image_location = jsonObject.optString("image_location");
		}
		return this;
	}

}
