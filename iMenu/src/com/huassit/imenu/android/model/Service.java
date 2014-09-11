package com.huassit.imenu.android.model;

import java.io.Serializable;

import org.json.JSONObject;

public class Service extends BaseModel<Service> implements Serializable{
    public String serviceId;
    public String serviceName;
    public String service_image;
    public String thumb_image;
    public String sort_order;

    @Override
    public Service parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            serviceId = jsonObject.optString("service_id");
            serviceName = jsonObject.optString("service_name");
            service_image = jsonObject.optString("service_image");
            thumb_image = jsonObject.optString("thumb_image");
            sort_order = jsonObject.optString("sort_order");
        }
        return this;
    }
}
