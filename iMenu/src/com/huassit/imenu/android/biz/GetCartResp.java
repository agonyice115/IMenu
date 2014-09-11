package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Cart;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.CartParser;

public class GetCartResp extends BaseInvoker{

	private String token;
	private String memberId;
	private CartParser cartParser;
	
	public GetCartResp(Context ctx, Handler handler,String token,String memberId) {
		super(ctx, handler);
		this.token =token;
		this.memberId =memberId;
		cartParser =new CartParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_CART));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	
	
	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("member_id", memberId);
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
			Cart cart =cartParser.doParse(response);
			if(cartParser.getResponseCode()==BaseParser.SUCCESS && cart!=null){
				sendMessage(ON_SUCCESS, cart);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
