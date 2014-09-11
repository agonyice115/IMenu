package com.huassit.imenu.android.parser;

import com.huassit.imenu.android.model.MemberIcon;
import org.json.JSONException;
import org.json.JSONObject;

public class IconParser extends BaseParser<MemberIcon> {

    @Override
    public MemberIcon parseData(JSONObject jsonObject) throws JSONException {
        MemberIcon icon = new MemberIcon();
        JSONObject jsonData = jsonObject.optJSONObject("data");
        icon.memberId = jsonData.optString("memberId");
        icon.iconDate = jsonData.optString("iconDate");
        icon.iconLocation = jsonData.optString("iconLocation");
        icon.iconName = jsonData.optString("iconName");
        return icon;
    }

}
