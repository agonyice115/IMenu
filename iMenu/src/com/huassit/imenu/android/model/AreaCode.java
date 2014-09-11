package com.huassit.imenu.android.model;

import org.json.JSONObject;

public class AreaCode extends BaseModel<AreaCode> {

    public String area_code_id;
    public String area_code_name;
    public String area_code_value;
    public String sort_order;
    public String defaultValue;
    public boolean isSelected;

    @Override
    public String toString() {
        return area_code_name;
    }

    @Override
    public boolean equals(Object o) {
        return area_code_id.equals(((AreaCode) o).area_code_id);
    }

    @Override
    public AreaCode parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            area_code_id = jsonObject.optString("area_code_id");
            area_code_name = jsonObject.optString("area_code_name");
            area_code_value = jsonObject.optString("area_code_value");
            sort_order = jsonObject.optString("sort_order");
            defaultValue = jsonObject.optString("default");
        }
        return this;
    }
}
