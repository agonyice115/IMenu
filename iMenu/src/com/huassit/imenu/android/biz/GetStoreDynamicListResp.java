package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.StoreDynamicListParser;

public class GetStoreDynamicListResp extends BaseInvoker {

	private StoreDynamicListParser storeDynamicListParser;
	private String token;
	private String memberId;
	private Store store;
	private String dynamicCount;
	private String page;
	private String menuCount;
	private String filterDateType;
	private String dynamicLastDate;
	private String lat;
	private String log;

	public GetStoreDynamicListResp(Context ctx, Handler handler, String token,
			String memberId, Store store, String dynamicCount, String page,
			String menuCount, String filterDateType, String dynamicLastDate,
			String lat, String log) {
		super(ctx, handler);
		this.token = token;
		this.memberId = memberId;
		this.store = store;
		this.dynamicCount = dynamicCount;
		this.page = page;
		this.menuCount = menuCount;
		this.lat = lat;
		this.log = log;
		this.filterDateType = filterDateType;
		this.dynamicLastDate = dynamicLastDate;
		storeDynamicListParser = new StoreDynamicListParser(
				Integer.parseInt(filterDateType));// 公用解析类，传0，不区分时间分类
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_STORE_DYNAMICLIST));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", memberId);
			json.put("store_id", store.id);
			json.put("dynamic_count", dynamicCount);
			json.put("page", page);
			json.put("menu_count", menuCount);
			json.put("filter_date_type", filterDateType);
			json.put("longitude_num", log);
			json.put("latitude_num", lat);
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
			Map<String, Object> storeDynamicData = storeDynamicListParser
					.doParse(response);
			if (storeDynamicListParser.getResponseCode() == BaseParser.SUCCESS
					&& storeDynamicData != null) {
				sendMessage(ON_SUCCESS, storeDynamicData);
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
