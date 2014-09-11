package com.huassit.imenu.android.model;

import java.util.List;

import org.json.JSONObject;

/**
 * 双列表菜单实体类
 * */
@SuppressWarnings("rawtypes")
public class MenuAndCategory extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5970405588282446871L;
	/** 菜单 */
	public List<Menu> menu_list;
	/** 菜单分类 */
	public List<MenuCategory> menu_category_list;
	/** 商家 */
	public Store mStore;

	@Override
	public MenuAndCategory parse(JSONObject jsonObject) {

		return this;
	}

}
