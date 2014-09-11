package com.huassit.imenu.android.parser;

import com.huassit.imenu.android.model.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sylar on 14-7-15.
 */
public class GetFrontPageParser extends BaseParser<Map<String, BaseModel>> {
    @Override
    public Map<String, BaseModel> parseData(JSONObject jsonObject) throws JSONException {
        Map<String, BaseModel> result = new HashMap<String, BaseModel>();
        JSONObject data = jsonObject.optJSONObject("data");
        if (data != null) {
            result.put("menu_info", new Menu().parse(data.optJSONObject("menu_info")));
            result.put("store_info", new Store().parse(data.optJSONObject("store_info")));
            result.put("dynamic_info", new Dynamic().parse(data.optJSONObject("dynamic_info")));
            result.put("member_info", new Member().parse(data.optJSONObject("member_info")));
        }
        return result;
    }
}
