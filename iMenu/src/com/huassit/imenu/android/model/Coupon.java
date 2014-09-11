package com.huassit.imenu.android.model;

import java.io.Serializable;

import org.json.JSONObject;

public class Coupon implements Serializable{

	public String couponId;
	public String couponTitle;
	
	public Coupon parseCoupon(JSONObject jsonObject){
		if(jsonObject!=null){
			couponId =jsonObject.optString("coupon_id");
			couponTitle=jsonObject.optString("coupon_title");
		}
		return this;
	}
}
