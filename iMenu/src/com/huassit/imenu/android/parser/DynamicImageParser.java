package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;
import com.huassit.imenu.android.model.DynamicImage;

public class DynamicImageParser extends BaseParser<DynamicImage> {

	@Override
	public DynamicImage parseData(JSONObject jsonObject) throws JSONException {
		DynamicImage mImage = new DynamicImage();
		JSONObject jsonData = jsonObject.optJSONObject("data");
		mImage = new DynamicImage().parse(jsonData);
		return mImage;
	}

}
