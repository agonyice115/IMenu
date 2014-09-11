package com.huassit.imenu.android.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Store extends BaseModel<Store> implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2114292098070326382L;
	public String id;
    public String name;
    public String logoName;
    public String logoLocation;
    public String logoDate;
    public String dynamicName;
    public String dynamicLocation;
    public String dynamicDate;
    public String followCount;
    public String viewCount;
    public String hours;
    public String tel1;
    public String tel2;
    public String per;
    public String star;
    public String distance;
    public String address;
    public String longitude;
    public String latitude;
    public String description;
    public String dynamicCount;
    public String dynamicCountType;
    public String vipLevel;
    public String signingType;
    public String couponType;
    public String categoryId;
    public ArrayList<Service> serviceList;
    public ArrayList<Environment> environmentList;
    public String followStatus;
    public String uploadMemberId;
    public String uplodaMemberName;
    public ArrayList<Recommand> recommandList;
    public ArrayList<Coupon> couponList;
    public ArrayList<Menu> menuList;

    @Override
    public Store parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            id = jsonObject.optString("store_id");
            name = jsonObject.optString("store_name");
            logoName = jsonObject.optString("store_logo_name");
            logoLocation = jsonObject.optString("store_logo_location");
            logoDate = jsonObject.optString("store_logo_date");
            dynamicName = jsonObject.optString("store_dynamic_name");
            dynamicLocation = jsonObject.optString("store_dynamic_location");
            dynamicDate = jsonObject.optString("store_dynamic_date");
            per = jsonObject.optString("per");
            star = jsonObject.optString("star");
            distance = jsonObject.optString("distance");
            followCount = jsonObject.optString("followered_count");
            viewCount = jsonObject.optString("view_count");
            hours = jsonObject.optString("hours");
            tel1 = jsonObject.optString("tel_1");
            tel2 = jsonObject.optString("tel_2");
            address = jsonObject.optString("address");
            longitude = jsonObject.optString("store_longitude_num");
            latitude = jsonObject.optString("store_latitude_num");
            description = jsonObject.optString("description");
            dynamicCount = jsonObject.optString("dynamic_count");
            dynamicCountType = jsonObject.optString("dynamic_count_type");
            vipLevel = jsonObject.optString("vip_level");
            signingType = jsonObject.optString("signing_type");
            couponType = jsonObject.optString("coupon_type");
            categoryId = jsonObject.optString("category_id");
        }
        return this;
    }
}
