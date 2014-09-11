package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Consume;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.Menu;
import com.huassit.imenu.android.model.OrderDate;
import com.huassit.imenu.android.model.OrderInfo;
import com.huassit.imenu.android.model.OrderList;
import com.huassit.imenu.android.model.Store;
import com.huassit.imenu.android.util.TimeUtil;

/**
 * 订单列表解析
 * 
 * @author shang_guan
 * 
 */
public class OrderListParser extends BaseParser<Map<String, Object>> {
	/**
	 * 存储筛选后的子项集合
	 */
	private ArrayList<OrderInfo> newOrderLists;
	/**
	 * 种类为年份
	 */
	private final static int YEAR = 1;
	/**
	 * 种类为月份
	 */
	private final static int MONTH = 2;
	/**
	 * 种类为周
	 */
	private final static int WEEK = 3;
	/**
	 * 请求的时间分类
	 */
	private int TimeType;
	/**
	 * 订单信息集合, 获取服务器传来的子项集合
	 * */
	private List<OrderInfo> mOrderList;
	/** 订单实体类 */
	private OrderInfo info;

	public OrderListParser(int TimeType) {
		this.TimeType = TimeType;
	}

	@Override
	public Map<String, Object> parseData(JSONObject jsonObject)
			throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray jsonDataArray = jsonObject.optJSONArray("data");
		for (int i = 0; i < jsonDataArray.length(); i++) {
			JSONObject object = (JSONObject) jsonDataArray.get(i);
			/** 时间的key-value */
			JSONArray filter_date_list = object
					.optJSONArray("filter_date_list");
			/** 订单信息集合 */
			JSONArray order_list = object.optJSONArray("order_list");
			/** 个人信息 */
			JSONObject member_info = object.optJSONObject("member_info");
			/** 总消费信息 */
			JSONObject total_order_info = object
					.optJSONObject("total_order_info");
			/***************************************************************************************************************/
			/** 个人信息数据 */
			if (member_info != null) {
				Member viewMember = new Member();
				viewMember.memberId = member_info.optString("member_id");
				viewMember.memberName = member_info.optString("member_name");
				viewMember.iconName = member_info.optString("icon_name");
				viewMember.iconLocation = member_info
						.optString("icon_location");
				viewMember.iconDate = member_info.optString("icon_date");
				viewMember.sex = member_info.optString("sex");
				viewMember.vipLevel = member_info.optString("vip_level");
				viewMember.signature = member_info.optString("signature");
				map.put("member_info", viewMember);
			}
			/** 总消费信息 */
			if (total_order_info != null) {
				OrderList order = new OrderList();
				order.total_menu = total_order_info.optString("total_menu");
				order.total_order = total_order_info.optString("total_order");
				order.total_order_year = total_order_info
						.optString("total_order_year");
				order.total_store = total_order_info.optString("total_store");
				order.total_order_count = total_order_info
						.optString("total_order_count");
				map.put("total_order_info", order);
			}
			/** 订单信息 */
			mOrderList = new ArrayList<OrderInfo>();
			if (order_list != null && order_list.length() > 0) {
				for (int j = 0; j < order_list.length(); j++) {
					JSONObject orderInfo = order_list.optJSONObject(j);
					info = new OrderInfo();
					info.order_id = orderInfo.optString("order_id");
					info.order_date = orderInfo.optString("order_date");
					info.store_name = orderInfo.optString("store_name");
					info.people = orderInfo.optString("people");
					info.total = orderInfo.optString("total");
					info.order_type = orderInfo.optString("order_type");
					info.exprie_date=orderInfo.optString("exprie_date");
					/** 菜品信息 */
					JSONObject menu_info = orderInfo.optJSONObject("menu_info");
					if (menu_info != null) {
						Menu menu = new Menu();
						menu.menu_id = menu_info.optString("menu_id");
						menu.menu_name = menu_info.optString("menu_name");
						menu.menu_image_name = menu_info
								.optString("image_name");
						menu.menu_image_location = menu_info
								.optString("image_location");
						info.menuInfo=menu;
					}
					/** 商家信息 */
					JSONObject store_info = orderInfo
							.optJSONObject("store_info");
					if (store_info != null) {
						Store mStore = new Store();
						mStore.id = store_info.optString("store_id");
						mStore.name = store_info.optString("store_name");
						mStore.logoName = store_info
								.optString("store_logo_name");
						mStore.logoLocation = store_info
								.optString("store_logo_location");
						mStore.logoDate = store_info
								.optString("store_logo_date");
						mStore.vipLevel = store_info.optString("vip_level");
						info.mStoreInfo=mStore;
					}
					/** 动态信息 */
					JSONObject dynamic_info = orderInfo
							.optJSONObject("dynamic_info");
					if (dynamic_info != null) {
						Dynamic mDynamic = new Dynamic();
						mDynamic.setDynamicType(dynamic_info
								.optString("dynamic_type"));
						mDynamic.setDynamicId(dynamic_info
								.optString("dynamic_id"));
						info.mDynamicInfo=mDynamic;
					}
					/** 消费信息 */
					JSONObject consume_info = orderInfo
							.optJSONObject("consume_info");
					if (consume_info != null) {
						Consume mConsume = new Consume();
						mConsume.exprie_date = consume_info
								.optString("exprie_date");
						mConsume.consume_code = consume_info
								.optString("consume_code");
						info.mConsumeInfo=mConsume;
					}
					mOrderList.add(info);
				}
			}
			/** 父项的数据及其对应的子项集合 */
			ArrayList<OrderDate> mOrderDates = new ArrayList<OrderDate>();
			for (int j = 0; j < filter_date_list.length(); j++) {
				newOrderLists = new ArrayList<OrderInfo>();
				JSONObject filterDateObject = filter_date_list.optJSONObject(j);
				OrderDate orderDate = new OrderDate();
				/** 获取每个父项的时间和条目数 */
				String key = filterDateObject.optString("key").replace("-", "");
				String value = filterDateObject.optString("value");
				orderDate.key = key;
				orderDate.value = value;
				orderDate.mOrderLists = mOrderList;
				if (TimeType == MONTH) {
					for (int k = 0; k < mOrderList.size(); k++) {
						String dynamicDate = TimeUtil.itemMonthDate(mOrderList
								.get(k).order_date, MONTH);
						if (dynamicDate.equals(key)) {
							newOrderLists.add(mOrderList.get(k));
						}
					}
					orderDate.mOrderLists = newOrderLists;
				}
				mOrderDates.add(orderDate);
			}
			map.put("filter_date_list", mOrderDates);
		}
		return map;
	}
}
