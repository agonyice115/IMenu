package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;

import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.OrderListParser;

/**
 * 获取订单列表
 * 
 * @author shang_guan
 * 
 */
public class GetOrderListResp extends BaseInvoker {

	private OrderListParser mOrderListParser;
	private String token;
	/** 用户ID */
	private String memberId;
	/** 订单数量 */
	private String order_count;
	/** 第几页 */
	private String page;
	/** 时间类型1-年/2-月/3-周 */
	private String filter_date_type;
	/** 订单类型 */
	private String order_type;

	public GetOrderListResp(Activity ctx, Handler handler, String token,
			String memberId, String order_count, String page,
			String filter_date_type, String order_type) {
		super(ctx, handler);
		this.memberId = memberId;
		this.order_count = order_count;
		this.page = page;
		this.filter_date_type = filter_date_type;
		this.order_type = order_type;
		this.token = token;
		mOrderListParser = new OrderListParser(2);
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_ORDER_LIST));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", memberId);
			json.put("order_count", order_count);
			json.put("page", page);
			json.put("filter_date_type", filter_date_type);
			json.put("order_type", order_type);
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
			Map<String, Object> dynamicData = mOrderListParser
					.doParse(response);
			if (mOrderListParser.getResponseCode() == BaseParser.SUCCESS
					&& dynamicData != null) {
				sendMessage(ON_SUCCESS, dynamicData);
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
