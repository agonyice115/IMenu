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
import com.huassit.imenu.android.model.StoreRecommand;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.StoreRecommandParser;

/**
 * 获取店长推荐列表请求处理类
 * 
 * @author Administrator
 * 
 */
public class GetRecommendMenuListResp extends BaseInvoker {

	private StoreRecommandParser mParser;
	private String storeId;
	private String recommed_people;

	public GetRecommendMenuListResp(Activity ctx, Handler handler,
			String storeId, String recommed_people) {
		super(ctx, handler);
		this.storeId = storeId;
		this.recommed_people = recommed_people;
		mParser = new StoreRecommandParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_RECOMMEND_MENULIST));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("store_id", storeId);
			json.put("recommend_people", recommed_people);
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
			List<StoreRecommand> mRecommand = mParser.doParse(response);
			if (mParser.getResponseCode() == BaseParser.SUCCESS
					&& mRecommand != null) {
				sendMessage(ON_SUCCESS, mRecommand);
				// sendMessage(ON_FAILURE, "response null");
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
