package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;

import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.CartParser;
import com.huassit.imenu.android.util.StringUtils;

public class EditCartResp extends BaseInvoker {

	private String token;
	private String operatorStatus;
	private String memberId;
	private String storeId;
	private String people;
	private String total;
	private JSONArray jsonMenuList;
	private CartParser cartParser;
	/** 优惠ID */
	private String couponId;

	public EditCartResp(Context ctx, Handler handler, String token,
			String operatorStatus, String memberId, String storeId,
			String people, String total, JSONArray jsonMenuList, String couponId) {
		super(ctx, handler);
		this.token = token;
		this.operatorStatus = operatorStatus;
		this.memberId = memberId;
		this.storeId = storeId;
		this.people = people;
		this.total = total;
		this.couponId = couponId;
		this.jsonMenuList = jsonMenuList;
		cartParser = new CartParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.EDIT_CART));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("operator_status", operatorStatus);
			json.put("member_id", memberId);
			json.put("store_id", storeId);
			json.put("people", people);
			json.put("total", total);
			if(!StringUtils.isBlank(couponId)){
				json.put("coupon_id", couponId);
			}
			json.put("menu_list", jsonMenuList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public String getRequestUrl() {
		return API.server;
	}

	@Override
	public HTTP_METHOD getRequestMethod() {
		return HTTP_METHOD.METHOD_POST;
	}

	@Override
	protected void handleResponse(String response) {
		try {
			Cart cart = cartParser.doParse(response);
			if (cartParser.getResponseCode() == BaseParser.SUCCESS
					&& cart != null) {
				sendMessage(ON_SUCCESS, cart);
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
