package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.model.Store;

public class DynamicDetailParser extends BaseParser<Map<String, Object>> {

	@Override
	public Map<String, Object> parseData(JSONObject jsonObject)
			throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray jsonDataArray = jsonObject.optJSONArray("data");
		for (int i = 0; i < jsonDataArray.length(); i++) {
			JSONObject object = (JSONObject) jsonDataArray.get(i);
			JSONObject jsonViewMember = object.optJSONObject("view_member");
			Member viewMember = new Member();
			viewMember.memberId = jsonViewMember.optString("member_id");
			viewMember.memberName = jsonViewMember.optString("member_name");
			viewMember.iconName = jsonViewMember.optString("iconName");
			viewMember.iconLocation = jsonViewMember.optString("iconLocation");
			viewMember.iconDate = jsonViewMember.optString("iconDate");
			viewMember.dynamicName = jsonViewMember.optString("dynamic_name");
			viewMember.dynamicLocation = jsonViewMember
					.optString("dynamic_location");
			viewMember.dynamicDate = jsonViewMember.optString("dynamic_date");
			viewMember.sex = jsonViewMember.optString("sex");
			viewMember.vipLevel = jsonViewMember.optString("vip_level");
			viewMember.signature = jsonViewMember.optString("signature");
			map.put("view_member", viewMember);

			JSONArray jsonMenuList = object.optJSONArray("menu_list");
			ArrayList<MemberMenu> memberMenuList = new ArrayList<MemberMenu>();
			for (int j = 0; j < jsonMenuList.length(); j++) {
				JSONObject jsonMenuObject = jsonMenuList.optJSONObject(j);
				MemberMenu memberMenu = new MemberMenu();
				memberMenu.menu_id = jsonMenuObject.optString("menu_id");
				memberMenu.menu_name = jsonMenuObject.optString("menu_name");
				memberMenu.member_menu_image_id = jsonMenuObject
						.optString("member_menu_image_id");
				memberMenu.image_name = jsonMenuObject.optString("image_name");
				memberMenu.image_location = jsonMenuObject
						.optString("image_location");
				memberMenu.goods_count = jsonMenuObject
						.optString("goods_count");
				memberMenu.comment_count = jsonMenuObject
						.optString("comment_count");
				memberMenuList.add(memberMenu);
			}
			map.put("menu_list", memberMenuList);
			JSONObject jsonStoreInfo = object.optJSONObject("store_info");
			Store store = new Store();
			store.id = jsonStoreInfo.optString("store_id");
			store.name = jsonStoreInfo.optString("store_name");
			store.vipLevel = jsonStoreInfo.optString("vip_level");
			store.address = jsonStoreInfo.optString("address");
			map.put("store_info", store);

			JSONArray jsonDynamicList = object.optJSONArray("dynamic_info");
			for (int k = 0; k < jsonDynamicList.length(); k++) {
				JSONObject jsonDynamicInfo = jsonDynamicList.optJSONObject(k);
				Dynamic dynamic = new Dynamic();
				dynamic.setDynamicId(jsonDynamicInfo.optString("dynamic_id"));
				dynamic.setDynamicDate(jsonDynamicInfo
						.optString("dynamic_date"));
				dynamic.setTitle(jsonDynamicInfo.optString("title"));
				dynamic.setDistance(jsonDynamicInfo.optString("distance"));
				dynamic.setPeople(jsonDynamicInfo.optString("people"));
				dynamic.setGoodsCount(jsonDynamicInfo.optString("goods_count"));
				dynamic.setCommentCount(jsonDynamicInfo
						.optString("comment_count"));
				dynamic.setDynamicType(jsonDynamicInfo
						.optString("dynamic_type"));
				map.put("dynamic_info", dynamic);
			}
		}
		return map;
	}

}
