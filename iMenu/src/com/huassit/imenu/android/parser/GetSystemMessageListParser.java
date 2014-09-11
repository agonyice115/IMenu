package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Message;

/**
 * 获取系统消息列表解析类
 */
public class GetSystemMessageListParser extends BaseParser<List<Message>> {

	@Override
	public List<Message> parseData(JSONObject jsonObject) throws JSONException {
		List<Message> messages = new ArrayList<Message>();
		JSONObject data = jsonObject.optJSONObject("data");
		if (data != null) {
			JSONArray system_message_list = data
					.getJSONArray("system_message_list");
			for (int i = 0; i < system_message_list.length(); i++) {
				JSONObject obj = system_message_list.getJSONObject(i);
				Message message = new Message().parse(obj);
				messages.add(message);
			}
		}
		return messages;
	}
}
