package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.huassit.imenu.android.model.Fans;

/**
 * 粉丝列表解析
 * 
 */
public class FansListParser extends BaseParser<List<Fans>> {

	@Override
	public List<Fans> parseData(JSONObject jsonObject) throws JSONException {
		List<Fans> menuObjectList = new ArrayList<Fans>();
		JSONObject data = jsonObject.optJSONObject("data");
		if (data != null) {
			JSONArray memberList = data.optJSONArray("member_list");
			if (memberList != null && memberList.length() > 0) {
				for (int i = 0; i < memberList.length(); i++) {
					JSONObject mObject = memberList.optJSONObject(i);
					Fans fans = new Fans();
					fans.follow_status = mObject.optString("follow_status");
					fans.followed_count = mObject.optString("followed_count");
					fans.iconDate = mObject.optString("iconDate");
					fans.iconLocation = mObject.optString("iconLocation");
					fans.iconName = mObject.optString("iconName");
					fans.member_id = mObject.optString("member_id");
					fans.member_name = mObject.optString("member_name");
					fans.vip_level = mObject.optString("vip_level");
					fans.member_name = mObject.optString("member_name");
					menuObjectList.add(fans);
				}
			}
		}
		return menuObjectList;
	}
}
