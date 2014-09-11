package com.huassit.imenu.android.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huassit.imenu.android.model.Dynamic;
import com.huassit.imenu.android.model.Member;

/**
 * Created by Sylar on 14-6-28.
 */
public class DynamicListParser extends BaseParser<List<Dynamic>> {

    @Override
    public List<Dynamic> parseData(JSONObject jsonObject) throws JSONException {
        List<Dynamic> result = new ArrayList<Dynamic>();
        JSONObject data = jsonObject.optJSONObject("data");
        if (data != null) {
            JSONArray dynamicArray = data.optJSONArray("dynamic_list");
            for (int i = 0; i < dynamicArray.length(); i++) {
                JSONObject dynamicObject = dynamicArray.optJSONObject(i);
                Dynamic dynamic = new Dynamic();
                dynamic.setDynamicId(dynamicObject.optString("dynamic_id"));
                dynamic.setDynamicDate(dynamicObject.optString("dynamic_date"));
                dynamic.setTitle(dynamicObject.optString("title"));
                dynamic.setGoodsCount(dynamicObject.optString("goods_count"));
                dynamic.setCommentCount(dynamicObject.optString("comment_count"));

                JSONObject memberInfoObject = dynamicObject.optJSONObject("member_info");
                if (memberInfoObject != null) {
                    Member member = new Member();
                    member.memberId = memberInfoObject.optString("member_id");
                    member.memberName = memberInfoObject.optString("member_name");
                    member.iconName = memberInfoObject.optString("icon_name");
                    member.iconLocation = memberInfoObject.optString("icon_location");
                    member.iconDate = memberInfoObject.optString("icon_date");
                    member.vipLevel = memberInfoObject.optString("vip_level");
                    member.sex = memberInfoObject.optString("sex");
                    dynamic.setMemberInfo(member);
                }
                result.add(dynamic);
            }
        }
        return result;
    }
}
