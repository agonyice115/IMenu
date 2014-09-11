package com.huassit.imenu.android.model;

import org.json.JSONObject;

/**
 * Created by Sylar on 14-7-15.
 */
public class SearchVague {
    public String resultName;
    public String resultCount;


    public SearchVague parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            resultName = jsonObject.optString("result_name");
            resultCount = jsonObject.optString("result_count");
        }
        return this;
    }
}
