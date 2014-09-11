package com.huassit.imenu.android.parser;

import org.json.JSONException;
import org.json.JSONObject;
import com.huassit.imenu.android.model.MemberMenu;

public class MenuImageParser extends BaseParser<MemberMenu> {

	@Override
	public MemberMenu parseData(JSONObject jsonObject) throws JSONException {
		MemberMenu icon = new MemberMenu().parse(jsonObject);
		return icon;
	}

}
