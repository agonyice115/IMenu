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
import com.huassit.imenu.android.parser.MenuImageParser;

/**
 * 编辑动态菜品图片
 * 
 * @author Administrator
 * 
 */
public class EditMenuImageResp extends BaseInvoker {

	private String filePath;
	private String member_id;
	private String dynamic_id;
	private String menu_id;
	private String token;
	private MenuImageParser iconParser;

	public EditMenuImageResp(Activity ctx, Handler handler, String member_id,
			String dynamic_id, String menu_id, String filePath, String token) {
		super(ctx, handler);
		this.member_id = member_id;
		this.filePath = filePath;
		this.dynamic_id = dynamic_id;
		this.menu_id = menu_id;
		this.token = token;
		iconParser = new MenuImageParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.EDIT_MENU_IMAGE));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		System.out.println(nvp);
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			json.put("dynamic_id", dynamic_id);
			json.put("menu_id", menu_id);
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
			MemberMenu icon = iconParser.doParse(response);
			if (iconParser.getResponseCode() == BaseParser.SUCCESS
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
