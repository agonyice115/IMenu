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
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.DynamicDetailParser;

public class GetDynamicDetailResp extends BaseInvoker {

	private Dynamic dynamic;
	private DynamicDetailParser dynamicDetailParser;
	private String token;
	private String memberId;

	public GetDynamicDetailResp(Activity ctx, Handler handler, Dynamic dynamic,
			String token, String memberId) {
		super(ctx, handler);
		this.dynamic = dynamic;
		this.token = token;
		this.memberId = memberId;
		dynamicDetailParser = new DynamicDetailParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_DYNAMIC_DETAIL));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", memberId);
			json.put("dynamic_id", dynamic.getDynamicId());
			if (dynamic.getStoreInfo() != null) {
				json.put("longitude_num", dynamic.getStoreInfo().longitude);
				json.put("latitude_num", dynamic.getStoreInfo().latitude);
			}
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
			Map<String, Object> map = dynamicDetailParser.doParse(response);
			if (dynamicDetailParser.getResponseCode() == BaseParser.SUCCESS
					&& map != null) {
				sendMessage(ON_SUCCESS, map);
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
