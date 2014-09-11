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
import com.huassit.imenu.android.model.MenuAndCategory;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.MenuAndCategoryParser;

public class GetMenuListAndCategoryListResp extends BaseInvoker{

	private MenuAndCategoryParser menuParser;
	private String storeId;
	public GetMenuListAndCategoryListResp(Activity ctx, Handler handler,String storeId) {
		super(ctx, handler);
		this.storeId =storeId;
		menuParser =new MenuAndCategoryParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_MENULIST_AND_CATEGORYLIST));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("store_id", storeId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
			MenuAndCategory menuObjectList =menuParser.doParse(response);
			if(menuParser.getResponseCode()==BaseParser.SUCCESS && menuObjectList!=null){
				sendMessage(ON_SUCCESS, menuObjectList);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
