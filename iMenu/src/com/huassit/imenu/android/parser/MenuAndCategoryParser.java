package com.huassit.imenu.android.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.MenuAndCategory;
import com.huassit.imenu.android.model.MenuCategory;
import com.huassit.imenu.android.model.Store;

public class MenuAndCategoryParser extends BaseParser<MenuAndCategory> {

	@Override
	public MenuAndCategory parseData(JSONObject jsonObject)
			throws JSONException {
		MenuAndCategory menuAndCategory = new MenuAndCategory();
		menuAndCategory.menu_list = new ArrayList<Menu>();
		menuAndCategory.menu_category_list = new ArrayList<MenuCategory>();
		if (!jsonObject.isNull("data")) {
			JSONArray json_data = jsonObject.optJSONArray("data");
			for (int k = 0; k < json_data.length(); k++) {
				JSONObject json_menuObjectList = json_data.optJSONObject(k);
				JSONArray json_menuList = json_menuObjectList
						.optJSONArray("menu_list");
				for (int i = 0; i < json_menuList.length(); i++) {
					JSONObject json_menu = json_menuList.optJSONObject(i);
					Menu menu = new Menu().parse(json_menu);
					menuAndCategory.menu_list.add(menu);
				}
				JSONArray json_menu_categoryList = json_menuObjectList
						.optJSONArray("menu_category_list");
				for (int i = 0; i < json_menu_categoryList.length(); i++) {
					JSONObject json_menu_category = json_menu_categoryList
							.optJSONObject(i);
					MenuCategory category = new MenuCategory();
					category.setMenu_category_id(json_menu_category
							.optString("menu_category_id"));
					category.setMenu_category_name(json_menu_category
							.optString("menu_category_name"));
					category.setParent_id(json_menu_category
							.optString("parent_id"));
					category.setMenu_category_image_name(json_menu_category
							.optString("menu_category_image_name"));
					category.setMenu_category_image_location(json_menu_category
							.optString("menu_category_image_location"));
					category.setMenu_category_image_date(json_menu_category
							.optString("menu_category_image_date"));
					category.setSort_order(json_menu_category
							.optString("sort_order"));
					category.setMenu_ids(json_menu_category
							.optString("menu_ids"));
					menuAndCategory.menu_category_list.add(category);
				}
				JSONObject store_info = json_menuObjectList
						.optJSONObject("store_info");
				menuAndCategory.mStore = new Store().parse(store_info);
			}
		}
		return menuAndCategory;
	}
}
