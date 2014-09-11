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
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.MenuAndStoreParser;

public class GetMenuDetailResp extends BaseInvoker{

	private String menuId;
	private MenuAndStoreParser menuAndStoreParser;
	public GetMenuDetailResp(Activity ctx, Handler handler,String menuId) {
		super(ctx, handler);
		this.menuId =menuId;
		menuAndStoreParser =new MenuAndStoreParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_MENU_DETAIL));
		nvp.add(new BasicNameValuePair("token", ""));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}
	
	

	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("menu_id", menuId);
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
			Map<String,Object> menuAndStore =menuAndStoreParser.doParse(response);
			if(menuAndStoreParser.getResponseCode()==BaseParser.SUCCESS && menuAndStore!=null){
				sendMessage(ON_SUCCESS, menuAndStore);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
