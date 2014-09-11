package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.model.ScoreInfo;

public class AddOrderParser extends BaseParser<OrderInfo>{

	@Override
	public OrderInfo parseData(JSONObject jsonObject) throws JSONException {
		OrderInfo orderInfo=null;
		JSONObject obj=jsonObject.optJSONObject("data"); 
		if(obj!=null){
			orderInfo =new OrderInfo().parse(obj.optJSONObject("order_info"));
			orderInfo.memberId =obj.optString("member_id");
			orderInfo.dynamicId =obj.optString("dynamic_id");
			orderInfo.scoreInfo =new ScoreInfo().parse(obj.optJSONObject("score_info"));
		}
		return orderInfo;
	}

}
