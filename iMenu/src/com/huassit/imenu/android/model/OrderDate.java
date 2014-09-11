package com.huassit.imenu.android.model;

import java.util.List;

/**
 * 
 * @author Administrator
 * 
 */
public class OrderDate {

	/** 订单时间显示 */
	public String key;
	/** 有几条订单 */
	public String value;
	/** 此时间内的list集合 */
	public List<OrderInfo> mOrderLists;
}
