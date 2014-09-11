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
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.MemberMenuParser;

public class GetMenuInfoListResp extends BaseInvoker{

	private MemberMenuParser memberMenuParser;
	private String token;
	private String menuId;
	private String count;
	private String page;
	private String memberId;
	public GetMenuInfoListResp(
			Activity ctx, Handler handler,String token,String menuId,String count,String page,String memberId) {
		super(ctx, handler);
		this.token =token;
		this.menuId =menuId;
		this.count =count;
		this.page =page;
		this.memberId =memberId;
		memberMenuParser =new MemberMenuParser();
		
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_MENU_INFO_LIST));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("menu_id", menuId);
			json.put("count", count);
			json.put("page", page);
			json.put("member_Id", memberId);
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
			ArrayList<MemberMenu> memberMenuList =memberMenuParser.doParse(response);
			if(memberMenuParser.getResponseCode()==BaseParser.SUCCESS && memberMenuList!=null){
				sendMessage(ON_SUCCESS, memberMenuList);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	

}
