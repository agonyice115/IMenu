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
import com.huassit.imenu.android.model.DynamicMessageList;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.GetDynamicMessageListParser;
import com.huassit.imenu.android.util.StringUtils;

/**
 * 获取动态消息列表请求处理类
 * 
 * @author shang_shang
 * 
 */
public class GetDynamicMessageListResp extends BaseInvoker {

	/** 用户ID */
	private String member_id;
	/** 消息请求数量 */
	private String message_count;
	/** 页数 */
	private String page;
	/** 1-已读/0-未读 */
	private String is_open;
	/** 最后一个动态消息的(在未读完成后读取已读列表需要传入此字段) */
	private String last_dynamic_message_to_member_id;
	private String token;

	private GetDynamicMessageListParser messageListParser;

	public GetDynamicMessageListResp(Activity ctx, Handler handler,
			String member_id, String message_count, String page,
			String is_open, String last_dynamic_message_to_member_id,
			String token) {
		super(ctx, handler);
		this.member_id = member_id;
		this.message_count = message_count;
		this.page = page;
		this.token = token;
		this.is_open = is_open;
		this.last_dynamic_message_to_member_id = last_dynamic_message_to_member_id;
		messageListParser = new GetDynamicMessageListParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_DYNAMIC_MESSAGE_LIST));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("message_count", message_count);
			json.put("page", page);
			json.put("is_open", is_open);
			if (!StringUtils.isBlank(last_dynamic_message_to_member_id)) {
				json.put("last_dynamic_message_to_member_id",
						last_dynamic_message_to_member_id);
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
			List<DynamicMessageList> messages = messageListParser
					.doParse(response);
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
