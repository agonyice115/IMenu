package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.OrderInfo;

/**
 * 申请退款解析
 */
public class ApplyReturnParser extends BaseParser<OrderInfo> {

	@Override
	public OrderInfo parseData(JSONObject jsonObject) throws JSONException {
		JSONObject data = jsonObject.optJSONObject("data");
		OrderInfo info = null;
		if (data != null) {
			info = new OrderInfo();
			info = new OrderInfo().parse(data.getJSONObject("order_info"));
		}
		return info;
	}
}
