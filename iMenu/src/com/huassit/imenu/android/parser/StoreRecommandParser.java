package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.StoreRecommand;

public class StoreRecommandParser extends BaseParser<List<StoreRecommand>> {

	@Override
	public List<StoreRecommand> parseData(JSONObject jsonObject)
			throws JSONException {
		JSONObject jsonData = jsonObject.optJSONObject("data");
		List<StoreRecommand> mList = new ArrayList<StoreRecommand>();
		JSONArray mArray = jsonData.optJSONArray("menu_recommend_list");
		for (int i = 0; i < mArray.length(); i++) {
			JSONObject object = mArray.optJSONObject(i);
			StoreRecommand mRecommand = new StoreRecommand().parse(object);
			mList.add(mRecommand);
		}
		return mList;
	}
}
