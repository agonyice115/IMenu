package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;

/**
 * 轮询请求是否有新消息
 * 
 * @author shang_guan
 * 
 */
public class HaveNewsMessageResp extends BaseInvoker {

	private String member_id;
	private String token;

	public HaveNewsMessageResp(Context ctx, Handler handler, String member_id,
			String token) {
		super(ctx, handler);
		this.member_id = member_id;
		this.token = token;
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.HAVE_NEW_SYSTEM_MESSAGE));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject member = new JSONObject();
		try {
			member.put("member_id", this.member_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return member.toString();
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
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.optString("error").equals("")) {
				JSONObject data = jsonObject.optJSONObject("data");
				if (null != data && !data.equals("")) {
					if (null != data.optJSONObject("new_info")) {
						sendMessage(ON_SUCCESS, data.optJSONObject("new_info"));
					}
				} else {
					sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
				}
			} else {
				sendMessage(ON_FAILURE, jsonObject.optString("error"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
