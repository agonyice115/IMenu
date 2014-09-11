package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.huassit.imenu.android.parser.MenuGoodsParser;

public class GetMenuGoodsResp extends BaseInvoker{

	private MenuGoodsParser goodsParser;
	private MemberMenu memberMenu;
	private String count;
	private String page;
	private String  memberId;
	private String token;
	
	public GetMenuGoodsResp(Activity ctx, Handler handler,String memberId,MemberMenu memberMenu,String count,String page,String token) {
		super(ctx, handler);
		this.memberMenu =memberMenu;
		this.count =count;
		this.page =page;
		this.memberId =memberId;
		this.token =token;
		goodsParser =new MenuGoodsParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_MENU_GOODS));
		if("".equals(token)){
			nvp.add(new BasicNameValuePair("token", ""));
		}else{
			nvp.add(new BasicNameValuePair("token", token));
		}
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	
	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("member_id",memberId);
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
		System.out.println("goods"+response);
		try {
			HashMap<String, Object> map =(HashMap<String, Object>) goodsParser.doParse(response);
			if(goodsParser.getResponseCode()==BaseParser.SUCCESS && map!=null){
				sendMessage(ON_SUCCESS, map);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
