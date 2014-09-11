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
import com.huassit.imenu.android.model.Comment;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.DynamicCommentParser;

public class GetDynamicCommentResp extends BaseInvoker{

	private DynamicCommentParser commentParser;
	private Dynamic dynamic;
	private String count;
	private String page;
	private String token;
	
	public GetDynamicCommentResp(Activity ctx, Handler handler,Dynamic dynamic,String count,String page,String token) {
		super(ctx, handler);
		this.dynamic =dynamic;
		this.count =count;
		this.page =page;
		this.token=token;
		commentParser =new DynamicCommentParser();
	}


	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new  ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_DYNAMIC_COMMENT));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("member_id", "");
			json.put("dynamic_id",dynamic.getDynamicId());
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
		try {
			ArrayList<Comment> commentList =commentParser.doParse(response);
			if(commentParser.getResponseCode()==BaseParser.SUCCESS && commentList!=null){
				sendMessage(ON_SUCCESS, commentList);
			}else{
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
