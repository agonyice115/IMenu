package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Fans;
import com.huassit.imenu.android.model.Follow;
import com.huassit.imenu.android.model.FollowAndFans;

/**
 * 关注列表解析
 * 
 */
public class FollowListParser extends BaseParser<FollowAndFans> {

	@Override
	public FollowAndFans parseData(JSONObject jsonObject) throws JSONException {
		FollowAndFans mFollowAndFans = new FollowAndFans();
		JSONObject data = jsonObject.optJSONObject("data");
		if (data != null) {
			/** 用户列表 */
			JSONArray memberList = data.optJSONArray("member_list");
			if (memberList != null && memberList.length() > 0) {
				List<Fans> mFans = new ArrayList<Fans>();
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
					mFans.add(fans);
				}
				mFollowAndFans.mFansList = mFans;
			}
			/** 商家列表 */
			JSONArray storeList = data.optJSONArray("store_list");
			if (storeList != null && storeList.length() > 0) {
				List<Follow> mFollows = new ArrayList<Follow>();
				for (int i = 0; i < storeList.length(); i++) {
					JSONObject mObject = storeList.optJSONObject(i);
					Follow follow = new Follow();
					follow.follow_status = mObject.optString("follow_status");
					follow.address = mObject.optString("address");
					follow.store_id = mObject.optString("store_id");
					follow.store_latitude_num = mObject
							.optString("store_latitude_num");
					follow.store_logo_date = mObject
							.optString("store_logo_date");
					follow.store_logo_location = mObject
							.optString("store_logo_location");
					follow.store_logo_name = mObject
							.optString("store_logo_name");
					follow.store_longitude_num = mObject
							.optString("store_longitude_num");
					follow.store_name = mObject.optString("store_name");
					follow.tel_1 = mObject.optString("tel_1");
					follow.tel_2 = mObject.optString("tel_2");
					mFollows.add(follow);
				}
				mFollowAndFans.mFollowsList = mFollows;
			}
		}
		return mFollowAndFans;
	}
}
