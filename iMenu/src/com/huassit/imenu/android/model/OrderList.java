package com.huassit.imenu.android.model;

import java.util.List;

/**
 * 订单列表实体类
 * 
 * @author shang_guan
 * 
 */
public class OrderList {
	/** 总花费 */
	public String total_order;
	/** 本年度花费 */
	public String total_order_year;
	/** 总商家数量 */
	public String total_store;
	/** 总菜单数量 */
	public String total_menu;
	/** 订单总数量 */
	public String total_order_count;
	/** 个人信息 */
	public Member memberInfo;
	/** 订单信息 */
	public List<OrderInfo> mOrderList;
}
