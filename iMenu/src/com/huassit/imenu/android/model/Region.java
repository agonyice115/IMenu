package com.huassit.imenu.android.model;


import org.json.JSONObject;

import java.util.List;

public class Region extends BaseModel {

    public String region_id;
    public String region_image;
    public String parent_id;
    public String sort_order;
    public String region_name;
    public String aliases_name;
    public String level;
    public String is_open;

    public List<Region> children;

    @Override
    public String toString() {
        return region_name;
    }

    @Override
    public Region parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            region_id = jsonObject.optString("region_id");
            region_image = jsonObject.optString("region_image");
            parent_id = jsonObject.optString("parent_id");
            sort_order = jsonObject.optString("sort_order");
            region_name = jsonObject.optString("region_name");
            aliases_name = jsonObject.optString("aliases_name");
            level = jsonObject.optString("level");
            is_open = jsonObject.optString("is_open");
        }
        return this;
    }
}
