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

/**
 * 更改关注状态请求类
 */
public class EditFollowStatusResp extends BaseInvoker {

	/** 会员ID */
	private final String member_id;
	/** 关注会员ID */
	private final String following_member_id;
	/** 关注商家ID */
	private final String following_store_id;
	/** 状态更改：1-添加，2-取消 */
	private final String following_status;
	private final String token;

	public EditFollowStatusResp(Activity ctx, Handler handler, String token,
			String member_id, String following_member_id,
			String following_store_id, String following_status) {
		super(ctx, handler);
		this.member_id = member_id;
		this.following_member_id = following_member_id;
		this.following_store_id = following_store_id;
		this.following_status = following_status;
		this.token = token;
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.EDIT_FOLLOWING_STATUS));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("following_member_id", following_member_id);
			json.put("following_store_id", following_store_id);
			json.put("following_status", following_status);
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
			JSONObject mObject = new JSONObject(response);
			if (mObject.optString("error").equals("")) {
				JSONObject data = mObject.optJSONObject("data");
				if (data != null) {
					sendMessage(ON_SUCCESS, data);
				} else {
					sendMessage(ON_FAILURE, mObject.optString("success"));
				}
			} else {
				sendMessage(ON_FAILURE, mObject.optString("error"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
		}
	}
}
