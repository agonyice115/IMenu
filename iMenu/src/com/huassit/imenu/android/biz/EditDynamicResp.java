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
 * 发布动态，编辑动态标题接口
 * 
 * @author shang_guan
 * 
 */
public class EditDynamicResp extends BaseInvoker {

	/** 用户ID */
	private String member_id;
	/** 动态ID */
	private String dynamic_id;
	/** 标题 */
	private String title;
	/** 2-保存/1-保存并发布/0-删除 */
	private String dynamic_type;
	private String toekn;

	public EditDynamicResp(Context ctx, Handler handler, String member_id,
			String dynamic_id, String title, String dynamic_type, String toekn) {
		super(ctx, handler);
		this.member_id = member_id;
		this.dynamic_id = dynamic_id;
		this.title = title;
		this.dynamic_type = dynamic_type;
		this.toekn = toekn;
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.EDIT_DYNAMIC));
		nvp.add(new BasicNameValuePair("token", toekn));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("dynamic_id", dynamic_id);
			json.put("title", title);
			json.put("dynamic_type", dynamic_type);
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
					sendMessage(ON_SUCCESS, object.optString("success"));
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
