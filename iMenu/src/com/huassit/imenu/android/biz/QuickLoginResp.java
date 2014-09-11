package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Handler;

import com.huassit.imenu.android.MD5;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.UserInfoParser;

/**
 * 快速登陆、注册
 * 
 * @author shangshang
 * 
 */
public class QuickLoginResp extends BaseInvoker {

	private UserInfoParser userParser;
	private String menuCode;
	private String areaCode;
	private String mobile;

	public QuickLoginResp(Activity ctx, Handler handler, String menuCode,
			String areaCode, String mobile) {
		super(ctx, handler);
		this.menuCode = menuCode;
		this.areaCode = areaCode;
		this.mobile = mobile;
		userParser = new UserInfoParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.QUICK_LOGIN));
		nvp.add(new BasicNameValuePair("token", ""));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject member = new JSONObject();
		try {
			member.put("menuCode", MD5.MD5Encode(menuCode + "siyo_imenu"));
			member.put("areaCode", areaCode);
			member.put("mobile", mobile);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return member.toString();
	}
	
	@Override
	protected boolean isServerHasSession() {
		return true;
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
			Member user = userParser.doParse(response);
			if (userParser.getResponseCode() == BaseParser.SUCCESS
					&& user != null) {
				sendMessage(ON_SUCCESS, user);
			} else {
				sendMessage(ON_FAILURE, userParser.getResponseMessage());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
