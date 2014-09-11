package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.huassit.imenu.android.MD5;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.UserInfoParser;

public class ResetPasswordResp extends BaseInvoker{

	private String loginType;
	private String loginName;
	private String newPassword;
	private String menuCode;
	private UserInfoParser userParser;
	
	public ResetPasswordResp(Context ctx, Handler handler,String loginType,String loginName,String newPassword,String menuCode) {
		super(ctx, handler);
		this.loginType =loginType;
		this.loginName =loginName;
		this.newPassword =newPassword;
		this.menuCode =menuCode;
		userParser =new UserInfoParser();
	}

	@Override
	protected boolean isServerHasSession() {
		return true;
	}
	
	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.RESET_PASSWORD));
		nvp.add(new BasicNameValuePair("token", ""));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("type", loginType);
			json.put("loginName", loginName);
			json.put("newPassword", newPassword);
			json.put("menuCode", MD5.MD5Encode(menuCode+"siyo_imenu"));
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
			Member user =userParser.doParse(response);
			if(userParser.getResponseCode()==BaseParser.SUCCESS && user!=null){
				sendMessage(ON_SUCCESS, user);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
