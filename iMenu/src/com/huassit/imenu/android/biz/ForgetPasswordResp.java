package com.huassit.imenu.android.biz;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.huassit.imenu.android.MD5;
import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Verify;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.VerifyParser;

public class ForgetPasswordResp extends BaseInvoker{

	private String loginName;
	private String loginType;
	private VerifyParser parser;
	
	public ForgetPasswordResp(Context ctx, Handler handler,String loginName,String loginType) {
		super(ctx, handler);
		this.loginName =loginName;
		this.loginType =loginType;
		parser =new VerifyParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.FORGET_PASSWORD));
		nvp.add(new BasicNameValuePair("token", ""));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("type", loginType);
			json.put("loginName", loginName);
			json.put("code", MD5.MD5Encode(loginName+"siyo_imenu"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
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
	            Verify verify =parser.doParse(response);
	            if (parser.getResponseCode() == BaseParser.SUCCESS) {
	                if (verify != null) {
	                    sendMessage(ON_SUCCESS, verify);
	                } else {
	                    sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
	                }
	            } else {
	                sendMessage(ON_FAILURE, parser.getResponseMessage());
	            }

	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	}

}
