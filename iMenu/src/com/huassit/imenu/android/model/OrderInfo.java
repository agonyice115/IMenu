package com.huassit.imenu.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 订单内容实体类
 * 
 * @author shang_guan
 * 
 */
public class OrderInfo extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4093117548920084717L;
	/** 订单ID */
	public String order_id;
	/** 订单号 */
	public String order_no;
	/** 订单日期create_date */
	public String order_date;
	/** 商家名称 */
	public String store_name;
	/** 人数 */
	public String people;
	/** 总价 */
	public String total;
	/** 菜单实体类 */
	public Menu menuInfo;
	/** 商家实体类 */
	public Store mStoreInfo;
	/**
	 * 订单类型10-待付款 ,20-已支付 ,21-已支付未消费, 22-已支付已消费 ,30-退款 ,31-退款中, 32-已退款 ,40-到店支付,
	 *  41- 到店支付未消费, 42-到店支付已消费 ,50-过期, 51-代付款过期, 52-已付款过期
	 */
	public String order_type;
	/** 动态信息 */
	public Dynamic mDynamicInfo;
	/** 消费信息 */
	public Consume mConsumeInfo;
	/** 单品种类数量 */
	public String menu_type_count;
	/** 积分信息 */
	public ScoreInfo mScoreInfo;
	/** 过期时间 */
	public String exprie_date;
	/**退款详情相关*/
	public ReturnInfo returnInfo;

	/**
	 * 原价
	 */
	public String originalTotal;
	public ArrayList<Menu> menuList = new ArrayList<Menu>();
	public Consume consumeInfo;
	public ArrayList<Category> categoryList = new ArrayList<Category>();;
	public String memberId;
	public String dynamicId;
	public ScoreInfo scoreInfo;

	@Override
	public OrderInfo parse(JSONObject jsonObject) {
		if (jsonObject != null) {
			order_id = jsonObject.optString("order_id");
			order_no = jsonObject.optString("order_no");
			mConsumeInfo = new Consume().parse(jsonObject
					.optJSONObject("consume_info"));
			order_date = jsonObject.optString("create_date");
			JSONObject jsonStore = new JSONObject();
			jsonStore = jsonObject.optJSONObject("store_info");
			mStoreInfo = new Store().parse(jsonStore);
			people = jsonObject.optString("people");
			total = jsonObject.optString("total");
			originalTotal = jsonObject.optString("original_total");
			JSONArray jsonMenuList = jsonObject.optJSONArray("menu_list");
			for (int i = 0; i < jsonMenuList.length(); i++) {
				Menu menu = new Menu().parse(jsonMenuList.optJSONObject(i));
				menuList.add(menu);
			}
			order_type = jsonObject.optString("order_type");
			exprie_date = jsonObject.optString("exprie_date");
			JSONObject jsonConsume = jsonObject.optJSONObject("consume_info");
			consumeInfo = new Consume().parse(jsonConsume);
//			mDynamicInfo=new Dynamic().parse()
			JSONArray jsonCategoryList = jsonObject
					.optJSONArray("menu_category_list");
			if (null != jsonCategoryList && jsonCategoryList.length() > 0) {
				for (int i = 0; i < jsonCategoryList.length(); i++) {
					Category category = new Category().parse(jsonCategoryList
							.optJSONObject(i));
					categoryList.add(category);
				}
			}
			menu_type_count = jsonObject.optString("menu_type_count");
		}
		return this;
	}

}
