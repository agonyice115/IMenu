package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;
import com.huassit.imenu.android.model.OrderInfo;

/**
 * 订单详情解析
 * 
 * @author shang_guan
 * 
 */
public class SubmitOrderParser extends BaseParser<OrderInfo> {

	@Override
	public OrderInfo parseData(JSONObject jsonObject) throws JSONException {
		JSONObject data = jsonObject.optJSONObject("data");
		OrderInfo mInfo = null;
		if (data != null) {
			JSONObject order_info = data.optJSONObject("order_info");
			mInfo = new OrderInfo().parse(order_info);
		}
		return mInfo;
	}
}
