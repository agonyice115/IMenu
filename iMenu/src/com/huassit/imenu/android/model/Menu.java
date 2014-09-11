package com.huassit.imenu.android.model;

import com.huassit.imenu.android.util.NumberFormatUtils;
import com.huassit.imenu.android.util.StringUtils;

import org.json.JSONObject;

import java.io.Serializable;

public class Menu extends BaseModel<Menu> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int menu_count;
    public String menu_count_type;
    public String menu_taste_ids;
    public String menu_unit_id;
    public String upload_member_id;
    public String upload_member_name;
    public String menu_id;
    public String menu_name;
    public String menu_price;
    public String menu_image_name;
    public String menu_image_location;
    public String menu_image_date;
    public String sort_order;
    public String menu_coupon_price;
    public boolean isOrdered;

    // public int orderNumber;

    public Menu parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            menu_id = jsonObject.optString("menu_id");
            menu_name = jsonObject.optString("menu_name");
            Float price = Float.parseFloat(jsonObject.optString("menu_price"));
            menu_price = NumberFormatUtils.format(price);
            if (jsonObject.has("menu_image_name")) {
                menu_image_name = jsonObject.optString("menu_image_name");
            } else {
                menu_image_name = jsonObject.optString("image_name");
            }
            if (jsonObject.has("menu_image_location")) {
                menu_image_location = jsonObject.optString("menu_image_location");
            } else {
                menu_image_location = jsonObject.optString("image_location");
            }

            if (jsonObject.has("menu_image_date")) {
                menu_image_date = jsonObject.optString("menu_image_date");
            } else {
                menu_image_date = jsonObject.optString("image_date");
            }
            menu_count = jsonObject.optInt("menu_count");
            menu_count_type = jsonObject.optString("menu_count_type");
            sort_order = jsonObject.optString("sort_order");
            menu_taste_ids = jsonObject.optString("menu_taste_ids");
            menu_unit_id = jsonObject.optString("menu_unit_id");
            upload_member_id = jsonObject.optString("upload_member_id");
            upload_member_name = jsonObject.optString("upload_member_name");
            if(!StringUtils.isBlank(jsonObject.optString("menu_coupon_price"))){
            	Float coupon_price = Float.parseFloat(jsonObject.optString("menu_coupon_price"));
            	menu_coupon_price = NumberFormatUtils.format(coupon_price);
            }
        }
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((menu_id == null) ? 0 : menu_id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Menu other = (Menu) obj;
        if (menu_id == null) {
            if (other.menu_id != null)
                return false;
        } else if (!menu_id.equals(other.menu_id))
            return false;
        return true;
    }

}
