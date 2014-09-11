package com.huassit.imenu.android.model;

import org.json.JSONObject;

/**
 * Created by Sylar on 14-7-10.
 */
public class Order {
	/**
	 * 订单数量
	 */
	public int orderCount;
	/**
	 * 待付款订单数量
	 */
	public int orderPendingCount;
	/**
	 * 已付款总订单数
	 */
	public int orderPaymentCount;
	/**
	 * 到店支付未消费数量
	 */
	public int orderNonConsumeCount;
	/**
	 * 到店支付已消费数量
	 */
	public int orderConsumeCount;
	/** 已支付未消费订单数量 */
	public String order_non_consume_count;
	/** 已支付已消费订单数量 */
	public String order_consume_count;

	public Order parserOrder(JSONObject orderJson) {
		if (orderJson != null) {
			orderCount = orderJson.optInt("order_count");
			orderPendingCount = orderJson.optInt("order_pending_count");
			orderPaymentCount = orderJson.optInt("order_paymen_count");
			orderNonConsumeCount = orderJson
					.optInt("order_cod_non_consume_count");
			orderConsumeCount = orderJson.optInt("order_cod_consume_count");
			order_non_consume_count = orderJson.optString("order_non_consume_count");
			order_consume_count = orderJson.optString("order_consume_count");
		}
		return this;
	}
}
