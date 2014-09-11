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
import com.huassit.imenu.android.parser.DynamicParser;

public class GetRecommandDynamicListResp extends BaseInvoker {

	private DynamicParser dynamicParser;
	private String token;
	private String memberId;
	private String dynamicCount;
	private String page;
	private String menuCount;
	private String filterDateType;
	private String longitude;
	private String latitude;
	private String dynamicLastDate;

	public GetRecommandDynamicListResp(Activity ctx, Handler handler,
			String token, String memberId, String dynamicCount, String page,
			String menuCount, String filterDateType, String longitude,
			String latitude, String dynamicLastDate) {
		super(ctx, handler);
		this.memberId = memberId;
		this.dynamicCount = dynamicCount;
		this.page = page;
		this.menuCount = menuCount;
		this.filterDateType = filterDateType;
		this.longitude = longitude;
		this.latitude = latitude;
		this.dynamicLastDate = dynamicLastDate;
		dynamicParser = new DynamicParser(getTimeType(filterDateType));
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_RECOMMAND_DYNAMICLIST));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", memberId);
			json.put("dynamic_count", dynamicCount);
			json.put("page", page);
			json.put("menu_count", menuCount);
			json.put("filter_date_type", filterDateType);
			json.put("longitude_num", longitude);
			json.put("latitude_num", latitude);
			json.put("dynamic_last_date", dynamicLastDate);
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
			Map<String, Object> dynamicData = dynamicParser.doParse(response);
			if (dynamicParser.getResponseCode()==BaseParser.SUCCESS && dynamicData != null) {
				sendMessage(ON_SUCCESS, dynamicData);
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 向解析类传送时间分类，返回100,200,300，详情在解析类有注释 */
	private int getTimeType(String type) {
		if (type.equals("1")) {
			// 年
			return 1;
		}
		if (type.equals("2")) {
			// 月
			return 2;
		}
		if (type.equals("3")) {
			// 星期
			return 3;
		}
		return 1;
	}

}
