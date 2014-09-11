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
import com.huassit.imenu.android.model.DynamicImage;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.DynamicImageParser;

/**
 * 编辑动态封面图片请求处理类
 * 
 * @author 上官
 * 
 */
public class EditMemberDynamicImageResp extends BaseInvoker {

	private Member member;
	private String filePath;
	private DynamicImageParser mImageParser;

	public EditMemberDynamicImageResp(Activity ctx, Handler handler,
			Member member, String filePath) {
		super(ctx, handler);
		this.member = member;
		this.filePath = filePath;
		mImageParser = new DynamicImageParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.EDIT_MEMBER_DYNAMICIMAGE));
		nvp.add(new BasicNameValuePair("token", member.token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		System.out.println(nvp);
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("memberId", member.memberId);
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
		return HTTP_METHOD.METHOD_UPLOAD_IMAGE;
	}

	@Override
	public NameValuePair getUploadFiles() {
		return new BasicNameValuePair("image", filePath);
	}

	@Override
	protected void handleResponse(String response) {
		System.out.println(response);
		try {
			DynamicImage icon = mImageParser.doParse(response);
			if (mImageParser.getResponseCode() == BaseParser.SUCCESS
					&& icon != null) {
				sendMessage(ON_SUCCESS, icon);
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
