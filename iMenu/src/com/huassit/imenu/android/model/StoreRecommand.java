package com.huassit.imenu.android.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class StoreRecommand extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 推荐菜品列表ID */
	public String recommend_store_id;
	/** 推荐名称 */
	public String recommend_name;
	/** 推荐人数 */
	public String recommend_people;
	/** 推荐内容 */
	public String recommend_content;
	/** 推荐排序 */
	public String sort_order;
	/** 菜品列表 */
	public ArrayList<Menu> menu_list;

	@Override
	public StoreRecommand parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			recommend_store_id = jsonObject.optString("");
			recommend_name = jsonObject.optString("recommend_name");
			recommend_people = jsonObject.optString("recommend_people");
			recommend_content = jsonObject.optString("recommend_content");
			sort_order = jsonObject.optString("sort_order");
			JSONArray mArray = new JSONArray();
			if (jsonObject.optJSONArray("menu_list") != null
					&& jsonObject.optJSONArray("menu_list").length() > 0) {
				mArray = jsonObject.optJSONArray("menu_list");
				JSONObject object = new JSONObject();
				menu_list=new ArrayList<Menu>();
				for (int i = 0; i < mArray.length(); i++) {
					object = mArray.optJSONObject(i);
					Menu menu = new Menu().parse(object);
					menu_list.add(menu);
				}
			}
		}
		return this;
	}
}
