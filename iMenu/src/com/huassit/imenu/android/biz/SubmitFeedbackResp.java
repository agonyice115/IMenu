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
 * 提交反馈信息接口
 * 
 * @author shang_guan
 * 
 */
public class SubmitFeedbackResp extends BaseInvoker {
	/** 会员ID */
	private String member_id;
	/** 电话 */
	private String mobile;
	/** 邮件 */
	private String email;
	/** 反馈ID */
	private String feedback_id;
	/** 反馈内容 */
	private String feedback_content;
	/** 反馈类型(1-商家,2-菜单,3-其他,4-系统) */
	private String feedback_type;
	/** 错误ID号码(商家ID/菜单ID) */
	private String error_id;

	public SubmitFeedbackResp(Context ctx, Handler handler, String member_id,
			String mobile, String email, String feedback_id,
			String feedback_content, String feedback_type, String error_id) {
		super(ctx, handler);
		this.member_id = member_id;
		this.mobile = mobile;
		this.email = email;
		this.feedback_id = feedback_id;
		this.feedback_content = feedback_content;
		this.feedback_type = feedback_type;
		this.error_id = error_id;
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.SUBMIT_FEEDBACK));
		nvp.add(new BasicNameValuePair("token", ""));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("mobile", mobile);
			json.put("email", email);
			json.put("feedback_id", feedback_id);
			json.put("feedback_content", feedback_content);
			json.put("feedback_type", feedback_type);
			json.put("error_id", error_id);
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
