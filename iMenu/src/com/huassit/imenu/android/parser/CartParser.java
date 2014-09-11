package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Cart;

public class CartParser extends BaseParser<Cart>{

	@Override
	public Cart parseData(JSONObject jsonObject) throws JSONException {
		JSONObject jsonData =jsonObject.optJSONObject("data");
		Cart cart =new Cart().parserCart(jsonData);
		return cart;
	}

}
