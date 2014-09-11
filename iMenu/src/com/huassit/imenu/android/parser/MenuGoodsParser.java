package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.huassit.imenu.android.model.Goods;
import com.huassit.imenu.android.model.MemberGoods;

public class MenuGoodsParser extends BaseParser<Map<String ,Object>>{

	@Override
	public Map<String ,Object> parseData(JSONObject jsonObject) throws JSONException {
		Map<String,Object> map =null;
		ArrayList<Goods> goodsList =null;
		if(!jsonObject.isNull("data")){
			map =new HashMap<String, Object>();
			goodsList =new ArrayList<Goods>();
			JSONArray jsonData =jsonObject.optJSONArray("data");
			for(int i=0;i<jsonData.length();i++){
				JSONObject object =jsonData.optJSONObject(i);
				String goodsCount =object.optString("goods_count");
				map.put("goodsCount", goodsCount);
				
				JSONArray jsonGoodsList =object.optJSONArray("goods_list");
				for(int j =0;j<jsonGoodsList.length();j++){
					JSONObject goodsObject =jsonGoodsList.optJSONObject(j);
					Goods goods =new Goods();
					goods.member_menu_image_goods_id =goodsObject.optString("member_menu_image_goods_id");
					goods.member_id =goodsObject.optString("member_id");
					goods.goods_date =goodsObject.optString("goods_date");
					goods.member_name =goodsObject.optString("member_name");
					goods.icon_name =goodsObject.optString("iconName");
					goods.icon_location =goodsObject.optString("iconLocation");
					goods.icon_date =goodsObject.optString("iconDate");
					goodsList.add(goods);
				}
				map.put("goodsList", goodsList);
				
				JSONObject jsonMemberGoods =object.optJSONObject("member_goods");
				MemberGoods memberGoods =new MemberGoods();
				memberGoods.member_menu_image_goods_id =jsonMemberGoods.optString("member_menu_image_goods_id");
				memberGoods.goods_status =jsonMemberGoods.optString("goods_status");
				map.put("memberGoods", memberGoods);
			}
		}
		return map;
	}

}
