package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Menu;

/**
 * Created by Sylar on 14-6-28.
 */
public class MenuParser extends BaseParser<List<Menu>> {

    @Override
    public List<Menu> parseData(JSONObject jsonObject) throws JSONException {
        List<Menu> result = new ArrayList<Menu>();
        JSONObject data = jsonObject.optJSONObject("data");
        if (data != null) {
            JSONArray menuList = data.getJSONArray("menu_list");
            for (int i = 0; i < menuList.length(); i++) {
                result.add(new Menu().parse(menuList.getJSONObject(i)));
            }
        }
        return result;
    }
}
