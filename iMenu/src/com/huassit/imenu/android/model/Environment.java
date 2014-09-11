package com.huassit.imenu.android.model;

import java.io.Serializable;

import org.json.JSONObject;

public class Environment extends BaseModel<Environment> implements Serializable{

    public String environment_id;
    public String environment_image;
    public String thumb_image;
    public String sort_order;
    public String environment_name;

    @Override
    public Environment parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            environment_id = jsonObject.optString("environment_id");
            environment_image = jsonObject.optString("environment_image");
            thumb_image = jsonObject.optString("thumb_image");
            sort_order = jsonObject.optString("sort_order");
            environment_name = jsonObject.optString("environment_name");
        }
        return this;
    }
}
