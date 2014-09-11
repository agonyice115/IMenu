package com.huassit.imenu.android.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.huassit.imenu.android.model.Member;
import com.huassit.imenu.android.model.MemberMenu;

public class MemberMenuParser extends BaseParser<ArrayList<MemberMenu>>{
	
	@Override
	public ArrayList<MemberMenu> parseData(JSONObject jsonObject) throws JSONException {
		ArrayList<MemberMenu> memberMenuList =null;
		if(!jsonObject.isNull("data")){
			memberMenuList =new ArrayList<MemberMenu>();
			JSONObject jsonData =jsonObject.optJSONObject("data");
			JSONArray jsonList =jsonData.optJSONArray("member_menu_list");
			for(int i =0;i<jsonList.length();i++){
				JSONObject json_object =jsonList.optJSONObject(i);
				MemberMenu memberMenu=new MemberMenu();
				memberMenu.member_menu_image_id =json_object.optString("member_menu_image_id");
				memberMenu.image_name =json_object.optString("image_name");
				memberMenu.image_location =json_object.optString("image_location");
				memberMenu.goods_count =json_object.optString("goods_count");
				memberMenu.comment_count =json_object.optString("comment_count");
				memberMenu.member_menu_image_date =json_object.optString("member_menu_imag_date");
				memberMenu.dynamic_id =json_object.optString("dynamic_id");
				JSONObject json_memberInfo=json_object.optJSONObject("member_info");
				Member member =new Member();
				member.memberId =json_memberInfo.optString("member_id");
				member.memberName =json_memberInfo.optString("member_name");
				member.iconName =json_memberInfo.optString("iconName");
				member.iconDate =json_memberInfo.optString("iconDate");
				member.iconLocation =json_memberInfo.optString("iconLocation");
				member.vipLevel =json_memberInfo.optString("vip_level");
				memberMenu.member_info =member;
				memberMenuList.add(memberMenu);
			}
		}
		return memberMenuList;
	}

}
