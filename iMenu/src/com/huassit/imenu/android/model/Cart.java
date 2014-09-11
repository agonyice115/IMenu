package com.huassit.imenu.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.util.TimeUtil;

/**
 * Created by Sylar on 14-7-10.
 */
public class Cart implements Serializable {
	public String memberId;
	public String createDate;
	public String modifyDate;
	public Store storeInfo;
	public String people;
	// 优惠前总价
	public String total;
	// 优惠后总价
	public String originalTotal;
	public ArrayList<Menu> menuList = new ArrayList<Menu>();
	public ArrayList<Coupon> couponList = new ArrayList<Coupon>();
	public ArrayList<Category> menuCategoryList = new ArrayList<Category>();
	/**
	 * 1有，0无
	 */
	public int hasCart;

	public Cart parserCart(JSONObject jsonObject) throws JSONException {

		if (jsonObject != null) {
			hasCart = jsonObject.optInt("has_cart");

			memberId = jsonObject.optString("member_id");
			if (!"".equals(jsonObject.optString("create_date"))) {
				createDate = TimeUtil.getTime(
						jsonObject.optString("create_date"),
						"yyyy-MM-dd HH:mm:ss") / 1000 + "";
			}
			if (!"".equals(jsonObject.optString("modify_date"))) {
				modifyDate = TimeUtil.getTime(
						jsonObject.optString("modify_date"),
						"yyyy-MM-dd HH:mm:ss") / 1000 + "";
			}
			storeInfo = new Store().parse(jsonObject
					.optJSONObject("store_info"));

			people = jsonObject.optString("people");
			total = jsonObject.optString("total");
			originalTotal = jsonObject.optString("origina_total");
			if (jsonObject.optJSONArray("menu_list") != null) {
				JSONArray jsonMenuList = jsonObject.getJSONArray("menu_list");
				for (int i = 0; i < jsonMenuList.length(); i++) {
					Menu menu = new Menu().parse(jsonMenuList.optJSONObject(i));
					menuList.add(menu);
				}
			}
			if (jsonObject.optJSONObject("store_info") != null
					&& jsonObject.optJSONObject("store_info").optJSONArray(
							"coupon_list") != null) {
				JSONArray jsonCouponList = jsonObject.optJSONObject(
						"store_info").getJSONArray("coupon_list");
				for (int i = 0; i < jsonCouponList.length(); i++) {
					Coupon coupon = new Coupon().parseCoupon(jsonCouponList
							.optJSONObject(i));
					couponList.add(coupon);
				}
			}
			if (jsonObject.optJSONArray("menu_category_list") != null) {
				JSONArray jsonCategoryList = jsonObject
						.getJSONArray("menu_category_list");
				for (int i = 0; i < jsonCategoryList.length(); i++) {
					Category category = new Category().parse(jsonCategoryList
							.optJSONObject(i));
					menuCategoryList.add(category);
				}
			}
		}
		return this;
	}
}
