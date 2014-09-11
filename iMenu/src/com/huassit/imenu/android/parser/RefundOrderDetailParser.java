package com.huassit.imenu.android.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Consume;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.model.ReturnInfo;
import com.huassit.imenu.android.model.ScoreInfo;
import com.huassit.imenu.android.model.Store;

/**
 * 退款订单详情解析
 * 
 * @author shang_guan
 * 
 */
public class RefundOrderDetailParser extends BaseParser<OrderInfo> {

	@Override
	public OrderInfo parseData(JSONObject jsonObject) throws JSONException {
		JSONObject data = jsonObject.optJSONObject("data");
		OrderInfo mInfo = null;
		if (data != null) {
			JSONObject order_info = data.optJSONObject("order_info");
			mInfo = new OrderInfo();
			mInfo.order_date = order_info.optString("create_date");
			mInfo.order_id = order_info.optString("order_id");
			mInfo.order_no = order_info.optString("order_no");
			mInfo.people = order_info.optString("people");
			mInfo.total = order_info.optString("total");
			mInfo.menu_type_count = order_info.optString("menu_type_count");
			mInfo.originalTotal = order_info.optString("original_total");
			mInfo.order_type = order_info.optString("order_type");
			mInfo.mStoreInfo=new Store().parse(order_info.optJSONObject("store_info"));
			JSONObject return_info = data.optJSONObject("return_info");
			mInfo.returnInfo = new ReturnInfo().parse(return_info);
		}
		return mInfo;
	}
}
