package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;

import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Comment;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.MenuCommentParser;

public class GetMenuCommentResp extends BaseInvoker{

	private MenuCommentParser commentParser;
	private MemberMenu memberMenu;
	private String count;
	private String page;
	public GetMenuCommentResp(Activity ctx, Handler handler,MemberMenu memberMenu,String count,String page) {
		super(ctx, handler);
		this.memberMenu =memberMenu;
		this.count =count;
		this.page =page;
		commentParser =new MenuCommentParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_MENU_COMMENT));
		if(memberMenu.member_info==null){
			nvp.add(new BasicNameValuePair("token", ""));
		}else{
			nvp.add(new BasicNameValuePair("token", memberMenu.member_info.token));
		}
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}


	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			if(memberMenu.member_info==null){
				json.put("member_id", "");
			}else{
				json.put("member_id", memberMenu.member_info.memberId);
				
			}
			json.put("member_menu_image_id",memberMenu.member_menu_image_id);
			json.put("count", count);
			json.put("page", page);
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
		System.out.println("comment"+response);
		try {
			Map<String,Object> map =commentParser.doParse(response);
			if(commentParser.getResponseCode()==BaseParser.SUCCESS && map!=null){
				sendMessage(ON_SUCCESS, map);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
