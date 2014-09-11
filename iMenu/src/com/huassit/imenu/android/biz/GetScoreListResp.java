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
import com.huassit.imenu.android.model.Score;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.ScoreListParser;

/**
 * 获取积分记录列表请求处理类
 * 
 * @author Administrator
 * 
 */
public class GetScoreListResp extends BaseInvoker {

	private ScoreListParser mScoreListParser;
	private String token;
	/** 用户ID */
	private String memberId;
	/** 数量 */
	private String score_count;
	/** 页数 */
	private String page;

	public GetScoreListResp(Activity ctx, Handler handler, String memberId,
			String score_count, String page, String token) {
		super(ctx, handler);
		this.memberId = memberId;
		this.page = page;
		this.score_count = score_count;
		this.token = token;
		mScoreListParser = new ScoreListParser();
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.GET_SCORE_LIST));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json = new JSONObject();
		try {
			json.put("member_id", memberId);
			json.put("page", page);
			json.put("score_count", score_count);
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
			List<Score> mScoresList = mScoreListParser.doParse(response);
			if (mScoreListParser.getResponseCode() == BaseParser.SUCCESS
					&& mScoresList != null) {
				sendMessage(ON_SUCCESS, mScoresList);
			} else {
				sendMessage(ON_FAILURE, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
