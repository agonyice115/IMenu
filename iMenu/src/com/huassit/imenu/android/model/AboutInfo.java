package com.huassit.imenu.android.model;

import org.json.JSONObject;

public class AboutInfo extends BaseModel<AboutInfo> {

    public String key;
    public String value;

    @Override
    public AboutInfo parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            key = jsonObject.optString("key");
            value = jsonObject.optString("value");
        }
        return this;
    }
}
