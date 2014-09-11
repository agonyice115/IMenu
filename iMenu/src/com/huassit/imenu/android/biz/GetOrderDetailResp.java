package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;

import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.OrderDetailParser;

/**
 * 获取订单详情
 * 
 * @author shang_guan
 * 
 */
public class GetOrderDetailResp extends BaseInvoker {

	private OrderDetailParser mDetailParser;
	private String token;
	/** 用户ID */
	private String memberId;
	/** 订单ID */
	private String order_id;

	public GetOrderDetailResp(Activity ctx, Handler handler, String token,
			String memberId, String order_id) {
		super(ctx, handler);
		this.memberId = memberId;
		this.order_id = order_id;
		this.token = token;
		mDetailParser = new OrderDetailParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_ORDER_DETAIL));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", memberId);
			json.put("order_id", order_id);
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
			OrderInfo mInfo = mDetailParser.doParse(response);
			if (mDetailParser.getResponseCode() == BaseParser.SUCCESS
					&& mInfo != null) {
				sendMessage(ON_SUCCESS, mInfo);
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
