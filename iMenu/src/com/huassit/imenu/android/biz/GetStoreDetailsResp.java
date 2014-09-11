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
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.StoreParser;
import com.huassit.imenu.android.util.StringUtils;

public class GetStoreDetailsResp extends BaseInvoker{

	private Store store;
	private String memberId;
	private String token;
	private StoreParser storeParser;
	
	public GetStoreDetailsResp(Activity ctx, Handler handler,Store store,String memberId,String token ) {
		super(ctx, handler);
		this.store =store;
		this.memberId =memberId;
		this.token =token;
		storeParser =new StoreParser();
		
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_STORE_DETAILS));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}
	
	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("store_id", store.id);
			if(!StringUtils.isBlank(token)){
				json.put("member_id", memberId);
			}else{
				json.put("member_id", "");
			}
			json.put("longitude_num",store.longitude);
			json.put("latitude_num", store.latitude);
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
			Store store =storeParser.doParse(response);
			if(storeParser.getResponseCode()==BaseParser.SUCCESS && store!=null){
				sendMessage(ON_SUCCESS, store);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
