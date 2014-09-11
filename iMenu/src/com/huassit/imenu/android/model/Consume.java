package com.huassit.imenu.android.model;

import org.json.JSONObject;

/**
 * 消费实体类
 * 
 * @author shang_guan
 * 
 */
public class Consume extends BaseModel{
	/** 过期时间 */
	public String exprie_date;
	/** 消费码 */
	public String consume_code;
	
	public Consume parse(JSONObject jsonObject){
		if(jsonObject!=null){
			exprie_date =jsonObject.optString("exprie_date");
			consume_code =jsonObject.optString("consume_code");
		}
		return this;
		
	}
}
