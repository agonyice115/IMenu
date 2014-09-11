package com.huassit.imenu.android.model;

import org.json.JSONObject;

/**
 * Created by Sylar on 14-6-30.
 */
public class SearchHistory extends BaseModel<SearchHistory> {
    public String keyword;
    public String lastUpdated;

    @Override
    public String toString() {
        return keyword;
    }

    @Override
    public SearchHistory parse(JSONObject jsonObject) {
        return null;
    }
}
