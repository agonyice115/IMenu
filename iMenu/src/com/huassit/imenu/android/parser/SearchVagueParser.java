package com.huassit.imenu.android.parser;

import com.huassit.imenu.android.model.SearchVague;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sylar on 14-7-15.
 */
public class SearchVagueParser extends BaseParser<List<SearchVague>> {

    @Override
    public List<SearchVague> parseData(JSONObject jsonObject) throws JSONException {
        JSONObject data = jsonObject.optJSONObject("data");
        List<SearchVague> result = new ArrayList<SearchVague>();
        if (data != null) {
            JSONArray resultList = data.optJSONArray("result_list");
            for (int i = 0; i < resultList.length(); i++) {
                result.add(new SearchVague().parse(resultList.getJSONObject(i)));
            }
        }
        return result;
    }
}
