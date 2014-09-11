package com.huassit.imenu.android.model;

import org.json.JSONObject;

/**
 * Created by Sylar on 14-7-15.
 */
public class Version extends BaseModel {
    public String version;
    public String url;

    @Override
    public BaseModel parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            version = jsonObject.optString("version");
            url = jsonObject.optString("url");
        }
        return this;
    }
}
