package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.util.StringUtils;

public abstract class BaseParser<T> {

	public static final int SUCCESS = 100;

	public static final int FAILURE = 200;

	/**
	 * 失败原因/成功说明*
	 */
	private String responseMessage = "";

	/**
	 * 返回的状态码*
	 */
	private int responseCode = FAILURE;

	public String getResponseMessage() {
		return responseMessage;
	}

	private void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	private void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public abstract T parseData(JSONObject jsonObject) throws JSONException;

	public T doParse(String content) throws JSONException {
		JSONObject jsonObject = new JSONObject(content);
		if (!StringUtils.isBlank(jsonObject.optString("success"))) {
			setResponseCode(SUCCESS);
			if (!StringUtils.isBlank(jsonObject.optString("data"))) {
				return parseData(jsonObject);
			}
		} else if (!StringUtils.isBlank(jsonObject.optString("error"))) {
			setResponseCode(FAILURE);
			setResponseMessage(jsonObject.optString("error"));
		}
		return null;
	}
}
