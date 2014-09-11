package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.FilterDate;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.util.TimeUtil;

public class StoreDynamicListParser extends BaseParser<Map<String, Object>> {

	/**
	 * 获取服务器传来的子项集合
	 */
	private ArrayList<Dynamic> dynamicList;
	/**
	 * 存储筛选后的子项集合
	 */
	private ArrayList<Dynamic> newDynamicsList;
	/**
	 * 种类为年份
	 */
	private final static int YEAR = 1;
	/**
	 * 种类为月份
	 */
	private final static int MONTH = 2;
	/**
	 * 种类为周
	 */
	private final static int WEEK = 3;
	/**
	 * 请求的时间分类
	 */
	private int TimeType;

	public StoreDynamicListParser(int TimeType) {
		this.TimeType = TimeType;
	}

	@Override
	public Map<String, Object> parseData(JSONObject jsonObject)
			throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray jsonDataArray = jsonObject.optJSONArray("data");
		for (int i = 0; i < jsonDataArray.length(); i++) {
			JSONObject object = (JSONObject) jsonDataArray.get(i);
			JSONArray jsonfilterDateList = object
					.optJSONArray("filter_date_list");
			/** 动态的个人信息数据 */
			if (object.optJSONObject("view_store") != null) {
				JSONObject jsonStore = object.optJSONObject("view_store");
				Store store = new Store().parse(jsonStore);
				map.put("view_store", store);
			}
			/*** 子项的全部动态 */
			JSONArray jsonDynamicList = object.optJSONArray("dynamic_list");
			dynamicList = new ArrayList<Dynamic>();
			for (int k = 0; k < jsonDynamicList.length(); k++) {
				JSONObject dynamicObject = jsonDynamicList.optJSONObject(k);
				Dynamic dynamic = new Dynamic();
				dynamic.setDynamicId(dynamicObject.optString("dynamic_id"));
				dynamic.setDynamicDate(dynamicObject.optString("dynamic_date"));
				dynamic.setTitle(dynamicObject.optString("title"));
				dynamic.setPeople(dynamicObject.optString("people"));
				dynamic.setDistance(dynamicObject.optString("distance"));
				dynamic.setGoodsCount(dynamicObject.optString("goods_count"));
				dynamic.setCommentCount(dynamicObject
						.optString("comment_count"));
				dynamic.setDynamicType(dynamicObject.optString("dynamic_type"));
				/***********************/
				JSONObject memberInfoObject = dynamicObject
						.optJSONObject("member_info");
				Member member = new Member();
				member.memberId = memberInfoObject.optString("member_id");
				member.memberName = memberInfoObject.optString("member_name");
				member.iconName = memberInfoObject.optString("iconName");
				member.iconLocation = memberInfoObject
						.optString("iconLocation");
				member.iconDate = memberInfoObject.optString("iconDate");
				member.vipLevel = memberInfoObject.optString("vip_level");
				dynamic.setMemberInfo(member);
				/*************************/
				JSONObject storeInfoObject = dynamicObject
						.optJSONObject("store_info");
				Store store = new Store();
				store.id = storeInfoObject.optString("store_id");
				store.name = storeInfoObject.optString("store_name");
				store.vipLevel = storeInfoObject.optString("vip_level");
				store.address = storeInfoObject.optString("address");
				dynamic.setStoreInfo(store);
				/*************************/
				JSONArray jsonMenuList = dynamicObject
						.optJSONArray("menu_list");
				ArrayList<MemberMenu> menuList = new ArrayList<MemberMenu>();
				for (int p = 0; p < jsonMenuList.length(); p++) {
					MemberMenu memberMenu = new MemberMenu();
					JSONObject jsonMenuObject = jsonMenuList.optJSONObject(p);
					memberMenu.menu_id = jsonMenuObject.optString("menu_id");
					memberMenu.menu_name = jsonMenuObject
							.optString("menu_name");
					memberMenu.member_menu_image_id = jsonMenuObject
							.optString("member_menu_image_id");
					memberMenu.image_name = jsonMenuObject
							.optString("image_name");
					memberMenu.image_location = jsonMenuObject
							.optString("image_location");
					memberMenu.goods_count = jsonMenuObject
							.optString("goods_count");
					memberMenu.comment_count = jsonMenuObject
							.optString("comment_count");
					menuList.add(memberMenu);
				}
				dynamic.setMenuList(menuList);
				dynamicList.add(dynamic);
			}
			/** 父项的数据及其对应的子项集合 */

			ArrayList<FilterDate> filterDateList = new ArrayList<FilterDate>();
			for (int j = 0; j < jsonfilterDateList.length(); j++) {
				newDynamicsList = new ArrayList<Dynamic>();
				JSONObject filterDateObject = jsonfilterDateList
						.optJSONObject(j);
				FilterDate filterDate = new FilterDate();
				/** 获取每个父项的时间和条目数 */
				String key = filterDateObject.optString("key");
				String value = filterDateObject.optString("value");
				filterDate.key = key;
				filterDate.value = value;
				filterDate.mDynamicsList = dynamicList;
				/** 按照月份分类：遍历子项的list集合，获取与父项key值所对应的数据 */
				if (TimeType == MONTH) {
					for (int k = 0; k < dynamicList.size(); k++) {
						String dynamicDate = TimeUtil.itemMonthDate(dynamicList
								.get(k).getDynamicDate(), MONTH);
						if (dynamicDate.equals(key)) {
							newDynamicsList.add(dynamicList.get(k));
						}
					}
					filterDate.mDynamicsList = newDynamicsList;
				}
				/** 按照年份分类：遍历子项的list集合，获取与父项key值所对应的数据 */
				if (TimeType == YEAR) {
					for (int k = 0; k < dynamicList.size(); k++) {
						String dynamicDate = TimeUtil.itemMonthDate(dynamicList
								.get(k).getDynamicDate(), YEAR);
						if (dynamicDate.equals(key)) {
							newDynamicsList.add(dynamicList.get(k));
						}
					}
					filterDate.mDynamicsList = newDynamicsList;
				}
				/** 按照周分类：遍历子项的list集合，获取与父项key值所对应的数据 */
				if (TimeType == WEEK) {
					for (int k = 0; k < dynamicList.size(); k++) {
						String dynamicDate = TimeUtil.itemMonthDate(dynamicList
								.get(k).getDynamicDate(), WEEK);
						if (dynamicDate.equals(key.substring(key.length() - 2,
								key.length()))) {
							newDynamicsList.add(dynamicList.get(k));
						}
					}
					filterDate.mDynamicsList = newDynamicsList;
				}
				filterDateList.add(filterDate);
			}
			map.put("filter_date_list", filterDateList);
		}
		return map;

	}

}
