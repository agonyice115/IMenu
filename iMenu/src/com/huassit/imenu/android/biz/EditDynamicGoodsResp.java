package com.huassit.imenu.android.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.huassit.imenu.android.R;
import com.huassit.imenu.android.http.BaseInvoker;
import com.huassit.imenu.android.model.API;
import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.Goods;
import com.huassit.imenu.android.model.MemberMenu;
import com.huassit.imenu.android.parser.BaseParser;
import com.huassit.imenu.android.parser.DefaultParser;

public class EditDynamicGoodsResp extends BaseInvoker{

	private Dynamic dynamic;
	private Goods goods;
	private String token;
	private String goodsType;
	
	public EditDynamicGoodsResp(Activity ctx, Handler handler,Dynamic dynamic,Goods goods,String token,String goodsType) {
		super(ctx, handler);
		this.dynamic =dynamic;
		this.goods =goods;
		this.token =token;
		this.goodsType =goodsType;
	}

	@Override
	public List<NameValuePair> getParameters() {
		List<NameValuePair> nvp =new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("route", API.EDIT_DYNAMIC_GOODS));
		nvp.add(new BasicNameValuePair("token", token));
		nvp.add(new BasicNameValuePair("jsonText", makeJsonText()));
		return nvp;
	}

	private String makeJsonText() {
		JSONObject json =new JSONObject();
		try {
			json.put("member_id", goods.member_id);
			json.put("dynamic_id",dynamic.getDynamicId());
			json.put("goods_type", goodsType);
			json.put("dynamic_goods_id", goods.dynamic_goods_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	@Override
	public String getRequestUrl() {
		return API.server;
	}

	@Override
	public HTTP_METHOD getRequestMethod() {
		return HTTP_METHOD.METHOD_POST;
	}

	@Override
	protected void handleResponse(String response) {
		DefaultParser parser =new DefaultParser();
		try {
			parser.setKey("dynamic_goods_id");
			String dynamic_goods_id =(String) parser.doParse(response);
			 if (parser.getResponseCode() == BaseParser.SUCCESS) {
	                if (dynamic_goods_id != null) {
	                    sendMessage(ON_SUCCESS, dynamic_goods_id);
	                } else {
	                    sendMessage(ON_FAILURE, ctx.getString(R.string.data_error));
	                }
	            } else {
	                sendMessage(ON_FAILURE, parser.getResponseMessage());
	            }
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
