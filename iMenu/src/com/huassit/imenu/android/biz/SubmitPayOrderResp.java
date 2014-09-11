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
import com.huassit.imenu.android.parser.SubmitOrderParser;

/**
 * 提交支付订单,如果失败，连续访问三次
 * 
 * @author shang_shang
 * 
 */
public class SubmitPayOrderResp extends BaseInvoker {

	/** 会员ID */
	private String member_id;
	/** 订单ID */
	private String order_id;
	/** 支付类型 */
	private String payment_type;
	/** 支付返回参数 */
	private String payment_result;
	private String token;
	private SubmitOrderParser mDetailParser;

	public SubmitPayOrderResp(Activity ctx, Handler handler, String token,
			String member_id, String order_id, String payment_type,
			String payment_result) {
		super(ctx, handler);
		this.token = token;
		this.member_id = member_id;
		this.order_id = order_id;
		this.payment_type = payment_type;
		this.payment_result = payment_result;
		mDetailParser = new SubmitOrderParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.Submit_PayOrder));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("order_id", order_id);
			json.put("payment_type", payment_type);
			json.put("payment_result", payment_result);
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
			OrderInfo info = mDetailParser.doParse(response);
			if (mDetailParser.getResponseCode() == BaseParser.SUCCESS
					&& info != null) {
				sendMessage(ON_SUCCESS, info);
			} else {
				sendMessage(ON_FAILURE, mDetailParser.getResponseMessage());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
