package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.DynamicMessageList;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.HaveNewDynamicMessageParser;

/**
 * 轮询请求是否有新动态提示
 * 
 * @author shang_guan
 * 
 */
public class HaveNewDynamicMessageResp extends BaseInvoker {

	private String member_id;
	private String token;
	private HaveNewDynamicMessageParser mDynamicMessageParser;

	public HaveNewDynamicMessageResp(Context ctx, Handler handler,
			String member_id, String token) {
		super(ctx, handler);
		this.member_id = member_id;
		this.token = token;
		mDynamicMessageParser = new HaveNewDynamicMessageParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.HAVE_NEW_DYNAMIC_MESSAGE));
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
			DynamicMessageList mDynamicMessageList = mDynamicMessageParser
					.doParse(response);
			if (mDynamicMessageParser.getResponseCode() == BaseParser.SUCCESS
					&& mDynamicMessageList != null) {
				sendMessage(ON_SUCCESS, mDynamicMessageList);
			} else {
				sendMessage(ON_FAILURE,
						mDynamicMessageParser.getResponseMessage());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
