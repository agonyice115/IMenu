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

public class RegistMemberResp extends BaseInvoker{
	private UserInfoParser UserParser;
	private Member user;
	private String menuCode;
	public RegistMemberResp(Activity ctx, Handler handler,Member user,String menuCode) {
		super(ctx, handler);
		this.user =user;
		this.menuCode =menuCode;
		UserParser =new UserInfoParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.REGIST_MEMBER_API));
		nvp.add(new BasicNameValuePair("token", ""));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}


	private String makeJsonText(){
		JSONObject member =new JSONObject();
		try {
			String newCode =MD5.MD5Encode(menuCode+"siyo_imenu");
		member.put("menuCode", newCode);
		member.put("password", user.getPassWord());
		member.put("memberName", user.getMemberName());
		member.put("areaCode", "+86");
		member.put("mobile", user.getMobile());
		member.put("sex", user.getSex());
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
			Member user =UserParser.doParse(response);
			if(UserParser.getResponseCode()==BaseParser.SUCCESS && user!=null){
				sendMessage(ON_SUCCESS, user);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
