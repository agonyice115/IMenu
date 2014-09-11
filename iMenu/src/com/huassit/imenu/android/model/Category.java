package com.huassit.imenu.android.model;

import org.json.JSONObject;

import java.util.List;

public class Category extends BaseModel<Category> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String category_id;
    public String category_image;
    public String parent_id;
    public String sort_order;
    public String category_name;
    public String aliases_name;
    public String level;
    public List<Category> children;
    
    @Override
    public Category parse(JSONObject jsonObject) {
        if (jsonObject != null) {
            category_id = jsonObject.optString("category_id");
            category_image = jsonObject.optString("category_image");
            aliases_name = jsonObject.optString("aliases_name");
            category_name = jsonObject.optString("category_name");
            level = jsonObject.optString("level");
            parent_id = jsonObject.optString("parent_id");
            sort_order = jsonObject.optString("sort_order");
        }
        return this;
    }
}
