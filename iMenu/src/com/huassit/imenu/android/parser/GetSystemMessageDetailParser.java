package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;
import com.huassit.imenu.android.model.Message;

/**
 * 获取消息详情解析类
 */
public class GetSystemMessageDetailParser extends BaseParser<Message> {

	@Override
	public Message parseData(JSONObject jsonObject) throws JSONException {
		Message messages = new Message();
		JSONObject data = jsonObject.optJSONObject("data");
		messages = new Message().parse(data
				.optJSONObject("system_message_info"));
		return messages;
	}
}
