package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;

import com.huassit.imenu.android.BaseActivity;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Message;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.GetSystemMessageDetailParser;

/**
 * 获取新消息详情
 * 
 * @author shangshang
 * 
 */
public class GetSystemMessageDetailResp extends BaseInvoker {

	/** 用户ID */
	private String member_id;
	/** 消息ID */
	private String system_message_id;
	private String token;

	private GetSystemMessageDetailParser messageListParser;

	public GetSystemMessageDetailResp(Activity ctx, Handler handler,
			String member_id, String system_message_id, String token) {
		super(ctx, handler);
		this.member_id = member_id;
		this.token = token;
		this.system_message_id = system_message_id;
		messageListParser = new GetSystemMessageDetailParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_SYSTEM_MESSAGE_DETAIL));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("system_message_id", system_message_id);
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
			Message messages = messageListParser.doParse(response);
			if (messageListParser.getResponseCode() == BaseParser.SUCCESS) {
				sendMessage(ON_SUCCESS, messages);
			} else {
				sendMessage(ON_FAILURE, messageListParser.getResponseMessage());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
