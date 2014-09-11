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

public class GeneratePayDataResp extends BaseInvoker {

	private String token;
	private String member_id;
	private String order_id;
	private String payment_type;

	public GeneratePayDataResp(Activity ctx, Handler handler, String token,
			String payment_type, String member_id, String order_id) {
		super(ctx, handler);
		this.token = token;
		this.payment_type = payment_type;
		this.member_id = member_id;
		this.order_id = order_id;
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GENERATE_PAY_DATA));
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
			JSONObject object = new JSONObject(response);
			if (object.optString("error").equals("")) {
				if (!object.optString("success").equals("")) {
					sendMessage(ON_SUCCESS, object.optJSONObject("data"));
				} else {
					sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
				}
			} else {
				sendMessage(ON_FAILURE, object.optString("error"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
