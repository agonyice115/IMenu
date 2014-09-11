package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.Store;

public class MenuAndStoreParser extends BaseParser<Map<String,Object>>{

	@Override
	public Map<String,Object> parseData(JSONObject jsonObject)
			throws JSONException {
		Map<String,Object> menuObjectMap=new HashMap<String,Object>();
		if(!jsonObject.isNull("data")){
			JSONArray jsonData =jsonObject.optJSONArray("data");
			for(int i=0;i<jsonData.length();i++){
				JSONObject jsonDataObject =jsonData.optJSONObject(i);
				JSONObject json_menu =jsonDataObject.optJSONObject("menu_info");
				Menu menu =new Menu();
				menu.menu_id =json_menu.optString("menu_id");
				menu.menu_name =json_menu.optString("menu_name");
				menu.menu_price =json_menu.optString("menu_price");
				menu.menu_coupon_price=json_menu.optString("menu_coupon_price");
				menu.menu_image_name =json_menu.optString("menu_image_name");
				menu.menu_image_location =json_menu.optString("menu_image_location");
				menu.menu_image_date =json_menu.optString("menu_image_date");
				menu.menu_count =json_menu.optInt("menu_count");
				menu.menu_count_type =json_menu.optString("menu_count_type");
				menu.sort_order =json_menu.optString("sort_order");
				menu.menu_taste_ids =json_menu.optString("menu_taste_ids");
				menu.menu_unit_id =json_menu.optString("menu_unit_id");
				menu.upload_member_id =json_menu.optString("upload_member_id");
				menu.upload_member_name =json_menu.optString("upload_member_name");
				menuObjectMap.put("menu",menu);
				
				JSONObject json_store =jsonDataObject.optJSONObject("store_info");
				Store store =new Store().parse(json_store);
				store.id =json_store.optString("store_id");
				store.name =json_store.optString("store_name");
				store.logoName =json_store.optString("store_logo_name");
				store.logoLocation =json_store.optString("store_logo_location");
				store.logoDate =json_store.optString("store_logo_date");
				store.address =json_store.optString("address");
				store.vipLevel =json_store.optString("vip_level");
				menuObjectMap.put("store",store);
			}
		}
		return menuObjectMap;
	}

	
}
