package com.huassit.imenu.android.model;

import org.json.JSONObject;

public class MenuTaste extends BaseModel<MenuTaste> {

    public String menu_taste_id;
    public String menu_taste_image;
    public String sort_order;
    public String menu_taste_name;

    @Override
    public MenuTaste parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            menu_taste_id = jsonObject.optString("menu_taste_id");
            menu_taste_image = jsonObject.optString("menu_taste_image");
            menu_taste_name = jsonObject.optString("menu_taste_name");
            sort_order = jsonObject.optString("sort_order");
        }
        return this;
    }
}
