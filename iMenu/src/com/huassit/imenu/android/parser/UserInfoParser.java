package com.huassit.imenu.android.parser;

import com.huassit.imenu.android.model.Member;
import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoParser extends BaseParser<Member> {

    @Override
    public Member parseData(JSONObject jsonObject) throws JSONException {
        Member user = new Member();
        if (!jsonObject.isNull("data")) {
            JSONObject json_user = jsonObject.getJSONObject("data");

            user.setMemberId(json_user.optString("memberId"));
            user.setMemberName(json_user.optString("memberName"));
            user.setEmail(json_user.optString("email"));
            user.setAreaCode(json_user.optString("areaCode"));
            user.setMobile(json_user.optString("mobile"));
            user.setIconName(json_user.optString("iconName"));
            user.setIconLocation(json_user.optString("iconLocation"));
            user.setIconDate(json_user.optString("iconDate"));
            user.setDynamicName(json_user.optString("dynamicName"));
            user.setDynamicLocation(json_user.optString("dynamicLocation"));
            user.setDynamicDate(json_user.optString("dynamicDate"));
            user.setRegionId(json_user.optString("regionId"));
            user.setStatus(json_user.optString("status"));
            user.setToken(json_user.optString("token"));
            user.setRealname(json_user.optString("realname"));
            user.setBirthday(json_user.optString("birthday"));
            user.setSex(json_user.optString("sex"));
            user.setSignature(json_user.optString("signature"));
            user.setPassWord(json_user.optString("password"));
            return user;
        } else {
            return null;
        }

    }

}
