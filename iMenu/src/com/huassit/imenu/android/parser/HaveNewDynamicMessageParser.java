package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;
import com.huassit.imenu.android.model.DynamicMessageList;
import com.huassit.imenu.android.model.HaveNews;
import com.huassit.imenu.android.model.Member;

/**
 * 轮询新动态消息提示解析类
 */
public class HaveNewDynamicMessageParser extends BaseParser<DynamicMessageList> {

	@Override
	public DynamicMessageList parseData(JSONObject jsonObject)
			throws JSONException {
		DynamicMessageList mDynamicMessageList = new DynamicMessageList();
		JSONObject data = jsonObject.optJSONObject("data");
		if (data != null) {
			JSONObject new_info = data.getJSONObject("new_info");
			JSONObject member_info = data.getJSONObject("member_info");
			mDynamicMessageList.new_info = new HaveNews().parse(new_info);
			mDynamicMessageList.member_info = new Member().parse(member_info);
		}
		return mDynamicMessageList;
	}
}
