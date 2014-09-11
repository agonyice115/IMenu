package com.huassit.imenu.android.model;

import org.json.JSONObject;

public class MenuUnit extends BaseModel<MenuUnit> {

    public String menu_unit_id;
    public String menu_unit_title;

    @Override
    public MenuUnit parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            menu_unit_id = jsonObject.optString("menu_unit_id");
            menu_unit_title = jsonObject.optString("menu_unit_title");
        }
        return this;
    }
}
