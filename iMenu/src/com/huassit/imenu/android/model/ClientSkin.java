package com.huassit.imenu.android.model;

import org.json.JSONObject;

public class ClientSkin extends BaseModel<ClientSkin> {

    public String client_skin_id;
    public String client_skin_name;
    public String client_skin_value;
    public String sort_order;
    public String sex;
    public String defaultValue;
    public boolean isSelected;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientSkin)) return false;

        ClientSkin skin = (ClientSkin) o;

        if (client_skin_id != null ? !client_skin_id.equals(skin.client_skin_id) : skin.client_skin_id != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return client_skin_id != null ? client_skin_id.hashCode() : 0;
    }

    @Override
    public ClientSkin parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            client_skin_id = jsonObject.optString("client_skin_id");
            client_skin_name = jsonObject.optString("client_skin_name");
            client_skin_value = jsonObject.optString("client_skin_value");
            sex = jsonObject.optString("sec");
            defaultValue = jsonObject.optString("default");
        }
        return this;
    }
}
