package com.huassit.imenu.android.parser;

import android.content.Context;
import android.widget.Toast;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.Service;
import com.huassit.imenu.android.model.Store;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreListParser extends BaseParser<ArrayList<Store>> {

    @Override
    public ArrayList<Store> parseData(JSONObject jsonObject) throws JSONException {
        ArrayList<Store> storeList = null;
        ArrayList<Service> serviceList = null;
        
        if (!jsonObject.isNull("data")) {
            storeList = new ArrayList<Store>();
            JSONObject json_data = jsonObject.optJSONObject("data");
            JSONArray json_storeList = json_data.optJSONArray("store_list");
            if (json_storeList != null) {
                for (int i = 0; i < json_storeList.length(); i++) {
                    JSONObject json_store = json_storeList.optJSONObject(i);
                    Store store = new Store();
                    store.id = json_store.optString("store_id");
                    store.name = json_store.optString("store_name");
                    store.logoName = json_store.optString("store_logo_name");
                    store.logoLocation = json_store.optString("store_logo_location");
                    store.logoDate = json_store.optString("store_logo_date");
                    store.vipLevel = json_store.optString("vip_level");
                    store.address = json_store.optString("address");
                    JSONArray json_serviceList = json_store.optJSONArray("service_list");
                    if (json_serviceList != null) {
                    	serviceList =new ArrayList<Service>();
                        for (int j = 0; j < json_serviceList.length(); j++) {
                            Service service = new Service();
                            JSONObject json_service = json_serviceList.optJSONObject(j);
                            service.serviceId = json_service.optString("service_id");
                            service.serviceName = json_service.optString("service_name");
                            serviceList.add(service);
                        }
                    }
                    store.serviceList = serviceList;
                    store.per = json_store.optString("per");
                    store.star = json_store.optString("star");
                    store.distance = json_store.optString("distance");
                    store.signingType =json_store.optString("signing_type");
                    store.couponType =json_store.optString("coupon_type");
                    JSONArray json_menuList = json_store.optJSONArray("menu_list");
                    if (json_menuList != null) {
                    	ArrayList<Menu> menuList = new ArrayList<Menu>();
                        for (int k = 0; k < json_menuList.length(); k++) {
                            Menu menu = new Menu();
                            JSONObject json_menu = json_menuList.optJSONObject(k);
                            menu.menu_id = json_menu.optString("menu_id");
                            menu.menu_name = json_menu.optString("menu_name");
                            menu.menu_price = json_menu.optString("menu_price");
                            menu.menu_image_name = json_menu.optString("menu_image_name");
                            menu.menu_image_location = json_menu.optString("menu_image_location");
                            menu.menu_image_date = json_menu.optString("menu_image_date");
                            menu.sort_order = json_menu.optString("sort_order");
                            menu.menu_coupon_price =json_menu.optString("menu_coupon_price");
                            menuList.add(menu);
                        }
                        store.menuList = menuList;
                    }
                    store.longitude = json_store.optString("store_longitude_num");
                    store.latitude = json_store.optString("store_latitude_num");
                    storeList.add(store);
                }
            }

        } 
        return storeList;
    }

}
