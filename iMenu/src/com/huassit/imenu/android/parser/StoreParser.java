package com.huassit.imenu.android.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.huassit.imenu.android.model.Coupon;
import com.huassit.imenu.android.model.Environment;
import com.huassit.imenu.android.model.Recommand;
import com.huassit.imenu.android.model.Service;
import com.huassit.imenu.android.model.Store;

public class StoreParser extends BaseParser<Store>{


	@Override
	public Store parseData(JSONObject jsonObject) throws JSONException {
		
		JSONObject jsonData =jsonObject.optJSONObject("data");
		Store store =new Store().parse(jsonData);
		
		JSONArray jsonServiceList=jsonData.optJSONArray("service_list");
		ArrayList<Service> serviceList =new  ArrayList<Service>();
		for(int i =0;i<jsonServiceList.length();i++){
			JSONObject jsonService =jsonServiceList.optJSONObject(i);
			Service service =new Service();
			service.serviceId =jsonService.optString("service_id");
			service.serviceName =jsonService.optString("service_name");
			serviceList.add(service);
		}
		store.serviceList =serviceList;
		
		JSONArray jsonEnviromentList=jsonData.optJSONArray("environment_list");
		ArrayList<Environment> enviromentList =new  ArrayList<Environment>();
		for(int i =0;i<jsonEnviromentList.length();i++){
			JSONObject jsonEnviroment =jsonEnviromentList.optJSONObject(i);
			Environment enviroment =new Environment();
			enviroment.environment_id =jsonEnviroment.optString("environment_id");
			enviroment.environment_name =jsonEnviroment.optString("environment_name");
			enviromentList.add(enviroment);
		}
		store.environmentList =enviromentList;
		
		store.followStatus =jsonData.optString("follow_status");
		store.uploadMemberId =jsonData.optString("upload_member_id");
		store.uplodaMemberName =jsonData.optString("upload_member_name");
		
		if(jsonData.optJSONArray("recommend_list")!=null){
			JSONArray jsonRecommandList =jsonData.optJSONArray("recommend_list");
			ArrayList<Recommand> recommandList =new ArrayList<Recommand>();
			for(int i =0;i<jsonRecommandList.length();i++){
				JSONObject jsonRecommand =jsonRecommandList.optJSONObject(i);
				Recommand recommand =new Recommand();
				recommand.recommandPeople =jsonRecommand.optString("recommend_people");
				recommand.recommandCount =jsonRecommand.optString("recommend_count");
				recommandList.add(recommand);
			}
			store.recommandList =recommandList;
		}
		
		
		if(jsonData.optJSONArray("coupon_list")!=null){
			JSONArray jsonCouponList =jsonData.optJSONArray("coupon_list");
			ArrayList<Coupon> couponList =new ArrayList<Coupon>();
			for(int i =0;i<jsonCouponList.length();i++){
				JSONObject jsonCoupon =jsonCouponList.optJSONObject(i);
				Coupon coupon =new Coupon();
				coupon.couponId =jsonCoupon.optString("coupon_id");
				coupon.couponTitle =jsonCoupon.optString("coupon_title");
				couponList.add(coupon);
			}
			store.couponList =couponList;
		}
		
		
		return store;
	}

}
