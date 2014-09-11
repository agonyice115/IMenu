package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Verify;

public class VerifyParser extends BaseParser<Verify>{

	@Override
	public Verify parseData(JSONObject jsonObject) throws JSONException {
		Verify verify =new Verify();
		JSONObject jsonData =jsonObject.optJSONObject("data");
		if(jsonData.optString("verifyCode")!=null){
			verify.verifyCode =jsonData.optString("verifyCode");
		}
		if(jsonData.optString("menuCode")!=null){
			verify.menuCode =jsonData.optString("menuCode");
		}
		return verify;
	}

}
