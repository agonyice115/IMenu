package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Score;

/**
 * 积分记录列表解析类
 */
public class ScoreListParser extends BaseParser<List<Score>> {

	@Override
	public List<Score> parseData(JSONObject jsonObject) throws JSONException {
		List<Score> mScores = new ArrayList<Score>();
		JSONObject data = jsonObject.optJSONObject("data");
		if (data != null) {
			JSONArray score_list = data.getJSONArray("score_list");
			for (int i = 0; i < score_list.length(); i++) {
				JSONObject obj = score_list.getJSONObject(i);
				Score score = new Score();
				score.score_date = obj.optString("score_date");
				score.score = obj.optString("score");
				score.score_description = obj.optString("score_description");
				score.score_id = obj.optString("score_id");
				score.score_title = obj.optString("score_title");
				mScores.add(score);
			}
		}
		return mScores;
	}
}
