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
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.parser.AddOrderParser;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.util.StringUtils;

/**
 * 生成订单接口
 * 
 * @author shang_guan
 * 
 */
public class AddOrderResp extends BaseInvoker {
	/** 会员ID */
	private String member_id;
	/** 电话 */
	private String mobile;
	/** 邮件 */
	private String email;

	private Cart cart;
	private String token;

	private AddOrderParser addOrderParser;

	public AddOrderResp(Context ctx, Handler handler, String member_id,
			String mobile, String email, Cart cart, String token) {
		super(ctx, handler);
		this.token = token;
		this.member_id = member_id;
		this.mobile = mobile;
		this.email = email;
		this.cart = cart;
		addOrderParser = new AddOrderParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.ADD_ORDER));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("mobile", mobile);
			json.put("email", email);
			json.put("create_date", cart.createDate);
			if(StringUtils.isBlank(cart.modifyDate)){
				json.put("modify_date", "");
			}else{
				json.put("modify_date", cart.modifyDate);
			}
			json.put("store_id", cart.storeInfo.id);
			json.put("people", cart.people);
			json.put("total", cart.total);
			json.put("menu_list", makeJSONArray(cart.menuList));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	private JSONArray makeJSONArray(ArrayList<Menu> menuList) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < menuList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("menu_id", menuList.get(i).menu_id);
				jsonObject.put("menu_count",
						String.valueOf(menuList.get(i).menu_count));
				jsonArray.put(i, jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
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
			OrderInfo orderInfo = addOrderParser.doParse(response);
			if (addOrderParser.getResponseCode() == BaseParser.SUCCESS
					&& orderInfo != null) {
				sendMessage(ON_SUCCESS, orderInfo);
			} else {
				sendMessage(ON_FAILURE, null);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
