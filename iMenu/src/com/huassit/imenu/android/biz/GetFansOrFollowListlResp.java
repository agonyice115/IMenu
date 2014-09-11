package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Fans;
import com.huassit.imenu.android.model.FollowAndFans;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.FansListParser;
import com.huassit.imenu.android.parser.FollowListParser;

/**
 * 获取粉丝列表请求类
 */
public class GetFansOrFollowListlResp extends BaseInvoker {

	private final String member_id;
	private final String followed_member_id;
	private final String followed_store_id;
	private final String following_member_id;
	private final String token;
	private FansListParser mFansListParser;
	private FollowListParser mFollowListParser;
	/** 种类标石：1/获取粉丝列表接口,2/获取关注列表接口 */
	private int typeCode;

	public GetFansOrFollowListlResp(Activity ctx, Handler handler,
			String member_id, String followed_member_id,
			String followed_store_id, String token, int typeCode,
			String following_member_id) {
		super(ctx, handler);
		this.member_id = member_id;
		this.followed_member_id = followed_member_id;
		this.followed_store_id = followed_store_id;
		this.following_member_id = following_member_id;
		this.token = token;
		this.typeCode = typeCode;
		if (typeCode == 1) {
			// 获取粉丝接口
			mFansListParser = new FansListParser();
		}
		if (typeCode == 2) {
			// 获取关注接口
			mFollowListParser = new FollowListParser();
		}
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		if (typeCode == 1) {
			nvp.add(new BasicNameValuePair("route", API.GET_FOLLOWED_LIST));
		} else {
			nvp.add(new BasicNameValuePair("route", API.GET_FOLLOWING_LIST));
		}
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", member_id);
			if (typeCode == 1) {
				// 获取粉丝接口
				json.put("followed_member_id", followed_member_id);
				json.put("followed_store_id", followed_store_id);
			}
			if (typeCode == 2) {
				// 获取关注接口
				json.put("following_member_id", following_member_id);
			}
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
			System.out.println("response:"+response);
			if (typeCode == 1) {
				List<Fans> memberDetail = mFansListParser.doParse(response);
				if (mFansListParser.getResponseCode() == BaseParser.SUCCESS) {
					if (memberDetail != null) {
						sendMessage(ON_SUCCESS, memberDetail);
					} else {
						sendMessage(ON_FAILURE, "20|暂无粉丝！");
					}
				} else {
					sendMessage(ON_FAILURE,
							mFansListParser.getResponseMessage());
				}
			} else {
				FollowAndFans mAndFans = mFollowListParser.doParse(response);
				if (mFollowListParser.getResponseCode() == BaseParser.SUCCESS) {
					if (mAndFans != null) {
						sendMessage(ON_SUCCESS, mAndFans);
					} else {
						sendMessage(ON_FAILURE, "20|暂无关注！");
					}
				} else {
					sendMessage(ON_FAILURE,
							mFollowListParser.getResponseMessage());
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
		}
	}
}
