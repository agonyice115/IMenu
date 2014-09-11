package com.huassit.imenu.android.model;

import java.io.Serializable;

import org.json.JSONObject;

public class Verify extends BaseModel implements Serializable {

	/** 验证码 */
	public String verifyCode;
	/** 生成的CODE,用于用户在注册第二步骤加密后或快速登录接口加密后回传服务器 */
	public String menuCode;

	@Override
	public Verify parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			verifyCode = jsonObject.optString("verifyCode");
			menuCode = jsonObject.optString("menuCode");
		}
		return this;
	}

}
