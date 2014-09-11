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
import com.huassit.imenu.android.model.Message;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.GetSystemMessageListParser;
import com.huassit.imenu.android.util.StringUtils;

/**
 * 获取新消息请求
 * 
 * @author shangshang
 * 
 */
public class GetSystemMessageListResp extends BaseInvoker {

	/** 用户ID */
	private String member_id;
	/** 消息请求数量 */
	private String message_count;
	/** 页数 */
	private String page;
	/** 1-已读/0-未读 */
	private String is_open;
	private String toekn;
	private GetSystemMessageListParser messageListParser;

	public GetSystemMessageListResp(Activity ctx, Handler handler,
			String token, String member_id, String message_count, String page,
			String is_open) {
		super(ctx, handler);
		this.member_id = member_id;
		this.message_count = message_count;
		this.page = page;
		this.is_open = is_open;
		this.toekn = token;
		messageListParser = new GetSystemMessageListParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_SYSTEM_MESSAGELIST));
		nvp.add(new BasicNameValuePair("token", toekn));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("message_count", message_count);
			json.put("page", page);
			if(!StringUtils.isBlank(is_open)){
				json.put("is_open", is_open);
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
			List<Message> messages = messageListParser.doParse(response);
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
