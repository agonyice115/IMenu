package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sylar on 14-7-10.
 */
public class DefaultParser extends BaseParser<Object> {

    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Object parseData(JSONObject jsonObject) throws JSONException {
        JSONObject data = jsonObject.optJSONObject("data");
        if (data != null) {
            return data.optString(key);
        }
        return null;
    }
}
