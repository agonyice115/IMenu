package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.RefundOrderDetailParser;

/**
 * 申请退款请求处理类
 * 
 * @author shang_guan
 * 
 */
public class ApplyReturnResp extends BaseInvoker {

	/** 会员ID */
	private final String memberId;
	/** 退款ID */
	private final String order_id;
	private final String token;
	private RefundOrderDetailParser parser;
	private String return_reason_id;

	public ApplyReturnResp(Activity ctx, Handler handler, String memberId,
			String order_id, String token, String return_reason_id) {
		super(ctx, handler);
		this.memberId = memberId;
		this.order_id = order_id;
		this.token = token;
		this.return_reason_id = return_reason_id;
		parser = new RefundOrderDetailParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.SUBMIT_RETURN));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", memberId);
			json.put("order_id", order_id);
			json.put("return_reason_id", return_reason_id);
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
			OrderInfo info = parser.doParse(response);
			if (parser.getResponseCode() == BaseParser.SUCCESS) {
				if (info != null) {
					sendMessage(ON_SUCCESS, info);
				} else {
					sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
				}
			} else {
				sendMessage(ON_FAILURE, parser.getResponseMessage());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
		}
	}
}
