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
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.DefaultParser;

public class EditMemberInfoResp extends BaseInvoker {

	private Member member;

	public EditMemberInfoResp(Activity ctx, Handler handler, Member member) {
		super(ctx, handler);
		this.member = member;
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.EDIT_MEMBER_INFO));
		nvp.add(new BasicNameValuePair("token", member.token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("memberId", member.memberId);
			if (!"".equals(member.memberName)) {
				json.put("memberName", member.memberName);
			}
			json.put("email", member.email);
			json.put("areaCode", member.areaCode);
			json.put("mobile", member.mobile);
			json.put("regionId", member.regionId);
			json.put("status", member.status);
			json.put("signature", member.signature);
			json.put("realname", member.realname);
			json.put("birthday", member.birthday);
			json.put("sex", member.sex);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/** 显示性别 */
	private String getSex(String sex) {
		if (sex.equals("男")) {
			return "1";
		}
		if (sex.equals("女")) {
			return "2";
		}
		return "0";
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
			DefaultParser parser = new DefaultParser();
			parser.doParse(response);
			if (parser.getResponseCode() == BaseParser.SUCCESS) {
				parser.setKey("success");
				sendMessage(ON_SUCCESS, null);
			} else {
				parser.setKey("error");
				sendMessage(ON_FAILURE, parser.getResponseMessage());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
