package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.DynamicMessageList;

/**
 * 获取动态消息列表解析类
 */
public class GetDynamicMessageListParser extends
		BaseParser<List<DynamicMessageList>> {

	@Override
	public List<DynamicMessageList> parseData(JSONObject jsonObject)
			throws JSONException {
		List<DynamicMessageList> messages = new ArrayList<DynamicMessageList>();
		DynamicMessageList message = new DynamicMessageList();
		JSONObject data = jsonObject.optJSONObject("data");
		if (data != null) {
			JSONArray dynamic_message_list = data
					.getJSONArray("dynamic_message_list");
			JSONObject dynamic_message_info = data
					.optJSONObject("dynamic_message_info");
			for (int i = 0; i < dynamic_message_list.length(); i++) {
				JSONObject obj = dynamic_message_list.getJSONObject(i);
				message = new DynamicMessageList().parse(obj);
				message.unread_count = dynamic_message_info
						.optString("unread_count");
				messages.add(message);
			}
		}
		return messages;
	}
}
