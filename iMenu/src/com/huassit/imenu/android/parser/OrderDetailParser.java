package com.huassit.imenu.android.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Consume;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.model.ScoreInfo;
import com.huassit.imenu.android.model.Store;

/**
 * 订单详情解析
 * 
 * @author shang_guan
 * 
 */
public class OrderDetailParser extends BaseParser<OrderInfo> {

	@Override
	public OrderInfo parseData(JSONObject jsonObject) throws JSONException {
		JSONArray data = jsonObject.optJSONArray("data");
		OrderInfo mInfo = null;
		if (data != null) {
			JSONObject order_info = data.optJSONObject(0).optJSONObject(
					"order_info");
			mInfo = new OrderInfo();
			mInfo.mStoreInfo = new Store().parse(order_info
					.getJSONObject("store_info"));
			mInfo.mScoreInfo = new ScoreInfo().parse(data.optJSONObject(0)
					.getJSONObject("score_info"));
			mInfo.mConsumeInfo = new Consume().parse(data.optJSONObject(0)
					.optJSONObject("consume_info"));
			mInfo.order_date = order_info.optString("create_date");
			mInfo.order_id = order_info.optString("order_id");
			mInfo.order_no = order_info.optString("order_no");
			mInfo.people = order_info.optString("people");
			mInfo.total = order_info.optString("total");
			ArrayList<Menu> menus = new ArrayList<Menu>();
			if (order_info.optJSONArray("menu_list") != null
					&& order_info.optJSONArray("menu_list").length() > 0) {
				for (int i = 0; i < order_info.optJSONArray("menu_list")
						.length(); i++) {
					Menu menu = new Menu().parse(order_info.optJSONArray(
							"menu_list").optJSONObject(i));
					menus.add(menu);
				}
				mInfo.menuList = menus;
			}
			mInfo.menu_type_count = order_info.optString("menu_type_count");
			mInfo.dynamicId = data.optJSONObject(0).optString("dynamic_id");
			mInfo.originalTotal = order_info.optString("original_total");
			mInfo.order_type = order_info.optString("order_type");
		}
		return mInfo;
	}
}
